<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>编辑易耗品品种</title>
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
					<td style="text-align:left;"><input id="yhpflCombobox" name="lxpid"></td>
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
				value:"${currYHPLXInfo.lxId}",
				readonly: true,
				iconCls:'icon-lock'
			});
			$("#yhpflCombobox").combobox({
				width: 280,
				value:"${currYHPLXInfo.lxPid}",
				url: getContextPath() +"/console/catalog/yhppzgl/getYHPPZCombo.do",  
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
            	value:"${currYHPLXInfo.mc}",
            	required: true,
            	validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/yhppzgl/checkYHPPZByUpdateMC.do?oldMC=${currYHPLXInfo.mc}','newMC']", // 唯一性验证
                invalidMessage: "品种名称已经存在"
            });
            $("#remarkTextbox").textbox({
            	width: 280,
            	height:45,
            	value:"${currYHPLXInfo.remark}",
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
                        url: getContextPath() + "/console/catalog/yhppzgl/updateYHPPZ.do",
                        onSubmit: function(param) {
							param.id = "${currYHPLXInfo.id}";
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
                                $("#updateYHPPZDialog").dialog('close');
                                $("#yhppzListDatagrid").datagrid('reload');
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
                    $("#updateYHPPZDialog").dialog("close");
                }
            });
		});
	</script>
</body>
</html>