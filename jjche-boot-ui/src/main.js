import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css'

import Element from 'element-ui'
import VForm from 'vform-builds'  //引入VForm库

// 数据字典
import dict from './components/Dict'
import plugins from './plugins' // plugins

// 权限指令
import permission from './components/Permission'
import './assets/styles/element-variables.scss'
// global css
import './assets/styles/index.scss'
import '@/assets/styles/ruoyi.scss' // ruoyi css
import 'vform-builds/dist/VFormDesigner.css'  //引入VForm样式

import App from './App'
import store from './store'
import router from './router/routers'

import './assets/icons' // icon
import './router/index' // permission control

Vue.use(permission)
Vue.use(dict)
Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})
Vue.use(VForm)
// bpmnProcessDesigner 需要引入
import MyPD from '@/components/bpmnProcessDesigner/package/index.js'
Vue.use(MyPD)
Vue.use(plugins)
import "@/components/bpmnProcessDesigner/package/theme/index.scss";

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
