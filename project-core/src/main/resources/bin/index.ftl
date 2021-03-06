<div style="padding-left: 5px;" id="${pageVar}List" data-refresh="false">
    <div class="layui-row layui-col-space4">

        <div class="layui-col-md12">
            <div class="layui-panel" style="border:1px solid silver;">
                <div style="padding:5px;">
                    <form class="layui-form" action="">
                        <div class="layui-form-item" style="margin-bottom: 0px">
                            <div class="layui-inline">
                                <label class="layui-form-label">查询1：</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="queryName1" lay-verify="title" autocomplete="off"
                                           class="layui-input">
                                </div>
                                <label class="layui-form-label">查询2：</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="queryName2" autocomplete="off" class="layui-input">
                                </div>
                                <div class="layui-input-inline">
                                    <button type="submit" class="layui-btn" lay-submit="" lay-filter="${pageVar}Search">查询
                                    </button>
                                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <table id="${pageVar}Table" lay-filter="${pageVar}Table"></table>
        </div>
    </div>
</div>
<script type="text/html" id="${pageVar}Bar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="add"><i class="layui-icon">&#xe654;</i>添加</button>
        <button class="layui-btn layui-btn-sm" lay-event="update"><i class="layui-icon">&#xe642;</i>编辑</button>
        <button class="layui-btn layui-btn-sm" lay-event="delete"><i class="layui-icon">&#xe640;</i>删除</button>
    </div>
</script>

<script>
    layui.use(['dtree','util', 'table', 'fwAdmin'], function () {
        let util = layui.util,
            dtree = layui.dtree,
            fwAdmin = layui.fwAdmin,
            form = layui.form,
            table = layui.table;
        //监听部门工具栏事件
        let pageH = fwAdmin.pageAreaHeight();
        table.on('toolbar(${pageVar}Table)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id)
                , data = checkStatus.data; //获取选中的数据
            switch (obj.event) {
                case 'add':
                    //Ajax获取
                    showEditWin("");
                    break;
                case 'update':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else if (data.length > 1) {
                        layer.msg('只能同时编辑一个');
                    } else {
                        showEditWin(checkStatus.data[0].primaryKey);
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        del${pageVar}(checkStatus.data)
                    }
                    break;
            } ;
        });
        table.on('tool(${pageVar}Table)', function (obj) {
            //obj 当前行对象
            if (obj.event == "del")
                layer.msg("删除单行数据！");
            if (obj.event == "edit") {
                showEditWin(obj.data.primaryKey, obj);
            }
        });
        let ${pageVar}Table = null;
        form.on("submit(${pageVar}Search)", function (obj) {
            ${pageVar}Table.reload({
                where: {
                    param: {
                        //roleName: obj.field.roleName,
                    }
                }
            });
            return false;
        });
        function del${pageVar}(datas) {
            let ids = [];
            try {
                datas.forEach(function (data, index) {
                    ids.push(data.primaryKey);
                });
            } catch (e) {
                return;
            }
            let load = layer.load(0, {shade: false});
            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: "/${actionName}/delete",
                traditional: true, //指定序列化
                data: JSON.stringify(ids),
                success: function (result) {
                    layer.close(load);
                    if (result.status.code === 1) {
                        layer.msg('删除成功!,共删除' + result.status.data + "条数据！", {icon: 1, time: 3000});
                        ${pageVar}Table.reload();

                    } else {
                        layer.msg(result.message, {icon: 2, time: 3000});
                    }
                }
            });
        }

        function tableLoad() {
            ${pageVar}Table = table.render({
                elem: '#${pageVar}Table'
                , url: '/${actionName}/data' //数据接口
                , height: pageH - 60 //查询区域高度及边距
                , parseData: function (res) { //res 即为原始返回的数据
                    let result = res.status;
                    return {
                        "code": result.code, //解析接口状态
                        "msg": result.message, //解析提示文本
                        "count": result.data.totalSize, //解析数据长度
                        "data": result.data.pageDatas //解析数据列表
                    };
                }
                ,response: {
                    statusCode: 1
                }
                , toolbar: '#${pageVar}Bar' //开启工具栏，此处显示默认图标，自定义模板，默认为default,图标形式
                , defaultToolbar: ['filter', 'print', 'exports', {
                    title: '提示' //标题
                    , layEvent: 'LAYTABLE_TIPS' //事件名，用于 toolbar 事件中使用
                    , icon: 'layui-icon-tips' //图标类名
                }]
                , totalRow: false //开启合计行
                , page: true //开启分页
                , cols: [[ //表头
                    {type: 'checkbox', fixed: 'left'}
                    , {field: 'xh', width: 50, title: '序号', fixed: 'left', type: 'numbers'}
                    <#if columns??>
                        <#list columns as column >
                             , {field: '${column.javaProperty}', title: '${column.remarks[0..*4]}', width: 120, fixed: 'left'}
                        </#list>
                    </#if>
                ]]
            });
        }

        function showEditWin(dataId) {
            $.get("/${actionName}/edit", {id: dataId}, function (str) {
                layer.open({
                    type: 1,
                    title: "${module_chinaese}编辑",
                    scrollbar: false,
                    area: ['665px'],
                    content: str, //注意，如果str是object，那么需要字符拼接。
                    success: function (dom, index) {//父页面向子页面传值
                        $("#${pageVar}-win").data("index", index);
                    },
                    end: function () { //窗口关闭
                        let refresh = $("#${pageVar}List").data("refresh");
                        //弹窗关闭后是否刷新窗口
                        if (refresh) {
                            $("#${pageVar}List").data("refresh", false);
                            ${pageVar}Table.reload();
                        }
                    }
                });
            });
        }

        //表格初始
        tableLoad();
    });

</script>