import {deleteDictionary, getDictionary,saveDictionary} from "../../api/getData";

const state = {
	items: [],
	treeItems: [],
	selectNode: {},
	pageParam: {
		page: 1,
		limit: 9,
		pageCount: 5,
		total: 0,
	},
	queryParam: {
		codename: "",
	},
	dialogParam: {
		edit: true,
		show: false,
	},
	defaultDictionary: {
		codeid: '',
		codetype: '',
		content: '',
		code: '',
		codename: '',
		superid: '',
		level: '',
		orderby: 0,
		remark: '',
	},
	editData: {},
}

// getters
const getters = {
	dictionaries: (state, getters, rootState) => {

	},
	getDefaultDictionary: (state, getters, rootState) => {
		return Object.assign({}, state.defaultDictionary);
	},
	getCurrentTreeNodeData:(state,getters,rootState) =>{
		return state.selectNode;
	},
}

const actions = {
	async getAllDictionary({commit, state}, data) {

		let param = Object.assign({}, state.pageParam, {limit: 10000})
		param["param%5BsuperId%5D"] = state.selectNode.code+"/=/";
		param["param%5Bcodetype%5D"] = state.selectNode.codetype;
		param["param%5Bcodename%5D"] = state.queryParam.codename;
		const tmpData = await getDictionary(param);
		if (tmpData.code == 1) {
			commit('setDictionary', {
				items: tmpData.data,
				total: tmpData.data.length
			})
		}
	},

	async deleteDictionary({commit, state}, data) {
		let param = {
			ids: data,
		}
		let result = await deleteDictionary(param);
		// let result = {
		// 	code:1
		// }
		//修改
		if (result.code == 1) {
			// departmentTree.value.refreshNode(dataMap.department,rowData,2)
			let tmpData = JSON.parse(JSON.stringify(state.items))
			let curIndex = tmpData.findIndex(function (element) {
				return element["codeid"] == param.ids[0];
			});
			if (curIndex >= 0) {
				tmpData.splice(curIndex, 1)
			}
			commit('setDictionary', {
				items: tmpData,
				total: tmpData.length
			})
		}
	},
	async getDictionaryBySuper({commit, state}, data) {
		let param = Object.assign({}, state.pageParam, {limit: 10000})
		if (!data)
			param["param%5BsuperId%5D"] = 'xxx';
		else {
			param["param%5BsuperId%5D"] = data.code+"/=/";
			param["param%5Bcodetype%5D"] = data.codetype;
		}
		console.log(param);
		const tmpData = await getDictionary(param);
		if (tmpData.code == 1) {
			commit('setTreeItems', {
				items: tmpData.data
			})
		}
	},

	async saveDictionary({commit,state},data){
		let tmpData = Object.assign({},state.editData);
		let result= await saveDictionary(tmpData);
		if (result.code == 1) {
			let tmpItems = JSON.parse(JSON.stringify(state.items));
			let curIndex = tmpItems.findIndex(function (element) {
				    return element["codeid"] == tmpData["codeid"];
			});
			if (curIndex >= 0) {
				tmpItems[curIndex] = tmpData;
			} else //没找到，新增数据
			    curIndex = tmpItems.push(tmpData);

			commit("setDialogParam",{
				edit:true,
				show:false
			})
			commit("setDictionary",{
				items:tmpItems,
				total: tmpItems.length
			})
		}
	},
	openDialog({commit, state}, param) {
		commit('setDialogParam', {
			edit: param.edit,
			show: true,
		})
		commit('setEditData', param.data)
	},

	handleTreeNode({commit, state, dispatch,getters}, node) {
		 commit('setSelectNode', node);
		 dispatch("getAllDictionary");
	},

}

const mutations = {
	setDialogParam(state, data) {
		state.dialogParam = data;
	},
	setEditData(state, data) {
		if (data) {
			state.editData = JSON.parse(JSON.stringify(data));
		}
	},
	setDictionary(state, data) {
		state.items = data.items;
		state.pageParam.total = data.total;
	},

	setTreeItems(state, data) {
		state.treeItems = data.items;
	},
	setPageParam(state, data) {
		state.pageParam.page = data.page;
		state.pageParam.pageCount = data.pageCount;
	},
	setQueryParam(state, data) {
		if (data.codename == "" || data.codename)
			state.queryParam.codename = data.codename;
	},

	setSelectNode(state, node) {
		if (node)
			state.selectNode = node.data;
	}
}


export default {
	namespaced: true,
	state,
	getters,
	actions,
	mutations
}