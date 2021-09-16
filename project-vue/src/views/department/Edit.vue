<template>
    <div class="editStyle">
        <el-dialog ref="dialog" v-model="dialogParam.show"
                   :before-close="beforeClose" modal title="信息编辑" width="625px" @close="afterCloseD"
                   @open="afterOpenDialog">
            <el-form ref="deptForm" :disabled="!dialogParam.edit" :inline="true" :model="department"
                     label-position="right" label-width="90px" status-icon width="380px">
                <input v-model="department.deptId" type="hidden">
                <input v-model="department.superId" type="hidden">
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item
                                :rules="[{required:true,message:'部门名不能为空',trigger:'blue'},
                                        {min: 1,max: 15,message: '长度在1到30个字符',trigger: 'blur' }]"
                                label="部门名称：" prop="deptName">
                                <el-input v-model="department.deptName" placeholder="部门名称"
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>

                </el-row>
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item label="上级部门：">
                                <el-input v-if="department.superDeptName!='xxx' " v-model="department.superDeptName" disabled placeholder="上级部门"
                                          size="small"></el-input>
                                <el-input v-else model-value="上级部门" disabled placeholder=""
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item label="显示顺序：">
                                <el-input v-model.number="department.sequence" size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>

                </el-row>
                 <el-row :gutter="5">
                    <el-col :span="24">
                        <div>
                            <el-form-item label="描述信息：">
                                <el-input v-model="department.deptDesc" placeholder="地址"
                                          type="textarea"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
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
    computed,
    ref,
    defineComponent,
    onActivated,
    onRenderTriggered,
    onMounted,
    onUpdated,
    onRenderTracked,
    getCurrentInstance,
    reactive,
    onUnmounted,
    watch,
    nextTick,
    watchEffect,
    unref,
    toRefs
} from "vue";
import _ from 'lodash';
import {ElForm, ElMessage, ElMessageBox} from 'element-plus'
import "cropperjs/dist/cropper.css";
import {saveDepartment,  getDepartmentBySuperId} from "../../api/getData";

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
    },
    components: {},
    setup(props, ctx) {
        //基本操作不用返回的
        const deptForm = ref(ElForm);
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

        const selectDept = ref(null);
        const dataMap = reactive({
            deptProps: {
                multiple: false,
                // checkStrictly:true,
                //emitPath: true,
                lazy: true,
                value: "deptId",
                label: 'title',
                lazyLoad: async (node, resolve) => {
                    const {level} = node
                    let param = {
                        superId: level != 0 ? node.value : "xxx"
                    }
                    let result = await getDepartmentBySuperId(param);
                    if (result.code == 1) {
                        const tmpNode = result.data;
                        resolve(tmpNode)
                    }
                },
            },
            department: {
                deptId:'',
                deptName:'',
                superId:'',
                sequence:0,
                deptDesc:'',
                superDeptName:"",
            },
            isChanged: false
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
                let tmpData = Object.assign({}, dataMap.department)
                let result= await saveDepartment(tmpData);
                if (result.code == 1) {
                    props.dialogParam.show = false;
                    dataMap.department.deptId = result.data.deptId;
                    ctx.emit('CloseD', dataMap.department);

                }
            },
            handleChange(value) {
                console.log(value)
            },
            afterOpenDialog: () => {
                dataMap.department = {};
                dataMap.department = props.editData;
                dataMap.isChanged = false;
            }
        })

        return {
              deptForm, selectDept,
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
</style>


