#!/bin/bash
je=/home/jochen/jdk-17/bin/jextract
package=de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.c

mkdir temp

for header in \
  libavcodec/codec.h \
  libavcodec/avcodec.h \
  libavformat/avformat.h \
  libswresample/swresample.h \
  ; do
  (
    cd temp || exit 1
    $je --source -t $package /usr/include/x86_64-linux-gnu/$header
  )
done
