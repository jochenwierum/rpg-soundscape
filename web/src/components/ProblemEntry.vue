<template>
  <li class="bg-red-300 border border-red-400 m-2 p-1">
    <div>
      {{ entry.message }}
    </div>

    <ul v-if="entry.errors && entry.errors.length > 0" class="pt-4 pl-6 pb-2">
      <li class="list-disc" v-for="error in entry.errors" :key="error"> {{ error }}</li>
    </ul>

    <pre
        class="pt-2 border-t-2 border-white border-solid"
        v-if="entry.source"
    ><code>{{ entry.source }}</code></pre>

    <p
        class="pt-2 border-t-2 border-white border-solid"
        v-if="entry.link">
      <a :href="entry.link" target="_blank" class="applink">Solve</a>
    </p>
  </li>
</template>

<script>
export default {
  name: "ProblemEntry",
  props: {
    entry: {required: true}
  }
}
</script>
