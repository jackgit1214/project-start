layui.define(["element", "layer", "jquery"], function (exports) {
    var element = layui.element,
        layer = layui.layer,
        $ = layui.$;

    var menuTab = {

        tabOptions: {
            maxTabNum: 20,
            filter: null,
            multiModule: false,
            urlHashLocation: false,
            menuList: [],
            homeInfo: {},
            listenSwitchCallback: function () {

            }
        },

        /**
         * 初始化tab
         * @param options
         */
        render: function (options) {
            this.tabOptions = $.extend({}, this.tabOptions, options);
            if (options.pageMode === 1) {
                menuTab.createTabContainer();
            }
            menuTab.listen();
            menuTab.listenRoll();
            menuTab.listenSwitch();
            menuTab.listenHash();
        },

        createTabContainer: function () {
            let tabFilter = $("[lay-filter='fwTab']");
            if (!tabFilter.hasClass("layui-tab-rollTool layui-tab")) {
                tabFilter.addClass("layui-tab-rollTool layui-tab");
            }

            let tabTitle = "<ul class=\"layui-tab-title\">\n" +
                //"<#--                <li class=\"layui-this\" id=\"fwHomeTabId\" lay-id=\"\"></li>-->\n" +
                "            </ul>"
            let tabControl = "<div class=\"layui-tab-control\">\n" +
                "                <li class=\"fw-tab-roll-left layui-icon layui-icon-left\"></li>\n" +
                "                <li class=\"fw-tab-roll-right layui-icon layui-icon-right\"></li>\n" +
                "                <li class=\"layui-tab-tool layui-icon layui-icon-down\">\n" +
                "                    <ul class=\"layui-nav close-box\">\n" +
                "                        <li class=\"layui-nav-item\">\n" +
                "                            <a href=\"javascript:;\"><span class=\"layui-nav-more\"></span></a>\n" +
                "                            <dl class=\"layui-nav-child\">\n" +
                "                                <dd><a href=\"javascript:;\" fw-tab-close=\"current\">关 闭 当 前</a></dd>\n" +
                "                                <dd><a href=\"javascript:;\" fw-tab-close=\"other\">关 闭 其 他</a></dd>\n" +
                "                                <dd><a href=\"javascript:;\" fw-tab-close=\"all\">关 闭 全 部</a></dd>\n" +
                "                            </dl>\n" +
                "                        </li>\n" +
                "                    </ul>\n" +
                "                </li>\n" +
                "            </div>";
            let tabContent = "<div class=\"layui-tab-content\">\n" +
                // "<#--                <div id=\"fwHomeTabIframe\" class=\"layui-tab-item layui-show\"></div>-->\n" +
                "            </div>";
            tabFilter.append(tabTitle);
            tabFilter.append(tabControl);
            tabFilter.append(tabContent);
        },

        /**
         * 创建TAB
         * @param menuId 菜单或模块ID
         * @param href 后台响应连接
         * @param title 标签名称
         * @returns {boolean}
         */
        create: function (menuId, href, title) {
            let tabOptions = {
                tabId: menuId || null,
                href: href || null,
                title: title || null,
            };
            if ($(".fw-tab .layui-tab-title li").length >= this.tabOptions.maxTabNum) {
                layer.msg('Tab窗口已达到限定数量，请先关闭部分Tab');
                return false;
            }
            var ele = element;
            if (null == href || "" == href) {
                return false;
            }
            $.get(href, function (data, status) {
                ele.tabAdd('fwTab', {
                    title: '<span class="fw-tab-active"></span><span>' + tabOptions.title + '</span><i class="layui-icon layui-unselect layui-tab-close">ဆ</i>' //用于演示
                    // , content: '<iframe width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0"   src="' + tabOptions.href + '"></iframe>'
                    , content: data
                    , id: tabOptions.tabId
                });
                $('.fw-menu-left').attr('fw-tab-tag', 'add');

                //location.hash = 'nav=' + $(elem[0]).parent().attr("id");
            }).done(function () {
                element.tabChange('fwTab', menuId);
            }).fail(function () {
                ele.tabAdd('fwTab', {
                    title: '<span class="fw-tab-active"></span><span>' + tabOptions.title + '</span><i class="layui-icon layui-unselect layui-tab-close">ဆ</i>' //用于演示
                    , content: "页面加载失败..........."
                    , id: tabOptions.tabId
                });
                element.tabChange('fwTab', menuId);
            });
            sessionStorage.setItem('fwmenu_' + tabOptions.tabId, tabOptions.title);
        },

        /**
         * 切换选项卡，供外部调用
         * @param tabId
         */
        change: function (tabId) {
            element.tabChange('fwTab', tabId);
        },

        /**
         * 删除tab窗口
         * @param tabId
         */
        delete: function (tabId) {
            // todo 未知BUG，不知道是不是layui问题，必须先删除元素
            $(".fw-tab .layui-tab-title .layui-unselect .layui-tab-bar").remove();
            element.tabDelete('fwTab', tabId);
        },

        /**
         * 判断tab窗口
         */
        check: function (tabId) {
            // 根据ID,判断选项卡上是否有已经打开的TAB
            var checkTab = false;
            $(".layui-tab-title li").each(function () {
                var checkTabId = $(this).attr('lay-id');
                if (checkTabId != null && checkTabId === tabId) {
                    checkTab = true;
                }
            });
            return checkTab;
        },

        /**
         * 开启tab右键菜单
         * @param tabId
         * @param left
         */
        openTabRignMenu: function (tabId, left) {
            menuTab.closeTabRignMenu();
            var menuHtml = '<div class="layui-unselect layui-form-select layui-form-selected fw-tab-mousedown layui-show" data-tab-id="' + tabId + '" style="left: ' + left + 'px!important">\n' +
                '<dl>\n' +
                '<dd><a href="javascript:;" fw-tab-menu-close="current">关 闭 当 前</a></dd>\n' +
                '<dd><a href="javascript:;" fw-tab-menu-close="other">关 闭 其 他</a></dd>\n' +
                '<dd><a href="javascript:;" fw-tab-menu-close="all">关 闭 全 部</a></dd>\n' +
                '</dl>\n' +
                '</div>';
            var makeHtml = '<div class="fw-tab-make"></div>';
            $('.fw-tab .layui-tab-title').after(menuHtml);
            $('.fw-tab .layui-tab-content').after(makeHtml);
        },

        /**
         * 关闭tab右键菜单
         */
        closeTabRignMenu: function () {
            $('.fw-tab-mousedown').remove();
            $('.fw-tab-make').remove();
        },

        /**
         * 查询菜单信息
         * @param href
         * @param menuList
         */
        searchMenu: function (href, menuList) {
            var menu;
            for (key in menuList) {
                var item = menuList[key];
                if (item.href === href) {
                    menu = item;
                    break;
                }
                if (item.child) {
                    newMenu = menuTab.searchMenu(href, item.child);
                    if (newMenu) {
                        menu = newMenu;
                        break;
                    }
                }
            }
            return menu;
        },

        /**
         * 根据参数，供外部调用创建TAB
         * @param menuId
         * @param href
         * @param title
         * @param target
         */
        openMenuTab: function (menuId, href, title, target) {
            if (menuId === null || menuId === undefined) menuId = new Date().getTime();
            var checkTab = menuTab.check(menuId);
            if (!checkTab) {
                menuTab.create(menuId, href, title, true);
            }
        },

        /**
         * 监听
         * @param options
         */
        listen: function () {
            /**
             * 在iframe子菜单上打开新窗口
             */
            $('body').on('click', '[fw-content-href]', function () {
                var loading = parent.layer.load(0, {shade: false, time: 2 * 1000});
                var tabId = $(this).attr('fw-content-href'),
                    href = $(this).attr('fw-content-href'),
                    title = $(this).attr('data-title'),
                    target = $(this).attr('target');
                if (target === '_blank') {
                    parent.layer.close(loading);
                    window.open(href, "_blank");
                    return false;
                }
                if (tabId === null || tabId === undefined) tabId = new Date().getTime();
                var checkTab = menuTab.check(tabId, true);
                if (!checkTab) {
                    menuTab.create(tabId, href, title, true);
                }
                parent.layui.element.tabChange('fwTab', tabId);
                parent.layer.close(loading);
            });

            /**
             * 关闭选项卡
             **/
            $('body').on('click', '.fw-tab .layui-tab-title .layui-tab-close', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var $parent = $(this).parent();
                var tabId = $parent.attr('lay-id');
                if (tabId !== undefined || tabId !== null) {
                    menuTab.delete(tabId);
                }
                layer.close(loading);
            });

            /**
             * 选项卡操作
             */
            $('body').on('click', '[fw-tab-close]', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var closeType = $(this).attr('fw-tab-close');
                $(".fw-tab .layui-tab-title li").each(function () {
                    var tabId = $(this).attr('lay-id');
                    var id = $(this).attr('id');
                    var isCurrent = $(this).hasClass('layui-this');
                    if (id !== 'fwHomeTabId') { //如果不是首页面
                        if (closeType === 'all') {
                            menuTab.delete(tabId);
                        } else {
                            if (closeType === 'current' && isCurrent) {
                                menuTab.delete(tabId);
                            } else if (closeType === 'other' && !isCurrent) {
                                menuTab.delete(tabId);
                            }
                        }
                    }
                });
                layer.close(loading);
            });

            /**
             * 禁用网页右键
             */
            $(".fw-tab .layui-tab-title").unbind("mousedown").bind("contextmenu", function (e) {
                e.preventDefault();
                return false;
            });

            /**
             * 注册鼠标右键
             */
            $('body').on('mousedown', '.fw-tab .layui-tab-title li', function (e) {
                var left = $(this).offset().left - $('.fw-tab ').offset().left + ($(this).width() / 2),
                    tabId = $(this).attr('lay-id');
                if (e.which === 3) {
                    menuTab.openTabRignMenu(tabId, left);
                }
            });

            /**
             * 关闭tab右键菜单
             */
            $('body').on('click', '.layui-body,.layui-header,.fw-menu-left,.fw-tab-make', function () {
                menuTab.closeTabRignMenu();
            });

            /**
             * tab右键选项卡操作
             */
            $('body').on('click', '[fw-tab-menu-close]', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var closeType = $(this).attr('fw-tab-menu-close'),
                    currentTabId = $('.fw-tab-mousedown').attr('data-tab-id');
                $(".fw-tab .layui-tab-title li").each(function () {
                    var tabId = $(this).attr('lay-id');
                    var id = $(this).attr('id');
                    if (id !== 'fwHomeTabId') {
                        if (closeType === 'all') {
                            menuTab.delete(tabId);
                        } else {
                            if (closeType === 'current' && currentTabId === tabId) {
                                menuTab.delete(tabId);
                            } else if (closeType === 'other' && currentTabId !== tabId) {
                                menuTab.delete(tabId);
                            }
                        }
                    }
                });
                menuTab.closeTabRignMenu();
                layer.close(loading);
            });
        },

        /**
         * 监听tab切换
         * @param options
         */
        listenSwitch: function () {
            var options = menuTab.tabOptions;
            element.on('tab(' + options.filter + ')', function (data) {
                var tabId = $(this).attr('lay-id');
                if (options.urlHashLocation) {
                    location.hash = '/' + tabId;
                }
                if (typeof options.listenSwitchCallback === 'function') {
                    options.listenSwitchCallback();
                }
                // 判断是否为新增窗口
                if ($('.fw-menu-left').attr('fw-tab-tag') === 'add') {
                    $('.fw-menu-left').attr('fw-tab-tag', 'no')
                } else {
                    $("[fw-href]").parent().removeClass('layui-this');
                    if (options.multiModule) {
                        menuTab.listenSwitchMultiModule(tabId);
                    } else {
                        menuTab.listenSwitchSingleModule(tabId);
                    }
                }
                menuTab.rollPosition();
            });
        },

        /**
         * 监听hash变化
         * @param options
         * @returns {boolean}
         */
        listenHash: function () {
            var options = menuTab.tabOptions;

            if (!options.urlHashLocation) return false;
            var tabId = location.hash.replace(/^#\//, '');
            if (tabId === null || tabId === undefined || tabId === '') return false;

            // 判断是否为首页
            if (tabId === options.homeInfo.href) return false;

            // 判断是否为右侧菜单
            var menu = menuTab.searchMenu(tabId, options.menuList);
            if (menu !== undefined) {
                menuTab.create(tabId, tabId, menu.title, false);
                $('.fw-menu-left').attr('fw-tab-tag', 'no');
                element.tabChange('fwTab', tabId);
                return false;
            }

            // 判断是否为快捷菜单
            var isSearchMenu = false;
            $("[fw-content-href]").each(function () {

                if ($(this).attr("fw-content-href") === tabId) {
                    var title = $(this).attr("data-title");
                    menuTab.create({
                        tabId: tabId,
                        href: tabId,
                        title: title,
                        isIframe: false,
                        maxTabNum: options.maxTabNum,
                    });
                    $('.fw-menu-left').attr('fw-tab-tag', 'no');
                    element.tabChange('fwTab', tabId);
                    isSearchMenu = true;
                    return false;
                }
            });
            if (isSearchMenu) return false;

            // 既不是右侧菜单、快捷菜单,就直接打开
            var title = sessionStorage.getItem('fwmenu_' + tabId) === null ? tabId : sessionStorage.getItem('fwmenu_' + tabId);
            menuTab.create({
                tabId: tabId,
                href: tabId,
                title: title,
                isIframe: false,
                maxTabNum: options.maxTabNum,
            });
            element.tabChange('fwTab', tabId);
            return false;
        },

        /**
         * 监听滚动
         */
        listenRoll: function () {
            $(".fw-tab-roll-left").click(function () {
                menuTab.rollClick("left");
            });
            $(".fw-tab-roll-right").click(function () {
                menuTab.rollClick("right");
            });
        },

        /**
         * 单模块切换
         * @param tabId
         */
        listenSwitchSingleModule: function (tabId) {
            $("[fw-href]").each(function () {
                if ($(this).attr("fw-href") === tabId) {
                    // 自动展开菜单栏
                    var addMenuClass = function ($element, type) {
                        if (type === 1) {
                            $element.addClass('layui-this');
                            if ($element.hasClass('layui-nav-item') && $element.hasClass('layui-this')) {
                                $(".fw-header-menu li").attr('class', 'layui-nav-item');
                            } else {
                                addMenuClass($element.parent().parent(), 2);
                            }
                        } else {
                            $element.addClass('layui-nav-itemed');
                            if ($element.hasClass('layui-nav-item') && $element.hasClass('layui-nav-itemed')) {
                                $(".fw-header-menu li").attr('class', 'layui-nav-item');
                            } else {
                                addMenuClass($element.parent().parent(), 2);
                            }
                        }
                    };
                    addMenuClass($(this).parent(), 1);
                    return false;
                }
            });
        },

        /**
         * 多模块切换
         * @param tabId
         */
        listenSwitchMultiModule: function (tabId) {
            $("[fw-href]").each(function () {
                if ($(this).attr("fw-href") === tabId) {

                    // 自动展开菜单栏
                    var addMenuClass = function ($element, type) {
                        if (type === 1) {
                            $element.addClass('layui-this');
                            if ($element.hasClass('layui-nav-item') && $element.hasClass('layui-this')) {
                                var moduleId = $element.parent().attr('id');
                                $(".fw-header-menu li").attr('class', 'layui-nav-item');
                                $("#" + moduleId + "HeaderId").addClass("layui-this");
                                $(".fw-menu-left .layui-nav.layui-nav-tree").attr('class', 'layui-nav layui-nav-tree layui-hide');
                                $("#" + moduleId).attr('class', 'layui-nav layui-nav-tree layui-this');
                            } else {
                                addMenuClass($element.parent().parent(), 2);
                            }
                        } else {
                            $element.addClass('layui-nav-itemed');
                            if ($element.hasClass('layui-nav-item') && $element.hasClass('layui-nav-itemed')) {
                                var moduleId = $element.parent().attr('id');
                                $(".fw-header-menu li").attr('class', 'layui-nav-item');
                                $("#" + moduleId + "HeaderId").addClass("layui-this");
                                $(".fw-menu-left .layui-nav.layui-nav-tree").attr('class', 'layui-nav layui-nav-tree layui-hide');
                                $("#" + moduleId).attr('class', 'layui-nav layui-nav-tree layui-this');
                            } else {
                                addMenuClass($element.parent().parent(), 2);
                            }
                        }
                    };
                    addMenuClass($(this).parent(), 1);
                    return false;
                }
            });
        },

        /**
         * 自动定位
         */
        rollPosition: function () {
            var $tabTitle = $('.fw-tab  .layui-tab-title');
            var autoLeft = 0;
            $tabTitle.children("li").each(function () {
                if ($(this).hasClass('layui-this')) {
                    return false;
                } else {
                    autoLeft += $(this).outerWidth();
                }
            });
            $tabTitle.animate({
                scrollLeft: autoLeft - $tabTitle.width() / 3
            }, 200);
        },

        /**
         * 点击滚动
         * @param direction
         */
        rollClick: function (direction) {
            var $tabTitle = $('.fw-tab  .layui-tab-title');
            var left = $tabTitle.scrollLeft();
            if ('left' === direction) {
                $tabTitle.animate({
                    scrollLeft: left - 450
                }, 200);
            } else {
                $tabTitle.animate({
                    scrollLeft: left + 450
                }, 200);
            }
        }

    };

    exports("menuTab", menuTab);
});
