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
            <span class="journal-name" v-if="record.hosting_institution">{{ record.hosting_institution }}</span>
            <span class="meta-dot" v-if="record.hosting_institution && record.country">·</span>
            <span class="journal-year" v-if="record.country">{{ record.country }}</span>
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
            <div class="meta-item" v-if="record.cstr">
              <span class="meta-key">CSTR</span>
              <span class="meta-val">{{ record.cstr }}</span>
            </div>
            <div class="meta-item" v-if="record.country">
              <span class="meta-key">Country</span>
              <span class="meta-val">{{ record.country }}</span>
            </div>
            <div class="meta-item" v-if="record.hosting_institution">
              <span class="meta-key">Hosting Institution</span>
              <span class="meta-val">{{ record.hosting_institution }}</span>
            </div>
            <div class="meta-item" v-if="record.contact_information">
              <span class="meta-key">Contact</span>
              <span class="meta-val contact-val">{{ record.contact_information }}</span>
            </div>
            <div class="meta-item" v-if="record.update_time">
              <span class="meta-key">Updated</span>
              <span class="meta-val">{{ record.update_time }}</span>
            </div>
          </div>
        </section>

        <!-- 相关链接 -->
        <section class="metadata-section card" v-if="record.detail_link || record.application_guide || record.equipments || record.use_cases">
          <h3 class="sub-title">Links</h3>
          <div class="meta-grid">
            <div class="meta-item" v-if="record.detail_link">
              <span class="meta-key">Facility Detail</span>
              <span class="meta-val"><a :href="record.detail_link" target="_blank" rel="noopener">{{ record.detail_link }}</a></span>
            </div>
            <div class="meta-item" v-if="record.application_guide">
              <span class="meta-key">Application Guide</span>
              <span class="meta-val"><a :href="record.application_guide" target="_blank" rel="noopener">{{ record.application_guide }}</a></span>
            </div>
            <div class="meta-item" v-if="record.equipments">
              <span class="meta-key">Equipment</span>
              <span class="meta-val"><a :href="record.equipments" target="_blank" rel="noopener">{{ record.equipments }}</a></span>
            </div>
            <div class="meta-item" v-if="record.use_cases">
              <span class="meta-key">User Cases</span>
              <span class="meta-val"><a :href="record.use_cases" target="_blank" rel="noopener">{{ record.use_cases }}</a></span>
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
import { SearchFacilityDetail } from '@/service/FacilitySearch'
import ToTop from '@/components/ToTop.vue'

export default {
  components: { ToTop },
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

<style scoped lang="scss">
@import '~@/assets/detail.scss';
</style>
