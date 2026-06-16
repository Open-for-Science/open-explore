<template>
  <div id="app">
    <TopNav />
    <router-view/>
  </div>
</template>

<script>
import TopNav from './components/TopNav.vue'

export default {
  components: {
    TopNav
  }
}
</script>

<style lang="scss">
html,body,#app{
  width: 100%;
  height: 100%;
  padding: 0px;
  margin: 0px;
  overflow: hidden;
  letter-spacing: 1px;
}
#app{
  display: flex;
  flex-direction: column;
  > div:not(.TopNav){
    flex: 1;
    min-height: 0;
    overflow: auto;
  }
}
*::-webkit-scrollbar
{
    width: 5px;
    height: 3px;
    background-color: #ffffff;
}

/*定义滚动条轨道 内阴影+圆角*/
*::-webkit-scrollbar-track
{
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    border-radius: 10px;
    background-color: #F5F5F5;
}

/*定义滑块 内阴影+圆角*/
*::-webkit-scrollbar-thumb
{
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #bfbfbf;
}
</style>
