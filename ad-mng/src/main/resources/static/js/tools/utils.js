/**
 * 公共文案
 */
var Messages = {
    confirmFormSubmit:      "您是否确认提交表单数据？",
    confirmDeleteData:      function(id){return "您是否确认删除该记录【"+id+"】？";},
    confirmOperation:       function(id){return "您是否确认对【"+id+"】进行此操作？"},
    operationSuccess:       "系统处理成功",
    serverNoResponse:       "服务器响应失败"
};

/**
 * 搜索功能简化
 */
var SearchUtil = {
    buildEnterSearch:function(){
        $(document).keydown(function(event){
            if (event.keyCode == 13) {
                SearchUtil.doSearch();
            }
        });
        $(".sea-btn").click(function(){
            SearchUtil.doSearch();
        });
    },
    doSearch:function() {
        var queryParams = [];
        $.each($("[name^='f_']"), function(){
            var name = $(this).attr("name");
            var value = $(this).val();
            if (name && value) {
                queryParams.push(name+"="+value);
            }
        });
        var queryString = queryParams.join("&");
        self.location.href="?"+queryString;
    }
};

/**
 * 表单控件处理方法
 */
var FormUtil = {
    toJson:function(formId, paramMap) {
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
        if (paramMap != null && paramMap != 'undifined') {
            $.each(paramMap, function(k,v){
                result[k] = v;
            });
        }
        return result;
    }
};

/**
 * 异步处理抽象
 */
var AjaxUtil = {
    get:function(url, data, callback) {
        $.ajax({
            type:       "GET",
            cache:      false,
            url:        url,
            data:       data,
            success:    function(result) {
                if (result && typeof result !== 'object') {
                    result = JSON.parse(result);
                }
                if (callback) {
                    callback(result);
                }
            }
        });
    },
    post:function(url, data, callback) {
        $.ajax({
            type:       "POST",
            cache:      false,
            url:        url,
            data:       data,
            success:    function(result) {
                if (result && typeof result !== 'object') {
                    result = JSON.parse(result);
                }
                if (callback) {
                    callback(result);
                }
            }
        });
    },
    handleFormSubmitResult:function(result) {
        if (result && result.success) {
            NoticeUtil.put(true, Messages.operationSuccess);
        } else if (result && result.error) {
            NoticeUtil.put(false, result.message);
        } else {
            NoticeUtil.put(false, Messages.serverNoResponse);
        }
    }
};

/**
 * 用户提示信息的设置与展示
 */
var NoticeUtil = {
    cookieKey:"userNoticeHolder",
    put:function(success, message) {
        var data = {success:success, message:message};
        $.cookie(this.cookieKey, JSON.stringify(data), {path:"/"});
    },
    get:function() {
        var cookieValue = $.cookie(this.cookieKey);
        if (!cookieValue) {
            return null;
        }
        $.cookie(this.cookieKey, null, {path:"/"}); //clear the cookie
        return JSON.parse(cookieValue);
    },
    show:function(containId) {
        var cookie = this.get();
        if (cookie) {
            var alert;
            if (cookie.success) {
                alert =
                '<div class="alert alert-success alert-dismissable" style="margin-bottom:0;">' +
                    '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' + cookie.message +
                '</div>';
            } else {
                alert =
                '<div class="alert alert-danger alert-dismissable" style="margin-bottom:0;">' +
                    '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' + cookie.message +
                '</div>';
            }
            $("#"+containId).html(alert);
        }
    },
    alert:function(success, message, containId) {
        containId = containId || "alert-wrapper";
        this.put(success, message);
        this.show(containId);
    },
    close:function(containId) {
        containId = containId || "alert-wrapper";
        $("#"+containId).html("");
    }
};

/**
 * 用于创建html元素
 */
var HtmlUtil = {
    createOption:function(value, text) {
        return "<option value='$1'>$2</option>".replace("$1", value).replace("$2", text);
    },
    back:function() {
        self.location.href = document.referrer || "/";
    }
};

/**
 * 字符串校验
 */
var StringUtil = {
    isNumeric: function(str) {
        return /^\d+(\.\d+)?$/.test(str);
    },
    isWrapped: function(str) {
        return /^'[^']*'$/.test(str) || /^"[^"]*"$/.test(str);
    },
    isArray: function(str) {
        return /^\[.*\]$/.test(str);
    },
    removeHeadTail: function(str) {
        return (!str || str.length <= 2) ? "" : str.substring(1, str.length - 1);
    }
};

/**
 * 代码检查
 */
var CodeUtil = {
    /**
     * 只允许变量值为数值、字符串或数组，不允许变量值中使用等号或逗号。
     */
    isJsDeclareLegal: function(declare) {
        if (!declare) return false;

        var nameValue = declare.split("=")||[];
        if (nameValue.length != 2) return false;

        var name = nameValue[0], value = nameValue[1];
        if (!name || !value) return false;

        if (StringUtil.isNumeric(value)) {
            return true;
        }
        if (StringUtil.isWrapped(value)) {
            return true;
        }
        if (StringUtil.isArray(value)) {
            value = StringUtil.removeHeadTail(value);
            var parts = value.split(",")||[], result = true;
            $.each(parts, function(index, item) {
                item = item.trim();
                if (!StringUtil.isNumeric(item) && !StringUtil.isWrapped(item)) {
                    result = false;
                    return false;
                }
            });
            return result;
        }
        return false;
    },
    /**
     * 检查多行变量定义，返回语法错误的行数，若无错误则返回[0]。
     */
    isJsDeclareLinesLegal: function(text) {
        if (!text) return 0;
        var lines = text.split("\n")||[], errorLineNum = 0;
        $.each(lines, function(index, line){
            if (CodeUtil.isJsDeclareLegal(line)) {
                return true;
            } else {
                errorLineNum = index + 1;
                return false;
            }
        });
        return errorLineNum;
    }
};

/**
 * 字符串转换
 */
var JSONUtil = {
    parseJSON: function(str) {
        if (typeof str === 'string') {
            while (typeof str !== 'object') {
                str = JSON.parse(str);
            }
        }
        return str;
    }
};

/**
 * 数组工具类
 */
var ArrayUtil = {
    contains:function(arr, ele) {
        var contains = false;
        $.each(arr, function(index, item){
            if (ele == item) {
                contains = true;
                return false;
            }
        });
        return contains;
    }
};

/**
 * 文件上传工具类
 */
var FileUploadUtil = {
    bind:function(id, path, fileObjName, sucCallback, funcBeforeUpload) {
        var queueId = "fileUploadQueue";
        if (document.getElementById(queueId) == null) {
            $("body").append("<div id='"+queueId+"' style='display:none'></div>");
        }
        $(id).uploadify({
            "buttonText":   "选择文件",
            "width":        120,
            "height":       35,
            "queueID":      queueId,
            "swf":          "/js/plugins/uploadify/uploadify.swf",
            "uploader":     path,
            "fileObjName":  fileObjName,
            "formData":     {respType:"json"},
            'onDialogClose'  : function(queueData) {
                if(funcBeforeUpload){
                    funcBeforeUpload(queueData);
                }
            },
            "onUploadSuccess":function(file, result, response) {
                console.log("upload result");
                result = JSONUtil.parseJSON(result);
                if (result && result.error) {
                    alert(result.message);
                }
                if (result && result.success) {
                    sucCallback(file, result);
                }
            }
        });
    }
};

var Validator = {
    /**
     * Fields in the array
     * @param fields field name array
     * @returns {boolean} pass=true
     */
    validateMandatoryFields: function(fields){
        var pass = true;
        $.each(fields, function (idx, field) {
            var $node = $("[name=" + field + "]");
            if($node.val() == ""){
                $node.addClass("invalid-input");
                pass = false;
            }else{
                $node.removeClass("invalid-input");
            }
        });
        return pass;
    },

    /**
     * endDate can't be before startDate
     * @param startDate
     * @param endDate
     */
    validateDatePeriod: function(startDate, endDate){
        var pass = true;
        var $startDate = $("input[name=" + startDate + "]");
        var $endDate = $("input[name=" + endDate + "]");

        var startDate = new Date($startDate.val());
        var endDate = new Date($endDate.val());

        if(endDate<startDate){
            pass = false;
            $endDate.addClass("invalid-input");
        }else{
            $endDate.removeClass("invalid-input");
        }
        return pass;
    }
};

var fileUpload = function(files, fileHandler) {
    for(var i=0; i < files.length; i++) {
        var file = files[i]; // 获取文件对象

        var form = new FormData();
        form.append("token", new Date().getTime());
        form.append("file", file);

        var xhr = new XMLHttpRequest();
        xhr.open("post", "/cdn/saveFile.do", true);
        xhr.onload = function (e) {
            console.log("Upload finished", e.target.status, e.target.response);
            var result = JSON.parse(e.target.response);
            result = JSON.parse(result);

            if(fileHandler){
                fileHandler(result);
            }
        };

        xhr.send(form);
    }
};
