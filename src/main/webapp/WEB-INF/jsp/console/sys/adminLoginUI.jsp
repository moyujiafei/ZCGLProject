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
        <script src="${pageContext.servletContext.contextPath }/style/assets/js/amazeui.min.js"></script>
        <script src="${pageContext.servletContext.contextPath }/style/assets/js/app.js"></script>
    </body>
    <script type="text/javascript">
        function login() {
            $.messager.progress(); // 显示进度条
            $('#loginForm').form('submit', {
                url: '${pageContext.servletContext.contextPath}/console/user/login.do',
                onSubmit: function(param) {
                	param.isSuperAdmin = "true";
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
