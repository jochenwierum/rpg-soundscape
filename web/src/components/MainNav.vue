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
        <span class="inline-block rounded-full text-white bg-red-500 px-2 text-xs font-bold text-sm">{{
            problemsCount
          }}</span>
      </nav-button>
    </ul>

    <span class="mr-4">
      <button v-if="exit" @click="sendExit" class="ml-3">
        <image-shutdown class="h-8 w-8"/>
      </button>

      <button v-if="fullscreen" @click="requestFullScreen" class="ml-3">
        <image-fullscreen class="h-8 w-8"/>
      </button>
    </span>
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
import ImageShutdown from "@/components/icons/ImageShutdown";

export default {
  name: 'MainNav',
  components: {
    ImageShutdown,
    ImageFullscreen, ImageErrors, ImageEffect, ImageMusic, ImageSoundscape, ImagePlayer, NavButton
  },

  data() {
    return {
      exit: false,
      fullscreen: false
    }
  },

  props: {
    active: {type: String, required: true},
    problemsCount: {type: Number, default: () => 0}
  },
  emits: ['select'],

  created() {
    fetch('/api/system/config')
        .then(response => response.json())
        .then(data => {
          this.exit = data.exit;
          this.fullscreen = data.fullscreen;
        })
        .catch(e => console.log("Could not fetch config:", e));
  },

  methods: {
    requestFullScreen() {
      if (document.fullscreenElement) {
        document.exitFullscreen();
      } else {
        document.documentElement.requestFullscreen();
      }
    },

    sendExit() {
      fetch('/api/system/exit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      })
    }
  }
}
</script>
