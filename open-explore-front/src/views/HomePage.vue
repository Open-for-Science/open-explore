<template>
  <div class="HomePage" :lang="$i18n.locale">
    <main>
      <div class="page-atmosphere" aria-hidden="true">
        <svg class="page-waves page-waves--hero" viewBox="0 0 1600 750" preserveAspectRatio="none">
          <path d="M0 0h280C190 120 100 130 0 140Z" fill="white" opacity=".65" />
          <path d="M1600 0v280C1470 390 1390 440 1130 466c262-80 405-203 470-466Z" fill="white" opacity=".5" />
          <path d="M0 460c260 50 370 190 750 255C360 680 140 570 0 580Z" fill="#c5d8ff" opacity=".4" />
          <path d="M0 490c270 44 360 170 750 225" fill="none" stroke="white" opacity=".75" />
          <path d="M1600 260c-130 160-260 207-470 206" fill="none" stroke="white" opacity=".85" />
        </svg>
        <svg class="page-waves page-waves--middle" viewBox="0 0 1600 430" preserveAspectRatio="none">
          <path d="M0 130C190 0 300 250 540 200S850 90 1010 140s320-110 440-100 150 70 150 70v320H0Z" fill="#dfe9ff" opacity=".4" />
          <path d="M0 145C250 15 300 270 650 180s400-20 650-90 200-10 300 10" fill="none" stroke="white" opacity=".45" />
        </svg>
      </div>
      <section class="hero" aria-labelledby="home-title">
        <div class="hero__grid page-width">
          <div class="hero__content">
            <div class="hero__eyebrow"><ResourceIcon name="spark" />{{ $t('home.eyebrow') }}</div>
            <h1 id="home-title">{{ $t('home.title') }}<span>{{ $t('home.titleAccent') }}</span></h1>
            <p class="hero__intro">{{ $t('home.intro') }}</p>
            <div class="discovery-search">
              <div class="search-tabs" role="group" :aria-label="$t('home.discovery')">
                <button v-for="resource in resources" :key="resource.key" type="button" :class="{ active: searchType === resource.key }" :aria-pressed="searchType === resource.key ? 'true' : 'false'" @click="searchType = resource.key">
                  <ResourceIcon :name="resource.key" /><span>{{ resource.label }}</span>
                </button>
              </div>
              <form class="search-field" role="search" @submit.prevent="goSearch()">
                <ResourceIcon name="search" />
                <input id="home-search" v-model="searchKeyword" type="search" :aria-label="$t('home.placeholder')" :placeholder="$t('home.placeholder')" autocomplete="off" />
                <button type="submit">{{ $t('home.search') }}</button>
              </form>
            </div>
            <div class="hero__topics">
              <span>{{ $t('home.topicsLabel') }}</span>
              <button v-for="topic in $t('home.topics')" :key="topic" type="button" @click="goSearch(topic)">{{ topic }}</button>
            </div>
          </div>
          <div class="hero-art" aria-hidden="true">
            <span class="hero-art__cross hero-art__cross--top">+</span>
            <span class="hero-art__cross hero-art__cross--bottom">+</span>
            <img class="hero-art__atom" src="@/assets/generated/science-atom.webp" alt="" width="1254" height="1254" fetchpriority="high" />
            <div v-for="resource in resources" :key="resource.key" class="art-label" :class="'art-label--' + resource.key">
              <span class="resource-icon" :class="'resource-icon--' + resource.key"><ResourceIcon :name="resource.key" /></span>
              <span>{{ resource.label }}</span>
            </div>
            <div class="hero-art__caption"><span></span>{{ $t('home.artCaption') }}</div>
          </div>
        </div>
      </section>
      <section class="stats-band" :aria-label="$t('home.discovery')">
        <div class="stats-band__inner page-width">
          <div v-for="resource in resources" :key="resource.key" class="stat">
            <ResourceIcon :name="resource.key" />
            <div><strong>{{ formatNumber(stats[resource.stat]) }}</strong><span>{{ resource.label }}</span></div>
          </div>
        </div>
      </section>
      <section class="resource-section page-width" aria-labelledby="resources-title">
        <div class="section-heading">
          <div>
            <p class="eyebrow">{{ $t('home.resourceEyebrow') }}</p>
            <h2 id="resources-title">{{ $t('home.resourceTitle') }}</h2>
            <p class="section-intro">{{ $t('home.resourceIntro') }}</p>
          </div>
          <router-link class="text-link" to="/search">{{ $t('home.browseAll') }}<ResourceIcon name="arrow" /></router-link>
        </div>
        <div class="resource-grid">
          <router-link v-for="resource in resources" :key="resource.key" class="resource-card" :class="'resource-card--' + resource.key" :to="searchRoute(resource.key)">
            <div class="resource-card__top">
              <span class="resource-icon" :class="'resource-icon--' + resource.key"><ResourceIcon :name="resource.key" /></span>
              <span class="resource-card__english">{{ resource.english }}</span>
            </div>
            <h3>{{ resource.label }}</h3>
            <p>{{ $t('home.descriptions.' + resource.key) }}</p>
            <div class="resource-card__bottom">
              <span class="resource-card__count"><ResourceIcon name="sciencedata" /><strong>{{ formatNumber(stats[resource.stat]) }}</strong> {{ $t('home.records') }}</span>
              <span class="resource-card__action">{{ $t('home.explore') }}<ResourceIcon name="arrow" /></span>
            </div>
          </router-link>
        </div>
      </section>
      <section class="latest-section" aria-labelledby="latest-title">
        <div class="page-width">
          <div class="section-heading">
            <div>
              <p class="eyebrow">{{ $t('home.latestEyebrow') }}</p>
              <h2 id="latest-title">{{ $t('home.latestTitle') }}</h2>
              <p class="section-intro">{{ $t('home.latestIntro') }}</p>
            </div>
            <div class="latest-section__motif" aria-hidden="true"><i></i><i></i><i></i></div>
          </div>
          <div class="latest-toolbar">
            <div class="latest-tabs" role="tablist" :aria-label="$t('home.latestTitle')">
              <button v-for="resource in resources" :id="'latest-tab-' + resource.key" :key="resource.key" :ref="'latestTab-' + resource.key" type="button" role="tab" :aria-selected="latestType === resource.key ? 'true' : 'false'" aria-controls="latest-panel" :tabindex="latestType === resource.key ? 0 : -1" :class="{ active: latestType === resource.key }" @click="latestType = resource.key" @keydown="onLatestTabKeydown($event, resource.key)">
                <ResourceIcon :name="resource.key" />{{ resource.label }}
              </button>
            </div>
            <router-link class="text-link" :to="searchRoute(latestType)">{{ $t('home.viewMore') }}<ResourceIcon name="arrow" /></router-link>
          </div>
          <div id="latest-panel" class="latest-panel" role="tabpanel" :aria-labelledby="'latest-tab-' + latestType" :aria-busy="currentFeed.status === 'loading' ? 'true' : 'false'" tabindex="0">
            <div v-if="currentFeed.status === 'loading'" class="feed-state" role="status">
              <ResourceIcon class="loading-icon" name="refresh" /><p>{{ $t('home.loading') }}</p>
            </div>
            <div v-else-if="currentFeed.status === 'error'" class="feed-state" role="status">
              <ResourceIcon name="globe" /><h3>{{ $t('home.errorTitle') }}</h3><p>{{ $t('home.error') }}</p>
              <button class="retry-button" type="button" @click="loadFeed(latestType)">{{ $t('home.retry') }}</button>
            </div>
            <div v-else-if="!currentFeed.items.length" class="feed-state" role="status">
              <ResourceIcon name="spark" /><h3>{{ $t('home.emptyTitle') }}</h3><p>{{ $t('home.empty') }}</p>
            </div>
            <div v-else class="latest-grid">
              <router-link v-for="(item, index) in currentFeed.items" :key="item.uuid || index" class="latest-card" :to="resourceRoute(item, latestType)" :aria-label="resourceTitle(item) + ' · ' + $t('home.viewDetail')">
                <div class="latest-card__meta"><span class="type-tag" :class="'type-tag--' + latestType">{{ resourceType(item) }}</span><span v-if="resourceYear(item)">{{ resourceYear(item) }}</span></div>
                <h3>{{ resourceTitle(item) }}</h3>
                <p class="latest-card__abstract">{{ resourceDescription(item) }}</p>
                <div class="latest-card__footer"><span>{{ resourceSource(item) }}</span><ResourceIcon :name="latestType" /></div>
              </router-link>
            </div>
          </div>
        </div>
      </section>
      <section class="mission-section page-width" aria-labelledby="mission-title">
        <div class="mission">
          <div class="mission__content">
            <p class="eyebrow">{{ $t('home.missionEyebrow') }}</p>
            <h2 id="mission-title">{{ $t('home.missionTitle') }}</h2>
            <p>{{ $t('home.missionIntro') }}</p>
            <div class="mission__tags"><span v-for="tag in $t('home.missionTags')" :key="tag"><ResourceIcon name="check" />{{ tag }}</span></div>
          </div>
          <router-link class="mission__button" to="/about">{{ $t('home.about') }}<ResourceIcon name="arrow" /></router-link>
        </div>
      </section>
    </main>
    <footer class="home-footer page-width">
      <div class="home-footer__top">
        <div class="home-footer__brand"><img src="@/assets/logo.png" alt="" width="36" height="36" /><div><strong>Open Explore</strong><p>{{ $t('home.footerTagline') }}</p></div></div>
        <nav :aria-label="$t('home.discovery')"><router-link to="/search">{{ $t('home.discovery') }}</router-link><router-link to="/about">{{ $t('nav.about') }}</router-link><a :href="'mailto:' + $t('contactEmail')"><ResourceIcon name="mail" />{{ $t('home.contact') }}</a></nav>
      </div>
      <div class="home-footer__bottom"><span>© {{ year }} Open Explore</span><span>{{ $t('home.copyright') }}</span><span>OPEN SCIENCE · OPEN POSSIBILITIES</span></div>
    </footer>
  </div>
</template>

<script>
import ResourceIcon from '@/components/ResourceIcon.vue'

const RESOURCE_TYPES = [
  { key: 'article', stat: 'articleTotal', english: 'LITERATURE' },
  { key: 'sciencedata', stat: 'scienceDataTotal', english: 'SCIENTIFIC DATA' },
  { key: 'software', stat: 'softwareTotal', english: 'OPEN SOURCE' },
  { key: 'facility', stat: 'facilityTotal', english: 'RESEARCH FACILITIES' }
]

export default {
  name: 'HomePage',
  components: { ResourceIcon },
  data() {
    return {
      searchKeyword: '', searchType: 'article', latestType: 'article', stats: {},
      feeds: Object.fromEntries(RESOURCE_TYPES.map(({ key }) => [key, { status: 'loading', items: [] }])),
      year: new Date().getFullYear()
    }
  },
  computed: {
    resources() { return RESOURCE_TYPES.map(resource => ({ ...resource, label: this.$t('search.tab.' + resource.key) })) },
    currentFeed() { return this.feeds[this.latestType] }
  },
  created() {
    this.requestControllers = new Set()
    this.loadStats()
    RESOURCE_TYPES.forEach(({ key }) => this.loadFeed(key))
  },
  beforeDestroy() { this.requestControllers.forEach(controller => controller.abort()) },
  methods: {
    searchRoute(type, query = '') { return { path: '/search', query: { tab: type, ...(query ? { query } : {}) } } },
    goSearch(keyword) {
      const query = (typeof keyword === 'string' ? keyword : this.searchKeyword).trim()
      this.$router.push(this.searchRoute(this.searchType, query))
    },
    async request(path) {
      const controller = new AbortController()
      this.requestControllers.add(controller)
      const timeout = setTimeout(() => controller.abort(), 10000)
      try {
        const response = await fetch('/searchApi/' + path, { signal: controller.signal })
        if (!response.ok) throw new Error('Request failed')
        const json = await response.json()
        if ((json.code !== undefined && json.code !== 0) || !json.data) throw new Error('Invalid response')
        return json.data
      } finally {
        clearTimeout(timeout)
        this.requestControllers.delete(controller)
      }
    },
    async loadStats() {
      try { this.stats = await this.request('stats/v1/totals') } catch (_) { this.stats = {} }
    },
    async loadFeed(type) {
      this.feeds[type].status = 'loading'
      try {
        const data = await this.request(type + '/v1/latest?size=3')
        if (!Array.isArray(data.content)) throw new Error('Invalid resource list')
        this.feeds[type] = { status: 'ready', items: data.content.slice(0, 3) }
      } catch (_) { this.feeds[type] = { status: 'error', items: [] } }
    },
    formatNumber(value) {
      if (value === undefined || value === null || value === '') return '—'
      const match = String(value).match(/^(\d+)(\+?)$/)
      return match ? Number(match[1]).toLocaleString(this.$i18n.locale) + match[2] : String(value)
    },
    resourceRoute(item, type) { return item.uuid ? { path: '/' + type + '/detail/' + encodeURIComponent(item.uuid) } : this.searchRoute(type, this.resourceTitle(item)) },
    resourceTitle(item) { return item.title || item['title-en'] || this.$t('home.unknownSource') },
    resourceDescription(item) {
      const description = item.abstracts || item.description || item.abstract || this.$t('home.noDescription')
      return Array.isArray(description) ? description.join(' ') : String(description)
    },
    resourceType(item) { return item.type || item['dataset-type'] || item.category || this.$t('search.tab.' + this.latestType) },
    resourceYear(item) { return item.year || item['pub-year'] || '' },
    resourceSource(item) {
      const source = item.journal || item.organization || item.institution || item.author || item.provider
      return (Array.isArray(source) ? source.slice(0, 2).join(' · ') : source) || this.$t('home.unknownSource')
    },
    onLatestTabKeydown(event, key) {
      if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return
      event.preventDefault()
      const index = RESOURCE_TYPES.findIndex(resource => resource.key === key)
      const count = RESOURCE_TYPES.length
      const next = event.key === 'Home' ? 0 : event.key === 'End' ? count - 1 : (index + (event.key === 'ArrowRight' ? 1 : -1) + count) % count
      this.latestType = RESOURCE_TYPES[next].key
      this.$nextTick(() => this.$refs['latestTab-' + this.latestType][0].focus())
    }
  }
}
</script>

<style lang="scss" scoped>
$blue: #1754ec;
$ink: #111e3e;
$muted: #586780;

.HomePage {
  width: 100%;
  color: $ink;
  background: #f6f8ff;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 16px;
  line-height: 1.6;
  letter-spacing: normal;
  scroll-behavior: smooth;
  *, *::before, *::after { box-sizing: border-box; }
  button, input { font: inherit; }
  button, a { touch-action: manipulation; -webkit-tap-highlight-color: transparent; }
  button { cursor: pointer; }
  a { color: inherit; text-decoration: none; }
  button:focus-visible, a:focus-visible, [tabindex="0"]:focus-visible { outline: 3px solid #80a7ff; outline-offset: 4px; }
  svg { width: 24px; height: 24px; flex-shrink: 0; }
  main { position: relative; isolation: isolate; }
}
h1, h2, h3, p { margin: 0; }
.page-width { width: min(1120px, calc(100% - 80px)); margin-inline: auto; }
.page-atmosphere {
  position: absolute; inset: 0; z-index: -1; overflow: hidden; pointer-events: none;
  background:
    radial-gradient(ellipse 72% 25% at 80% 4%, #b3cfff9e, transparent 90%),
    radial-gradient(ellipse 48% 20% at 0 8%, #e4dbff8c, transparent 85%),
    radial-gradient(ellipse 55% 30% at 99% 53%, #eae2ff85, transparent 85%),
    radial-gradient(ellipse 50% 29% at 0 56%, #e5f1ffbc, transparent 85%),
    linear-gradient(180deg, #eef2ff 0%, #f8faff 41%, #f6f8ff 100%);
}
.HomePage .page-waves { position: absolute; left: 0; width: 100%; pointer-events: none; &--hero { top: 0; height: 730px; } &--middle { top: 870px; height: 390px; } }
.page-waves--middle { mask-image: linear-gradient(to bottom, #000 55%, transparent 100%); }
.hero { position: relative; overflow: hidden; }
.hero__grid { display: grid; grid-template-columns: 1.06fr 1fr; align-items: center; min-height: 510px; padding-block: 48px 58px; gap: 35px; }
.hero__content { position: relative; z-index: 2; min-width: 0; }
.hero__eyebrow { display: inline-flex; align-items: center; gap: 8px; margin-bottom: 20px; color: #244a9d; font-size: .875rem; font-weight: 500; svg { width: 17px; height: 17px; padding: 2px; border-radius: 50%; color: white; background: #4d63ff; } }
.hero h1 { font-size: clamp(2.5rem, 3.35vw, 3.1rem); font-weight: 750; letter-spacing: -.04em; line-height: 1.25; span { display: block; margin-top: 5px; color: $blue; } }
.hero__intro { max-width: 550px; margin-top: 22px; color: #53617b; font-size: 1rem; line-height: 1.8; white-space: pre-line; }
.discovery-search { margin-top: 27px; }
.search-tabs, .latest-tabs { display: flex; gap: 22px; max-width: 100%; overflow-x: auto; scrollbar-width: none; &::-webkit-scrollbar { display: none; }
  button { display: inline-flex; flex-shrink: 0; align-items: center; gap: 5px; padding: 0 0 9px; border: 0; border-bottom: 2px solid transparent; color: #5e6a83; background: transparent; font-size: .875rem; white-space: nowrap;
    svg { width: 16px; height: 16px; }
    &.active { color: $blue; border-bottom-color: $blue; font-weight: 600; }
    &:hover { color: $blue; }
    &:focus-visible { outline-offset: -3px; }
  }
}
.search-tabs { padding: 0 3px 8px; }
.search-field { display: flex; align-items: center; gap: 11px; height: 55px; padding: 5px 5px 5px 16px; border: 1px solid #b9cdff; border-radius: 12px; background: #ffffffed; box-shadow: 0 4px 12px #5c7ce114, inset 0 0 0 2px #fff; transition: border-color .2s, box-shadow .2s;
  > svg { width: 20px; height: 20px; color: #6685dc; }
  &:focus-within { border-color: $blue; box-shadow: 0 0 0 3px #1754ec18; }
  input { min-width: 0; width: 100%; height: 100%; padding: 0; border: 0; outline: 0; color: $ink; background: transparent; font-size: 1rem; &::placeholder { color: #8d96ac; } }
  button { flex-shrink: 0; align-self: stretch; padding: 0 23px; border: 1px solid #668fff; border-radius: 9px; color: #fff; background: linear-gradient(145deg, #276aff, #1045e5); box-shadow: 0 3px 9px #1d51e13d, inset 0 1px 2px #ffffff4d; font-size: .875rem; font-weight: 600; &:hover { background: #1246cc; } }
}
.hero__topics { display: flex; align-items: center; flex-wrap: wrap; gap: 8px 12px; margin-top: 15px; font-size: .8125rem;
  > span { color: #5f6c85; }
  button { padding: 1px 10px; border: 1px solid #d4dff6; border-radius: 20px; color: #4f6389; background: #ffffffbc; box-shadow: 0 2px 4px #3455a409; font-size: inherit; line-height: 1.5; &:hover { color: $blue; border-color: #9ab7ff; background: white; } }
}
.hero-art { position: relative; align-self: stretch; min-width: 0; }
.hero-art__atom { position: absolute; top: 47%; left: 52%; width: 90%; max-width: none; height: auto; transform: translate(-50%, -50%); filter: drop-shadow(0 22px 20px #5571cd17); }
.hero-art__cross { position: absolute; color: white; font-size: 24px; font-weight: 500; &--top { top: -8px; left: 45%; } &--bottom { bottom: 25%; right: 0; font-size: 16px; } }
.art-label { position: absolute; z-index: 1; display: flex; align-items: center; gap: 9px; padding: 9px 14px 9px 9px; border: 1px solid #fff; border-radius: 11px; color: #29395f; background: linear-gradient(145deg, #ffffffeb, #f3f6ffe0); box-shadow: 0 5px 13px #4973c524, inset 0 0 0 2px #ffffff8c; font-size: .8125rem; font-weight: 500; white-space: nowrap;
  .resource-icon { width: 28px; height: 28px; border-radius: 8px; svg { width: 19px; height: 19px; } }
  &--article { top: 5%; left: 4%; transform: rotate(-6deg); }
  &--sciencedata { top: 21%; right: -2%; transform: rotate(5deg); }
  &--software { bottom: 16%; left: -2%; transform: rotate(-4deg); }
  &--facility { right: 0; bottom: 9%; transform: rotate(4deg); }
}
.hero-art__caption { position: absolute; bottom: -28px; left: 52%; transform: translateX(-50%); color: #3e69d3; font-size: .75rem; letter-spacing: .15em; white-space: nowrap; }
.stats-band { position: relative; z-index: 3; margin-top: -3px; }
.stats-band__inner { display: grid; grid-template-columns: repeat(4, 1fr); width: min(1200px, calc(100% - 64px)); padding: 22px 26px; border: 1px solid #ffffffee; border-radius: 19px; background: linear-gradient(120deg, #ffffffe6, #f7f7ffb3); box-shadow: 0 8px 25px #5076d11a, inset 0 0 0 1px #ffffffa6; backdrop-filter: blur(18px); }
.stat { display: flex; align-items: center; justify-content: center; gap: 17px; border-right: 1px solid #d4dff1;
  &:last-child { border: 0; > svg { color: #8263ff; } }
  > svg { width: 32px; height: 32px; color: #3974ff; stroke-width: 1.7; }
  div { display: flex; align-items: baseline; flex-wrap: wrap; gap: 3px 12px; }
  strong { color: #13223f; font-size: 1.65rem; font-weight: 700; line-height: 1.3; letter-spacing: -.035em; }
  span { color: #69778d; font-size: .875rem; }
}
.resource-section { position: relative; padding-block: 44px 46px; }
.section-heading { position: relative; display: flex; justify-content: space-between; align-items: flex-end; gap: 24px; margin-bottom: 24px; }
.eyebrow { margin-bottom: 7px; color: #4a6bd3; font-size: .75rem; font-weight: 500; letter-spacing: .12em; }
.HomePage h2 { color: #14213e; font-size: 1.75rem; font-weight: 700; letter-spacing: -.035em; line-height: 1.35; }
.section-intro { margin-top: 7px; color: $muted; font-size: 1rem; line-height: 1.65; }
.text-link { display: inline-flex; flex-shrink: 0; align-items: center; gap: 10px; color: $blue !important; font-size: .875rem; font-weight: 500; white-space: nowrap; svg { width: 17px; height: 17px; } &:hover { color: #0534b1 !important; text-decoration: underline; text-underline-offset: 4px; } }
.resource-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 17px; }
.resource-icon { display: inline-flex; align-items: center; justify-content: center; width: 44px; height: 44px; border-radius: 13px; border: 1px solid #ffffffb3; box-shadow: inset 0 1px 3px #ffffffb3;
  &--article { color: #1966ff; background: linear-gradient(135deg, #deedff, #c7deff); }
  &--sciencedata { color: #00a388; background: linear-gradient(135deg, #e2fff7, #c9f5ed); }
  &--software { color: #7d4fff; background: linear-gradient(135deg, #f1eaff, #e2d6ff); }
  &--facility { color: #b87816; background: linear-gradient(135deg, #fff8e9, #ffebc7); }
}
.resource-card { position: relative; display: flex; flex-direction: column; padding: 20px 21px 17px; border: 1px solid #fff; border-radius: 18px; box-shadow: 0 5px 16px #4664b30a, 0 0 0 1px #cfdafa75; background: linear-gradient(135deg, #edf5ffd9, #ffffffc4); transition: transform .2s, box-shadow .2s;
  &::before { content: ''; position: absolute; inset: 4px; border: 1px solid #ffffffbd; border-radius: 14px; pointer-events: none; }
  &--sciencedata { background: linear-gradient(135deg, #effcf9e6, #fbfdffd4); }
  &--software { background: linear-gradient(135deg, #f6f1ffd9, #fcfcffd9); }
  &--facility { background: linear-gradient(135deg, #fffaf2e0, #fcfaffd4); }
  &:hover { transform: translateY(-4px); box-shadow: 0 12px 26px #4664b31a, 0 0 0 1px #a6bff1; }
  h3 { margin-top: 13px; font-size: 1.125rem; font-weight: 650; }
  > p { flex: 1; margin-top: 7px; color: #58657e; font-size: 1rem; line-height: 1.65; }
}
.resource-card__top { display: flex; align-items: center; justify-content: space-between; gap: 8px; }
.resource-card__english { max-width: 108px; color: #7c8cad; font-size: .75rem; line-height: 1.5; text-align: right; letter-spacing: .025em; }
.resource-card__bottom { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 9px; margin-top: 18px; padding-top: 13px; border-top: 1px solid #dce3f085; color: #71809a; font-size: .75rem; strong { color: #425678; font-weight: 600; } }
.resource-card__count { display: inline-flex; align-items: center; gap: 4px; svg { width: 13px; height: 13px; margin-right: 2px; } }
.resource-card__action { display: inline-flex; align-items: center; gap: 6px; color: $blue; font-size: .8125rem; font-weight: 500; svg { width: 14px; height: 14px; } }
.latest-section { position: relative; padding-block: 12px 24px; }
.latest-section .section-heading { margin-bottom: 17px; }
.latest-section__motif { position: absolute; right: 9%; bottom: -34px; width: 250px; height: 130px; pointer-events: none;
  i { position: absolute; width: 108px; height: 96px; border: 1px solid #ffffffb3; border-radius: 9px; transform: skewX(-22deg) rotate(-8deg); background: linear-gradient(140deg, #a1d1ff99, #bdcaff17); box-shadow: inset 0 1px 1px #ffffff8c;
    &:first-child { left: 12px; top: 22px; background: linear-gradient(135deg, #a1e3ff99, #8cc7ff26); }
    &:nth-child(2) { left: 66px; top: 8px; background: linear-gradient(140deg, #8cbaffb3, #c6c5ff3b); }
    &:last-child { left: 114px; top: 35px; background: linear-gradient(140deg, #e8caff9e, #b3b9ff21); }
  }
}
.latest-toolbar { position: relative; display: flex; align-items: center; justify-content: space-between; gap: 20px; margin-bottom: 18px; border-bottom: 1px solid #dbe4f3; > .text-link { margin-bottom: 13px; } }
.latest-tabs { gap: 28px; button { padding-bottom: 12px; } }
.latest-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 19px; }
.latest-card { display: flex; flex-direction: column; min-height: 184px; padding: 17px 19px 13px; border: 1px solid #d7e2f5; border-radius: 13px; background: #ffffffb8; box-shadow: 0 5px 17px #3e64b105, inset 0 1px 0 #fff; transition: border-color .2s, box-shadow .2s;
  &:hover { border-color: #97b4f4; box-shadow: 0 8px 22px #466bb414; h3 { color: $blue; } }
  h3 { display: -webkit-box; overflow: hidden; margin-top: 11px; color: #192541; font-size: 1rem; font-weight: 650; line-height: 1.5; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
}
.latest-card__meta { display: flex; align-items: center; justify-content: space-between; gap: 14px; color: #7c8ba3; font-size: .75rem; }
.type-tag { overflow: hidden; max-width: 80%; padding: 2px 9px; border-radius: 5px; color: #3967c6; background: #e6eeff; text-overflow: ellipsis; white-space: nowrap;
  &--sciencedata { color: #258776; background: #e3f6f1; } &--software { color: #8057c4; background: #efe7ff; } &--facility { color: #a47927; background: #fff1d7; }
}
.latest-card__abstract { display: -webkit-box; overflow: hidden; margin-top: 8px; color: #758096; font-size: 1rem; line-height: 1.65; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.latest-card__footer { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-top: auto; padding-top: 12px; border-top: 1px solid #e2e7f285; color: #8b97ac; font-size: .75rem; > span { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; } svg { width: 15px; height: 15px; color: #7395d4; } }
.latest-card__abstract + .latest-card__footer { margin-top: 12px; }
.feed-state { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 200px; padding: 25px; text-align: center;
  > svg { width: 30px; height: 30px; margin-bottom: 12px; color: #7d99bf; } h3 { font-size: 1.125rem; font-weight: 500; } p { margin-top: 8px; color: $muted; font-size: 1rem; }
}
.retry-button { margin-top: 15px; padding: 7px 18px; border: 1px solid #b8ccef; border-radius: 8px; color: $blue; background: #ffffffdb; font-size: .875rem !important; &:hover { border-color: $blue; } }
.loading-icon { animation: spin 1.4s linear infinite; }
.mission-section { padding-block: 24px 37px; }
.mission { position: relative; display: flex; align-items: center; justify-content: space-between; gap: 28px; overflow: hidden; padding: 26px 42px; border: 1px solid #779ce8; border-radius: 17px; background: linear-gradient(90deg, #0d2a61a8, #173e7c0a), url('~@/assets/generated/earth-horizon.webp') center 64% / cover; color: #fff; box-shadow: inset 0 1px 1px #b2c8ff63;
  .eyebrow { color: #c4d5f4; font-size: .75rem; }
  h2 { color: #fff; font-size: 1.75rem; }
  .mission__content > p:not(.eyebrow) { margin-top: 8px; color: #d4dff4; font-size: 1rem; }
}
.mission__content { max-width: 76%; }
.mission__tags { display: flex; flex-wrap: wrap; gap: 10px 25px; margin-top: 16px; color: #d5e1f9; font-size: .75rem;
  span { display: flex; align-items: center; gap: 7px; } svg { width: 16px; height: 16px; color: #9cbdff; border-radius: 50%; background: #195bdd; }
}
.mission__button { display: inline-flex; flex-shrink: 0; align-items: center; justify-content: center; gap: 12px; padding: 11px 22px; border: 1px solid #fff; border-radius: 10px; color: #0c42cb !important; background: linear-gradient(180deg, #fff, #dae5ff); box-shadow: 0 4px 14px #081e5c29, inset 0 0 0 2px #ffffff73; font-size: .875rem; font-weight: 600;
  svg { width: 17px; height: 17px; } &:hover { background: #fff; }
}
.home-footer { padding-bottom: 20px; }
.home-footer__top { display: flex; align-items: center; justify-content: space-between; gap: 24px; padding: 4px 0 22px;
  nav { display: flex; align-items: center; gap: 36px; color: #586781; font-size: .875rem; a { display: flex; align-items: center; gap: 7px; &:hover { color: $blue; } } svg { width: 16px; height: 16px; } }
}
.home-footer__brand { display: flex; align-items: center; gap: 11px; strong { font-size: 1rem; font-weight: 650; letter-spacing: -.025em; } p { margin-top: 0; color: #8996ae; font-size: .75rem; } }
.home-footer__bottom { display: flex; justify-content: space-between; gap: 20px; padding-top: 13px; border-top: 1px solid #dce4f2; color: #8b97ae; font-size: .75rem; span:last-child { letter-spacing: .05em; } }
@keyframes spin { to { transform: rotate(360deg); } }
@media (min-width: 1600px) { .hero__grid { min-height: 535px; } }
@media (max-width: 1100px) {
  .page-width { width: calc(100% - 72px); }
  .hero__grid { gap: 20px; grid-template-columns: 1.1fr 1fr; }
  .hero h1 { font-size: 2.65rem; }
  .hero-art__atom { width: 108%; }
  .art-label { padding: 7px 10px 7px 7px; gap: 6px; font-size: .75rem; }
  .hero-art__caption { font-size: .625rem; }
  .search-tabs { gap: 16px; }
  .resource-grid { gap: 14px; }
  .resource-card { padding-inline: 18px; }
  .resource-card__english { font-size: .6875rem; }
  .resource-card__bottom { gap: 7px; }
  .stat { gap: 12px; div { gap: 3px 8px; } strong { font-size: 1.5rem; } }
  .mission { padding-inline: 30px; }
}
@media (max-width: 900px) {
  .page-width { width: calc(100% - 48px); }
  .hero__grid { min-height: 495px; grid-template-columns: 1.25fr 1fr; padding-block: 40px 55px; }
  .hero h1 { font-size: 2.3rem; }
  .hero-art__atom { width: 125%; left: 51%; }
  .hero-art__caption { display: none; }
  .art-label { font-size: .75rem; .resource-icon { width: 25px; height: 25px; } &--article { left: 0; top: 9%; } &--sciencedata { right: -12px; top: 25%; } &--software { left: -4%; bottom: 18%; } &--facility { bottom: 7%; right: -4%; } }
  .stats-band__inner { width: calc(100% - 32px); padding-inline: 14px; }
  .stat { gap: 11px; > svg { width: 28px; height: 28px; } div { flex-direction: column; gap: 0; } span { font-size: .8125rem; } }
  .resource-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 18px; }
  .resource-card__english { max-width: none; font-size: .75rem; }
  .latest-grid { gap: 13px; }
  .latest-card { padding-inline: 15px; }
  .latest-section__motif { right: 0; opacity: .7; }
  .mission__content { max-width: 70%; }
  .mission h2 { font-size: 1.5rem; }
  .mission__button { padding-inline: 16px; }
  .home-footer__bottom { flex-wrap: wrap; span:last-child { font-size: .6875rem; } }
}
@media (max-width: 700px) {
  .hero__grid { display: flex; flex-direction: column; align-items: stretch; gap: 0; min-height: 0; padding-block: 32px 26px; }
  .hero h1 { font-size: 2.5rem; }
  .hero__eyebrow { margin-bottom: 17px; font-size: .8125rem; }
  .hero__intro { margin-top: 18px; white-space: normal; }
  .discovery-search { margin-top: 23px; }
  .hero__topics { gap: 7px 9px; }
  .hero-art { display: block; align-self: center; width: min(90%, 390px); height: 300px; margin-top: 22px; }
  .hero-art__atom { width: 92%; max-width: 320px; top: 50%; left: 50%; }
  .hero-art__cross--top { left: 46%; top: 3%; }
  .art-label { padding: 6px 8px; font-size: .75rem; .resource-icon { width: 23px; height: 23px; svg { width: 15px; height: 15px; } } &--article { top: 9%; left: -2%; } &--sciencedata { top: 24%; right: -7%; } &--software { bottom: 25%; left: -6%; } &--facility { bottom: 10%; right: -3%; } }
  .hero-art__caption { display: block; left: 50%; bottom: -6px; font-size: .5625rem; }
  .search-tabs { gap: 19px; }
  .search-field { gap: 9px; height: 55px; padding-left: 12px; button { padding-inline: 16px; } }
  .stats-band { margin-top: 8px; }
  .stats-band__inner { grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 22px 0; padding: 23px 14px; border-radius: 16px; }
  .stat { justify-content: center; gap: 13px; &:nth-child(2) { border: 0; } > svg { width: 28px; height: 28px; } strong { font-size: 1.45rem; } span { font-size: .8125rem; } }
  .resource-section { padding-block: 35px 31px; }
  .section-heading { flex-wrap: wrap; align-items: flex-start; gap: 13px; margin-bottom: 21px; }
  .HomePage h2 { font-size: 1.45rem; }
  .eyebrow { font-size: .6875rem; letter-spacing: .1em; }
  .section-intro { font-size: 1rem; line-height: 1.65; }
  .resource-grid { gap: 14px; }
  .resource-card { padding: 17px 16px 15px; border-radius: 15px; h3 { font-size: 1.0625rem; margin-top: 12px; } > p { line-height: 1.65; } }
  .resource-card__english { display: none; }
  .resource-icon { width: 39px; height: 39px; border-radius: 11px; svg { width: 23px; height: 23px; } }
  .resource-card__bottom { align-items: flex-start; flex-direction: column; gap: 10px; margin-top: 17px; padding-top: 12px; }
  .resource-card__action { font-size: .875rem; }
  .latest-section { padding-block: 10px 15px; }
  .latest-section__motif { display: none; }
  .latest-toolbar { align-items: flex-end; flex-wrap: wrap; gap: 13px; }
  .latest-tabs { width: 100%; gap: 24px; }
  .latest-toolbar > .text-link { margin: 0 0 10px auto; }
  .latest-grid { grid-template-columns: 1fr; gap: 14px; }
  .latest-card { min-height: 0; padding: 18px; }
  .latest-card__abstract { -webkit-line-clamp: 2; }
  .mission-section { padding-block: 21px 30px; }
  .mission { display: block; padding: 27px 24px; border-radius: 15px; background-position: 62% center; }
  .mission__content { max-width: none; }
  .mission h2 { font-size: 1.45rem; }
  .mission__tags { gap: 10px 15px; }
  .mission__button { margin-top: 23px; padding: 10px 17px; }
  .home-footer__top { align-items: flex-start; flex-direction: column; gap: 22px; nav { width: 100%; justify-content: space-between; gap: 16px; font-size: .8125rem; } }
  .home-footer__bottom { justify-content: flex-start; gap: 8px 20px; font-size: .6875rem; span:last-child { width: 100%; } }
}
@media (max-width: 380px) {
  .page-width { width: calc(100% - 32px); }
  .hero h1 { font-size: 2.15rem; }
  .search-field { > svg { display: none; } button { padding-inline: 12px; } }
  .search-tabs button, .latest-tabs button { svg { display: none; } }
  .resource-card { padding-inline: 13px; }
  .resource-card__count { font-size: .6875rem; gap: 3px; svg { display: none; } }
  .stat { gap: 9px; }
}
@media (prefers-reduced-motion: reduce) {
  .HomePage { scroll-behavior: auto; *, *::before, *::after { animation: none !important; transition: none !important; } }
}
</style>
