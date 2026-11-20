<template>
  <div class="LangSwitcher" @keydown.esc="close">
    <button
      class="LangSwitcher__current"
      type="button"
      aria-haspopup="true"
      :aria-expanded="open"
      @click.stop="toggleMenu"
    >
      <svg class="LangSwitcher__globe" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" aria-hidden="true">
        <circle cx="12" cy="12" r="9" />
        <path d="M3 12h18M12 3a14 14 0 0 1 0 18M12 3a14 14 0 0 0 0 18" />
      </svg>
      <span class="LangSwitcher__label">{{ current.label }}</span>
      <svg class="LangSwitcher__arrow" :class="{ 'LangSwitcher__arrow--open': open }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
        <path d="m8 10 4 4 4-4" />
      </svg>
    </button>
    <transition name="lang-fade">
      <div v-if="open" class="LangSwitcher__menu" @click.stop>
        <button
          class="LangSwitcher__option"
          v-for="lang in languages"
          :key="lang.code"
          :class="{ 'LangSwitcher__option--active': lang.code === currentCode }"
          type="button"
          @click="select(lang.code)"
        >
          <span>{{ lang.label }}</span>
          <svg v-if="lang.code === currentCode" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
            <path d="m5 12 4 4L19 6" />
          </svg>
        </button>
      </div>
    </transition>
  </div>
</template>

<script>
import { switchLanguage } from '@/i18n'

const LANGUAGES = [
  { code: 'zh', label: '中文' },
  { code: 'en', label: 'English' },
  { code: 'es', label: 'Español' },
  { code: 'ru', label: 'Русский' },
  { code: 'fr', label: 'Français' }
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
  font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
  letter-spacing: normal;

  &__current {
    display: flex;
    align-items: center;
    gap: 8px;
    height: 40px;
    padding: 0 12px;
    border: 1px solid #e1e6ed;
    border-radius: 10px;
    background: #fff;
    color: #475467;
    font-family: inherit;
    font-size: 13px;
    cursor: pointer;
    transition: border-color 0.2s ease, background 0.2s ease, box-shadow 0.2s ease;

    &:hover,
    &[aria-expanded="true"] {
      border-color: #c7d3e5;
      background: #f8faff;
      box-shadow: 0 2px 6px rgba(16, 24, 40, 0.04);
    }

    &:focus-visible {
      outline: 3px solid rgba(40, 100, 220, 0.24);
      outline-offset: 3px;
    }
  }

  &__globe {
    width: 18px;
    height: 18px;
    color: #52637a;
  }

  &__label {
    font-weight: 500;
  }

  &__arrow {
    width: 14px;
    height: 14px;
    margin-left: 2px;
    color: #98a2b3;
    transition: transform 0.2s ease;

    &--open {
      transform: rotate(180deg);
    }
  }

  &__menu {
    position: absolute;
    top: calc(100% + 10px);
    right: 0;
    z-index: 200;
    min-width: 168px;
    padding: 6px;
    overflow: hidden;
    background: #fff;
    border: 1px solid #e7ebf0;
    border-radius: 12px;
    box-shadow: 0 12px 32px rgba(16, 24, 40, 0.14);
    box-sizing: border-box;
  }

  &__option {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
    width: 100%;
    padding: 10px 12px;
    border: 0;
    border-radius: 7px;
    background: transparent;
    color: #475467;
    font-family: inherit;
    font-size: 13px;
    text-align: left;
    cursor: pointer;
    transition: color 0.15s ease, background 0.15s ease;

    svg {
      width: 16px;
      height: 16px;
    }

    &:hover {
      background: #f4f7fb;
      color: #1d2939;
    }

    &--active {
      background: #edf3ff;
      color: #2856b8;
      font-weight: 600;
    }

    &:focus-visible {
      outline: 2px solid rgba(40, 100, 220, 0.3);
      outline-offset: -2px;
    }
  }
}

.lang-fade-enter-active,
.lang-fade-leave-active {
  transition: opacity 0.15s ease, transform 0.15s ease;
}

.lang-fade-enter,
.lang-fade-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

@media (max-width: 640px) {
  .LangSwitcher {
    &__current {
      gap: 4px;
      height: 38px;
      padding: 0 9px;
      border-radius: 9px;
    }

    &__label {
      display: none;
    }

    &__arrow {
      width: 12px;
      height: 12px;
      margin-left: 0;
    }
  }
}

@media (prefers-reduced-motion: reduce) {
  .LangSwitcher__arrow,
  .lang-fade-enter-active,
  .lang-fade-leave-active {
    transition: none;
  }
}
</style>
