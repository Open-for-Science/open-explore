import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from './locale/en'
import zh from './locale/zh'
import es from './locale/es'
import ru from './locale/ru'
import fr from './locale/fr'

Vue.use(VueI18n)

function getBrowserLang() {
  const navLang = (navigator.language || navigator.browserLanguage || '').toLowerCase()
  if (navLang.startsWith('zh')) return 'zh'
  if (navLang.startsWith('es')) return 'es'
  if (navLang.startsWith('ru')) return 'ru'
  if (navLang.startsWith('fr')) return 'fr'
  return 'en'
}

function getSavedLang() {
  const saved = localStorage.getItem('language')
  if (saved && ['zh', 'en', 'es', 'ru', 'fr'].includes(saved)) {
    return saved
  }
  // Check URL param
  const params = new URLSearchParams(window.location.search)
  const langParam = params.get('lang')
  if (langParam && ['zh', 'en', 'es', 'ru', 'fr'].includes(langParam)) {
    return langParam
  }
  return getBrowserLang()
}

const locale = getSavedLang()
localStorage.setItem('language', locale)

const i18n = new VueI18n({
  locale,
  fallbackLocale: 'en',
  messages: { en, zh, es, ru, fr }
})

export function switchLanguage(lang) {
  if (['zh', 'en', 'es', 'ru', 'fr'].includes(lang)) {
    i18n.locale = lang
    localStorage.setItem('language', lang)
  }
}

export default i18n
