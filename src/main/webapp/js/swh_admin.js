$.ajaxSetup({
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if (XMLHttpRequest.status == 403) {
			alert('您没有权限访问此资源或进行此操作');
			return false;
		}
	},

	complete : function(XMLHttpRequest, textStatus) {
		var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头,sessionstatus，
		if (sessionstatus == 'timeout') {
			// 如果超时就处理 ，指定要跳转的页面
			var top = getTopWinow(); // 获取当前页面的顶层窗口对象
			alert('登录超时, 请重新登录.');
			// 获取绝对路径

			top.location.href = getContextPath() + "/console/user/loginUI.do"; // 跳转到登陆页面
		}
	}
});

/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * 
 * @return 当前页面的顶层窗口对象
 */  
function getTopWinow() {
	var p = window;
	while (p != p.parent) {
		p = p.parent;
	}
	return p;
}

function getContextPath() {
	var localObj = window.location;
	var contextPath = localObj.pathname.split("/")[1];
	return localObj.protocol + "//" + localObj.host + "/" + contextPath;
}

//传入一个JSON字符串和一个newValue值，会根据value值来获得相应的数据
//在combobox的Onchange事件调用
function getPYMList(info,newValue) {
	var json = new Array();
	if(newValue!=''&&newValue!=null){
			var value=newValue.trim();
			for(var i=0,j=0;i<info.length;i++){
				 var pym=info[i].pym;
				 if(pym!=null&&pym.indexOf(value)==0){
						json[j++]=info[i];
				 }
			}
			return json;
		}else{
			return info;
		}
}

//传入URL获取JSON数据
function getJson(url) {
	 var info='';
	 $.ajax({
	    	  url: url,
		      dataType: 'json',
		      async:false,
		  cache:false,					     
		      success: function( data ) {
		    	  info=data;
				},
			});
	return info;
}
