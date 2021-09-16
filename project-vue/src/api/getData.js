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

/**
 * 某一天订单数量
 */

export const orderCount = date => fetch('/statis/order/' + date + '/count');


/**
 * 某一天管理员注册量
 */

export const adminDayCount = date => fetch('/statis/admin/' + date + '/count');

/**
 * 管理员列表
 */

export const adminList = data => fetch('/admin/all', data);

/**
 * 管理员数量
 */

export const adminCount = () => fetch('/admin/count');

/**
 * 获取定位城市
 */

export const cityGuess = () => fetch('/v1/cities', {
	type: 'guess'
});

/**
 * 添加商铺
 */

export const addShop = data => fetch('/shopping/addShop', data, 'POST');

/**
 * 获取搜索地址
 */

export const searchplace = (cityid, value) => fetch('/v1/pois', {
	type: 'search',
	city_id: cityid,
	keyword: value
});

/**
 * 获取当前店铺食品种类
 */

export const getCategory = restaurant_id => fetch('/shopping/getcategory/' + restaurant_id);

/**
 * 添加食品种类
 */

export const addCategory = data => fetch('/shopping/addcategory', data, 'POST');


/**
 * 添加食品
 */

export const addFood = data => fetch('/shopping/addfood', data, 'POST');


/**
 * category 种类列表
 */

export const foodCategory = (latitude, longitude) => fetch('/shopping/v2/restaurant/category');

/**
 * 获取餐馆列表
 */

export const getResturants = data => fetch('/shopping/restaurants', data);

/**
 * 获取餐馆详细信息
 */

export const getResturantDetail = restaurant_id => fetch('/shopping/restaurant/' + restaurant_id);

/**
 * 获取餐馆数量
 */

export const getResturantsCount = () => fetch('/shopping/restaurants/count');

/**
 * 更新餐馆信息
 */

export const updateResturant = data => fetch('/shopping/updateshop', data, 'POST');

/**
 * 删除餐馆
 */

export const deleteResturant = restaurant_id => fetch('/shopping/restaurant/' + restaurant_id, {}, 'DELETE');

/**
 * 获取食品列表
 */

export const getFoods = data => fetch('/shopping/v2/foods', data);

/**
 * 获取食品数量
 */

export const getFoodsCount = data => fetch('/shopping/v2/foods/count', data);


/**
 * 获取menu列表
 */

export const getMenu = data => fetch('/shopping/v2/menu', data);

/**
 * 获取menu详情
 */

export const getMenuById = category_id => fetch('/shopping/v2/menu/' + category_id);

/**
 * 更新食品信息
 */

export const updateFood = data => fetch('/shopping/v2/updatefood', data, 'POST');

/**
 * 删除食品
 */

export const deleteFood = food_id => fetch('/shopping/v2/food/' + food_id, {}, 'DELETE');



/**
 * 获取订单列表
 */

export const getOrderList = data => fetch('/bos/orders', data);

/**
 * 获取订单数量
 */

export const getOrderCount = data => fetch('/bos/orders/count', data);



/**
 * 获取地址信息
 */

export const getAddressById = address_id => fetch('/v1/addresse/' + address_id);

/**
 * 获取用户分布信息
 */

export const getUserCity = () => fetch('/v1/user/city/count');
