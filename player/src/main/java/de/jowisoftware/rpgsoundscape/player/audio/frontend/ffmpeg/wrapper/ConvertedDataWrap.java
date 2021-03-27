package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avcodec_h;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemorySegment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static jdk.incubator.foreign.MemoryAddress.NULL;

public class ConvertedDataWrap extends AbstractMemoryWrapper implements AutoCloseable {
    private final int channels;
    private int size = 0;
    private boolean free = true;

    public ConvertedDataWrap(int channels) {
        super("converted_data", CLinker.C_POINTER, MemorySegment.allocateNative(CLinker.C_POINTER));
        this.channels = channels;
    }

    public void free() {
        if (!free) {
            free = true;
            avcodec_h.av_freep(segment);
        }
    }

    public void grow(int newSize) {
        if (size < newSize) {
            if (size > 0) {
                free();
            }

            avcodec_h.av_samples_alloc(segment, NULL, channels,
                    newSize, avcodec_h.AV_SAMPLE_FMT_S16(), 0);
            size = newSize;
        }
    }

    public ByteBuffer asByteBuffer(int skip, int size) {
        return ((MemoryAddress) MemoryHandles.asAddressVarHandle(CLinker.C_POINTER.varHandle(long.class)).get(segment))
                .addOffset(skip)
                .asSegmentRestricted(size)
                .asByteBuffer()
                .order(ByteOrder.nativeOrder());
    }


    @Override
    public MemoryAddress address() {
        free = false;
        return super.address();
    }

    @Override
    public void close() throws Exception {
        free();
    }
}
