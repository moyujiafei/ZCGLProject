<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>My JSP 'updateFJUI.jsp' starting page</title>
  </head>
  
<body>
<div>
	<form id="updateFJForm" method="post">
		<table align="center" cellspacing="10px" style="margin-top:1px">
	 		<tr><td style="text-align:right;">建  筑  物：</td><td><input id="updateFJ_jzw" name="jzwid" /></td></tr>
			<tr><td style="text-align:right;">楼&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;层：</td><td><input id="updateFJ_floor" name="floor" /></td></tr>
			<tr><td style="text-align:right;">房&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</td><td><input id="updateFJ_room" name="room" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
			<tr><td style="text-align:right;">所属部门：</td><td><input id="updateFJ_deptName" name="deptName" /></td>
			</tr>
			<tr><td style="text-align:right;">管  理  人：</td><td><input id="updateFJ_glr" name="glrmc" userid=""/></td>
			</tr>
			<tr><td style="text-align:right;">停用标志：</td><td><input id="updateFJ_tybz" name="tybz" /></td></tr>
			</table>
          	<table>
          		 <tr>
              		 <td width="140px"></td>
              		 <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_fjgl">确定</a></td>
              		 <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_fjgl">取消</a></td>
           		</tr>
       		</table>
	</form>
</div>
<script type="text/javascript">
	var updateFJ = $("#fjListDatagrid").datagrid("getSelected");
	$("#updateFJ_jzw").textbox({
		width: 320,
		required:true,  
		editable : false,
		iconCls : 'icon-lock',
		value : updateFJ.jzw,
	});
	
	$("#updateFJ_floor").combobox({
		width: 320,
		required:true,  
	    url: getContextPath()+"/console/catalog/fjgl/getFloorCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable : true,
		value : updateFJ.floor,
	});
	$("#updateFJ_room").textbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : true,
		value : updateFJ.room,
		validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/fjgl/checkFJByUpdateRoom.do?jzwid="+updateFJ.jzwId+"&oldRoom="+updateFJ.room+"','newRoom']",
        invalidMessage: "房间名已存在",
	});
	$("#updateFJ_deptName").searchbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : false,
		searcher: function(value,name){
			testOpt.queryDeptName("queryDeptName", "/console/wxgl/wxdept/queryWXDeptUI.do", "查找所属部门", {isEdit: true},"updateFJ_deptName"); 
		},
		value : updateFJ.deptName,
	});
	
	$("#updateFJ_glr").searchbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : false,
		searcher: function(value,name){
			testOpt.queryWXUser("queryGLRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找管理员", {isEdit:true},"updateFJ_glr"); 
		},
		value : updateFJ.glrmc,
	});
 	$("#updateFJ_tybz").combobox({
 		width: 320,
		required:true,  
	    url: getContextPath()+"/console/catalog/fjgl/getTYBZCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable : true,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
	});
 	
	$("#okBtn_fjgl").linkbutton({    
	    iconCls: 'icon-ok',
	    onClick : function(){
	    	$.messager.progress();	
	    	$("#updateFJForm").form('submit',{
	    		url:getContextPath() + "/console/catalog/fjgl/updateFJ.do",    
    		    onSubmit: function(param){
    		    	param.id = updateFJ.fjId;
    		    	param.jzwid = updateFJ.jzwId;
    		    	var isValid = $(this).form('validate');
					if (!isValid){
						$.messager.progress('close');	
					}
					param.glr=$("#updateFJ_glr").attr("userid");
					 return isValid;
    		    },
    		    success:function(data){
    		    	$.messager.progress('close');
    		    	if(data == "success"){
    					$.messager.alert('成功','编辑房间成功','info');
    					$("#updateFJUI").dialog('close');
    					$("#fjListDatagrid").datagrid("reload");
    				} else{
    					$.messager.alert('失败',data,'waring');
    				}
    		    },
	    	});
	    },
	});  
	$("#cancelBtn_fjgl").linkbutton({    
	    iconCls: 'icon-no'   
	});  
	
	$("#cancelBtn_fjgl").click(function(){    
		$("#updateFJUI").dialog('close');
	}); 
	
	var testOpt = {
	      	  newDialog: function(dialogId,url,title,param){
	      	    var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                  href: getContextPath() + url,
	                  title: title,
	                  queryParams: param,
	                  width: 512,
	                  height: 300,
	                  collapsible: true,
	                  modal: true,
	                  draggable: true,
	                  inline: true,
	                  onClose: function() {
	                      dialogObj.remove();// 关闭时remove对话框
	                  }
	              });
	      	},
	      	queryWXUser: function(dialogId,url,title,param,searchBoxId){
	      		dialogObj = $('<div id="' + dialogId + '"></div>');
	      		wxuser=null;
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                  href: getContextPath() + url,
	                  title: title,
	                  queryParams: param,
	                  width: 512,
	                  height: 300,
	                  cache: false,
	                  collapsible: true,
	                  modal: true,
	                  draggable: true,
	                  inline: true,
	                  onClose: function() {
	                      if(wxuser!=null){
	                      	$("#"+searchBoxId).searchbox("setValue",wxuser.name);
	                      	$("#"+searchBoxId).attr("userid",wxuser.userid)
	                      }
	                      dialogObj.remove();// 关闭时remove对话框
	                  }
	              });
	      	},
	      	queryDeptName: function(dialogId,url,title,param,searchBoxId){
	      		dialogObj = $('<div id="' + dialogId + '"></div>');
	      		wxdept=null;
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                  href: getContextPath() + url,
	                  title: title,
	                  queryParams: param,
	                  width: 512,
	                  height: 300,
	                  cache: false,
	                  collapsible: true,
	                  modal: true,
	                  draggable: true,
	                  inline: true,
	                  onClose: function() {
	                      if(wxdept!=null){
	                      	$("#"+searchBoxId).searchbox("setValue",wxdept.deptName);
	                      }
	                      dialogObj.remove();// 关闭时remove对话框
	                  }
	              });
	      	}
	    };
</script>
</body>
</html>
