<template>
  <div class="AboutPage" :lang="$i18n.locale">
    <main>
      <section class="AboutHero" aria-labelledby="about-title">
        <div class="AboutHero__overlay" aria-hidden="true"></div>
        <div class="AboutHero__inner">
          <div class="AboutHero__copy">
            <h1 id="about-title" class="AboutHero__title">{{ aboutData.title }}</h1>
            <p class="AboutHero__subtitle">{{ aboutData.subtitle }}</p>
          </div>
          <div class="AboutHero__art" aria-hidden="true">
            <img src="@/assets/generated/knowledge-globe.webp" alt="" width="1254" height="1254" fetchpriority="high" />
          </div>
        </div>
      </section>

      <section v-if="aboutData.introduction.length" class="AboutSection" :aria-label="aboutData.title">
        <div class="AboutSection__inner">
          <div class="AboutSection__content">
            <p v-for="(paragraph, index) in aboutData.introduction" :key="'intro-' + index" class="AboutSection__text">{{ paragraph }}</p>
          </div>
        </div>
      </section>

      <section v-if="aboutData.contributors.length" class="AboutSection AboutSection--contributors" aria-labelledby="contributors-title">
        <div class="AboutSection__inner">
          <h2 id="contributors-title" class="AboutSection__heading">{{ aboutData.contributorsTitle || 'Contributors' }}</h2>
          <div class="ContributorsGrid">
            <div v-for="(person, index) in aboutData.contributors" :key="'contrib-' + index" class="ContributorCard">
              <div class="ContributorCard__avatar"><img :src="person.avatar" :alt="person.name" @error="onAvatarError" /></div>
              <div class="ContributorCard__info"><span class="ContributorCard__name">{{ person.name }}</span></div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <footer class="AboutFooter"><p>{{ aboutData.footer }}</p></footer>
  </div>
</template>

<script>
export default {
  name: 'AboutUs',
  data() {
    return { aboutData: { title: '', subtitle: '', introduction: [], contributors: [], contributorsTitle: '', footer: '' } }
  },
  created() { this.refreshAboutData() },
  watch: { '$i18n.locale': 'refreshAboutData' },
  methods: {
    refreshAboutData() {
      const contributors = this.$te('about.contributors') ? this.$t('about.contributors') : []
      this.aboutData = {
        title: this.$t('about.title'),
        subtitle: this.$t('about.subtitle'),
        introduction: this.$t('about.introduction'),
        contributors: Array.isArray(contributors) ? contributors : [],
        contributorsTitle: this.$te('about.contributorsTitle') ? this.$t('about.contributorsTitle') : '',
        footer: this.$t('about.footer')
      }
    },
    onAvatarError(event) {
      event.target.src = 'data:image/svg+xml,%3Csvg xmlns%3D%22http%3A//www.w3.org/2000/svg%22 width%3D%2280%22 height%3D%2280%22 viewBox%3D%220 0 80 80%22%3E%3Crect fill%3D%22%23e5e7eb%22 width%3D%2280%22 height%3D%2280%22/%3E%3Ctext x%3D%2240%22 y%3D%2240%22 text-anchor%3D%22middle%22 dominant-baseline%3D%22central%22 fill%3D%22%239ca3af%22 font-size%3D%2224%22%3E%3F%3C/text%3E%3C/svg%3E'
    }
  }
}
</script>

<style lang="scss" scoped>
.AboutPage {
  display: flex;
  flex-direction: column;
  width: 100%;
  color: #172640;
  background: #f6f8ff;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 16px;
  line-height: 1.8;
  letter-spacing: normal;
  *, *::before, *::after { box-sizing: border-box; }
  main { flex: 1 0 auto; }
}
h1, h2, p { margin: 0; }
.AboutHero { position: relative; overflow: hidden; background: radial-gradient(ellipse at 83% 35%, #bfd6ffbd, transparent 61%), linear-gradient(115deg, #eeedff, #f1f5ff 47%, #e1eaff); }
.AboutHero__overlay { position: absolute; inset: 0; pointer-events: none;
  &::before { content: ''; position: absolute; left: -200px; top: -360px; width: 530px; height: 500px; border-radius: 50%; background: #ffffff87; }
  &::after { content: ''; position: absolute; right: -260px; top: -330px; width: 670px; height: 690px; border: 55px solid #ffffff59; border-radius: 50%; transform: rotate(-25deg); }
}
.AboutHero__inner { position: relative; display: grid; grid-template-columns: 1.25fr 1fr; align-items: center; gap: 65px; width: min(1120px, calc(100% - 80px)); min-height: 350px; margin-inline: auto; padding-block: 36px; }
.AboutHero__copy { min-width: 0; }
.AboutHero__title { color: #172b56; font-size: clamp(2rem, 3vw, 2.75rem); font-weight: 700; line-height: 1.3; letter-spacing: -.035em; }
.AboutHero__subtitle { max-width: 620px; margin-top: 20px; color: #5a6b89; font-size: 1rem; line-height: 1.9; }
.AboutHero__art { position: relative; height: 280px;
  img { position: absolute; top: 50%; left: 50%; width: 310px; max-width: 100%; height: auto; transform: translate(-50%, -50%); filter: drop-shadow(0 15px 22px #6c89c514); }
}
.AboutSection { padding: 40px 0; }
.AboutSection__inner { width: min(1120px, calc(100% - 80px)); margin-inline: auto; }
.AboutSection__content { padding: 32px 38px; border: 1px solid white; border-radius: 18px; background: linear-gradient(135deg, #ffffffed, #f9fbffe0); box-shadow: 0 6px 23px #5471b80a, 0 0 0 1px #dae4f27d; }
.AboutSection__text { color: #586a84; font-size: 1rem; line-height: 2; + .AboutSection__text { margin-top: 18px; } }
.AboutSection--contributors { padding-top: 2px; }
.AboutSection__heading { margin-bottom: 26px; color: #223656; font-size: 1.5rem; font-weight: 600; }
.ContributorsGrid { display: grid; grid-template-columns: repeat(auto-fill, minmax(170px, 1fr)); gap: 20px; }
.ContributorCard { display: flex; flex-direction: column; align-items: center; gap: 14px; padding: 24px 20px; border: 1px solid white; border-radius: 15px; background: #ffffffb8; box-shadow: 0 0 0 1px #dce5f280; }
.ContributorCard__avatar { width: 82px; height: 82px; overflow: hidden; border: 3px solid white; border-radius: 50%; box-shadow: 0 3px 14px #597cb621;
  img { display: block; width: 100%; height: 100%; object-fit: cover; }
}
.ContributorCard__name { color: #3b5275; font-size: 1rem; font-weight: 500; }
.AboutFooter { width: min(1120px, calc(100% - 80px)); margin-inline: auto; padding: 19px 0 25px; border-top: 1px solid #dce5f2; color: #7b899f; font-size: .8125rem; text-align: center; }
@media (max-width: 1100px) {
  .AboutHero__inner, .AboutSection__inner, .AboutFooter { width: calc(100% - 72px); }
  .AboutHero__inner { gap: 32px; }
}
@media (max-width: 900px) {
  .AboutHero__inner, .AboutSection__inner, .AboutFooter { width: calc(100% - 48px); }
  .AboutHero__inner { grid-template-columns: 1.4fr 1fr; min-height: 320px; gap: 24px; }
  .AboutHero__art { height: 240px; }
  .AboutSection__content { padding: 28px 30px; }
}
@media (max-width: 640px) {
  .AboutHero__inner { display: flex; flex-direction: column; gap: 17px; min-height: 0; padding-block: 34px 22px; }
  .AboutHero__copy { width: 100%; }
  .AboutHero__title { font-size: 2rem; }
  .AboutHero__subtitle { margin-top: 15px; }
  .AboutHero__art { width: 100%; height: 180px; img { width: 200px; } }
  .AboutSection { padding-block: 25px; }
  .AboutSection__content { padding: 23px; border-radius: 15px; }
  .AboutSection__text { line-height: 1.95; + .AboutSection__text { margin-top: 16px; } }
  .AboutSection--contributors { padding-top: 0; }
  .ContributorsGrid { grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 14px; }
  .ContributorCard { padding-inline: 13px; }
  .AboutFooter { font-size: .75rem; }
}
@media (max-width: 380px) {
  .AboutHero__inner, .AboutSection__inner, .AboutFooter { width: calc(100% - 32px); }
  .AboutHero__title { font-size: 1.8rem; }
  .AboutSection__content { padding-inline: 19px; }
}
</style>
