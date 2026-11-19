<template>
  <div class="Explore__page">
    <div class="ContentLayout">
      <!-- 左侧聚合筛选 -->
      <aside class="ContentLayout__sideColumn">
        <div class="AppSearchAggregation">
          <div class="AppSearchAggregation__label">
            <i class="base-icon-menu"></i>{{ $t('search.filterTitle') }}
          </div>
          <base-skeleton v-if="aggsLoading" :rows="6" animated />
          <template v-else>
            <AggsItemV
              v-for="item,index in aggs"
              :key="'agg' + index"
              :title="item.alias"
              :datas="item.aggregations"
              :checkList.sync="item.selected_aggregation"
              @change="handleAggChange"
            />
          </template>
        </div>
      </aside>

      <!-- 右侧结果区 -->
      <div class="ContentLayout__mainColumn">
        <!-- 资源类型 Tab -->
        <div class="AppSearchTabsShell">
          <div class="AppSearchTabs">
            <div
              v-for="t in resourceTabs"
              :key="t.key"
              class="AppSearchTab"
              :class="{ 'is-active': activeTab === t.key }"
              @click="switchResourceTab(t.key)"
            >
              {{ $t('search.tab.' + t.key) }}
            </div>
          </div>
        </div>

        <!-- 筛选栏 -->
        <div class="AppSearchFilters">
          <div class="AppFilterMeta MetaCounting">
            {{ $t('search.totalResults', { n: total }) }}
          </div>
          <div class="AppSearchFilterItems">
            <div class="AppFilterInput">
              <base-input
                v-model="searchText"
                size="small"
                :ripple="false"
                :show-label="false"
                :placeholder="currentTabPlaceholder"
                @key-up-enter="handleSearch"
              >
                <i
                  class="base-icon-search base-input__icon"
                  slot="suffix"
                  :class="{ 'is-disabled': !searchText }"
                  @mousedown.prevent="handleSearch"
                ></i>
              </base-input>
            </div>
            <button type="button" class="AppSearchSubmit" @click="handleSearch">
              {{ $t('search.searchBtn') }}
            </button>
          </div>
        </div>

        <!-- 已选筛选条件 -->
        <div class="AppSearchRefineItems" v-if="fiterConditions.length">
          <div class="AppSearchRefineLabel">{{ $t('search.filterConditions') }}</div>
          <div class="AppSearchRefineItem" v-for="item,index in fiterConditions" :key="'fiterConditions' + index">
            <span class="AppSearchRefineItemText">{{ item.label }}：{{ item.value }}</span>
            <i class="base-icon-close" @click="handleClearSearch(item)"></i>
          </div>
          <div class="AppSearchRefineAction" @click="handleClearAll">
            <i class="base-icon-delete"></i>{{ $t('search.clearAll') }}
          </div>
        </div>

        <!-- 结果列表 -->
        <div class="List">
          <div
            class="List__item"
            v-for="(item,index) in tableData"
            :key="'tableData' + index"
            @click="handleTitleClick(item)"
          >
            <div class="List__itemIndex">{{ (searchParams.page - 1) * searchParams.size + index + 1 }}.</div>
            <div class="List__itemBody">
              <div class="List__itemInner">
                <div class="List__itemMain">
                  <h3 class="List__itemTitle">{{ item.title }}</h3>

                  <p class="List__itemDesc" v-if="activeTab === 'facility' && item.description_information">{{ truncate(item.description_information, 180) }}</p>
                  <p class="List__itemDesc" v-if="activeTab === 'software' && item.description">{{ truncate(item.description, 180) }}</p>
                  <p class="List__itemDesc" v-if="activeTab === 'article' && item.abstracts && item.abstracts !== 'null'">{{ truncate(item.abstracts, 220) }}</p>
                  <p class="List__itemDesc" v-if="activeTab === 'sciencedata' && (item.description || item.abstracts)">{{ truncate(item.description || item.abstracts, 220) }}</p>

                  <div class="List__itemMeta">
                    <!-- 设施 -->
                    <template v-if="activeTab === 'facility'">
                      <span class="List__itemTag" v-if="item.country">{{ item.country }}</span>
                      <span class="List__itemMetaItem" v-if="item.hosting_institution">{{ item.hosting_institution }}</span>
                      <span class="List__itemMetaItem" v-if="item.cstr">{{ item.cstr }}</span>
                    </template>
                    <!-- 软件 -->
                    <template v-if="activeTab === 'software'">
                      <span class="List__itemTag" v-if="item.category">{{ item.category }}</span>
                      <span class="List__itemTag" v-if="item.license">{{ item.license }}</span>
                      <span class="List__itemMetaItem" v-if="item.organization">{{ item.organization }}</span>
                      <span class="List__itemMetaItem List__itemMetaItem--year" v-if="item['pub-year']">{{ item['pub-year'] }}</span>
                    </template>
                    <!-- 文献 -->
                    <template v-if="activeTab === 'article'">
                      <span class="List__itemTag" v-if="item.type">{{ item.type }}</span>
                      <span class="List__itemTag" v-if="item.journal">{{ item.journal }}</span>
                      <span class="List__itemMetaItem" v-if="item.author && item.author.length">{{ item.author.slice(0,3).join('; ') }}{{ item.author.length > 3 ? ' et al.' : '' }}</span>
                      <span class="List__itemMetaItem" v-if="item.publisher">{{ item.publisher }}</span>
                      <span class="List__itemMetaItem List__itemMetaItem--year" v-if="item.year || item['pub-year']">{{ item.year || item['pub-year'] }}</span>
                    </template>
                    <!-- 科学数据 -->
                    <template v-if="activeTab === 'sciencedata'">
                      <span class="List__itemTag" v-if="item['dataset-type']">{{ item['dataset-type'] }}</span>
                      <span class="List__itemMetaItem" v-if="item.author && item.author.length">{{ item.author.slice(0,2).join('; ') }}</span>
                      <span class="List__itemMetaItem List__itemMetaItem--year" v-if="item['pub-year']">{{ item['pub-year'] }}</span>
                    </template>
                  </div>

                  <div class="List__itemExtra" v-if="(activeTab === 'article' || activeTab === 'sciencedata') && (item.doi || item.issn)">
                    <span v-if="item.doi">DOI: {{ item.doi }}</span>
                    <span v-if="item.issn">ISSN: {{ item.issn }}</span>
                  </div>
                </div>

                <div class="List__itemCover" v-if="(activeTab === 'facility' || activeTab === 'software') && item.image">
                  <img :src="item.image" :alt="item.title" @error="e => e.target.parentNode.style.display = 'none'" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="AppSearchPagination">
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
  article: {
    subject: 'search.agg.subject',
    journal: 'search.agg.journal',
    year: 'search.agg.year',
    type: 'search.agg.type'
  },
  facility: {
    country: 'search.agg.country',
    hosting_institution: 'search.agg.institution'
  },
  software: {
    resource_type: 'search.agg.resourceType',
    category: 'search.agg.category',
    language_main: 'search.agg.language',
    license: 'search.agg.license',
    pub_year: 'search.agg.year'
  },
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
          alias: aliasMap[key] ? this.$t(aliasMap[key]) : key,
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
// ===== 设计变量（对齐 pubscholar ExplorePageV2 资源浏览风格）=====
$accent: #1d4ed8;
$accent-2: #0f86c6;
$text-main: #0f172a;
$text-sub: #64748b;
$text-mute: #95a3b8;
$line: #dfe8f5;
$card-bg: rgba(255, 255, 255, 0.92);

.Explore__page {
  width: 100%;
  height: 100%;
  display: flex;
  overflow: hidden;
  box-sizing: border-box;
  background:
    radial-gradient(circle at 8% 0%, rgba(29, 78, 216, 0.16) 0, rgba(29, 78, 216, 0) 34%),
    radial-gradient(circle at 92% 8%, rgba(8, 145, 178, 0.14) 0, rgba(8, 145, 178, 0) 32%),
    linear-gradient(180deg, #f5f8fc 0%, #eef4fb 46%, #f8fafc 100%);
}

.ContentLayout {
  flex: 1;
  min-width: 0;
  min-height: 0;
  display: flex;
  align-items: flex-start;
  gap: 24px;
  padding: 24px;
  box-sizing: border-box;
}

// ===== 左侧聚合 =====
.ContentLayout__sideColumn {
  flex: 0 0 280px;
  width: 280px;
  max-height: 100%;
  overflow-y: auto;
  padding-right: 2px;
  box-sizing: border-box;
}

.AppSearchAggregation {
  &__label {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 0 4px 10px;
    color: $text-sub;
    font-size: 13px;
    font-weight: 600;
    letter-spacing: 0.5px;

    i {
      font-size: 14px;
    }
  }

  ::v-deep .warp {
    margin-bottom: 16px;
    border: 1px solid rgba(223, 232, 245, 0.96);
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 12px 34px rgba(15, 23, 42, 0.06);
    overflow: hidden;

    &:last-child {
      margin-bottom: 0;
    }

    .title__warp {
      min-height: 44px;
      padding: 10px 12px;
      border: none;
      background: linear-gradient(180deg, rgba(255, 255, 255, 0.98), rgba(248, 251, 255, 0.94));
      color: #1f4f9a;
      font-size: 14px;
      font-weight: 700;
      letter-spacing: 0;
    }

    .title__warp .icon {
      background: transparent;
      padding: 0;
      color: #7b8aa3;
    }

    .Search__wrap {
      padding: 8px 12px;
    }

    .item_warp {
      list-style: none;
      margin: 0;
      padding: 6px 6px 8px;

      li {
        padding: 8px 6px;
        border-radius: 10px;
        transition: background-color 0.2s;

        &:first-of-type {
          margin-top: 0;
        }

        &:last-of-type {
          margin-bottom: 0;
        }

        &:hover {
          background: #f1f7ff;
        }

        .item {
          width: 16px;
          margin-right: 8px;
        }

        .text {
          color: #1f2937;
          font-size: 14px;
        }

        .num {
          margin-left: 12px;
          color: $text-mute;
          font-size: 13px;
        }
      }

      .base-checkbox__inner {
        border: 1px solid #cfd9e8;
        border-radius: 6px;
        background: #fff;
      }

      .base-checkbox__input.is-checked .base-checkbox__inner {
        border-color: $accent;
        background: $accent;
      }
    }

    .readmore {
      color: $text-mute;

      &:hover {
        color: $accent;
      }
    }
  }
}

// ===== 右侧主区 =====
.ContentLayout__mainColumn {
  flex: 1;
  min-width: 0;
  max-height: 100%;
  overflow-y: auto;
  padding-right: 4px;
  box-sizing: border-box;
}

// ===== 资源类型 Tab =====
.AppSearchTabsShell {
  display: flex;
  align-items: center;
  padding: 8px;
  margin-bottom: 16px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.68);
  backdrop-filter: blur(16px);
  overflow: hidden;
}

.AppSearchTabs {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;

  &::-webkit-scrollbar {
    display: none;
  }
}

.AppSearchTab {
  flex: 0 0 auto;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  min-width: 92px;
  padding: 0 16px;
  border: 1px solid transparent;
  border-radius: 14px;
  color: #52647a;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.25s ease;

  &:hover {
    border-color: rgba(29, 78, 216, 0.2);
    background: rgba(255, 255, 255, 0.88);
    color: $accent;
  }

  &.is-active {
    border-color: rgba(29, 78, 216, 0.22);
    background: linear-gradient(135deg, $accent 0%, $accent-2 100%);
    color: #fff;
    box-shadow: 0 14px 28px rgba(29, 78, 216, 0.22);
  }
}

// ===== 筛选栏 =====
.AppSearchFilters {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px 14px;
  padding: 16px;
  margin-bottom: 16px;
  border-radius: 20px;
  background: $card-bg;
  backdrop-filter: blur(14px);
}

.AppFilterMeta {
  color: $text-sub;
  font-size: 14px;

  span,
  b {
    color: $text-main;
    font-weight: 700;
  }
}

.MetaCounting {
  display: flex;
  align-items: center;
  padding: 0 16px 0 4px;
}

.AppSearchFilterItems {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px 12px;
  margin-left: auto;
}

.AppFilterInput {
  width: 260px;

  ::v-deep .base-input {
    width: 100%;
    border-radius: 10px;

    &.is-focused {
      border-radius: 10px;
    }
  }

  ::v-deep .base-input__icon {
    cursor: pointer;

    &.is-disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  }
}

.AppSearchSubmit {
  height: 32px;
  padding: 0 22px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, $accent 0%, $accent-2 100%);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 8px 18px rgba(29, 78, 216, 0.2);

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 12px 22px rgba(29, 78, 216, 0.26);
  }
}

// ===== 已选条件 =====
.AppSearchRefineItems {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  padding: 16px 16px 3px;
  margin-bottom: 16px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.88);
}

.AppSearchRefineLabel {
  margin-right: 16px;
  margin-bottom: 13px;
  color: #475569;
  font-weight: 700;
  font-size: 14px;
}

.AppSearchRefineItem {
  display: flex;
  align-items: center;
  margin-right: 13px;
  margin-bottom: 13px;
  padding: 5px 10px 5px 12px;
  border: 1px solid #dbeafe;
  border-radius: 999px;
  background: #f8fbff;

  .base-icon-close {
    margin-left: 8px;
    color: #94a3b8;
    cursor: pointer;
    transition: color 0.2s;

    &:hover {
      color: $accent;
    }
  }
}

.AppSearchRefineItemText {
  max-width: 260px;
  display: inline-block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #1f2937;
  font-size: 13px;
}

.AppSearchRefineAction {
  margin-bottom: 13px;
  padding: 5px 12px;
  border-radius: 999px;
  color: $accent;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;

  i {
    margin-right: 5px;
  }

  &:hover {
    background: #eef4ff;
  }
}

// ===== 结果列表 =====
.List__item {
  position: relative;
  display: flex;
  padding: 16px;
  margin-bottom: 16px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.94);
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;

  &:last-child {
    margin-bottom: 0;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 18px 40px rgba(15, 23, 42, 0.1);
  }
}

.List__itemIndex {
  flex: 0 0 40px;
  width: 40px;
  padding-top: 2px;
  color: $text-mute;
  font-size: 15px;
}

.List__itemBody {
  flex: 1;
  min-width: 0;
}

.List__itemInner {
  display: flex;
  gap: 18px;
}

.List__itemMain {
  flex: 1;
  min-width: 0;
}

.List__itemTitle {
  margin: 0 0 8px;
  color: $text-main;
  font-size: 17px;
  font-weight: 700;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.2s;

  &:hover {
    color: $accent;
  }
}

.List__itemDesc {
  margin: 0 0 10px;
  color: #475569;
  font-size: 14px;
  line-height: 1.7;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.List__itemMeta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px 12px;
}

.List__itemTag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 999px;
  background: #eef2ff;
  color: $accent;
  font-size: 12px;
  font-weight: 600;
}

.List__itemMetaItem {
  color: $text-mute;
  font-size: 13px;
  white-space: nowrap;

  &--year {
    color: #7b8aa3;
    font-weight: 600;
  }
}

.List__itemExtra {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 8px;
  color: $text-mute;
  font-size: 12px;
  font-family: 'SF Mono', 'Fira Code', 'Consolas', monospace;
}

.List__itemCover {
  flex: 0 0 132px;
  width: 132px;
  height: 92px;
  border-radius: 12px;
  overflow: hidden;
  background: #f6faff;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
    transition: transform 0.4s ease;
  }
}

.List__item:hover .List__itemCover img {
  transform: scale(1.05);
}

// ===== 分页 =====
.AppSearchPagination {
  padding: 20px 0 8px;
  display: flex;
  justify-content: center;

  ::v-deep .base-pagination {
    display: flex;
    justify-content: center;
  }

  ::v-deep .base-pagination button,
  ::v-deep .base-pagination .base-pager li {
    border-radius: 10px;
    border: 1px solid #e2e8f2;
    background: #fff;
    transition: all 0.2s;

    &:hover {
      color: $accent;
      border-color: rgba(29, 78, 216, 0.3);
    }

    &.active {
      color: #fff;
      border-color: $accent;
      background: linear-gradient(135deg, $accent 0%, $accent-2 100%);
    }
  }
}

// ===== 自适应 =====
@media (max-width: 1180px) {
  .ContentLayout {
    gap: 16px;
    padding: 16px;
  }

  .ContentLayout__sideColumn {
    flex: 0 0 240px;
    width: 240px;
  }

  .AppFilterInput {
    width: 200px;
  }
}

@media (max-width: 900px) {
  .Explore__page {
    overflow-y: auto;
  }

  .ContentLayout {
    flex-direction: column;
    align-items: stretch;
  }

  .ContentLayout__sideColumn {
    flex: none;
    width: 100%;
    max-height: none;
    overflow: visible;
  }

  .ContentLayout__mainColumn {
    max-height: none;
    overflow: visible;
  }
}
</style>
