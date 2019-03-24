var StatExpand = {
		
		/**
		 * 推广页展开
		 */
		"expandPromotion" : function(date, expandId, rowId) {
			
			$.ajax({
				type: 'post',
				dataType : "json",
				data: {
					"date" : date,
					"expandId" : expandId,
				},
				url: "/stat/expandPromotion.do",
				async:true,
				success: function(result) {
					alert(rowId);
					$("#"+rowId).append(result);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) { }
			});			
			
		},
		
		"end" : null
		
}