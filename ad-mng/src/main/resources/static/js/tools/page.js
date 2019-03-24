/**
 * select关联加载工具
 */
var Selects = {
    onSelectChanged:function(topSelectId, bottomSelectId, url, define, callback, required) {
        //parameter prepare
        if (define && typeof define == 'function') {
            callback = define;
            define = {};
        }

        //extend default setting
        define = $.extend({id:"id", name:"name"}, define);

        var $topSelect = $("#"+topSelectId);
        var $bottomSelect = $("#"+bottomSelectId);
        //bind change event
        $topSelect.change(function(){
            if ($(this).val()=="") {
                $bottomSelect.attr("disabled", "disabled");
                $bottomSelect.find("option").remove();
                if (!required ) {
                    $bottomSelect.append("<option value=''>请选择：</option>");
                }
                if (callback) {
                    callback();
                }
            } else {
                var formData = FormUtil.toJson(topSelectId);
                var name2 = $topSelect.attr("name2");
                if (name2) {
                    formData[name2] = $topSelect.val();
                }
                AjaxUtil.post(url, formData, function(result) {
                    $bottomSelect.removeAttr("disabled");
                    $bottomSelect.find("option").remove();
                    if (!required ) {
                        $bottomSelect.append("<option value=''>请选择：</option>");
                    }
                    //append the data
                    $.each(result, function(index, object){
                        $bottomSelect.append(HtmlUtil.createOption(object[define.id], object[define.name]));
                    });
                    if (callback) {
                        callback();
                    }
                });
            }
        });
    },
    onSelectChanged2:function(topSelectId, bottomSelectId, url, define, callback) {
        //parameter prepare
        if (define && typeof define == 'function') {
            callback = define;
            define = {};
        }

        //extend default setting
        define = $.extend({id:"id", name:"name"}, define);

        var $topSelect = $("#"+topSelectId);
        var $bottomSelect = $("#"+bottomSelectId);
        //bind change event
        $topSelect.change(function(){
            if ($(this).val()=="") {
                $bottomSelect.attr("disabled", "disabled");
                $bottomSelect.find("option").remove();
            } else {
                var formData = FormUtil.toJson(topSelectId);
                var name2 = $topSelect.attr("name2");
                if (name2) {
                    formData[name2] = $topSelect.val();
                }
                AjaxUtil.post(url, formData, function(result) {
                    $bottomSelect.removeAttr("disabled");
                    $bottomSelect.find("option").remove();
                    //append the data
                    $.each(result, function(index, object){
                        $bottomSelect.append(HtmlUtil.createOption(object[define.id], object[define.name]));
                    });
                    if (callback) {
                        callback();
                    }
                });
            }
        });
    },
    onSelectChangedMulti:function(topSelectId, bottomSelectId, url, define, callback, callback1) {
        //parameter prepare
        if (define && typeof define == 'function') {
            callback = define;
            define = {};
        }

        //extend default setting
        define = $.extend({id:"id", name:"name"}, define);

        var $topSelect = $("#"+topSelectId);
        var $bottomSelect = $("#"+bottomSelectId);
        //bind change event
        $topSelect.change(function(){
        	//alert($topSelect.attr("id"));
        	if(topSelectId == 'gameId' && bottomSelectId == 'server') {
        		//alert($("#gameId").val());
    			if($("#gameId").val() && $("#gameId").val().length > 1) {
    				//alert('1');
    				$("#server").find("option").remove();
    				$("#server").attr("disabled", "disabled");
    				return;
    			}
        	}
            if ($(this).val()=="") {
                $bottomSelect.attr("disabled", "disabled");
                $bottomSelect.find("option").remove();
            } else {
                var formData = FormUtil.toJson(topSelectId);
                var name2 = $topSelect.attr("name2");
                if (name2) {
                    formData[name2] = $topSelect.val();
                }
                AjaxUtil.post(url, formData, function(result) {
                    $bottomSelect.removeAttr("disabled");
                    $bottomSelect.find("option").remove();
                    //append the data
                    $.each(result, function(index, object){
                        $bottomSelect.append(HtmlUtil.createOption(object[define.id], object[define.name]));
                    });
                    if (callback) {
                        callback();
                    }
                    if (callback1) {
                        callback1(result);
                    }
                });
            }
        });
    },
    clear:function(selectId) {
        var $select = $("#"+selectId);
        $select.attr("disabled", "disabled");
        $select.find("option").remove();
        $select.append("<option value=''>请选择：</option>");
    },
    clearN:function(selectIds) {
        $.each(selectIds, function(index, item){
            Selects.clear(item);
        });
    },
    onSelectDate:function(topSelectId, bottomSelectId) {
        var $topSelect = $("#"+topSelectId);
        var $bottomSelect = $("#"+bottomSelectId);
        $topSelect.change(function(){
            var val = $topSelect.val();
            if ($(this).val()!="") {
                $bottomSelect.val(val);
            }
        });
    }
};
