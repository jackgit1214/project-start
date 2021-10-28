import {call, put,all, takeEvery} from 'redux-saga/effects'
import {fetchToken, request} from "../api/request";
import {getStore, setStore,removeStore} from "../api/mUtils";
import {MessageBox} from "element-react";

const initialTypeState = {
	loading: false,
	deptTree:[],
	departments:[],
	listQuery: {
		page: 1,
		limit: 10,
		pageCount: 5,
		total: 0,
		deptName: "",
	},
};

export const actionType = {
	UPDATE_DEPARTMENT_STATE:"updateDepartmentState",
	DEPARTMENT_PAGE_DATA:"getDeptTreePageData"
}

const DepartmentReducers = (state = initialTypeState, action) => {
	switch (action.type) {
		case actionType.UPDATE_DEPARTMENT_STATE:{
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

export default DepartmentReducers;

//以下sagas内容
export function* watchDepartment() {
	yield takeEvery(actionType.DEPARTMENT_PAGE_DATA, getPageData);
}

function* getPageData(action){
	let param = {
		superId: "xxx",
		includeSelf:false,
	}
	const [treeData] = yield all([
		call(request, '/department/departments/tree',"get",param),
	])
	if (treeData.code && treeData.code == 1) { //成功
		let tmpData = treeData.data;

		yield put({
			type: actionType.UPDATE_DEPARTMENT_STATE,
			properties: {"deptTree":tmpData}
		});
	}
}