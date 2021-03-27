package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg;

import de.jowisoftware.rpgsoundscape.model.Play;
import de.jowisoftware.rpgsoundscape.player.audio.backend.AudioStream;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avcodec_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.codec_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.swresample_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvCodecContextWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvCodecPointerWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvCodecWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvFormatContextWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvFrameWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvPacketWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.AvStreamWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.ConvertedDataWrap;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper.SwrContextWrap;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import java.util.concurrent.CountDownLatch;

import static de.jowisoftware.rpgsoundscape.player.audio.JavaAudioUtils.bytesPerSample;
import static jdk.incubator.foreign.MemoryAddress.NULL;

public class FfmpegClip implements InterruptibleTask {
    private final AudioStream audioStream;
    private final AudioFormat targetFormat;
    private final int bytesPerSample;

    private final String file;
    private final Play play;
    private long skipBytes;
    private long playBytes;

    private final CountDownLatch starting = new CountDownLatch(1);

    public FfmpegClip(Play play, String file, long skipBytes, long playBytes, AudioStream audioStream, AudioFormat targetFormat) {
        this.targetFormat = targetFormat;
        this.bytesPerSample = bytesPerSample(targetFormat);

        this.file = file;
        this.skipBytes = skipBytes;
        this.playBytes = playBytes;
        this.play = play;
        this.audioStream = audioStream;
    }

    @Override
    public void run(boolean resume) throws RuntimeException {
        int res;

        try (audioStream) {
            if (resume) {
                audioStream.resume();
            } else {
                audioStream.pause();
            }
            starting.countDown();

            try (AvPacketWrap packet = new AvPacketWrap();
                 AvFormatContextWrap formatContext = new AvFormatContextWrap(file);
                 AvCodecPointerWrap codecPointer = new AvCodecPointerWrap()) {

                if ((res = avformat_h.avformat_find_stream_info(formatContext, NULL)) < 0) {
                    throw new FfmpegApiException(res, "avformat_find_stream_info");
                }
                int streamId = findAudioStream(formatContext, codecPointer);

                try (AvCodecContextWrap codecContext = openCodec(formatContext, codecPointer.followPointer(), streamId)) {
                    if (codecContext.channelLayout() == 0) {
                        codecContext.channelLayout(avformat_h.av_get_default_channel_layout(codecContext.channels()));
                    }

                    try (SwrContextWrap swrContext = createResamplingContext(codecContext);
                         AvFrameWrap audioFrameDecoded = new AvFrameWrap();
                         ConvertedDataWrap convertedData = new ConvertedDataWrap(targetFormat.getChannels())) {
                        audioFrameDecoded.format(codecContext.sampleFmt());
                        audioFrameDecoded.channelLayout(codecContext.channelLayout());
                        audioFrameDecoded.channels(codecContext.channels());
                        audioFrameDecoded.sampleRate(codecContext.sampleRate());

                        audioStream.applyAmplification(play);

                        int outSamples;
                        int outBytes;
                        int read = 0;

                        while (read >= 0 && audioStream.isOpen()) {
                            read = avformat_h.av_read_frame(formatContext, packet);
                            if (packet.streamIndex() != streamId) {
                                packet.unref();
                                continue;
                            }

                            if (read == avformat_h.AVERROR_EOF) {
                                sendFlush(codecContext, packet);
                            } else {
                                sendPacket(codecContext, packet);
                            }
                            packet.unref();

                            while (audioStream.isOpen() && receiveFrame(codecContext, audioFrameDecoded)) {
                                outSamples = (int) avcodec_h.av_rescale_rnd(
                                        swresample_h.swr_get_delay(swrContext,
                                                (int) targetFormat.getSampleRate()) +
                                                audioFrameDecoded.nbSamples(),
                                        audioFrameDecoded.sampleRate(),
                                        (int) targetFormat.getSampleRate(), codec_h.AV_ROUND_UP());

                                convertedData.grow(outSamples);

                                outSamples = swresample_h.swr_convert(swrContext,
                                        convertedData, outSamples,
                                        audioFrameDecoded.data(), audioFrameDecoded.nbSamples());

                                outBytes = outSamples * bytesPerSample;

                                if (skipBytes < outBytes) {
                                    if (playBytes > 0) {
                                        if ((outBytes - skipBytes) > playBytes) {
                                            outBytes = (int) (playBytes - skipBytes);
                                            playBytes = 0;
                                        } else {
                                            playBytes -= outBytes + skipBytes;
                                            outBytes -= skipBytes;
                                        }
                                    }

                                    audioStream.write(convertedData.asByteBuffer((int) skipBytes, outBytes));

                                    skipBytes = 0;
                                    if (playBytes == 0) {
                                        read = -1; // EOF
                                        break;
                                    }
                                } else {
                                    skipBytes -= outBytes;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                starting.countDown();
                throw new RuntimeException(e);
            }
        }
    }

    private SwrContextWrap createResamplingContext(AvCodecContextWrap codecContext) {
        SwrContextWrap swrContext = new SwrContextWrap(
                encodingToFormat(targetFormat.getEncoding()),
                (int) targetFormat.getSampleRate(),
                codecContext.channelLayout() != 0 ? codecContext.channelLayout() : avformat_h.AV_CH_LAYOUT_STEREO,
                codecContext.sampleFmt(),
                codecContext.sampleRate());

        int res = swresample_h.swr_init(swrContext);
        if (res < 0) {
            throw new FfmpegApiException(res, "swr_init");
        }
        return swrContext;
    }

    private int encodingToFormat(Encoding encoding) {
        if (encoding == Encoding.PCM_SIGNED) {
            return avformat_h.AV_SAMPLE_FMT_S16();
        } else if (encoding == Encoding.PCM_FLOAT) {
            return swresample_h.AV_SAMPLE_FMT_FLTP();
        } else {
            throw new IllegalArgumentException("Unsupported encoding " + encoding);
        }
    }

    private AvCodecContextWrap openCodec(AvFormatContextWrap formatContext, AvCodecWrap codec, int streamId) {
        AvCodecContextWrap codecContext = new AvCodecContextWrap(codec);
        AvStreamWrap avStreamWrap = formatContext.streams(streamId);

        int res = avcodec_h.avcodec_parameters_to_context(codecContext, avStreamWrap.codecpar());
        if (res < 0) {
            throw new FfmpegApiException(res, "avcodec_parameters_to_context");
        }

        res = avcodec_h.avcodec_open2(codecContext, codec, NULL);
        if (res < 0) {
            throw new FfmpegApiException(res, "avcodec_open2");
        }

        codecContext.pktTimebase(avStreamWrap.timeBase());
        return codecContext;
    }

    private int findAudioStream(AvFormatContextWrap formatContext, AvCodecPointerWrap codecPointer) {
        int streamId;
        streamId = avformat_h.av_find_best_stream(formatContext,
                avformat_h.AVMEDIA_TYPE_AUDIO(), -1, -1, codecPointer, 0);
        if (streamId < 0) {
            throw new FfmpegApiException(streamId, "av_find_best_stream");
        }
        return streamId;
    }

    private void sendPacket(AvCodecContextWrap avctx, AvPacketWrap avpkt) {
        int ret = avcodec_h.avcodec_send_packet(avctx, avpkt);
        if (ret != 0) {
            throw new FfmpegApiException(ret, "avcodec_send_packet");
        }
    }

    private boolean receiveFrame(AvCodecContextWrap avctx, AvFrameWrap frame) {
        int ret = avcodec_h.avcodec_receive_frame(avctx, frame);

        if (ret == 0) {
            return true;
        } else if (ret == avformat_h.EAGAIN || ret == avformat_h.AVERROR_EOF) {
            return false;
        } else {
            throw new FfmpegApiException(ret, "avcodec_receive_frame");
        }
    }

    private void sendFlush(AvCodecContextWrap avctx, AvPacketWrap avpkt) {
        avpkt.reset();
        avcodec_h.avcodec_send_packet(avctx, avpkt);
    }

    @Override
    public void pause() {
        finishStarting();
        audioStream.pause();
    }

    @Override
    public void startOrResume() {
        finishStarting();
        audioStream.resume();
    }

    @Override
    public void abort() {
        finishStarting();
        audioStream.close(false);
    }

    private void finishStarting() {
        try {
            starting.await();
        } catch (InterruptedException ignored) {
        }
    }
}
