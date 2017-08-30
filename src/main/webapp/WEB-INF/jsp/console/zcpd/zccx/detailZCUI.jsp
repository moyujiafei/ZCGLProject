<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<div id="detailZCUI" class="easyui-layout" fit="true">   
    <div data-options="region:'north'" style="height:80px;">
      <table align="center">
      	<tr><td style="height: 5px;"></td></tr>
        <tr>
          <td style="text-align: right;">资产编号:</td><td><input value="${zc.zcdm}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">资产名称:</td><td><input value="${zc.zc}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">资产类型:</td><td><input value="${zc.zclx}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">资产状态:</td><td><input id="detailZCUI_txbZczt" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">使用人:</td><td><input value="${zc.syrmc}" class="easyui-textbox" data-options="editable:false,width:100,iconCls:'icon-lock'" style="width: 130px;"/></td>
        </tr>
        <tr>
          <td style="text-align: right;">存放地点:</td><td><input value="${zc.cfdd}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">购置时间:</td><td><input id="detailZCUI_txbGzsj" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">规格型号:</td><td><input value="${zc.xh}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">出厂编号:</td><td><input value="${zc.ccbh}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 136px;"/></td>
          <td style="text-align: right;padding-left: 10px;">数量:</td><td><input value="${zc.num}" class="easyui-textbox" data-options="editable:false,iconCls:'icon-lock'" style="width: 130px;"/></td>
        </tr>
      </table>
    </div>   
    <div data-options="region:'south'" style="height:32px;">
      <div style="text-align: center; padding-top: 2px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="detailZCUI.cancel()">关闭</a>
      </div>
    </div>  
    <div data-options="region:'west'" style="width:180px;">
      <table id="detailZCUI_zttable" align="center">
        <c:forEach items="${ztObj.ztList}" var="zt" varStatus="ztStatus">
        <tr>
          <td><a href="#" id="ztButton${ztStatus.index}" class="easyui-linkbutton" data-options="plain:true,width: 140" onclick="detailZCUI.changeCenter(${zt.id})"></a></td>
        </tr>
        </c:forEach>
      </table>
    </div>  
    <div id="detailZCUI_menuCenter" data-options="region:'center'" >
    </div>   
</div> 
<script>
	function FormatDate(date){
		
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		
	}
	function FormatZczt (zczt) {
		 if(zczt == 0) {
   		  return "未使用";
	   	  }
	   	  if(zczt == 1) {
	   		  return "使用中";
	   	  }
	   	  if(zczt == 2) {
	   		  return "维修中";
	   	  }
	   	  if(zczt == 3) {
	   		  return "闲置";
	   	  }
	   	  if(zczt == 4) {
	   		  return "申请维修";
	   	  }
	   	  if(zczt == 5) {
	   		  return "申请报废";
	   	  }
	   	  if(zczt == 6) {
	   		  return "申请闲置";
	   	  }
	   	  if(zczt == 7) {
	   		  return "巡检中";
	   	  }
	   	  if(zczt == 8) {
	   		  return "报废";
	   	  }
	   	  if(zczt == 9) {
	   		  return "已登记";
	   	  }
	   	  if (zczt == 10) {
	   		  return "领用中";
	   	  }
	   	  if (zczt == 11) {
	   		  return "归还中";
	   	  }
	   	  if (zczt == 12) {
	   		  return "上交中";
	   	  }
	   	  if (zczt == 13) {
	   		  return "未登记";
	   	  }
	}
	var tables = $("#detailZCUI_zttable");
	var ztList = ${ztObj.ztList};
	for (var i = 0;i<ztList.length;i++) {
		var jlsj=FormatDate(new Date(ztList[i].jlsj));
		$("#ztButton"+i).html(jlsj+"（"+ztList[i].jlrmc+"）");
	}
	var detailZCUI = {
			
		cancel: function (ztid) {
			$("#newDetailZC").dialog("close");
		},
		
		changeCenter: function (ztid) {
			$("#detailZCUI_menuCenter").panel({
				href: getContextPath() + "/console/zcpd/zccx/detailZTXZUI.do?ztid="+ztid,
			});
		}
	};
	
	$("#detailZCUI_txbZczt").textbox({
		value: FormatZczt("${zc.zczt}")
	}); 
	
	$("#detailZCUI_txbGzsj").textbox({
		value: FormatDate(new Date("${zc.gzsj}"))
	}); 
	
	

	
</script>
</body>
</html>
