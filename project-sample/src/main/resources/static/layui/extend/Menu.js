layui.define(["element", "laytpl", "jquery", "menuTab"], function (exports) {
    var element = layui.element,
        $ = layui.$,
        laytpl = layui.laytpl,
        menuTab = layui.menuTab;
    layer = layui.layer;

    var Menu = {

        menuSetupOptions: {
            href: "href",
            title: "title",
            icon: "icon",
            target: "target",
            id: "id",
        },

        render: function (options) {
            options.menuList = options.menuList || [];
            options.menuChildOpen = options.menuChildOpen || false;
            this.menuSetupOptions = options.menuSetup || this.menuSetupOptions;
            this.menuPageMode = options.pageMode || 0;
            Menu.renderSingleModule(options.menuList, options.menuChildOpen);
            Menu.listen();
        },

        /**
         * 单模块
         * @param menuList 菜单数据
         * @param menuChildOpen 是否默认展开
         */
        renderSingleModule: function (menuList, menuChildOpen) {
            menuList = menuList || [];
            var leftMenuHtml = '',
                childOpenClass = '',
                leftMenuCheckDefault = 'layui-this';
            var me = this;
            if (menuChildOpen) childOpenClass = ' layui-nav-itemed';
            leftMenuHtml = this.renderLeftMenu(menuList, {childOpenClass: childOpenClass});
            $('.layui-layout-body').addClass('fw-single-module'); //单模块标识
            $('.fw-header-menu').remove();
            $('.fw-menu-left').html(leftMenuHtml);
            element.init();
        },

        /**
         * 渲染一级菜单
         */
        compileMenu: function (menu, isSub) {

            var menuHtml = '<li {{#if( d.menu){ }}  data-menu="{{d.menu}}" {{#}}} class="layui-nav-item menu-li {{d.childOpenClass}} {{d.className}}"' +
                '  {{#if( d.id){ }}  id="{{d.id}}" {{#}}}> ' +
                ' <a {{#if( d.href){ }} fw-href="{{d.href}}" {{#}}} {{#if( d.target){ }}   target="{{d.target}}" {{#}}}  {{#if( d.id){ }}  id="{{d.id}}" {{#}}} href="javascript:;">' +
                '{{#if( d.icon){ }}  <i class="fa {{d.icon}}"></i> {{#}}} <span class="layui-left-nav">{{d.title}}</span></a>  {{# if(d.children){}} {{d.children}} {{#}}} ' +
                '</li>';
            if (isSub) {
                menuHtml = '<dd class="menu-dd {{d.childOpenClass}} {{ d.className }}"> ' +
                    '<a href="javascript:;"  {{#if( d.menu){ }}  data-menu="{{d.menu}}" {{#}}} {{#if( d.id){ }}  id="{{d.id}}" {{#}}} {{#if(( !d.child || !d.child.length ) && d.href){ }} fw-href="{{d.href}}" {{#}}} {{#if( d.target){ }}  target="{{d.target}}" {{#}}}>' +
                    ' {{#if( d.icon){ }}  <i class="fa {{d.icon}}"></i> {{#}}} ' +
                    '<span class="layui-left-nav"> {{d.title}}</span>' +
                    '</a> {{# if(d.children){}} {{d.children}} {{#}}}' +
                    '</dd>'

            }
            return laytpl(menuHtml).render(menu);
        },
        compileMenuContainer: function (menu, isSub) {
            var wrapperHtml = '<ul class="layui-nav layui-nav-tree layui-left-nav-tree {{d.className}}" id="{{d.id}}">{{d.children}}</ul>';
            if (isSub) {
                wrapperHtml = '<dl class="layui-nav-child ">{{d.children}}</dl>';
            }
            if (!menu.children) {
                return "";
            }
            return laytpl(wrapperHtml).render(menu);
        },

        each: function (list, callback) {
            var _list = [];
            for (var i = 0, length = list.length; i < length; i++) {
                _list[i] = callback(i, list[i]);
            }
            return _list;
        },

        renderChildrenMenu: function (menuList, options) {
            var me = this;
            menuList = menuList || [];
            var html = this.each(menuList, function (idx, menu) {
                var tmpMenu = {} || menu;
                if (menu.child && menu.child.length) {
                    tmpMenu.children = me.renderChildrenMenu(menu.child, {childOpenClass: options.childOpenClass || ''});
                }
                tmpMenu.className = "";
                tmpMenu.childOpenClass = options.childOpenClass || '';
                tmpMenu.id = menu[me.menuSetupOptions.id];
                tmpMenu.target = menu[me.menuSetupOptions.target];
                tmpMenu.title = menu[me.menuSetupOptions.title];
                tmpMenu.href = menu[me.menuSetupOptions.href];
                tmpMenu.icon = menu[me.menuSetupOptions.icon];
                tmpMenu.id = menu[me.menuSetupOptions.id];
                return me.compileMenu(tmpMenu, true);
            }).join("");
            return me.compileMenuContainer({children: html}, true)
        },

        renderLeftMenu: function (leftMenus, options) {
            options = options || {};
            var me = this;
            var leftMenusHtml = me.each(leftMenus || [], function (idx, leftMenu) { // 左侧菜单遍历
                var children = me.renderChildrenMenu(leftMenu.child, {childOpenClass: options.childOpenClass});
                var leftMenuHtml = me.compileMenu({
                    href: leftMenu[me.menuSetupOptions.href],
                    target: leftMenu[me.menuSetupOptions.target],
                    childOpenClass: options.childOpenClass,
                    icon: leftMenu[me.menuSetupOptions.icon],
                    title: leftMenu[me.menuSetupOptions.title],
                    id: leftMenu[me.menuSetupOptions.id],
                    children: children,
                    className: '',
                });
                return leftMenuHtml;
            }).join("");

            leftMenusHtml = me.compileMenuContainer({
                id: options.parentMenuId,
                className: options.leftMenuCheckDefault,
                children: leftMenusHtml
            });
            return leftMenusHtml;
        },

        /**
         * 多模块
         * @param menuList 菜单数据
         * @param menuChildOpen 是否默认展开
         */
        renderMultiModule: function (menuList, menuChildOpen) {
            menuList = menuList || [];
            var me = this;
            var headerMenuHtml = '',
                headerMobileMenuHtml = '',
                leftMenuHtml = '',
                leftMenuCheckDefault = 'layui-this',
                childOpenClass = '',
                headerMenuCheckDefault = 'layui-this';

            if (menuChildOpen) childOpenClass = ' layui-nav-itemed';
            var headerMenuHtml = this.each(menuList, function (index, val) { //顶部菜单渲染
                var menu = 'multi_module_' + index;
                var id = menu + "HeaderId";
                var topMenuItemHtml = "";
                topMenuItemHtml = me.compileMenu({
                    className: headerMenuCheckDefault,
                    menu: menu,
                    id: id,
                    title: val.title,
                    href: "",
                    target: "",
                    children: ""
                });
                leftMenuHtml += me.renderLeftMenu(val.child, {
                    parentMenuId: menu,
                    childOpenClass: childOpenClass,
                    leftMenuCheckDefault: leftMenuCheckDefault
                });
                headerMobileMenuHtml += me.compileMenu({
                    id: id,
                    menu: menu,
                    id: id,
                    icon: val.icon,
                    title: val.title,
                }, true);
                headerMenuCheckDefault = "";
                leftMenuCheckDefault = "layui-hide";
                return topMenuItemHtml;
            }).join("");
            $('.layui-layout-body').addClass('fw-multi-module'); //多模块标识
            $('.fw-menu-header-pc').html(headerMenuHtml); //电脑
            $('.fw-menu-left').html(leftMenuHtml);
            $('.fw-menu-header-mobile').html(headerMobileMenuHtml); //手机

            element.init();
        },

        loadPageContent: function (href, divElement) {
            if (null == href || "" == href)
                return;
            $.get(href, function (data, status) {
                $(divElement).empty();
                $(divElement).html(data);
                //location.hash = 'nav=' + $(elem[0]).parent().attr("id");
            });
        },

        /**
         * 监听
         */
        listen: function () {

            /**
             * 菜单模块切换
             */
            $('body').on('click', '[data-menu]', function () {

                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var menuId = $(this).attr('data-menu');
                // header
                $(".fw-header-menu .layui-nav-item .layui-this").removeClass('layui-this');
                $(this).addClass('layui-this');
                // left
                $(".fw-menu-left .layui-nav.layui-nav-tree.layui-this").addClass('layui-hide');
                $(".fw-menu-left .layui-nav.layui-nav-tree.layui-this.layui-hide").removeClass('layui-this');
                $("#" + menuId).removeClass('layui-hide');
                $("#" + menuId).addClass('layui-this');
                layer.close(loading);
            });


            /**
             * 监听菜单点击
             */
            $('body').on('click', '[fw-href]', function () {

                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var menuId = $(this).attr('id'),
                    href = $(this).attr('fw-href'),
                    title = $(this).text(),
                    target = $(this).attr('target');
                var el = $("[fw-href='" + href + "']", ".fw-menu-left");
                layer.close(window.openTips);
                if (Menu.menuPageMode == 0) { //普通方式打开
                    Menu.loadPageContent(href, ".fw-tab");
                } else {
                    menuTab.openMenuTab(menuId, href, title, target);
                }
                if (el.length) {
                    $(el).closest(".layui-nav-tree").find(".layui-this").removeClass("layui-this");
                    $(el).parent().addClass("layui-this");
                }

                layer.close(loading);
            });

            /**
             * 菜单缩放
             */
            $('body').on('click', '.fw-site-mobile', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var isShow = $('.fw-tool [data-side-fold]').attr('data-side-fold');
                if (isShow == 1) { // 缩放
                    $('.fw-tool [data-side-fold]').attr('data-side-fold', 0);
                    $('.fw-tool [data-side-fold]').attr('class', 'fa fa-indent');
                    $('.layui-layout-body').removeClass('fw-all');
                    $('.layui-layout-body').addClass('fw-mini');
                } else { // 正常
                    $('.fw-tool [data-side-fold]').attr('data-side-fold', 1);
                    $('.fw-tool [data-side-fold]').attr('class', 'fa fa-outdent');
                    $('.layui-layout-body').removeClass('fw-mini');
                    $('.layui-layout-body').addClass('fw-all');
                    layer.close(window.openTips);
                }
                element.init();
                layer.close(loading);
            });


            /**
             * 手机端点开模块
             */
            $('body').on('click', '.fw-header-menu.fw-mobile-show dd', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var check = $('.fw-tool [data-side-fold]').attr('data-side-fold');
                if (check === "1") {
                    $('.fw-site-mobile').trigger("click");
                    element.init();
                }
                layer.close(loading);
            });
        },

    };


    exports("menu", Menu);
});
