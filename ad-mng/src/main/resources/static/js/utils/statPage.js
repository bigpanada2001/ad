var StatPage = {

	/**
	 * 扩展数据url
	 */
	"expandUrl" : "",
	
	
	
	/**
	 * 初始化统计页面
	 */
	"init" : function(expandUrl) {
		this.expandUrl = expandUrl;
	},


		
    /**
		 * 点击数据展开
		 */
    "clickExpand" : function(date, key, expandTypeId, rowId) {

        if (this.isExpanding(rowId, key, expandTypeId)) {

            // 收缩
        	//this.shrink(rowId, key, expandTypeId);

        } else {
            // 展开
        	this.expand(date, key, expandTypeId, rowId);
        }
    },

    
    
    /**
		 * 增加扩展列
		 */
    "expandColumn" : function(angleId, expandTypeId) {

        if (this.existExpandColumn(expandTypeId)) {
            return;
        }

        var expandColumnDiv = "<td group='expandColumn_" + expandTypeId + "'></td>";
        var expandTitleColumnDiv = "<th group='expandColumn_" + expandTypeId + "' rowspan='2'>" + ExpandType.getColumnNameById(angleId, expandTypeId) + "</td>";

        var baseTh = $("th[group='expandColumn_" + (expandTypeId - 1) + "']");
        var baseTd = $("td[group='expandColumn_" + (expandTypeId - 1) + "']");

        $(baseTh).after(expandTitleColumnDiv);
        $(baseTd).after(expandColumnDiv);
    },
    
    
    
    
    /**
			 * 是否存在扩展列
			 */
    "existExpandColumn" : function(expandTypeId) {
        return $("th[group='expandColumn_" + expandTypeId + "']").length > 0;
    },

    
    
    
    /**
			 * 判断当前key是否展开状态
			 */
    "isExpanding" : function(rowId, key, expandTypeId) {
        var trs = $("table").find("tr");

        var expandBase = $("#" + rowId).find("td[group='expandColumn_" + (expandTypeId - 1) + "'][key]");

        return $(expandBase).attr("open") != null;
    },

    
    
    
    /**
			 * 收缩扩扩展行
			 */
    "shrink" : function(rowId, key, expandTypeId) {

        // 点击列
        var expandBase = $("#" + rowId).find("td[group='expandColumn_" + (expandTypeId - 1) + "'][key]");

        // 点击行后的每一行
        var nextTrs = $("#" + rowId).nextAll("tr");

        // 移除点击行后的每一行
        $.each(nextTrs, function(i, tr) {

            var group = $(tr).find("td:eq(" + (expandTypeId - 1) + ")").attr("group");
            if (group == "expandGroup") {
                $(tr).remove();
            } else {
                return false; // break
            }
        });

        // 去除展开标志
        $(expandBase).removeAttr("open");

        // 去除展开列
        this.shrinkColumn(expandTypeId);
    },

    
    
    
    /**
		 * 收缩扩展列
		 */
    "shrinkColumn" : function(expandTypeId) {

    	var existOpen = false;
    	var baseTds = $("td[group='expandColumn_" + (expandTypeId-1) + "']");
    	$.each(baseTds, function(i, td) {
    		if($(td).attr("open") == "open") {
    			existOpen = true;
    			return false;
    		}
    	});
    	if(existOpen) return;
    	
    	
    	
        var baseTh = $("th[group='expandColumn_" + (expandTypeId - 1) + "']");

        // 点击标题列后的每一列
        var nextThs = $(baseTh).nextAll("th");

        // 移除点击标题列后的每一列
        $.each(nextThs, function(i, th) {

            var group = $(th).attr("group");

            if (group != null) {
            	
            	//列是否存在展开
            	var existOpen = false;
            	$.each($("td[group='" + group + "']"), function(i, td) {
            		if($(td).attr("key") != null) {
            			existOpen = true;
            			return false;
            		}
            	});
            	if(existOpen) {
            		return true;
            	}
            	//列是否存在展开END
            	
            	//不存在展开，则删除列
                $("th[group='" + group + "']").remove();
                $("td[group='" + group + "']").remove();
            } else {
                return false; // break
            }
        });
    },

    
    
    
    /**
		  * 推广页展开
		  */
    "expand" : function(date, key, expandTypeId, rowId) {

    	var dateParam = "date=" + date;
    	
    	/** 兼容范围时间的展开 */
    	if(date == null || date == '') {
    		var startDate = $("#stroeStartDate").val();
    		var endDate = $("#stroeEndDate").val();
    		var history = $("#stroeHistory").val();
    		dateParam = "startDate=" + startDate + "&endDate=" + endDate + "&f_history=" + history;
    	}
    	
    	
    	var angleId = $("#stroeAngleId").val();

        var jsonData = dateParam + "&f_key=" + key + "&angleId=" + angleId + "&expandTypeId=" + expandTypeId;

        jsonData += ("&" + this.getSearchField(true));
        
        var column = ExpandType.getColumnById(angleId, expandTypeId);
        if (column != null && jsonData.indexOf("&f_" + column + "=") == -1) {
            jsonData += "&f_" + column + "=" + key;
        }

        jsonData += "&rowId=" + rowId;
        
        $.ajax({
            type: 'post',
            data: jsonData,
            url: this.expandUrl,
            async: false,
            dataType: "html",
            success: function(result) {
                // 这里怎么判断非成功
                result = result.trim();
                if (result.indexOf("<tr") == 0) {
                    var treetableEnabled = $("#treetable-report").length > 0;
                    if(!treetableEnabled){
                        $("#" + rowId).after(result);
                    }else{
                        var parentRow = $("#treetable-report").treetable("node", rowId);
                        $("#treetable-report").treetable("loadBranch", parentRow, result);
                    }

                    var expandBase = $("#" + rowId).find("td[group='expandColumn_" + (expandTypeId - 1) + "'][key]");

                    $(expandBase).attr("open", "true");

                    if(!treetableEnabled){
                        StatPage.expandColumn(angleId, expandTypeId);
                    }

                    if($("input[name=fieldSel]").length > 0){   // show/hide columns features only enabled in 2 reports
                        showHideColumns();
                    }
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {}
        });

    },
    
    
    /**
     * 补足扩展后的列
     */
    "fullExpandColumn" : function(newRowIdPre, expandTypeId) {
    	
    	var newTr = $("tr[id^='"+newRowIdPre+"']");
    	
    	var appendDiv = "";
    	for(var i=0; i<parseInt(expandTypeId); i++) {
    		appendDiv += "<td group=\"expandGroup\"></td>";
    	}
    	$(newTr).prepend(appendDiv);
    	
    	
    	var td = $(newTr).find("td[group='expandColumn_"+expandTypeId+"']");
    	var lastColumnType = $("th[group^='expandColumn_']").last().attr("group");
    	var lastColumnTypeId = lastColumnType.match(/expandColumn_(.+?)/i)[1];
    	
    	var appendExpandDiv = "";
    	
    	for(var i=parseInt(expandTypeId)+1; i<=lastColumnTypeId; i++) {
    		appendExpandDiv += "<td group=\"expandColumn_"+i+"\"></td>";
    	}
    	td.after(appendExpandDiv);
    },
    
    
    /**
     * 切换 历史查询/当日查询
     */
    "realTimeChange" : function() {
    	
    	if($("#historyId").is(":checked")) {
    		$("#selectDateId").show();
    		$("#historyId").val("1");
    	} else {
    		$("#selectDateId").hide();
    		$("#historyId").val("0");
    	}
    },
    
    
    /** 搜索 */
    "search" : function(view) {
    	if(!this.checkDate()) {
    		return;
    	}
        var params = this.getSearchField();
        if(view && view=="treetable"){
            var url =self.location.href + "?f_view=treetable&" + params;
            $.get(url, function(html) {
                //console.log(html);
                $("#treetable-container").html(html);
            });
        }else{
            self.location.href="?" + params;
        }
    },
    
    /** 搜索 */
    "searchUrl" : function(url) {
    	if(!this.checkDate()) {
    		return;
    	}
        var params = this.getSearchField();
       	window.location.href = url + "?" + params;
    },
    
    
    /** 获取搜索参数 */
    "getSearchField" : function(isStore) {
    	var pre = "f_";
    	var nameAttr = "name";
    	if(isStore) {
            if(this.storedQueryParams){
                console.log("this.storedQueryParams", this.storedQueryParams);
                return this.storedQueryParams;
            }
    		pre = "fstore_";
    		nameAttr = "linkName";
    	}
    	var queryParams = [];
        $.each($("[name^='"+pre+"']"), function(){
            var name = $(this).attr(nameAttr);
            var value = $(this).val();
            if (name && value) {
                queryParams.push(name+"="+value);
            }
        });

        // add values from the zTree for Channel Group/Channel

        this.storedQueryParams = queryParams.join("&");
        return this.storedQueryParams;
    },
    
    
    /** 获取存储的查询参数 */
    "getStoreField" : function() {
    	var queryParams = [];
        $.each($("[name^='fstore_']"), function(){
            var name = $(this).attr("name");
            var value = $(this).val();
            if (name && value) {
                queryParams.push(name+"="+value);
            }
        });
        return queryParams.join("&");
    },
    
    
    /** 导出 */
    "export" : function(url) {
        var idx = url.indexOf("?");
        if(idx > 0){
            self.location.href=url + "&" + this.getSearchField();
        }else{
            self.location.href=url + "?" + this.getSearchField();
        }
    },
    
    
    /** 检查日期填写 */
    "checkDate" : function() {
    	if(!$("#historyId").is(":checked")) {
    		return true;
    	}
    	var startDate = $("input[name='f_startDate']").val();
    	var endDate = $("input[name='f_endDate']").val();
    	if(startDate == '' || endDate == '') {
    		NoticeUtil.alert(false, "起始时间不能为空");
    		return false;
    	}
    	return true;
    },
    
    /** 改变查询维度 */
    "changeAngle" : function(angelId) {
    	$("#selectAngleId").val(angelId);
        if (angelId == 0) {
            $("#selectGameId").hide();
        } else {
            $("#selectGameId").show();
        }
        $("#gameId").val("");
    },

    /** 改变显示 */
    "changeShow" : function(operate,name) {
        if (operate == 0) {
            $("#"+name).hide();
        } else {
        	$("#"+name).show();
        }
    },
    
    /** 查询顺序 */
    "orderByChange" : function(angelId) {
    	if($("#"+angelId).is(":checked")) {
    		$("#"+angelId).val("1");
    	} else {
    		$("#"+angelId).val("0");
    	}
    },

    "end": null

}