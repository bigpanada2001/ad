var Common = {
	"displayConfirmDialog" : function(message){
		var messageDiv = $("#common-confirm-message");
		messageDiv.html(message);
		$("#common-confirm-message").dialog("open");
	}
}

$(function() {
	var messageDiv = $("#common-confirm-message");
	if(messageDiv.length == 0){
		$("body").append("<div id='common-confirm-message' title='消息' style='display:none;'></div>");
	}
	
	$("#common-confirm-message").dialog({
		modal: true,
		autoOpen: false,
		width:400
	});
});