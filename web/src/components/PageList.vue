<template>
  <div>
    <nav v-if="this.pages > 1">
      <ul class="flex items-center content-center justify-center">
        <li
            class="button-group"
            :class="{ 'button-disabled': !hasPrevious }"
        >
          <a href="#" @click.prevent="back(10)">
            <span>&laquo;</span>
          </a>
        </li>

        <li
            class="button-group"
            :class="{ 'button-disabled': !hasPrevious }"
        >
          <a href="#" @click.prevent="back(1)">
            <span>&lsaquo;</span>
          </a>
        </li>

        <li
            class="button-group"
            v-for="i in this.pageList"
            :key="i"
            :class="{
            'button-group-active': i != null && page === i - 1
          }"
        >
          <a href="#" @click.prevent="$emit('update:page', i - 1)">
            {{ i }}
          </a>
        </li>

        <li
            class="button-group"
            :class="{ 'button-disabled': !hasNext }"
        >
          <a href="#" @click.prevent="next(1)">
            <span>&rsaquo;</span>
          </a>
        </li>

        <li
            class="button-group"
            :class="{ 'button-disabled': !hasNext }"
        >
          <a href="#" @click.prevent="next(10)">
            <span>&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
export default {
  name: "PageList",
  props: {
    page: {type: Number, required: true},
    pages: {type: Number, required: true}
  },
  emits: ['update:page'],

  computed: {
    hasPrevious() {
      return this.page > 0;
    },
    hasNext() {
      return this.page < this.pages - 1;
    },
    pageList() {
      const list = [];

      const first = Math.max(1, this.page - 5);
      const last = Math.min(this.page + 5, this.pages);

      for (let i = first; i <= last; i += 1) {
        list.push(i);
      }

      return list;
    },
  },

  methods: {
    back(p) {
      this.$emit("update:page", Math.max(0, this.page - p));
    },
    next(p) {
      this.$emit("update:page", Math.min(this.pages - 1, this.page + p));
    }
  }
};
</script>
