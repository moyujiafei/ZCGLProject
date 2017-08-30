<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
<table>
	<tr><td style="height: 10px;"></td></tr>
	<tr><td style="width: 10px;"></td><td style="font-weight: 600;font-size: 14px;">备注：</td><td><label style="font-size: 14px;">${zt.remark}</label></td></tr>
	<tr><td style="height: 10px;"></td></tr>
</table>
<div id="detailZTXZUI_imagediv" style="margin:0 auto;" class="flexslider1" >
	<ul class="slides">
		<li>
			<c:forEach items="${imageList}" var="imageInfo">
		      <div class="image" style="padding: 30px 10px 0px 50px;float: left;" >
			      <a rel="gallery" title="${imageInfo.briefImageURI}" href="${imageInfo.briefImageURI}">
			      	<img  width="115px" height="120px" style="border: solid 1px #1A98E5;" title="${imageInfo.briefImageURI}" src="${imageInfo.briefImageURI}	"/>
			      </a>
		      </div>
			</c:forEach>   					   		
		</li>
	</ul>
</div>
<div>
	<ul class="slides">
		<li>
			<c:forEach items="${voiceList}" var="voiceInfo">
			   <div style="padding: 50px 0 20px 80px;">
			   	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="150" height="20"
				    codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab">
				    <param name="movie" value="singlemp3player.swf?showDownload=true" />
				    <param name="wmode" value="transparent" />
				    <embed wmode="transparent" width="640" height="20" src="singlemp3player.swf?file=${voiceInfo.mediaURI}&showDownload=false"
				    type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
				</object>
			   </div>
			</c:forEach>   					   		
		</li>
	</ul>
</div>
<script>
	$(function(){
		//设置图片div宽度
		$('#detailZTXZUI_imagediv').flexslider({
		    controlNav: false,
		    animationLoop: false,
		    slideshow: false,
		    keyboardNav: true, 
		    directionNav:true,
		    itemWidth: 130,
		    start: function(carousel){
	   		  $('body').removeClass('loading');
	    }
		});
		
		//设置全屏预览
		$('.image img').fullscreenslides();

		var $container = $('#fullscreenSlideshowContainer');
	
		$container.bind("init", function() { 
		
			$container
			.append('<div class="ui" id="fs-close" >&times;</div>')
			.append('<div class="ui" id="fs-loader">Loading...</div>')
			.append('<div class="ui" id="fs-prev">&lt;</div>')
			.append('<div class="ui" id="fs-next">&gt;</div>')
			.append('<div class="ui" id="fs-caption"><span></span></div>');
		
			$('#fs-prev').click(function(){
				$container.trigger("prevSlide");
			});
			
			$('#fs-next').click(function(){
				$container.trigger("nextSlide");
			});
			
			$('#fs-close').click(function(){
				$container.trigger("close");
			});
		
		})
		
		.bind("startLoading", function() { 
			$('#fs-loader').show();
		})
		
		.bind("stopLoading", function() { 
			$('#fs-loader').hide();
		})
		
		.bind("startOfSlide", function(event, slide) { 
	
			$('#fs-caption span').text(slide.title);
			$('#fs-caption').show();
		})
		
		.bind("endOfSlide", function(event, slide) { 
			$('#fs-caption').hide();
		});
	});
</script>
</body>
</html>