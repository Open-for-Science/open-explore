<template>
  <div class="LangSwitcher" @click.stop="toggleMenu">
    <div class="LangSwitcher__current">
      <span class="LangSwitcher__flag">{{ current.flag }}</span>
      <span class="LangSwitcher__label">{{ current.label }}</span>
      <svg class="LangSwitcher__arrow" :class="{ 'LangSwitcher__arrow--open': open }" viewBox="0 0 24 24" width="14" height="14" fill="currentColor">
        <path d="M7 10l5 5 5-5z"/>
      </svg>
    </div>
    <transition name="lang-fade">
      <div v-if="open" class="LangSwitcher__menu" @click.stop>
        <div
          class="LangSwitcher__option"
          v-for="lang in languages"
          :key="lang.code"
          :class="{ 'LangSwitcher__option--active': lang.code === currentCode }"
          @click="select(lang.code)"
        >
          <span class="LangSwitcher__flag">{{ lang.flag }}</span>
          <span>{{ lang.label }}</span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { switchLanguage } from '@/i18n'

const LANGUAGES = [
  { code: 'zh', label: '中文', flag: '🇨🇳' },
  { code: 'en', label: 'English', flag: '🇬🇧' },
  { code: 'es', label: 'Español', flag: '🇪🇸' },
  { code: 'ru', label: 'Русский', flag: '🇷🇺' },
  { code: 'fr', label: 'Français', flag: '🇫🇷' }
]

export default {
  name: 'LanguageSwitcher',
  data() {
    return {
      open: false,
      languages: LANGUAGES
    }
  },
  computed: {
    currentCode() {
      return this.$i18n.locale
    },
    current() {
      return this.languages.find(l => l.code === this.currentCode) || this.languages[1]
    }
  },
  mounted() {
    document.addEventListener('click', this.close)
  },
  beforeDestroy() {
    document.removeEventListener('click', this.close)
  },
  methods: {
    toggleMenu() {
      this.open = !this.open
    },
    close() {
      this.open = false
    },
    select(code) {
      if (code !== this.currentCode) {
        switchLanguage(code)
        this.$forceUpdate()
      }
      this.open = false
    }
  }
}
</script>

<style lang="scss" scoped>
.LangSwitcher {
  position: relative;
  user-select: none;
  margin-left: auto;
  &__current {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    border-radius: 6px;
    cursor: pointer;
    color: rgba(255,255,255,0.85);
    font-size: 14px;
    transition: background 0.2s;
    &:hover {
      background: rgba(255,255,255,0.15);
    }
  }
  &__flag {
    font-size: 18px;
    line-height: 1;
  }
  &__label {
    font-weight: 500;
  }
  &__arrow {
    transition: transform 0.2s;
    &--open {
      transform: rotate(180deg);
    }
  }
  &__menu {
    position: absolute;
    top: calc(100% + 4px);
    right: 0;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0,0,0,0.15);
    padding: 4px;
    min-width: 140px;
    z-index: 200;
    overflow: hidden;
  }
  &__option {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 14px;
    border-radius: 6px;
    cursor: pointer;
    font-size: 14px;
    color: #333;
    transition: background 0.15s;
    &:hover {
      background: #f0f2f8;
    }
    &--active {
      background: #ebedf8;
      color: #2F3A91;
      font-weight: 600;
    }
  }
}

.lang-fade-enter-active,
.lang-fade-leave-active {
  transition: opacity 0.15s, transform 0.15s;
}
.lang-fade-enter,
.lang-fade-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
