<template>
    <div class="sidebar">
        <el-menu :collapse="collapse" :default-active="onRoutes" active-text-color="#20a0ff" background-color="#324157"
                 class="sidebar-el-menu" router text-color="#bfcbd9" unique-opened>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :key="item.index" :index="item.index">
                        <template #title>
                            <i :class="item.icon"></i>
                            <span>{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :key="subItem.index" :index="subItem.index">
                                <template #title>
                                    <i :class="subItem.icon"></i>
                                    <span>{{ subItem.title }}</span>
                                </template>
                                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                                    <i :class="threeItem.icon"></i>
                                    <span>{{ threeItem.title }}</span>
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :key="subItem.index" :index="subItem.index">
                                <i :class="subItem.icon"></i>
                                <span>{{ subItem.title }}</span>
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :key="item.index" :index="item.index">
                        <i :class="item.icon"></i>
                        <template #title>{{ item.title }}</template>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import {computed, watch, reactive} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {getUserModuleList} from '../api/getData';

export default {
    data() {
        return {
            tableData: [],
            currentRow: null,
            offset: 0,
            limit: 20,
            count: 0,
            currentPage: 1,
        }
    },
    created() {
        //this.restaurant_id = this.$route.query.restaurant_id;
        this.initData();
    },
    setup() {
        let items = reactive([]);
        const route = useRoute();
        const onRoutes = computed(() => {
            return route.path;
        });
        const store = useStore();
        const collapse = computed(() => store.state.navi.collapse);
        return {
            items,
            onRoutes,
            collapse,
        };
    },
    methods: {
        async initData() {
            try {
                const moduleData = await getUserModuleList("admin",{page:1,limit:10000});
                if (moduleData.code == 1) {
                    Object.assign(this.items,this.generateItems(moduleData.data))
                    //console.log(this.items);
                } else {
                    throw new Error('获取数据失败');
                }
            } catch (err) {

            };
        },
        generateItems(moduleData) {
            let tempItem = []
            // console.log(moduleData)
            moduleData.forEach(item => {
                let itemData = {};
                itemData.icon = 'fa ' + item.funIcon;
                itemData.title = item.funName;
                itemData.index = item.urlLink;
                itemData.id = item.funId;
                if (item.child && item.child.length > 0)
                    itemData.subs = this.generateItems(item.child)
                tempItem.push(itemData);
            });
            // console.log(tempItem);
            return tempItem;
        }
    }
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
    width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
    width: 200px;
}

.sidebar > ul {
    height: 100%;
}
</style>
