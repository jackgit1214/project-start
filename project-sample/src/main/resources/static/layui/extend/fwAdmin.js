/**
 * 框架基础
 */


layui.define(["jquery", "element", "theme", "menu", "menuTab"], function (exports) {
    var $ = layui.$,
        layer = layui.layer,
        theme = layui.theme,
        menu = layui.menu,
        menuTab = layui.menuTab,
        element = layui.element;
    if (!/http(s*):\/\//.test(location.href)) {
        var tips = "请先将项目部署至web容器（Apache/Tomcat/Nginx/IIS/等），否则部分数据将无法显示";
        return layer.alert(tips);
    }
    const header = $("meta[name='_csrf_header']").attr("content");
    const token = $("meta[name='_csrf']").attr("content");
    //配置全局错误
    $.ajaxSetup({
        error:function(xhr, thrownError,errorObject,) {
            let last_layer_index = layer.index;
            let errorInfo = xhr.responseJSON;
            layer.open({
                type: 1
                ,title: "出错了！！！"
                ,closeBtn: false
                ,area: ['500px', '350px']
                ,shade: 0.8
                ,id: 'error_notice9898' //设定一个id，防止重复弹出
                ,btn: ['关闭']
                ,btnAlign: 'c'
                ,moveType: 1 //拖拽模式，0或者1
                ,content: '<div style="padding: 10px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;overflow: auto">' +
                    '出错了，错误码为：' +errorInfo.status+"错误信息如下："+
                    '<br>时间：' +errorInfo.timestamp +'<br>状态：' +errorInfo.status+'<br>错误内容：' +errorInfo.error   +errorInfo.trace   +
                    '<br>路径：' +errorInfo.path + '<br>数据：' +errorInfo.errorData+
                '<br></div>'
                ,success: function(layero){
                    layer.close(last_layer_index);
                }
            });
        },
        //配置全局请求头
        beforeSend: function (xhr) {
            if (header!="" && token!=""){
                xhr.setRequestHeader(header, token);
            }
        },
    });
    var fwAdmin = {

        projectInfo: {
            "projectName": "XXX管理系统 ",
            "defaultTheme": false,
            "loadingTime": 0,// 初始化加载时间
            "logoInfo": {
                "image": "images/favicon.png",
                "href": ""
            },
            "menuHref": "/userMenus",
            "urlHashLocation": false,      // 是否打开hash定位
            "bgColorDefault": false,      // 主题默认配置
            "menuChildOpen": false,       // 是否默认展开菜单
            "pageMode": 0,                // 页面是以标签还是普通页面，0普通 ，1为标签。
            "maxTabNum": 20,              // 标签页打开时，最大的tab打开数量
            "pageAnim": true,             // iframe窗口动画
            "editPageMode": 0              // 打开页面模式，弹窗还是本页面 0为弹窗
        },

        render: function () {
            let options = fwAdmin.projectInfo;
            theme.render({
                bgColorDefault: options.bgColorDefault,
                listen: true,
            });
            fwAdmin.renderLogo(options);
            fwAdmin.renderClear("");
            fwAdmin.renderUserInfo();
            $.getJSON(options.menuHref, function (data) {
                menu.render({
                    menuList: data,
                    menuChildOpen: false,
                    pageMode: options.pageMode,
                    menuSetup: {
                        href: "urlLink",
                        title: "funName",
                        icon: "funIcon",
                        target: "targetDiv",
                        id: "funId"
                    }
                });
                menuTab.render({
                    filter: 'fwTab',
                    urlHashLocation: options.urlHashLocation,
                    multiModule: options.multiModule,
                    menuChildOpen: options.menuChildOpen,
                    maxTabNum: options.maxTabNum,
                    menuList: data,
                    pageMode: options.pageMode,
                    homeInfo: data.homeInfo,
                    listenSwitchCallback: function () {
                        fwAdmin.renderDevice();
                    }
                });

            }).fail(function () {
                fwAdmin.error('菜单初始化错误');
            });
            fwAdmin.listen();
            fwAdmin.deleteLoader(fwAdmin.projectInfo.loadingTime);

        },

        renderLogo: function (data) {
            let logoInfo = data.logoInfo;
            var html = '<a href="' + logoInfo.href + '"><img src="' + logoInfo.image + '" alt="logo"><h1>' + data.projectName + '</h1></a>';
            $('.fw-logo').html(html);
        },

        renderUserInfo: function () {
            $.getJSON("/userInfo", function (data) {
                fwAdmin.UserInfo = data;
                $("#userAvatar").attr("src", data.avatarPic);
                $("#userAvatar").attr("alt", data.name);
            });

        },
        renderClear: function (clearUrl) {
            $('.fw-clear').attr('data-href', clearUrl);
        },

        pageAreaHeight: function () {
            let tmpH = $('.fw-tab').innerHeight();
            if (fwAdmin.projectInfo.pageMode === 1)
                tmpH = tmpH - 35; //去除TAB选项的高度
            return tmpH;
        },

        fullScreen: function () {
            var el = document.documentElement;
            var rfs = el.requestFullScreen || el.webkitRequestFullScreen;
            if (typeof rfs != "undefined" && rfs) {
                rfs.call(el);
            } else if (typeof window.ActiveXObject != "undefined") {
                var wscript = new ActiveXObject("WScript.Shell");
                if (wscript != null) {
                    wscript.SendKeys("{F11}");
                }
            } else if (el.msRequestFullscreen) {
                el.msRequestFullscreen();
            } else if (el.oRequestFullscreen) {
                el.oRequestFullscreen();
            } else if (el.webkitRequestFullscreen) {
                el.webkitRequestFullscreen();
            } else if (el.mozRequestFullScreen) {
                el.mozRequestFullScreen();
            } else {
                fwAdmin.error('浏览器不支持全屏调用！');
            }
        },

        /**
         * 退出全屏
         */
        exitFullScreen: function () {
            var el = document;
            var cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.exitFullScreen;
            if (typeof cfs != "undefined" && cfs) {
                cfs.call(el);
            } else if (typeof window.ActiveXObject != "undefined") {
                var wscript = new ActiveXObject("WScript.Shell");
                if (wscript != null) {
                    wscript.SendKeys("{F11}");
                }
            } else if (el.msExitFullscreen) {
                el.msExitFullscreen();
            } else if (el.oRequestFullscreen) {
                el.oCancelFullScreen();
            } else if (el.mozCancelFullScreen) {
                el.mozCancelFullScreen();
            } else if (el.webkitCancelFullScreen) {
                el.webkitCancelFullScreen();
            } else {
                fwAdmin.error('浏览器不支持全屏调用！');
            }
        },

        /**
         * 初始化设备端
         */
        renderDevice: function () {
            if (fwAdmin.checkMobile()) {
                $('.fw-tool i').attr('data-side-fold', 1);
                $('.fw-tool i').attr('class', 'fa fa-outdent');
                $('.layui-layout-body').removeClass('fw-mini');
                $('.layui-layout-body').addClass('fw-all');
            }
        },


        /**
         * 初始化加载时间
         * @param loadingTime
         */
        deleteLoader: function (loadingTime) {
            setTimeout(function () {
                $('.fw-loader').fadeOut();
            }, loadingTime * 1000)
        },

        /**
         * 成功
         * @param title
         * @returns {*}
         */
        success: function (title) {
            return layer.msg(title, {icon: 1, shade: this.shade, scrollbar: false, time: 2000, shadeClose: true});
        },

        /**
         * 失败
         * @param title
         * @returns {*}
         */
        error: function (title) {
            return layer.msg(title, {icon: 2, shade: this.shade, scrollbar: false, time: 3000, shadeClose: true});
        },

        /**
         * 判断是否为手机
         * @returns {boolean}
         */
        checkMobile: function () {
            var ua = navigator.userAgent.toLocaleLowerCase();
            var pf = navigator.platform.toLocaleLowerCase();
            var isAndroid = (/android/i).test(ua) || ((/iPhone|iPod|iPad/i).test(ua) && (/linux/i).test(pf))
                || (/ucweb.*linux/i.test(ua));
            var isIOS = (/iPhone|iPod|iPad/i).test(ua) && !isAndroid;
            var isWinPhone = (/Windows Phone|ZuneWP7/i).test(ua);
            var clientWidth = document.documentElement.clientWidth;
            if (!isAndroid && !isIOS && !isWinPhone && clientWidth > 1024) {
                return false;
            } else {
                return true;
            }
        },

        clearCache: function (clearUrl) {
            var loading = layer.load(0, {shade: false, time: 2 * 1000});
            sessionStorage.clear();
            // 判断是否清理服务端
            if (clearUrl != undefined && clearUrl != '' && clearUrl != null) {
                $.getJSON(clearUrl, function (data, status) {
                    layer.close(loading);
                    if (data.code != 1) {
                        return fwAdmin.error(data.msg);
                    } else {
                        return fwAdmin.success(data.msg);
                    }
                }).fail(function () {
                    layer.close(loading);
                    return fwAdmin.error('清理缓存接口有误');
                });
            } else {
                layer.close(loading);
                return fwAdmin.success('清除缓存成功');
            }
        },


        /**
         * 监听
         */
        listen: function () {

            /**
             * test
             */
            $('body').on('click', '[data-test]', function () {
                //window.location.href="/testExce";
                $.getJSON("/error/bsExceHtml", function (data) {
                     //console.log(data);
                    fwAdmin.UserInfo = data;
                    $("#userAvatar").attr("src", data.avatarPic);
                    $("#userAvatar").attr("alt", data.name);
                });
            });


            /**
             * 清理
             */
            $('body').on('click', '[data-clear]', function () {
                var clearUrl = $('.fw-clear').attr('data-href');
                fwAdmin.clearCache(clearUrl);
            });

            /**
             * 刷新
             */
            $('body').on('click', '[page-refresh]', function () {
                // $(".layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload();
                fwAdmin.success('刷新成功');
            });

            $('body').on('click', '[fw-slide-href][userPreview]', function () {
                let loading = parent.layer.load(0, {shade: false, time: 2 * 1000});
                let href = $(this).attr('fw-slide-href'),
                    title = $(this).attr('data-title');
                let X = $(this).offset().top;  //获取当前元素x坐标
                let clientWidth = document.documentElement.clientWidth;
                $.get(href, function (data) {
                    layer.open({
                        type: 1,
                        title: title,
                        closeBtn: 0,
                        shade: 0.2,
                        anim: 2,
                        shadeClose: true,
                        id: 'userPreview-win',
                        area: ['340px', '520px'],
                        offset: [X, clientWidth - 340],
                        content: data,
                        success: function (index, layero) {
                            $("#userPreviewWin").data("userData", fwAdmin.UserInfo);
                        },
                        end: function () {

                        }
                    });
                });
                parent.layer.close(loading);
            });

            $('body').on('click', '[fw-slide-href][editPassword]', function () {
                let loading = parent.layer.load(0, {shade: false, time: 2 * 1000});
                let href = $(this).attr('fw-slide-href'),
                    title = $(this).attr('data-title');
                let X = $(this).offset().top;  //获取当前元素x坐标
                let clientWidth = document.documentElement.clientWidth;
                $.get(href, function (data) {
                    layer.open({
                        type: 1,
                        title: title,
                        id: 'pwd-Win',
                        area: ['340px', '260px'],
                        content: data,
                        success: function (index, layero) {
                            console.log(index);
                            console.log(layero);
                            $("#pwdWin").data("index", layero);
                        },
                        end: function () {

                        }
                    });
                });
                parent.layer.close(loading);
            });

            /**
             * 菜单缩放
             */
            $('body').on('click', '[data-side-fold]', function () {
                var loading = layer.load(0, {shade: false, time: 2 * 1000});
                var isShow = $('.fw-tool [data-side-fold]').attr('data-side-fold');
                if (isShow == 1) { // 缩放
                    $('.fw-tool [data-side-fold]').attr('data-side-fold', 0);
                    $('.fw-tool [data-side-fold]').attr('class', 'fa fa-indent');
                    $('.layui-layout-body').removeClass('fw-all');
                    $('.layui-layout-body').addClass('fw-mini');
                    // $(".menu-li").each(function (idx,el) {
                    //     $(el).addClass("hidden-sub-menu");
                    // });
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
             * 全屏
             */
            $('body').on('click', '[data-check-screen]', function () {
                var check = $(this).attr('data-check-screen');
                if (check == 'full') {
                    fwAdmin.fullScreen();
                    $(this).attr('data-check-screen', 'exit');
                    $(this).html('<i class="fa fa-compress"></i>');
                } else {
                    fwAdmin.exitFullScreen();
                    $(this).attr('data-check-screen', 'full');
                    $(this).html('<i class="fa fa-arrows-alt"></i>');
                }
            });

            $('body').on("click", '.login-out', function () {
                let header = $("meta[name='_csrf_header']").attr("content");
                let token = $("meta[name='_csrf']").attr("content");
                $.ajax({
                    type: "POST",
                    url: "/logout",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (result) {
                        window.location = '/login';
                    },
                    error: function (e) {
                        layer.msg('网络错误', {icon: 2});
                    }
                });
                // layer.msg('退出登录成功', function () {
                //     window.location = '/login';
                // });
            });


            /**
             * 监听提示信息
             */
            $("body").on("mouseenter", ".layui-nav-tree .menu-li", function () {
                if (fwAdmin.checkMobile()) {
                    return false;
                }
                var classInfo = $(this).attr('class'),
                    tips = $(this).prop("innerHTML"),
                    isShow = $('.fw-tool i').attr('data-side-fold');

                if (isShow == 0 && tips) {
                    tips = "<ul class='fw-menu-left-zoom layui-nav layui-nav-tree layui-this'><li class='layui-nav-item layui-nav-itemed'>" + tips + "</li></ul>";
                    window.openTips = layer.tips(tips, $(this), {
                        tips: [2, '#2f4056'],
                        time: 300000,
                        skin: "popup-tips",
                        success: function (el) {
                            var left = $(el).position().left - 10;
                            $(el).css({left: left});
                            element.render();
                        }
                    });
                }
            });

            $("body").on("mouseleave", ".popup-tips", function () {
                if (fwAdmin.checkMobile()) {
                    return false;
                }
                var isShow = $('.fw-tool i').attr('data-side-fold');
                if (isShow == 0) {
                    try {
                        layer.close(window.openTips);
                    } catch (e) {
                        console.log(e.message);
                    }
                }
            });

            /**
             * 点击遮罩层
             */
            $('body').on('click', '.fw-make', function () {
                fwAdmin.renderDevice();
            });
        }
    };


    exports("fwAdmin", fwAdmin);
});
