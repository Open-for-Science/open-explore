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
            <span class="journal-name" v-if="article.journal">{{ article.journal }}</span>
            <span class="meta-dot" v-if="article.journal && article.year">·</span>
            <span class="journal-year" v-if="article.year">{{ article.year }}</span>
            <span class="meta-dot" v-if="article.year && volumeText">·</span>
            <span v-if="volumeText">{{ volumeText }}</span>
          </div>
        </section>

        <!-- 摘要 -->
        <section class="abstract-section card" v-if="article.abstracts">
          <h2 class="section-title">Abstract</h2>
          <p class="abstract-body">{{ article.abstracts }}</p>
        </section>

        <!-- 关键词 -->
        <section class="keywords-section card" v-if="article.keyword && article.keyword.length">
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
            <div class="meta-item" v-if="article.issn">
              <span class="meta-key">ISSN</span>
              <span class="meta-val">{{ article.issn }}</span>
            </div>
            <div class="meta-item" v-if="article.type">
              <span class="meta-key">Type</span>
              <span class="meta-val">{{ article.type }}</span>
            </div>
            <div class="meta-item" v-if="article.subject && article.subject.length">
              <span class="meta-key">Research Area</span>
              <span class="meta-val">{{ (article.subject || []).join(' · ') }}</span>
            </div>
            <div class="meta-item" v-if="article.publisher">
              <span class="meta-key">Publisher</span>
              <span class="meta-val">{{ article.publisher }}</span>
            </div>
            <div class="meta-item" v-if="article.license">
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
        <section class="affiliation-section card" v-if="article.institution && article.institution.length">
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
        <section class="doi-section card" v-if="article.link || article.doi">
          <a
            :href="article.link || ('https://doi.org/' + article.doi)"
            target="_blank"
            rel="noopener noreferrer"
            class="doi-link"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.72"/>
              <path d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.72-1.72"/>
            </svg>
            {{ article.doi || 'View original' }}
          </a>
        </section>
      </div>
    </main>

    <!-- 底部占位 -->
    <footer class="reader-footer">
      <p>Figures, citation network and recommended articles coming soon…</p>
    </footer>
    <ToTop />
  </div>
</template>

<script>
import { SearchArticleDetail } from '@/service/ArticleSerach';
import ToTop from '@/components/ToTop.vue';
export default {
  components: { ToTop },
  data() {
    return {
      article: {}
    };
  },
  computed: {
    // 卷期展示：只有存在值时才拼接，避免出现 "Vol.-" 之类的占位
    volumeText() {
      const vol = this.article.volume ? 'Vol.' + this.article.volume : '';
      const issue = this.article.issue ? 'Issue' + this.article.issue : '';
      return [vol, issue].filter(Boolean).join(' ');
    }
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
@import '~@/assets/detail.scss';
</style>
