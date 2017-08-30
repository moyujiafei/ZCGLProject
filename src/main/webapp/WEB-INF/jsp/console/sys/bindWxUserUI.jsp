<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>微信账户绑定</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
</head>
<body>
	<div id="bindWxDialog">
		<form id="dataForm" method="post">
           <table align="center" style="margin-top: 20px;" cellspacing="10">
           	   <tr>
           	   	  <td colspan="4" style="border: 1px solid #eee">
           	   	  	<p>检测到该微信用户还未绑定管理平台账号，</p>
           	   	  	<p>请输入管理平台账号和密码进行绑定</p>
           	   	  </td>
           	   </tr>
               <tr>
                   <td></td>
                   <td style="text-align:right;">账号:</td>
                   <td>
                       <input id="txbuname" style="padding-left: 6px;width:160px;" name="uname" />
                   </td>
                   <td style="color:#FF0000">*</td>
               </tr>
               <tr>
                   <td></td>
                   <td style="text-align:right;">密码:</td>
                   <td>
                       <input id="txbupw" style="padding-left: 6px;width:160px;" name="upw" />
                   </td>
                   <td style="color:#FF0000">*</td>
               </tr>
           </table>
           <table align="center" style="margin-top: 15px;">
               <tr>
                   <td style="text-align: center"><a href="javascript:void(0);" id="okBtn_user">绑定</a>
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" id="cancelBtn_user">取消</a></td>
               </tr>
           </table>
       </form>
	</div>
	<script type="text/javascript">
	var bindWxOpt = {
		toLogin: function(){
			window.location.href="${pageContext.servletContext.contextPath }/console/user/loginUI.do";
		}
	};
	$(function () {
		var validFlag = "${validFlag}";
		var currAppId = "${currAppId}";
// 		if (validFlag !=="1" || !currAppId) {
// 			$.messager.alert("","非法用户","warning",function(){
// 				bindWxOpt.toLogin();
// 			});
// 			return false;
// 		}
		$("#bindWxDialog").dialog({
            title: "微信账户绑定",
            width: 512,
            height: 400,
            inline: true,
            cache: false,
            shadow: true,
            closed: false,
            modal: true,
            closable: false
        });
        $("#bindWxDialog").dialog("move",{
            top: 20
        });
        $("#txbuname").textbox({
            iconCls: "icon-man",
            iconAlign: "right",
            required: true,
            prompt: '请输入用户名'
        });
        $("#txbupw").textbox({
            iconCls: "icon-key",
            iconAlign: "right",
            validType: "length[6,25]",
            required: true,
            type: "password"
        });
        
        $("#okBtn_user").linkbutton({
           iconCls: "icon-ok",
           onClick: function(){
           	$.messager.progress(); // 显示进度条
            $("#dataForm").form("submit", {
                url: "${pageContext.servletContext.contextPath }/console/user/bindWxUser.do",
                onSubmit: function(param) {
                    var valid = $(this).form("validate");
                    if (!valid) {
                        $.messager.progress('close');
                        $.messager.alert("提示", "请按照要求填写表单", "info");
                    }
                    return valid;
                },
                success: function(data) {
                    $.messager.progress('close');
                    if (data == 'success') {
                        $.messager.alert('', '绑定成功', 'info',function () {
                        	window.location.href="${pageContext.servletContext.contextPath }/console/home.do";
                        });
                    } else {
                        $.messager.alert('失败', data , 'info');
                    }
                }
            });
           }
       });

       $("#cancelBtn_user").linkbutton({
           iconCls: "icon-no",
           onClick: function(){
           		bindWxOpt.toLogin();
           }
       });
	});
	</script>
</body>
</html>