<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>申请上交资产</title>
</head>

<body>
	<div>
		<form id="dataForm" method="post">
			<table align="center" cellspacing="5" style="margin-top: 15px;">
				<tr>
					<input type="hidden" value="${zcid}" name="zcid">
					<td  valign="top">申请原因：</td>
					<td><input id="remarkTextbox" name="remark"/></td>
					<td></td>
				</tr>
			 <table>
                    <tr>
                        <td height="10px"></td>
                    </tr>
             </table>
             <table>
                <tr>
                    <td width="140px"></td>
                    <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_zcpz">确定</a></td>
                    <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_zcpz">取消</a></td>
                </tr>
             </table>
		</form>
	</div>
	<script type="text/javascript">
		$(function(){
			$("#remarkTextbox").textbox({
				width:250,
				height:150,
				multiline:true,
			});
            $("#okBtn_zcpz").linkbutton({
                iconCls: "icon-ok",
                onClick: function(){
                	$.messager.progress(); // 显示进度条
                    $("#dataForm").form("submit", {
                        url: getContextPath() + "/console/zcgl/leading/sendbackZC.do",
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
                                $.messager.alert('成功', '提交成功', 'info');
                                $("#sendbackZCDialog").dialog('close');
                                $("#leadingZCDatagrid").datagrid('reload');
                            } else {
                                $.messager.alert('失败', data, 'info');
                            }
                        }
                    });
                }
            });

            $("#cancelBtn_zcpz").linkbutton({
                iconCls: "icon-no",
                onClick: function(){
                    $("#sendbackZCDialog").dialog("close");
                }
            });
		});
	</script>
</body>
</html>