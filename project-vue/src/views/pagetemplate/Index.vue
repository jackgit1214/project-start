<template>
    <el-container>
        <el-aside width="200px">
            <el-card class="box-card">
                <department-tree @treeClick="handleTreeClick"></department-tree>
            </el-card>
        </el-aside>
        <el-main>
            <el-card class="box-card" style="margin-bottom:5px;height:50px;">
                <el-form :inline="true" ref="queryForm">
                    <el-form-item label="姓名：" prop="name">
                        <el-input ref="username" v-model="listQuery.name" clearable placeholder="用户姓名" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="登录名：" prop="username">
                        <el-input ref="username" v-model="listQuery.userName" clearable placeholder="登录名" size="small"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-search" size="small" type="primary" @click="handleFilter">查询</el-button>
                        <el-button icon="el-icon-edit" size="small" type="primary" @click="handleAddData">新增</el-button>
                    </el-form-item>
                    <!--                    @close-dialog="dataSubmit"-->
                    <edit-info v-if="editData" ref="editDialog" :dialogParam="dialogParam" :roles="roles"
                               :userData="editData"
                               @CloseD="handleClose"/>
                </el-form>
            </el-card>
            <el-card class="box-card">
                <!--   :data="userData"     -->
                <el-table ref="userData" :data="userList" border class="table" height="510" max-height="590"
                          header-cell-class-name="table-header">
                    <el-table-column align="center" label="头像" width="60">
                        <template #default="scope">
                            <el-image v-if="scope.row.avatar"
                                      :src="'data:image/gif;base64,'+scope.row.avatar" class="table-td-thumb"
                                      style="width:32px;height:32px;">
                            </el-image>
                            <el-image v-else :src="'src/assets//images/user.jpg'" class="table-td-thumb"
                                      style="width:32px;height:32px;">
                            </el-image>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="登录名" prop="userName" width="120"></el-table-column>
                    <el-table-column align="center" label="用户姓名" prop="name" width="140"></el-table-column>
                    <el-table-column align="center" label="性别" prop="gender" width="60">
                        <template #default="scope">
                            <el-switch v-model="scope.row.gender" :active-value=1 :inactive-value=0 disabled
                                       style="display: block">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="年龄" prop="age" width="60">
                    </el-table-column>
                    <el-table-column align="center" label="电话" prop="phone" width="110">
                    </el-table-column>
                    <!--                    <el-table-column label="状态" prop="USERSTATUS" width="80">-->
                    <!--                    </el-table-column>-->
                    <!--                    <el-table-column label="注册时间" prop="date"></el-table-column>-->
                    <el-table-column label="地址" prop="address" show-overflow-tooltip>
                    </el-table-column>
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
                                   @size-change="handlePageSizeChange" @current-change="handleCurrentPageChange"></el-pagination>
                </div>
            </el-card>

        </el-main>
    </el-container>
</template>

<script lang="ts">
import editInfo from "./Edit.vue";
import DepartmentTree from "../common/DepartmentTree.vue";
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import {getALLRoles, getUserList,deleteUser} from "../../api/getData";
import {onMounted, reactive, toRefs} from 'vue'
import {DefaultUserModel, UserModel} from "./user";

export default {
    name: "userListPage",
    components: {
        editInfo,
        DepartmentTree
    },
    setup(props: any, ctx: any) {
        onMounted(() => {
            operatorMap.getDataList(null, null, null)
            operatorMap.getRoleData();
        })
        const dataMap = reactive({
            userList: Array<UserModel>(),
            roles: Array(),
            department: {
                deptId: ""
            },
            editData: DefaultUserModel,
            dataLoading: true,
            listQuery: {
                page: 1,
                limit: 9,
                pageCount: 5,
                total: 0,
                userName: "",
                name: "",
                departmentId: ""
            },
        })
        const dialogParam = reactive({
            edit: true,
            show: false,
        })
        const operatorMap = reactive({
            async getDataList(page?: any, total?: any, limit?: any) {
                if (page) {
                    dataMap.listQuery.page = page
                }
                if (limit) {
                    dataMap.listQuery.limit = limit
                }

                dataMap.dataLoading = true
                let queryParam = Object.assign({}, dataMap.listQuery)
                queryParam["param%5Blogincode%5D"] =dataMap.listQuery.userName;
                queryParam["param%5Busername%5D"] =dataMap.listQuery.name;
                queryParam.departmentId = dataMap.department.deptId;
                let tmpData = await getUserList(queryParam);
                if (tmpData.code == 1) {
                    dataMap.userList = tmpData.data.pageDatas;
                    dataMap.listQuery.total = tmpData.data.totalSize;
                    dataMap.dataLoading = false
                }
            },
            getRoleData: async () => {
                let tmpData = await getALLRoles();
                if (tmpData.code == 1) {
                    dataMap.roles = tmpData.data
                }
            },
            handleEdit(index: any, rowData: any, isEdit: boolean) {
                dataMap.editData = Object.assign({}, rowData);
                dialogParam.edit = isEdit;
                dialogParam.show = true;
            },
            handleClose(user: any) {
                let curIndex = dataMap.userList.findIndex(function (element) {
                    return element.userId == user.userId;
                });
                if (curIndex>=0){
                    dataMap.userList[curIndex] = user;
                }else //没找到，新增数据
                    curIndex = dataMap.userList.push(user);
            },
            handleDelete(index:any,rowData:any){

                ElMessageBox.confirm("确定要删除用户["+rowData.name+"]？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let userIds = [];
                    userIds.push(rowData.userId);
                    let result = await deleteUser(userIds);
                    if (result.code ==1){
                        ElMessageBox.alert("数据删除成功！")
                        let curIndex = dataMap.userList.findIndex(function (element) {
                            return element.userId == rowData.userId;
                        });
                        if (curIndex>=0){
                            dataMap.userList.splice(curIndex, 1)
                        }
                    }
                }).catch(() => {
                    return false;
                });
            },
            handlePageSizeChange(val: any) {
                dataMap.listQuery.limit = val;
                operatorMap.getDataList();
            },
            handleCurrentPageChange(val: any) {
                dataMap.listQuery.page = val;
                operatorMap.getDataList();
            },
            handleAddData(){
                dataMap.editData = Object.assign({},DefaultUserModel);
                dialogParam.edit = true;
                dialogParam.show = true;
            },
            handleFilter() {
                dataMap.listQuery.page = 1
                operatorMap.getDataList();
            },
            handleTreeClick(data: any) {
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
