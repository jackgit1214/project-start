<template>
    <el-tree :load="lazyTree" lazy style="height: 600px;" @node-click="handleNodeClick"
             :expand-on-click-node=true node-key="deptId" ref="departmentTree" highlight-current :props="treeProps">
            </el-tree>
</template>

<script>
import {
    computed, onBeforeMount, onBeforeUpdate,
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
import {getDepartmentTree,getDepartmentBySuperId} from "../../api/getData";
export default {
    name: "department-tree",
    props: {},
    components: {},
    setup(props, ctx) {
        const {proxy} = getCurrentInstance();
        onBeforeMount(() => {
            console.log(".department...................onBeforeMount......")
        })
        onMounted(() => {
            console.log(".department...................onMounted......")
            //operatorMap.getDepartments();
        })
        onActivated(() => {
            console.log(".department...................onActivated......")
        })
        onRenderTriggered(() => {
            console.log(".department...................onRenderTriggered......")
        })
        onBeforeUpdate(() => {
            console.log(".department...................onBeforeUpdate......")
        })
        onUpdated(() => {
            console.log(".department...................onUpdated......")
            nextTick(() => {
                console.log(".department.........onUpdated..........nextTick......")
            })
        })
        onUnmounted(() => {
            console.log(".department...................onUnmounted......")
        })
        const dataMap = reactive({
            departments: [],
            treeProps:{
                label: 'title',
                isLeaf: 'endFlag',
                // children
            }
        });
        const departmentTree = ref(null);
        const operatorMap = reactive({
            getDepartments: async () => {
                let result = await getDepartmentTree();

                if (result.code == 1) {
                    dataMap.departments = result.data;
                    return result.data;
                }
            },
            lazyTree: async (node, resolve) => {
                let param = {
                    superId: node.data?node.key:"xxx",
                    includeSelf:false,
                }
                // param["param%5BdeptName%5D"] ='';
                let result = await getDepartmentBySuperId(param);
                if (result.code == 1) {
                    dataMap.departments = result.data;
                    resolve(result.data)
                }
            },
            handleNodeClick: (data,node,nodeComm) =>{
                if(ctx.attrs.onTreeClick)
                    ctx.emit('treeClick', data);
                else
                    console.log("无事件响应................");
            },
            /**
             * 根据节点变化 ，刷新节点，节点下可能是增加、删除或修改，
             * @param parentdata 需要处理的节点data，
             * @param data 节点下的数据，
             * @param type 处理类型，1为增加，2为删除，3为更新
             */
            refreshNode:(parentData,data,type) =>{
                let parentNode = departmentTree.value.getNode(parentData.deptId);
                if (type!=2){
                    parentNode.loaded = false; //动态加载的，需要重新刷新
                    parentNode.expand();
                }else{
                    parentNode.loaded = false; //动态加载的，需要重新刷新
                    parentNode.expand();
                }

            }

        });
        return {
            departmentTree,
            ...toRefs(operatorMap),
            ...toRefs(dataMap)
        }
    }

};
</script>
