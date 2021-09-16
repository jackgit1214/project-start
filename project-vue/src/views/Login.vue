<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form ref="login" :model="param" :rules="rules" class="ms-content" label-width="0px">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="username">
                        <template #prepend>
                            <el-button icon="el-icon-user"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="param.password" placeholder="password" type="password"
                              @keyup.enter="submitForm()">
                        <template #prepend>
                            <el-button icon="el-icon-lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import {ref, reactive} from "vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {loginAndGetToken} from '../config/tokenHandler';
import {getUserInfo} from "../api/getData";
export default {
    setup() {
        const router = useRouter();
        const param = reactive({
            username: "admin",
            password: "123456",
        });
        const rules = {
            username: [
                {
                    required: true,
                    message: "请输入用户名",
                    trigger: "blur",
                },
            ],
            password: [
                {required: true, message: "请输入密码", trigger: "blur"},
            ],
        };
        const store = useStore();
        store.commit("clearTags");
        const login = ref(null);
        const submitForm = () => {
            login.value.validate(async (valid) => {
                if (valid) {
                    const response = await loginAndGetToken(param.username,param.password);
                    if (response.status==200){
                        let tmpUser = await getUserInfo();
                        ElMessage.success("登录成功");
                         localStorage.setItem("curUser", JSON.stringify(tmpUser));
                        router.push("/");
                    }else{
                        ElMessage.error("登录失败:"+response.status+"("+response.error+")");
                        return false;
                    }
                } else {
                    ElMessage.error("登录失败");
                    return false;
                }
            });
        };
        return {
            param,
            rules,
            login,
            submitForm,
        };
    }
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(src/assets/images/project-bg.jpg);
    background-size: 100%;
}

.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}

.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}

.ms-content {
    padding: 30px 30px;
}

.login-btn {
    text-align: center;
}

.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}

.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>