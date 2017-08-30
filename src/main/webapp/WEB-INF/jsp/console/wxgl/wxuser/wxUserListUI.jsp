<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>微信用户管理</title>
    <style type="text/css">
        body{
           margin-left:0px;             
           margin-top:0px;
           margin-right:0px;
           margin-bottom:0px;
        }
    </style>
  </head>
  
  <body>
     <div id="wxuserListMain" class="easyui-layout">
        <div id="divsqrevertToolbar" style="width: 100%;height:30px;padding-top: 5px;">
          <form id="vzcSQREVERT_Form" method="post">
           <table align="right">
               <tr>
                  <td style="padding-left:20px;padding-right:10px">所属部门</td>
                  <td input id="selectBm" name="bm" style="width:110px"></td>
                  <td style="padding-left:20px;padding-right:10px">标签</td>
                  <td input id="selectBq" name="bq" style="width:130px"></td>
                  <td style="padding-left:10px"><a href="#" id="searchSqrevertBtn">筛选</a></td>
               </tr>
            </table>
           </form>
        </div>
        <table id="wxuserList"></table>
          
      </div>
    
     

     <script type="text/javascript">
     $.ajax({
  	   url: getContextPath()+"/console/wxgl/wxuser/getWXUserList.do",
  	   data: {"page":1,"rows":15},
  	   success: function (result) {
  		   JSON.stringify(result);
  	   },
  	   erro: function () {
  		   alert("Ajax error!");
  	   }
     });
         $("#wxuserListMain").dialog({
        	 title:'微信用户列表',
        	 width:1024,
        	 height:600,
        	 closed:false,
        	 cache:false,
             shadow:true, //显示阴影
             resizable:false, //不允许改变大小
             model:true, //是否窗口化
             inline:true, //是否在父容器中，若不在会出现很多bug
         });
         
         var wxUserListObj={
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
					onClose: function () {
						if(wxdept != null) {
							dialogObj.remove();
							$("#selectBm").searchbox("setValue",wxdept.deptName);
							$("#selectBm").attr("deptno",wxdept.deptNo); 
						}
						dialogObj.remove();
					}
				});
			}
         };
         
         $("#selectBm").searchbox({
        	 editable:false,
			 searcher: function(value,name){
			 	wxUserListObj.query_dept();
			 }
         });
         
         $("#selectBq").combobox({
        	 required:true,
        	 url: getContextPath()+"/console/wxgl/wxuser/getWXTagComboWithAll.do",
        	 valueField:'id',
        	 textField:'text',
        	 editable:false,
        	 panelHeight:125,
        	 onLoadSuccess:function(){
        		 var data=$(this).combobox("getData");
        		 $(this).combobox("select",data[0].id);
        	 }
         });
         
         $("#wxuserList").datagrid({       	
        	 url: getContextPath()+"/console/wxgl/wxuser/getWXUserList.do",
        	idField: "zcdm",
        	method:"post",
        	width : "100%",
        	height: "100%",
 	        pagination : true, //是否有分页工具
   	  		pagePosition : "bottom", //分页工具位置
   	  		pageSize : 15, //分页默认大小
   	  		pageList : [15], //页面选择大小
   	  		striped : true, //斑马线样式,
   	  		nowrap : true, //是否在一行显示所有，自动换行
   	  		loadMsg : "努力加载中，请稍后。。。。",//加载信息
   	  		rownumbers : true, //是否显示行号
   	  		singleSelect : true, //只能同时选择一行
   	  		showHeader : true,//显示行头，默认true;
   	  		toolbar : "#divsqrevertToolbar",
   	  		emptyMsg : "没有数据",    
   	  	    align: "center",
    	    resizable: true,
    	    selectOnCheck: false,
			checkOnSelect: false,
			rownumbers: true,
			singleSelect: true,
        	columns:[[
        	       {
        	    	   width:"9%",
        	    	   field:"name",
        	    	   title:"姓名",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	       },   
        	       {
        	    	   width:"12%",
        	    	   field:"userid",
        	    	   title:"微信号",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	       },       	       
        	       {
        	    	   width:"9%",
        	    	   field:"sex",
        	    	   title:"性别",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	       },
        	       {
        	    	   width:"17%",
        	    	   field:"mobile",
        	    	   title:"电话",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	       },
        	       {
        	    	   width:"15%",
        	    	   field:"status",
        	    	   title:"当前状态",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	       },
        	       {
        	    	   width:"15%",
        	    	   field:"deptList",
        	    	   title:"所属部门",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   formatter:function(value,row,index){
        	    		   if(row.deptList){
        	    			   var dept = "";
        	    			   for(var i=0;i<value.length;i++){
        	    				   dept = dept+"  "+value[i].deptName;
        	    			   }
        	    			   return dept;
        	    		   }else{
        	    			   return value;
        	    		   }
        	    	   },
        	    	   resuzable:true,
        	       },
        	       {
        	    	   width:"25%",
        	    	   field:"tagList",
        	    	   title:"标签",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   formatter:function(value,row,index){
        	    		   if(value){
        	    			   var tagn = "";
        	    			   for(var i=0;i<value.length;i++){
        	    				   if(i==0){
        	    					   tagn=value[i].tagName;
        	    				   }else{
        	    					   tagn = tagn+"、"+value[i].tagName;
        	    				   }
        	    				   
        	    			   }
        	    			   
        	    			   return tagn;
        	    		   }else{
        	    			   return value;
        	    		   }
        	    	   },
        	    	   resuzable:true,
        	       },
        	          ]],
         });
         

     	$("#searchSqrevertBtn").linkbutton({
     		plain: true,
    		iconCls: 'icon-search',
    		onClick: function (){
    			doSearch();
    		}
    	});
     	
     	function doSearch(){
    		var bm = $("#selectBm").attr("deptno");
    		var bq  = $("#selectBq").combobox("getValue");
    		var param ={"deptNo":bm,"tagNo":bq};
    		$("#wxuserList").datagrid("reload",param);
    	};
     </script>
  </body>
</html>
