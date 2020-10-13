<template>
  <section>
    <filter-list
        kind="effects"
        :version="version"
        @data="update($event)"
    />

    <ul>
      <li v-for="effect in state" :key="effect.name" class="p-4">
        <selectable-clip
            :name="effect.name"
            :show-pin="!effect.pinned"
            :show-delete="effect.pinned"
            @open="play(effect.name)"
            @pin="$emit('pin', effect.name)"
            @delete="$emit('unpin', effect.name)"
            @describe="describe(effect.name)"
        />
      </li>
    </ul>
  </section>
</template>

<script>
import SelectableClip from "@/components/SelectableClip";
import FilterList from "@/components/FilterList";

export default {
  name: "EffectsView",
  components: {FilterList, SelectableClip},
  props: {
    pinned: {type: Array, required: true},
    version: {type: Number, required: true}
  },
  emits: ['pin', 'unpin'],

  data() {
    return {
      effects: []
    }
  },

  computed: {
    state() {
      return this.effects.map(e => ({name: e, pinned: this.pinned.indexOf(e) >= 0}));
    }
  },

  methods: {
    update(data) {
      this.effects.splice(0, this.effects.length, ...data);
    },
    play(effect) {
      fetch('/api/player/effect', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: effect})
      })
          .catch(e => console.log("Could not play effect:", e));
    },

    describe(effect) {
      fetch('/api/effect/' + encodeURIComponent(effect) + "/info")
          .then(response => response.json())
          .then(data => this.$emit('describe', data))
          .catch(e => console.log("Could not describe soundscapes:", e));
    }
  }
}
</script>
