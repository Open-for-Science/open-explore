<template>
  <div class="AboutPage">
    <!-- Hero 横幅 -->
    <section class="AboutHero">
      <div class="AboutHero__overlay"></div>
      <div class="AboutHero__inner">
        <h1 class="AboutHero__title">{{ aboutData.title }}</h1>
        <p class="AboutHero__subtitle">{{ aboutData.subtitle }}</p>
      </div>
    </section>

    <!-- 简介 -->
    <section class="AboutSection" v-if="aboutData.introduction">
      <div class="AboutSection__inner">
        <div class="AboutSection__content">
          <p v-for="(p, i) in aboutData.introduction" :key="'intro-' + i" class="AboutSection__text">{{ p }}</p>
        </div>
      </div>
    </section>

    <!-- 贡献人员 -->
    <section class="AboutSection AboutSection--alt" v-if="aboutData.contributors && aboutData.contributors.length">
      <div class="AboutSection__inner">
        <h2 class="AboutSection__heading">{{ aboutData.contributorsTitle || 'Contributors' }}</h2>
        <div class="ContributorsGrid">
          <div
            class="ContributorCard"
            v-for="(person, idx) in aboutData.contributors"
            :key="'contrib-' + idx"
          >
            <div class="ContributorCard__avatar">
              <img :src="person.avatar" :alt="person.name" @error="onAvatarError" />
            </div>
            <div class="ContributorCard__info">
              <span class="ContributorCard__name">{{ person.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 底部 -->
    <footer class="AboutFooter">
      <p>{{ aboutData.footer || '' }}</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'AboutUs',
  data() {
    return {
      aboutData: {
        title: '',
        subtitle: '',
        introduction: [],
        contributors: [],
        contributorsTitle: '',
        footer: ''
      }
    }
  },
  created() {
    this.refreshAboutData()
  },
  watch: {
    '$i18n.locale': 'refreshAboutData'
  },
  methods: {
    refreshAboutData() {
      this.aboutData = {
        title: this.$t('about.title'),
        subtitle: this.$t('about.subtitle'),
        introduction: this.$t('about.introduction'),
        contributors: this.$t('about.contributors'),
        contributorsTitle: this.$t('about.contributorsTitle'),
        footer: this.$t('about.footer')
      }
    },
    onAvatarError(e) {
      e.target.src = 'data:image/svg+xml,%3Csvg xmlns%3D%22http%3A//www.w3.org/2000/svg%22 width%3D%2280%22 height%3D%2280%22 viewBox%3D%220 0 80 80%22%3E%3Crect fill%3D%22%23e5e7eb%22 width%3D%2280%22 height%3D%2280%22/%3E%3Ctext x%3D%2240%22 y%3D%2240%22 text-anchor%3D%22middle%22 dominant-baseline%3D%22central%22 fill%3D%22%239ca3af%22 font-size%3D%2224%22%3E%3F%3C/text%3E%3C/svg%3E'
    }
  }
}
</script>

<style scoped>
.AboutPage {
  min-height: 100vh;
  background: #fff;
}

/* Hero */
.AboutHero {
  position: relative;
  background: linear-gradient(135deg, #1a237e 0%, #283593 30%, #3949ab 70%, #5c6bc0 100%);
  padding: 80px 24px 64px;
  text-align: center;
  overflow: hidden;
}
.AboutHero__overlay {
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 30% 50%, rgba(255,255,255,0.08) 0%, transparent 60%);
  pointer-events: none;
}
.AboutHero__inner {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
}
.AboutHero__title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
}
.AboutHero__subtitle {
  font-size: 18px;
  color: rgba(255,255,255,0.8);
  line-height: 1.6;
}

/* Section */
.AboutSection {
  padding: 64px 24px;
}
.AboutSection--alt {
  background: #f8f9fb;
}
.AboutSection__inner {
  max-width: 960px;
  margin: 0 auto;
}
.AboutSection__heading {
  font-size: 28px;
  font-weight: 700;
  color: #111;
  text-align: center;
  margin-bottom: 48px;
}
.AboutSection__text {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  margin-bottom: 16px;
}

/* Contributors Grid */
.ContributorsGrid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 32px;
  justify-items: center;
}
.ContributorCard {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  transition: transform 0.2s;
}
.ContributorCard:hover {
  transform: translateY(-4px);
}
.ContributorCard__avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.ContributorCard__avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.ContributorCard__info {
  text-align: center;
}
.ContributorCard__name {
  font-size: 16px;
  font-weight: 600;
  color: #111;
}

/* Footer */
.AboutFooter {
  text-align: center;
  padding: 32px 24px;
  font-size: 14px;
  color: #9ca3af;
  border-top: 1px solid #e5e7eb;
}
</style>
