function getContextPath() {
	var localObj = window.location;
	var contextPath = localObj.pathname.split("/")[1];
	return localObj.protocol + "//" + localObj.host + "/" + contextPath;
}

function wx_alert(title, info, returnUrl, button) {
	return "<div id='wx_alert_dialog' class='weui-dialog__alert'>"+
				    "<div class='weui-mask'></div>"+
				    "<div class='weui-dialog'>"+
				        "<div class='weui-dialog__hd'><strong class='weui-dialog__title'>"+title+"</strong></div>"+
				        "<div class='weui-dialog__bd'>"+info+"</div>"+
				        "<div class='weui-dialog__ft'>"+
				           " <a id='wx_alert_btn' href='"+returnUrl+"' class='weui-dialog__btn'>" + button + "</a>"+
				       " </div>"+
				    "</div>"+
				    "</div>";
	
	
}

//下载微信服务器端临时素材到WEB服务器指定目录 （media/）下。
function downloadMedia(mediaType, res) {
		$.ajax({
				url: getContextPath() + "/wx/download.do",
		        async : true,
		        type : 'POST',
		        data : {"mediaType" : mediaType, "mediaIds" : res.serverId},//查询状态为2，指定二维码的文件是否存在
		        cache:false,
		        dataType:'text',//返回字符串
		        success : function(){ 
		        	
		        },
		        error : function(){
		        }
			});
	};