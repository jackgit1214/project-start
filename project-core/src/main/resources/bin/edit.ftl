<div style="padding: 10px;" id="${pageVar}-win" data-index="" data-edit="false">
    <form class="layui-form" action="" lay-filter="${pageVar}">
        <#if keyColumns??>
            <#list keyColumns as column >
                <input type="hidden" name="${column.javaProperty}"  value="${'$'}{data.${column.javaProperty}!''}"/>
            </#list>
        </#if>
        <#assign columnCount = 2>
        <#if columns.size() % columnCount == 0>
            <#assign rowCount = ( columns.size() / columnCount) - 1 >
        <#else>
            <#assign rowCount = ( columns.size() / columnCount) >
        </#if>
        <#if columns??>
            <#list columns as column >

                <#if column?is_odd_item>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">${column.remarks[0..*4]}：</label>
                            <div class="layui-input-inline">
                                <#if  (column.jdbcCharacterColumn)>
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{data.${column.javaProperty}!''}" />
                                <#elseif (column.JDBCDateColumn) >
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('yyyy-MM-dd'))!''}" />
                                <#elseif (column.JDBCTimeColumn) >
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('yyyy-MM-dd'))!''}" />
                                <#else>
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('#'))!}" />
                                </#if>
                            </div>
                        </div>
                    <#if column?is_last></div></#if>
                <#else>
                        <div class="layui-inline">
                            <label class="layui-form-label">${column.remarks[0..*4]}：</label>
                            <div class="layui-input-inline">
                                <#if  (column.jdbcCharacterColumn)>
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{data.${column.javaProperty}!''}" />
                                <#elseif (column.JDBCDateColumn) >
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('yyyy-MM-dd'))!''}" />
                                <#elseif (column.JDBCTimeColumn) >
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('yyyy-MM-dd'))!''}" />
                                <#else>
                                    <input type="text" name="${column.javaProperty}" lay-verify="required" autocomplete="off" placeholder="请输入${column.remarks}"
                                           class="layui-input" value="${'$'}{(data.${column.javaProperty}?string('#'))!}" />
                                </#if>
                            </div>
                        </div>
                    </div>
                </#if>

            </#list>
        </#if>

        <div class="layui-btn-container" style="text-align: center;margin-bottom: 0px">
            <div class="layui-inline">
                <button class="layui-btn" lay-submit="" lay-filter="add${pageVar}">保存</button>
                <button class="layui-btn" type="reset">重置</button>
                <button class="layui-btn" id="${pageVar}WinClose">关闭</button>
            </div>
        </div>
    </form>

</div>

<script>
    layui.use(['form', 'layer'], function () {
        let form = layui.form,
            layer = layui.layer;

        //自定义验证规则
        form.verify({

        });

        $(".layui-input").on("input", function (ele) {
            $("#${pageVar}-win").data("edit", true);
        })

        //监听提交
        form.on('submit(add${pageVar})', function (data) {
            let load = layer.load(0, {shade: false});
            $.ajax({
                type: 'POST',
                contentType: 'application/x-www-form-urlencoded',
                url: "/${actionName}/save",
                data: data.field,
                success: function (result) {
                    layer.close(load);
                    $("#${pageVar}List").data("refresh", true);
                    let winIndex = $("#${pageVar}-win").data("index");
                    if (result.status.code === 1) {
                        $("#${pageVar}-win").data("edit", false);
                        layer.close(winIndex);//关闭当前页
                        layer.msg('保存成功', {icon: 1, time: 3000});
                        return false;
                    } else {
                        layer.msg(result.status.message, {icon: 2, time: 3000});
                    }
                    return false;
                }
            });
            return false;
        });

        $("#${pageVar}WinClose").click(function () {
            let winIndex = $("#${pageVar}-win").data("index");
            let editStatus = $("#${pageVar}-win").data("edit");
            if (editStatus === false) {
                layer.close(winIndex);//关闭当前页
                return false;
            } else {
                layer.confirm('数据已经修改，是否确认关闭?', {icon: 3, title: '提示'}, function (index) {
                    layer.close(index);
                    layer.close(winIndex);//关闭当前页
                });
                return false;
            }
        });
    });

</script>