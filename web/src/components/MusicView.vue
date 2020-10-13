<template>
  <section>
    <filter-list
        kind="music"
        :version="version"
        @data="update($event)"
    />

    <ul>
      <li v-for="music in musics" :key="music" class="p-4">
        <selectable-clip
            :name="music"
            @open="switchTo(music)"
            @describe="describe(music)"
        />
      </li>
    </ul>
  </section>
</template>

<script>
import SelectableClip from "@/components/SelectableClip";
import FilterList from "@/components/FilterList";

export default {
  name: "MusicView",
  components: {FilterList, SelectableClip},
  props: {
    version: {type: Number, required: true}
  },
  emits: ['switched', 'describe'],

  data() {
    return {
      musics: []
    }
  },

  methods: {
    update(data) {
      this.musics.splice(0, this.musics.length, ...data);
    },
    switchTo(music) {
      fetch('/api/player/music', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: music})
      })
          .then(() => this.$emit('switched'))
          .catch(e => console.log("Could not switch music:", e));
    },
    describe(music) {
      fetch('/api/music/' + encodeURIComponent(music) + "/info")
          .then(response => response.json())
          .then(data => this.$emit('describe', data))
          .catch(e => console.log("Could not describe music:", e));
    }
  }
}
</script>
