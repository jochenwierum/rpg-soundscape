<template>
  <section>
    <filter-list
        kind="soundscapes"
        :version="version"
        @data="update($event)"
    />

    <ul>
      <li v-for="soundscape in soundscapes" :key="soundscape" class="resultlist">
        <selectable-clip
            :name="soundscape"
            @open="switchTo(soundscape)"
            @describe="describe(soundscape)"
        />
      </li>
    </ul>
  </section>
</template>

<script>
import SelectableClip from "@/components/SelectableClip";
import FilterList from "@/components/FilterList";

export default {
  name: "SoundscapeView",
  components: {FilterList, SelectableClip},

  props: {
    version: {type: Number, required: true}
  },
  emits: ['switched'],

  data() {
    return {
      soundscapes: []
    }
  },

  methods: {
    update(data) {
      this.soundscapes.splice(0, this.soundscapes.length, ...data);
    },
    switchTo(soundscape) {
      fetch('/api/player/soundscape', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: soundscape})
      })
          .then(() => this.$emit('switched'))
          .catch(e => console.log("Could not switch soundscapes:", e));
    },
    describe(soundscape) {
      fetch('/api/soundscape/' + encodeURIComponent(soundscape) + "/info")
          .then(response => response.json())
          .then(data => this.$emit('describe', data))
          .catch(e => console.log("Could not describe soundscapes:", e));
    }
  }
}
</script>
