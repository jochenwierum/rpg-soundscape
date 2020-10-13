<template>
  <div class="text-xl m-4 bg-gray-300 rounded border-8 border-transparent border-solid flex items-center">
    <span class="pr-2">
      <a href="#" class="appbutton" v-show="track.running" @click.prevent="$emit('pause')">
        <image-pause class="w-10 h-10"/>
      </a>
      <a href="#" class="appbutton" v-show="!track.running" @click.prevent="$emit('play')">
        <image-play class="w-10 h-10"/>
      </a>
      <a href="#" class="appbutton ml-2" v-if="showDelete" @click.prevent="$emit('delete')">
        <image-delete class="w-10 h-10"/>
      </a>
    </span>

    <span class="flex-grow text-center font-bold">
      <image-repeat class="w-6 h-6 pr-2" v-if="track.looping"/>
      {{ track.title }}
    </span>

    <a href="#" class="appbutton" v-if="showInfo" @click.prevent="$emit('info')">
      <image-info class="w-8 h-8"/>
    </a>
  </div>
</template>

<script>
import ImagePause from "@/components/icons/ImagePause";
import ImagePlay from "@/components/icons/ImagePlay";
import ImageRepeat from "@/components/icons/ImageRepeat";
import ImageInfo from "@/components/icons/ImageInfo";
import ImageDelete from "@/components/icons/ImageDelete";

export default {
  name: "ClipControl",
  components: {ImageDelete, ImageInfo, ImageRepeat, ImagePlay, ImagePause},

  props: {
    track: {required: true},
    showInfo: {type: Boolean, default: () => true},
    showDelete: {type: Boolean, default: () => false}
  },

  emits: ['play', 'delete', 'pause', 'reset', 'info']
}
</script>
