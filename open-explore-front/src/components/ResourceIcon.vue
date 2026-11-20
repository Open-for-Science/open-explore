<template>
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.65" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true" focusable="false">
    <g v-if="name === 'article'"><path d="M5 3h9l5 5v13H5zM14 3v5h5M9 12h6M9 16h6" /></g>
    <g v-else-if="name === 'sciencedata'"><ellipse cx="12" cy="5" rx="8" ry="3" /><path d="M4 5v14c0 1.7 3.6 3 8 3s8-1.3 8-3V5M4 12c0 1.7 3.6 3 8 3s8-1.3 8-3" /></g>
    <g v-else-if="name === 'software'"><rect x="3" y="4" width="18" height="16" rx="3" /><path d="m8 9-3 3 3 3m8-6 3 3-3 3m-3-7-2 8" /></g>
    <g v-else-if="name === 'facility'"><circle cx="12" cy="12" r="2" /><ellipse cx="12" cy="12" rx="10" ry="4" transform="rotate(-35 12 12)" /><ellipse cx="12" cy="12" rx="10" ry="4" transform="rotate(35 12 12)" /><path d="M12 2v3m0 14v3" /></g>
    <g v-else-if="name === 'search'"><circle cx="10.5" cy="10.5" r="6.5" /><path d="m16 16 4.5 4.5" /></g>
    <g v-else-if="name === 'globe'"><circle cx="12" cy="12" r="9" /><ellipse cx="12" cy="12" rx="4" ry="9" /><path d="M3 12h18" /></g>
    <g v-else-if="name === 'spark'"><path d="m12 2 2.7 7.3L22 12l-7.3 2.7L12 22l-2.7-7.3L2 12l7.3-2.7z" /></g>
    <g v-else-if="name === 'mail'"><rect x="3" y="5" width="18" height="14" rx="3" /><path d="m3 7 9 6 9-6" /></g>
    <g v-else-if="name === 'refresh'"><path d="M20 7v5h-5M4 17v-5h5M6 6a8 8 0 0 1 13 2M5 16a8 8 0 0 0 13 2" /></g>
    <g v-else-if="name === 'check'"><circle cx="12" cy="12" r="9" /><path d="m8 12 3 3 5-6" /></g>
    <g v-else-if="name === 'arrow'"><path d="M4 12h15m-6-6 6 6-6 6" /></g>
  </svg>
</template>
<script>
export default {
  name: 'ResourceIcon',
  props: { name: { type: String, required: true } }
}
</script>
