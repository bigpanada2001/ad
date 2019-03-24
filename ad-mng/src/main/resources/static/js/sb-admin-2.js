//cache the menu which was click before.
$(function() {
    $('#side-menu').metisMenu();
    var currentSideMenuId = $.cookie("currentSideMenuId");
    var currentSideHrefId = function(){
            var selfSideHref = $("[to='"+self.location.pathname+"']");
            if (selfSideHref && selfSideHref.length > 0) {
                return selfSideHref.attr("id");
            } else {
                return null;
            }
        }() || $.cookie("currentSideHrefId");
    if (currentSideMenuId) {
        $("#"+currentSideMenuId).click();
    }
    if (currentSideHrefId) {
        $("#"+currentSideHrefId).css("color", "#ffffff");
        $("#"+currentSideHrefId).parent().css("background-color", "#428bca");
    }
    $("[id^='side-menu-']").click(function() {
        if ($(this).parent().find('[aria-expanded="true"]').length > 0) {
            $.cookie("currentSideMenuId", $(this).attr("id"), {path:"/"});
        } else {
            $.cookie("currentSideMenuId", null, {path:"/"});
        }
    });
    $("[id^='side-href-']").click(function(){
        if ($(this).find("i").length > 0) {
            $.cookie("currentSideMenuId", null, {path:"/"});
        }
        $.cookie("currentSideHrefId", $(this).attr("id"), {path:"/"});
        self.location.href = $(this).attr("to");
    });
    $("#index-href").click(function(){
        $.cookie("currentSideMenuId", null, {path:"/"});
        $.cookie("currentSideHrefId", null, {path:"/"});
        self.location.href = "/";
    });
    $("[mark='head-href']").click(function(){
        $.cookie("currentSideMenuId", null, {path:"/"});
        $.cookie("currentSideHrefId", null, {path:"/"});
        self.location.href = $(this).attr("to");
    });
});

//增加新标签的入口
$(function(){
    $("[id^='side-href-']").each(function(index, item){
        $(this).append("<span mark='new-window' class='pull-right glyphicon glyphicon-new-window' style='padding-left:10px;padding-bottom:10px' title='新标签页'></span>");
    });
    $("[mark='new-window']").click(function(){
        window.open($(this).parent().attr("to"), "_blank");
        return false;
    });
});

//show the notice in nav.jsp。
$(function(){
    NoticeUtil.show("alert-wrapper");
});

//common bootstrap invoke
$(function(){
    $('[data-toggle="tooltip"]').tooltip({html:true});
    $("[data-toggle='popover']").popover({html:true});
});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse')
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse')
        }

        height = (this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    })
});
