<template>
  <h2 class="maintitle">
    <span class="flex items-center" v-show="music!==''">
      <span class="pr-2">
        <a href="#" class="appbutton" @click.prevent="musicCommand('restart')">
          <image-reset class="w-10 h-10"/>
        </a>
        <a href="#" class="appbutton" v-show="playing" @click.prevent="musicCommand('pause')">
          <image-pause class="w-10 h-10"/>
        </a>
        <a href="#" class="appbutton" v-show="!playing" @click.prevent="musicCommand('resume')">
          <image-play class="w-10 h-10"/>
        </a>
      </span>

      <span class="flex-grow text-center font-bold">
        <image-music class="w-6 h-6 pr-2"/>
        {{ music }}
      </span>

      <a href="#" class="appbutton" @click.prevent="$emit('info')">
        <image-info class="w-8 h-8"/>
      </a>
    </span>

    <span v-show="music===''">
      <image-music class="w-6 h-6"/> (none)
    </span>
  </h2>
</template>

<script>
import ImageMusic from "@/components/icons/ImageMusic";
import ImagePause from "@/components/icons/ImagePause";
import ImagePlay from "@/components/icons/ImagePlay";
import ImageReset from "@/components/icons/ImageReset";
import ImageInfo from "@/components/icons/ImageInfo";

export default {
  name: "MainViewMusic",
  components: {ImageInfo, ImageReset, ImagePlay, ImagePause, ImageMusic},
  props: {
    music: {type: String, required: true},
    playing: {type: Boolean, required: true}
  },

  methods: {
    musicCommand(command) {
      fetch("/api/player/music/" + command, {
        method: "POST",
      })
          .catch(e => console.log("error from player", e));
    }
  }
}
</script>
