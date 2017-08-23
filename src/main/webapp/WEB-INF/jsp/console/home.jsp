<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资产管理平台</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/flexslider/flexslider.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/fullscreenslides/css/fullscreenstyle.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/style/assets/css/mystyle.css">
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/highcharts/exporting.js"></script>    
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/highcharts/highcharts-zh_CN.js"></script> 
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/flexslider/jquery.flexslider-min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/fullscreenslides/js/jquery.fullscreenslides.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jmp3/jquery.jmp3.js"></script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.welcomeBg{
	background-image: url(${pageContext.servletContext.contextPath}/images/welcome_admin.jpg);
	background-size:100% 100%;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${pageContext.servletContext.contextPath}/images/welcome_admin.jpg',sizingMethod='scale');
}
.style7 {font-size: 12px; color: #FFF; }
.STYLE3 {
	font-size: 12px;
	color: #435255;
}

.STYLE4 {font-size: 12px}
.STYLE5 {font-size: 12px; font-weight: bold; }
.link_color{color:#3399CC; text-decoration:none;font-weight:bold;}/*链接设置*/
.link_color:visited{color:#3399CC; text-decoration:none;font-weight:bold;}/*访问过的链接设置*/
.link_color:hover{color:#3399CC; text-decoration:none;font-weight:bold;}/*鼠标放上的链接设置*/
</style>
</head>
<body class="easyui-layout" data-options="border:true,fit:true" >
      	<div style="height:130px;padding: 0px;margin: 0px;overflow: hidden;"
 			data-options="border:true,region:'north',split:false">
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				    <td height="57" background="${pageContext.servletContext.contextPath }/images/main_03.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
				      <tr>
				        <td width="378" height="57" background="${pageContext.servletContext.contextPath }/images/main_01.gif">
				        	<div style="margin-left: 78px;font-weight: bolder;font-size: 24px;color: white;">
					        	<span style="font-family:sans-serif;">企业资产</span>&nbsp;<span style="font-size: 18px;font-family: cursive;">云管理平台</span>
				        	</div>
				        </td>
				        <td>&nbsp;</td>
				        <td width="281" valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
				          <tr>
				            <td width="33" height="27"><img src="${pageContext.servletContext.contextPath }/images/main_05.gif" width="33" height="27" /></td>
				            <td width="248" background="${pageContext.servletContext.contextPath }/images/main_06.gif"><table width="225" border="0" align="center" cellpadding="0" cellspacing="0">
				              <tr>
				                <td height="17">
				                	<div align="right" ><a href="javascript:void(0);" onclick="updatepwd(${currentUser.uid});" ><img style="border:0;" src="${pageContext.servletContext.contextPath }/images/pass.gif" width="69" height="17" /></a></div></td>
				                <td><div align="right"><a href="javascript:void(0);" onclick="currUser();"><img style="border:0;" src="${pageContext.servletContext.contextPath }/images/user.gif" width="69" height="17" /></a></div></td>
				                <td><div align="right"><a href="javascript:void(0);" onclick="quit();"><img style="border:0;" src="${pageContext.servletContext.contextPath }/images/quit.gif" width="69" height="17" /></a></div></td>
				              </tr>
				            </table></td>
				          </tr>
				        </table></td>
				      </tr>
				    </table></td>
				  </tr>
				  <tr>
				    <td height="40" background="${pageContext.servletContext.contextPath }/images/main_10.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
				      <tr>
				        <td width="194" height="40" background="${pageContext.servletContext.contextPath }/images/main_07.gif">&nbsp;</td>
				        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
				          <tr>
				            <td>&nbsp;</td>
				          </tr>
				        </table></td>
				        <td width="248" background="${pageContext.servletContext.contextPath }/images/main_11.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
				          <tr>
				            <td width="16%" align="right">&nbsp;</td>
				            <td width="75%"><div align="left"><span class="style7" style="font-size: 12px; color: #FFF;">欢迎：${sessionScope.loginInfo.name}（${sessionScope.loginInfo.roleName}）</span></div></td>
				            <td width="9%">&nbsp;</td>
				          </tr>
				        </table></td>
				      </tr>
				    </table></td>
				  </tr>
				  <tr>
				    <td height="30" >
				    	<c:forEach items="${currUserMenus}" var="pMenu">
					    	<div style="float: left;margin: 3px;">
						    	<a href="javascript:void(0)" class="easyui-menubutton" data-options="menu:'#${pMenu.keyName}',iconCls:'${pMenu.iconClass}'" >${pMenu.name}</a>
						    	<div id="${pMenu.keyName}">
						    		<c:forEach items="${pMenu.children}" var="chiMenu" >
						    			<div 
						    				data-options="iconCls:'${chiMenu.iconClass}'"
						    				onclick="changeCenter('${pageContext.servletContext.contextPath}'+'${chiMenu.url}')">${chiMenu.name}</div>
						    			<c:if test="${chiMenu.flag == 1}">
						    				<div class="menu-sep"></div>
						    			</c:if>
						    		</c:forEach>
						    	</div>
					    	</div>
				    	</c:forEach>
				    </td>
				  </tr>
			</table>
			<div id="pwddialog" style="display: none;overflow:hidden;"></div>
			<div id="currUser" style="display: none;overflow:hidden;"></div>
		</div>
   		<div id="center" class="welcomeBg"
   		 				data-options="region:'center',
	     						split:false">
	     
	    </div>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/admin/home.js"></script>						
</body>
</html>
