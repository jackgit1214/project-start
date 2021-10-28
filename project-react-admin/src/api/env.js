/**
 * 配置编译环境和线上环境之间的切换
 * 
 * baseUrl: 域名地址
 * routerMode: 路由模式
 * baseImgPath: 图片存放地址
 * 
 */
let baseUrl = '';
let tokenUrl = '';
let routerMode = 'hash';
let baseImgPath;

const clientProperties = {
	grant_type:"password",
	client_id:"UserManagement",
	client_secret:"user123",
	scope:"all",
}
if (process.env.NODE_ENV == 'development') {
	baseUrl = 'http://localhost:9002/res/api/v1';
	tokenUrl = 'http://localhost:9001/oauth/token';
    baseImgPath = '/img/';
}else{
	baseUrl = '//';
    baseImgPath = '//';
}

export {
	baseUrl,
	tokenUrl,
	routerMode,
	baseImgPath,
	clientProperties
}