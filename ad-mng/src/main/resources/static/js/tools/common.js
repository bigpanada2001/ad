/**
 * 常用变量
 */
var ggVar = {
    submitConfirm:"您是否确认提交表单数据？",
    deleteConfirm:function(item){
        return "警告！请您谨慎操作，您是否确认删除记录：" + item + "？删除后不可恢复。";
    },
    addConfirm:function(item){
        return "您是否确认添加记录：" + item + "？";
    }
};

/**
 * form to json
 * @param formId
 * @returns {{}}
 */
var form2json = function(formId) {
    var array = $("#"+formId).serializeArray();
    var result = {};
    $.each(array, function(index, object) {
        var name = object.name;
        var value = object.value;
        if (result[name]) {
            result[name] += (',' + value);
        } else {
            result[name] = value;
        }
    });
    return result;
};

var ggAjax = {
    get:function(url, data, callback) {
        var that = this;
        $.ajax({type:"GET", url:url, data:data, success:function(result){
            that.commonCallback(result, callback);
        }});
    },
    getForJSON:function(url, data, callback) {
        $.ajax({type:"GET", url:url, data:data, success:function(result){
            if (result)
                result = JSON.parse(result);
            if (callback)
                callback(result);
        }});
    },
    post:function(url, data, callback) {
        var that = this;
        $.ajax({type:"POST", url:url, data:data, success:function(result) {
            that.commonCallback(result, callback);
        }});
    },
    postForJSON:function(url, data, callback) {
        $.ajax({type:"POST", url:url, data:data, success:function(result) {
            if (result)
                result = JSON.parse(result);
            if (callback)
                callback(result);
        }});
    },
    commonCallback:function(result, callback) {
        //将处理结果存放到cookie
        if (result) {
            result = JSON.parse(result);
            if (result.success) {
                ggNotice.put(true, "系统处理成功");
            } else {
                ggNotice.put(false, result.message);
            }
        } else {
            ggNotice.put(false, "服务器异常");
        }
        //可能需要调用callback
        if (callback) {
            callback(result);
        }
    }
};

var ggNotice = {
    cookieKey:"userNoticeCookieKey",
    put:function(success, message) {
        var data = {
            success:success,
            message:message
        };
        //set the cookie
        $.cookie(
            this.cookieKey,
            JSON.stringify(data),
            {path:"/"}
        );
    },
    get:function() {
        var cookieValue = $.cookie(this.cookieKey);
        if (cookieValue) {
            //clear the cookie
            $.cookie(
                this.cookieKey,
                null,
                {path:"/"}
            );
            return JSON.parse(cookieValue);
        }
        return null;
    },
    show:function(containId) {
        var cookie = this.get();
        if (cookie) {
            var alert;
            if (cookie.success) {
                alert = '<div class="alert alert-success alert-dismissable">' +
                            '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' + cookie.message +
                        '</div>';
            } else {
                alert = '<div class="alert alert-danger alert-dismissable">' +
                            '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' + cookie.message +
                        '</div>';
            }
            $("#"+containId).html(alert);
        }
    }
};

var ggEvent = {
    bindEnterDown:function(callback) {
        document.onkeydown = function(event){
            var ev = document.all ? window.event : event;
            if (ev.keyCode == 13) {
                if (callback) {
                    callback();
                }
            }
        }
    }
};

var ggString = {
    likeWrap:function(str) {
        return "%25" + str + "%25";
    }
};

var ggCommon = {
    isImg:function(fileName){
        if (fileName) {
            var splits = fileName.split(".");
            var postfix = splits[splits.length - 1];
            if (postfix == 'jpg' ||
                postfix == 'jpeg' ||
                postfix == 'png' ||
                postfix == "gif") {
                return true;
            }
        }
        return false;
    },
    dftDatePattern:"yyyy-mm-dd hh:ii:00"
};

var ggHtml = {
    createOption:function(value, text){
        return "<option value='$1'>$2</option>".replace("$1", value).replace("$2", text);
    }
};