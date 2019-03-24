/**
 * User: 王超
 * Date: 2015-4-24
 * 珠海PC业务常用接口(udb相关)
 * 1.YPCSDK.YYLogin(url) 调用udb登录
 * 2.YPCSDK.YYLogOut(url) 调用udb退出登录
 */
$.extend(ypcSdk.prototype, {
    /**
     * 快速登录框
     * @method  YYLogin
     * @param {string} url 登录后调回的url
     */
    YYLogin: function (url) {
        var _this = this;
        if (typeof UDBSDKProxy != 'undefined') {
            UDBSDKProxy.ready(function(){
                UDBSDKProxy.openByFixProxy(url || '');
            });
        } else {
            _this.loadJs('http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/proxy/udb.sdk.pcweb.proxy.min.js', function () {
                UDBSDKProxy.ready(function(){
                    UDBSDKProxy.openByFixProxy(url || '');
                });
            });
        }
    },
    /**
     * 快速登录框退出登录
     * @method  YYLogOut
     * @param {string} url 登出回调的url
     */
    YYLogOut: function (url) {
        var _this = this;
        if (typeof UDBSDKProxy != 'undefined') {
            UDBSDKProxy.deleteCookieByFixProxyWithCallBack(url||"");
        } else {
            _this.loadJs('http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/proxy/udb.sdk.pcweb.proxy.min.js', function () {
                UDBSDKProxy.deleteCookieByFixProxyWithCallBack(url||"");
            });
        }
    },
    /**
     * 获取用户信息
     * @param callback
     */
    getUserInfo: function(callback){
        var _this = this;
        _this.loadJs('http://usercenter.game.yy.com/js/client/UsercenterClient-4.2.1.js', function () {
            UsercenterClient.getCacheUserInfo(function (d) {
                if (d) {
                    callback&&callback(d);
                }
            });
        });
    },
    ajaxCount: 0,
    isLogin: null,
    /**
     * 检测UDB登录
     * @param fn
     */
    checkLogin: function(fn) {
        var _this = this;
        _this.ajaxCount++;
        if(_this.isLogin!=null){
            fn&&fn(_this.isLogin);
        }else{
            var ajaxFun = $.ajax({
                url: 'http://udblogin.duowan.com/ext/isLogin.do',
                type: 'get',
                dataType: 'jsonp',
                timeout : 5000, //超时时间设置，单位毫秒
                cache: false,
                success:function(d){
                    if(+d.status==200&& d.data=='true'){
                        _this.isLogin = true;
                        fn&&fn(true);
                    }else{
                        _this.isLogin = false;
                        fn&&fn(false);
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    ajaxFun.abort();
                    if(_this.ajaxCount>30){
                        alert('验证登录请求发生异常，请刷新页面');
                    }else{
                        //console.log('验证登录请求发生异常，重新请求');
                        _this.checkLogin(fn);
                    }
                },
                complete: function(XMLHttpRequest,status){ //请求完成后最终执行参数
                    if(status=='timeout'){//超时,status还有success,error等值的情况
                        ajaxFun.abort();
                        if(_this.ajaxCount>30){
                            alert('验证登录请求超时，请刷新页面');
                        }else{
                            //console.log('验证登录请求超时，重新请求');
                            _this.checkLogin(fn);
                        }
                    }
                }
            });
        }
    }
});

window.YPCSDK = new ypcSdk();