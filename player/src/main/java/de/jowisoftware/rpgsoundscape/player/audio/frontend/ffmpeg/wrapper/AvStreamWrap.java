package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVStream;
import jdk.incubator.foreign.Addressable;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;

public class AvStreamWrap extends AbstractMemoryWrapper {
    public AvStreamWrap(MemoryAddress address) {
        super("avStream", AVStream.layout(), address);
    }

    public Addressable codecpar() {
        return AVStream.codecpar$get(segment);
    }

    public MemorySegment timeBase() {
        return AVStream.timeBase$slice(segment);
    }
}
