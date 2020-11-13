import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.javacpp.PointerPointer;

import java.nio.ByteBuffer;
import java.util.Optional;

import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_alloc_context3;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_close;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_find_decoder;
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
import static org.bytedeco.ffmpeg.global.avutil.AVMEDIA_TYPE_AUDIO;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_alloc;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_free;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_unref;

public class Main implements AutoCloseable {
    private final String vf_path;
    private final AVFormatContext formatContext;

    public Main(String vf_path) {
        this.vf_path = vf_path;

        formatContext = new AVFormatContext(null);
        readStreamInfo();
    }

    @Override
    public void close() {
        avformat_close_input(formatContext);
    }

    public void play() {
        // av_dump_format(formatContext, 0, vf_path, 0);

        int streamIdx = findAudioStream()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find audio stream"));

        System.out.printf("Bitrate: %d\n", formatContext.streams(streamIdx).codecpar().bit_rate());

        AVPacket packet = new AVPacket();
        AVCodecContext codec_ctx = avcodec_alloc_context3(null);
        AVFrame frame = av_frame_alloc();
        try {
            openCodec(codec_ctx, streamIdx);

            while (av_read_frame(formatContext, packet) == 0) {
                if (packet.stream_index() == streamIdx) {
                    if (avcodec_send_packet(codec_ctx, packet) != 0) {
                        throw new IllegalArgumentException("avcodec_send_packet failed");
                    }

                    av_frame_unref(frame);
                    if (avcodec_receive_frame(codec_ctx, frame) == 0) {
                        //System.out.println("nb_samples" + frame.nb_samples());
                        //System.out.println("best_effort_timestamp" + frame.best_effort_timestamp());

                        System.out.println(frame.nb_samples() + "@" + frame.sample_rate());

                        ByteBuffer bb = frame.data().asByteBuffer();
                        //System.out.print("frame: ");

                        while (bb.hasRemaining()) {
                            //System.out.printf("%02X ", bb.get() & 0xFF);
                            bb.get();
                        }
                        //System.out.println();
                    }
                }
                av_packet_unref(packet);
            }

            /*
            // Determine required buffer size and allocate buffer
            int numBytes = av_image_get_buffer_size(AV_PIX_FMT_RGB24, codec_ctx.width(),
                    codec_ctx.height(), 1);
            BytePointer buffer = new BytePointer(av_malloc(numBytes));

            SwsContext sws_ctx = sws_getContext(
                    codec_ctx.width(),
                    codec_ctx.height(),
                    codec_ctx.pix_fmt(),
                    codec_ctx.width(),
                    codec_ctx.height(),
                    AV_PIX_FMT_RGB24,
                    SWS_BILINEAR,
                    null,
                    null,
                    (DoublePointer) null
            );

            av_image_fill_arrays(pFrameRGB.data(), pFrameRGB.linesize(),
                    buffer, AV_PIX_FMT_RGB24, codec_ctx.width(), codec_ctx.height(), 1);

            int i = 0, ret1, ret2 = -1;
            while (av_read_frame(formatContext, packet) >= 0) {
                if (packet.stream_index() == streamIdx) {
                    ret1 = avcodec_send_packet(codec_ctx, packet);
                    ret2 = avcodec_receive_frame(codec_ctx, frame);
                    System.out.printf("ret1 %d ret2 %d\n", ret1, ret2);
                    // avcodec_decode_video2(codec_ctx, frame, fi, pkt);
                }
                // if not check ret2, error occur [swscaler @ 0x1cb3c40] bad src image pointers
                // ret2 same as fi
                // if (fi && ++i <= 5) {
                if (ret2 >= 0 && ++i <= 5) {
                    sws_scale(
                            sws_ctx,
                            frame.data(),
                            frame.linesize(),
                            0,
                            codec_ctx.height(),
                            pFrameRGB.data(),
                            pFrameRGB.linesize()
                    );

                    // save_frame(pFrameRGB, codec_ctx.width(), codec_ctx.height(), i);
                    // save_frame(frame, codec_ctx.width(), codec_ctx.height(), i);
                }
                av_packet_unref(packet);
                if (i >= 5) {
                    break;
                }
            }
         */
        } finally {
            av_frame_free(frame);
            avcodec_close(codec_ctx);
            avcodec_free_context(codec_ctx);
        }
    }

    private void readStreamInfo() {
        if (avformat_open_input(formatContext, vf_path, null, null) < 0) {
            throw new IllegalStateException("Open audio file '%s' failed".formatted(vf_path));
        }

        if (avformat_find_stream_info(formatContext, (PointerPointer<?>) null) < 0) {
            throw new IllegalStateException("Cannot find stream info in file '%s'".formatted(vf_path));
        }
    }

    private void openCodec(AVCodecContext codec_ctx, int streamIdx) {
        avcodec_parameters_to_context(codec_ctx, formatContext.streams(streamIdx).codecpar());

        AVCodec codec = avcodec_find_decoder(codec_ctx.codec_id());
        if (codec == null) {
            throw new IllegalStateException("Unsupported codec for audio file");
        }

        int ret = avcodec_open2(codec_ctx, codec, (PointerPointer<?>) null);
        if (ret < 0) {
            throw new IllegalStateException("Can not open codec");
        }
    }

    private Optional<Integer> findAudioStream() {
        int ret = av_find_best_stream(formatContext, AVMEDIA_TYPE_AUDIO, -1, -1, (PointerPointer<?>) null, 0);
        return ret < 0 ? Optional.empty() : Optional.of(ret);
    }

    public static void main(String[] args) throws Exception {
        try (var m = new Main(".cache/youtubedl-https-www-youtube-com-watch-v-N0yn4kVHB14.cache")) {
            m.play();
        }
    }
}
