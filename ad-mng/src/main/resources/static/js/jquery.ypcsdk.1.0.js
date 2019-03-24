/**
 * User: 王超
 * Date: 2015-4-21
 * 珠海PC业务常用接口(jquery环境调用)，包括如下功能
 * 1.YPCSDK.getParams(key, url) 获取页面传参
 * 2.YPCSDK.initStat(conf, fn) 初始化数据上报，参数说明 ——> conf：初始化参数, fn：回调函数
 *   conf详细说明
     pro: 产品编号
     rso: 来源
     rsoDesc: 来源描述
     loginEid: 登录上报
     loginEidDesc: 登录上报描述
     logoutEid: 退出页面上报
     logoutEidDesc: 退出页面上报描述
 * 3.YPCSDK.clickStat(eid, eidDesc) 点击上报
 * 4.YPCSDK.gameStat(eid, eidDesc, gameId, serverId, isOpenGame) 点击游戏上报
 * 5.YPCSDK.loadJs(url, fn) 动态载入JS，参数说明 ——> url：JS路径，可以是一个数组, fn：回调函数
 * 6.YPCSDK.formatDate(date, format) 格式化日期
 * 7.YPCSDK.dealCookie(name, value) 读写cookie
 * 8.YPCSDK.getRemoteData(url, dataObj, fn, isCache) 读取script数据
 * 9.YPCSDK.getRemoteJsonData(url, dataObj, fn, isCache) 读取json格式数据（支持跨域）
 * 10.YPCSDK.getRandom(min, max) 取min到max之间的随机数
 * 11.YPCSDK.isYYE() 判断是否YY浏览器
 * 12.YPCSDK.maxLen(str, len) 字符串截取
 * 13.YPCSDK.getSystemTime(fn) 获取系统时间
 */
var ypcSdk = function(){};

$.extend(ypcSdk.prototype, {
    /**
     * 动态载入JS
     * @param url JS路径，可以是一个数组
     * @param fn 载入完成后回调
     */
    loadJs: function(url, fn, idx){
        var _this = this;
        if(typeof(url)=='object'){ //载入多个JS
            if(idx==undefined){
                _this.loadJs(url, fn, 0);
            }else{
                _this.loadJs(url[idx], function(){
                    if(idx<url.length-1){
                        _this.loadJs(url, fn, idx+1);
                    }else{
                        fn&&fn();
                    }
                });
            }
            return;
        }
        var script = document.createElement("script");
        var head = document.getElementsByTagName('head')[0];
        script.type = "text/javascript";
        script.src = url;
        head.appendChild(script);
        if (script.readyState) { //IE
            script.onreadystatechange = function () {
                if (script.readyState == "loaded" || script.readyState == "complete") {
                    script.onreadystatechange = null;
                    fn&&fn();
                    head.removeChild(script); //使用完成后删除
                }
            };
        } else { //标准的DOM浏览器
            script.onload = function () {
                fn&&fn();
                head.removeChild(script); //使用完成后删除
            };
        }
    },
    /**
     * 获取系统时间
     * @param fn
     */
    getSystemTime: function(fn){
        var _this = this;
        _this.getRemoteJsonData('http://nav2.game.yy.com/nav2/getTime.do', null, function(d){
            _this.SystemTime=+d.status==200?new Date(d.data):new Date();
            _this.refreshSystemTime();
            fn&&fn();
        });
    },
    /**
     * 刷新系统时间
     */
    refreshSystemTime:function(){
        var _this = this;
        setTimeout(function(){
            _this.SystemTime.setSeconds(_this.SystemTime.getSeconds()+1);
            _this.refreshSystemTime();
        },1000);
    },
    /**
     * 获取页面参数
     * @param key
     * @param url
     * @returns {*}
     */
    getParams: function (key, url) {
        var search = url || location.search;
        if(search.indexOf('?')>=0){
            search=search.slice(1);
        }
        var paramsArray = search.split('&'), params , tmp;
        if (paramsArray && paramsArray.length) {
            params={};
            for (var i = 0, len = paramsArray.length; i < len; i++) {
                tmp = paramsArray[i];
                tmp && (tmp = tmp.split('='));
                if (tmp && tmp.length) {
                    params[tmp[0]] = (tmp[1]&&decodeURIComponent(tmp[1]))||'';
                }
            }
        }
        if (key) {
            return params[key];
        }
        return params;
    },
    /**
     * 取min到max之间的随机数
     * @param min
     * @param max
     * @returns {number|string}
     */
    getRandom: function(min, max){
        return Math.floor(Math.random() * (max - min + 1)) + min;
    },
    //判断是否YY浏览器
    isYYE: function(){
        return /yye/i.test(window.navigator.userAgent)||(window.external && typeof(window.external.IsYYE)!='undefined' && window.external.IsYYE()=='true');
    },
    /**
     * 字符串截断
     * @param 字符串
     * @param 长度
     * @returns 截断后的字符串
     */
    maxLen: function (str, len, type) {
        if (!str || str == null) return '';
        if (str.length <= len)
            return str;
        return str.substring(0, len) + (type?'':'...');
    },
    /**
     * 处理cookie
     * 浏览器不能存储超过30个cookie
     * 另外在同一域下不能存储超过20个cookie
     * 最后cookie的名/值不能大于4K
     * @example
     * set dealCookie('name','test',{});
     * get dealCookie('name')
     */
    dealCookie:function (name, value, options) {
        if (typeof value != 'undefined') { // name and value given, set cookie
            options = options || {};
            if (value === null) {
                value = '';
                options.expires = -1;
            }
            var expires = '';
            if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                var date;
                if (typeof options.expires == 'number') {
                    date = new Date();
                    date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
                } else {
                    date = options.expires;
                }
                expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
            }
            var path = options.path ? '; path=' + options.path : '';
            var domain = options.domain ? '; domain=' + options.domain : '';
            var secure = options.secure ? '; secure' : '';
            document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
        } else { // only name given, get cookie
            var cookieValue = null;
            if (document.cookie) {
                var cookies = document.cookie.split(';');
                for (var i = 0, len = cookies.length; i < len; i++) {
                    var cookie = $.trim(cookies[i]);
                    // Does this cookie string begin with the name we want?
                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                        try {
                            cookieValue = this.decode(cookie.substring(name.length + 1));
                        }
                        catch (e) {
                            this.dealCookie(name, null);
                            cookieValue = null;
                        }
                        break;
                    }
                }
            }
            return cookieValue;
        }
    },
    /**
     * 初始化数据上报
     * @param conf
     * @param fn
     */
    initStat: function(conf, fn){
        var _this = this;
        var params = _this.getParams();
        var defaults = {
            pro: 'yyexplorer', //产品编号
            rso: params['rso']||'FROM_YYEXPLORER', //来源
            rsoDesc: params['rso_desc']||'浏览器游戏中心', //来源描述
            yyuid: _this.dealCookie('yyuid')||'', //yyuid
            username: _this.dealCookie('username')||'', //YY用户名
            mid: params['mid']||'', //机器码（用于移动端）
            loginEid: '', //登录上报
            loginEidDesc: '', //登录上报描述
            logoutEid: '', //退出页面上报
            logoutEidDesc: '' //退出页面上报描述
        };
        if(_this.YaStat){
            fn&&fn();
        }else{
            if(!_this.YaOpts){
                _this.YaOpts = $.extend(defaults, conf);
            }
            _this.loadJs('http://sz.duowan.com/s/ya/ya.1.3.1-min.js', function(){
                _this.YaStat = new YA.report.YYAnalytics(_this.YaOpts.pro, _this.YaOpts.mid.length>0?{mid:_this.YaOpts.mid}:_this.YaOpts.username);

                //产品启动
                _this.YaStat.reportProductStartUp({pro: _this.YaOpts.pro, rso: _this.YaOpts.rso, rso_desc: _this.YaOpts.rsoDesc});

                //产品心跳
                _this.YaStat.startProductHeartbeat();

                if(_this.YaOpts.loginEid.length>0){
                    //登陆上报
                    _this.clickStat(_this.YaOpts.loginEid, _this.YaOpts.loginEidDesc);
                }

                //产品退出
                window.onbeforeunload=function(){
                    if(_this.YaOpts.logoutEid.length>0){
                        //退出页面上报
                        _this.clickStat(_this.YaOpts.logoutEid, _this.YaOpts.logoutEidDesc);
                    }
                    _this.YaStat.reportProductEndUp();
                };

                fn&&fn();
            });
        }
    },
    /**
     * 点击事件上报
     * @param eid
     * @param eidDesc
     */
    clickStat: function(eid, eidDesc){
        var _this = this;
        _this.initStat(null, function(){
            var data = {
                eid: $.trim(eid),
                eid_desc: $.trim(eidDesc),
                yyuid: _this.YaOpts.yyuid
            };
            if(_this.YaOpts.mid.length>0){
                data.mid = _this.YaOpts.mid;
            }
            _this.YaStat.reportProductEvent(data);
        });
    },
    /**
     * 点击打开游戏上报
     * @param eid 埋点
     * @param eidDesc 埋点描述
     * @param gameId 游戏编号
     * @param serverId 游戏区服
     * @param isOpenGame 是否打开游戏
     */
    gameStat: function(eid, eidDesc, gameId, serverId, isOpenGame){
        var _this = this;
        _this.initStat(null, function(){
            eid = $.trim(eid);
            eidDesc = $.trim(eidDesc)||'';
            var pro = _this.YaOpts.pro, rso = _this.YaOpts.rso, rsoDesc = _this.YaOpts.rsoDesc;
            if(isOpenGame){
                var url = 'http://udblogin.duowan.com/login.do?game='+gameId+'&server='+serverId+'&pid=&report_ver=new&ref='+eid+'&ref_desc='+eidDesc+'&pro='+pro+'&rso='+rso+'&rso_desc='+rsoDesc+'&from='+pro+'&webyygame&showtools=0';
                var a = document.createElement('A');
                a.target = '_blank';
                a.href = url;
                var e = document.createEvent('MouseEvents');
                e.initEvent( 'click', true, true );
                a.dispatchEvent(e);
            }
            var data = {
                eid: eid,
                eid_desc: eidDesc,
                pro: pro,
                gam: gameId,
                gse: serverId,
                yyuid: _this.YaOpts.yyuid,
                rso: rso,
                rso_desc: rsoDesc
            };
            _this.YaStat.reportProductEvent(data, '');
        });
    },
    //获取数据script格式
    getRemoteData: function (/*string*/ url, /*object*/ dataObj, /*function*/fn, /*boolean*/ isCache) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'script',
            data: dataObj || '',
            cache: isCache || false,
            success: function (data, textStatus, xhr) {
                fn && fn(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                // console.log('getRecommendData:get data error')
            }
        });
    },
    //获取数据json格式
    getRemoteJsonData:function(/*string*/ url, /*object*/ dataObj, /*function*/fn, /*boolean*/ isCache){
        $.ajax({
            url:url,
            type:'get',
            dataType:'jsonp',
            data: dataObj || '',
            cache: isCache || false,
            success:function(data){
                fn && fn(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                // console.log('getRemoteJsonData:get data error')
            }
        });
    },
    /**
     * 图片延迟加载
     * @param id
     */
    imgHold: function (id) {
        var _this = this;
        var $container = id?$('#'+id):$('body');
        $container.find('[data-original]').each(function () {
            tmp = $(this);
            var size = $(this).attr('size');
            if (tmp.attr('data-original')) {
                if(size.indexOf('|')!=-1){
                    var sizeArr = size.split('|');
                    _this.createImg($(this), sizeArr[0], sizeArr[1]);
                }else{
                    _this.createImg($(this), size, size);
                }
            }
        });
    },
    /**
     * 根据父级元素图片属性生成img
     * @param {object} el jq元素对象
     * @void
     * @example
     */
    createImg: function ($el, width, height) {
        if ($el.size() > 0) {
            var url = $el.attr('data-original');
            if (url && url.length>0) {
                var image = new Image();
                image.onload =function(){
                    $el.append('<img width="' + width + '" height="'+height+'" src="' + url + '" align="absmiddle" />');
                };
                image.onerror =function(){
                    $el.append('<img width="' + width + '" height="'+height+'" src="img/default.png" align="absmiddle" />');
                };
                image.src = url;
                $el.removeAttr('data-original');
            }
        }
    },
    /**
     * 格式化日期
     * @param date 日期字符串或时间戳
     * @param format 格式化如yyyy-MM-dd
     * @returns 格式化后的日期
     */
    formatDate : function(date, format){
        var _this = new Date(date);
        var o = {
            "M+" : _this.getMonth()+1, //month
            "d+" : _this.getDate(), //day
            "h+" : _this.getHours(), //hour
            "m+" : _this.getMinutes(), //minute
            "s+" : _this.getSeconds(), //second
            "q+" : Math.floor((_this.getMonth()+3)/3), //quarter
            "S" : _this.getMilliseconds() //millisecond
        };

        if(/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (_this.getFullYear()+"").substr(4 - RegExp.$1.length));
        }

        for(var k in o) {
            if(new RegExp("("+ k +")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
            }
        }
        return format;
    }
});

window.YPCSDK = new ypcSdk();

Array.prototype.indexOf = function(Object){
    for(var i = 0;i<this.length;i++){
        if(this[i] == Object){
            return i;
        }
    }
    return -1;
};