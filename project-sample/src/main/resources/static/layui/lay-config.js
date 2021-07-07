/**
 * 配置所有layui扩展模块
 */


/**
 * 取得当前根路径
 * @type {string}
 */
window.rootPath = (function (src) {
    src = document.scripts[document.scripts.length - 1].src;
    return src.substring(0, src.lastIndexOf("/") + 1);
})();

layui.config({
    base: window.rootPath + "extend/",
    version: true
}).extend({
    fwAdmin: "fwAdmin",
    theme: "Theme",
    menu: "Menu",
    menuTab: "menuTab",
    dtree: 'dtree/dtree', //树形控件
    step: 'step-lay/step', // 分步表单扩展
    treeTable: 'treetable-lay/treeTable', //table树形扩展
    tableSelect: 'tableSelect/tableSelect', // table选择扩展
    iconPicker: 'iconPicker/iconPicker', // 图标选择扩展
    echarts: 'echarts/echarts', // echarts图表扩展
    echartsTheme: 'echarts/echartsTheme', // echarts图表主题扩展
});
