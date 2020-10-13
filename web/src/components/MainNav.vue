<template>
  <nav class="fixed flex w-full bg-white" style="height: 56px">
    <ul class="flex justify-center flex-grow">
      <nav-button :active="active === 'main'" @click="$emit('select', 'main')">
        <image-player class="h-4 w-4"/>
        Main
      </nav-button>
      <nav-button :active="active === 'soundscapes'" @click="$emit('select', 'soundscapes')">
        <image-soundscape class="h-4 w-4"/>
        Sound Scapes
      </nav-button>
      <nav-button :active="active === 'music'" @click="$emit('select', 'music')">
        <image-music class="h-4 w-4"/>
        Music
      </nav-button>
      <nav-button :active="active === 'effects'" @click="$emit('select', 'effects')">
        <image-effect class="h-4 w-4"/>
        Effects
      </nav-button>
      <nav-button v-show="problemsCount > 0" :active="active === 'problems'" @click="$emit('select', 'problems')">
        <image-errors class="h-4 w-4"/>
        Problems
        <span class="inline-block rounded-full text-white bg-red-500 px-2 text-xs font-bold text-sm">{{ problemsCount }}</span>
      </nav-button>
    </ul>
    <button @click="requestFullScreen" class="m-4">
      <image-fullscreen class="h-6 w-6"/>
    </button>
  </nav>
</template>


<script>
import NavButton from "@/components/NavButton";
import ImagePlayer from "@/components/icons/ImagePlayer";
import ImageSoundscape from "@/components/icons/ImageSoundscape";
import ImageMusic from "@/components/icons/ImageMusic";
import ImageEffect from "@/components/icons/ImageEffect";
import ImageErrors from "@/components/icons/ImageErrors";
import ImageFullscreen from "@/components/icons/ImageFullscreen";

export default {
  name: 'MainNav',
  components: {ImageFullscreen, ImageErrors, ImageEffect, ImageMusic, ImageSoundscape, ImagePlayer, NavButton},
  props: {
    active: {type: String, required: true},
    problemsCount: {type: Number, default: () => 0}
  },
  emits: ['select'],

  methods: {
    requestFullScreen() {
      if (document.fullscreenElement) {
        document.exitFullscreen();
      } else {
        document.documentElement.requestFullscreen();
      }
    }
  }
}
</script>
