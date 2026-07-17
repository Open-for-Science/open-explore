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
          <div class="facility-image-wrap" v-if="record.image">
            <img :src="record.image" :alt="record.title" class="facility-image" @error="onImgError" />
          </div>
          <h1 class="article-title">{{ record.title || record['title-en'] || 'Untitled' }}</h1>
          <div class="journal-meta">
            <span class="journal-name" v-if="record['facility-type']">{{ record['facility-type'] }}</span>
            <span class="meta-dot" v-if="record['facility-type'] && record['pub-year']">·</span>
            <span class="journal-year" v-if="record['pub-year']">{{ record['pub-year'] }}</span>
            <span class="meta-dot" v-if="record['pub-year'] && record['facility-location']">·</span>
            <span v-if="record['facility-location']">{{ record['facility-location'] }}</span>
          </div>
        </section>
        <section class="abstract-section card" v-if="record.abstracts || record['abstract-en'] || record.description_information">
          <h2 class="section-title">Description</h2>
          <p class="abstract-body">{{ record.description_information || record.abstracts || record['abstract-en'] }}</p>
        </section>
        <section class="keywords-section card" v-if="(record.keyword && record.keyword.length)">
          <h3 class="sub-title">Keywords</h3>
          <div class="keyword-cloud">
            <span v-for="(k, i) in record.keyword" :key="i" class="keyword-pill">{{ k }}</span>
          </div>
        </section>
        <section class="metadata-section card">
          <h3 class="sub-title">Facility Information</h3>
          <div class="meta-grid">
            <div class="meta-item" v-if="record['facility-name']">
              <span class="meta-key">Facility Name</span>
              <span class="meta-val">{{ record['facility-name'] }}</span>
            </div>
            <div class="meta-item" v-if="record['facility-type']">
              <span class="meta-key">Type</span>
              <span class="meta-val">{{ record['facility-type'] }}</span>
            </div>
            <div class="meta-item" v-if="record['facility-location']">
              <span class="meta-key">Location</span>
              <span class="meta-val">{{ record['facility-location'] }}</span>
            </div>
            <div class="meta-item" v-if="record['facility-status']">
              <span class="meta-key">Status</span>
              <span class="meta-val">{{ record['facility-status'] }}</span>
            </div>
            <div class="meta-item" v-if="record['operation-year']">
              <span class="meta-key">Operation Year</span>
              <span class="meta-val">{{ record['operation-year'] }}</span>
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
  </div>
</template>

<script>
import { SearchFacilityDetail } from '@/service/FacilitySearch'

export default {
  data() { return { record: null } },
  created() { this.loadDetail() },
  methods: {
    async loadDetail() {
      const id = this.$route.params.id
      if (!id) return
      const res = await SearchFacilityDetail(id)
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

<style scoped>
.fullscreen-reader { min-height: 100vh; background: #fff; }
.top-toolbar { position: sticky; top: 0; z-index: 100; display: flex; align-items: center; height: 56px; padding: 0 24px; background: #fff; border-bottom: 1px solid #e5e7eb; }
.back-btn { display: inline-flex; align-items: center; gap: 6px; background: none; border: none; cursor: pointer; font-size: 14px; color: #374151; }
.back-btn:hover { color: #2563eb; }
.toolbar-divider { flex: 1; }
.reader-main { max-width: 800px; margin: 0 auto; padding: 40px 24px; }
.article-title { font-size: 28px; font-weight: 700; color: #111; line-height: 1.3; margin-bottom: 12px; }
.journal-meta { font-size: 14px; color: #6b7280; margin-bottom: 24px; }
.meta-dot { margin: 0 8px; }
.card { background: #f9fafb; border-radius: 8px; padding: 20px; margin-bottom: 20px; }
.section-title { font-size: 18px; font-weight: 600; margin-bottom: 12px; color: #111; }
.sub-title { font-size: 16px; font-weight: 600; margin-bottom: 10px; color: #111; }
.abstract-body { font-size: 15px; line-height: 1.8; color: #374151; }
.keyword-cloud { display: flex; flex-wrap: wrap; gap: 8px; }
.keyword-pill { padding: 4px 12px; background: #e5e7eb; border-radius: 16px; font-size: 13px; color: #374151; }
.facility-image-wrap { margin-bottom: 20px; border-radius: 12px; overflow: hidden; max-height: 400px; }
.facility-image { width: 100%; height: auto; max-height: 400px; object-fit: cover; display: block; }
.meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.meta-item { display: flex; flex-direction: column; }
.meta-key { font-size: 12px; color: #9ca3af; text-transform: uppercase; margin-bottom: 4px; }
.meta-val { font-size: 14px; color: #111; }
.meta-val a { color: #2563eb; word-break: break-all; }
.loading-placeholder { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 60vh; color: #9ca3af; }
.spinner { width: 32px; height: 32px; border: 3px solid #e5e7eb; border-top-color: #2563eb; border-radius: 50%; animation: spin 0.6s linear infinite; margin-bottom: 12px; }
@keyframes spin { to { transform: rotate(360deg); } }
</style>
