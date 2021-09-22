<template>
    <div class="editStyle">
        <el-dialog ref="dialog" v-model="dialogParam.show"
                   :before-close="beforeClose" modal title="信息编辑" width="625px" @close="afterCloseD"
                   @open="afterOpenDialog" :destroy-on-close="true">
            <el-form ref="codeForm" :disabled="!dialogParam.edit" :inline="true" :model="editData"
                     label-position="right" label-width="140px" status-icon width="380px">
                <input v-model="editData.codeid" type="hidden">
                <input v-model="editData.superid" type="hidden">
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item label="字典类型代码：">
                                <el-input v-model="editData.codetype" disabled placeholder=""
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div>
                            <el-form-item label="字典类型名称：">
                                <el-input v-model="editData.content" disabled placeholder=""
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item
                                :rules="[{required:true,message:'代码不能为空',trigger:'blue'},
                                        {min: 1,max: 15,message: '长度在1到30个字符',trigger: 'blur' }]"
                                label="代码：" prop="code">
                                <el-input v-model="editData.code" placeholder="代码"
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div>
                            <el-form-item
                                :rules="[{required:true,message:'代码名称不能为空',trigger:'blue'},
                                        {min: 1,max: 15,message: '长度在1到30个字符',trigger: 'blur' }]"
                                label="代码名称：" prop="codeName">
                                <el-input v-model="editData.codename" placeholder="代码"
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="5">
                    <el-col :span="12">
                        <div>
                            <el-form-item label="上级：">
                                <el-input v-model="editData.superid" disabled placeholder="上级："
                                          size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div>
                            <el-form-item label="显示顺序：">
                                <el-input v-model.number="editData.orderby" size="small"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>

                </el-row>
                 <el-row :gutter="5">
                    <el-col :span="24">
                        <div>
                            <el-form-item label="描述信息：">
                                <el-input v-model="editData.remark" placeholder="备注"
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
import { useStore} from 'vuex'

export default defineComponent({

    components: {},
    setup(props, ctx) {
       const store = useStore();
        //基本操作不用返回的
       const codeForm = ref(ElForm);
        onMounted(() => {
            // DOM 元素将在初始渲染后分配给 ref
            watch(() => _.cloneDeep(dataMap.editData),
                (newValue, oldValue) => {
                    store.commit("dictionary/setEditData",newValue);
                },
                {deep: true}
            );

        })
       const dataMap = reactive({
           editData:{},
           isChanged: false,
           dialogParam:computed(()=>store.state.dictionary.dialogParam),
           oriData: computed(()=>{
               let tmpData = store.state.dictionary.editData;
               return JSON.parse(JSON.stringify(tmpData));
           }),
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
                        store.commit("dictionary/setDialogParam",{
                            show:false,
                            edit:true,
                        })
                        return true;
                    });
                } else {
                    store.commit("dictionary/setDialogParam",{
                        show:false,
                        edit:true,
                    })
                    return true;
                }
            },
            saveData: () => {
               store.dispatch("dictionary/saveDictionary");
                // ctx.emit('CloseD', dataMap.department);
            },
            handleChange(value) {
                console.log(value)
            },
            afterOpenDialog: () => {
                dataMap.editData = dataMap.oriData;
                 dataMap.isChanged = false;
            }
        })

        nextTick(()=>{
            //console.log(props.editData)
        })
        return {
            codeForm,store,
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
                    this.store.commit("dictionary/setDialogParam",{
                        show:false,
                        edit:true,
                    })
                    return true;
                });
            } else {
                // done(); 将窗口状态放入store时，不能直接更改
                this.store.commit("dictionary/setDialogParam",{
                    show:false,
                    edit:true,
                })
                return true;
            }
        },
        afterCloseD() {


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


