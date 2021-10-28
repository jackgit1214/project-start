import {call, put,all, takeEvery} from 'redux-saga/effects'
import {fetchToken, request} from "../api/request";
import {getStore, setStore,removeStore} from "../api/mUtils";
import {MessageBox} from "element-react";

const initialTypeState = {
	loading: false,
	logData: [],
	statisticData:{
		SysUser:{
			count:0,
		},
		SysRole:{
			count:0,
		},
		SysDept:{
			count:0
		}
	}
};

const actionType = {
	UPDATE_HOME_STATE:"updateHomeState",
	GET_HOME_LOG:"getHomeLogData",
	GET_HOME_STATISTIC:"getHomeStatisticData",
	GET_HOME_DATA:"getHomeData"
}

const HomeData = (state = initialTypeState, action) => {
	switch (action.type) {
		case actionType.UPDATE_HOME_STATE:{
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

export default HomeData;

/** action 内容 **/
export function getHomeLogData(){

}


export function getHomeStaticData(){

}

//以下sagas内容
export function* watchHome() {
	yield takeEvery(actionType.GET_HOME_LOG, getHomeLog);
	yield takeEvery(actionType.GET_HOME_STATISTIC, getHomeStatistics);
	yield takeEvery(actionType.GET_HOME_DATA, getHome);
}

function* getHomeLog(){

}

function* getHomeStatistics(){

}

function* getHome(){
	console.log("sage gethome.............")
	const [logData, statisticData] = yield all([
		call(request, '/stats/log',"get"),
		call(request, '/stats/tableInfo',"get")
	])
	yield put({
		type: actionType.UPDATE_HOME_STATE,
		properties: {"logData":logData,"statisticData":statisticData}
	});
}