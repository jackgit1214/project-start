<template>
    <el-container>
        <el-aside width="200px">
            <el-card class="box-card">
                <department-tree ref="departmentTree" @treeClick="handleTreeClick"></department-tree>
            </el-card>
        </el-aside>
        <el-main>
            <el-card class="box-card" style="margin-bottom:5px;height:50px;">
                <el-form ref="queryForm" :inline="true">
                    <el-form-item label="部门名称：" prop="deptName">
                        <el-input ref="username" v-model="listQuery.deptName" clearable placeholder="部门名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-search" size="small" type="primary" @click="handleFilter">查询
                        </el-button>
                        <el-button icon="el-icon-edit" size="small" type="primary" @click="handleAddData">新增</el-button>
                    </el-form-item>
                    <edit-info  ref="editDialog" :dialogParam="dialogParam"
                               :editData="editData" @CloseD="handleClose"/>
                </el-form>
            </el-card>
            <el-card class="box-card">
                <el-table :data="dataList" border class="table" header-cell-class-name="table-header" height="510"
                          max-height="590">
                    <el-table-column align="center" label="序号" type="index" width="50"></el-table-column>
                    <el-table-column align="center" label="部门名称" prop="deptName" width="120"></el-table-column>
                    <el-table-column align="center" label="顺序" prop="sequence" width="120"></el-table-column>
                    <el-table-column align="center" label="是否末级" prop="endFlag" width="120"></el-table-column>
                    <el-table-column align="center" label="描述" prop="deptDesc" width="200"></el-table-column>

                    <el-table-column align="center" label="操作" width="180">
                        <template #default="scope">
                            <el-button icon="el-icon-edit" type="text"
                                       @click="handleEdit(scope.$index, scope.row,true)">编辑
                            </el-button>
                            <el-button class="red" icon="el-icon-delete" type="text"
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
import DepartmentTree from "../common/DepartmentTree.vue";
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import {getDepartmentBySuperId,deleteDepartment} from "../../api/getData";
import {onMounted, reactive, toRefs,ref} from 'vue'

export default {
    name: "deptListPage",
    components: {
        editInfo,
        DepartmentTree
    },
    setup(props, ctx) {
        onMounted(() => {
            operatorMap.getDataList(null, null, null)
        })
        const departmentTree = ref(null);
        const dataMap = reactive({
            dataList: null,
            roles: null,
            department: {
                deptId: ""
            },
            editData: null,
            dataLoading: true,
            listQuery: {
                page: 1,
                limit: 9,
                pageCount: 5,
                total: 0,
                deptName: ""
            },
        })
        const dialogParam = reactive({
            edit: true,
            show: false,
        })
        const operatorMap = reactive({
            async getDataList(page, total, limit) {
                if (page) {
                    dataMap.listQuery.page = page
                }
                if (limit) {
                    dataMap.listQuery.limit = limit
                }
                dataMap.dataLoading = true
                let queryParam = Object.assign({}, dataMap.listQuery)
                queryParam["param%5BDEPT_NAME%5D"] = dataMap.listQuery.deptName;
                queryParam.superId = dataMap.department.deptId;
                queryParam.includeSelf = true;
                let tmpData = await getDepartmentBySuperId(queryParam);
                if (tmpData.code == 1) {
                    dataMap.dataList = tmpData.data;
                    dataMap.listQuery.total = tmpData.data.length;
                    dataMap.dataLoading = false
                }
            },
            handleEdit(index, rowData, isEdit) {
                dataMap.editData = Object.assign({}, rowData);
                dialogParam.edit = isEdit;
                dialogParam.show = true;
            },
            handleClose(paramData) {
                let curIndex = dataMap.dataList.findIndex(function (element) {
                    return element["deptId"] == paramData["deptId"];
                });
                let type=1;
                if (curIndex >= 0) {
                    type=3;
                    dataMap.dataList[curIndex] = paramData;
                } else //没找到，新增数据
                    curIndex = dataMap.dataList.push(paramData);
                //树中数据处理
                departmentTree.value.refreshNode(dataMap.department,paramData,type)
            },
            handleDelete(index, rowData) {
                ElMessageBox.confirm("确定要删除部门[" + rowData.deptName + "]？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let ids = [];
                    ids.push(rowData["deptId"]);
                    let param = {
                        deptIds:ids,
                        superId:dataMap.department.deptId,
                    }
                    //修改
                    let result = await deleteDepartment(param);
                    if (result.code ==1){
                        departmentTree.value.refreshNode(dataMap.department,rowData,2)
                        let curIndex = dataMap.dataList.findIndex(function (element) {
                            return element["deptId"] == rowData["deptId"];
                        });
                        if (curIndex>=0){
                            dataMap.dataList.splice(curIndex, 1)
                        }
                        await ElMessageBox.alert("数据删除成功！")
                    }
                }).catch(() => {
                    return false;
                });
            },
            handlePageSizeChange(val) {
                dataMap.listQuery.limit = val;
                operatorMap.getDataList();
            },
            handleCurrentPageChange(val) {
                dataMap.listQuery.page = val;
                operatorMap.getDataList();
            },
            handleAddData() {
                dataMap.editData = Object.assign({});
                dataMap.editData.superId = dataMap.department.deptId;
                dataMap.editData.superDeptName = dataMap.department.deptName;
                dialogParam.edit = true;
                dialogParam.show = true;
            },
            handleFilter() {
                dataMap.listQuery.page = 1
                operatorMap.getDataList();
            },
            handleTreeClick(data) {
                dataMap.department = data;
                operatorMap.handleFilter();
            }
        })

        return {
            dialogParam,departmentTree,
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
