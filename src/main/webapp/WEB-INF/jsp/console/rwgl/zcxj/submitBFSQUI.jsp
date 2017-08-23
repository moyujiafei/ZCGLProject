<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
<title>申请报废</title>
  </head>
  
  <body>
	  <div>
            <form id="dataForm" method="post" enctype="multipart/form-data">
                <table align="center" cellspacing="10" style="margin-top: 15px;">
                    <tr>
                       
                        <td style="text-align:right;">图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:</td>
                        <td>
                            <input id="imageFilebox" type="text" name="image_upload"  />
                        </td>
                    </tr>
                    <tr>
                        
                        <td style="text-align:right;">音&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;频:</td>
                        <td>
                            <input id="voiceFilebox" type="text" name="voice_upload" />
                        </td>
                    </tr>
                     <tr>
                        
                        <td style="text-align:right;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
                        <td>
                            <input id="sqRemark"  name="sqRemark" />
                        </td>
                    </tr>
                   
                </table>
                <table>
                    <tr>
                        <td height="10px"></td>
                    </tr>
                </table>
                <table style="margin-bottom:15px">
                    <tr>
                        <td width="140px"></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn">确定</a></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn">取消</a></td>
                    </tr>
                </table>
            </form>
        </div>
   <script type="text/javascript">
  		$("#sqRemark").textbox({
  			width:320,
  			height:80,
  			multiline:true
  		});
   		$("#imageFilebox").filebox({
   			prompt: "单个文件小于1M，格式为jpg",
   		 	width:320,
		 	accept:'image/jpeg',
   			buttonText:'选择图片',
   			buttonAlign:'right'
   		});
   		
   		$("#voiceFilebox").filebox({
   			prompt: "单个文件小于2M，格式为mp3",
   		 	width:320,
   		 	accept:'audio/mpeg',
   			buttonText:'选择音频',
   			buttonAlign:'right'
   		});
   		
   		
   	 $("#okBtn").linkbutton({
         iconCls: "icon-ok",
         plain : true,
         onClick: function(){
         	$.messager.progress(); // 显示进度条
             $("#dataForm").form("submit", {
                 url: getContextPath() + "/console/rwgl/zcxj/submitBFSQ.do",
                 onSubmit: function(param) {
                  	param.zcid='${jzc.id}';
                 },
                 success: function(data) {
                     $.messager.progress('close');
                     //表单提交成功后
                     if (data == 'success') {
                         $.messager.alert('成功', '提交成功', 'info');
                         $("#submitBFSQ").dialog('close');
                         $("#tbzcxjDateGrid").datagrid('reload');
                     } else {
                         $.messager.alert('失败', data, 'info');
                     }
                 }
             });
         }
     });
   	 
   	 $("#cancelBtn").linkbutton({
         iconCls: "icon-no",
         plain : true,
         onClick: function(){
             $("#submitBFSQ").dialog("close");
         }
     });

   </script>
  </body>
</html>
