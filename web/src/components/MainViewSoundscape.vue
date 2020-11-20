<template>
  <h2 class="maintitle">
    <span class="flex items-center">
      <span v-show="soundscape!==''">
        <a href="#" class="appbutton appbutton-sm" @click.prevent="soundscapeCommand('reset')">
          <image-reset class="icon"/>
        </a>
        <a href="#" class="appbutton appbutton-sm ml-1 md:ml-2" @click.prevent="soundscapeCommand('pauseAll')">
          <image-pause class="icon"/>
        </a>
      </span>

      <span class="flex-grow text-center">
          <image-soundscape class="icon pr-2"/>
          {{ soundscape }}
          <span v-show="soundscape===''">(none)</span>
        </span>

      <a href="#" class="appbutton appbutton-sm" v-show="soundscape!==''">
        <image-info class="icon"/>
      </a>
    </span>
  </h2>

  <section v-if="soundscape !== ''">
    <ul>
      <li v-for="track in tracks" :key="track.name">
        <clip-control
            :track="track"
            :show-info="false"
            @play="trackCommand('resumetrack', track.name)"
            @pause="trackCommand('pausetrack', track.name)"
        />
      </li>
    </ul>
  </section>

  <section class="m-6" v-else>
    Nothing loaded
  </section>
</template>

<script>
import ClipControl from "@/components/ClipControl";
import ImageSoundscape from "@/components/icons/ImageSoundscape";
import ImageReset from "@/components/icons/ImageReset";
import ImagePause from "@/components/icons/ImagePause";
import ImageInfo from "@/components/icons/ImageInfo";

export default {
  name: "MainViewSoundscape",
  components: {ImageInfo, ImagePause, ImageReset, ImageSoundscape, ClipControl},
  props: {
    soundscape: {type: String, required: true},
    runningTracks: {type: Array, required: true}
  },

  data() {
    return {
      tracks: []
    }
  },

  mounted() {
    this.updateSoundscape = () => {
      if (this.soundscape === '') {
        this.tracks.splice(0, this.tracks.length);
        return;
      }

      fetch("/api/soundscape/" + encodeURIComponent(this.soundscape))
          .catch(console.error)
          .then(response => response.json())
          .then(data => {
            const tracks = data.tracks;
            tracks.forEach(track => track.running = this.runningTracks.includes(track.name));
            this.tracks.splice(0, this.tracks.length, ...tracks);
          });
    };

    this.updateSoundscape();
  },

  watch: {
    soundscape() {
      if (this.soundscape !== '') {
        this.updateSoundscape();
      } else {
        this.tracks.splice(0, this.tracks.length);
      }
    },

    runningTracks: {
      deep: true,
      handler() {
        this.tracks.forEach(track => track.running = this.runningTracks.includes(track.name));
      }
    }
  },

  computed: {
    manualTracks() {
      return this.tracks.filter(t => !t.looping);
    },
    loopingTracks() {
      return this.tracks.filter(t => t.looping);
    }
  },

  methods: {
    trackCommand(command, track) {
      fetch("/api/player/soundscape/" + command, {
        method: "POST",
        headers: {
          "content-type": "application/json"
        },
        body: JSON.stringify({name: track})
      })
          .catch(e => console.log("error from player", e));
    },

    soundscapeCommand(command) {
      fetch("/api/player/soundscape/" + command, {
        method: "POST",
      })
          .catch(e => console.log("error from player", e));
    }
  }
}
</script>
