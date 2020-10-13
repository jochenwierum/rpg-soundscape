<template>
  <div class="flex justify-between">
    <span>
      <a
          href="#"
          class="appbutton"
          @click.prevent="$emit('open')">
        <image-play class="w-8 h-8"/>
      </a>

      <a
          href="#"
          class="appbutton ml-2"
          v-if="showPin"
          @click.prevent="$emit('pin')">
        <image-pin class="w-8 h-8"/>
      </a>

      <a
          href="#"
          class="appbutton ml-2"
          v-if="showDelete"
          @click.prevent="$emit('delete')">
        <image-delete class="w-8 h-8"/>
      </a>

      {{ name }}
    </span>

    <span>
      <a
          href="#"
          class="appbutton ml-2"
          @click.prevent="$emit('describe')">
        <image-info class="w-8 h-8"/>
      </a>
    </span>
  </div>
</template>

<script>
import ImagePlay from "@/components/icons/ImagePlay";
import ImagePin from "@/components/icons/ImagePin";
import ImageDelete from "@/components/icons/ImageDelete";
import ImageInfo from "@/components/icons/ImageInfo";

export default {
  name: "SelectableClip",
  components: {ImageInfo, ImageDelete, ImagePin, ImagePlay},
  props: {
    name: {type: String, required: true},
    showPin: {type: Boolean, default: () => false},
    showDelete: {type: Boolean, default: () => false}
  },
  emits: ['describe', 'open', 'pin', 'delete']
}
</script>
