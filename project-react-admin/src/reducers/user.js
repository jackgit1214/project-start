import {call, put,all, takeEvery} from 'redux-saga/effects'
import {fetchToken, request} from "../api/request";
import {getStore, setStore,removeStore} from "../api/mUtils";
import {MessageBox} from "element-react";

const initialTypeState = {
	loading: false,
	deptTree:[],
	user:[],
	currentDept:{},
	listQuery: {
		page: 1,
		limit: 10,
		pageCount: 5,
		total: 0,
		userName: "",
		name: "",
		departmentId: ""
	},
};

const actionType = {
	UPDATE_USER_STATE:"updateUserState",
	GET_PAGE_DATA:"getPageData"
}

const UserReducers = (state = initialTypeState, action) => {
	switch (action.type) {
		case actionType.UPDATE_USER_STATE:{
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

export default UserReducers;
/* action 处理*/


//以下sagas内容
export function* watchUser() {
	yield takeEvery(actionType.GET_PAGE_DATA, getPageData);
}

function* getPageData(action){
	let queryParam = Object.assign({}, initialTypeState.listQuery);
	queryParam.page = action.page?action.page:initialTypeState.listQuery.page;
	queryParam.limit =action.limit?action.limit:initialTypeState.listQuery.limit;

	queryParam["param%5Blogincode%5D"] =action.userName?action.userName:"";
	queryParam["param%5Busername%5D"] =action.loginCode?action.loginCode:"";

	queryParam.departmentId = action.departmentId;
	const [userData] = yield all([
		call(request, '/user/users',"get",queryParam),
	])
	if (userData.code && userData.code == 1) { //成功
		let user = userData.data;
		yield put({
			type: actionType.UPDATE_USER_STATE,
			properties: {"user":user}
		});
	}
}