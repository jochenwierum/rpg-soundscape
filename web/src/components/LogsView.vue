<template>
  <section>
    <h1 class="font-bold text-center text-xl">Logs</h1>
    <table>
      <thead>
        <tr>
          <th>Timestamp</th>
          <th>Prio</th>
          <th>Logger</th>
          <th>Thread</th>
          <th>Message</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="log in logs" :key="log.id" class="logline">
          <td>{{ log.timestamp }}</td>
          <td>{{ log.priority }}</td>
          <td>{{ log.logger }}</td>
          <td>{{ log.thread }}</td>
          <td>{{ log.message }}</td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script>
export default {
  name: "LogsView",
  data() {
    return {
      logs: []
    }
  },

  mounted() {
    this.reopen = () => {
      this.eventSource = new EventSource("/api/logs");

      this.eventSource.addEventListener("log", (event) => {
        this.logs.unshift(JSON.parse(event.data));
      });

      this.eventSource.onerror = (err) => {
        this.eventSource.close();
        console.error("EventSource for logs failed, retrying in 1s:", err);
        this.logs.splice(0, this.logs.length);
        setTimeout(this.reopen, 1000);
      };
    }

    this.reopen();
  }
}
</script>
