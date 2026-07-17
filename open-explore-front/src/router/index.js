import HomePage from '../views/HomePage.vue'
import SearchPage from '../views/SearchPage.vue'
import AboutUs from '../views/AboutUs.vue'

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage
  },
  {
    path: '/search',
    name: 'SearchPage',
    component: SearchPage
  },
  {
    path: '/article/detail/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue')
  },
  {
    path: '/sciencedata/detail/:id',
    name: 'ScienceDataDetail',
    component: () => import('../views/ScienceDataDetail.vue')
  },
  {
    path: '/software/detail/:id',
    name: 'SoftwareDetail',
    component: () => import('../views/SoftwareDetail.vue')
  },
  {
    path: '/facility/detail/:id',
    name: 'FacilityDetail',
    component: () => import('../views/FacilityDetail.vue')
  },
  {
    path: '/about',
    name: 'AboutUs',
    component: AboutUs
  }
]

export default routes
