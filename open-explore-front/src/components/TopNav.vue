<template>
  <div class="TopNav">
    <div class="TopNav__logo" @click="goHome">
      <i class="base-icon-discover"></i>
      <span>Open Explore</span>
    </div>
    <div class="TopNav__tabs">
      <div
        class="TopNav__tab"
        :class="{ 'TopNav__tab--active': activeTab === 'home' }"
        @click="switchTab('home')"
      >
        <i class="base-icon-discover"></i>
        <span>{{ $t('nav.home') }}</span>
      </div>
      <div
        class="TopNav__tab"
        :class="{ 'TopNav__tab--active': activeTab === 'search' }"
        @click="switchTab('search')"
      >
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px;">
          <circle cx="11" cy="11" r="8"/>
          <path d="M21 21l-4.35-4.35"/>
        </svg>
        <span>{{ $t('nav.search') }}</span>
      </div>
      <div
        class="TopNav__tab"
        :class="{ 'TopNav__tab--active': activeTab === 'about' }"
        @click="switchTab('about')"
      >
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px;">
          <circle cx="12" cy="12" r="10"/>
          <path d="M12 16v-4M12 8h.01"/>
        </svg>
        <span>{{ $t('nav.about') }}</span>
      </div>
    </div>
    <LanguageSwitcher />
  </div>
</template>

<script>
import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'TopNav',
  components: { LanguageSwitcher },
  computed: {
    activeTab() {
      const path = this.$route.path
      if (path === '/' || path.startsWith('/home')) {
        return 'home'
      } else if (path.startsWith('/about')) {
        return 'about'
      } else if (path.startsWith('/search') || path.startsWith('/article') || path.startsWith('/sciencedata') || path.startsWith('/software') || path.startsWith('/facility')) {
        return 'search'
      } else {
        return 'home'
      }
    }
  },
  methods: {
    goHome() {
      if (this.$route.path !== '/') {
        this.$router.push('/')
      }
    },
    switchTab(tab) {
      if (tab === this.activeTab) return
      if (tab === 'home') {
        this.$router.push('/')
      } else if (tab === 'search') {
        this.$router.push('/search')
      } else if (tab === 'about') {
        this.$router.push('/about')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@zkwq/business/dist/var.scss';

.TopNav {
  width: 100%;
  height: 56px;
  flex-shrink: 0;
  background: $--color-primary;
  display: flex;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 100;

  &__logo {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    margin-right: 48px;
    white-space: nowrap;
    cursor: pointer;

    i {
      font-size: 22px;
    }
  }

  &__tabs {
    display: flex;
    align-items: center;
    gap: 4px;
    height: 100%;
  }

  &__tab {
    display: flex;
    align-items: center;
    gap: 6px;
    height: 100%;
    padding: 0 20px;
    color: rgba(255, 255, 255, 0.75);
    font-size: 15px;
    cursor: pointer;
    transition: all 0.3s;
    border-bottom: 3px solid transparent;
    box-sizing: border-box;

    i {
      font-size: 16px;
    }

    &:hover {
      color: #fff;
      background: rgba(255, 255, 255, 0.1);
    }

    &--active {
      color: #fff;
      border-bottom-color: #fff;
      font-weight: bold;
    }
  }
}
</style>
