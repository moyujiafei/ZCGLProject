<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>

    <head>
        <meta charset="utf-8">
        <title></title>
    </head>

    <body>
        <form id="roleInfoForm" method="post">
            <div class="easyui-layout" style="width:497px;height:262px;">
                <div data-options="region:'south'" style="height:35px;">
                    <table>
                        <tr>
                            <td width='106px'></td>
                            <td width='100px' align="center"><a id="lbRolenfoSave" href="javascript:void(0);">保存</a></td>
                            <td width='60px'></td>
                            <td width='100px' align="center"><a id="lbRoleinfoCancel" href="javascript:void(0);">取消</a></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'west',iconCls:'icon-man',title:'角色信息',collapsible:false" style="width:240px;">
                    <table>
                        <tr height='20px'>
                            <td></td>
                        </tr>
                        <tr height='20px'>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align='right'>名 称：</td>
                            <td>
                                <input id="tbRoleinfoName" name="name" style="width:120px">
                            </td>
                            <td style="color:#FF0000">*</td>
                        </tr>
                        <tr height='20px'>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align='right'>状 态：</td>
                            <td>
                                <input id="tbRoleinfoStatus" name="tybz" style="width:120px">
                            </td>
                            <td style="color:#FF0000">*</td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'east',iconCls:'icon-more',title:'权限分配',collapsible:false" style="width:257px;">
                    <ul id="rolePrivilegeTree"></ul>
                </div>
            </div>
        </form>
        <script type="text/javascript">
            $(function() {
                $('#tbRoleinfoName').textbox({
                    required: true,
                    validType: "remote['${pageContext.servletContext.contextPath}/console/role/checkRoleName.do','newName']",
                    invalidMessage: "角色名已存在"
                });
                $('#tbRoleinfoStatus').combobox({
                    valueField: 'id',
                    textField: 'text',
                    required: true,
                    editable: false,
                    panelHeight: "auto", //自适应高度  
                    data: [{
                        id: '0',
                        text: '激活'
                    }, {
                        id: '1',
                        text: '停用'
                    }],
                    value: '0'
                });
                //加载权限分配树
                $('#rolePrivilegeTree').tree({
                    url: getContextPath() + '/console/role/roleMeunTree.do?role_id=0',
                    checkbox: true,
                    lines: true,
                });

                $("#lbRolenfoSave").linkbutton({
                    iconCls: 'icon-save',
                    onClick: function() {
                        $.messager.progress();
                        $("#roleInfoForm").form('submit', {
                            url: getContextPath() + "/console/role/insertRoleInfo.do",
                            onSubmit: function(param) {
                                var isValid = $(this).form('validate');
                                if (!isValid) {
                                    $.messager.progress('close');
                                    $.messager.alert('提示', '请按照要求填写表单', 'info');
                                    return isValid; // 返回false终止表单提交
                                }

                                var checknodes = $('#rolePrivilegeTree').tree('getChecked');
                                if (checknodes == "") {
                                    $.messager.progress('close');
                                    $.messager.alert('提示', '请至少选择一项权限', 'info');
                                    return false;
                                }
                                var privList = "";
                                for (var i = 0; i < checknodes.length; i++) {
                                    var nodesid = checknodes[i].id;
                                    privList += nodesid;
                                    if (i != checknodes.length - 1) {
                                        privList += ";";
                                    }
                                }
                                //一定要将两部分加到一起。
                                var OneNode = $('#rolePrivilegeTree').tree('getChecked', 'indeterminate');
                                if (OneNode != "") {
                                    privList += ";";
                                }
                                for (var i = 0; i < OneNode.length; i++) {
                                    var nodesid = OneNode[i].id;
                                    privList += nodesid;
                                    if (i != OneNode.length - 1) {
                                        privList += ";";
                                    }
                                }
                                param.privList = privList;

                            },
                            success: function(data) {
                                $.messager.progress('close');
                            	//表单提交成功后
                                if (data == 'success') {
                                    $.messager.alert('成功', '新增成功', 'info');
                                    $("#insertRoleDialog").dialog('close');
                                    //获取页数增加完后跳到最后一页查看  这里也相当于一次刷新操作
                                    $("#roleListDatagrid").datagrid('reload');
                                } else {
                                    $.messager.alert('失败', data, 'info');
                                }
                            }
                        });
                    }
                });

                $("#lbRoleinfoCancel").linkbutton({
                    iconCls: 'icon-cancel',
                    onClick: function() {
                        $("#insertRoleDialog").dialog('close');
                    }
                });
            });
        </script>
    </body>

</html>
