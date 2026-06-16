<template>
  <transition name="fade">
    <div class="ToTop" v-show="visible" @click="scrollToTop">
      <i class="base-icon-arrow-up"></i>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'ToTop',
  props: {
    target: {
      type: String,
      default: ''
    },
    visibilityHeight: {
      type: Number,
      default: 200
    }
  },
  data() {
    return {
      visible: false,
      scrollEl: null
    }
  },
  mounted() {
    this.scrollEl = this.target
      ? document.querySelector(this.target)
      : this.$el.parentElement
    if (this.scrollEl) {
      this.scrollEl.addEventListener('scroll', this.handleScroll)
    }
  },
  beforeDestroy() {
    if (this.scrollEl) {
      this.scrollEl.removeEventListener('scroll', this.handleScroll)
    }
  },
  methods: {
    handleScroll() {
      this.visible = this.scrollEl.scrollTop > this.visibilityHeight
    },
    scrollToTop() {
      this.scrollEl.scrollTo({ top: 0, behavior: 'smooth' })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@zkwq/business/dist/var.scss';

.ToTop {
  position: fixed;
  right: 24px;
  bottom: 24px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: $--color-primary;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  transition: opacity 0.3s, transform 0.3s;
  z-index: 10;

  i {
    font-size: 18px;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
