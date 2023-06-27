import Vue from 'vue'

import Cookies from 'js-cookie'
import { parseTime, resetForm, handleTree, addBeginAndEndTime, divide} from "@/utils/ruoyi";

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
import DocAlert from '@/components/DocAlert'

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.handleTree = handleTree
Vue.prototype.divide = divide

Vue.use(permission)
Vue.use(dict)
Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})
Vue.use(VForm)
Vue.use(plugins)

// bpmnProcessDesigner 需要引入
import MyPD from "@/components/bpmnProcessDesigner/package/index.js";
Vue.use(MyPD);
import "@/components/bpmnProcessDesigner/package/theme/index.scss";
import "bpmn-js/dist/assets/diagram-js.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";

Vue.config.productionTip = false
Vue.component('DocAlert', DocAlert)

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
