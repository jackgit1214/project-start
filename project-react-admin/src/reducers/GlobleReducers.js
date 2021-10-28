import {call, put, takeEvery} from 'redux-saga/effects'
import {fetchToken, request} from "../api/request";
import {getStore, setStore,removeStore} from "../api/mUtils";
import {MessageBox} from "element-react";

const initialTypeState = {
	loading: false,
	collapseStatus: true,
    user: {},
	loginInfo: localStorage.getItem("userInfo") ? localStorage.getItem("userInfo") : "",
	token: getStore("token") ? JSON.parse(getStore("token")) : null,
	menuData: []
};

const actionType = {
	SWITCH_SLIDER: 'switchSlider', //切换
	SYSTEM_LOGIN: 'systemLogin', //
	SYSTEM_LOGOUT: 'systemLogout', //退出
	RETRIEVE_MENU_DATA: "retrieveMenuData",
	GET_USER_INFO: "getUserInfo",
    UPDATE_STATE:"updateState",
}

const GlobalData = (state = initialTypeState, action) => {
	switch (action.type) {
		case actionType.SWITCH_SLIDER: {
			let newValue = !state.collapseStatus;
			return Object.assign({}, state, {collapseStatus: newValue})
		}
		case actionType.SYSTEM_LOGIN:{
			console.log("reducer............................")
			return state;
		}
        case actionType.UPDATE_STATE: {
            let pro = action.properties;
            let newValue ={};
            for( let key in pro ){
				newValue[key] = pro[key];
            }
            return Object.assign({}, state, newValue);
        }
		default:
			return state;
			break;
	}
}

export default GlobalData;

/**
 * action部分
 */

/**
 * 导航开关
 */
export function switchSlider() {
	return {
		type: actionType.SWITCH_SLIDER,
	}
}

/**
 * 登录
 */
export function systemLogin(loginInfo) {
	console.log("action ..............")
	return {
		type: actionType.SYSTEM_LOGIN,
		loginInfo,
	}
}

/**
 * 登出
 */
export function systemLogout() {
	removeStore("token");
	removeStore("userInfo")
	return {
		type: actionType.UPDATE_STATE,
		properties:{"user":{},"loginInfo": "","token": null,"menuData":[]}
	}
}

/**
 * 获取菜单数据，根据用户信息
 */
export function retrieveMenuData(userInfo) {
	return {
		type: actionType.RETRIEVE_MENU_DATA,
		userInfo,
	}
}


//以下sagas内容
export function* watchGlobal() {
	yield takeEvery(actionType.SYSTEM_LOGIN, login);
	yield takeEvery(actionType.SYSTEM_LOGOUT, logout);
	yield takeEvery(actionType.RETRIEVE_MENU_DATA, getMenuData);
	yield takeEvery(actionType.GET_USER_INFO, getUserData);
}

function* login(action) {
	console.log("saga ..............")
	let loginInfo = action.loginInfo;
	const responseResult = yield call(fetchToken, loginInfo);
	if (responseResult.status) {
		setStore("token", JSON.stringify(responseResult))
		setStore("userInfo", loginInfo.userName)
		yield put({
			type: actionType.UPDATE_STATE,
			properties: {"token":responseResult,"loginInfo":loginInfo.userName}
		});
	} else {
		let errorInfo = responseResult.errorInfo;
		MessageBox.alert(errorInfo);
	}

}

function* logout(action) {
	// let args = action.params;
	// const responseResult = yield call(fetch, 'task/analysisTask', 'post', args);
	//yield put(pageResponse(responseResult));
}

function* getMenuData(action) {
	let userInfo = action.userInfo;

	let menuUrl = '/module/modules/' + userInfo.userName
	const responseResult = yield call(request, menuUrl, 'get', userInfo);
	if (responseResult.code && responseResult.code == 1) { //成功
		let menuData = responseResult.data;
		yield put({
			type: actionType.UPDATE_STATE,
			properties: {"menuData":menuData}
		});
	}

}

function* getUserData(action) {
	let userInfo = action.userInfo;
	let url = '/user/currentUser'
	const responseResult = yield call(request, url, 'get', userInfo);
	yield put({
		type: actionType.UPDATE_STATE,
		properties: {"user":responseResult}
	});
}