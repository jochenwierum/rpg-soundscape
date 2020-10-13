<template>
  <div>
    <table class="mx-auto">
      <tr>
        <td>
          <label for="category" class="pr-2">Category:</label>
        </td>
        <td>
          <select id="category" v-model="category">
            <option v-for="category in categories" :key="category">
              {{ category }}
            </option>
          </select>
        </td>
      </tr>
      <tr v-show="category !== '(all)'">
        <td>
          <label for="categoryValue" class="pr-2">Value:</label>
        </td>
        <td>
          <select id="categoryValue" v-model="categoryValue">
            <option v-for="value in categoryValues" :key="value">
              {{ value }}
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <td><label for="filter" class="pr-2">Name contains:</label></td>
        <td><input id="filter" type="text" v-model="debouncedSearch"></td>
      </tr>
      <tr><td>&nbsp;</td>
      <td><a href="#" @click="reset" class="applink">reset</a></td></tr>
    </table>
    <div>
      <page-list :pages="pages" v-model:page="page"/>
    </div>
  </div>
</template>

<script>
import PageList from "@/components/PageList";

export default {
  name: "FilterList",
  components: {PageList},
  props: {
    kind: {type: String, required: true},
    version: {type: Number, required: true}
  },
  emits: ['data'],

  data() {
    return {
      categories: [],
      category: "(all)",

      categoryValues: [],
      categoryValue: "",

      search: "",

      pages: 0,
      page: 1
    }
  },
  computed: {
    debouncedSearch: {
      get() {
        return this.search;
      },
      set(val) {
        if (this.timeout) clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          this.search = val
        }, 300)
      }
    }
  },

  created() {
    this.lastRequest = {
      c: "", p: "", q: ""
    };
    this.updateContent = () => {
      if (this.lastRequest.c === this.category && this.lastRequest.p === this.page && this.lastRequest.q === this.search) {
        return;
      } else {
        this.lastRequest = {
          c: this.category, p: this.page, q: this.search
        };
      }

      let uri;
      if (this.category === '(all)') {
        uri = '/api/' + this.kind;
      } else {
        uri = '/api/' + this.kind
            + '/category/' + encodeURIComponent(this.category)
            + '/' + encodeURIComponent(this.categoryValue);
      }

      uri += "?p=" + this.page;
      if (this.search) uri += "&q=" + encodeURIComponent(this.search);

      fetch(uri)
          .catch(e => console.log("Could not fetch " + this.kind, e))
          .then(response => response.json())
          .then(data => {
            this.pages = data.pages;
            this.$emit('data', data.data);
          });
    };

    this.update = () => {
      fetch('/api/' + this.kind + '/categories')
          .then(response => response.json())
          .then(data => {
            data.unshift("(all)");
            this.categories.splice(0, this.categories.length, ...data);
            this.category = "(all)";
            this.page = 0;
          })
          .catch(e => console.log("Could not fetch categories for " + this.kind, e));
      this.updateContent();
    };

    this.update();
  },

  watch: {
    version() {
      this.update();
    },

    category() {
      this.page = 0;
      if (this.category === "(all)") {
        this.categoryValues.splice(0, this.categoryValues.length);
        this.updateContent();
      } else {
        fetch('/api/' + this.kind + '/category/' + encodeURIComponent(this.category))
            .catch(e => console.log("Could not fetch category values for " + this.kind + "/" + this.category, e))
            .then(response => response.json())
            .then(data => {
              this.categoryValues.splice(0, this.categoryValues.length, ...data);
              this.categoryValue = this.categoryValues[0];
            });
      }
    },

    categoryValue() {
      this.page = 0;
      this.updateContent();
    },

    page() {
      this.updateContent();
    },

    search() {
      this.updateContent();
    }
  },

  methods: {
    reset() {
      this.category = '(all)';
      this.search = '';
    }
  }
}
</script>
