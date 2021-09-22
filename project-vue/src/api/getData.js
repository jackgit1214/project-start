import fetch from '../config/fetch'

/**
 * 测试
 */

export const testLink = data=> fetch('http://localhost:7003/thir/api/v1/user', {},'GET')

/**
 * 退出，主要是去除本地，并调用远程退出
 */

export const signout = () => fetch('/admin/signout');

/**
 * 获取用户信息
 */

export const getAdminInfo = () => fetch('/admin/info');

/**  以上是测试。。。。。。。。。。。。。。。。。。。。。。 */

/**
 * 首页统计信息
 * @returns {*|Promise<unknown>}
 */
export const getStatsInfo =()=>fetch('/stats/tableInfo');

export const getStatsLog =()=>fetch('/stats/log');

/**
 * 用户操作相关API定义----------------------------------------------------------------------------------
 */
/*** 取得当前登录用户信息 */
export const getUserInfo = () => fetch('/user/currentUser');

/*** 取得当前登录用户信息 */
export const getUserList = (data) => fetch('/user/users',data);

/*** 更新用户 */
export const saveUserByUserId = (data,userId) => fetch('/user/'+userId,data,"post",true);

/*** 创建用户 */
export const createUser = (data) => fetch('/user/user',data,"post",true);

export const deleteUser = (data)=> fetch('/user/users',data,'delete');

/**
 * ***********************    获取模块列表   ****************************
 */

export const getModuleList = data => fetch('/module/modules', data);

/**
 * 获取当前登录用户模块列表
 */
export const getUserModuleList = (user_id,data) => fetch('/module/modules/'+user_id,data);


/**
 *  ************************ 角色操作API *****************************************
 * */
export const getALLRoles = (data)=> fetch("/role/roles/all",data);

export const saveRole = (data)=>fetch("/role/role",data,"post")

export const deleteRole=(data)=>fetch("/role/"+data,data,"delete");
/**
 *  ************************ 组织结构操作api *****************************************
 * */
export const getDepartmentBySuperId =(data)=> fetch("/department/departments",data)

export const getDepartmentTree =() => fetch("/department/departments/tree")

export const getDepartmentAll = (data)=> fetch("/department/all",data)

export const saveDepartment =(data) => fetch("/department/department",data,"post")

export const deleteDepartment = (data)=> fetch('/department/department/delete',data,'delete');

/**
 *  ************************ 数据字典操作api *****************************************
 * */
export const getDictionary=(data)=> fetch('/dictionary/dictionaries',data);

export const deleteDictionary=(data)=>fetch('/dictionary/dictionaries',data,'delete');

export const saveDictionary = (data)=>fetch('/dictionary/dictionary',data,"post")
/**********************  以下可删除*************************************************/
/**
 * api请求量
 */
export const apiCount = date => fetch('/statis/api/' + date + '/count');

/**
 * 所有api请求量
 */
export const apiAllCount = () => fetch('/statis/api/count');


/**
 * 所有api请求信息
 */

export const apiAllRecord = () => fetch('/statis/api/all');

/**
 * 用户注册量
 */

export const userCount = date => fetch('/statis/user/' + date + '/count');



