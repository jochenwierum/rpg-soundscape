package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.FfmpegApiException;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avcodec_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVCodecContext;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;

import static jdk.incubator.foreign.MemoryAddress.NULL;

public class AvCodecContextWrap extends AbstractMemoryWrapper implements AutoCloseable {
    public AvCodecContextWrap(AvCodecWrap codec) {
        super("avCodecContext", AVCodecContext.layout(), allocate(codec));
    }

    private static MemoryAddress allocate(AvCodecWrap codec) {
        MemoryAddress segmentPointer = avcodec_h.avcodec_alloc_context3(codec);
        if (segmentPointer.equals(NULL)) {
            throw new FfmpegApiException(0, "avcodec_alloc_context3");
        }

        return segmentPointer;
    }

    public void pktTimebase(MemorySegment timeBase) {
        AVCodecContext.pkt_timebase$slice(segment).copyFrom(timeBase);
    }

    public int channels() {
        return AVCodecContext.channels$get(segment);
    }

    public long channelLayout() {
        return AVCodecContext.channel_layout$get(segment);
    }

    public void channelLayout(long channelLayout) {
        AVCodecContext.channel_layout$set(segment, channelLayout);
    }

    public int sampleFmt() {
        return AVCodecContext.sample_fmt$get(segment);
    }

    public int sampleRate() {
        return AVCodecContext.sample_rate$get(segment);
    }

    @Override
    public void close() throws Exception {
        withSegmentPointer(avformat_h::avcodec_free_context);
    }
}
