<template>
    <div class="editStyle">
        <el-dialog ref="dialog" v-model="dialogParam.show"
                   :before-close="beforeClose" modal title="信息编辑" width="625px" @close="afterCloseD"
                   @opened="afterOpenDialog">
            <el-form ref="roleForm" :disabled="!dialogParam.edit || role.isSystem == 1" :inline="true" :model="role"
                     label-position="right" label-width="90px" status-icon width="380px">
                <el-tabs style="height: 300px;" type="card">
                    <el-tab-pane label="角色信息">
                        <input v-model="role.roleId" type="hidden">
                        <el-row :gutter="5">
                            <el-col :span="12">
                                <div>
                                    <el-form-item
                                        :rules="[{required:true,message:'角色名不能为空',trigger:'blue'},
                                        {min: 1,max: 15,message: '长度在1到30个字符',trigger: 'blur' }]"
                                        label="角色名称：" prop="roleName">
                                        <el-input v-model="role.roleName" placeholder="角色名称"
                                                  size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>

                        </el-row>
                        <el-row :gutter="5">
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="系统角色：">
                                        <el-switch v-model="role.isSystem"  active-text="是" active-value="1"   inactive-value="0"
                                                   inactive-text="否" disabled active-color="#13ce66" inactive-color="#ff4949">
                                        </el-switch>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">
                            <el-col :span="24">
                                <div>
                                    <el-form-item label="描述信息：">
                                        <el-input v-model="role.roleDesc" placeholder="描述信息"
                                                  type="textarea"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                    </el-tab-pane>
                    <el-tab-pane label="角色功能">
                        <div style="text-align: center;height:240px;overflow-y: auto">
                            <el-tree ref="moduleTree" show-checkbox   :data="modules"  node-key="funId"   :props="treeProps" >
                                <template #default="{ node,data }">
                                    <el-table style="width: 100%" :data="[data]"  :show-header="false">
                                        <el-table-column prop="funName" width="160"> </el-table-column>
                                        <el-table-column prop="funType" width="80"> </el-table-column>
                                        <el-table-column prop="modDesc" width="260"> </el-table-column>
                                    </el-table>
                                </template>
                            </el-tree>
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeDialog">关 闭</el-button>
                    <el-button v-if="dialogParam.edit" type="primary" @click="saveData">确 定</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
import {
    ref,
    defineComponent,
    onMounted,
    onUpdated,
    reactive,
    computed,
    watch,
    nextTick,
    watchEffect,
    unref,
    toRefs
} from "vue";
import _ from 'lodash';
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import {saveRole} from "../../api/getData";

export default defineComponent({
    props: {
        dialogParam: {
            type: Object,
            default: {
                show: false,
                edit: false,
            }
        },
        editData: Object,
        modules:Object,
    },
    components: {},
    setup(props, ctx) {
        //基本操作不用返回的
        const roleForm = ref(ElForm);
        const moduleTree = ref();
        onMounted(() => {

            // DOM 元素将在初始渲染后分配给 ref
            watch(() => _.cloneDeep(props.dialogParam),
                (newValue, oldValue) => { //这里新旧相同，需要处理，单独监听基本类型时,是不一样的，应用了cloneDeep，对象类型即实现
                },
                {deep: true}
            );

        })

        onUpdated(() => {
            nextTick(() => {
            })
        })

        const dataMap = reactive({
            role: {
                roleId:'',
                roleName:'',
                roleDesc:'',
                isSystem:'',
            },
            checkModules:[],
            isChanged: false,
            treeProps: {
                children: 'child',
                label: 'funName',
            },
        })

        const operatorMap = reactive({
            closeDialog: () => {
                if (dataMap.isChanged) {
                    ElMessageBox.confirm("数据已修改，是否保存？", '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        return false;
                    }).catch(() => {
                        props.dialogParam.show = false;
                        return true;
                    });
                } else {
                    props.dialogParam.show = false;
                    return true;
                }
            },
            saveData: async () => {
                let roleFun = [];
                moduleTree.value.getCheckedNodes(false,true).forEach((obj,index)=>{
                   roleFun.push(obj.funId);
                });
                let tmpData = Object.assign({}, dataMap.role)
                let param = {
                    role:tmpData,
                    roleFun:roleFun,
                }
                let result= await saveRole(param);
                if (result.code == 1) {
                    props.dialogParam.show = false;
                    dataMap.role.modules =result.data.modules;
                    dataMap.role.roleId = result.data.roleId;
                    ctx.emit('CloseD', dataMap.role);
                }
            },

            handleChange(value) {
                console.log(value)
            },

            afterOpenDialog: () => {
                dataMap.role = {};
                dataMap.role = props.editData;
                dataMap.checkModules.length = 0;
                props.editData.modules && props.editData.modules.forEach((obj,index)=>{
                    if (obj.child.length==0) //选中页子节点，父节点交给e-tree
                        dataMap.checkModules.push(obj.funId);
                })
                //dataMap.checkModules.push("500")
                moduleTree.value.setCheckedKeys(dataMap.checkModules)
                dataMap.isChanged = false;
            }
        })

        return {
              roleForm,moduleTree,
            ...toRefs(operatorMap),
            ...toRefs(dataMap)
        }
    },
    methods: {

        beforeClose(done) {
            let $this = this;
            if (this.isChanged) {
                this.$confirm("数据已修改，是否保存？", "提示", {
                    confirmButtonText: "是",
                    cancelButtonText: "否"
                }).then(_ => {
                    return false;
                }).catch(_ => {
                    done();
                    return true;
                });
            } else {
                done();
                return true;
            }
            console.log("............点击关闭（右上角的X）的执行事件.............")
        },
        afterCloseD() {
            console.log("................关闭后回调.....................")

        }
    },

});
</script>
<style lang="scss" scoped>
.editStyle {
    .el-input {
        width: 140px;
    }

    .el-textarea {
        width: 400px;
    }

}

.info {
    text-align: center;
    padding: 35px 0;
}

.info-image {
    position: relative;
    margin: auto;
    width: 100px;
    height: 100px;
    background: #f8f8f8;
    border: 1px solid #eee;
    border-radius: 5px;
    overflow: hidden;
}

.info-image img {
    width: 100%;
    height: 100%;
}

.info-edit {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.info-edit i {
    color: #eee;
    font-size: 25px;
}

.info-image:hover .info-edit {
    opacity: 1;
}

.info-name {
    margin: 15px 0 10px;
    font-size: 24px;
    font-weight: 500;
    color: #262626;
}

.crop-demo-btn {
    position: relative;
}

.crop-input {
    position: absolute;
    width: 100px;
    height: 40px;
    left: 0;
    top: 0;
    opacity: 0;
    cursor: pointer;
}
.el-table td, .el-table th {
    padding: 2px 0 !important;
}
</style>


