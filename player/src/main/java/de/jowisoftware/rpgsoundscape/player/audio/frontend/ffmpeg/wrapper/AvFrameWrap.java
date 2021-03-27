package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.FfmpegApiException;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avcodec_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h.AVFrame;
import jdk.incubator.foreign.MemoryAddress;

import static jdk.incubator.foreign.MemoryAddress.NULL;

public class AvFrameWrap extends AbstractMemoryWrapper implements AutoCloseable {
    public AvFrameWrap() {
        super("avFrame", AVFrame.layout(), allocate());
    }

    private static MemoryAddress allocate() {
        MemoryAddress avFrameAddress = avformat_h.av_frame_alloc();
        if (avFrameAddress.address().equals(NULL)) {
            throw new FfmpegApiException(0, "av_frame_alloc");
        }

        return avFrameAddress;
    }

    public void format(int sampleFmt) {
        avcodec_h.AVFrame.format$set(segment, sampleFmt);
    }

    public void channelLayout(long channelLayout) {
        avcodec_h.AVFrame.channel_layout$set(segment, channelLayout);
    }

    public void channels(int channels) {
        avcodec_h.AVFrame.channels$set(segment, channels);
    }

    public void sampleRate(int sampleRate) {
        avcodec_h.AVFrame.sample_rate$set(segment, sampleRate);
    }

    public long sampleRate() {
        return avcodec_h.AVFrame.sample_rate$get(segment);
    }

    public int nbSamples() {
        return avcodec_h.AVFrame.nb_samples$get(segment);
    }

    public MemoryAddress data() {
        return avcodec_h.AVFrame.extended_data$get(segment);
    }

    @Override
    public void close() throws Exception {
        withSegmentPointer(avcodec_h::av_frame_free);
    }
}
