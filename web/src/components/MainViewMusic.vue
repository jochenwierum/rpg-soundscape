<template>
  <h2 class="maintitle nomb">
    <span class="flex items-center" v-show="music!==''">
      <span class="pr-2">
        <a href="#" class="appbutton" @click.prevent="musicCommand('restart')">
          <image-reset class="icon"/>
        </a>
        <a href="#" class="appbutton ml-1 md:ml-2" v-show="playing" @click.prevent="musicCommand('pause')">
          <image-pause class="icon"/>
        </a>
        <a href="#" class="appbutton ml-1 md:ml-2" v-show="!playing" @click.prevent="musicCommand('resume')">
          <image-play class="icon"/>
        </a>
      </span>

      <span class="flex-grow text-center">
        <image-music class="w-6 h-6 pr-2"/>
        {{ music }}
      </span>

      <a href="#" class="appbutton" @click.prevent="$emit('info')">
        <image-info class="icon"/>
      </a>
    </span>

    <span v-show="music===''">
      <image-music class="icon"/> (none)
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
