<template>
  <div class="Search__page">
    <!-- 左侧聚合面板 -->
    <div class="Agg__wrap">
      <base-scrollbar style="height: 100%;">
        <div class="Agg__items">
          <div class="Agg__title">
            {{$t('search.filterTitle')}}
          </div>
          <base-skeleton v-if="aggsLoading" :rows="6" animated />
          <div v-if="!aggsLoading">
            <AggsItemV
              v-for="item,index in aggs"
              :key="'agg' + index"
              :title="item.alias"
              :datas="item.aggregations"
              :checkList.sync="item.selected_aggregation"
              @change="handleAggChange"
            />
          </div>
        </div>
      </base-scrollbar>
    </div>

    <!-- 右侧主搜索区域 -->
    <div class="Search__wrap">
      <!-- 子导航 Tab：4 种资源类型 -->
      <div class="SubTabs">
        <div
          v-for="t in resourceTabs"
          :key="t.key"
          class="SubTabs__tab"
          :class="{ 'SubTabs__tab--active': activeTab === t.key }"
          @click="switchResourceTab(t.key)"
        >
          <span>{{ $t('search.tab.' + t.key) }}</span>
        </div>
      </div>

      <div class="Search__content">
        <div class="Search__info">
          <input
            v-model="searchText"
            type="text"
            :placeholder="currentTabPlaceholder"
            @keyup.enter="handleSearch"
          />
          <div class="Search__btn" @click="handleSearch">
            <i class="base-icon-search"></i>&nbsp;{{$t('search.searchBtn')}}
          </div>
        </div>
      </div>

      <!-- 筛选条件标签 -->
      <div class="Filterd__wrap" v-if="fiterConditions.length">
        <div>{{$t('search.filterConditions')}}</div>
        <div class="Filterd__item" v-for="item,index in fiterConditions" :key="'fiterConditions' + index">
          <span>{{item.label}}：</span>
          {{item.value}}
          <i class="base-icon-close" @click="handleClearSearch(item)"></i>
        </div>
        <div class="Filterd__clean--btn" @click="handleClearAll">
          <i class="base-icon-delete"></i>{{$t('search.clearAll')}}
        </div>
      </div>

      <div class="Total__wrap">
        <div class="Total__text">{{$t('search.totalResults', {n: total})}}</div>
      </div>

      <div class="Result__wrap">
        <template v-for="(item,index) in tableData">
          <div :key="'tableData' + index" class="RichCard" :class="'RichCard--' + activeTab" :style="{animationDelay: 0.06 * index + 's'}" @click="handleTitleClick(item)">
            <div class="RichCard__index">{{(searchParams.page - 1) * searchParams.size + index + 1}}</div>
            <div class="RichCard__inner">
              <!-- 有图的类型显示图片 -->
              <div class="RichCard__pic" v-if="(activeTab === 'facility' || activeTab === 'software') && item.image">
                <img :src="item.image" :alt="item.title" @error="e => e.target.style.display='none'" />
              </div>
              <div class="RichCard__body">
                <h3 class="RichCard__title">{{ item.title }}</h3>
                <!-- 描述/摘要: 按类型取不同字段 -->
                <p class="RichCard__desc" v-if="activeTab === 'facility' && item.description_information">{{ truncate(item.description_information, 120) }}</p>
                <p class="RichCard__desc" v-if="activeTab === 'software' && item.description">{{ truncate(item.description, 120) }}</p>
                <p class="RichCard__desc" v-if="activeTab === 'article' && item.abstracts">{{ truncate(item.abstracts, 150) }}</p>
                <p class="RichCard__desc" v-if="activeTab === 'sciencedata' && (item.description || item.abstracts)">{{ truncate(item.description || item.abstracts, 150) }}</p>

                <div class="RichCard__meta">
                  <!-- 设施标签 -->
                  <template v-if="activeTab === 'facility'">
                    <span class="RichCard__tag RichCard__tag--type" v-if="item['facility-type']">{{ item['facility-type'] }}</span>
                    <span class="RichCard__tag RichCard__tag--status" v-if="item['facility-status']">{{ item['facility-status'] }}</span>
                    <span class="RichCard__meta-item" v-if="item['facility-location']">📍 {{ item['facility-location'] }}</span>
                    <span class="RichCard__meta-item" v-if="item['hosting_institution']">🏛 {{ item['hosting_institution'] }}</span>
                    <span class="RichCard__meta-item RichCard__meta-item--year" v-if="item['pub-year']">📅 {{ item['pub-year'] }}</span>
                  </template>
                  <!-- 软件标签 -->
                  <template v-if="activeTab === 'software'">
                    <span class="RichCard__tag RichCard__tag--type" v-if="item.category">{{ item.category }}</span>
                    <span class="RichCard__tag RichCard__tag--license" v-if="item.license">{{ item.license }}</span>
                    <span class="RichCard__meta-item" v-if="item.organization">🏛 {{ item.organization }}</span>
                    <span class="RichCard__meta-item RichCard__meta-item--year" v-if="item['pub-year']">📅 {{ item['pub-year'] }}</span>
                  </template>
                  <!-- 文献标签 -->
                  <template v-if="activeTab === 'article'">
                    <span class="RichCard__tag RichCard__tag--type" v-if="item.type">{{ item.type }}</span>
                    <span class="RichCard__tag RichCard__tag--journal" v-if="item.journal">{{ item.journal }}</span>
                    <span class="RichCard__meta-item" v-if="item.author && item.author.length">✍️ {{ item.author.slice(0,3).join('; ') }}{{ item.author.length > 3 ? ' et al.' : '' }}</span>
                    <span class="RichCard__meta-item" v-if="item.publisher">📚 {{ item.publisher }}</span>
                    <span class="RichCard__meta-item RichCard__meta-item--year" v-if="item.year || item['pub-year']">📅 {{ item.year || item['pub-year'] }}</span>
                  </template>
                  <!-- 科学数据标签 -->
                  <template v-if="activeTab === 'sciencedata'">
                    <span class="RichCard__tag RichCard__tag--type" v-if="item['dataset-type']">{{ item['dataset-type'] }}</span>
                    <span class="RichCard__meta-item" v-if="item.author && item.author.length">🏛 {{ item.author.slice(0,2).join('; ') }}</span>
                    <span class="RichCard__meta-item RichCard__meta-item--year" v-if="item['pub-year']">📅 {{ item['pub-year'] }}</span>
                  </template>
                </div>
                <div class="RichCard__extra" v-if="(activeTab === 'article' || activeTab === 'sciencedata') && (item.doi || item.issn)">
                  <span v-if="item.doi" class="RichCard__extra-item">DOI: {{ item.doi }}</span>
                  <span v-if="item.issn" class="RichCard__extra-item">ISSN: {{ item.issn }}</span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>

      <div style="margin-top:12px;width:100%;display:flex;justify-content: center;">
        <base-pagination
          background
          layout="prev, pager, next"
          :page-size="searchParams.size"
          @current-change="handleSearch()"
          :current-page.sync="searchParams.page"
          :total="total > 10000 ? 10000 : total"
        />
      </div>

      <ToTop />
    </div>
  </div>
</template>

<script>
import {AggsItemV, ReadableItem} from '@zkwq/business'
import ToTop from '@/components/ToTop.vue'

import { SearchArticle, SearchArticleAgg } from '@/service/ArticleSerach'
import { SearchScienceData, SearchScienceDataAgg } from '@/service/ScienceDataSearch'
import { SearchSoftware, SearchSoftwareAgg } from '@/service/SoftwareSearch'
import { SearchFacility, SearchFacilityAgg } from '@/service/FacilitySearch'

/**
 * 资源类型 tab 定义
 * 每个 tab 映射到对应的 API 请求函数
 */
const RESOURCE_TABS = [
  { key: 'article', label: '文献', search: SearchArticle, agg: SearchArticleAgg, detailPrefix: '/article/detail/' },
  { key: 'sciencedata', label: '科学数据', search: SearchScienceData, agg: SearchScienceDataAgg, detailPrefix: '/sciencedata/detail/' },
  { key: 'software', label: '科学软件', search: SearchSoftware, agg: SearchSoftwareAgg, detailPrefix: '/software/detail/' },
  { key: 'facility', label: '大型装置', search: SearchFacility, agg: SearchFacilityAgg, detailPrefix: '/facility/detail/' }
]

/**
 * 资源类型对应的聚合别名映射
 */
const ALIAS_MAP = {
  sciencedata: {
    'record.classification.first': 'search.agg.classificationFirst',
    'record.classification.second': 'search.agg.classificationSecond',
    'record.pub-year': 'search.agg.year',
    'record.dataset-type': 'search.agg.datasetType',
    'record.type': 'search.agg.type'
  }
}

/**
 * 资源类型对应的数据展平映射（从后端嵌套字段 => 前端展示字段）
 */
const DATA_FLATTEN = {
  sciencedata: (item) => ({
    ...item,
    date: item['pub-date'] || '',
    year: item['pub-year'] || ''
  }),
  software: (item) => ({
    ...item,
    image: item['image-url'] || item.image || '',
    date: item['pub-date'] || item.publish_time || '',
    year: item['pub-year'] || item.create_year || ''
  })
}

export default {
  components: { AggsItemV, ReadableItem, ToTop },
  data() {
    const tabFromQuery = this.$route.query.tab || 'article'
    const initialTab = RESOURCE_TABS.find(t => t.key === tabFromQuery) ? tabFromQuery : 'article'

    return {
      activeTab: initialTab,
      resourceTabs: RESOURCE_TABS,
      total: 0,
      tableData: [],
      loading: false,
      searchText: this.$route.query.query || '',
      searchParams: {
        page: 1,
        size: 10,
        order_field: '_score'
      },
      aggs: [],
      queryText: '',
      aggsLoading: false
    }
  },
  computed: {
    currentTab() {
      return RESOURCE_TABS.find(t => t.key === this.activeTab) || RESOURCE_TABS[0]
    },
    currentTabPlaceholder() {
      return this.$t('search.placeholder.' + this.activeTab) || this.$t('search.searchBtn')
    },
    fiterConditions() {
      let arr = []
      if (this.queryText) {
        arr.push({ label: '检索文本', type: 'query', value: this.queryText })
      }
      this.aggs.forEach(item => {
        if (item.selected_aggregation && item.selected_aggregation.length) {
          item.selected_aggregation.forEach(v => {
            arr.push({ label: item.alias, key: item.key, type: 'agg', value: v })
          })
        }
      })
      return arr
    }
  },
  created() {
    this.handleSearch()
  },
  methods: {
    /**
     * 切换资源类型 tab — 重置所有搜索状态并重新搜索
     */
    switchResourceTab(key) {
      if (key === this.activeTab) return
      this.activeTab = key
      this.searchText = ''
      this.queryText = ''
      this.searchParams.page = 1
      this.aggs = []
      this.total = 0
      this.tableData = []
      this.$router.replace({ query: { tab: key } })
      this.handleSearch()
    },

    handleTitleClick(item) {
      this.$router.push({
        path: this.currentTab.detailPrefix + item.uuid
      })
    },

    handleAggChange() {
      this.searchParams.page = 1
      this.handleSearch()
    },

    handleClearSearch(item) {
      if (item.type === 'query') {
        this.searchText = ''
        this.queryText = ''
      } else if (item.type === 'agg') {
        this.aggs.forEach(a => {
          if (a.key === item.key && a.selected_aggregation) {
            const idx = a.selected_aggregation.indexOf(item.value)
            if (idx > -1) a.selected_aggregation.splice(idx, 1)
          }
        })
      }
      this.searchParams.page = 1
      this.handleSearch()
    },

    handleClearAll() {
      this.searchText = ''
      this.aggs.forEach(item => { item.selected_aggregation = [] })
      this.searchParams.page = 1
      this.handleSearch()
    },

    buildAggregations() {
      const aggs = {}
      this.aggs.forEach(item => {
        if (item.selected_aggregation && item.selected_aggregation.length) {
          aggs[item.key] = item.selected_aggregation.join(',')
        }
      })
      return aggs
    },

    /**
     * 核心搜索方法 — 根据当前 activeTab 调用对应 API
     */
    handleSearch() {
      const tab = this.currentTab
      this.loading = true

      const params = {
        query: this.searchText,
        page: this.searchParams.page,
        size: this.searchParams.size,
        order_field: this.searchParams.order_field,
        order_direction: 'desc',
        aggregations: this.buildAggregations()
      }

      tab.search(params).then(res => {
        if (res.error) {
          this.$message.error(res.error)
          return
        }
        const data = res.result.data
        this.total = data.total
        const flatten = DATA_FLATTEN[this.activeTab]
        this.tableData = (data.content || []).map(e => {
          const base = { ...e, type: this.activeTab === 'article' ? 'article' : this.activeTab }
          return flatten ? flatten(base) : base
        })
        this.queryText = this.searchText
      }).finally(() => {
        this.loading = false
      })

      this.handleSearchAgg()
    },

    handleSearchAgg() {
      if (this.searchParams.page > 1) return
      const tab = this.currentTab
      this.aggsLoading = true

      const params = {
        query: this.searchText,
        page: this.searchParams.page,
        size: this.searchParams.size,
        order_field: this.searchParams.order_field,
        order_direction: 'desc',
        aggregations: this.buildAggregations()
      }

      tab.agg(params).then(res => {
        if (res.error) {
          this.$message.error(res.error)
          return
        }
        const data = res.result.data
        const aliasMap = ALIAS_MAP[this.activeTab] || {}
        this.aggs = Object.keys(data || {}).map(key => ({
          key,
          alias: aliasMap[key] || key,
          aggregations: data[key].buckets,
          selected_aggregation: this.aggs.find(item => item.key === key)?.selected_aggregation || []
        }))
      }).finally(() => {
        this.aggsLoading = false
      })
    },
    truncate(text, len) {
      if (!text) return ''
      return text.length > len ? text.slice(0, len) + '…' : text
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@zkwq/business/dist/var.scss';

// ===== Global Design Tokens =====
$brand: #2F3A91;
$brand-light: rgba(47,58,145,0.06);
$bg-soft: #f6f8fc;
$bg-card: #ffffff;
$border-soft: #e9ecf2;
$text-primary: #1a1f36;
$text-secondary: #5a6070;
$text-muted: #8e96ab;

.Search__page {
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  gap: 0;
  background: $bg-soft;

  // ===== Left Filter Panel =====
  .Agg__wrap {
    width: 280px;
    height: 100%;
    background: #fff;
    border-right: 1px solid $border-soft;
    flex-shrink: 0;

    .Agg__items {
      padding: 20px 16px;
      height: auto;
      min-height: calc(100vh - 56px);

      .Agg__title {
        font-size: 15px;
        font-weight: 700;
        color: $text-primary;
        text-align: left;
        padding: 0 0 12px 4px;
        height: auto;
        line-height: normal;
        letter-spacing: 0.3px;
        position: relative;

        &::after {
          content: '';
          display: block;
          width: 24px;
          height: 3px;
          background: $brand;
          border-radius: 2px;
          margin-top: 8px;
        }
      }

      ::v-deep .warp {
        background: transparent;
        margin-bottom: 2px;

        .title__warp {
          background: $bg-soft;
          border-radius: 8px;
          border: none;
          padding: 8px 12px;
          font-size: 13px;
          font-weight: 600;
          color: $text-primary;
          transition: background 0.2s;

          &:hover {
            background: darken($bg-soft, 2%);
          }
        }
      }
    }
  }

  // ===== Right Content Area =====
  .Search__wrap {
    flex: 1;
    height: 100%;
    overflow-y: auto;
    padding: 20px 24px;
    position: relative;

    // ===== Sub Tabs =====
    .SubTabs {
      display: flex;
      gap: 0;
      margin-bottom: 20px;
      background: #fff;
      border-radius: 10px;
      padding: 4px;
      box-shadow: 0 1px 3px rgba(0,0,0,0.04);

      &__tab {
        flex: 1;
        padding: 10px 0;
        text-align: center;
        font-size: 14px;
        font-weight: 500;
        color: $text-muted;
        cursor: pointer;
        transition: all 0.25s ease;
        border-radius: 8px;
        position: relative;

        &:hover {
          color: $text-primary;
          background: $bg-soft;
        }

        &--active {
          color: #fff;
          font-weight: 600;
          background: linear-gradient(135deg, $brand, #4A56A8);
          box-shadow: 0 2px 8px rgba(47,58,145,0.20);
          border: none;
        }

        span {
          vertical-align: middle;
        }
      }
    }

    // ===== Search Bar (参考图风格 ~ pill shape with icon inside) =====
    .Search__content {
      margin-bottom: 16px;

      .Search__info {
        display: flex;
        align-items: center;
        background: #fff;
        border-radius: 50px;
        border: 2px solid $border-soft;
        transition: all 0.3s;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0,0,0,0.04);

        &:focus-within {
          border-color: $brand;
          box-shadow: 0 0 0 4px rgba(47,58,145,0.08), 0 4px 12px rgba(0,0,0,0.06);
        }

        input {
          flex: 1;
          border: none;
          outline: none;
          padding: 14px 20px;
          font-size: 15px;
          color: $text-primary;
          background: transparent;
          height: auto;

          &::placeholder {
            color: $text-muted;
          }
        }
        .Search__btn {
          display: flex;
          align-items: center;
          gap: 6px;
          padding: 10px 28px;
          margin: 4px;
          border-radius: 50px;
          background: linear-gradient(135deg, $brand, #4A56A8);
          color: #fff;
          font-size: 15px;
          font-weight: 500;
          cursor: pointer;
          transition: all 0.25s;
          white-space: nowrap;
          border: none;

          &:hover {
            transform: scale(1.03);
            box-shadow: 0 4px 14px rgba(47,58,145,0.30);
          }

          i { font-size: 16px; }
        }
      }
    }

    // ===== Filter Tags =====
    .Filterd__wrap {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      align-items: center;
      margin-bottom: 16px;
      padding: 12px 16px;
      background: #fff;
      border-radius: 10px;
      border: 1px solid $border-soft;

      > div:first-child {
        font-size: 12px;
        font-weight: 600;
        color: $text-muted;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-right: 4px;
      }

      .Filterd__item {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 6px 12px;
        background: $bg-soft;
        border-radius: 20px;
        font-size: 13px;
        color: $text-primary;

        span {
          color: $text-muted;
          font-weight: 500;
        }
        i {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 16px;
          height: 16px;
          border-radius: 50%;
          cursor: pointer;
          color: $text-muted;
          transition: all 0.2s;
          font-size: 10px;

          &:hover {
            background: rgba(0,0,0,0.08);
            color: $text-primary;
          }
        }
      }
      .Filterd__clean--btn {
        font-size: 12px;
        color: $brand;
        cursor: pointer;
        padding: 6px 10px;
        border-radius: 20px;
        transition: all 0.2s;
        font-weight: 500;

        i { margin-right: 4px; }

        &:hover {
          background: rgba(47,58,145,0.08);
        }
      }
    }

    // ===== Result Count =====
    .Total__wrap {
      margin-bottom: 16px;
      padding: 0 4px;

      .Total__text {
        font-size: 14px;
        color: $text-secondary;
        display: flex;
        align-items: center;
        gap: 4px;

        span {
          color: $brand;
          font-weight: 700;
          font-size: 16px;
        }
      }
    }

    // ===== Results Area =====
    .Result__wrap {
      width: 100%;
      height: auto;

      // ===== Rich Card (统一适用于所有4种类型) =====
      .RichCard {
        display: flex;
        gap: 14px;
        background: #fff;
        border-radius: 12px;
        padding: 18px 20px;
        margin-bottom: 12px;
        border: 1px solid $border-soft;
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
        animation: cardFadeIn 0.45s ease both;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 24px rgba(0,0,0,0.06);
          border-color: darken($border-soft, 8%);
        }

        &__index {
          width: 30px;
          height: 30px;
          flex-shrink: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 12px;
          font-weight: 700;
          color: #fff;
          background: linear-gradient(135deg, $brand, #5A66B8);
          border-radius: 8px;
          margin-top: 2px;
        }

        &__inner {
          flex: 1;
          display: flex;
          gap: 18px;
          min-width: 0;
        }

        &__pic {
          width: 180px;
          height: 120px;
          flex-shrink: 0;
          border-radius: 8px;
          overflow: hidden;
          background: $bg-soft;
          position: relative;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            display: block;
            transition: transform 0.4s;
          }

          &::after {
            content: '';
            position: absolute;
            inset: 0;
            background: linear-gradient(180deg, transparent 60%, rgba(0,0,0,0.10));
            pointer-events: none;
          }
        }

        &:hover &__pic img {
          transform: scale(1.06);
        }

        &__body {
          flex: 1;
          min-width: 0;
          display: flex;
          flex-direction: column;
          gap: 8px;
        }

        &__title {
          font-size: 16px;
          font-weight: 700;
          color: $text-primary;
          margin: 0;
          line-height: 1.5;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          transition: color 0.2s;

          &:hover {
            color: $brand;
          }
        }

        &__desc {
          font-size: 13px;
          color: $text-secondary;
          line-height: 1.6;
          margin: 0;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        &__meta {
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          gap: 8px 14px;
          margin-top: auto;
          padding-top: 8px;
        }

        &__tag {
          display: inline-block;
          padding: 3px 10px;
          border-radius: 20px;
          font-size: 11px;
          font-weight: 600;
          letter-spacing: 0.2px;

          &--type    { background: #eef2ff; color: $brand; }
          &--status  { background: #e4f7e9; color: #0c833e; }
          &--license { background: #fff3e0; color: #b85c00; }
          &--journal { background: #f3e8fd; color: #7c3aed; }
        }

        &__meta-item {
          font-size: 12px;
          color: $text-muted;
          white-space: nowrap;
          display: flex;
          align-items: center;
          gap: 3px;

          &--year {
            margin-left: auto;
            font-weight: 500;
            color: darken($text-muted, 10%);
          }
        }

        &__extra {
          display: flex;
          flex-wrap: wrap;
          gap: 14px;
          margin-top: 2px;

          &-item {
            font-size: 11px;
            color: darken($text-muted, 5%);
            font-family: 'SF Mono', 'Fira Code', 'Consolas', monospace;
          }
        }

        // ===== Article / Sciencedata (no-image variants) =====
        &--article,
        &--sciencedata {
          .RichCard__inner {
            padding-left: 4px;
          }
          .RichCard__body {
            gap: 6px;
          }
        }
      }
    }

    // ===== Pagination =====
    ::v-deep .base-pagination {
      margin: 24px auto 40px;
      display: flex;
      justify-content: center;

      button, .el-pager li {
        border-radius: 8px !important;
        border: 1px solid $border-soft !important;
        background: #fff !important;
        font-size: 13px !important;
        min-width: 36px;
        height: 36px;
        margin: 0 3px;
        transition: all 0.2s;

        &:hover {
          border-color: $brand !important;
          color: $brand !important;
        }

        &.active {
          background: $brand !important;
          border-color: $brand !important;
          color: #fff !important;
        }
      }
    }
  }
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(14px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
