$(function() {});
// 修改密码
function updatepwd(uid) {
	$('#pwddialog').dialog({
		title : '修改密码',
		width : 512,
		height : 300,
		closed : false,
		cache : false,
		href : getContextPath() + '/console/user/updatePwdUI.do',
		modal : true
	});
}
// 当前用户信息
function currUser() {
	$('#currUser').dialog({
		title : '用户信息',
		width : 512,
		height : 300,
		closed : false,
		cache : false,
		href : getContextPath() + '/console/user/currUser.do',
		modal : true
	});
}
// 退出系统
function quit() {
	$.messager.confirm('退出系统', '确定要退出系统吗?', function(r) {
		if (r) {
			window.location.href = getContextPath() + '/console/user/quit.do';
		}
	});
}
//刷新center窗口
function changeCenter(url){
//	$('#center').panel('refresh',url);
	$("#center").panel({
		cache : false,
		href : url
	});
}
