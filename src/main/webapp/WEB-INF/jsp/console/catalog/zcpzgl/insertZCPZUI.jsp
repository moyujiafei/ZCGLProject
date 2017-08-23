<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>新建资产品种</title>
</head>

<body>
	<div>
		<form id="dataForm" method="post" enctype="multipart/form-data">
			<table class="mytable" style="margin-top:5px">
				<tr>
					<td style="text-align:right;">品种名称：</td>
					<td style="text-align:left;"><input id="pzNameTextbox" name="mc"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				</tr>
				<tr>
					<td style="text-align:right;">品种照片：</td>
					<td><input id="zcpzImageFilebox" type="text" name="image_upload" ><span
						style="color:#FF0000;margin-left: 3px;">*</span></td>
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
					<td >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td style="text-align:left;"><input id="remarkTextbox" type="text" name="remark"></td>
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
				prompt: "必填项",
				width: 280,
				required: true,
				validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/zcpzgl/checkZCPZByLXID.do','lxid']", 
                invalidMessage: "品种号已经存在"
			});
			$("#zcflCombobox").combobox({
				width: 110,
				url: getContextPath() +"/console/catalog/zcpzgl/getZCPZCombo.do",  
                valueField:'id',    
                textField:'text',
                editable:false,
                onSelect:function(record){
                	$("#zcflCombobox").val(record.id);
                },
                onLoadSuccess:function(){
                	var val = $(this).combobox("getData");
                	for(var item in val[0]){
                		if(item =="id"){
                			$(this).combobox("select",val[0][item]);
                		}
                	}
                }
            });
            $("#pzNameTextbox").textbox({
            	prompt: "必填项",
            	width: 280,
            	required: true,
            	validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/zcpzgl/checkZCPZByMC.do','mc']", // 唯一性验证
                invalidMessage: "品种名称已经存在"
            });
            $("#zjnxNumberbox").numberbox({
            	prompt : "只能输入数字",
            	width: 90,
				required: true,
				value:3,
				validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/zcpzgl/checkZCPZByZJNX.do','zjnx']", // 唯一性验证
                invalidMessage: "折旧年限必须大于0"
			});
            $("#zcpzImageFilebox").filebox({ 
				prompt: "必选项",
				required : true,
				multiple:true,
				accept:'image/jpeg',
				width:280,
			    buttonText: '选择文件', 
			    buttonAlign: 'right' 
			}),
            $("#remarkTextbox").textbox({
            	width: 280,
            	height:45,
            	multiline:true
			});
            $("#okBtn_zcpz").linkbutton({
                iconCls: "icon-ok",
                onClick: function(){
                	$.messager.progress(); // 显示进度条
                    $("#dataForm").form("submit", {
                        url: getContextPath() + "/console/catalog/zcpzgl/insertZCPZ.do",
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
                                $.messager.alert('成功', '添加成功', 'info');
                                $("#insertZCPZDialog").dialog('close');
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
                    $("#insertZCPZDialog").dialog("close");
                }
            });
		});
	</script>
</body>
</html>