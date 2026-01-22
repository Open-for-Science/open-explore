import {BaseUtil,Base64Util} from '@zkwq/business'
import {MOBILE_CLIENT_WIDTH} from '@/constants'
import { mapState } from 'vuex'
export default{
  data(){
    return {
      isMobileClient:false,
      HKY_PREFIX_LOGO:"https://scholarin.cn/file/downloadbyfastdfspath?fastdfspath=",
    }
  },
  created(){
    this.isMobileClient = document.body.clientWidth <= MOBILE_CLIENT_WIDTH
  },
  computed:{
  },
  methods:{
    gmv(expressionFun,trueVal,falseVal = false){
      try{
        if(typeof expressionFun === 'function'){
          let result = expressionFun();
          return result?(trueVal || result):falseVal
        }
        return expressionFun?(trueVal || expressionFun):falseVal
      }catch(ex){
        console.warn('$gmv',ex)
        return falseVal;
      }
    },
    isArrayEmpty:BaseUtil.isArrayEmpty,
    formatDisplayNumber:BaseUtil.formatNumber,
    arrayContains:BaseUtil.isArrayContains,
    openUrl(url){
      window.open(url)
    },
    clearHtml(str){
      str = str?.replace(/<[^>]+>/g, "");
      str = str?.replace(/&nbsp;/ig, "");
      return str;
    }
  }
}