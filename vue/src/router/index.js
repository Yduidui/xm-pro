import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/login'},
    {
      path: '/manager',
      component: () => import('../views/Manager.vue'),  // 将 Manager.vue 设置为父组件
      children: [
        {
          path: 'home',
          name: 'home',
          meta: { title: '主页' },
          component: () => import('../views/Home.vue'),
        },
        {
          path: 'test',
          name: 'test',
          meta: { title: '测试页' },
          component: () => import('../views/Test.vue'),
        },
        {
          path: 'data',
          name: 'data',
          meta: { title: '数据页' },
          component: () => import('../views/Data.vue'),
        },
        {
          path: 'employee',
          name: 'employee',
          meta: { title: '员工信息页' },
          component: () => import('../views/Employee.vue'),
        },
        {
          path: 'admin',
          name: 'admin',
          meta: { title: '管理员信息页' },
          component: () => import('../views/Admin.vue'),
        },
        {
          path: 'article',
          name: 'article',
          meta: { title: '文章信息页' },
          component: () => import('../views/Article.vue'),
        },
        {
          path: 'personalinfo',
          name: 'personalinfo',
          meta: { title: '个人信息页' },
          component: () => import('../views/PersonalInfo.vue'),
        },
        {
          path: 'changePassword',
          name: 'changePassword',
          meta: { title: '修改密码' },
          component: () => import('../views/changePassword.vue'),
        },
        {
          path: 'department',
          name: 'department',
          meta: { title: '部门信息页' },
          component: () => import('../views/department.vue'),
        },
      ],
    },
    {
      path: '/404',
      name: 'NotFound',
      meta: { title: '404' },
      component: () => import('../views/404.vue'),
    },
    {
      path: '/login',
      name: '登录',
      meta: { title: '登录界面' },
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/register',
      name: '注册',
      meta: { title: '注册界面' },
      component: () => import('../views/Register.vue'),
    },
    {
      path: '/:pathMatch(.*)',
      redirect: '/404',
    },
  ],
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title
  next()
})

export default router