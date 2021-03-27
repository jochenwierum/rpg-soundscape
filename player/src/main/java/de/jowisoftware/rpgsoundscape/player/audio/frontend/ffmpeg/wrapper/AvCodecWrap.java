package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVCodec;
import jdk.incubator.foreign.MemoryAddress;

public class AvCodecWrap extends AbstractMemoryWrapper {
    public AvCodecWrap(MemoryAddress address) {
        super("avCodec", AVCodec.layout(), address);
    }
}
