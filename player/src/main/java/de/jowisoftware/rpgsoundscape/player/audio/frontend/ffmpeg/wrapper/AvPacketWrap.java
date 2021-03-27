package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avcodec_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVPacket;
import jdk.incubator.foreign.MemoryAddress;

public class AvPacketWrap extends AbstractMemoryWrapper implements AutoCloseable {
    public AvPacketWrap() {
        super("avPacket", AVPacket.layout(), avcodec_h.av_packet_alloc());
    }

    public int streamIndex() {
        return AVPacket.stream_index$get(segment);
    }

    public void unref() {
        avformat_h.av_packet_unref(segment);
    }

    public void reset() {
        AVPacket.data$set(segment, MemoryAddress.NULL);
        AVPacket.size$set(segment, 0);
    }

    @Override
    public void close() throws Exception {
        avcodec_h.av_packet_free(segment);
    }
}
