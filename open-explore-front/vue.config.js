const { defineConfig } = require('@vue/cli-service')
const {name} = require('./package')
const themeConfig = require('./src/theme')
const ThemeColorReplacer = require('webpack-theme-color-replacer')
const forElementUI = require('webpack-theme-color-replacer/forElementUI')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: '/',
  productionSourceMap:false,
  devServer:{
    proxy:{
      '/searchApi':{
        target:'http://10.0.11.117:8088',
        changeOrigin:true,
        pathRewrite:{
          '^/searchApi':''
        }
      }
    }
  },
  configureWebpack: {
    output: {
        library: `${name}-[name]`,
        libraryTarget: 'umd',// 把微应用打包成 umd 库格式
        chunkLoadingGlobal: `webpackJsonp_${name}`,
    },
    plugins: [
        new ThemeColorReplacer({
            matchColors:  themeConfig.getThemeColors(themeConfig.themeColor, forElementUI.getElementUISeries, ThemeColorReplacer.varyColor, []),
            // fileName: '/static/theme-colors-[contenthash:8].css', //optional. output css file name, suport [contenthash] and [hash].
            externalCssFiles: ['./node_modules/@zkwq/business/dist/var.scss'], // optional, String or string array. Set external css files (such as cdn css) to extract colors.
            changeSelector: forElementUI.changeSelector,
            injectCss: true, // optional. Inject css text into js file, no need to download `theme-colors-xxx.css` any more.
            isJsUgly: process.env.NODE_ENV !== 'development', // optional. Set to `true` if your js is uglified. Default is set by process.env.NODE_ENV.
        })
    ]
  }
})
