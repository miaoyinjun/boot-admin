import Vue from 'vue'
import Router from 'vue-router'
import Layout from '../layout/index'

Vue.use(Router)

export const constantRouterMap = [
  {
    path: '/login',
    meta: { title: '登录', noCache: true },
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/features/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/features/401'], resolve),
    hidden: true
  },
  {
    path: '/502',
    component: (resolve) => require(['@/views/features/502'], resolve),
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: (resolve) => require(['@/views/features/redirect'], resolve)
      }
    ]
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: (resolve) => require(['@/views/home'], resolve),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'index', affix: true, noCache: true },
        hidden: true
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'center',
        component: (resolve) => require(['@/views/system/user/center'], resolve),
        name: '个人中心',
        meta: { title: '个人中心' }
      }
    ]
  },
  {
    path: '/bpm',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [{
      path: 'oa/leave/create',
      component: (resolve) => require(['@/views/bpm/oa/leave/create'], resolve),
      name: '发起 OA 请假',
      meta: {title: '发起 OA 请假', icon: 'form', activeMenu: '/bpm/oa/leave'}
    }, {
      path: 'oa/leave/detail',
      component: (resolve) => require(['@/views/bpm/oa/leave/detail'], resolve),
      name: '查看 OA 请假',
      meta: {title: '查看 OA 请假', icon: 'view', activeMenu: '/bpm/oa/leave'}
    }
    ]
  },
  {
    path: '/bpm',
    component: Layout,
    hidden: true,
    children: [{
      path: 'manager/form/edit',
      component: (resolve) => require(['@/views/bpm/form/formEditor'], resolve),
      name: '流程表单-编辑',
      meta: { title: '流程表单-编辑', activeMenu: '/bpm/manager/form' }
    },
      {
        path: 'manager/definition',
        component: (resolve) => require(['@/views/bpm/definition/index'], resolve),
        name: '流程定义',
        meta: { title: '流程定义', activeMenu: '/bpm/manager/model' }
      }, {
        path: 'manager/model/design',
        component: (resolve) => require(['@/views/bpm/model/modelEditor'], resolve),
        name: '设计流程',
        meta: { title: '设计流程', activeMenu: '/bpm/manager/model' }
      }, {
        path: 'process-instance/create',
        component: (resolve) => require(['@/views/bpm/processInstance/create'], resolve),
        name: '发起流程',
        meta: { title: '发起流程', activeMenu: '/bpm/task/my' }
      }, {
        path: 'process-instance/detail',
        component: (resolve) => require(['@/views/bpm/processInstance/detail'], resolve),
        name: '流程详情',
        meta: { title: '流程详情', activeMenu: '/bpm/task/my' }
      }
    ]
  }
]

export default new Router({
  mode: 'hash',
  // mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
