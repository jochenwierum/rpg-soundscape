<template>
  <section>
    <ul v-for="entry in entries" :key="entry.id">
      <problem-entry :entry="entry"/>
    </ul>
  </section>
</template>

<script>
import ProblemEntry from "@/components/ProblemEntry";

export default {
  name: 'ProblemView',
  components: {ProblemEntry},
  props: {
    problemsCount: {type: Number, required: true}
  },

  data() {
    return {
      entries: []
    }
  },


  created() {
    this.update = () => {
      fetch('/api/status/problems')
          .then(response => response.json())
          .then(data => {
            this.entries.splice(0, this.entries.length, ...data);
          })
          .catch(e => console.log("Could not fetch problems", e));
    };

    this.update();
  },
  watch: {
    problemsCount() {
      this.update();
    }
  }
}
</script>
