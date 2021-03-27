// Generated by jextract

package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api;

import jdk.incubator.foreign.Addressable;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;

public final class avcodec_h {

    public static MemoryAddress av_packet_alloc() {
        var mh$ = avcodec_h_constants.av_packet_alloc$MH();
        try {
            return (MemoryAddress) mh$.invokeExact();
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static void av_packet_free(Addressable pkt) {
        var mh$ = avcodec_h_constants.av_packet_free$MH();
        try {
            mh$.invokeExact(pkt.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static MemoryAddress avcodec_alloc_context3(Addressable codec) {
        var mh$ = avcodec_h_constants.avcodec_alloc_context3$MH();
        try {
            return (MemoryAddress) mh$.invokeExact(codec.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int avcodec_parameters_to_context(Addressable codec, Addressable par) {
        var mh$ = avcodec_h_constants.avcodec_parameters_to_context$MH();
        try {
            return (int) mh$.invokeExact(codec.address(), par.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int avcodec_open2(Addressable avctx, Addressable codec, Addressable options) {
        var mh$ = avcodec_h_constants.avcodec_open2$MH();
        try {
            return (int) mh$.invokeExact(avctx.address(), codec.address(), options.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int avcodec_send_packet(Addressable avctx, Addressable avpkt) {
        var mh$ = avcodec_h_constants.avcodec_send_packet$MH();
        try {
            return (int) mh$.invokeExact(avctx.address(), avpkt.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int avcodec_receive_frame(Addressable avctx, Addressable frame) {
        var mh$ = avcodec_h_constants.avcodec_receive_frame$MH();
        try {
            return (int) mh$.invokeExact(avctx.address(), frame.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int AV_SAMPLE_FMT_S16() {
        return avcodec_h_constants.AV_SAMPLE_FMT_S16();
    }

    public static int av_samples_alloc(Addressable audio_data, Addressable linesize, int nb_channels, int nb_samples, int sample_fmt, int align) {
        var mh$ = avcodec_h_constants.av_samples_alloc$MH();
        try {
            return (int) mh$.invokeExact(audio_data.address(), linesize.address(), nb_channels, nb_samples, sample_fmt, align);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static void av_frame_free(Addressable frame) {
        var mh$ = avcodec_h_constants.av_frame_free$MH();
        try {
            mh$.invokeExact(frame.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static void av_freep(Addressable ptr) {
        var mh$ = avcodec_h_constants.av_freep$MH();
        try {
            mh$.invokeExact(ptr.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static long av_rescale_rnd(long a, long b, long c, int rnd) {
        var mh$ = avcodec_h_constants.av_rescale_rnd$MH();
        try {
            return (long) mh$.invokeExact(a, b, c, rnd);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static class AVFrame {

        public static MemoryAddress extended_data$get(MemorySegment seg) {
            return (jdk.incubator.foreign.MemoryAddress) avcodec_h_constants.AVFrame$extended_data$VH().get(seg);
        }

        public static int nb_samples$get(MemorySegment seg) {
            return (int) avcodec_h_constants.AVFrame$nb_samples$VH().get(seg);
        }

        public static void format$set(MemorySegment seg, int x) {
            avcodec_h_constants.AVFrame$format$VH().set(seg, x);
        }

        public static int sample_rate$get(MemorySegment seg) {
            return (int) avcodec_h_constants.AVFrame$sample_rate$VH().get(seg);
        }

        public static void sample_rate$set(MemorySegment seg, int x) {
            avcodec_h_constants.AVFrame$sample_rate$VH().set(seg, x);
        }

        public static void channel_layout$set(MemorySegment seg, long x) {
            avcodec_h_constants.AVFrame$channel_layout$VH().set(seg, x);
        }

        public static void channels$set(MemorySegment seg, int x) {
            avcodec_h_constants.AVFrame$channels$VH().set(seg, x);
        }

    }

}

