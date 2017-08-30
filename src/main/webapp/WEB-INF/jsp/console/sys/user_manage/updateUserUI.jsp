<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <title>当前用户信息</title>
    </head>

    <body>
        <div>
            <form id="dataForm" method="post">
                <table align="center" style="margin-top: 20px;">
                    <c:if test="${sessionScope.is_super_admin}">
                        <tr>
                            <td></td>
                            <td style="text-align:right;">企&nbsp;业&nbsp;号&nbsp;:</td>
                            <td>
                                <input id="appid_comb" name="appId" style="padding-left: 6px;width:160px;" />
                            </td>
                            <td style="color:#FF0000">*</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</td>
                        <td>
                            <input id="txbuname" style="padding-left: 6px;width:160px;" name="uname" />
                        </td>
                        <td style="color:#FF0000">*</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">用户姓名:</td>
                        <td>
                            <input id="txbname" style="padding-left: 6px;width:160px;" name="name" />
                        </td>
                        <td style="color:#FF0000">*</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色:</td>
                        <td>
                            <input id="roleInfo" name="roleId" style="padding-left: 6px;width:160px;" />
                        </td>
                        <td style="color:#FF0000">*</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="text-align:right;">微&nbsp;信&nbsp;号&nbsp;:</td>
                        <td>
                            <input id="wxUsername" name="wxUsername" style="padding-left: 6px;width:160px;" />
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
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_user">确定</a></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_user">取消</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <script type="text/javascript">
            $(function() {
                $("#txbuname").textbox({
                    iconCls: "icon-lock",
                    iconAlign: "right",
                    required: true,
                    readonly : true,
                    value:"${currUser4Edit.uname}"
                });
                $("#txbname").textbox({
                    iconCls: "icon-man",
                    iconAlign: "right",
                    prompt: '请输入真实姓名',
                    required: true,
                    value:"${currUser4Edit.name}"
                });
                $("#appid_comb").combobox({
                    url: getContextPath() + "/console/user/getCorpInfoCombo.do",
                    iconAlign: "right",
                    editable: false,
                    required: true,
                    valueField: 'id',
                    textField: 'text',
                    value:"${currUser4Edit.appId}"
                });
                $("#roleInfo").combobox({
                    url: getContextPath() + "/console/user/getRoleCombo.do",
                    iconAlign: "right",
                    valueField: 'id',
                    textField: 'text',
                    required: true,
                    editable: false,
                    value:"${currUser4Edit.roleId}"
                });
                $("#wxUsername").textbox({
                    prompt: "用户微信账号",
                    value: "${currUser4Edit.wxUsername}"
                });

                $("#okBtn_user").linkbutton({
                    iconCls: "icon-ok",
                });
                $("#okBtn_user").click(function() {
                    $.messager.progress(); // 显示进度条
                    $("#dataForm").form("submit", {
                        url: getContextPath() + "/console/user/updateUserInfo.do",
                        onSubmit: function(param) {
                        	param.uid = "${currUser4Edit.uid}";
                        	param.originalUname = "${currUser4Edit.uname}";
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
                                $.messager.alert('成功', '编辑成功', 'info');
                                $("#updateUserDialog").dialog("close");
                                $("#userListDatagrid").datagrid('reload');
                            } else {
                                $.messager.alert('失败', data, 'info');
                            }
                        }
                    });
                });
                $("#cancelBtn_user").linkbutton({
                    iconCls: "icon-no",
                });
                $("#cancelBtn_user").click(function() {
                    $("#updateUserDialog").dialog("close");
                });
            });
        </script>
    </body>

</html>
