<template>
  <h2 class="maintitle">
    <image-effect class="w-8 h-8"/>
    Effects
  </h2>
  <ul class="m-4">
    <li v-for="effect in effects" :key="effect">
      <clip-control
          :track="track(effect)"
          :show-delete="true"
          @play="play(effect)"
          @delete="$emit('unpin', effect)"
      />
    </li>
  </ul>
</template>

<script>
import ImageEffect from "@/components/icons/ImageEffect";
import ClipControl from "@/components/ClipControl";

export default {
  name: "MainViewEffects",
  components: {ClipControl, ImageEffect},

  props: {
    effects: {type: Array, required: true}
  },
  emits: ['unpin'],

  methods: {
    play(effect) {
        fetch("/api/player/effect", {
          method: "POST",
          headers: {
            "content-type": "application/json"
          },
          body: JSON.stringify({name: effect})
        })
            .catch(e => console.log("error from player", e));
      },
    track(effect) {
      return {
        name: effect,
        title: effect,
        looping: false,
        running: false
      }
    }
  }
}
</script>
