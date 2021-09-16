<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img :src="userInfo.imgSrc()" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{ userInfo.name }}</div>
                            <div>{{ userInfo.role() }}</div>
                        </div>
                    </div>
                    <div class="user-info-list">
                        上次登录时间：
                        <span>2021-11-01</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>北京</span>
                    </div>
                </el-card>
                <el-card shadow="hover" style="height:252px;">
                    <template #header>
                        <div class="clearfix">
                            <span>语言详情</span>
                        </div>
                    </template>
                    Vue
                    <el-progress :percentage="71.3" color="#42b983"></el-progress>JavaScript
                    <el-progress :percentage="24.1" color="#f1e05a"></el-progress>CSS
                    <el-progress :percentage="13.7"></el-progress>HTML
                    <el-progress :percentage="5.9" color="#f56c6c"></el-progress>
                </el-card>
            </el-col>
            <el-col :span="16">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-user-solid grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statsInfo["SysUser"]}}</div>
                                    <div>用户数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-message-solid grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statsInfo["SysRole"]}}</div>
                                    <div>角色数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-s-goods grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statsInfo["SysDept"]}}</div>
                                    <div>组织结构</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card shadow="hover" style="height:403px;">
                    <template #header>
                        <div class="clearfix">
                            <span>系统日志</span>
<!--                            <el-button style="float: right; padding: 3px 0" type="text">添加</el-button>-->
                        </div>
                    </template>

                    <el-table :show-header="false" :data="logList" style="width:100%;">
                        <el-table-column width="40">
                            <template #default="scope">
                                <el-checkbox v-model="scope.row.logId"></el-checkbox>
                            </template>
                        </el-table-column>
                        <el-table-column>
                            <template #default="scope">
                                <div class="todo-item" >{{ scope.row.modelName }}[{{scope.row.logTime}}]</div>
                            </template>
                        </el-table-column>
                        <el-table-column>
                            <template #default="scope">
                                <div class="todo-item" >{{ scope.row.description }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column width="60">
                            <template>
                                <i class="el-icon-edit"></i>
                                <i class="el-icon-delete"></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
             <el-col :span="12">
                 <el-tree                     :data="treeData"                     :props="treeProps"                 >
                     <template #default="{ node}">
                        <span class="custom-tree-node">
                          <span>{{ node.label }}</span>
                        </span>
                     </template>
                 </el-tree>
            </el-col>
            <el-col :span="12">
                <el-tree show-checkbox   :data="treeData"       :props="treeProps"                 >
                    <template #default="{ node,data }">
                        <el-table style="width: 100%" :data="[data]" :show-header="false">
                            <el-table-column prop="label" width="180"> </el-table-column>
                            <el-table-column prop="title" width="180"> </el-table-column>
                        </el-table>
                    </template>
                </el-tree>
            </el-col>
        </el-row>

    </div>
</template>

<script>
import Schart from "vue-schart";
import { reactive,onMounted,toRefs } from "vue";
import {getStatsInfo,getStatsLog} from "../api/getData";

export default {
    name: "dashboard",
    components: { Schart },

    setup(props, ctx) {
        const user = JSON.parse(localStorage.getItem("curUser"));

        onMounted(() => {
            operatorMap.initData();
        });

        let userInfo = Object.assign({},{
            loginName:user.username,
            name:user.name,
            role:()=>{
                let tmpRole;
                user.roles.forEach((curValue,index)=>{
                    tmpRole+curValue.roleName+","
                });
                return tmpRole;
            },
            imgSrc: ()=>{
                if (user.avatarPic.indexOf("data:image/gif")>=0)
                    return user.avatarPic;
                else
                    return "src/assets"+user.avatarPic;
            }
        });
        const statsInfo = reactive({});

        const logList = reactive([

        ]);

        const treeData = [
            {
                label:'label标题',
                title:'title标题',
                children: []
            },
            {
                id: 1,
                label: '一级 1',
                title: '一级 1',
                children: [
                    {
                        id: 4,
                        label: '二级 1-1',
                        title: '一级 1',
                        children: [
                            {
                                id: 9,
                                label: '三级 1-1-1',
                                title: '一级 1',
                            },
                            {
                                id: 10,
                                label: '三级 1-1-2',
                                title: '一级 1',
                            },
                        ],
                    },
                ],
            },
            {
                id: 2,
                label: '一级 2',
                title: '一级 1',
                children: [
                    {
                        id: 5,
                        label: '二级 2-1',
                        title: '一级 1',
                    },
                    {
                        id: 6,
                        label: '二级 2-2',
                        title: '一级 1',
                    },
                ],
            },
            {
                id: 3,
                label: '一级 3',
                title: '一级 1',
                children: [
                    {
                        id: 7,
                        label: '二级 3-1',
                        title: '一级 1',
                    },
                    {
                        id: 8,
                        label: '二级 3-2',
                        title: '一级 1',
                    },
                ],
            },
        ]
        const treeProps = {
            children: 'children',
            label: 'label',
        }
        const operatorMap = reactive({
            async initData(){
                try {
                    let tmpInfo = await getStatsInfo();
                    let $statsInfo = statsInfo;
                    for (let key in tmpInfo){
                        $statsInfo[key] = tmpInfo[key]["count"];
                    }
                    let logs = await getStatsLog();
                    logs.forEach((log,index)=>{
                       logList.push(log);
                    });
                } catch (err) {

                };
            },
        })
        return {
            userInfo,
            statsInfo,treeProps,treeData,
            logList,...toRefs(operatorMap)
        };
    }
};
</script>

<style scoped>
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart {
    width: 100%;
    height: 300px;
}
</style>
