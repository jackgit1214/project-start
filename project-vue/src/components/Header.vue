<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseCharge">
            <i v-if="!collapse" class="el-icon-s-fold"></i>
            <i v-else class="el-icon-s-unfold"></i>
        </div>
        <div class="logo">信息系统基础管理平台</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 消息中心 -->
                <div class="btn-bell">
                    <el-tooltip effect="dark" :content="userInfo.message?`有${userInfo.message}条未读消息`:`消息中心`" placement="bottom">
                        <router-link to="/tabs">
                            <i class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="userInfo.message"></span>
                </div>
                <!-- 用户头像 -->
                <div class="user-avator">
                    <img v-if="userInfo.isBase64Img" :src="userInfo.imgSrc" />
                    <img v-if="!userInfo.isBase64Img" :src="'src/assets'+userInfo.imgSrc"/>
                </div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{userInfo.loginName}}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="pwdEdit">密码修改</el-dropdown-item>
                            <el-dropdown-item command="user">个人中心</el-dropdown-item>
                            <a href="https://github.com/lin-xin/vue-manage-system" target="_blank">
                                <el-dropdown-item divided >项目仓库</el-dropdown-item>
                            </a>
                            <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
<!--                //@close-dialog="dataSubmit"-->
                <v-UserEdit  :userData="userInfo.userData" :dialogParam="winParam"
                             @close-dialog="childWinClose"  ref="editUserWin" :roles="roles" />

            </div>
        </div>
    </div>
</template>
<script>
import { computed, onMounted,reactive,ref,toRefs } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import {getUserInfo,getALLRoles} from "@/api/getData";
import vUserEdit from "@/views/user/UserEdit.vue"

export default {
    components:{
        vUserEdit
    },
    setup(props) {
        const winParam = reactive({
            edit:false,
            show:false,
        })
        const userInfo = reactive({
            loginName:'',
            name:'',
            imgSrc:'',
            message:0,
            isBase64Img:false,
            userData:{}
        });
        const store = useStore();
        const roles = ref(null);
        const collapse = computed(() => store.state.navi.collapse);
        // 侧边栏折叠
        const collapseCharge = () => {
            //store.dispatch('navi/handleCollapse', !collapse.value)
            store.commit("navi/handleCollapse", !collapse.value);
        };
        onMounted(() => {
            operatorMap.initData();
            operatorMap.getRoleData();
            if (document.body.clientWidth < 1500) {
                collapseCharge();
            }
        });

        const router = useRouter();
        const handleCommand = (command) =>{
            // 用户名下拉菜单选择事件
            if (command == "loginout") {
                localStorage.removeItem("sysToken");
                localStorage.removeItem("curUser");
                router.push("/login");
            } else if (command == "user") {
                //router.push("/user");
                winParam.show=true;
            } else if (command == "pwdEdit"){

            }
        }

        const operatorMap = reactive({
            async initData(){
                try {
                    let tmpUser = await getUserInfo();
                    Object.assign(userInfo,{
                        loginName:tmpUser.username,
                        name:tmpUser.name,
                        message: 5,
                        imgSrc: tmpUser.avatarPic,
                        isBase64Img: computed(()=>{
                            if (tmpUser.avatarPic.indexOf("data:image/gif")>=0)
                                return true;
                            return false;
                        }),
                        userData:tmpUser
                    })
                } catch (err) {

                };
            },
            async getRoleData(){
                let tmpData = await getALLRoles();
                if (tmpData.code == 1) {
                    roles.value = tmpData.data
                }
            },
        })
        return {
            userInfo,
            collapse,
            winParam,
            store,roles,
            collapseCharge,
            handleCommand,...toRefs(operatorMap)
        };
    },
    methods:{
        childWinClose(val){

            console.log("............childWinClose......................")
            console.log(val)
        },
        show(){
            console.log("............show......................")
        }

    }
};
</script>
<style scoped>
.header {
    position: relative;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 20px;
    color: #fff;
}
.collapse-btn {
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo {
    float: left;
    width: 250px;
    line-height: 70px;
}
.header-right {
    float: right;
    padding-right: 50px;
}
.header-user-con {
    display: flex;
    height: 70px;
    align-items: center;
}
.btn-fullscreen {
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
    position: relative;
    width: 30px;
    height: 30px;
    text-align: center;
    border-radius: 15px;
    cursor: pointer;
}
.btn-bell-badge {
    position: absolute;
    right: 0;
    top: -2px;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background: #f56c6c;
    color: #fff;
}
.btn-bell .el-icon-bell {
    color: #fff;
}
.user-name {
    margin-left: 10px;
}
.user-avator {
    margin-left: 20px;
}
.user-avator img {
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.el-dropdown-link {
    color: #fff;
    cursor: pointer;
}
.el-dropdown-menu__item {
    text-align: center;
}
</style>
