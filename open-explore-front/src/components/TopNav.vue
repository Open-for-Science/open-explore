<template>
  <header class="TopNav TopNav--presentation">
    <div class="TopNav__inner">
      <button class="TopNav__logo" type="button" aria-label="Open Explore home" @click="goHome">
        <span class="TopNav__logoMark" aria-hidden="true">
          <img :src="logoSrc" alt="" />
        </span>
        <span class="TopNav__logoText">Open <strong>Explore</strong></span>
      </button>

      <nav class="TopNav__tabs" aria-label="Primary navigation">
        <button
          class="TopNav__tab"
          :class="{ 'TopNav__tab--active': activeTab === 'home' }"
          :aria-current="activeTab === 'home' ? 'page' : null"
          type="button"
          @click="switchTab('home')"
        >
          {{ $t('nav.home') }}
        </button>
        <button
          class="TopNav__tab"
          :class="{ 'TopNav__tab--active': activeTab === 'search' }"
          :aria-current="activeTab === 'search' ? 'page' : null"
          type="button"
          @click="switchTab('search')"
        >
          {{ activeTab === 'home' ? $t('home.discovery') : $t('nav.search') }}
        </button>
        <button
          class="TopNav__tab"
          :class="{ 'TopNav__tab--active': activeTab === 'about' }"
          :aria-current="activeTab === 'about' ? 'page' : null"
          type="button"
          @click="switchTab('about')"
        >
          {{ $t('nav.about') }}
        </button>
      </nav>

      <div class="TopNav__actions">
        <LanguageSwitcher />
      </div>
    </div>
  </header>
</template>

<script>
import LanguageSwitcher from './LanguageSwitcher.vue'
import logoSrc from '../assets/logo.png'

export default {
  name: 'TopNav',
  components: { LanguageSwitcher },
  data() {
    return { logoSrc }
  },
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
.TopNav {
  position: relative;
  z-index: 100;
  width: 100%;
  height: 72px;
  flex-shrink: 0;
  background: rgba(255, 255, 255, 0.96);
  border-bottom: 1px solid #e8ecf2;
  box-shadow: 0 2px 12px rgba(16, 24, 40, 0.04);
  backdrop-filter: blur(16px);
  font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
  letter-spacing: normal;

  &__inner {
    display: flex;
    align-items: center;
    gap: 40px;
    width: 100%;
    height: 100%;
    padding: 0 32px;
    box-sizing: border-box;
  }

  &__logo {
    display: inline-flex;
    align-items: center;
    flex-shrink: 0;
    gap: 11px;
    padding: 0;
    border: 0;
    background: transparent;
    white-space: nowrap;
    cursor: pointer;
  }

  &--presentation {
    height: 64px;
    background: linear-gradient(100deg, #fbfbffed, #eef4ffed);
    box-shadow: none;
    border-bottom-color: #ffffff9c;
    .TopNav__inner { max-width: none; padding-inline: 40px; gap: 62px; }
    @media (max-width: 1200px) { .TopNav__inner { padding-inline: 36px; gap: 40px; } }
    @media (max-width: 980px) { .TopNav__inner { padding-inline: 24px; gap: 30px; } }
    @media (max-width: 640px) { .TopNav__inner { padding-inline: 24px; gap: 20px; } }
    @media (max-width: 380px) { .TopNav__inner { padding-inline: 16px; gap: 14px; } }
  }

  &__logoMark {
    display: grid;
    place-items: center;
    width: 42px;
    height: 42px;
    flex-shrink: 0;
    transition: transform 0.2s ease;

    img {
      display: block;
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }

  &__logo:hover &__logoMark {
    transform: translateY(-1px);
  }

  &__logoText {
    color: #344054;
    font-size: 19px;
    font-weight: 500;
    letter-spacing: -0.45px;

    strong {
      color: #101828;
      font-weight: 700;
    }
  }

  &__tabs {
    display: flex;
    align-items: center;
    align-self: stretch;
    flex-shrink: 0;
    gap: 32px;
  }

  &__tab {
    position: relative;
    display: flex;
    align-items: center;
    height: 100%;
    padding: 0;
    border: 0;
    background: transparent;
    color: #667085;
    font-family: inherit;
    font-size: 14px;
    font-weight: 500;
    white-space: nowrap;
    cursor: pointer;
    transition: color 0.2s ease;

    &::after {
      content: '';
      position: absolute;
      right: 0;
      bottom: 0;
      left: 0;
      height: 2px;
      border-radius: 2px 2px 0 0;
      background: #2856b8;
      transform: scaleX(0);
      transition: transform 0.2s ease, background 0.2s ease;
    }

    &:hover {
      color: #1d2939;

      &::after {
        background: #c7d3e5;
        transform: scaleX(1);
      }
    }

    &--active,
    &--active:hover {
      color: #2856b8;

      &::after {
        background: #2856b8;
        transform: scaleX(1);
      }
    }
  }

  &__actions {
    display: flex;
    align-items: center;
    margin-left: auto;
  }

  button:focus-visible {
    outline: 3px solid rgba(40, 100, 220, 0.24);
    outline-offset: 3px;
  }

  @media (max-width: 900px) {
    &__inner {
      gap: 24px;
      padding: 0 20px;
    }

    &__tabs {
      gap: 24px;
    }
  }

  @media (max-width: 640px) {
    height: 64px;

    &__inner {
      display: flex;
      gap: 12px;
      padding: 0 14px;
    }

    &__logoText {
      display: none;
    }

    &__logoMark {
      width: 36px;
      height: 36px;
    }

    &__tabs {
      flex: 1;
      justify-content: flex-start;
      gap: 18px;
    }

    &__tab {
      font-size: 13px;
    }
  }

  @media (max-width: 380px) {
    &__inner {
      gap: 10px;
      padding: 0 10px;
    }

    &__tabs {
      gap: 14px;
    }
  }

  @media (prefers-reduced-motion: reduce) {
    &__logoMark,
    &__tab {
      transition: none;
    }
  }
}
</style>
