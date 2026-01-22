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
      <!-- <button class="tool-btn">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M18 20L12 14l6-6M6 4v16"/>
        </svg>
      </button>
      <button class="tool-btn">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M8 6h8M8 12h8M8 18h8"/>
        </svg>
      </button>
      <button class="tool-btn">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
        </svg>
      </button> -->
    </header>

    <!-- 主内容区 -->
    <main class="reader-main">
      <div class="article-container">
        <!-- 标题区 -->
        <section class="hero-section">
          <h1 class="article-title">{{ article.title }}</h1>
          <div class="authors-list">
            <span
              v-for="(au, idx) in article.author"
              :key="idx"
              class="author-chip"
            >
              {{ au }}
            </span>
          </div>
          <div class="journal-meta">
            <span class="journal-name">{{ article.journal }}</span>
            <span class="meta-dot">·</span>
            <span class="journal-year">{{ article.year }}</span>
            <span class="meta-dot">·</span>
            <span>Vol.{{ article.volume }} Issue{{ article.issue }}</span>
          </div>
        </section>

        <!-- 摘要 -->
        <section class="abstract-section card">
          <h2 class="section-title">Abstract</h2>
          <p class="abstract-body">{{ article.abstracts }}</p>
        </section>

        <!-- 关键词 -->
        <section class="keywords-section card">
          <h3 class="sub-title">Keywords</h3>
          <div class="keyword-cloud">
            <span
              v-for="(k, i) in article.keyword"
              :key="i"
              class="keyword-pill"
            >
              {{ k }}
            </span>
          </div>
        </section>

        <!-- 索引信息 -->
        <section class="metadata-section card">
          <h3 class="sub-title">Indexing Information</h3>
          <div class="meta-grid">
            <div class="meta-item">
              <span class="meta-key">ISSN</span>
              <span class="meta-val">{{ article.issn }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Type</span>
              <span class="meta-val">{{ article.type }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Research Area</span>
              <span class="meta-val">{{ article.subject.join(' · ') }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Publisher</span>
              <span class="meta-val">{{ article.publisher }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">License</span>
              <span class="meta-val">{{ article.license }}</span>
            </div>
            <div v-if="article.funding" class="meta-item">
              <span class="meta-key">Funding</span>
              <span class="meta-val">{{ article.funding }}</span>
            </div>
            <div v-if="article.collection && article.collection.length" class="meta-item">
              <span class="meta-key">Indexed in</span>
              <div class="index-badges">
                <span
                  v-for="(c, i) in article.collection"
                  :key="i"
                  class="index-badge"
                >
                  {{ c }}
                </span>
              </div>
            </div>
          </div>
        </section>

        <!-- 作者机构 -->
        <section class="affiliation-section card">
          <h3 class="sub-title">Author Affiliations</h3>
          <ul class="affiliation-list">
            <li
              v-for="(inst, idx) in article.institution"
              :key="idx"
              class="affiliation-item"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              {{ inst }}
            </li>
          </ul>
        </section>

        <!-- DOI -->
        <section class="doi-section card">
          <a
            :href="article.link"
            target="_blank"
            rel="noopener noreferrer"
            class="doi-link"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.72"/>
              <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.72-1.72"/>
            </svg>
            {{ article.doi }}
          </a>
        </section>
      </div>
    </main>

    <!-- 底部占位 -->
    <footer class="reader-footer">
      <p>Figures, citation network and recommended articles coming soon…</p>
    </footer>
  </div>
</template>

<script>
import { SearchArticleDetail } from '@/service/ArticleSerach';
export default {
  data() {
    return {
      article: {}
    };
  },
  created() {
    this.getArticleDetail();
  },
  methods: {
    async getArticleDetail() {
      const id = this.$route.params.id;
      const data = await SearchArticleDetail(id);
      if (data.error) {
        console.error(data.error);
        return;
      }
      this.article = data.result.data;
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

.tool-btn {
  display: grid;
  place-items: center;
  width: 32px;
  height: 32px;
  border: none;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 6px;
  color: #4b5563;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
  &:hover {
    background: rgba(0, 0, 0, 0.06);
    color: #111827;
  }
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

.index-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.index-badge {
  background: #f3f4f6;
  color: #374151;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 13px;
}

/* 机构 */
.affiliation-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.affiliation-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
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

/* 底部 */
.reader-footer {
  text-align: center;
  padding: 24px;
  font-size: 14px;
  color: #9ca3af;
}
</style>