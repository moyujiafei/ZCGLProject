<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>资产管理</title>
        <meta name="description" content="资产管理">
        <meta name="keywords" content="index">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="renderer" content="webkit">
        <meta http-equiv="Cache-Control" content="no-siteapp" />
        <link rel="icon" type="image/png" href="${pageContext.servletContext.contextPath }/style/assets/i/favicon.png">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/assets/css/amazeui.min.css" />
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/assets/css/amazeui.datatables.min.css" />
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/assets/css/app.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
        <script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
    </head>

    <body data-type="login">
        <script src="${pageContext.servletContext.contextPath }/style/assets/js/theme.js"></script>
        <div class="am-g tpl-g">
        	<!-- 风格切换 -->
	        <div class="tpl-skiner">
	            <div class="tpl-skiner-toggle am-icon-cog">
	            </div>
	            <div class="tpl-skiner-content">
	                <div class="tpl-skiner-content-title">
	                   	微信登录
	                </div>
	                <div class="tpl-skiner-content-bar">
						<div>
				        	<a class="am-btn am-btn-success am-radius" href="#" onclick="showQrcodeDia()">
							  <i class="am-icon-weixin"></i>
							</a>
				        </div>
	                </div>
	            </div>
	        </div>
            <div class="tpl-login">
                <div class="tpl-login-content">
                    <div class="tpl-login-logo">
                    </div>
                    <form id="loginForm" method="post" class="am-form tpl-form-line-form">
                        <c:if test="${not empty appTreeInfo}">
                            <div class="am-form-group">
                                <select name="appId" id="user_corp">
                                    <option value="" style="display: none;">请选择企业号主体</option>
                                    <c:forEach items="${appTreeInfo}" var="appTreeItem">
                                        <option value="${appTreeItem.id}">${appTreeItem.text}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <div class="am-form-group">
                            <input name="uname" type="text" class="tpl-form-input" id="user_name" placeholder="请输入账号">
                        </div>
                        <div class="am-form-group">
                            <input name="upw" type="password" class="tpl-form-input" id="user_password" placeholder="请输入密码">
                        </div>
                        <div class="am-form-group">
                            <button type="button" onclick="login();" class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn">登 &nbsp;录</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <div id="wxLoginDialog" style="display: none;">
        	<div class="easyui-layout" fit="true">
	        	<div data-options="region:'center'" >
			        <table style="text-align: center;" cellpadding="0" cellspacing="0" border="0" width="100%">
			          <tr>
			          	<td>
			          		<c:if test="${not empty appTreeInfo}">
			          		  <form id="wx_corp_from" class="am-form am-form-horizontal">
			                       <div class="am-form-group">
			                       	   <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">企业号：</label>
			                       	   <div class="am-u-sm-10">
				                           <select name="appId" id="user_corp_wx" onchange="getQrcodeImg()" class="am-form-field">
				                               <option value="" style="display: none;">请选择企业号主体</option>
				                               <c:forEach items="${appTreeInfo}" var="appTreeItem">
				                                   <option value="${appTreeItem.id}">${appTreeItem.text}</option>
				                               </c:forEach>
				                           </select>
			                           </div>
			                       </div>
		                      </form>
		                     </c:if>
			          	</td>
			          </tr>
				      <tr>
				        <td>
				        	<div id="qrcodeImgContainer"></div>
				        </td>
				      </tr>
				    </table>
	        	</div>
			    <div data-options="region:'south',split:false" style="height: 40px;text-align: center;padding: 5px;">
			    	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeQrcodeDig()">关闭</a>
			    </div>
		    </div>
        </div>
        
        <script src="${pageContext.servletContext.contextPath }/style/assets/js/amazeui.min.js"></script>
        <script src="${pageContext.servletContext.contextPath }/style/assets/js/app.js"></script>
        <script type="text/javascript" src="http://rescdn.qqmail.com/node/ww/wwopenmng/js/sso/wwLogin-1.0.0.js"></script>
    </body>
    <script type="text/javascript">
    	function showQrcodeDia(){
			$("#wxLoginDialog").dialog({
                title: "微信登录",
                width: 512,
                height: 570,
                modal: true,
                draggable: true,
                inline: true,
                onClose: function() {
                	$('#user_corp_wx').val("")
                },
                onOpen: function () {
                }
            });
    	};
    	function closeQrcodeDig () {
    		$("#wxLoginDialog").dialog('close');
    	};
    	function getQrcodeImg () {
    		// qrcodeImgContainer
    		var appId = $('#user_corp_wx').val();
    		if (appId) {
	    		$.messager.progress(); // 显示进度条
	            $.ajax({
	                url: "${pageContext.servletContext.contextPath}/console/user/getQrcodeImg.do",
	                cache: false,
	                async: true,
	                data: {
	                    "appId": appId
	                },
	                type: "post",
	                dataType: "json",
	                success: function(result) {
	                    $.messager.progress('close');
	                    if (!result.error) {
	                    	var redirectUri = encodeURI(getContextPath() + "/console/user/wxLogin.do?appId="+appId);
	                    	window.WwLogin({
						        "id" : "qrcodeImgContainer",  
						        "appid" : result.appid,
						        "agentid" : result.agentid,
						        "redirect_uri" : redirectUri,
						        "state" : result.state,
						        "href" : ""
							});
	                    }
	                },
	                error: function() {
	                    $.messager.progress('close');
	                }
	            });
    		}
    	};
        function login() {
            $.messager.progress(); // 显示进度条
            $('#loginForm').form('submit', {
                url: '${pageContext.servletContext.contextPath}/console/user/login.do',
                onSubmit: function() {
                    var isValid = $(this).form('validate');//提交前进行表单验证
                    if (!isValid) {
                        $.messager.progress('close');
                    }
                    return isValid; // 返回false终止表单提交
                },
                success: function(data) {
                    $.messager.progress('close');
                    if (data != 'success') {
                        $.messager.alert('警告', data, 'warning');
                        $("#upw").select();
                        return false;
                    } else {
                        //验证通过，跳转页面
                        window.location.href = '${pageContext.servletContext.contextPath }/console/home.do';
                    }
                }
            });
        }
        // 回车直接提交
        document.onkeydown = function(e) {
            var event = window.event || e; //兼容火狐浏览器
            if (event.keyCode == 13) {
                login();
            }
        };
        $(function(){
        	$("#user_corp").validatebox({
        		required: true,
        	});
        	$("#user_name").validatebox({
        		required: true,
        	});
        	$("#user_password").validatebox({
        		required: true,
        	});
        });
    </script>

</html>
