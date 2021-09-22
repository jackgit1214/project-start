<template>
    <el-container>
        <el-aside width="200px">
            <el-card class="box-card">
                <code-tree ref="codeTree" @treeClick="handleTreeClick"></code-tree>
            </el-card>
        </el-aside>
        <el-main>
            <el-card class="box-card" style="margin-bottom:5px;height:50px;">
                <el-form ref="queryForm" :inline="true">
                    <el-form-item label="名称：" prop="codename">
                        <el-input ref="codename"   v-model="queryCodeName" clearable placeholder="名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-search" size="small" type="primary" @click="handleFilter">查询
                        </el-button>
                        <el-button icon="el-icon-edit" size="small" type="primary" @click="handleAddData">新增</el-button>
                    </el-form-item>
                    <edit-info  ref="editDialog" />
                </el-form>
            </el-card>
            <el-card class="box-card">
                <el-table :data="dataList" border class="table" header-cell-class-name="table-header" height="510"
                          max-height="590">
                    <el-table-column align="center" label="序号" type="index" width="50"></el-table-column>
                    <el-table-column align="center" label="类型代码" prop="codetype" width="120"></el-table-column>
                    <el-table-column align="center" label="类型名称" prop="content" width="180"></el-table-column>
                    <el-table-column align="center" label="代码" prop="code" width="120"></el-table-column>
                    <el-table-column align="center" label="名称" prop="codename" width="120"></el-table-column>
                    <el-table-column align="center" label="顺序" prop="orderby" width="120"></el-table-column>
<!--                    <el-table-column align="center" label="是否末级" prop="endFlag" width="120"></el-table-column>-->
                    <el-table-column align="center" label="描述" prop="remark"></el-table-column>
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
// import DepartmentTree from "../common/DepartmentTree.vue";
import CodeTree from "./CodeTree.vue";
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import {onMounted, reactive, toRefs, ref, computed, nextTick} from 'vue'
import { mapState, mapActions ,useStore} from 'vuex'
export default {
    name: "dicListPage",
    components: {
        editInfo,
         CodeTree
    },
    setup(props, ctx) {
        const store = useStore();
        onMounted(() => {
            // store.dispatch("dictionary/getAllDictionary")
            // operatorMap.getDataList(null, null, null)
        })
        const codeTree = ref(null);
        const dataMap = reactive({
            dataList:computed(() => {
               return operatorMap.handlePage(store.state.dictionary.items);
            }),
            listQuery:computed(() => store.state.dictionary.pageParam),
            queryParam:computed(() => store.state.dictionary.queryParam),
            queryCodeName:"", //
        })
        const operatorMap = reactive({
            handleEdit(index, rowData, isEdit) {
                store.dispatch("dictionary/openDialog", {
                        data:rowData,
                        edit:isEdit
                    });
            },
            handlePage(data){
                let startRow = 0;
                let endRow = 0;

                if (dataMap.listQuery.page > 1) {
                    startRow = (dataMap.listQuery.page - 1) * dataMap.listQuery.limit;
                }
                endRow = startRow + dataMap.listQuery.limit;
                let sliceData =data.slice(startRow, endRow);
                return sliceData;
            },
            handleDelete(index, rowData) {
                ElMessageBox.confirm("确定要删除[" + rowData.codename + "]？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let ids = [];
                    ids.push(rowData["codeid"]);
                    await store.dispatch("dictionary/deleteDictionary",ids);
                    await ElMessageBox.alert("数据删除成功！")
                }).catch(() => {
                    return false;
                });
            },
            handlePageSizeChange(val) {
                store.commit("dictionary/setPageParam",{
                    pageCount:val,
                    page:dataMap.listQuery.page,
                })
            },
            handleCurrentPageChange(val) {
                store.commit("dictionary/setPageParam",{
                    pageCount:dataMap.listQuery.pageCount,
                    page:val,
                })
            },
            handleAddData() {
                let tmpData = store.getters["dictionary/getDefaultDictionary"];
                let selectData = store.getters["dictionary/getCurrentTreeNodeData"];
                tmpData.codetype = selectData.codetype;
                tmpData.content = selectData.content;
                tmpData.superid = selectData.code;
                store.dispatch("dictionary/openDialog", {
                    data:tmpData,
                    edit:true
                });
            },
            handleFilter() {
                store.commit("dictionary/setQueryParam", {
                    codename:dataMap.queryCodeName
                })
                store.dispatch("dictionary/getAllDictionary")
            },
            handleTreeClick(node) {
                store.dispatch("dictionary/handleTreeNode",node)
            }
        })
        nextTick(()=>{
            //console.log(dataMap.dialogParam)
        })
        return {
            codeTree, input:ref(""),store,
            ...toRefs(dataMap), ...toRefs(operatorMap)
        }
    },
    computed: {
        // 将所有参数抛出
        // ...mapState({
        //     dialogParam: computed(() => state.dictionary.dialogParam),
        //     listQuery:computed(() => state.dictionary.pageParam),
        // }),
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
