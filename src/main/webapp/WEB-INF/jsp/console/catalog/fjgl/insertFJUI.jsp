<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>新建房间</title>
  </head>
  
  <body>
  <div>
  	<form id="insertFJForm" method="post">
  		<table align="center" cellspacing="5px" style="margin-top:1px">
  			<tr><td style="text-align:right;">校&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区：</td><td><input id="insertFJ_xqmc" name="xqid" /></td></tr>
  			<tr><td style="text-align:right;">建  筑  物：</td><td><input id="insertFJ_jzw" name="jzwid" /></td></tr>
  			<tr><td style="text-align:right;">楼&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;层：</td><td><input id="insertFJ_floor" name="floor" /></td></tr>
  			<tr><td style="text-align:right;">房&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</td><td><input id="insertFJ_room" name="room" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
  			<tr><td style="text-align:right;">所属部门：</td><td><input id="insertFJ_deptName" name="deptName" /><span style="color:#FF0000;margin-left: 3px;">*</span></td>
  			</tr>
  			<tr><td style="text-align:right;">管 理 人：</td><td><input id="insertFJ_glr" name="glrmc" userid="" /><span style="color:#FF0000;margin-left: 3px;">*</span></td>
  			</tr>
  			<tr><td style="text-align:right;">停用标志：</td><td><input id="insertFJ_tybz" name="tybz" /></td></tr>
  		</table>
       <table>
           <tr>
               <td width="140px"></td>
               <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_fj">确定</a></td>
               <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_fj">取消</a></td>
           </tr>
       </table>
  	</form>
  </div>
 <script type="text/javascript">
 	$("#insertFJ_xqmc").combobox({
 		width: 320,
	    required:true,  
	    url: getContextPath()+"/console/catalog/fjgl/getXQCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
	 	onSelect: function(rec){    
	 		var url = getContextPath()+"/console/catalog/fjgl/getJZWCombo.do?xqid="+rec.id;    
	        $('#insertFJ_jzw').combobox('reload',url);  
	    },
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		},
 	});
 	$("#insertFJ_jzw").combobox({
 		width: 320,
 		required:true,  
 	    valueField: 'id',
 		textField: 'text',
 		editable: false,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		},
 	});
 	
 	$("#insertFJ_floor").combobox({
 		width: 320,
		required:true,  
	    url: getContextPath()+"/console/catalog/fjgl/getFloorCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable : true,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
	});
	$("#insertFJ_room").textbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : true
	});
	$("#insertFJ_room").textbox("textbox").blur(function(){
		var jzwid = $("#insertFJ_jzw").combobox("getValue");
		var room = $.trim($("#insertFJ_room").textbox("getValue"));
		$.messager.progress(); // 显示进度条
        $.ajax({
            url: getContextPath() + "/console/catalog/fjgl/checkFJByRoom.do",
            cache: false,
            async: true,
            data: {
                "jzwid": jzwid,
                "room": room
            },
            type: "post",
            dataType: "json",// 返回类型必须为json
            success: function(result) {
                $.messager.progress('close');
                if (!result) {
                    $.messager.alert('失败', '房间名重复');
                }
            },
            error: function() {
                $.messager.progress('close');
            }
        });
	});
	$("#insertFJ_deptName").searchbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : false,
		searcher: function(value,name){
			testOpt.queryDeptName("queryDeptName", "/console/wxgl/wxdept/queryWXDeptUI.do", "查找所属部门", {isEdit: true},"insertFJ_deptName"); 
		}
	});
	
	
	$("#insertFJ_glr").searchbox({
		prompt: "必填项",
		width: 320,
		required:true,  
		editable : false,
		searcher: function(value,name){
			testOpt.queryWXUser("queryGLRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找管理员", {isEdit: true},"insertFJ_glr"); 
		}
	});
	
 	$("#insertFJ_tybz").combobox({
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
	$("#okBtn_fj").linkbutton({    
	    iconCls: 'icon-ok',
	    onClick : function(){
	    	$.messager.progress();	// 显示进度条
	    	$("#insertFJForm").form('submit',{
	    		url:getContextPath() + "/console/catalog/fjgl/insertFJ.do", 
    		    onSubmit: function(param){
    		    	var isValid = $(this).form('validate');
					if (!isValid){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
					}
					 param.glr=$("#insertFJ_glr").attr("userid");
					 return isValid;
    		    },
    		    success:function(data){
    		    	$.messager.progress('close');
    		    	if(data == "success"){
    					$.messager.alert('成功','新增房间成功','info');
    					$("#insertFJUI").dialog('close');
    					$("#fjListDatagrid").datagrid("reload");
    				} else{
    					$.messager.alert('失败',data,'waring');
    				}
    		    },
	    	});
	    },
	});  
	$("#cancelBtn_fj").linkbutton({    
	    iconCls: 'icon-no'   
	});  
	
	$("#cancelBtn_fj").click(function(){    
		$("#insertFJUI").dialog('close');
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
                      	$("#"+searchBoxId).attr("userid",wxuser.userid);
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
