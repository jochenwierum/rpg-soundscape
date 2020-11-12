<template>
  <span class="
  fixed right-0 bottom-0 z-10
  bg-gray-700 m-8
  border-solid border-8 border-gray-700 rounded
  text-gray-200
">
    <image-status-resolving class="w-8 h-8"/> {{ resolving }} <br />
    <image-status-failed class="w-8 h-8"/> {{ failed }} <br />
    <image-status-resolved class="w-8 h-8"/> {{ resolved }}
  </span>
</template>

<script>
import ImageStatusResolved from "@/components/icons/ImageStatusResolved";
import ImageStatusResolving from "@/components/icons/ImageStatusResolving";
import ImageStatusFailed from "@/components/icons/ImageStatusFailed";

export default {
  name: "ResolvedStatus",
  components: {ImageStatusFailed, ImageStatusResolving, ImageStatusResolved},

  props: {
    resolved: {type: Number, required: true},
    resolving: {type: Number, required: true},
    failed: {type: Number, required: true},
  }
}
</script>
