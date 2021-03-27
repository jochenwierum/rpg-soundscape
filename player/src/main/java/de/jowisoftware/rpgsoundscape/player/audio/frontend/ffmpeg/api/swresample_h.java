package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api;

import jdk.incubator.foreign.Addressable;
import jdk.incubator.foreign.MemoryAddress;

public final class swresample_h {
    private swresample_h() {
    }

    public static int AV_SAMPLE_FMT_FLTP() {
        return swresample_h_constants.AV_SAMPLE_FMT_FLTP();
    }

    public static int swr_init(Addressable s) {
        var mh$ = swresample_h_constants.swr_init$MH();
        try {
            return (int) mh$.invokeExact(s.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static MemoryAddress swr_alloc_set_opts(Addressable s, long out_ch_layout, int out_sample_fmt, int out_sample_rate, long in_ch_layout, int in_sample_fmt, int in_sample_rate, int log_offset, Addressable log_ctx) {
        var mh$ = swresample_h_constants.swr_alloc_set_opts$MH();
        try {
            return (MemoryAddress) mh$.invokeExact(s.address(), out_ch_layout, out_sample_fmt, out_sample_rate, in_ch_layout, in_sample_fmt, in_sample_rate, log_offset, log_ctx.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static void swr_free(Addressable s) {
        var mh$ = swresample_h_constants.swr_free$MH();
        try {
            mh$.invokeExact(s.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static void swr_close(Addressable s) {
        var mh$ = swresample_h_constants.swr_close$MH();
        try {
            mh$.invokeExact(s.address());
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static int swr_convert(Addressable s, Addressable out, int out_count, Addressable in, int in_count) {
        var mh$ = swresample_h_constants.swr_convert$MH();
        try {
            return (int) mh$.invokeExact(s.address(), out.address(), out_count, in.address(), in_count);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }

    public static long swr_get_delay(Addressable s, long base) {
        var mh$ = swresample_h_constants.swr_get_delay$MH();
        try {
            return (long) mh$.invokeExact(s.address(), base);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}


