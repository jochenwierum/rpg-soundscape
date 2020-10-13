<template>
  <li class="inline-block m-4">
    <a v-if="enabled"
        href="#"
        @click.prevent="$emit('click')"
        class="applink"
        :class="{'border-b': active}"
    >
      <slot/>
    </a>
    <span v-else
          class="applink text-gray-700"
    >
      <slot />
    </span>
  </li>
</template>

<script>
export default {
  name: 'NavButton',
  props: {
    active: {type: Boolean, required: true},
    enabled: {type: Boolean, default: () => true}
  },
  emits: ['click']
}
</script>
