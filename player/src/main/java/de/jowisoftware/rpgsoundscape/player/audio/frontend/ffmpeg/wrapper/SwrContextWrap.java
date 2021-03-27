package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.FfmpegApiException;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.avformat_h;
import de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api.swresample_h;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;

import static jdk.incubator.foreign.MemoryAddress.NULL;

public class SwrContextWrap extends AbstractMemoryWrapper implements AutoCloseable {
    public SwrContextWrap(int outSampleFormat, int outSampleRate, long inChLayout, int inSampleFmt, int inSampleRate) {
        super("SwrContext", CLinker.C_POINTER, alloc(outSampleFormat, outSampleRate, inChLayout, inSampleFmt, inSampleRate));
    }

    private static MemoryAddress alloc(int outSampleFormat, int outSampleRate, long inChLayout, int inSampleFmt, int inSampleRate) {
        MemoryAddress allocated = swresample_h.swr_alloc_set_opts(NULL,
                avformat_h.AV_CH_LAYOUT_STEREO,
                outSampleFormat,
                outSampleRate,
                inChLayout,
                inSampleFmt,
                inSampleRate,
                0,
                NULL);

        if (allocated.address().equals(NULL)) {
            throw new FfmpegApiException(0, "swr_alloc_set_opts");
        }

        return allocated;
    }

    @Override
    public void close() throws Exception {
        swresample_h.swr_close(segment);
        withSegmentPointer(swresample_h::swr_free);
    }
}
