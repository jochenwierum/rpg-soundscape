<template>
  <div>
    <MainNav @select="selected = $event" :active="selected" :problems-count="problemsCount"/>
    <main style="padding-top: 56px">
      <main-view v-if="selected === 'main'"
                 :running-tracks="runningTracks"
                 :music="music"
                 :music-playing="musicPlaying"
                 :effects="effects"
                 :soundscape="soundscape"
                 @unpin="unpin($event)"
                 @describe="describeClip = $event"
      />

      <soundscape-view v-if="selected === 'soundscapes'"
                       :version="libraryVersion"
                       @switched="selected = 'main'"
                       @describe="describeClip = $event"/>

      <music-view v-if="selected === 'music'"
                  :version="libraryVersion"
                  @switched="selected = 'main'"
                  @describe="describeClip = $event"/>

      <effects-view v-if="selected === 'effects'"
                    :version="libraryVersion"
                    :pinned="effects"
                    @pin="effects.push($event)"
                    @unpin="unpin($event)"
                    @describe=" describeClip=$event"/>

      <problem-view v-if="selected === 'problems'"/>
    </main>

    <clip-info :show-description="describeClip"/>
  </div>
</template>

<script>
import '@/assets/styles/tailwind.css';

import MainNav from "@/components/MainNav";
import ProblemView from "@/components/ProblemView";
import SoundscapeView from "@/components/SoundscapeView";
import MainView from "@/components/MainView";
import MusicView from "@/components/MusicView";
import EffectsView from "@/components/EffectsView";
import ClipInfo from "@/components/ClipInfo";

export default {
  name: 'App',
  components: {
    ClipInfo,
    EffectsView,
    MusicView,
    MainView,
    SoundscapeView,
    ProblemView,
    MainNav,
  },

  data() {
    return {
      selected: 'main',
      libraryVersion: 0,
      problemsCount: 0,

      soundscape: '',
      runningTracks: [],

      music: '',
      musicPlaying: true,

      effects: [],

      describeClip: null,
    }
  },

  created() {
    this.reopen = () => {
      this.eventSource = new EventSource("/api/status");

      this.eventSource.addEventListener("problems", (event) => {
        this.problemsCount = JSON.parse(event.data);
        if (this.problemsCount === 0 && this.selected === 'problems') this.selected = 'main';
      });

      this.eventSource.addEventListener("updateLibrary", () => {
        this.libraryVersion++;
      });

      this.eventSource.addEventListener("soundscapeChanged", (event) => {
        const data = JSON.parse(event.data);
        this.soundscape = data.soundscape;
        this.runningTracks.splice(0, this.runningTracks.length, ...data.runningTracks);
      });


      this.eventSource.addEventListener("musicChanged", (event) => {
        const data = JSON.parse(event.data);
        this.music = data.name;
        this.musicPlaying = data.playing;
      });

      this.eventSource.addEventListener("ping", () => console.log("ping"));

      this.eventSource.onerror = (err) => {
        this.eventSource.close();
        console.error("EventSource failed, retrying in 1s:", err);
        this.problemsCount = 0;
        this.selected = 'main';
        setTimeout(this.reopen, 1000);
      };
    }
  },
  methods: {
    unpin(effect) {
      const index = this.effects.indexOf(effect);
      if (index > -1) {
        this.effects.splice(index, 1);
      }
    }
  },
  mounted() {
    this.reopen();
  }
}
</script>
