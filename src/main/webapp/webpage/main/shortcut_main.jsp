<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
<title><t:mutiLang langKey="jeect.platform"/></title>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<script type="text/javascript" src="plug-in/easyui/portal/jquery.portal.js"></script>
<link rel="stylesheet" type="text/css" href="plug-in/easyui/portal/portal.css">

<style type="text/css">
a {
	color: Black;
	text-decoration: none;
}

a:hover {
	color: black;
	text-decoration: none;
}
/*update-start--Author:zhangguoming  Date:20140622 for：左侧树调整：加大宽度、更换节点图标、修改选中颜色*/
.tree-node-selected{
    background: #eaf2ff;
}
/*update-end--Author:zhangguoming  Date:20140622 for：左侧树调整：加大宽度、更换节点图标、修改选中颜色*/
</style>
<SCRIPT type="text/javascript">

	$(function() {
		$('#layout_jeecg_onlineDatagrid').datagrid({
			url : 'systemController.do?datagridOnline&field=ip,logindatetime,user.userName,',
			title : '',
			iconCls : '',
			fit : true,
			fitColumns : true,
			pagination : true,
			pageSize : 10,
			pageList : [ 10 ],
			nowarp : false,
			border : false,
			idField : 'id',
			sortName : 'logindatetime',
			sortOrder : 'desc',
			frozenColumns : [ [ {
				title : '<t:mutiLang langKey="common.code"/>',
				field : 'id',
				width : 150,
				hidden : true
			} ] ],
			columns : [ [ {
				title : '<t:mutiLang langKey="common.login.name"/>',
				field : 'user.userName',
				width : 130,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : 'IP',
				field : 'ip',
				width : 150,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : '<t:mutiLang langKey="common.login.time"/>',
				field : 'logindatetime',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				},
				hidden : true
			} ] ],
			onClickRow : function(rowIndex, rowData) {
			},
			onLoadSuccess : function(data) {
				$('#layout_jeecg_onlinePanel').panel('setTitle', '( ' + data.total + ' )' + ' <t:mutiLang langKey="lang.user.online"/>');
			}
		}).datagrid('getPager').pagination({
			showPageList : false,
			showRefresh : false,
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : ''
		});		
		
		$('#layout_jeecg_onlinePanel').panel({
			tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
				}
			} ]
		});
		
		$('#layout_east_calendar').calendar({
			fit : true,
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', new Date());
			}
		});
		$(".layout-expand").click(function(){
			$('#layout_east_calendar').css("width","auto");
			$('#layout_east_calendar').parent().css("width","auto");
			$("#layout_jeecg_onlinePanel").find(".datagrid-view").css("max-height","200px");
			$("#layout_jeecg_onlinePanel .datagrid-view .datagrid-view2 .datagrid-body").css("max-height","180px").css("overflow-y","auto");
		});
	});
	var onlineInterval;
	
	function easyPanelCollapase(){
		window.clearTimeout(onlineInterval);
	}
	function easyPanelExpand(){
		onlineInterval = window.setInterval(function() {
			$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
		}, 1000 * 20);
	}

    function panelCollapase(){
        $(".easyui-layout").layout('collapse','north');
    }
	
	$(document).ready(function(){
		var url = "noticeController.do?getNoticeList";
		var roll = false;
		$.ajax({
    		url:url,
    		type:"GET",
    		dataType:"JSON",
    		async: false,
    		success:function(data){
    			if(data.success){
    				var noticeList = data.attributes.noticeList;
    				var noticehtml = "";
    				if(noticeList.length > 0){
    					noticehtml = noticehtml + "<marquee behavior='scroll'><a href='javascript:goNotice(1);'>";
        				for(var i=0;i<noticeList.length;i++){
        					noticehtml = noticehtml + noticeList[i].noticeTitle + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        				}
        				noticehtml = noticehtml + "</a></marquee>";
        				$("#notice").html(noticehtml);
    					roll = true;
    					$("#noticeTitle").show();
    					$("#notice").show();
    				}else{
    					$("#noticeTitle").hide();
    					$("#notice").hide();
    				}
    			}
    		}
    	});
	});
    
    function goNotice(id){
    	var addurl = "noticeController.do?noticeList";

    }
</SCRIPT>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<!-- 顶部-->
<div region="north" border="false" title="" style="BACKGROUND: #A8D7E9; height: 100px; padding: 1px; overflow: hidden;">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td align="left" style="vertical-align: text-bottom">
    <div style="position: absolute; top: 21px;left: 4px;font-size: 31px;">App发布管理系统 <span style="letter-spacing: -1px;"></span></div>
    </td>
    <td align="right" nowrap>
        <table border="0" cellpadding="0" cellspacing="0">
            <tr style="height: 25px;" align="right">
                <td style="" colspan="2">
                    <div style="background: url(plug-in/login/images/top_bg.jpg) no-repeat right center; float: right;">

                    <div style="float: left; margin-left: 18px;">
                        <div style="right: 0px; bottom: 0px;">
                            <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-comturn" style="color: #FFFFFF">
                                <t:mutiLang langKey="common.control.panel"/>
                            </a>&nbsp;&nbsp;
                            <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-exit" style="color: #FFFFFF">
                                <t:mutiLang langKey="common.logout"/>
                            </a>
                        </div>
                        <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
                            <div onclick="openwindow('<t:mutiLang langKey="common.profile"/>','userController.do?userinfo')">
                                <t:mutiLang langKey="common.profile"/>
                            </div>
                            <div class="menu-sep"></div>
                            <div onclick="add('<t:mutiLang langKey="common.change.password"/>','userController.do?changepassword','',550,200)">
                                <t:mutiLang langKey="common.change.password"/>
                            </div>



                             <div onclick="clearLocalstorage()">
                       		 	<t:mutiLang langKey="common.clear.localstorage"/>
                   			 </div>
                        </div>
                        <div id="layout_north_zxMenu" style="width: 100px; display: none;">
                            <div onclick="exit('loginController.do?logout','<t:mutiLang langKey="common.exit.confirm"/>',1);">
                                <t:mutiLang langKey="common.exit"/>
                            </div>
                        </div>
                    </div>
                    <div style="float: left; margin-left: 8px;margin-right: 5px; margin-top: 5px;">
                        <img src="plug-in/easyui/themes/default/images/layout_button_up.gif"
                             style="cursor:pointer" onclick="panelCollapase()" />
                    </div>
                    </div>
                </td>
            </tr>
            <tr style="height: 80px;">
                <td colspan="2">
                    <ul class="shortcut">
                        <!-- 动态生成并赋值过来 -->
                    </ul>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</div>
<!-- 左侧-->
<div region="west" split="true" href="loginController.do?shortcut_top" title="<t:mutiLang langKey="common.navegation"/>" style="width: 200px; padding: 1px;"></div>
<!-- 中间-->
<div id="mainPanle" region="center" style="overflow: hidden;">
	<style type="text/css">  
	<!--  
	.proccess{display:none;background-color:#f2f2f2;border:0px solid;border-color:#009900;height:100%;line-height:600px;width:100%;text-align:center;margin:100;position:absolute;top:0;left:0;}  
	.proccess b{vertical-align:middle;background:url(plug-in/layer/skin/default/loading-2.gif) no-repeat 0 center;padding-left:55px;display:inline-block;}  
	-->  
	</style> 
	<div class="proccess" id="panelloadingDiv"><b>&nbsp;</b></div>
    <div id="maintabs" class="easyui-tabs" fit="true" border="false">
        查询接口（ post请求）：bizAppvsionController.do?getappbyid

        <br> id：402880ec6b70430c016b704609be0004
    </div>
</div>
<!-- 右侧 -->
<div collapsed="true" region="east" iconCls="icon-reload" title="<t:mutiLang langKey="common.assist.tools"/>" split="true" style="width: 190px;"
	data-options="onCollapse:function(){easyPanelCollapase()},onExpand:function(){easyPanelExpand()}">
	<!--
    <div id="tabs" class="easyui-tabs" border="false" style="height: 240px">
        <div title='<t:mutiLang langKey="common.calendar"/>' style="padding: 0px; overflow: hidden; color: red;">
            <div id="layout_east_calendar"></div>
        </div>
    </div>
    <div id="layout_jeecg_onlinePanel" data-options="fit:true,border:false" title=<t:mutiLang langKey="common.online.user"/>>
        <table id="layout_jeecg_onlineDatagrid"></table>
    </div>
    -->
    <div class="easyui-layout" fit="true" border="false">
		<div region="north" border="false" style="height:180px;overflow: hidden;">
			<div id="tabs" class="easyui-tabs" border="false" style="height: 240px">
				<div title='<t:mutiLang langKey="common.calendar"/>' style="padding: 0px; overflow: hidden; color: red;">
					<div id="layout_east_calendar"></div>
				</div>
			</div>
		</div>
		<div region="center" border="false" style="overflow: hidden;">
			<div id="layout_jeecg_onlinePanel" fit="true" border="false" title='<t:mutiLang langKey="common.online.user"/>'>
				<table id="layout_jeecg_onlineDatagrid"></table>
			</div>
		</div>
	</div>
</div>
<!-- 底部 -->
<div region="south" border="false" style="height: 25px; overflow: hidden;">

</div>

</body>
</html>