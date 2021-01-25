<template>
  <div v-if="description !== null"
       class="inset-0 absolute p-12"
       style="background-color: rgba(255,255,255,0.8)">
    <div class="h-full bg-gray-100 p-4 flex justify-between flex-col overflow-y-scroll">
      <div>
        <h2 class="text-3xl">{{ description.name }}</h2>
        <p v-if="description.description" v-html="save(description.description)"></p>

        <section class="mt-8" v-if="description.categories && Object.keys(description.categories).length > 0">
          <h3 class="text-2xl">Categories</h3>
          <dl>
            <template v-for="c in Object.keys(description.categories)" :key="c">
              <dt class="font-bold">{{ c }}:</dt>
              <dd>
                <ul class="ml-6 list-disc">
                  <li v-for="value in description.categories[c]" :key="value">{{ value }}</li>
                </ul>
              </dd>
            </template>
          </dl>
        </section>

        <section class="mt-8" v-if="description.sampleCount || description.trackCount">
          <h3 class="text-2xl">Statistics</h3>
          <ul class="ml-6 list-disc">
            <li>{{ description.sampleCount }} Samples</li>
            <li>{{ description.trackCount }} Tracks</li>
          </ul>
        </section>

        <section class="mt-8" v-if="description.attributions">
          <h3 class="text-2xl">Attributions</h3>
          <p v-html="save(description.attributions)"></p>
        </section>
      </div>
      <div class="text-right"><a href="#" class="applink" @click.prevent="description = null">[ close ]</a></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ClipInfo",
  portal: 'body',
  props: {
    showDescription: {type: Object}
  },
  data() {
    return {
      description: null
    }
  },
  watch: {
    showDescription() {
      this.description = this.showDescription;
    }
  },
  methods: {
    save(raw) {
      return raw
          .replace(/&/g, "&amp;")
          .replace(/</g, "&lt;")
          .replace(/>/g, "&gt;")
          .replace(/"/g, "&quot;")
          .replace(/'/g, "&#039;")
          .replace(/\n/g, '<br>');
    }
  }
}
</script>
