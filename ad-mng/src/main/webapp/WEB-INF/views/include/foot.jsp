<script type="text/javascript" src="/js/jquery.js"></script>							<!-- jQuery -->
<script type="text/javascript" src="/js/jquery-ui.min.js"></script>                      <!-- jQuery UI -->
<script type="text/javascript" src="/js/jquery.cookie.js"></script>                      <!-- jQuery cookie Javascript -->
<script type="text/javascript" src="/js/bootstrap.min.js"></script>                      <!-- Bootstrap Core JavaScript -->
<script type="text/javascript" src="/js/bootstrap-datetimepicker.min.js"></script>       <!-- Time picker Javascript-->
<script type="text/javascript" src="/js/bootstrap-datetimepicker.zh-CN.js"></script>     <!-- Time Picker Locale -->
<script type="text/javascript" src="/js/bootstrap-select.min.js"></script>               <!-- Select Javascript -->
<script type="text/javascript" src="/js/plugins/metisMenu/metisMenu.min.js"></script>    <!-- Metis Menu Plugin JavaScript -->
<script type="text/javascript" src="/js/plugins/morris/raphael.min.js"></script>         <!-- Morris Charts JavaScript -->
<script type="text/javascript" src="/js/plugins/morris/morris.min.js"></script>          <!-- Morris Charts JavaScript -->
<script type="text/javascript" src="/js/sb-admin-2.js"></script>                         <!-- SB-admin-custom -->
<script type="text/javascript" src="/js/plugins/uploadify/jquery.uploadify.min.js"></script>    <!-- Uploadify -->
<script type="text/javascript" src="/js/tools/date.js"></script>
<script type="text/javascript" src="/js/tools/utils.js"></script>
<script type="text/javascript" src="/js/tools/page.js"></script>
<script type="text/javascript" src="/js/utils/statPage.js"></script>
<script type="text/javascript" src="/js/utils/expandType.js"></script>
<script type="text/javascript" src="/js/tools/test.js"></script>
<script type="text/javascript" src="/js/jquery.zclip.js"></script>
<script type="text/javascript" src="/js/jquery.treetable.js"></script>
<script type="text/javascript" src="/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery.ypcsdk.1.0.js"></script>
<script type="text/javascript" src="/js/jquery.ypcsdk.udb.1.0.js"></script>
<script type="text/javascript" src="/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="/js/highcharts/exporting.js"></script>
<script type="text/javascript" src="/js/excellentexport.js"></script>
<script type="text/javascript" src="/js/angular/angular.js"></script>
<script type="text/javascript" src="/js/angular/angular-animate.js"></script>
<script type="text/javascript" src="/js/angular/angular-route.js"></script>
<script type="text/javascript" src="/js/angular/moment.js"></script>
<script type="text/javascript" src="/js/angular/angular-moment.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		YPCSDK.checkLogin(function(isLogin){
			if(!isLogin){
				YPCSDK.YYLogin();
			}
		});
	});
</script>
<script type="text/javascript">
    $.ajaxSetup({
        //timeout: 500,
        //dataType: 'html',

        //请求成功后触发
        //success: function (data) { show.append('success invoke!' + data + '<br/>'); },

        //请求失败遇到异常触发
        error: function (xhr, status, e) {
            console.log("error", status, e);
            NoticeUtil.alert(false, e);
        },

        //完成请求后触发。即在success或error触发后触发
        complete: function (xhr, status) {
            //console.log("complete", status);
            var $loading = $("#loading-icon");
            $loading.css("display", "none");
        },

        //发送请求前触发
        beforeSend: function (xhr) {
            //console.log("beforeSend");
            var $loading = $("#loading-icon");
            $loading.css("display", "inline-block");
            NoticeUtil.close();
        }
    });
</script>

