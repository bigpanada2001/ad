<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0;font-size: 13px">
    <%-- top navigator --%>
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="javascript:void(0);" id="index-href"><strong>手游推广运营平台</strong></a>
    </div>
    <%-- top navigator drop down --%>
    <ul class="nav navbar-top-links navbar-right">
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                <i class="fa fa-user fa-fw"></i>
                <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
                <li>
                	<a href="javascript:void(0);" mark="head-href" to="/logout"><i class="fa fa-gear fa-fw"></i>登出</a>
                    <a href="javascript:void(0);" mark="head-href" to="/setting/page.do"><i class="fa fa-gear fa-fw"></i>系统设置</a>
                </li>
            </ul>
        </li>
    </ul>
    <%-- side navigetor --%>
    <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
                <%-- 搜索框 --%>
                <li class="sidebar-search">
                    <div class="input-group custom-search-form">
                        <input type="text" class="form-control" placeholder="回车搜索">
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="button">
                                <i class="fa fa-search"></i>
                            </button>
                        </span>
                    </div>
                </li>
                <%-- 数据统计报表 --%>
                <li>
                    <a href="javascript:void(0);" id="side-menu-report">
                        <i class="fa fa-bar-chart-o fa-fw"></i>市场统计报表<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <%--<li><a href="javascript:void(0);" id="side-href-report-1" to="/">平台数据</a></li>--%>
                        <li><a href="javascript:void(0);" id="side-href-report-2" to="/stat/listPromotion.do">推广查询</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-3" to="/stat/listChannelRecover.do">渠道回收</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-4" to="/stat/listChannelRecoverLTV.do">渠道回收-LTV</a></li>
                        <!-- <li><a href="javascript:void(0);" id="side-href-report-5" to="/stat/listChannelKeep.do">渠道留存</a></li> -->
                        <li><a href="javascript:void(0);" id="side-href-report-6" to="/stat/listChannelKeepVertica.do">渠道留存</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-7" to="/stat/listChannelLoss.do">渠道折损</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-8" to="/stat/listGamePayStat.do">游戏充值统计</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-9" to="/stat/listAdvertRealTimeStat.do">实时监控</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-10" to="/stat/listFlashStat.do">落地页报表</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-11" to="/userAction/list.do">用户行为</a></li>
                        <li><a href="javascript:void(0);" id="side-href-report-12" to="/stat/userIPList.do">用户IP列表</a></li>
                    </ul>
                </li>
                <li>
                    <a href="javascript:void(0);" id="side-menu-opration">
                        <i class="fa fa-bar-chart-o fa-fw"></i>运营统计报表<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-opration-1" to="/operate/stat.do">数据概览</a></li>
                        <!-- <li><a href="javascript:void(0);" id="side-href-opration-7" to="/operate/statMulti.do">数据概览(新)</a></li> -->
                        <li><a href="javascript:void(0);" id="side-href-opration-12" to="/operate/statMultiVertica.do">数据概览(新)</a></li>
                        <!-- <li><a href="javascript:void(0);" id="side-href-opration-2" to="/operate/roleKeep.do">新增创角留存</a></li> -->
                        <li><a href="javascript:void(0);" id="side-href-opration-11" to="/operate/roleKeepVertica.do">新增创角留存</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-4" to="/operate/ltv.do">LTV</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-13" to="/operate/ltvOverview.do">LTV概览</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-3" to="/appRebate/list.do">渠道分成管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-5" to="/stat/rechargeInfo.do">财务对账报表</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-6" to="/paySourceConfig/list.do">充值渠道配置管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-8" to="/operate/userRoleLevel.do">游戏用户等级查询</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-9" to="/h5game/statH5Game.do">H5Game充值查询</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-10" to="/h5game/statCPS.do">CPS充值查询</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-15" to="/order/recharge-detail.do">充值订单详情</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-14" to="/operate/gameCenterEventAnalysis.do">游戏广场概览</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-16" to="/operate/statPayUser.do">付费用户报表</a></li>
                        <li><a href="javascript:void(0);" id="side-href-opration-17" to="/operate/statLevelLoss.do">等级用户报表</a></li>
                    </ul>
                </li>
                <%-- 市场配置管理 --%>
                <li>
                    <a href="javascript:void(0);" id="side-menu-market">
                        <i class="fa fa-wrench fa-fw"></i>市场配置管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-market-1" to="/userSource/list.do">用户来源管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-market-2" to="/channelGroup/list.do">渠道分组管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-market-3" to="/channelInfo/list.do">渠道信息管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-market-4" to="/advertPlace/list.do">广告位管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-market-5" to="/author/list.do">设计师管理</a></li>
                    </ul>
                </li>
                <%-- 游戏及区服管理 --%>
                <li>
                    <a href="javascript:void(0);" id="side-menu-game" >
                        <i class="fa fa-rocket fa-fw"></i>游戏管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-game-1" to="/game/list.do">游戏及区服管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-game-2" to="/apiKeyGame/list.do">游戏上报(apiKey)</a></li>
                    </ul>
                </li>
                <%-- CDN文件管理 --%>
                <li>
                    <a href="javascript:void(0);" id="side-menu-cdn">
                        <i class="fa fa-file fa-fw"></i>CDN文件管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-cdn-1" to="/cdn/uploader.do">文件发布器</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cdn-2" to="/template/list.do">广告页模板管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cdn-3" to="/flash/list.do">广告页素材管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cdn-4" to="/media/list.do?f_media_type=1">视频库管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cdn-5" to="/media/list.do?f_media_type=2">媒体库管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cdn-6" to="/appPackage/list.do">游戏安装包管理</a></li>
                    </ul>
                </li>
                <%-- 广告信息管理 --%>
                <li>
                    <a href="javascript:void(0);" id="side-href-advert-1" to="/advert/list.do">
                        <i class="fa fa-image fa-fw"></i>广告信息管理
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0);" id="side-href-platform-redirect-1" to="/platformRedirect/list.do">
                        <i class="fa fa-image fa-fw"></i>平台跳转管理
                    </a>
                </li>
                <%-- 广告费用录入 --%>
                <li>
                    <a href="javascript:void(0);" id="side-href-cost-1" to="/cost/list.do">
                        <i class="fa fa-rmb fa-fw"></i>广告费用录入
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0);" id="side-menu-friends">
                        <i class="fa fa-file fa-fw"></i>广告商管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <!-- <li><a href="javascript:void(0);" id="side-href-friends-1" to="/friends/list.do">广告商列表</a></li>
                        <li><a href="javascript:void(0);" id="side-href-friends-2" to="/friends/user/list.do">广告主管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-friends-3" to="/friends/api-callback/stat.do">API回调统计</a></li>
                        <li><a href="javascript:void(0);" id="side-href-friends-4" to="/friendsStat/realTimeMonitorStat.do">广告商报表</a></li>
                        <li><a href="javascript:void(0);" id="side-href-friends-5" to="/friends/dsp/stat.do">DSP报表</a></li>
                         -->
                        <li><a href="javascript:void(0);" id="side-href-friends-6" to="/guest/listGuest.do">推广访客管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-friends-7" to="/guestProrataReduction/listGuest.do">推广访客核减管理</a></li>

                    </ul>
                </li>
                    <li>
                        <a href="javascript:void(0);" id="side-menu-promotion">
                            <i class="fa fa-file fa-fw"></i>推广管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li><a href="javascript:void(0);" id="side-href-promotion-1" to="/promotion/manager.do">渠道管理</a></li>
                            <li><a href="javascript:void(0);" id="side-href-promotion-2" to="/promoter/listWxAdKey.do">微信广告秘钥管理</a></li>
                        </ul>
                    </li>
                <li>
                    <a href="javascript:void(0);" id="side-menu-cpl">
                        <i class="fa fa-file fa-fw"></i>CPL管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-cpl-1" to="/cpl/stat.do">CPL广告统计</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cpl-2" to="/cpl/list.do">CPL管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cpl-3" to="/cpl/listGuest.do">CPL访客管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cpl-4" to="/cpl/rechargeDataExport.do">CPL充值数据导出</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cpl-5" to="/cplAdvertMapping/list.do">CPL广告映射管理</a></li>
                    </ul>
                </li>
                <li>
                    <a href="javascript:void(0);" id="side-menu-cps">
                        <i class="fa fa-file fa-fw"></i>CPS管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="javascript:void(0);" id="side-href-cps-1" to="/cps/stat.do">CPS广告统计</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cps-2" to="/cps/list.do">CPS管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cps-3" to="/cps/listAdvert.do">CPS广告管理</a></li>
                        <li><a href="javascript:void(0);" id="side-href-cps-4" to="/cps/listGuest.do">CPS访客管理</a></li>
                    </ul>
                </li>

                    <li>
                        <a href="javascript:void(0);" id="side-menu-permission">
                            <i class="fa fa-file fa-fw"></i>权限管理<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li><a href="javascript:void(0);" id="side-href-permission-1" to="/promoter/listPromoter.do">推广专员管理</a></li>
                            <li><a href="javascript:void(0);" id="side-href-permission-3" to="/promoter/listPromoterGame.do">推广专员游戏权限管理</a></li>
                            <li><a href="javascript:void(0);" id="side-href-permission-2" to="/busigroupuserconf/list.do">用户业务组管理</a></li>
                            <li><a href="javascript:void(0);" id="side-href-permission-4" to="/promoter/listSpecialUser.do">用户组织管理</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="javascript:void(0);" id="side-menu-userBasic">
                            <i class="fa fa-file fa-fw"></i>用户数据<span class="pull-right glyphicon glyphicon-chevron-left"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li><a href="javascript:void(0);" id="side-href-userBasic-1" to="/userBasic/list.do">用户信息详情</a></li>
                        </ul>
                    </li>
                <%-- 提示语 --%>
                <li>
                    <div id="alert-wrapper"></div>
                </li>
            </ul>
        </div>
    </div>
</nav>