<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>编辑资产品种</title>
</head>

<body>
	<div>
		<form id="dataForm" method="post" enctype="multipart/form-data">
			<table class="mytable" style="margin-top:5px">
				<tr>
					<td style="text-align:right;">品 种 号：</td>
					<td><input id="pzNumTextbox" name="lxid"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">品种名称：</td>
					<td><input id="pzNameTextbox" name="mc"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				</tr>
				<tr>
					<td style="text-align:right;">品种照片：</td>
					<td><input id="zcpzImageFilebox" type="text" name="image_upload" ></td>
				</tr>
				<tr>
					<td style="text-align:right;">资产分类：</td>
					<td>
						<table>
							<tr>
								<td style="text-align:left;"><input id="zcflCombobox" name="lxpid"></td>
								<td  style="padding-left:10px">折旧年限：</td>
								<td><input type="text" id="zjnxNumberbox" name="zjnx"/><span style="color:#FF0000;margin-left: 3px;">*</span> 年</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td><input id="remarkTextbox" type="text" name="remark"></td>
				</tr>
			</table>
             <table >
				<tr>
					<td width="140px"></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="okBtn_zcpz">确定</a></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="cancelBtn_zcpz">取消</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(function(){
			$("#pzNumTextbox").textbox({
				width: 280,
				value:"${currZCLXInfo.lxId}",
				readonly: true,
				iconCls:'icon-lock'
			});
			$("#zcflCombobox").combobox({
				width: 110,
				value:"${currZCLXInfo.lxPid}",
				url: getContextPath() +"/console/catalog/zcpzgl/getZCPZCombo.do",  
                valueField:'id',    
                textField:'text',
                editable:false,
                onSelect:function(record){
                	$("#flmcCombox").val(record.id);
                }
			}); 
            $("#pzNameTextbox").textbox({
            	prompt: "必填项",
            	width: 280,
            	value:"${currZCLXInfo.mc}",
            	required: true,
            	validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/zcpzgl/checkZCPZByUpdateMC.do?oldMC=${currZCLXInfo.mc}','newMC']", // 唯一性验证
                invalidMessage: "品种名称已经存在"
            });
            $("#zjnxNumberbox").numberbox({
            	width: 90,
            	prompt : "只能输入数字",
            	value:"${currZCLXInfo.zjnx}",
				required: true,
				validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/zcpzgl/checkZCPZByZJNX.do','zjnx']", 
                invalidMessage: "折旧年限必须大于0"
			});
            $("#remarkTextbox").textbox({
            	width: 280,
            	height:45,
            	value:"${currZCLXInfo.remark}",
            	multiline:true
			});
            $("#zcpzImageFilebox").filebox({ 
				required : false,
				multiple:true,
				accept:'image/jpeg',
				width:280,
			    buttonText: '选择文件', 
			    buttonAlign: 'right' 
			}),
            $("#okBtn_zcpz").linkbutton({
                iconCls: "icon-ok",
                onClick: function(){
                	$.messager.progress(); // 显示进度条
                    $("#dataForm").form("submit", {
                        url: getContextPath() + "/console/catalog/zcpzgl/updateZCPZ.do",
                        onSubmit: function(param) {
							param.id = "${currZCLXInfo.id}";
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
                                $.messager.alert('成功', '修改成功', 'info');
                                $("#updateZCPZDialog").dialog('close');
                                $("#zcpzListDatagrid").datagrid('reload');
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
                    $("#updateZCPZDialog").dialog("close");
                }
            });
		});
	</script>
</body>
</html>