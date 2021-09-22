<template>
    <el-tree :load="lazyTree" lazy style="height: 600px;" @node-click="handleNodeClick"
             :expand-on-click-node=true node-key="codeid" ref="codeTree" highlight-current :props="treeProps">
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
import { mapState, mapActions ,useStore} from 'vuex'
export default {
    name: "Code-tree",
    props: {},
    components: {},
    setup(props, ctx) {
        const store = useStore();
        onMounted(() => {

        })
        const isFirstLoad = ref(false);
        const dataMap = reactive({
            treeProps:{
                label: 'codename',
                isLeaf: 'endFlag',
                // children
            }
        });
        const codeTree = ref(null);
        const operatorMap = reactive({
            setDefaultNode:(nodeKey)=>{
                 codeTree.value.setCurrentKey(nodeKey);
                 let curNode = codeTree.value.getNode(nodeKey);
                ctx.emit('treeClick', curNode);
            },
            lazyTree: async (node, resolve) => {
                let param;
                if (node.data){
                    param={
                        codetype:node.data.codetype,
                        code: node.data.code,
                    }
                }
                await store.dispatch("dictionary/getDictionaryBySuper",param)
                const treeData =  store.state.dictionary.treeItems;
                resolve(treeData)
                 await nextTick(()=>{
                    if (!isFirstLoad.value){
                        isFirstLoad.value = true;
                        let nodeKey = treeData[0].codeid;
                        operatorMap.setDefaultNode(nodeKey);
                    }
                })
            },
            handleNodeClick: (data,node,nodeComm) =>{
                if(ctx.attrs.onTreeClick)
                    ctx.emit('treeClick', node);
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
                // let parentNode = departmentTree.value.getNode(parentData.deptId);
                // if (type!=2){
                //     parentNode.loaded = false; //动态加载的，需要重新刷新
                //     parentNode.expand();
                // }else{
                //     parentNode.loaded = false; //动态加载的，需要重新刷新
                //     parentNode.expand();
                // }

            }

        });
        return {
            codeTree,
            ...toRefs(operatorMap),
            ...toRefs(dataMap)
        }
    }

};
</script>
