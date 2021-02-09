<template>
  <section>
    <table class="mx-auto">
      <tr>
        <td>
          <label for="guild" class="pr-2">Guild:</label>
        </td>
        <td>
          <select id="guild" v-model="guild">
            <option v-for="g in guilds" :key="g.id" :value="g.id">
              {{ g.name }}
            </option>
          </select>
        </td>
      </tr>

      <tr v-if="guild !== null">
        <td>
          <label for="channel" class="pr-2">Channel:</label>
        </td>
        <td>
          <select id="channel" v-model="channel">
            <option v-for="c in channels" :key="c.id" :value="c.id">
              {{ c.name }}
            </option>
          </select>
        </td>
      </tr>

      <tr>
        <td>&nbsp;</td>
        <td>
          <a
              class="applink"
              href="#"
              @click="refresh"
          >
            (Refresh)
          </a>
        </td>
      </tr>
    </table>

    <p class="pt-8 text-center">
      <a
          v-show="channel!==null && joinedChannel!==channel"
          class="applink appbutton"
          href="#"
          @click="join"
      >
        Join selected channel
      </a>
      <span
          v-show="!(channel!==null && joinedChannel!==channel)"
          class="applink disabled appbutton"
      >
        Join selected channel
      </span>

      <a
          v-show="joinedChannel !== null"
          class="applink appbutton ml-2"
          href="#"
          @click="leave"
      >
        Leave current channel
      </a>
      <span
          v-show="!(joinedChannel !== null)"
          class="applink disabled appbutton ml-2"
      >
        Leave current channel
      </span>
    </p>

    <p class="pt-4 text-center">
      <a
          class="applink appbutton"
          :href="inviteUrl"
          target="_blank">
        If your guild is missing, invite the bot!
      </a>
    </p>

  </section>
</template>

<script>
export default {
  name: "DiscordView",

  data() {
    return {
      guilds: [],
      guild: null,
      channels: [],
      channel: null,

      joinedGuild: null,
      joinedChannel: null,

      inviteUrl: null
    }
  },

  created() {
    this.updateStatus = () =>
        fetch('/api/discord/status')
            .then(response => response.json())
            .then(data => {
              this.inviteUrl = data.inviteUrl;
              this.joinedGuild = data.guildId;
              this.joinedChannel = data.channelId;
            })
            .catch(e => {
              console.log("Problem while loading discord status", e);
              this.inviteUrl = null;
              this.joinedChannel = null;
              this.joinedGuild = null;
            })

    this.updateGuilds = () => {
      return fetch('/api/discord/guilds')
          .then(response => response.json())
          .then(data => {
            this.guilds.splice(0, this.guilds.length, ...data);
            if (this.guilds.length > 0) {
              if (this.guilds.filter(guild => guild.id === this.joinedGuild).length === 1) {
                this.guild = this.joinedGuild;
              } else {
                this.guild = this.guilds[0].id;
              }
            } else {
              this.guild = null;
            }
          })
          .catch(e => console.log("Could not fetch guilds", e))
    };

    this.updateChannels = () => {
      if (this.guild == null) {
        this.channels.splice(0, this.channels.length);
        this.channel = null;
        return;
      }

      return fetch('/api/discord/guild/' + this.guild + '/channels')
          .then(response => response.json())
          .then(data => {
            this.channels.splice(0, this.channels.length, ...data);
            if (this.channels.filter(channel => channel.id === this.joinedChannel).length === 1) {
              this.channel = this.joinedChannel;
            } else if (this.channels.length > 0) {
              this.channel = this.channels[0].id;
            }
          })
          .catch(e => console.log("Could not fetch guilds", e));
    };

    this.updateStatus().then(() => this.updateGuilds());
  },

  watch: {
    guild() {
      this.updateChannels();
    }
  },

  methods: {
    join() {
      fetch('/api/discord/guild/' + this.guild + '/channel/' + this.channel + '/join', {method: 'POST'})
          .then(() => setTimeout(this.updateStatus, 1000))
          .catch(e => console.log("Cannot join channel " + this.channel + " in guild " + this.guild, e));
      return false;
    },
    leave() {
      fetch('/api/discord/leave', {method: 'POST'})
          .then(() => setTimeout(this.updateStatus, 1000))
          .catch(e => console.log("Cannot join channel " + this.channel + " in guild " + this.guild, e));
      return false;
    },
    refresh() {
      this.updateStatus();
      return false;
    }
  }
}
</script>
