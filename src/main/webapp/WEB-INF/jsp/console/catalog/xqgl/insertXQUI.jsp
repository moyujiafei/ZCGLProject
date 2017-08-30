<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <title>新增校区</title>
    </head>

    <body>
        <div>
            <form id="dataForm" method="post">
                <table class="mytable">
                    <tr>
                        <td></td>
                        <td style="text-align:right;">校区名称:</td>
                        <td>
                            <input id="xqmcTextbox" name="xqmc"   /><span style="color:#FF0000;margin-left: 3px;">*</span>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">校区地址:</td>
                        <td>
                            <input id="xqdzTextbox"  name="xqdz" />
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编:</td>
                        <td>
                            <input id="ybNumberbox"  name="yb" />
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td height="10px"></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td width="140px"></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_xq">确定</a></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_xq">取消</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <script type="text/javascript">
            $(function() {
            	$("#xqmcTextbox").textbox({
            		prompt: "必填项",
            		
                    required: true,
                    validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/xqgl/checkXQByMC.do','newXQMC']", // 唯一性验证
                    invalidMessage: "校区名称已存在"
                });
                $("#xqdzTextbox").textbox({
                	height: 80,
                	multiline: true
                });
                $("#ybNumberbox").numberbox({
                    prompt: "只能输入数字",
                });
                $("#okBtn_xq").linkbutton({
                    iconCls: "icon-ok",
                    onClick: function(){
                    	$.messager.progress(); // 显示进度条
	                    $("#dataForm").form("submit", {
	                        url: getContextPath() + "/console/catalog/xqgl/insertXQ.do",
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
	                            //表单提交成功后
                                if (data == 'success') {
                                    $.messager.alert('成功', '新增成功', 'info');
                                    $("#insertXqDialog").dialog('close');
                                    $("#xqManageDatagrid").datagrid('reload');
                                } else {
                                    $.messager.alert('失败', data, 'info');
                                }
	                        }
	                    });
                    }
                });

                $("#cancelBtn_xq").linkbutton({
                    iconCls: "icon-no",
                    onClick: function(){
	                    $("#insertXqDialog").dialog("close");
                    }
                });
            });
        </script>
    </body>

</html>
