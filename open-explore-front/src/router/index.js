
import ArticleSearch from '../views/ArticleSearch.vue'


const routes = [
  {
    path: '/',
    name: 'ArticleSearch',
    component: ArticleSearch
  },
  {
    path: '/article/detail/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue')
  },
  {
    path: '/sciencedata',
    name: 'ScienceDataSearch',
    component: () => import('../views/ScienceDataSearch.vue')
  },
  {
    path: '/sciencedata/detail/:id',
    name: 'ScienceDataDetail',
    component: () => import('../views/ScienceDataDetail.vue')
  }
]

export default routes
