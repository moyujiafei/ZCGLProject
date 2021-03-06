<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>申请闲置资产列表</title>
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
     <div id="sqxzListMain" class="easyui-layout">
        <div id="divsqxzToolbar" style="width: 100%;height:30px;padding-top: 5px;">
          <form id="vzcSQXZ_Form" method="post">
           <table align="right">
               <tr>
                  <td style="padding-left:270px;padding-right:10px">资产名称</td>
                  <td input id="inputSqxz" name="mc" style="width:140px" onClick="doSearch()"/></td>
                  <td style="padding-left:20px;padding-right:10px">资产类型</td>
                  <td input id="selectZclx" name="lxmc" style="width:125px"></td>
                  <td style="padding-left:20px;padding-right:10px">所属部门</td>
                  <td input id="selectZcbm" name="bmmc" style="width:125px"></td>
                  <td style="padding-left:10px"><a href="#" id="searchSqxzBtn" plain="true">筛选</a></td>
               </tr>
            </table>
           </form>
        </div>
        <table id="sqxzList"></table>
        <div data-options="region:'south'" style="height:35px;">
              <table align="center">
                    <tr>
                        <td width="30%"/>
                        <td width='10%' align="center"><a id="confirm_SQXZ" href="#">同意</a></td>
                        <td width='20%'></td>
                        <td width='10%' align="center"><a id="refuse_SQXZ" href="#">拒绝</a></td>
                        <td></td>
                     </tr>
              </table>
         </div>
     </div>
     
     <script type="text/javascript">
         
         $("#sqxzListMain").dialog({
        	 title:'申请闲置资产列表',
        	 width:1024,
        	 height:600,
        	 closed:false,
        	 cache:false,
             shadow:true, //显示阴影
             resizable:false, //不允许改变大小
             model:true, //是否窗口化
             inline:true, //是否在父容器中，若不在会出现很多bug
         });
         
         var xzsqZCList={
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
         
         $("#inputSqxz").textbox({      	
         });
         
         $("#selectZclx").searchbox({
        	 editable:false,
        	 searcher: function(value,name){
        	 	xzsqZCList.query_zclx();
        	 }
         });
         
         $("#selectZcbm").searchbox({
        	 editable:false,
        	 searcher: function(value,name){
        	 	xzsqZCList.query_dept();
        	 }
         });
         
         $("#sqxzList").datagrid({       	
        	 url: getContextPath()+"/console/zcgl/xzsq/xzsqList.do",
        	 onLoadSuccess:function(data){
        		 $(".editBtn").linkbutton({
        			 plain:true,
        			 height:21,
        			 iconCls:'icon-edit'
        		 });
        	 },
        	idField: "zcdm",
        	method:"post",
        	width : "100%",
        	height: "94%",
 	        pagination : true, //是否有分页工具
   	  		pagePosition : "bottom", //分页工具位置
   	  		pageSize : 15, //分页默认大小
   	  		pageList : [ 15 ], //页面选择大小
   	  		striped : true, //斑马线样式,
   	  		nowrap : true, //是否在一行显示所有，自动换行
   	  		loadMsg : "努力加载中，请稍后。。。。",//加载信息
   	  		rownumbers : true, //是否显示行号
   	  		singleSelect : true, //只能同时选择一行
   	  		showHeader : true,//显示行头，默认true;
   	  		toolbar : "#divsqxzToolbar",
   	  		emptyMsg : "没有数据",    
   	  	    align: "center",
    	    resizable: true,
    	    selectOnCheck: false,
			checkOnSelect: false,
			rownumbers: true,
			singleSelect: true,
    	    
        	columns:[[
                    {
	                    title: "复选框",
	                    field: "fxk",
	                    align: "center",
	                    checkbox: true,	
	                    formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       }                                        
                   },
        	       {
        	    	   width:"13%",
        	    	   field:"zcdm",
        	    	   title:"资产编号",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
        	       },   
        	       {
        	    	   width:"14%",
        	    	   field:"zc",
        	    	   title:"资产名称",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
        	       },       	       
        	       {
        	    	   width:"15%",
        	    	   field:"zclx",
        	    	   title:"资产类型",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
        	       },
        	       {
        	    	   width:"15%",
        	    	   field:"zjnx",
        	    	   title:"折旧年限",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
        	       },
        	       {
        	    	   width:"15%",
        	    	   field:"num",
        	    	   title:"资产数量",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
        	       },
        	       {
        	    	   width:"15%",
        	    	   field:"deptName",
        	    	   title:"所属部门",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                            if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
        	       },
        	       {
        	    	   width:"12.1%",
        	    	   field:"glrmc",
        	    	   title:"部门资产管理员",
        	    	   halign:"center",
        	    	   align:"center",
        	    	   resuzable:true,
        	    	   formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
        	       },       	     
        	          ]],
        	      onLoadSuccess: function () {
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
        	          
         });
         
         
         
         
         
         $("#confirm_SQXZ").linkbutton({
        	 plain:true,
        	 iconCls:"icon-ok",
         });
         
         var arr=$("#sqxzList").datagrid("getChecked");
         $("#confirm_SQXZ").click(function(){     	             	    
        		if(arr.length==0){      				
        				$.messager.alert("提示","请至少选中一个申请闲置的资产",'info');        				
        		}else{
        				  sqxzInfoDiv=$("<div id=\"sqxzInfoDiv\"></div>");
        	        	  sqxzInfoDiv.appendTo("body");
        	      		  $("#sqxzInfoDiv").dialog({
        	      	      title: "同意资产闲置",
        	      		  width: 512,
        	      		  height: 300,
        	      		  href: getContextPath()+"/console/zcgl/xzsq/agreeXZSQUI.do",
        	      	      closed: false,
        	      		  cache: false,
        	      		  modal: true,       	      		  
        	      		  });
        			   }
        		     
        	     });
         
         $("#refuse_SQXZ").linkbutton({
        	 plain: true,
        	 iconCls: "icon-no",
         });
         
       
         $("#refuse_SQXZ").click(function(){     	         	    	    
     		if(arr.length==0){      				
     			$.messager.alert("提示","请至少选中一个申请闲置的资产",'info');        				
     		}else{
     				  jjxzInfoDiv=$("<div id=\"jjxzInfoDiv\"></div>");
     				  jjxzInfoDiv.appendTo("body");
     	      		  $("#jjxzInfoDiv").dialog({
     	      	      title: "拒绝资产闲置",
     	      		  width: 512,
     	      		  height: 300,
     	      		  href: getContextPath()+"/console/zcgl/xzsq/refuseXZSQUI.do",
     	      	      closed: false,
     	      		  cache: false,
     	      		  modal: true,     	      		 
     	      		  });
     			   }
     	     });
                          	
     	$("#searchSqxzBtn").linkbutton({
    		iconCls: 'icon-search',
    		onClick: function (){
    			doSearch();
    		}
    	});
     	
     	function doSearch(){
     		var zcmc= $.trim($("#inputSqxz").textbox("getValue"));
    		var zclx = $("#selectZclx").attr("lxid");
    		var deptNo  = $("#selectZcbm").attr("deptno");
    		var param ={"zcmc":zcmc,"zclx":zclx,"deptNo":deptNo};
    		$("#sqxzList").datagrid("reload",param);
    	};
     </script>
  </body>
</html>
