<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<div id="dlgBMZCListUI">
		<div id="tbBMZCListUI" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">资产名称：</div><div style="float:left;padding-right: 10px;"><input id="BMZCListUI_txbZcmc" name="zcmc" style="width: 150px;"/></div>
				<div style="float:left;line-height: 25px;">资产类型：</div><div style="float:left;padding-right: 10px;"><input id="BMZCListUI_searchbZclx" name="zclx" style="width: 150px;"/></div>
				<div style="float:left;line-height: 25px;">使用人：</div><div style="float:left;padding-right: 10px;"><input id="BMZCListUI_txbSyr" name="syr" style="width: 150px;"/></div>
				<div style="float:left;line-height: 25px;">资产状态：</div><div style="float:left;padding-right: 10px;"><input id="BMZCListUI_cmbZczt" name="zczt" style="width: 150px;"/></div>
				<div style="float:left;padding-right: 10px;"><a id="BMZCList_search" href="#" onclick="BMZCListUI.query()">筛选</a></div>
			</div>
		</div>
		<div id="dgBMZCListUI"></div>		
	</div>
<script>

 var BMZCListUI = {
			
		query: function () {
			
			var zcmc = $("#BMZCListUI_txbZcmc").val();
			var zclx= $("#BMZCListUI_searchbZclx").attr("lxid");
			var zczt= $("#BMZCListUI_cmbZczt").val();
			var syr=$("#BMZCListUI_txbSyr").attr("userid");
			var url=getContextPath () + "/console/zcpd/zccx/queryBMZCList.do";
			$("#dgBMZCListUI").datagrid("options").queryParams={"zc":zcmc,"zclx":zclx,"zczt":zczt,"syr":syr};
			$("#dgBMZCListUI").datagrid("reload",url);
		},
		
		detailZC: function (index) {
			var row = $("#dgBMZCListUI").datagrid("getData").rows[index];
			var newDetailZC = $("<div id='newDetailZC'></div>");
			newDetailZC.appendTo("#center");
			$("#newDetailZC").dialog({
				href: getContextPath() + "/console/zcpd/zccx/detailZCUI.do?zcid="+row.zcid,
				title: "资产详情",
				width: 1024,
				height: 600,
				cache: false,
		        shadow: true, // 显示阴影
		        resizable: false, // 不允许改变大小
		        modal: true, // 模式化窗口，锁定父窗口
		        inline: true, // 是否在父容器中，若不在会出现很多BUG
				onLoad: function () {
					if ($("#ztButton0").length > 0) {
						$("#ztButton0").click();
					}
				}
			});
		},
		querySyr: function () {
			wxuser=null;
			dialogObj = $("<div id='dialogObj'></div>");
			dialogObj.appendTo("body");
			$("#dialogObj").dialog({
				href: getContextPath() + "/console/wxgl/wxuser/queryWXUserUI.do",
				title: "资产使用人查询",
				width: 512,
				height: 300,
				modal: true,
				onClose: function () {
					if(wxuser!=null){
						$("#BMZCListUI_txbSyr").searchbox("setValue",wxuser.name); 
						$("#BMZCListUI_txbSyr").attr("userid",wxuser.userid);
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
							$("#BMZCListUI_searchbZclx").searchbox("setValue",czclx.mc);
							$("#BMZCListUI_searchbZclx").attr("lxid",czclx.lxId); 
						}
						dialogObj.remove();
					}
				});
			}

	};
	
	$("#BMZCList_search").linkbutton({
		iconCls : 'icon-search',
		plain : true
	});

	$("#dlgBMZCListUI").dialog({
		title: "部门资产列表",
		width: 1024,
		height: 600,
        cache: false,
        shadow: true, // 显示阴影
        resizable: false, // 不允许改变大小
        modal: true, // 模式化窗口，锁定父窗口
        inline: true, // 是否在父容器中，若不在会出现很多BUG
	});
	
	$("#BMZCListUI_txbZcmc").textbox({});
	
	$("#BMZCListUI_cmbZczt").combobox({
		url:getContextPath() + '/console/zcpd/zccx/getZCZTComboWithAll.do',    
	    valueField:'id',    
	    textField:'text' ,
	    onLoadSuccess: function () {
	    	var data = $(this).combobox("getData");
	    	$(this).combobox("select",data[0].id);
	    }
	});
	
	$("#BMZCListUI_searchbZclx").searchbox({
		eaditable: false,
		searcher: function(value,name){
			BMZCListUI.query_zclx();
		}
	});
	
	$("#BMZCListUI_txbSyr").searchbox({
		editable: false,
		searcher: function(value,name){
			BMZCListUI.querySyr();
		}
	});
	
	$("#dgBMZCListUI").datagrid({
		url: getContextPath() + "/console/zcpd/zccx/getBMZCList.do",
		fit: true,
        singleSelect: true,
        pagination: true,
        striped: true,
        rownumbers: true,
        toolbar: "#tbBMZCListUI",
        pageSize: 15,
        pageList: [15],
        emptyMsg : "没有数据",
        columns: [
                  [{
                      field: 'zcdm',
                      title: '资产编号',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }
                  }, {
                      field: 'zc',
                      title: '资产名称',
                      align: 'center',
                      width: '21%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }
                  }, {
                      field: 'zclx',
                      title: '资产类型',
                      align: 'center',
                      width: '16%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }
                  }, {
                      field: 'zjnx',
                      title: '折旧年限',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }

                  }, {
                      field: 'num',
                      title: '资产数量',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }

                  }, {
                      field: 'syrmc',
                      title: '使用人',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   if(value==null){
	                   	value="";
	                   }
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }
                      

                  },{
                      field: 'zcztmc',
                      title: '资产状态',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){ 
	                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
	                   return content;  
	             	  }

                  }, {
                      field: 'opt',
                      title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作',
                      align: 'left',
                      width: '15%',
                      resizable: false,
                      formatter:function (value,row,index) {
                    	  
                    	return "&nbsp;&nbsp;<a class='BMZCListUI_detailZcBtn' onclick='BMZCListUI.detailZC("+index+")' href='#'></a>";
                    	
                      }
                  }]
              ],
              onLoadSuccess: function () {
            	  
            	  $(".BMZCListUI_detailZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-detail",
            		  plain : true,
            	  });
            	  $(".BMZCListUI_detailZcBtn").tooltip({
            	  		content:'详情',
            	  		position : 'top',
	 					trackMouse: true
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
	});
	
</script>
</body>
</html>
