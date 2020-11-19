package de.jowisoftware.rpgsoundscape.player.audio.ffmpeg;

import de.jowisoftware.rpgsoundscape.player.threading.concurrency.InterruptibleTask;
import de.jowisoftware.rpgsoundscape.player.threading.concurrency.Pause;
import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.swresample.SwrContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static org.bytedeco.ffmpeg.global.avcodec.av_init_packet;
import static org.bytedeco.ffmpeg.global.avcodec.av_packet_free;
import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_alloc_context3;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_close;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_free_context;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_open2;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_parameters_to_context;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_receive_frame;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_send_packet;
import static org.bytedeco.ffmpeg.global.avformat.av_find_best_stream;
import static org.bytedeco.ffmpeg.global.avformat.av_read_frame;
import static org.bytedeco.ffmpeg.global.avformat.avformat_close_input;
import static org.bytedeco.ffmpeg.global.avformat.avformat_find_stream_info;
import static org.bytedeco.ffmpeg.global.avformat.avformat_open_input;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EOF;
import static org.bytedeco.ffmpeg.global.avutil.AVMEDIA_TYPE_AUDIO;
import static org.bytedeco.ffmpeg.global.avutil.AV_CH_LAYOUT_STEREO;
import static org.bytedeco.ffmpeg.global.avutil.AV_ROUND_UP;
import static org.bytedeco.ffmpeg.global.avutil.AV_SAMPLE_FMT_S16;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_alloc;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_free;
import static org.bytedeco.ffmpeg.global.avutil.av_freep;
import static org.bytedeco.ffmpeg.global.avutil.av_get_default_channel_layout;
import static org.bytedeco.ffmpeg.global.avutil.av_rescale_rnd;
import static org.bytedeco.ffmpeg.global.avutil.av_samples_alloc;
import static org.bytedeco.ffmpeg.global.swresample.swr_alloc_set_opts;
import static org.bytedeco.ffmpeg.global.swresample.swr_close;
import static org.bytedeco.ffmpeg.global.swresample.swr_convert;
import static org.bytedeco.ffmpeg.global.swresample.swr_free;
import static org.bytedeco.ffmpeg.global.swresample.swr_get_delay;
import static org.bytedeco.ffmpeg.global.swresample.swr_init;
import static org.bytedeco.ffmpeg.presets.avutil.AVERROR_EAGAIN;

public class FfmpegClip implements InterruptibleTask {
    static final AudioFormat TARGET_FORMAT = new AudioFormat(
            Encoding.PCM_SIGNED,
            44100,
            16,
            2,
            4,
            44100,
            false);

    private static final int BYTES_PER_SAMPLE = TARGET_FORMAT.getChannels() * TARGET_FORMAT.getSampleSizeInBits() / 8;

    private final String file;
    private long skipBytes;
    private long playBytes;

    private final Pause pause = new Pause(false);

    private SourceDataLine line;

    public FfmpegClip(String file, long skipBytes, long playBytes) {
        this.file = file;
        this.skipBytes = skipBytes;
        this.playBytes = playBytes;
    }

    @Override
    public void run(boolean resume) throws RuntimeException {
        AVFormatContext formatContext = new AVFormatContext(null);
        int res;
        if ((res = avformat_open_input(formatContext, file, null, null)) != 0) {
            throw new FfmpegApiException(res, "avformat_open_input: " + file);
        }
        try {
            if ((res = avformat_find_stream_info(formatContext, (PointerPointer<?>) null)) < 0) {
                throw new FfmpegApiException(res, "avformat_find_stream_info");
            }

            AVCodec codec = new AVCodec();
            int streamId = findAudioStream(formatContext, codec);
            AVCodecContext fileCodecContext = openCodec(formatContext, codec, streamId);

            if (fileCodecContext.channel_layout() == 0) {
                fileCodecContext.channel_layout(av_get_default_channel_layout(fileCodecContext.channels()));
            }

            try {
                SwrContext swrContext = createResamplingContext(fileCodecContext);
                try {
                    AVFrame audioFrameDecoded = av_frame_alloc();
                    if (audioFrameDecoded.isNull()) {
                        throw new FfmpegApiException(0, "Could not allocate audio frame");
                    }
                    try {
                        audioFrameDecoded.format(fileCodecContext.sample_fmt());
                        audioFrameDecoded.channel_layout(fileCodecContext.channel_layout());
                        audioFrameDecoded.channels(fileCodecContext.channels());
                        audioFrameDecoded.sample_rate(fileCodecContext.sample_rate());

                        AVPacket packet = new AVPacket();
                        packet.data(null);
                        packet.size(0);
                        av_init_packet(packet);

                        try {
                            line = (SourceDataLine) AudioSystem.getLine(new Info(SourceDataLine.class, TARGET_FORMAT));
                            line.open(TARGET_FORMAT);

                            if (resume) {
                                startOrResume();
                            }

                            PointerPointer<BytePointer> convertedData = new PointerPointer<>(2L);
                            byte[] bufferBytes = new byte[0];
                            int convertedDataSize = -1;
                            int outSamples, outBytes, written;
                            int read = 0;

                            while (read >= 0 && line.isOpen()) {
                                reinitPacket(packet);

                                read = av_read_frame(formatContext, packet);
                                if (packet.stream_index() != streamId) {
                                    continue;
                                }

                                if (read == AVERROR_EOF()) {
                                    sendFlush(fileCodecContext, packet);
                                } else {
                                    sendPacket(fileCodecContext, packet);
                                }

                                while (receiveFrame(fileCodecContext, audioFrameDecoded)) {
                                    outSamples = (int) av_rescale_rnd(
                                            swr_get_delay(swrContext, (int) TARGET_FORMAT.getSampleRate()) +
                                                    audioFrameDecoded.nb_samples(),
                                            audioFrameDecoded.sample_rate(),
                                            (int) TARGET_FORMAT.getSampleRate(), AV_ROUND_UP);

                                    convertedDataSize = reallocConvertedData(convertedData, convertedDataSize, outSamples);

                                    outSamples = swr_convert(swrContext, convertedData, outSamples,
                                            audioFrameDecoded.data(), audioFrameDecoded.nb_samples());

                                    outBytes = outSamples * BYTES_PER_SAMPLE;

                                    if (skipBytes < outBytes) {
                                        if (playBytes > 0) {
                                            if ((outBytes - skipBytes) > playBytes) {
                                                outBytes = (int) (playBytes - skipBytes);
                                                playBytes = 0;
                                            } else {
                                                playBytes -= outBytes + skipBytes;
                                            }
                                        }

                                        if (bufferBytes.length < outBytes) {
                                            bufferBytes = new byte[outBytes];
                                        }

                                        convertedData.get(BytePointer.class, 0)
                                                .limit(outBytes)
                                                .asByteBuffer()
                                                .get(bufferBytes, 0, outBytes);

                                        written = (int) skipBytes + line.write(bufferBytes, (int) skipBytes, outBytes - (int) skipBytes);
                                        while (written < outBytes && line.isOpen()) {
                                            pause.awaitToPass(); // prevent 100% cpu usage
                                            written += line.write(bufferBytes, written, outBytes - written);
                                        }

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

                            av_freep(convertedData);
                            line.drain();
                        } catch (LineUnavailableException e) {
                            throw new RuntimeException(e);
                        } finally {
                            line.close();
                            av_packet_unref(packet);
                            av_packet_free(packet);
                        }
                    } finally {
                        av_frame_free(audioFrameDecoded);
                    }
                } finally {
                    swr_close(swrContext);
                    swr_free(swrContext);
                }
            } finally {
                avcodec_close(fileCodecContext);
                avcodec_free_context(fileCodecContext);
            }
        } finally {
            avformat_close_input(formatContext);
        }
    }

    private int reallocConvertedData(PointerPointer<BytePointer> convertedData, int convertedDataSize, int outSamples) {
        if (convertedDataSize < outSamples) {
            if (convertedDataSize >= 0) {
                av_freep(convertedData);
            }
            av_samples_alloc(convertedData, null, TARGET_FORMAT.getChannels(),
                    outSamples, AV_SAMPLE_FMT_S16, 0);
            convertedDataSize = outSamples;
        }
        return convertedDataSize;
    }

    private SwrContext createResamplingContext(AVCodecContext fileCodecContext) {
        SwrContext swrContext = swr_alloc_set_opts(null,
                AV_CH_LAYOUT_STEREO,
                AV_SAMPLE_FMT_S16,
                (int) TARGET_FORMAT.getSampleRate(),
                fileCodecContext.channel_layout() != 0 ? fileCodecContext.channel_layout() : AV_CH_LAYOUT_STEREO,
                fileCodecContext.sample_fmt(),
                fileCodecContext.sample_rate(),
                0,
                null);

        int res = swr_init(swrContext);
        if (res < 0) {
            throw new FfmpegApiException(res, "swr_init");
        }
        return swrContext;
    }

    private AVCodecContext openCodec(AVFormatContext formatContext, AVCodec codec, int streamId) {
        AVCodecContext fileCodecContext = avcodec_alloc_context3(codec);
        AVStream stream = formatContext.streams(streamId);
        avcodec_parameters_to_context(fileCodecContext, stream.codecpar());

        int res = avcodec_open2(fileCodecContext, codec, (PointerPointer<?>) null);
        if (res < 0) {
            throw new FfmpegApiException(res, "avcodec_open2");
        }

        fileCodecContext.pkt_timebase(stream.time_base());
        return fileCodecContext;
    }

    private int findAudioStream(AVFormatContext formatContext, AVCodec codec) {
        int streamId = av_find_best_stream(formatContext, AVMEDIA_TYPE_AUDIO, -1, -1, codec, 0);
        if (streamId < 0) {
            throw new FfmpegApiException(streamId, "av_find_best_stream");
        }
        return streamId;
    }

    private void sendPacket(AVCodecContext avctx, AVPacket avpkt) {
        int ret = avcodec_send_packet(avctx, avpkt);
        if (ret != 0) {
            throw new FfmpegApiException(ret, "avcodec_send_packet");
        }
    }

    private boolean receiveFrame(AVCodecContext avctx, AVFrame frame) {
        int ret = avcodec_receive_frame(avctx, frame);

        if (ret == 0) {
            return true;
        } else if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF()) {
            return false;
        } else {
            throw new FfmpegApiException(ret, "avcodec_receive_frame");
        }
    }

    private void sendFlush(AVCodecContext avctx, AVPacket avpkt) {
        avpkt.data(null);
        avpkt.size(0);
        avcodec_send_packet(avctx, avpkt);
    }

    private void reinitPacket(AVPacket inPacket) {
        av_packet_unref(inPacket);
        inPacket.data(null);
        inPacket.size(0);
        av_init_packet(inPacket);
    }

    @Override
    public void pause() {
        pause.pause();
        if (line != null) {
            line.stop();
        }
    }

    @Override
    public void startOrResume() {
        if (line != null) {
            line.start();
        }
        pause.resume();
    }

    @Override
    public void abort() {
        if (line != null) {
            line.stop();
            line.close();
        }
        pause.resume();
    }
}
