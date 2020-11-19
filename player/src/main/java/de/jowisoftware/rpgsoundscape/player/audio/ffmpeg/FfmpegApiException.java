package de.jowisoftware.rpgsoundscape.player.audio.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.bytedeco.ffmpeg.global.avutil.AVERROR_BSF_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_BUFFER_TOO_SMALL;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_BUG;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_BUG2;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_DECODER_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_DEMUXER_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EACCES;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EAGAIN;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EBADF;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EDOM;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EEXIST;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EFAULT;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EFBIG;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EILSEQ;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EINTR;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EINVAL;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EIO;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENAMETOOLONG;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENCODER_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENODEV;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENOENT;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENOMEM;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENOSPC;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENOSYS;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ENXIO;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EOF;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EPERM;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EPIPE;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ERANGE;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_ESPIPE;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EXDEV;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EXIT;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EXPERIMENTAL;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_EXTERNAL;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_FILTER_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_BAD_REQUEST;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_FORBIDDEN;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_OTHER_4XX;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_SERVER_ERROR;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_HTTP_UNAUTHORIZED;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_INVALIDDATA;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_MUXER_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_OPTION_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_OUTPUT_CHANGED;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_PATCHWELCOME;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_PROTOCOL_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_STREAM_NOT_FOUND;
import static org.bytedeco.ffmpeg.global.avutil.AVERROR_UNKNOWN;

public class FfmpegApiException extends RuntimeException {
    private static final Logger LOG = LoggerFactory.getLogger(FfmpegApiException.class);

    public FfmpegApiException(int res, String message) {
        super(message + ", status: " + res + getStatusText(res).map(s -> " (" + s + ")").orElse(""));
        LOG.warn("About to throw: " + getMessage());
    }

    private static Optional<String> getStatusText(int res) {
        if (res == AVERROR_EACCES()) {
            return Optional.of("AVERROR_EACCES");
        } else if (res == AVERROR_EAGAIN()) {
            return Optional.of("AVERROR_EAGAIN");
        } else if (res == AVERROR_EBADF()) {
            return Optional.of("AVERROR_EBADF");
        } else if (res == AVERROR_EDOM()) {
            return Optional.of("AVERROR_EDOM");
        } else if (res == AVERROR_EEXIST()) {
            return Optional.of("AVERROR_EEXIST");
        } else if (res == AVERROR_EFAULT()) {
            return Optional.of("AVERROR_EFAULT");
        } else if (res == AVERROR_EFBIG()) {
            return Optional.of("AVERROR_EFBIG");
        } else if (res == AVERROR_EILSEQ()) {
            return Optional.of("AVERROR_EILSEQ");
        } else if (res == AVERROR_EINTR()) {
            return Optional.of("AVERROR_EINTR");
        } else if (res == AVERROR_EINVAL()) {
            return Optional.of("AVERROR_EINVAL");
        } else if (res == AVERROR_EIO()) {
            return Optional.of("AVERROR_EIO");
        } else if (res == AVERROR_ENAMETOOLONG()) {
            return Optional.of("AVERROR_ENAMETOOLONG");
        } else if (res == AVERROR_ENODEV()) {
            return Optional.of("AVERROR_ENODEV");
        } else if (res == AVERROR_ENOENT()) {
            return Optional.of("AVERROR_ENOENT");
        } else if (res == AVERROR_ENOMEM()) {
            return Optional.of("AVERROR_ENOMEM");
        } else if (res == AVERROR_ENOSPC()) {
            return Optional.of("AVERROR_ENOSPC");
        } else if (res == AVERROR_ENOSYS()) {
            return Optional.of("AVERROR_ENOSYS");
        } else if (res == AVERROR_ENXIO()) {
            return Optional.of("AVERROR_ENXIO");
        } else if (res == AVERROR_EPERM()) {
            return Optional.of("AVERROR_EPERM");
        } else if (res == AVERROR_EPIPE()) {
            return Optional.of("AVERROR_EPIPE");
        } else if (res == AVERROR_ERANGE()) {
            return Optional.of("AVERROR_ERANGE");
        } else if (res == AVERROR_ESPIPE()) {
            return Optional.of("AVERROR_ESPIPE");
        } else if (res == AVERROR_EXDEV()) {
            return Optional.of("AVERROR_EXDEV");
        } else if (res == AVERROR_BSF_NOT_FOUND) {
            return Optional.of("AVERROR_BSF_NOT_FOUND");
        } else if (res == AVERROR_BUG) {
            return Optional.of("AVERROR_BUG");
        } else if (res == AVERROR_BUFFER_TOO_SMALL) {
            return Optional.of("AVERROR_BUFFER_TOO_SMALL");
        } else if (res == AVERROR_DECODER_NOT_FOUND) {
            return Optional.of("AVERROR_DECODER_NOT_FOUND");
        } else if (res == AVERROR_DEMUXER_NOT_FOUND) {
            return Optional.of("AVERROR_DEMUXER_NOT_FOUND");
        } else if (res == AVERROR_ENCODER_NOT_FOUND) {
            return Optional.of("AVERROR_ENCODER_NOT_FOUND");
        } else if (res == AVERROR_EOF) {
            return Optional.of("AVERROR_EOF");
        } else if (res == AVERROR_EXIT) {
            return Optional.of("AVERROR_EXIT");
        } else if (res == AVERROR_EXTERNAL) {
            return Optional.of("AVERROR_EXTERNAL");
        } else if (res == AVERROR_FILTER_NOT_FOUND) {
            return Optional.of("AVERROR_FILTER_NOT_FOUND");
        } else if (res == AVERROR_INVALIDDATA) {
            return Optional.of("AVERROR_INVALIDDATA");
        } else if (res == AVERROR_MUXER_NOT_FOUND) {
            return Optional.of("AVERROR_MUXER_NOT_FOUND");
        } else if (res == AVERROR_OPTION_NOT_FOUND) {
            return Optional.of("AVERROR_OPTION_NOT_FOUND");
        } else if (res == AVERROR_PATCHWELCOME) {
            return Optional.of("AVERROR_PATCHWELCOME");
        } else if (res == AVERROR_PROTOCOL_NOT_FOUND) {
            return Optional.of("AVERROR_PROTOCOL_NOT_FOUND");
        } else if (res == AVERROR_STREAM_NOT_FOUND) {
            return Optional.of("AVERROR_STREAM_NOT_FOUND");
        } else if (res == AVERROR_BUG2) {
            return Optional.of("AVERROR_BUG2");
        } else if (res == AVERROR_UNKNOWN) {
            return Optional.of("AVERROR_UNKNOWN");
        } else if (res == AVERROR_EXPERIMENTAL) {
            return Optional.of("AVERROR_EXPERIMENTAL");
        } else if (res == AVERROR_OUTPUT_CHANGED) {
            return Optional.of("AVERROR_OUTPUT_CHANGED");
        } else if (res == AVERROR_HTTP_BAD_REQUEST) {
            return Optional.of("AVERROR_HTTP_BAD_REQUEST");
        } else if (res == AVERROR_HTTP_UNAUTHORIZED) {
            return Optional.of("AVERROR_HTTP_UNAUTHORIZED");
        } else if (res == AVERROR_HTTP_FORBIDDEN) {
            return Optional.of("AVERROR_HTTP_FORBIDDEN");
        } else if (res == AVERROR_HTTP_NOT_FOUND) {
            return Optional.of("AVERROR_HTTP_NOT_FOUND");
        } else if (res == AVERROR_HTTP_OTHER_4XX) {
            return Optional.of("AVERROR_HTTP_OTHER_4XX");
        } else if (res == AVERROR_HTTP_SERVER_ERROR) {
            return Optional.of("AVERROR_HTTP_SERVER_ERROR");
        } else {
            return Optional.empty();
        }
    }
}
