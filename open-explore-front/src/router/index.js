
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
  }
]

export default routes
