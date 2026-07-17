<template>
  <!-- ============================================================ -->
  <!-- 首页主页面 HomePage                                               -->
  <!-- 包含：Hero 横幅区 / 统计栏 / 开放科学能力框架 / 四个头条模块 / 底部   -->
  <!-- ============================================================ -->
  <div class="HomePage">

    <!-- ============================================================ -->
    <!-- 1. Hero 横幅区（左侧：标题 + 搜索框 | 右侧：轮播图）                -->
    <!-- ============================================================ -->
    <section class="Hero__section">
      <!-- 背景装饰遮罩 -->
      <div class="Hero__overlay"></div>
      <div class="Hero__inner">

        <!-- ----- 左侧：文字内容 + 搜索框 ----- -->
        <div class="Hero__content">
          <h1 class="Hero__title">{{ $t('hero.title') }}</h1>
          <p class="Hero__subtitle">{{$t('hero.subtitle')}}</p>
          <p class="Hero__tagline">{{ $t('hero.tagline') }}</p>
          <div class="Hero__search">
            <input
              v-model="searchKeyword"
              type="text"
              :placeholder="$t('hero.searchPlaceholder')"
              @keyup.enter="goSearch"
            />
            <button class="Hero__searchBtn" @click="goSearch">
              <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
              </svg>
              <span>Search</span>
            </button>
          </div>
        </div>

        <!-- ----- 右侧：横幅轮播图 ----- -->
        <div class="Hero__carousel">
          <div
            class="Hero__carouselSlide"
            v-for="(slide, idx) in $t('homepage.bannerSlides')"
            :key="slide.id || idx"
            :class="{ 'Hero__carouselSlide--active': idx === carouselIdx }"
          >
            <img :src="slide.image" :alt="slide.title" />
            <div class="Hero__carouselOverlay">
              <h3 class="Hero__carouselTitle">{{ slide.title }}</h3>
              <p class="Hero__carouselDesc">{{ slide.description }}</p>
            </div>
          </div>
          <!-- 轮播图指示点 -->
          <div class="Hero__carouselDots">
            <span
              v-for="(slide, idx) in $t('homepage.bannerSlides')"
              :key="'dot-' + idx"
              class="Hero__carouselDot"
              :class="{ 'Hero__carouselDot--active': idx === carouselIdx }"
              @click="carouselIdx = idx"
            ></span>
          </div>
        </div>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- 2. 统计栏（四个统计数字，前两个后端动态查询，后两个硬编码）       -->
    <!-- ============================================================ -->
    <section class="Stats__section">
      <div class="Stats__inner">
        <!-- 统计项 1：文章总量（动态） -->
        <div class="Stats__item">
          <span class="Stats__num">{{ formatNumber(statsData.articleTotal) }}</span>
          <span class="Stats__label">{{ $t('stats.articleTotal') }}</span>
        </div>
        <div class="Stats__divider"></div>
        <!-- 统计项 2：科学数据总量（动态） -->
        <div class="Stats__item">
          <span class="Stats__num">{{ formatNumber(statsData.scienceDataTotal) }}</span>
          <span class="Stats__label">{{ $t('stats.scienceDataTotal') }}</span>
        </div>
        <div class="Stats__divider"></div>
        <!-- 统计项 3：XX 总量（硬编码） -->
        <div class="Stats__item">
          <span class="Stats__num">{{ statsData.softwareTotal }}</span>
          <span class="Stats__label">{{ $t('stats.xxTotal1') }}</span>
        </div>
        <div class="Stats__divider"></div>
        <!-- 统计项 4：XX 总量（硬编码） -->
        <div class="Stats__item">
          <span class="Stats__num">{{ statsData.facilityTotal }}</span>
          <span class="Stats__label">{{ $t('stats.xxTotal2') }}</span>
        </div>
        <div class="Stats__divider"></div>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- 3. 开放科学能力框架（6 张卡片，3x2 网格布局）                    -->
    <!--     数据来自 /data/homepage.json → resourceTypes               -->
    <!-- ============================================================ -->
    <section class="Resources__section">
      <div class="Section__header">
        <h2>{{ $t('resources.title') }}</h2>
        <p>{{ $t('resources.subtitle') }}</p>
      </div>
      <div class="Resources__grid">
        <!-- 遍历 resourceTypes 渲染卡片 -->
        <div
          class="Resource__card"
          v-for="(item, idx) in $t('homepage.resourceTypes')"
          :key="item.id"
          :style="{ '--card-delay': idx * 0.05 + 's' }"
        >
          <div class="Resource__cardTitle">{{ $t( item.biaoti ) }}</div>
          <div class="Resource__cardDesc">{{ $t(item.xiangqing) }}</div>
        </div>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- 4. 文献模块 #1（最新文献列表 — 居中显示）                    -->
    <!--     数据来自后端接口 /article/v1/latest                        -->
    <!-- ============================================================ -->
    <section id="highlights" data-screen-label="Resources Highlights" class="Headlines__section">
      <div class="glow-b"></div>
      <div class="wrap">
        <div class="resources-headline-meta">
          <div class="sec-eyebrow sec-eyebrow--center">
            {{ $t('headlines.prefix') }} <span class="grad">{{ $t('headlines.prefixHighlight') }}</span>
          </div>
        </div>
        <div class="resources-section-title-wrap">
          <h2 class="sec-title resources-section-title">{{ $t('headlines.title') }}</h2>
          <p class="contact-line">{{ $t('headlines.contact') }}<a :href="'mailto:' + $t('contactEmail')">{{ $t('contactEmail') }}</a></p>
        </div>
        <!-- 居中展示的文献卡片（无右侧图片网格） -->
        <div class="headlines-panel headlines-panel--centered">
          <div class="hl-text-card hl-text-card--centered">
            <div class="hl-group-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right:6px;vertical-align:middle"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
              {{ $t('headlines.zongjie') }}
            </div>
            <div class="hl-date">{{ currentDate }}</div>
            <!-- 从左到右：序号 | 文献标题（竖排卡片风格，hover 向右移动） -->
            <div class="hl-row-list">
              <div
                class="hl-card-item"
                v-for="(art, idx) in latestArticles"
                :key="idx"
                @click="$router.push('/article/detail/' + art.uuid)"
              >
                <span class="hl-card-index">{{ (idx + 1).toString().padStart(2, '0') }}</span>
                <div class="hl-card-body">
                  <h4 class="hl-card-title">{{ art.title || art.journal || $t('headlines.more') }}</h4>
                  <div class="hl-card-meta">
                    <span class="hl-card-tag" v-if="art.type">{{ art.type }}</span>
                    <span class="hl-card-meta-item" v-if="art.author && art.author.length">{{ art.author.slice(0,2).join(', ') }}</span>
                    <span class="hl-card-meta-item" v-if="art.year || art['pub-year']">{{ art.year || art['pub-year'] }}</span>
                  </div>
                </div>
              </div>
            </div>
            <router-link to="/search" class="site-btn hl-detail-btn hl-detail-btn--centered">
              {{ $t('headlines.more') }}
              <svg width="14" height="12" viewBox="0 0 14 12" fill="none">
                <path d="M1 6h11M8.5 1.5L13 6l-4.5 4.5" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- 5. 科学数据 #2（动态数据，同模块 #1 文献样式）               -->
    <!--     数据来自后端接口 /searchApi/sciencedata/v1/latest, size=5      -->
    <!--     展示最新科学数据，hover 向右位移，点击跳详情页                -->
    <!-- ============================================================ -->
    <section id="highlights-2" data-screen-label="Science Data Highlights" class="Headlines__section">
      <div class="glow-b"></div>
      <div class="wrap">
        <div class="resources-headline-meta">
          <div class="sec-eyebrow sec-eyebrow--center">
            {{ $t('headlines2.prefix') }} <span class="grad">{{ $t('headlines2.prefixHighlight') }}</span>
          </div>
        </div>
        <div class="resources-section-title-wrap">
          <h2 class="sec-title resources-section-title">{{ $t('headlines2.title') }}</h2>
          <p class="contact-line">{{ $t('headlines2.contact') }}<a :href="'mailto:' + $t('contactEmail')">{{ $t('contactEmail') }}</a></p>
        </div>
        <div class="headlines-panel headlines-panel--centered">
          <!-- 居中：序号 + 标题（竖排卡片风格，同文献模块） -->
          <div class="hl-text-card hl-text-card--centered">
            <div class="hl-group-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right:6px;vertical-align:middle"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2" stroke="currentColor"/></svg>
              {{ $t('headlines2.zongjie') }}
            </div>
            <div class="hl-date">{{ currentDate }}</div>
            <div class="hl-row-list">
              <div
                class="hl-card-item"
                v-for="(item, idx) in latestScienceData"
                :key="'sc2-' + idx"
                @click="goScienceData(item.uuid)"
              >
                <span class="hl-card-index">{{ (idx + 1).toString().padStart(2, '0') }}</span>
                <div class="hl-card-body">
                  <h4 class="hl-card-title">{{ item.title || item['title-en'] || $t('headlines2.more') }}</h4>
                  <div class="hl-card-meta">
                    <span class="hl-card-tag hl-card-tag--green" v-if="item['dataset-type']">{{ item['dataset-type'] }}</span>
                    <span class="hl-card-meta-item" v-if="item.author && item.author.length">{{ item.author.slice(0,2).join(', ') }}</span>
                    <span class="hl-card-meta-item" v-if="item['pub-year']">{{ item['pub-year'] }}</span>
                  </div>
                </div>
              </div>
            </div>
            <router-link to="/search?tab=sciencedata" class="site-btn hl-detail-btn hl-detail-btn--centered">
              {{ $t('headlines2.more') }}
              <svg width="14" height="12" viewBox="0 0 14 12" fill="none">
                <path d="M1 6h11M8.5 1.5L13 6l-4.5 4.5" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </router-link>
          </div>
        </div>
      </div>
    </section>
    <!-- ============================================================ -->
    <!-- 6. 科学软件模块 #3（居中：序号 + 标题，同科学数据模块样）            -->
    <!--     数据来自后端接口 /searchApi/software/v1/latest, size=3          -->
    <!--     展示最新 3 条软件，hover 向右位移，点击跳详情页                -->
    <!-- ============================================================ -->
    <section id="highlights-3" data-screen-label="Software Highlights" class="Headlines__section">
      <div class="glow-b"></div>
      <div class="wrap">
        <div class="resources-headline-meta">
          <div class="sec-eyebrow sec-eyebrow--center">
            {{ $t('headlines3.prefix') }} <span class="grad">{{ $t('headlines3.prefixHighlight') }}</span>
          </div>
        </div>
        <div class="resources-section-title-wrap">
          <h2 class="sec-title resources-section-title">{{ $t('headlines3.title') }}</h2>
          <p class="contact-line">{{ $t('headlines3.contact') }}<a :href="'mailto:' + $t('contactEmail')">{{ $t('contactEmail') }}</a></p>
        </div>
        <div class="headlines-panel headlines-panel--centered">
          <div class="hl-text-card hl-text-card--centered">
            <div class="hl-group-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right:6px;vertical-align:middle"><rect x="3" y="3" width="18" height="18" rx="2"/><path d="M12 8v8M8 12h8" stroke="currentColor"/></svg>
              {{ $t('headlines3.zongjie') }}
            </div>
            <div class="hl-date">{{ currentDate }}</div>
            <div class="hl-row-list">
              <div
                class="hl-card-item"
                v-for="(item, idx) in latestSoftware"
                :key="'sw-' + idx"
                @click="$router.push('/software/detail/' + item.uuid)"
              >
                <span class="hl-card-index">{{ (idx + 1).toString().padStart(2, '0') }}</span>
                <div class="hl-card-body">
                  <h4 class="hl-card-title">{{ item.title || item['title-en'] || $t('headlines3.more') }}</h4>
                  <div class="hl-card-meta">
                    <span class="hl-card-tag" v-if="item.category">{{ item.category }}</span>
                    <span class="hl-card-tag hl-card-tag--orange" v-if="item.license">{{ item.license }}</span>
                    <span class="hl-card-meta-item" v-if="item.organization">{{ item.organization }}</span>
                    <span class="hl-card-meta-item" v-if="item['pub-year']">{{ item['pub-year'] }}</span>
                  </div>
                </div>
              </div>
            </div>
            <router-link to="/search?tab=software" class="site-btn hl-detail-btn hl-detail-btn--centered">
              {{ $t('headlines3.more') }}
              <svg width="14" height="12" viewBox="0 0 14 12" fill="none">
                <path d="M1 6h11M8.5 1.5L13 6l-4.5 4.5" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </router-link>
          </div>
        </div>
      </div>
    </section>
    <!-- ============================================================ -->
    <!-- 7. 大型科学装置模块 #4（左侧：文字列表 + 右侧：图片网格）       -->
    <!--     数据来自后端接口 /searchApi/facility/v1/latest, size=3          -->
    <!--     展示最新 3 条装置（左文右图），点击跳详情页                   -->
    <!-- ============================================================ -->
    <section id="highlights-4" data-screen-label="Facility Highlights" class="Headlines__section">
      <div class="glow-b"></div>
      <div class="wrap">
        <div class="resources-headline-meta">
          <div class="sec-eyebrow sec-eyebrow--center">
            {{ $t('headlines4.prefix') }} <span class="grad">{{ $t('headlines4.prefixHighlight') }}</span>
          </div>
        </div>
        <div class="resources-section-title-wrap">
          <h2 class="sec-title resources-section-title">{{ $t('headlines4.title') }}</h2>
          <p class="contact-line">{{ $t('headlines4.contact') }}<a :href="'mailto:' + $t('contactEmail')">{{ $t('contactEmail') }}</a></p>
        </div>
        <div class="headlines-panel">
          <!-- 左侧：文字列表（序号 + 标题 + 跳转） -->
          <div class="hl-text-card">
            <div class="hl-group-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right:6px;vertical-align:middle"><circle cx="12" cy="12" r="3"/><path d="M12 2v4M12 18v4M2 12h4M18 12h4" stroke="currentColor"/></svg>
              {{ $t('headlines4.zongjie') }}
            </div>
            <div class="hl-date">{{ currentDate }}</div>
            <div class="hl-row-list">
              <div
                class="hl-card-item"
                v-for="(item, idx) in latestFacility"
                :key="'fc-' + idx"
                @click="$router.push('/facility/detail/' + item.uuid)"
              >
                <span class="hl-card-index">{{ (idx + 1).toString().padStart(2, '0') }}</span>
                <div class="hl-card-body">
                  <h4 class="hl-card-title">{{ item.title || item['title-en'] || $t('headlines4.more') }}</h4>
                  <div class="hl-card-meta">
                    <span class="hl-card-tag" v-if="item['facility-type']">{{ item['facility-type'] }}</span>
                    <span class="hl-card-tag hl-card-tag--green" v-if="item['facility-status']">{{ item['facility-status'] }}</span>
                    <span class="hl-card-meta-item" v-if="item['facility-location']">{{ item['facility-location'] }}</span>
                    <span class="hl-card-meta-item" v-if="item['hosting_institution']">{{ item['hosting_institution'] }}</span>
                  </div>
                </div>
              </div>
            </div>
            <router-link to="/search?tab=facility" class="site-btn hl-detail-btn">
              {{ $t('headlines4.more') }}
              <svg width="14" height="12" viewBox="0 0 14 12" fill="none">
                <path d="M1 6h11M8.5 1.5L13 6l-4.5 4.5" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </router-link>
          </div>
          <!-- 右侧：图片网格（从装置数据中提取图片） -->
          <div class="hl-figures">
            <div class="figure-card" v-for="(item, idx) in latestFacility" :key="'fig-' + idx" @click="$router.push('/facility/detail/' + item.uuid)">
              <img :src="item.image" :alt="item.title" loading="lazy" width="640" height="360" class="figure-img">
              <div class="figure-cap">
                <span class="cap-txt">{{ item.title }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>


    <!-- ============================================================ -->
    <!-- 8. 页脚（品牌信息 / 联系方式 / 版权）                            -->
    <!--     数据来自 homepage.json → footer                            -->
    <!-- ============================================================ -->
    <footer class="Footer__section">
      <div class="Footer__inner">
        <div class="Footer__brand">
          <h3 class="Footer__logo">{{ $t('homepage.footer.brandName') }}</h3>
          <p class="Footer__orgName">{{ $t('homepage.footer.institutionName') }}</p>
        </div>
        <div class="Footer__contact">
          <h4 class="Footer__contactTitle">{{ $t('footer.contactUs') }}</h4>
          <div class="Footer__contactItem">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>
            <span>{{ $t('homepage.footer.address') }}</span>
          </div>
          <div class="Footer__contactItem">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>
            <a :href="'mailto:' + $t('homepage.footer.email')">{{ $t('homepage.footer.email') }}</a>
          </div>
        </div>
        <div class="Footer__copyright">{{ $t('footer.copyright') }}</div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  // ========================================================== //
  // 组件数据定义                                                //
  // ========================================================== //
  data() {
    return {
      homepage: {
        bannerSlides: [],
        resourceTypes: []
      },
      searchKeyword: '',         // 搜索框输入的关键词
      // ----- 统计栏数据（来自后端 /stats/v1/totals）----- //
      statsData: { articleTotal: 0, scienceDataTotal: 0 },
      latestArticles: [],        // 最新文章列表（来自 /article/v1/latest）
      latestScienceData: [],     // 最新科学数据列表（来自 /sciencedata/v1/latest）
      latestSoftware: [],        // 最新科学软件列表（来自 /software/v1/latest）
      latestFacility: [],        // 最新科学装置列表（来自 /facility/v1/latest）
      carouselIdx: 0,            // 轮播图当前索引
      carouselTimer: null,       // 轮播定时器句柄
      currentDate: ''            // 格式化后的当前日期
    }
  },
  // ========================================================== //
  // 生命周期钩子                                                //
  // ========================================================== //
  // 组件创建时加载首页数据
  created() {
    this.loadHomepage()
  },
  // 组件销毁前停止轮播定时器，防止内存泄漏
  beforeDestroy() {
    this.stopCarousel()
  },

  // ========================================================== //
  // 方法定义                                                   //
  // ========================================================== //
  methods: {
    /**
     * 加载首页所有数据
     * 并行请求：
     *   1. /data/homepage.json  →  静态配置（轮播、卡片、头条等）
     *   2. /searchApi/stats/v1/totals  →  统计数字（文章/科学数据总量）
     *   3. /searchApi/article/v1/latest?size=3  →  最新 3 篇文章
     *   4. /searchApi/sciencedata/v1/latest?size=3  →  最新 3 条科学数据
     *   5. /searchApi/software/v1/latest?size=3   →  最新 3 条软件
     *   6. /searchApi/facility/v1/latest?size=3   →  最新 3 条装置
     */
    async loadHomepage() {
      try {
        const [statsRes, articleRes, scienceRes, softwareRes, facilityRes] = await Promise.all([
          fetch('/searchApi/stats/v1/totals'),
          fetch('/searchApi/article/v1/latest?size=3'),
          fetch('/searchApi/sciencedata/v1/latest?size=3'),
          fetch('/searchApi/software/v1/latest?size=3'),
          fetch('/searchApi/facility/v1/latest?size=3')
        ])
        if (statsRes.ok) {
          const statsJson = await statsRes.json()
          if (statsJson.data) {
            this.statsData = statsJson.data
          }
        }
        if (articleRes.ok) {
          const articleJson = await articleRes.json()
          if (articleJson.data && articleJson.data.content) {
            this.latestArticles = articleJson.data.content
          }
        }
        if (scienceRes.ok) {
          const scienceJson = await scienceRes.json()
          if (scienceJson.data && scienceJson.data.content) {
            this.latestScienceData = scienceJson.data.content
          }
        }
        if (softwareRes.ok) {
          const softwareJson = await softwareRes.json()
          if (softwareJson.data && softwareJson.data.content) {
            this.latestSoftware = softwareJson.data.content
          }
        }
        if (facilityRes.ok) {
          const facilityJson = await facilityRes.json()
          if (facilityJson.data && facilityJson.data.content) {
            this.latestFacility = facilityJson.data.content
          }
        }
        this.currentDate = this.getFormattedDate()
        this.$nextTick(this.startCarousel)
      } catch (e) {
        console.error('Failed to load homepage data:', e)
      }
    },
    /**
     * 格式化数字为千分位逗号分隔
     * 例：1234567 → "1,234,567"
     */
    formatNumber(num) {
      if (!num && num !== 0) return '0'
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    /**
     * 返回格式化的当前日期（中文格式）
     * 例：2026年07月14日
     */
    /**
     * 跳转到科学数据详情页
     */
    goScienceData(uuid) {
      if (uuid) {
        this.$router.push('/sciencedata/detail/' + uuid)
      }
    },
    getFormattedDate() {
      const d = new Date()
      const y = d.getFullYear()
      const m = d.getMonth() + 1
      const day = d.getDate()
      return y + '年' + (m < 10 ? '0' + m : m) + '月' + (day < 10 ? '0' + day : day) + '日'
    },
    /**
     * 搜索按钮/回车事件：携带关键词跳转到文章搜索页
     */
    goSearch() {
      const kw = this.searchKeyword.trim()
      if (kw) {
        this.$router.push({ path: '/search', query: { query: kw } })
      }
    },
    /**
     * 启动 Hero 轮播图自动播放（每 4.5 秒切换一帧）
     */
    startCarousel() {
      const slides = this.$t('homepage.bannerSlides')
      this.stopCarousel()
      if (slides && slides.length > 1) {
        this.carouselTimer = setInterval(() => {
          this.carouselIdx = (this.carouselIdx + 1) % slides.length
        }, 4500)
      }
    },
    /**
     * 停止轮播自动播放
     */
    stopCarousel() {
      if (this.carouselTimer) {
        clearInterval(this.carouselTimer)
        this.carouselTimer = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
$primary: #2F3A91;
$primaryDark: #2A3D76;
$textDark: #1a1a2e;
$textGray: #6c757d;

.HomePage {
  width: 100%;
  min-height: 100%;
  overflow-x: hidden;
}

/* ===================== 通用区块标题 ===================== */
.Section__header {
  text-align: center;
  margin-bottom: 36px;
  h2 {
    font-size: 28px;
    font-weight: 700;
    color: $textDark;
    margin: 0 0 12px;
    letter-spacing: 0.5px;
  }
  p {
    font-size: 16px;
    color: $textGray;
    margin: 0;
    max-width: 600px;
    margin: 0 auto;
    line-height: 1.6;
  }
}

/* ===================== Hero 横幅区（左侧内容 + 右侧轮播） ===================== */
.Hero__section {
  position: relative;
  width: 100%;
  min-height: 460px;
  background: linear-gradient(135deg, $primary 0%, $primaryDark 50%, #1b2a4a 100%);
  display: flex;
  align-items: center;
  overflow: hidden;
}
.Hero__overlay {
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Ccircle cx='30' cy='30' r='30'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  pointer-events: none;
}
.Hero__inner {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 1300px;
  margin: 0 auto;
  padding: 50px 40px;
  display: flex;
  align-items: center;
  gap: 48px;
}
.Hero__content {
  flex: 1;
  min-width: 0;
}
.Hero__title {
  font-size: 44px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 2px;
  margin: 0 0 12px;
}
.Hero__subtitle {
  font-size: 18px;
  color: rgba(255,255,255,0.78);
  line-height: 1.7;
  margin: 0 0 6px;
}
.Hero__tagline {
  font-size: 14px;
  color: rgba(255,255,255,0.55);
  letter-spacing: 2px;
  text-transform: uppercase;
  margin: 0 0 32px;
}
.Hero__search {
  display: flex;
  max-width: 520px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  input {
    flex: 1;
    border: none;
    outline: none;
    padding: 16px 20px;
    font-size: 16px;
    color: #333;
    &::placeholder {
      color: #aaa;
    }
  }
}
.Hero__searchBtn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0 28px;
  background: $primary;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
  &:hover {
    background: darken($primary, 8%);
  }
}

/* ===================== Hero 右侧轮播图 ===================== */
.Hero__carousel {
  width: 420px;
  flex-shrink: 0;
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0,0,0,0.25);
  aspect-ratio: 16 / 10;
}
.Hero__carouselSlide {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity 0.6s ease;
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
  &--active {
    opacity: 1;
  }
}
.Hero__carouselOverlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 24px 20px 36px;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: #fff;
}
.Hero__carouselTitle {
  font-size: 16px;
  font-weight: 700;
  margin: 0 0 4px;
}
.Hero__carouselDesc {
  font-size: 13px;
  color: rgba(255,255,255,0.8);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.Hero__carouselDots {
  position: absolute;
  bottom: 12px;
  right: 16px;
  display: flex;
  gap: 6px;
}
.Hero__carouselDot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255,255,255,0.4);
  cursor: pointer;
  transition: background 0.2s;
  &--active {
    background: #fff;
    transform: scale(1.2);
  }
}

/* ===================== 统计栏 ===================== */
.Stats__section {
  background: #fff;
  border-bottom: 1px solid #eee;
}
.Stats__inner {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1000px;
  margin: 0 auto;
  padding: 32px 24px;
  gap: 12px;
}
.Stats__item {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 120px;
}
.Stats__num {
  font-size: 28px;
  font-weight: 800;
  color: $primary;
}
.Stats__label {
  font-size: 13px;
  color: $textGray;
  margin-top: 4px;
}
.Stats__divider {
  width: 1px;
  height: 48px;
  background: #e0e0e0;
}

/* ===================== 开放科学能力框架（居中对齐 3x2 网格） ===================== */
.Resources__section {
  padding: 80px 24px;
  background: #f8f9fc;
  text-align: center;
}
.Resources__grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
  text-align: left;
}
.Resource__card {
  background: #fff;
  border-radius: 10px;
  padding: 24px 20px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.05);
  transition: transform 0.25s, box-shadow 0.25s;
  animation: fadeInUp 0.5s ease both;
  animation-delay: var(--card-delay);
  border: 1px solid #eee;
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(47,58,145,0.10);
    border-color: lighten($primary, 40%);
  }
}
.Resource__cardIcon {
  font-size: 15px;
  margin-bottom: 12px;
}
.Resource__cardTitle {
  font-size: 16px;
  font-weight: 700;
  color: $textDark;
  margin-bottom: 6px;
}
.Resource__cardDesc {
  font-size: 13px;
  color: $textGray;
  line-height: 1.6;
  margin-bottom: 10px;
}
.Resource__cardCount {
  font-size: 12px;
  color: $primary;
  .count {
    font-weight: 800;
    font-size: 15px;
  }
}

/* ===================== 头条/亮点模块（os10 风格） ===================== */
.Headlines__section {
  position: relative;
  padding: 60px 24px;
  background: #f5f7fa;
  overflow: hidden;
}
.glow-b {
  position: absolute;
  top: -200px;
  left: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(47,58,145,0.04) 0%, transparent 70%);
  pointer-events: none;
}
.wrap {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 模块标签："资源" → "亮点" */
.resources-headline-meta {
  margin-bottom: 8px;
  text-align: center;
}
.sec-eyebrow {
  font-size: 14px;
  font-weight: 500;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 2px;
}
.sec-eyebrow .grad {
  background: linear-gradient(90deg, $primary, #6c7bcf);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 标题栏 + 联系邮箱行 */
.resources-section-title-wrap {
  text-align: center;
  margin-bottom: 36px;
}
.sec-title {
  font-size: 30px;
  font-weight: 800;
  color: $textDark;
  margin: 0 0 4px;
  letter-spacing: 2px;
}
.contact-line {
  font-size: 13px;
  color: #999;
  margin: 0;
  a {
    color: $primary;
    text-decoration: underline;
  }
}

/* 主面板：左侧文字卡片 + 右侧图片网格 */
.headlines-panel {
  display: flex;
  gap: 28px;
  align-items: stretch;
  &--flip {
    flex-direction: row-reverse;
  }
  /* 纯文字面板（无右侧图片时居中） */
  &--centered {
    justify-content: center;
  }
}

/* 左侧：文字卡片 */
.hl-text-card {
  flex: 0 0 420px;
  display: flex;
  flex-direction: column;
  padding: 0;
  /* 居中版（无右侧图片时文字居中） */
  &--centered {
    margin: 0 auto;
    text-align: center;
    .hl-group-title {
      justify-content: center;
    }
  }
}
.hl-group-title {
  font-size: 18px;
  font-weight: 700;
  color: $textDark;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
  .news-category-icon {
    width: 24px;
    height: 24px;
    border-radius: 4px;
    object-fit: cover;
  }
}
.hl-date {
  font-size: 12px;
  color: #aaa;
  margin-bottom: 16px;
}

/* 序号 + 标题竖排卡片（模块 #1 居中样式） */
.hl-row-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0;
}
.hl-card-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  width: 100%;
  max-width: 520px;
  padding: 14px 20px;
  border-bottom: 1px solid #eef1f4;
  transition: all 0.25s ease;
  cursor: pointer;
  text-align: left;
  &:hover {
    transform: translateX(4px);
    background: rgba(47,58,145,0.03);
    border-radius: 8px;
    .hl-card-title {
      color: $primary;
    }
  }
}
.hl-card-index {
  font-size: 13px;
  font-weight: 800;
  color: $primary;
  min-width: 28px;
  padding-top: 2px;
  text-align: center;
  flex-shrink: 0;
}
.hl-card-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.hl-card-title {
  font-size: 14px;
  font-weight: 600;
  color: $textDark;
  margin: 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.2s;
}
.hl-card-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px 10px;
  margin-top: 2px;
}
.hl-card-tag {
  display: inline-block;
  padding: 1px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 500;
  background: #eef2ff;
  color: #2F3A91;
  &--green {
    background: #e4f7e9;
    color: #0c833e;
  }
  &--orange {
    background: #fff3e0;
    color: #b85c00;
  }
}
.hl-card-meta-item {
  font-size: 11px;
  color: #8e96ab;
  white-space: nowrap;
}
.hl-list {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1;
  li {
    padding: 10px 0;
    border-bottom: 1px solid #f3f3f3;
    font-size: 14px;
    line-height: 1.6;
    color: $textDark;
    cursor: pointer;
    transition: color 0.15s;
    &:hover {
      color: $primary;
    }
    &:last-child {
      border-bottom: none;
    }
  }
}
.site-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-top: 14px;
  padding: 10px 24px;
  background: $primary;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  cursor: pointer;
  transition: background 0.2s, transform 0.15s;
  align-self: flex-start;
  &:hover {
    background: darken($primary, 8%);
    transform: translateX(2px);
  }
  svg {
    transition: transform 0.2s;
  }
  &:hover svg {
    transform: translateX(3px);
  }
  /* 居中版按钮 */
  &--centered {
    align-self: center;
  }
}

/* 右侧：图片网格 */
.hl-figures {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  align-content: start;
}
.figure-card {
  position: relative;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  &:first-child {
    grid-column: 1 / -1;
  }
  &:first-child .figure-img {
    height: 220px;
  }
  &:hover .figure-img {
    transform: scale(1.04);
  }
}
.figure-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  display: block;
  transition: transform 0.3s;
}
.figure-cap {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px 14px;
  background: linear-gradient(transparent, rgba(0,0,0,0.6));
}
.cap-txt {
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 0.5px;
}



/* ===================== 页脚 ===================== */
.Footer__section {
  background: #1b2a4a;
  color: rgba(255,255,255,0.8);
  padding: 48px 24px 32px;
}
.Footer__inner {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 32px 60px;
  justify-content: space-between;
}
.Footer__logo {
  font-size: 22px;
  font-weight: 800;
  color: #fff;
  margin: 0 0 4px;
  letter-spacing: 1px;
}
.Footer__orgName {
  font-size: 13px;
  color: rgba(255,255,255,0.6);
  margin: 0;
}
.Footer__contact {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.Footer__contactTitle {
  font-size: 15px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 2px;
}
.Footer__contactItem {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: rgba(255,255,255,0.7);
  svg {
    flex-shrink: 0;
    opacity: 0.5;
  }
  a {
    color: rgba(255,255,255,0.7);
    text-decoration: none;
    &:hover {
      color: #fff;
      text-decoration: underline;
    }
  }
}
.Footer__copyright {
  width: 100%;
  border-top: 1px solid rgba(255,255,255,0.1);
  padding-top: 20px;
  margin-top: 8px;
  font-size: 13px;
  color: rgba(255,255,255,0.4);
  text-align: center;
}

/* ===================== 动画 ===================== */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(24px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(-12px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* ===================== 响应式适配 ===================== */
@media (max-width: 1024px) {
  .Hero__inner {
    flex-direction: column;
    text-align: center;
  }
  .Hero__content {
    max-width: 100%;
  }
  .Hero__search {
    margin: 0 auto;
  }
  .Hero__carousel {
    width: 100%;
    max-width: 480px;
  }
  .Resources__grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .headlines-panel {
    flex-direction: column;
  }
  .hl-text-card {
    flex: none;
    width: 100%;
  }
}
@media (max-width: 768px) {
  .Hero__inner {
    padding: 40px 24px;
  }
  .Hero__title { font-size: 28px; }
  .Stats__inner { flex-wrap: wrap; }
  .Stats__divider { display: none; }
  .Resources__grid { grid-template-columns: 1fr; }
  .Section__header h2 { font-size: 22px; }
}
</style>
