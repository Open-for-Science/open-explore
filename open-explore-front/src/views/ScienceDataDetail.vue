<template>
  <!-- 全屏沉浸式阅读 -->
  <div class="fullscreen-reader">
    <!-- 顶部悬浮工具栏 -->
    <header class="top-toolbar">
      <button class="back-btn" @click="$router.back()">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
        Back
      </button>
      <div class="toolbar-divider"></div>
    </header>

    <!-- 主内容区 -->
    <main class="reader-main">
      <div class="article-container">
        <!-- 标题区 -->
        <section class="hero-section">
          <h1 class="article-title">{{ record.title || record['title-en'] || 'Untitled' }}</h1>
          <div class="authors-list" v-if="record.contributor && record.contributor.length">
            <span
              v-for="(contrib, idx) in record.contributor"
              :key="idx"
              class="author-chip"
            >
              {{ contrib.auth || contrib['auth-en'] }}
            </span>
          </div>
          <div class="authors-list" v-else-if="record.author || record['author-en']">
            <span class="author-chip">{{ record.author || record['author-en'] }}</span>
          </div>
          <div class="journal-meta">
            <span class="journal-name" v-if="record.publisher && record.publisher.name">{{ record.publisher.name }}</span>
            <span class="meta-dot" v-if="record.publisher && record.publisher.name && record['pub-year']">·</span>
            <span class="journal-year" v-if="record['pub-year']">{{ record['pub-year'] }}</span>
            <span class="meta-dot" v-if="record['pub-year'] && record['dataset-type']">·</span>
            <span v-if="record['dataset-type']">{{ record['dataset-type'] }}</span>
          </div>
        </section>

        <!-- 摘要 -->
        <section class="abstract-section card" v-if="record.abstract || record['abstract-en']">
          <h2 class="section-title">Abstract</h2>
          <p class="abstract-body">{{ record.abstract || record['abstract-en'] }}</p>
        </section>

        <!-- 关键词 -->
        <section class="keywords-section card" v-if="(record.keyword && record.keyword.length) || (record['keyword-en'] && record['keyword-en'].length)">
          <h3 class="sub-title">Keywords</h3>
          <div class="keyword-cloud">
            <span
              v-for="(k, i) in (record.keyword || record['keyword-en'])"
              :key="i"
              class="keyword-pill"
            >
              {{ k }}
            </span>
          </div>
        </section>

        <!-- 数据集信息 -->
        <section class="metadata-section card">
          <h3 class="sub-title">Dataset Information</h3>
          <div class="meta-grid">
            <div class="meta-item" v-if="record['dataset-id']">
              <span class="meta-key">Dataset ID</span>
              <span class="meta-val">{{ record['dataset-id'] }}</span>
            </div>
            <div class="meta-item" v-if="record['dataset-type']">
              <span class="meta-key">Dataset Type</span>
              <span class="meta-val">{{ record['dataset-type'] }}</span>
            </div>
            <div class="meta-item" v-if="record.type">
              <span class="meta-key">Type</span>
              <span class="meta-val">{{ record.type }}</span>
            </div>
            <div class="meta-item" v-if="record.size">
              <span class="meta-key">Size</span>
              <span class="meta-val">{{ record.size }}</span>
            </div>
            <div class="meta-item" v-if="record['file-count']">
              <span class="meta-key">File Count</span>
              <span class="meta-val">{{ record['file-count'] }}</span>
            </div>
            <div class="meta-item" v-if="record['record-count']">
              <span class="meta-key">Record Count</span>
              <span class="meta-val">{{ record['record-count'] }}</span>
            </div>
            <div class="meta-item" v-if="record['start-date'] && record['end-date']">
              <span class="meta-key">Time Range</span>
              <span class="meta-val">{{ record['start-date'] }} ~ {{ record['end-date'] }}</span>
            </div>
            <div class="meta-item" v-if="record['spatial-dimension']">
              <span class="meta-key">Spatial Dimension</span>
              <span class="meta-val">{{ record['spatial-dimension'] }}</span>
            </div>
            <div class="meta-item" v-if="record.lang">
              <span class="meta-key">Language</span>
              <span class="meta-val">{{ record.lang }}</span>
            </div>
            <div class="meta-item" v-if="record.cstr">
              <span class="meta-key">CSTR</span>
              <span class="meta-val">{{ record.cstr }}</span>
            </div>
            <div class="meta-item" v-if="record.rights">
              <span class="meta-key">Rights</span>
              <span class="meta-val">{{ record.rights }}</span>
            </div>
            <div class="meta-item" v-if="record.share">
              <span class="meta-key">Share Policy</span>
              <span class="meta-val">{{ record.share }}</span>
            </div>
          </div>
        </section>

        <!-- 分类 -->
        <section class="classification-section card" v-if="record.classification && record.classification.length">
          <h3 class="sub-title">Classification</h3>
          <div class="classification-list">
            <div v-for="(cls, idx) in record.classification" :key="idx" class="classification-item">
              <span class="classification-badge">{{ cls.first || cls.first_en }}</span>
              <span v-if="cls.second" class="classification-badge secondary">{{ cls.second }}</span>
            </div>
          </div>
        </section>

        <!-- 贡献者机构 -->
        <section class="affiliation-section card" v-if="record.contributor && record.contributor.length">
          <h3 class="sub-title">Contributors & Affiliations</h3>
          <ul class="affiliation-list">
            <li
              v-for="(contrib, idx) in record.contributor"
              :key="idx"
              class="affiliation-item"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              <div>
                <div class="contrib-name">{{ contrib.auth || contrib['auth-en'] }}</div>
                <div class="contrib-inst" v-if="contrib.institution && contrib.institution.name">
                  {{ contrib.institution.name || contrib.institution['name-en'] }}
                </div>
                <div class="contrib-email" v-if="contrib.email">{{ contrib.email }}</div>
              </div>
            </li>
          </ul>
        </section>

        <!-- 资助信息 -->
        <section class="funding-section card" v-if="record.funding && record.funding.length">
          <h3 class="sub-title">Funding</h3>
          <ul class="funding-list">
            <li
              v-for="(fund, idx) in record.funding"
              :key="idx"
              class="funding-item"
            >
              <div class="funding-name">{{ fund.name || fund.name_en }}</div>
              <div class="funding-id" v-if="fund.id">ID: {{ fund.id }}</div>
            </li>
          </ul>
        </section>

        <!-- 项目信息 -->
        <section class="project-section card" v-if="record.project && record.project.length">
          <h3 class="sub-title">Related Projects</h3>
          <ul class="project-list">
            <li
              v-for="(proj, idx) in record.project"
              :key="idx"
              class="project-item"
            >
              {{ proj.name }}
            </li>
          </ul>
        </section>

        <!-- DOI -->
        <section class="doi-section card" v-if="record.doi || record.url">
          <a
            v-if="record.url"
            :href="record.url"
            target="_blank"
            rel="noopener noreferrer"
            class="doi-link"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.72"/>
              <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.72-1.72"/>
            </svg>
            {{ record.doi || 'View Dataset' }}
          </a>
          <div v-else-if="record.doi" class="doi-text">
            DOI: {{ record.doi }}
          </div>
        </section>
      </div>
    </main>

    <!-- 底部占位 -->
    <footer class="reader-footer">
      <p>Additional visualization and related datasets coming soon…</p>
    </footer>
  </div>
</template>

<script>
import { SearchScienceDataDetail } from '@/service/ScienceDataSearch';
export default {
  data() {
    return {
      scienceData: {},
      record: {}
    };
  },
  created() {
    this.getScienceDataDetail();
  },
  methods: {
    async getScienceDataDetail() {
      const id = this.$route.params.id;
      const data = await SearchScienceDataDetail(id);
      if (data.error) {
        console.error(data.error);
        return;
      }
      this.scienceData = data.result.data;
      this.record = this.scienceData.record || {};
    }
  }
};
</script>

<style scoped lang="scss">
/* 全屏沉浸式布局 */
.fullscreen-reader {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7f1 100%);
  display: flex;
  flex-direction: column;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 顶部工具栏 */
.top-toolbar {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  padding: 12px 24px;
  gap: 12px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: none;
  font-size: 14px;
  color: #3b82f6;
  cursor: pointer;
  transition: color 0.2s;
  &:hover {
    color: #2563eb;
  }
}

.toolbar-divider {
  width: 1px;
  height: 20px;
  background: rgba(0, 0, 0, 0.08);
}

/* 主内容区 */
.reader-main {
  flex: 1;
  padding: 48px 24px;
  display: flex;
  justify-content: center;
}

.article-container {
  width: 100%;
  max-width: 840px;
}

/* 卡片通用样式 */
.card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  padding: 32px;
  margin-bottom: 24px;
}

/* 标题区 */
.hero-section {
  text-align: center;
  margin-bottom: 40px;
}

.article-title {
  font-size: 40px;
  font-weight: 700;
  line-height: 1.25;
  color: #111827;
  margin: 0 0 16px;
}

.authors-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
  margin-bottom: 16px;
}

.author-chip {
  background: #eef2ff;
  color: #4338ca;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

.journal-meta {
  font-size: 16px;
  color: #6b7280;
  .journal-name {
    font-weight: 600;
    color: #4b5563;
  }
  .meta-dot {
    margin: 0 6px;
  }
}

/* 摘要 */
.section-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 16px;
  color: #111827;
}

.abstract-body {
  font-size: 17px;
  line-height: 1.8;
  color: #374151;
}

/* 关键词 */
.sub-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 12px;
  color: #111827;
}

.keyword-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.keyword-pill {
  background: #ede9fe;
  color: #7e22ce;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

/* 索引信息 */
.meta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.meta-key {
  font-size: 13px;
  color: #6b7280;
}

.meta-val {
  font-size: 15px;
  color: #111827;
}

/* 分类 */
.classification-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.classification-item {
  display: flex;
  gap: 8px;
  align-items: center;
}

.classification-badge {
  background: #dbeafe;
  color: #1e40af;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  &.secondary {
    background: #e0e7ff;
    color: #3730a3;
  }
}

/* 机构 */
.affiliation-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.affiliation-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  font-size: 15px;
  color: #374151;
  svg {
    margin-top: 2px;
    flex-shrink: 0;
  }
}

.contrib-name {
  font-weight: 600;
  color: #111827;
  margin-bottom: 4px;
}

.contrib-inst {
  color: #6b7280;
  font-size: 14px;
}

.contrib-email {
  color: #3b82f6;
  font-size: 13px;
  margin-top: 2px;
}

/* 资助信息 */
.funding-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.funding-item {
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.funding-name {
  font-weight: 600;
  color: #111827;
  margin-bottom: 4px;
}

.funding-id {
  font-size: 13px;
  color: #6b7280;
}

/* 项目信息 */
.project-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.project-item {
  padding: 10px 16px;
  background: #f3f4f6;
  border-radius: 6px;
  color: #374151;
}

/* DOI */
.doi-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  color: #3b82f6;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
}

.doi-text {
  font-size: 15px;
  color: #374151;
}

/* 底部 */
.reader-footer {
  text-align: center;
  padding: 24px;
  font-size: 14px;
  color: #9ca3af;
}
</style>
