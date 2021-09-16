<template>
    <div class="userStyle">
        <el-dialog ref="dialog" v-model="dialogParam.show"
                   :before-close="beforeClose" modal title="信息编辑" width="625px" @close="afterCloseD"
                   @open="afterOpenDialog">
            <el-form ref="userForm" :disabled="!dialogParam.edit" :inline="true" :model="user" :rules="rules"
                     label-position="right" label-width="90px" status-icon width="380px">
                <el-tabs style="height: 470px;" type="card">
                    <el-tab-pane label="用户信息">
                        <input v-model="user.userId" type="hidden">
                        <el-row :gutter="5">
                            <el-col :span="12">
                                <div>
                                    <el-form-item
                                        :rules="[{required:true,message:'登录名不能为空',trigger:'blue'},
                                        {min: 6,max: 15,message: '长度在6到15个字符',trigger: 'blur' }]"
                                        label="登录名：" prop="username">
                                        <el-input v-if="user.userId!=''" v-model="user.userName" placeholder="登录名" size="small"
                                                  disabled></el-input>
                                        <el-input v-else v-model="user.userName" placeholder="登录名" size="small"
                                                   ></el-input>
                                    </el-form-item>
                                </div>
                                <div>
                                    <el-form-item
                                        :rules="[{required:true,message:'姓名不能为空'}, {max: 30,message: '长度介于1到30个字符',trigger: 'blur' }]"
                                        label="姓名：" prop="name">
                                        <el-input v-model="user.name" placeholder="姓名" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div class="info-image" @click="showDialog">
                                    <img :src="avatarData.avatarImg"/>
                                    <span class="info-edit">
                                <i class="el-icon-lx-camerafill"></i>
                            </span>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">

                            <el-col :span="12">
                                <div>
                                    <el-form-item label="所属部门：">
                                    <el-cascader v-model="userDepartments"  size="small"
                                                 ref="selectDept" :props="deptProps"  collapse-tags clearable ></el-cascader>
                                    </el-form-item>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="性别：">
                                        <el-switch v-model="user.gender" :active-value="1" :inactive-value="0"
                                                   style="display: block"/>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="出生日期：">
                                        <!--                                        :shortcuts="datepickerOption.shortcuts"-->
                                        <el-date-picker v-model="user.birthday"
                                                        :disabled-date="datepickerOption.disabledDate"
                                                        placeholder="选择出生日期"
                                                        size="small"
                                                        type="date"
                                                        value-format="YYYY-MM-DD"
                                                        @change="dataPickerChange"
                                        ></el-date-picker>
                                    </el-form-item>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="年龄：">
                                        <el-input v-model="user.age" :disabled="true" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">

                            <el-col :span="12">
                                <div>
                                    <el-form-item label="显示顺序：" prop="userorder">
                                        <el-input v-model.number="user.userorder" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="电话：" prop="phone">
                                        <el-input v-model="user.phone" placeholder="电话" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">
                            <el-col :span="12">
                                <div>
                                    <el-form-item
                                        :rules="[ { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]"
                                        label="email："
                                        prop="email"
                                    >
                                        <el-input v-model="user.email" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div>
                                    <el-form-item label="qq：" prop="qq">
                                        <el-input v-model.number="user.qq" size="small"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row :gutter="5">
                            <el-col :span="24">
                                <div>
                                    <el-form-item label="地址：">
                                        <el-input v-model="user.address" placeholder="地址"
                                                  type="textarea"></el-input>
                                    </el-form-item>
                                </div>
                            </el-col>
                        </el-row>
                    </el-tab-pane>
                    <el-tab-pane label="所属角色">
<!--                        //:left-default-checked="['6b912123f72a465782c91f0f4d330dec']"-->
<!--                        :right-default-checked="[1]"-->
                        <div style="text-align: center">
                            <el-transfer ref="selectRoles"
                                :data="roles" v-model="userRoles"
                                :format="{noChecked: '${total}',        hasChecked: '${checked}/${total}' }"
                                :props="{
                                      key: 'roleId',
                                      label: 'roleName'
                                }"
                                :titles="['角色', '已选角色']"
                                style="text-align: left; display: inline-block"
                            ></el-transfer>
                        </div>
                    </el-tab-pane>
                </el-tabs>

            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button   @click="closeDialog">关 闭</el-button>
                    <el-button v-if="dialogParam.edit" type="primary" @click="saveData">确 定</el-button>
                </span>
            </template>
        </el-dialog>
        <el-dialog v-model="avatarData.dialogVisible" title="裁剪图片" width="600px">
            <vue-cropper ref="cropper" :cropmove="cropImage" :ready="cropImage" :src="avatarData.imgSrc"
                         :zoom="cropImage"
                         style="width: 100%; height: 400px"></vue-cropper>
            <template #footer>
                <span class="dialog-footer">
                    <el-button class="crop-demo-btn" type="primary">选择图片
                        <input accept="image/*" class="crop-input" name="image" type="file" @change="setImage"/>
                    </el-button>
                    <el-button type="primary" @click="saveAvatar">确定</el-button>
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
import VueCropper from "vue-cropperjs";
import "cropperjs/dist/cropper.css";
import {createUser, saveUserByUserId,getDepartmentBySuperId} from "../../api/getData";
import {DefaultUserModel} from "./user";

export default defineComponent({
    props: {
        dialogParam: {
            type: Object,
            default: {
                show: false,
                edit: false,
            }
        },
        userData: Object,
        roles:Object,
    },
    components: {
        VueCropper,
    },
    setup(props, ctx) {
        //基本操作不用返回的
        const {proxy} = getCurrentInstance()
        const baseOperator = {
            checkAge: (rule, value, callback) => {
                // if (!value) {
                //     return callback(new Error('qq不能为空'));
                // }
                setTimeout(() => {
                    if (!Number.isInteger(value)) {
                        callback(new Error('请输入数字值'));
                    } else {
                        callback();
                    }
                }, 1000);
            },
            checkPhone: (rule, value, callback) => {
                let phoneP = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
                if (!value) {
                    return callback(new Error('电话不能为空'));
                }
                setTimeout(() => {
                    if (!phoneP.test(value)) {
                        callback(new Error('请输入正确的手机号码'));
                    } else {
                        callback();
                    }
                }, 1000);
            },
        }

        const userForm = ref(ElForm);
        onMounted(() => {

            // DOM 元素将在初始渲染后分配给 ref
            watch(() => _.cloneDeep(props.dialogParam),
                (newValue, oldValue) => { //这里新旧相同，需要处理，单独监听基本类型时,是不一样的，应用了cloneDeep，对象类型即实现
                    // console.log("this.  changed..........")
                    // console.log(oldValue)
                    // console.log(newValue)

                },
                {deep: true}
            );
            watch(() => _.cloneDeep(props.userData),
                (newValue, oldValue) => { //这里新旧相同，需要处理，单独监听基本类型时,是不一样的，应用了cloneDeep，对象类型即实现
                    // console.log("this.  changed..........")
                    // console.log(oldValue)
                    //dataMap.user = newValue;
                    dataMap.isChanged = true;
                },
                {deep: true}
            );
        })
        onActivated(() => {

        })
        onRenderTriggered(() => {
            //console.log('onRenderTriggered!')
        })
        onUpdated(() => {
            nextTick(() => {
            })
        })

        onUnmounted(() => {
            console.log('unmounted!')
        })

        const cropper = ref(null);
        const selectRoles = ref(null);
        const selectDept = ref(null);
        const dataMap = reactive({
            deptProps:{
                multiple:true,
                // checkStrictly:true,
                emitPath:true,
                lazy:true,
                value:"deptId",
                label: 'title',
                lazyLoad:async (node, resolve)=> {
                    const { level } = node
                    let param = {
                        superId: level!=0?node.value:"xxx"
                    }
                    let result = await getDepartmentBySuperId(param);
                    if (result.code == 1) {
                        const tmpNode = result.data;
                        resolve(tmpNode)
                    }
                },
            },
            user: {},
            userRoles:[],
            userDepartments:[],
            avatarData: {
                changePic: false,
                blobImg: "",
                imgSrc: "",
                cropImg: "",
                dialogVisible: ref(false),
                avatarImg: "",
            },
            datepickerOption: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                }
            },
            rules: {
                qq: [
                    {validator: baseOperator.checkAge, trigger: 'blur'}
                ],
                phone: [
                    {validator: baseOperator.checkPhone, trigger: 'blur', required: true}
                ]
            },
            isChanged: ref(false)
        })
        const imgOperator = reactive({
            showDialog: () => {
                dataMap.avatarData.dialogVisible = true;
                dataMap.avatarData.imgSrc = dataMap.avatarData.avatarImg;
            },
            setImage: (e) => {
                const file = e.target.files[0];
                if (!file.type.includes("image/")) {
                    return;
                }
                const reader = new FileReader();
                reader.onload = (event) => {
                    dataMap.avatarData.dialogVisible = true;
                    dataMap.avatarData.imgSrc = event.target.result;
                    cropper && cropper.value.replace(event.target.result);
                };
                reader.readAsDataURL(file);
            },
            cropImage: () => {
                let canvas = cropper.value.getCroppedCanvas();
                canvas.toBlob(function (blob) {
                    dataMap.avatarData.blobImg = blob;
                });
                dataMap.avatarData.cropImg = cropper.value.getCroppedCanvas().toDataURL();
            },
            saveAvatar: () => {
                dataMap.avatarData.avatarImg = dataMap.avatarData.cropImg;
                dataMap.avatarData.dialogVisible = false;
                dataMap.user.avatarPic  = dataMap.avatarData.cropImg;
                let arr = dataMap.avatarData.cropImg.split(',')
                dataMap.user.avatar = arr[1];
                dataMap.isChanged = true;
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
                let tmpData = Object.assign({},dataMap.user)
                tmpData.userRoles = selectRoles.value.modelValue;
                tmpData.departmentIds =[];
                selectDept.value.getCheckedNodes().forEach((obj,index)=>{
                    tmpData.departmentIds.push(obj.value);
                });
                let result;
                if (dataMap.user.userId=="") {
                    if (dataMap.avatarData.imgSrc){
                        tmpData.avatarFile = new File([dataMap.avatarData.blobImg],"new.jpg");
                    }else
                        tmpData.avatarFile = new File([], "new.jpg");
                    result = await createUser(tmpData);
                } else {
                    delete tmpData["departments"];
                    delete tmpData["authorities"];
                    delete tmpData["modules"];
                    delete tmpData["roles"];
                    delete tmpData["avatar"];
                    delete tmpData["avatarPic"];
                    let imgBlob ="";
                    if (dataMap.user.avatar)
                         imgBlob = operatorMap.dataURLtoBlob(dataMap.user.avatarPic);
                    tmpData.avatarFile = new File([imgBlob], tmpData.userId+".jpg");
                    result = await saveUserByUserId(tmpData, tmpData.userId);
                    dataMap.isChanged = false;
                }
                if (result.code ==1){
                    dataMap.user.roles = selectRoles.value.targetData;
                    props.dialogParam.show = false;
                    dataMap.user.userId= result.data.userId;
                    ctx.emit('CloseD', dataMap.user);
                }
            },
            dataURLtoBlob(dataUrl) {
                let arr = dataUrl.split(',')
                let mime = arr[0].match(/:(.*?);/)[1]
                let bStr = atob(arr[1])
                let n = bStr.length
                let u8arr = new Uint8Array(n)
                while (n--) {
                    u8arr[n] = bStr.charCodeAt(n)
                }
                return new Blob([u8arr], {type: mime})
            },
            handleChange(value) {
                console.log(value)
            },
            afterOpenDialog: () => {
                dataMap.user = {};
                dataMap.userRoles.length = 0; //清空数组
                dataMap.userDepartments.length = 0; //清空数组
                dataMap.user = props.userData;
                // dataMap.user.departments.forEach((obj,index)=>{
                //     dataMap.userDepartments.push([obj]);
                // })
                dataMap.userDepartments =   [
                   ["1","9594298122ab492693b7dd580aa23caa"],
                        ["1","e55b9950872040d6bb15bc888944e729"]
                    ]
                dataMap.user.roles　&& dataMap.user.roles.forEach(function(obj,index){
                    dataMap.userRoles.push(obj.roleId);
                })
                if (dataMap.user.avatar) {
                    dataMap.avatarData.avatarImg = 'data:image/gif;base64,'+dataMap.user.avatar;
                } else
                    dataMap.avatarData.avatarImg = "src/assets/images/user.jpg";

                if (cropper.value){
                    cropper.value.replace(dataMap.avatarData.avatarImg,true);
                }
                if (dataMap.user.userId==""){
                    dataMap.avatarData.imgSrc = "src/assets/images/user.jpg"
                }
                dataMap.isChanged = false;
            }
        })

        return {
            cropper,selectRoles, userForm,...toRefs(imgOperator),selectDept,
            ...toRefs(operatorMap),
            ...toRefs(dataMap)
        }
    },
    methods: {
        dataPickerChange(val) {
            let curDate = new Date();
            let birthday = new Date(val);
            this.userData.age = curDate.getFullYear() - birthday.getFullYear();
        },


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
.userStyle {
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


