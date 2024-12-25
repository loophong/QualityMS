/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import {isURL} from '@/utils/validate'
import {clearLoginInfo} from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  {path: '/404', component: _import('common/404'), name: '404', meta: {title: '404未找到'}},
  {path: '/login', component: _import('common/login'), name: 'login', meta: {title: '登录'}}
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: {name: 'home'},
  meta: {title: '主入口整体布局'},
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
    {path: '/home', component: _import('common/home'), name: 'home', meta: {title: '首页'}},
    {path: '/theme', component: _import('common/theme'), name: 'theme', meta: {title: '主题'}},
    {
      path: '/demo-echarts',
      component: _import('demo/echarts'),
      name: 'demo-echarts',
      meta: {title: 'demo-echarts', isTab: true}
    },
    {
      path: '/demo-ueditor',
      component: _import('demo/ueditor'),
      name: 'demo-ueditor',
      meta: {title: 'demo-ueditor', isTab: true}
    },
    {
      path: '/plan-add-page',
      component: _import('modules/taskmanagement/plan/plan-add-page'),
      name: 'plan-add-page',
      meta: {title: '计划新建页', isDynamic: false, isTab: false}
    },
    {
      path: '/plan-index',
      component: _import('modules/taskmanagement/plan/plan-index'),
      name: 'plan-index',
      meta: {title: '工作计划', isDynamic: true, isTab: true}
    },
    {
      path: '/plan-approval-index',
      component: _import('modules/taskmanagement/plan-approval/plan-approval-index'),
      name: 'plan-approval-index',
      meta: {title: '计划审批', isDynamic: true, isTab: true}
    },

    {
      path: '/plan-approval-page/:plabId',
      component: _import('modules/taskmanagement/plan-approval/plan-approval-page'),
      name: 'plan-approval-page',
      meta: {title: '计划审批页', isDynamic: false, isTab: false},
      props: true
    },

    {
      path: 'taskmanagement/task/my-tasks',
      component: _import('modules/taskmanagement/task/my-tasks'),
      name: 'task-list',
      meta: {title: '我的任务', isDynamic: false, isTab: false},
    },
    {
      path: '/task-update-page',
      component: _import('modules/taskmanagement/task/task-update-page'),
      name: 'task-update-page',
      meta: {title: '任务修改页', isDynamic: true, isTab: false},
      props: true
    },
    {
      path: '/plan-tree',
      component: _import('modules/taskmanagement/tree/plan-tree-display'),
      name: 'plan-tree',
      meta: {title: '计划结构图', isDynamic: false, isTab: false},
      props: true
    },

    {
      path: '/plan-update-page/:plabId',
      component: _import('modules/taskmanagement/plan/plan-update-page'),
      name: 'plan-update-page',
      meta: {title: '计划修改页', isDynamic: false, isTab: false},
      props: true
    },



    {
      path: '/plan-reject-reapproval-page/:plabId',
      component: _import('modules/taskmanagement/plan/plan-reject-reapproval-page'),
      name: 'plan-reject-reapproval-page',
      meta: { title: '计划修改页', isDynamic: false, isTab: false },
      props: true
    },
    {
      path: '/plan-indicator-index/:data',
      component: _import('modules/taskmanagement/plan/plan-indicator-index'),
      name: 'plan-indicator-index',
      meta: { title: '计划-指标关联页', isDynamic: false, isTab: false },
      props: true
    },
    {
      path: '/approval-index',
      component: _import('modules/taskmanagement/approval/approval-index'),
      name: 'task_approval-index',
      meta: { title: '任务审批首页', isDynamic: false, isTab: false },
      props: true
    },


    //QC
    {
      path: '/qcPlanNew',
      component: _import('modules/QCmanagement/qcSubjectManagement/plan/qcPlanNew'),
      name: 'qcPlanNew',
      meta: {title: '新建计划', isDynamic: true, isTab: false}
    },
    {
      path: '/submitEx',
      component: _import('modules/QCmanagement/qcSubjectManagement/examine/submitEx'),
      name: 'qcExamineStatus',
      meta: {title: '计划审核', isDynamic: true, isTab: false}
    },
    {
      path: '/SubjectRegistration',
      component: _import('modules/QCmanagement/qcSubjectManagement/registration/qcSubjectRegistration'),
      name: 'qcSubjectRegistration',
      meta: {title: '课题登记', isDynamic: true, isTab: false}
    },
    {
      path: '/statistics',
      component: _import('modules/QCmanagement/qcChart/statistics'),
      name: 'statistics',
      meta: {title: '点检统计', isDynamic: true, isTab: false}
    },
    {
      path: '/qcTools',
      component: _import('modules/QCmanagement/qcTools/system'),
      name: 'qcTools',
      meta: {title: 'QC工具-系统图', isDynamic: true, isTab: false}
    },
    {
      path: '/Scatter',
      component: _import('modules/QCmanagement/qcTools/Scatter'),
      name: 'Scatter',
      meta: {title: 'QC工具-散点图', isDynamic: true, isTab: false}
    },
    {
      path: '/histogram',
      component: _import('modules/QCmanagement/qcTools/histogram'),
      name: 'histogram',
      meta: {title: 'QC工具-直方图', isDynamic: true, isTab: false}
    },
    {
      path: '/control',
      component: _import('modules/QCmanagement/qcTools/control'),
      name: 'control',
      meta: {title: 'QC工具-控制图', isDynamic: true, isTab: false}
    },
    {
      path: '/fishBone',
      component: _import('modules/QCmanagement/qcTools/fishBone'),
      name: 'fishBone',
      meta: {title: 'QC工具-鱼骨图', isDynamic: true, isTab: false}
    },
    {
      path: '/lineAndBar',
      component: _import('modules/QCmanagement/qcTools/lineAndBar'),
      name: 'lineAndBar',
      meta: {title: 'QC工具-折线图', isDynamic: true, isTab: false}
    },
    {
      path: '/RelationGraph',
      component: _import('modules/QCmanagement/qcTools/RelationGraph'),
      name: 'RelationGraph',
      meta: {title: 'QC工具-关联图', isDynamic: true, isTab: false}
    },



    //仪器仪表
    {
      path: '/instrument-testmethod',
      component: _import('modules/publicmanagement/instrumenttestmethod'),
      name: 'instrument-testmethod',
      meta: {title: '关联检验方法', isDynamic: true, isTab: true}
    },
    //问题

    // {
    //   path: '/issue-issuetable',
    //   component: _import('modules/issueRectification/Rectificationissue'),
    //   name: 'issue-issueRectification',
    //   meta: {title: '任务详情', isDynamic: true, isTab: true}
    // },


    {
      path: '/issue-issuetable',
      component: _import('modules/issueRectification/Rectificationissue'),
      name: 'issue-issueRectification',
      meta: {title: '任务详情', isDynamic: true, isTab: true}
    },
    {
      path: '/issue-issuetable',
      component: _import('modules/issueexamine/issueExamine'),
      name: 'issue-issueexamine',
      meta: {title: '任务审核', isTab: true}
    },
    {
      path: '/issue-issuemask',
      component: _import('modules/issuemask/issuemasktable'),
      name: 'issue-issuemasktable',
      meta: {title: '任务执行', isDynamic: true, isTab: false}
    },
    {
      path: '/issue-issuemask',
      component: _import('modules/issuemask/creatorissuemask'),
      name: 'issue-issuemask',
      meta: {title: '任务详情', isDynamic: true, isTab: false}
    },
    {
      path: '/issue-issuemask',
      component: _import('modules/issuemask/creatormask'),
      name: 'issue-issuenewmask',
      meta: {title: '任务详情', isDynamic: true, isTab: false}
    },
    {
      path: '/issue-issuemask',
      component: _import('modules/issuemask/issueAllmask'),
      name: 'issue-issueAllmask',
      meta: {title: '任务详情', isDynamic: true, isTab: false}
    },
    {
      path: '/issue-issueflow',
      component: _import('modules/issuemask/issuemaskflow'),
      name: 'issue-issueflow',
      meta: {title: '任务流程', isDynamic: true, isTab: false}
    },
    {
      path: '/otherToIssue',
      component: _import('modules/issuefind/findissue'),
      name: 'otherToIssue',
      meta: {title: '问题操作', isTab: true}
    },
    {
      path: '/issue-issueView',
      component: _import('modules/issuetable/issueView'),
      name: 'issue-issueView',
      meta: {title: '问题展示', isDynamic: true, isTab: false}
    },
    //指标
    // 指标
    {
      path: '/indicator-display-charts-indicatorchart/:indicatorName',
      component: _import('modules/indicator/display/charts/indicatorchart'),
      name: 'indicatorchart',
      meta: {title: '指标详情', isTab: true}
    },
    {
      path: '/indicator-keyControlIndicators-indicatorkeyindicators',
      component: _import('modules/indicator/keyControlIndicators/indicatorkeyindicators'),
      name: 'indicator-key-indicators',
      meta: {title: '重点管控指标', isTab: true}
    },
    {
      path: '/indicator-indicatorOverview',
      component: _import('modules/indicator/indicatorOverview'),
      name: 'indicator-indicatorOverview',
      meta: { title: '指标总览', isTab: true }
    },
    // {
    //   path: '/indicator-display-charts-indicatorchart/:indicatorName',
    //   component: _import('modules/indicator/display/charts/indicatorchart'),
    //   name: route => `indicatorchart-${route.params.indicatorName}`, // 动态生成name
    //   meta: { title: '指标详情', isTab: true }
    // }
    //消息通知
    {
      path: '/toNotice',
      component: _import('modules/notice/messagenotification'),
      name: 'toNotice',
      meta: {title: '消息通知', isDynamic: false, isTab: true}
    },

  ],
  beforeEnter(to, from, next) {
    let token = Vue.cookie.get('token')
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({name: 'login'})
    }
    next()
  }
}

const router = new Router({
  mode: 'hash',
  scrollBehavior: () => ({y: 0}),
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储
  if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {
    http({
      url: http.adornUrl('/sys/menu/nav'),
      method: 'get',
      params: http.adornParams()
    }).then(({data}) => {
      if (data && data.code === 0) {
        fnAddDynamicMenuRoutes(data.menuList)
        router.options.isAddDynamicMenuRoutes = true
        sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
        sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
        next({...to, replace: true})
      } else {
        sessionStorage.setItem('menuList', '[]')
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({name: 'login'})
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType(route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes(menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].list && menuList[i].list.length >= 1) {
      temp = temp.concat(menuList[i].list)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      var route = {
        path: menuList[i].url.replace('/', '-'),
        component: null,
        name: menuList[i].url.replace('/', '-'),
        meta: {
          menuId: menuList[i].menuId,
          title: menuList[i].name,
          isDynamic: true,
          isTab: true,
          iframeUrl: ''
        }
      }
      // url以http[s]://开头, 通过iframe展示
      if (isURL(menuList[i].url)) {
        route['path'] = `i-${menuList[i].menuId}`
        route['name'] = `i-${menuList[i].menuId}`
        route['meta']['iframeUrl'] = menuList[i].url
      } else {
        try {
          route['component'] = _import(`modules/${menuList[i].url}`) || null
        } catch (e) {
        }
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    fnAddDynamicMenuRoutes(temp, routes)
  } else {
    mainRoutes.name = 'main-dynamic'
    mainRoutes.children = routes
    router.addRoutes([
      mainRoutes,
      {path: '*', redirect: {name: '404'}}
    ])
    sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
    console.log('\n')
    console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
    console.log(mainRoutes.children)
    console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
  }
}

export default router


