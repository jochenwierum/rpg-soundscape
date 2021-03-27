package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.FfmpegApiException;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVFormatContext;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemorySegment;

import java.lang.invoke.VarHandle;

import static jdk.incubator.foreign.MemoryAddress.NULL;


public class AvFormatContextWrap extends AbstractMemoryWrapper implements AutoCloseable {
    private static final MemoryLayout LAYOUT = CLinker.C_POINTER;
    private static final VarHandle VH = MemoryHandles.asAddressVarHandle(LAYOUT.varHandle(long.class));

    private final MemorySegment segmentPointer;
    private final MemorySegment segment;

    public AvFormatContextWrap(String file) {
        super("avFormatContext", AVFormatContext.layout());

        segmentPointer = MemorySegment.allocateNative(LAYOUT);

        try (MemorySegment fileP = CLinker.toCString(file)) {
            int res = avformat_h.avformat_open_input(segmentPointer, fileP, NULL, NULL);
            if (res != 0) {
                segmentPointer.close();
                throw new FfmpegApiException(res, "avformat_open_input: " + file);
            }
        }

        segment = ((MemoryAddress) VH.get(segmentPointer))
                .asSegmentRestricted(AVFormatContext.layout().byteSize());
    }

    @Override
    public MemoryAddress address() {
        return segment.address();
    }

    public AvStreamWrap streams(int streamId) {
        return new AvStreamWrap(AVFormatContext.streams$get(this.segment, streamId));
    }

    @Override
    public void close() throws Exception {
        avformat_h.avformat_close_input(segmentPointer);
        segmentPointer.close();
    }
}
