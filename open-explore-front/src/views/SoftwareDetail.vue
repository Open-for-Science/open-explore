<template>
  <div class="fullscreen-reader">
    <header class="top-toolbar">
      <button class="back-btn" @click="$router.back()">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
        Back
      </button>
      <div class="toolbar-divider"></div>
    </header>
    <main class="reader-main" v-if="record">
      <div class="article-container">
        <section class="hero-section">
          <div class="facility-image-wrap" v-if="record['image-url']">
            <img :src="record['image-url']" :alt="record.title" class="facility-image" @error="onImgError" />
          </div>
          <h1 class="article-title">{{ record.title || record['title-en'] || 'Untitled' }}</h1>
          <div class="journal-meta">
            <span class="journal-name" v-if="record.version">v{{ record.version }}</span>
            <span class="meta-dot" v-if="record.version && record['pub-year']">·</span>
            <span class="journal-year" v-if="record['pub-year']">{{ record['pub-year'] }}</span>
          </div>
        </section>
        <section class="abstract-section card" v-if="record.abstracts || record['abstract-en'] || record.description">
          <h2 class="section-title">Description</h2>
          <p class="abstract-body">{{ record.description || record.abstracts || record['abstract-en'] }}</p>
        </section>
        <section class="keywords-section card" v-if="(record.keyword && record.keyword.length)">
          <h3 class="sub-title">Keywords</h3>
          <div class="keyword-cloud">
            <span v-for="(k, i) in record.keyword" :key="i" class="keyword-pill">{{ k }}</span>
          </div>
        </section>
        <section class="metadata-section card">
          <h3 class="sub-title">Software Information</h3>
          <div class="meta-grid">
            <div class="meta-item" v-if="record['programming-language']">
              <span class="meta-key">Programming Language</span>
              <span class="meta-val">{{ record['programming-language'] }}</span>
            </div>
            <div class="meta-item" v-if="record['resource-type']">
              <span class="meta-key">Resource Type</span>
              <span class="meta-val">{{ record['resource-type'] }}</span>
            </div>
            <div class="meta-item" v-if="record.category">
              <span class="meta-key">Category</span>
              <span class="meta-val">{{ record.category }}</span>
            </div>
            <div class="meta-item" v-if="record.license">
              <span class="meta-key">License</span>
              <span class="meta-val">{{ record.license }}</span>
            </div>
            <div class="meta-item" v-if="record['source-url']">
              <span class="meta-key">Source</span>
              <span class="meta-val"><a :href="record['source-url']" target="_blank">{{ record['source-url'] }}</a></span>
            </div>
            <div class="meta-item" v-if="record.doi">
              <span class="meta-key">DOI</span>
              <span class="meta-val">{{ record.doi }}</span>
            </div>
            <div class="meta-item" v-if="record['pub-date']">
              <span class="meta-key">Publication Date</span>
              <span class="meta-val">{{ record['pub-date'] }}</span>
            </div>
          </div>
        </section>
      </div>
    </main>
    <div v-else class="loading-placeholder">
      <div class="spinner"></div>
      <p>Loading...</p>
    </div>
    <ToTop />
  </div>
</template>

<script>
import { SearchSoftwareDetail } from '@/service/SoftwareSearch'
import ToTop from '@/components/ToTop.vue'

export default {
  components: { ToTop },
  data() { return { record: null } },
  created() { this.loadDetail() },
  methods: {
    async loadDetail() {
      const id = this.$route.params.id
      if (!id) return
      const res = await SearchSoftwareDetail(id)
      if (res.result && res.result.data) {
        this.record = res.result.data
      }
    },
    onImgError(e) {
      e.target.style.display = 'none'
    }
  }
}
</script>

<style scoped lang="scss">
@import '~@/assets/detail.scss';
</style>
