<template>
    <el-container>
        <el-main>
            <el-card class="box-card" style="margin-bottom:5px;height:50px;">
                <el-form ref="queryForm" :inline="true">
                    <el-form-item label="角色名称：" prop="deptName">
                        <el-input ref="username" v-model="listQuery.roleName" clearable placeholder="角色名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-search" size="small" type="primary" @click="handleFilter">查询
                        </el-button>
                        <el-button icon="el-icon-edit" size="small" type="primary" @click="handleAddData">新增</el-button>
                    </el-form-item>
                    <edit-info  ref="editDialog" :dialogParam="dialogParam" :modules="modules"
                               :editData="editData" @CloseD="handleClose"/>
                </el-form>
            </el-card>
            <el-card class="box-card">
                <el-table :data="dataList" border class="table" header-cell-class-name="table-header" height="510"
                          max-height="590">
                    <el-table-column align="center" label="序号" type="index" width="50"></el-table-column>
                    <el-table-column align="center" label="角色名称" prop="roleName" width="200"></el-table-column>
                    <el-table-column align="center" label="系统角色" prop="isSystem" width="120">
                        <template #default="scope">
                            <el-switch v-model="scope.row.isSystem" active-value="1" inactive-value="0" disabled
                                       style="display: block">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="描述" prop="roleDesc" ></el-table-column>
                    <el-table-column align="center" label="操作" width="180">
                        <template #default="scope">
                            <el-button icon="el-icon-edit" type="text"
                                       @click="handleEdit(scope.$index, scope.row,true)">编辑
                            </el-button>
                            <el-button :disabled="scope.row.isSystem==1" class="red" icon="el-icon-delete" type="text"
                                       @click="handleDelete(scope.$index, scope.row)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block">
                    <el-pagination ref="pageBar" :current-page="listQuery.page" :page-size="listQuery.limit"
                                   :page-sizes="[5, 10, 15, 9999]"
                                   :pager-count="listQuery.pageCount" :total="listQuery.total"
                                   background layout="total, sizes, prev, pager, next, jumper"
                                   @size-change="handlePageSizeChange"
                                   @current-change="handleCurrentPageChange"></el-pagination>
                </div>
            </el-card>

        </el-main>
    </el-container>
</template>

<script>
import editInfo from "./Edit.vue";
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import {getALLRoles,getModuleList,deleteRole} from "../../api/getData";
import {onMounted, reactive, toRefs,ref} from 'vue'

export default {
    name: "roleListPage",
    components: {
        editInfo
    },
    setup(props, ctx) {
        onMounted(() => {
            operatorMap.getDataList(null, null, null);
            operatorMap.getModules();
        })
        const dataMap = reactive({
            allRoleData:null,
            dataList: null,
            roles: null,
            editData: null,
            dataLoading: true,
            modules:[
                // treetable树的标题
                // { funId:'all',funName:'功能名称',funType:'类型', modDesc:'描述'}
            ],
            listQuery: {
                page: 1,
                limit: 9,
                pageCount: 5,
                total: 0,
                roleName:'',
            },
        })
        const dialogParam = reactive({
            edit: true,
            show: false,
        })
        const operatorMap = reactive({
            getModules: async ()=>{
                let param = {}
                param["param%5Bsupermodid%5D"] = "0";
                let result= await getModuleList(param);
                if (result.code == 1) {
                    result.data.forEach((obj,index)=>{
                        dataMap.modules.push(obj);
                    });
                }
            },
            async getDataList(page, total, limit) {
                if (page) {
                    dataMap.listQuery.page = page
                }
                if (limit) {
                    dataMap.listQuery.limit = limit
                }
                dataMap.dataLoading = true
                let queryParam = Object.assign({}, dataMap.listQuery)
                 queryParam["param%5Brolename%5D"] = dataMap.listQuery.roleName;
                let tmpData = await getALLRoles(queryParam);
                if (tmpData.code == 1) {
                    ///返回所有数据，数据量少，使用逻辑分页
                   dataMap.allRoleData = tmpData.data;
                    dataMap.dataLoading = false
                    operatorMap.handlePage();
                }
            },
            handlePage(){
                let startRow = 0;
                let endRow = 0;
                let tmpData =  dataMap.allRoleData;
                if (dataMap.listQuery.page > 1) {
                    startRow = (dataMap.listQuery.page - 1) * dataMap.listQuery.limit;
                }
                endRow = startRow + dataMap.listQuery.limit;
                if (dataMap.listQuery.roleName!=""){
                    tmpData = dataMap.allRoleData.filter(obj=>{
                        return obj.roleName.includes(dataMap.listQuery.roleName);
                    })
                }
                let sliceData =tmpData.slice(startRow, endRow);
                dataMap.dataList = sliceData;
                dataMap.listQuery.total = tmpData.length;
            },
            handleEdit(index, rowData, isEdit) {
                dataMap.editData = Object.assign({}, rowData);
                dialogParam.edit = isEdit;
                dialogParam.show = true;
            },
            handleClose(paramData) {
                let curIndex = dataMap.allRoleData.findIndex(function (element) {
                    return element["roleId"] == paramData["roleId"];
                });
                if (curIndex >= 0) {
                    dataMap.allRoleData[curIndex] = paramData;
                } else //没找到，新增数据
                    curIndex = dataMap.allRoleData.push(paramData);
                operatorMap.handlePage();
            },
            handleDelete(index, rowData) {
                ElMessageBox.confirm("确定要删除角色[" + rowData.roleName + "]？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let ids = [];
                    ids.push(rowData["roleId"]);
                    //修改
                    let result = await deleteRole(ids);
                    if (result.code ==1){
                        let curIndex = dataMap.allRoleData.findIndex(function (element) {
                            return element["roleId"] == rowData["roleId"];
                        });
                        if (curIndex>=0){
                            dataMap.allRoleData.splice(curIndex, 1)
                        }
                        await ElMessageBox.alert("数据删除成功！")
                        operatorMap.handlePage();
                    }
                }).catch(() => {
                    return false;
                });
            },
            handlePageSizeChange(val) {
                dataMap.listQuery.limit = val;
                operatorMap.handlePage();
            },
            handleCurrentPageChange(val) {
                dataMap.listQuery.page = val;
                operatorMap.handlePage();
            },
            handleAddData() {
                dataMap.editData = Object.assign({});
                dialogParam.edit = true;
                dialogParam.show = true;
            },
            handleFilter() {
                dataMap.listQuery.page = 1
                operatorMap.handlePage();
                //operatorMap.getDataList();
            },
            handleTreeClick(data) {
                dataMap.department = data;
                operatorMap.handleFilter();
            }
        })

        return {
            dialogParam,
            ...toRefs(dataMap), ...toRefs(operatorMap)
        }
    },
};
</script>

<style lang="scss" scoped>
.el-main {
    padding-top: 1px;
    padding-left: 5px;
    padding-right: 5px;
}

.el-header {
    padding-right: 5px;
    padding-left: 5px;
}

.el-switch {
    padding-top: 0px;
}
</style>
