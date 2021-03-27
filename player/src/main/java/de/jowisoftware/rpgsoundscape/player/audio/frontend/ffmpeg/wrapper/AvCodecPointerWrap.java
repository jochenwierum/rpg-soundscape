package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemorySegment;

public class AvCodecPointerWrap extends AbstractMemoryWrapper implements AutoCloseable {
    public AvCodecPointerWrap() {
        super("codecPointer", CLinker.C_POINTER, MemorySegment.allocateNative(CLinker.C_POINTER));
    }

    public AvCodecWrap followPointer() {
        var targetAddress = (MemoryAddress) MemoryHandles.asAddressVarHandle(CLinker.C_POINTER.varHandle(long.class))
                .get(segment);
        return new AvCodecWrap(targetAddress);
    }

    @Override
    public void close() throws Exception {
        segment.close();
    }
}
