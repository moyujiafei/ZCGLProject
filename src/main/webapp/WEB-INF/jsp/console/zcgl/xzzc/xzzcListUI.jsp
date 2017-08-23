<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>闲置资产列表</title>
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
     <div id="xzzcListMain">
        <div id="divXzzcToolbar"  style="padding:5px;">
           <table align="right">
               <tr>
                  <td style="padding-left:270px;padding-right:10px">资产名称</td>
                  <td input id="inputXzzc" name="mc" style="width:140px" onClick="doSearch()"/></td>
                  <td style="padding-left:20px;padding-right:10px">资产类型</td>
                  <td input id="selectZclx" name="lxmc" style="width:125px"></td>
                  <td style="padding-left:20px;padding-right:10px">所属部门</td>
                  <td input id="selectZcbm" name="bmmc" style="width:125px"></td>
                  <td style="padding-left:10px"><a href="#" id="searchXzzcBtn">筛选</a></td>
               </tr>
           </table>
        </div>
        <table id="xzzcList"></table>
     </div>
     
     <script type="text/javascript">
         $("#xzzcListMain").dialog({
        	 title:'闲置资产列表',
        	 width:1024,
        	 height:600,
        	 closed:false,
        	 cache:false,
             shadow:true, //显示阴影
             resizable:false, //不允许改变大小
             model:true, //是否窗口化
             inline:true, //是否在父容器中，若不在会出现很多bug
         });
         
          var xzZCList={
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
							$("#selectZcbm").searchbox("setValue",wxdept.deptName);
							$("#selectZcbm").attr("deptno",wxdept.deptNo); 
						}
						dialogObj.remove();
					}
				});
			},
			query_zclx: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				czclx  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/catalog/zclxgl/queryZCLXUI.do",
					title: "资产类型查询",
					width: 512,
					height: 300,
					modal: true,
					onClose: function () {
						if(czclx != null) {
							dialogObj.remove();
							$("#selectZclx").searchbox("setValue",czclx.mc);
							$("#selectZclx").attr("lxid",czclx.lxId); 
						}
						dialogObj.remove();
					}
				});
			}
		};
         
         $("#inputXzzc").textbox({      	
         });
         
        $("#selectZclx").searchbox({
        	 editable:false,
        	 searcher: function(value,name){
        	 	xzZCList.query_zclx();
        	 }
         });
         
         $("#selectZcbm").searchbox({
        	 editable:false,
        	 searcher: function(value,name){
				xzZCList.query_dept();        	 
        	 }
         });
         
         $("#xzzcList").datagrid({       	
        	 url: getContextPath()+"/console/zcgl/xzzc/xzzcList.do",
        	 onLoadSuccess:function(data){
        		 $(".editBtn").linkbutton({
        			 plain:true,
        			 height:21,
        			 iconCls:'icon-feiwu'
        		 });
            	  $(".editBtn").tooltip({
            		  position: 'bottom',    
            		  content: '报废',
            	  });
            	  $(".tip").tooltip({ 
           		  	trackMouse: true,
                     onShow: function(){ 
                         $(this).tooltip('tip').css({   
                             width:300, 
                             height:300,
                             boxShadow: '1px 1px 3px #292929',
                         });  
                     }  
                  });
        	 },
        	 
        	method:"post",
        	width : "100%",
        	height: "92.7%",
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
   	  		toolbar : "#divJzwToolbar",
   	  		emptyMsg : "没有数据",    
   	  	    
    	    resizable: true,
        	columns:[[
        	       {
        	    	   width:"12%",
        	    	   field:"zcdm",
        	    	   title:"资产编号",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },   
        	       {
        	    	   width:"10.3%",
        	    	   field:"zc",
        	    	   title:"资产名称",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },       	       
        	       {
        	    	   width:"16.8%",
        	    	   field:"zclx",
        	    	   title:"资产类型",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },
        	       {
        	    	   width:"10%",
        	    	   field:"zjnx",
        	    	   title:"折旧年限",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },
        	       {
        	    	   width:"14%",
        	    	   field:"num",
        	    	   title:"资产数量",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },
        	       {
        	    	   width:"13%",
        	    	   field:"deptName",
        	    	   title:"所属部门",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },
        	       {
        	    	   width:"11%",
        	    	   field:"glrmc",
        	    	   title:"部门资产管理员",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       } 
        	       },
        	       {
 			    	  field: "opt1",
 			    	  title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
 			    	  width: "15%",
 			    	  formatter: function(value,row,index){
 			    		  if(value=="找不到数据"){
 			    			  return "";
 			    		  }
 			    		  return "&nbsp;&nbsp;<a href='#' class='editBtn' onclick='edit("+row.id+","+index+");'></a>";
 			    	  },
 			    	  align: "left",
 			    	  resizable: true
 			      },
        	          ]]
        	          
         });
         
         function edit(zcbmId,index){
        	zcbfInfoDiv=$("<div id=\"zcbfInfoDiv\"></div>");
     		zcbfInfoDiv.appendTo("body");
     		$("#zcbfInfoDiv").dialog({
     			title: "同意报废资产",
     			width: 512,
     			height: 300,
     			href: getContextPath()+"/console/zcgl/xzzc/agreeBFZCUI.do?id="+zcbmId,
     			closed: false,
     			cache: false,
     			modal: true,
     			onClose: function(){
     				$("#xzzcList").datagrid("reload");
     				$("#xzzcList").datagrid("selectRow",index);
     				zcbfInfoDiv.remove();
     			} 
     		});
     	};
     	
     	$("#searchXzzcBtn").linkbutton({
     		plain: true,
    		iconCls: 'icon-search',
    		onClick: function (){
    			doSearch();
    		}
    	});
     	
     	function doSearch(){
     		var zcmc= $.trim($("#inputXzzc").textbox("getValue"));
    		var zclx = $("#selectZclx").attr("lxid");
    		var deptNo  = $("#selectZcbm").attr("deptno");
    		var param ={"zcmc":zcmc,"zclx":zclx,"deptNo":deptNo};
    		$("#xzzcList").datagrid("reload",param);
    	};
     </script>
  </body>
</html>
