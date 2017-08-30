<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>消息通告</title>
</head>

<body>
	<div id="sendMsgDialog"> 
		<form id="dataForm" method="post" >
			<table class="mytable" style="margin-top:30px">
				<tr>
					<td >选择部门：</td>
					<td>
						<table>
							<tr>
								<td ><input id="WXDeptsearchbox" name="deptName" /></td>
								<td style="padding-left:10px">选择标签：</td>
								<td ><input id="WXTagCombobox" name="tagNo"/></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td >发送内容：</td>
					<td ><input id="Msg" name="nr" /><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				</tr>
			</table >
            <table style="margin-top:30px">
				<tr>
					<td width="140px"></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="okBtn_sendMsg">发送</a></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="cancelBtn_sendMsg">关闭</a></td>
				</tr>
			</table>
			</div>
		</form>
	
	<script type="text/javascript">
	$(function(){
		$("#sendMsgDialog").dialog({
			title: '消息通告',
			width: 512,
			height: 300,
			closed: false,
			cache: false,
			shadow: true,
			modal: true,
			inline: true,
		});
		
		var sendMsgUIOPt={
			query_dept: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				wxdept  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/wxgl/wxdept/queryWXDeptUI.do",
					title: "资产管理部门查询",
					width: 512,
					height: 300,
					modal: true,
					queryParams:{isEdit: true},
					onClose: function () {
						if(wxdept != null) {
							dialogObj.remove();
							$("#WXDeptsearchbox").searchbox("setValue",wxdept.deptName);
							$("#WXDeptsearchbox").attr("deptno",wxdept.deptNo); 
						}
						dialogObj.remove();
					}
				});
			}
		};
		
		$("#WXDeptsearchbox").searchbox({  
			width:120,
		    editable:false,
		    searcher: function(value,name){
		    	sendMsgUIOPt.query_dept();
		    }
		});
		$("#WXTagCombobox").combobox({   
			width:120,
		    url: getContextPath() +"/console/rwgl/msg/getWXTagCombo.do",    
		    valueField: 'id',
		    textField: 'text',
		    editable:false,
		    
		}); 
		$("#Msg").textbox({
			required: true,
			prompt: "必填项",
        	height:80,
        	multiline:true,
		});
		$("#okBtn_sendMsg").linkbutton({
            iconCls: "icon-ok",
            onClick: function(){
            	$.messager.progress(); // 显示进度条
                $("#dataForm").form("submit", {
                    url: getContextPath() + "/console/rwgl/msg/sendMsg.do",
                    onSubmit: function(param) {
                        var valid = $(this).form("validate");
                        if (!valid) {
                            $.messager.progress('close');
                            $.messager.alert("提示", "请按照要求填写表单", "info");
                        }
                        param.deptNo=$("#WXDeptsearchbox").attr("deptno");
                        return valid;
                    },
                    success: function(data) {
                        $.messager.progress('close');
                        //表单提交成功后
                        if (data == 'success') {
                            $.messager.alert('成功', '发送成功', 'info');                            
                        } else {
                            $.messager.alert('失败', data, 'info');
                        }
                    }
                });
            }
        });
        $("#cancelBtn_sendMsg").linkbutton({
            iconCls: "icon-no",
            onClick: function(){
                $("#sendMsgDialog").dialog("close");
            }
        });
	});
	</script>
</body>
</html>