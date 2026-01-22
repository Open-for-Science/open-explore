import Vue from 'vue'
import App from './App.vue'
import routes from './router'
import store from './store'
import BaseUI from '@zkwq/business';
import '@zkwq/business/dist/index.min.css';
import VueRouter from 'vue-router'
import AppMixin from './mixins'
import client from 'webpack-theme-color-replacer/client'
import getElementUISeries from 'webpack-theme-color-replacer/forElementUI/getElementUISeries'
import themeConfig from './theme'
Vue.config.productionTip = false
Vue.use(VueRouter);
Vue.use(BaseUI);

let theme = {
  primary:'#fff'
}
client.changer.changeColor({
  newColors: themeConfig.getThemeColors(theme.primaryColor ? theme.primaryColor : themeConfig.themeColor, getElementUISeries, client.varyColor, []),
  appendToEl: document.body
}, Promise).then(() => {})
let router = new VueRouter({
  routes,
  mode: 'history'
})
Vue.mixin(AppMixin)
let instance = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
