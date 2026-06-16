<template>
  <div class="Search__page">
    <div class="Agg__wrap">
      <base-scrollbar style="height: 100%;">
        <div class="Agg__items">
          <div class="Agg__title">
            Category
          </div>
          <base-skeleton v-if="aggsLoading" :rows="6" animated />
          <div v-if="!aggsLoading">
            <AggsItemV v-for="item,index in aggs" :key="'agg' + index" :title="item.alias" :datas="item.aggregations" :checkList.sync="item.selected_aggregation" @change="handleAggChange"></AggsItemV>
          </div>
        </div>
      </base-scrollbar>
    </div>
    <div class="Search__wrap">
      <div class="Search__content">
        <div class="Search__info">
          <input slot="reference" v-model="searchText" type="text" placeholder="Search Text" />
          <div class="Search__btn" @click="handleSearch">
            <i class="base-icon-search"></i>&nbsp;Search
          </div>
        </div>
      </div>
      <div class="Filterd__wrap" v-if="fiterConditions.length">
        <div>Filter Conditions</div>
        <div class="Filterd__item" v-for="item,index in fiterConditions" :key="'fiterConditions' + index">
          <span>{{item.label}}：</span>
          {{item.value}}
          <i class="base-icon-close" @click="handleClearSearch(item)"></i>
        </div>
        <div class="Filterd__clean--btn" @click="handleClearAll">
          <i class="base-icon-delete"></i>Clear All
        </div>
      </div>
      <div class="Total__wrap">
        <div class="Total__text">Found <span>{{total}}</span> results</div>
        
      </div>
      <div class="Result__wrap">
        <div class="Result__item" v-for="item,index in tableData" :key="'tableData' + index">
          <div class="Result__index">{{(searchParams.page - 1) * searchParams.size + index + 1}}</div>
          <ReadableItem :readable="item" @title-click="handleTitleClick(item)"></ReadableItem>
        </div>
      </div>
      <div style="margin-top:12px;width:100%;display:flex;justify-content: center;">
        <base-pagination
          background
          layout="prev, pager, next"
          :page-size="searchParams.size"
          @current-change="handleSearch()"
          :current-page.sync="searchParams.page"
          :total="total > 10000?10000:total">
        </base-pagination>
      </div>
      <ToTop />
    </div>
  </div>
</template>

<script>
import {AggsItemV,ReadableItem} from '@zkwq/business'
import { SearchArticle,SearchArticleAgg } from '@/service/ArticleSerach'
import ToTop from '@/components/ToTop.vue'

export default {
  components:{
    AggsItemV,ReadableItem,ToTop
  },
  data(){
    return {
      visible:false,
      total:0,
      tableData: [],
      loading: false,
      searchText:'',
      searchParams:{
        page: 1,
        size: 10,
        order_field: "_score"
      },
      aggs:[],
      queryText:'',
      aggsLoading:false
    }
  },
  created(){
    this.handleSearch();
  },
  computed:{
    fiterConditions(){
      let arr = [];
      if(this.queryText){
        arr.push({
          label: "检索文本",
          type: "query",
          value: this.queryText
        })
      }
      this.aggs.forEach(item => {
        if(item.selected_aggregation.length){
          item.selected_aggregation.forEach(item1 => {
            arr.push({
              label: item.alias,
              key: item.key,
              type: "agg",
              value: item1
            })
          })
        }
      })
      return arr;
    }
  },
  methods:{
    handleTitleClick(item){
      this.$router.push({
        path: '/article/detail/' + item.uuid
      })
    },
    handleAggChange(){
      this.searchParams.page = 1;
      this.handleSearch();
    },
    handleClearSearch(item){
      if(item.type == 'query'){
        this.searchText = '';
        this.queryText = '';
      }else if(item.type == 'agg'){
        this.aggs.forEach(item1 => {
          if(item1.key == item.key){
            item1.selected_aggregation.splice(item1.selected_aggregation.indexOf(item.value),1);
          }
        })  
      }
      this.searchParams.page = 1;
      this.handleSearch();
    },
    handleClearAll(){
      this.searchText = '';
      this.aggs.forEach(item => {
        item.selected_aggregation = [];
      })
      this.searchParams.page = 1;
      this.handleSearch();
    },
    buildAggregations(){
      let aggregations = {}
      if(this.aggs.length){
        this.aggs.forEach(item => {
          aggregations[item.key] = item.selected_aggregation.join(',')
        });
      }else{
      }
      return aggregations;
    },
    handleSearch(){
      this.loading = true;
      SearchArticle({
        query: this.searchText,
        page: this.searchParams.page,
        size: this.searchParams.size,
        order_field: this.searchParams.order_field,
        order_direction: "desc",
        aggregations: this.buildAggregations()
      }).then(res=>{
        if(res.error){
          this.$message.error(res.error);
          return;
        }
        let data = res.result.data
        this.total = data.total;
        this.tableData = data.content.map(e => {
          return {
            ...e,
            type: 'article'
          }
        });
        this.queryText = this.searchText;
      }).finally(()=>{
        this.loading = false;
      })
      this.handleSearchAgg();
    },
    handleSearchAgg(){
      if(this.searchParams.page > 1) return;
      this.aggsLoading = true;
      let aggregations = {}
      if(this.aggs.length){
        this.aggs.forEach(item => {
          aggregations[item.key] = item.selected_aggregation.join(',')
        });
      }else{
        aggregations = {
        }
      }
      SearchArticleAgg({
        query: this.searchText,
        page: this.searchParams.page,
        size: this.searchParams.size,
        order_field: this.searchParams.order_field,
        order_direction: "desc",
        aggregations: this.buildAggregations()
      }).then(res=>{
        if(res.error){
          this.$message.error(res.error);
          return;
        }
        let data = res.result.data
        this.aggs = Object.keys(data).map(key => {
          return {
            key,
            alias:key,
            aggregations: data[key].buckets,
            selected_aggregation: this.aggs.find(item => item.key == key)?.selected_aggregation || []
          }
        });
      }).finally(()=>{
        this.aggsLoading = false;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import '~@zkwq/business/dist/var.scss';
  .Search__page{
    width: 100%;
    height: 100%;
    overflow: hidden;
    padding: 12px;
    display: flex;
    gap: 16px;
    background: white;
    .Agg__wrap{
      width:360px;
      height: 100%;

      .Agg__items{
        background: #EEF1F4;
        width: 100%;
        height: auto;
        overflow: hidden;
        border-radius: 4px;
        padding: 4px 8px;
        min-height: calc(100vh - 56px - 24px - 64px);
        .Agg__title{
          width: 100%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          // color: #fff;
          font-size: 14px;
          font-weight: bold;
        }
        ::v-deep .warp{
          background: #EDF1F4;
          .title__warp{
            background: #fff;
            border-radius: 4px;
            border: none;
          }
        }
      }
    }
    .Search__wrap{
      width: 0px;
      flex: 1;
      height: 100%;
      overflow-y: auto;
      position: relative;
      .Search__content{
        width: 100%;
        height: 60px;
        border-radius: 4px;
        
        .Search__info{
          width: 100%;
          background: #fff;
          border-radius: 4px;
          height: 60px;
          display: flex;
          align-items: center;
          gap: 8px;
          border: 1px solid $--border-color-base;
          .Search__type{
            width: 120px;
            height: 60px;
            border-radius: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            font-size: 14px;
            color: $--color-text-regular;
            cursor: pointer;
            transition: all .5s;
            border:1px solid $--border-color-base;
          }
          
          .element__btn *{
            fill: $--color-primary;
          }
          .element__btn{
            cursor: pointer;
          }
          input{
            width: 100%;
            height: 100%;
            padding: 0px 8px;
          }
          .Search__btn{
            width: fit-content;
            padding: 0px 48px;
            height: 100%;
            background: $--color-primary;
            border-radius: 4px;
            display: flex;
            align-items: center;
            color: #fff;
            font-size: 18px;
            cursor: pointer;
          }
        }
      }
      .Filter__wrap{
        width: 100%;
        height: auto;
        overflow: visible;
        margin-top: 12px;
        border-radius: 4px;
        background: #fff;
        padding: 12px;
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
        .Filter__item{
          width: fit-content;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 4px;
          color: #fff;
          font-size: 14px;
          cursor: pointer;
        }
      }
      .Filterd__wrap{
        width: 100%;
        height: auto;
        overflow: hidden;
        margin-top: 12px;
        border-radius: 4px;
        background: #F4F5F9;
        padding: 12px;
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
        align-items: center;
        .Filterd__item{
          width: fit-content;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: black;
          font-size: 14px;
          background: #fff;
          padding: 0px 12px;
          span{
            font-size: 14px;
            color: $--color-text-regular;
          }
          i{
            display: inline-block;
            margin-left: 8px;
            cursor: pointer;
            fill: $--color-text-regular
          }
        }
        .Filterd__clean--btn{
          width: fit-content;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 4px;
          color: $--color-primary;
          font-size: 14px;
          cursor: pointer;
          gap: 4px;
          padding: 0px 12px;
          &:hover{
            background: $--color-primary;
            color: #fff;
          }
        }
      }
      .Total__wrap{
        width: 100%;
        height: 40px;
        font-size: 14px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 8px;
        padding: 0px 12px;
        .Total__text{
          color: $--color-text-regular;
          span{
            color: $--color-warning;
            display: inline-block;
            margin:0px 4px;
          }
        }
        .Total__pagination{
          width: 0px;
          flex: 1;
          display: flex;
          justify-content: flex-end;
        }
      }
      .Result__wrap{
        width: 100%;
        height: auto;
        overflow: hidden;
        margin-top: 12px;
        border-radius: 4px;
        background: #fff;
        padding: 12px;
        .Result__item{
          margin-bottom: 12px;
          display: flex;
          gap: 12px;
          .Result__index{
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: $--color-text-regular;
            background: $--color-primary;
            color: #fff;
            border-radius: 4px;
            margin-top: 4px;
          }
        }
      }
    }
  }
  .Search__list{
    width: 150px;
    height: auto;
    overflow: hidden;
    .Search__item{
      width: 100%;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      color: $--color-text-regular;
      cursor: pointer;
      &:hover{
        background: $--color-primary;
        color: #fff;
      }
    }
  }
</style>