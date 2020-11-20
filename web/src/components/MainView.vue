<template>
  <div class="flex justify-between flex-col" style="height: calc(100vh - 65px);">
    <div class="flex overflow-y-scroll flex-grow">
      <div class="w-7/12 xl:w-3/4">
        <main-view-soundscape
            :soundscape="soundscape"
            :running-tracks="runningTracks"
            :soundscape-version="soundscapeVersion"
        />
      </div>

      <div class="w-5/12 xl:w-1/4">
        <main-view-effects :effects="effects" @unpin="$emit('unpin', $event)"/>
      </div>
    </div>

    <div class="mt-1 xl:mt-2">
      <main-view-music
          :music="music"
          :playing="musicPlaying"/>
    </div>
  </div>
</template>

<script>
import MainViewSoundscape from "@/components/MainViewSoundscape";
import MainViewMusic from "@/components/MainViewMusic";
import MainViewEffects from "@/components/MainViewEffects";

export default {
  name: "MainView",
  components: {
    MainViewEffects,
    MainViewMusic,
    MainViewSoundscape,
  },

  props: {
    soundscape: {type: String, required: true},
    runningTracks: {type: Array, required: true},
    soundscapeVersion: {required: true},

    music: {type: String, required: true},
    musicPlaying: {type: Boolean, required: true},

    effects: {type: Array, required: true}
  },
  emits: ['unpin']
}
</script>
