import UserPage from "../views/user/UserPage";
import IndexHome from "../Framework";
import Dashboard from "../views/Dashboard";
import ErrorPage403 from "../components/error/403";
import ErrorPage404 from "../components/error/404";

const routes = [
    {
        path: '/home',
        name: '框架',
        icon: "fa ",
        component: IndexHome,
    },{
        path: "/dashboard",
        name: "首页",
        icon: "fa ",
        component: Dashboard,
    },{
        path: "/user/index",
        name: "用户管理",
        icon: "fa ",
        component: UserPage,
    },
    {
        path: "/dept/index",
        name: "部门管理",
        icon: "fa ",
        component: '',
    },
    {
        path: "/role/index",
        name: "角色管理",
        icon: "fa ",
        component: '',
    }, {
        path: "/dictionary/index",
        name: "数据字典",
        icon: "fa ",
        component: '',
    },{
        path: '/404',
        name: '找不到页面',
        icon: "fa ",
        component: ErrorPage404,
    },
    {
        path: '/403',
        name: '没有权限',
        icon: "fa ",
        component: ErrorPage403,
    },
    {
        path: '*',
        component: ErrorPage404,
    }
];


export default routes;