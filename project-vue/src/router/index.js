import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";
import {checkTokenExp} from "../config/tokenHandler";

const routes = [
    {
        path: '/',
        redirect: '/dashboard'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashboard",
                name: "dashboard",
                meta: {
                    title: '系统首页'
                },
                component: () => import ("../views/Dashboard.vue")
            },
            {
                path: "/user/index",
                name: "userManager",
                meta: {
                    title: '用户管理'
                },
                component: () => import ( "../views/user/UserIndex.vue")
            },
            {
                path: "/dept/index",
                name: "departmentManager",
                meta: {
                    title: '部门管理'
                },
                component: () => import ( "../views/department/Index.vue")
            },
            {
                path: "/role/index",
                name: "roleManager",
                meta: {
                    title: '角色管理'
                },
                component: () => import ( "../views/role/Index.vue")
             }, {
                path: "/dictionary/index",
                name: "数据字典",
                meta: {
                    title: '数据字典'
                },
                component: () => import ( /* webpackChunkName: "tabs" */ "../views/dictionary/Index.vue")
            },{
                path: '/404',
                name: '404',
                meta: {
                    title: '找不到页面'
                },
                component: () => import (/* webpackChunkName: "404" */ '../views/error/404.vue')
            },
            {
                path: '/403',
                name: '403',
                meta: {
                    title: '没有权限'
                },
                component: () => import (/* webpackChunkName: "404" */ '../views/error/403.vue')
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import ( /* webpackChunkName: "login" */ "../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | 基础信息管理平台`;
    const role = checkTokenExp();
    // const role1 = localStorage.getItem('ms_username');
    if (role && to.path !== '/login') {
        next('/login');
    } else if (to.meta.permission) {
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin'
            ? next()
            : next('/403');
    } else {
        next();
    }
});

export default router;