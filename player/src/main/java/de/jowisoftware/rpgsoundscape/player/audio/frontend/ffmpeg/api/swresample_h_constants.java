// Generated by jextract

package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api;

import jdk.incubator.foreign.FunctionDescriptor;

import java.lang.invoke.MethodHandle;

import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_LONG;
import static jdk.incubator.foreign.CLinker.C_POINTER;

class swresample_h_constants {

    static final FunctionDescriptor swr_init$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER
    );

    static final MethodHandle swr_init$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_init",
            "(Ljdk/incubator/foreign/MemoryAddress;)I",
            swr_init$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_init$MH() {
        return swr_init$MH_;
    }

    static final FunctionDescriptor swr_alloc_set_opts$FUNC_ = FunctionDescriptor.of(C_POINTER,
            C_POINTER,
            C_LONG,
            C_INT,
            C_INT,
            C_LONG,
            C_INT,
            C_INT,
            C_INT,
            C_POINTER
    );

    static final MethodHandle swr_alloc_set_opts$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_alloc_set_opts",
            "(Ljdk/incubator/foreign/MemoryAddress;JIIJIIILjdk/incubator/foreign/MemoryAddress;)Ljdk/incubator/foreign/MemoryAddress;",
            swr_alloc_set_opts$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_alloc_set_opts$MH() {
        return swr_alloc_set_opts$MH_;
    }

    static final FunctionDescriptor swr_free$FUNC_ = FunctionDescriptor.ofVoid(
            C_POINTER
    );

    static final MethodHandle swr_free$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_free",
            "(Ljdk/incubator/foreign/MemoryAddress;)V",
            swr_free$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_free$MH() {
        return swr_free$MH_;
    }

    static final FunctionDescriptor swr_close$FUNC_ = FunctionDescriptor.ofVoid(
            C_POINTER
    );

    static final MethodHandle swr_close$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_close",
            "(Ljdk/incubator/foreign/MemoryAddress;)V",
            swr_close$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_close$MH() {
        return swr_close$MH_;
    }

    static final FunctionDescriptor swr_convert$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER,
            C_POINTER,
            C_INT,
            C_POINTER,
            C_INT
    );

    static final MethodHandle swr_convert$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_convert",
            "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;ILjdk/incubator/foreign/MemoryAddress;I)I",
            swr_convert$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_convert$MH() {
        return swr_convert$MH_;
    }

    static final FunctionDescriptor swr_get_delay$FUNC_ = FunctionDescriptor.of(C_LONG,
            C_POINTER,
            C_LONG
    );

    static final MethodHandle swr_get_delay$MH_ = RuntimeHelper.downcallHandle(
            "swresample", "swr_get_delay",
            "(Ljdk/incubator/foreign/MemoryAddress;J)J",
            swr_get_delay$FUNC_
    );

    static java.lang.invoke.MethodHandle swr_get_delay$MH() {
        return swr_get_delay$MH_;
    }

    static int AV_SAMPLE_FMT_FLTP() {
        return (int) 8L;
    }
}

