package org.lf.wx.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信返回码 https://mp.weixin.qq.com/wiki/19/f582b1026ab6bf9055f55e80f6c34753.html
 */
public class WXErrCode {
	public static final String ERR_CODE = "errcode";
	public static final String ERR_MSG = "errmsg";

	private String code;
	private String msg;

	public WXErrCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return getJSON().toJSONString();
	}

	public JSONObject getJSON() {
		JSONObject json = new JSONObject();

		json.put(ERR_CODE, code);
		json.put(ERR_MSG, msg);

		return json;
	}

	public static WXErrCode MEDIA_NOT_FOUND = new WXErrCode("00001", "媒体不存在");
	public static WXErrCode BUSY = new WXErrCode("-1", "系统繁忙 ");
	public static WXErrCode OK = new WXErrCode("0", "请求成功");

	public static WXErrCode WX_40001 = new WXErrCode("40001", "验证失败");
	public static WXErrCode WX_40002 = new WXErrCode("40002", "无效的凭证类型");
	public static WXErrCode WX_40003 = new WXErrCode("40003", "无效的OpenID");
	public static WXErrCode WX_40004 = new WXErrCode("40004", "无效的媒体文件类型");
	public static WXErrCode WX_40005 = new WXErrCode("40005", "无效的文件类型");
	public static WXErrCode WX_40006 = new WXErrCode("40006", "无效的文件大小");
	public static WXErrCode WX_40007 = new WXErrCode("40007", "无效的媒体文件id");
	public static WXErrCode WX_40008 = new WXErrCode("40008", "无效的消息类型");
	public static WXErrCode WX_40009 = new WXErrCode("40009", "无效的图片文件大小");
	public static WXErrCode WX_40010 = new WXErrCode("40010", "无效的语音文件大小");
	public static WXErrCode WX_40011 = new WXErrCode("40011", "无效的视频文件大小");
	public static WXErrCode WX_40012 = new WXErrCode("40012", "无效的缩略图文件大小");
	public static WXErrCode WX_40013 = new WXErrCode("40013", "无效的AppID");
	public static WXErrCode WX_40014 = new WXErrCode("40014", "无效的access_token");
	public static WXErrCode WX_40015 = new WXErrCode("40015", "无效的菜单类型 ");
	public static WXErrCode WX_40016 = new WXErrCode("40016", "无效的按钮个数");
	public static WXErrCode WX_40017 = new WXErrCode("40017", "无效的按钮个数");
	public static WXErrCode WX_40018 = new WXErrCode("40018", "无效的的按钮名字长度");
	public static WXErrCode WX_40019 = new WXErrCode("40019", "无效的按钮KEY长度 ");
	public static WXErrCode WX_40020 = new WXErrCode("40020", "无效的按钮URL长度");
	public static WXErrCode WX_40021 = new WXErrCode("40021", "无效的菜单版本号");
	public static WXErrCode WX_40022 = new WXErrCode("40022", "无效的子菜单级数");
	public static WXErrCode WX_40023 = new WXErrCode("40023", "无效的子菜单按钮个数");
	public static WXErrCode WX_40024 = new WXErrCode("40024", "无效的子菜单按钮类型");
	public static WXErrCode WX_40025 = new WXErrCode("40025", "无效的子菜单按钮名字长度");
	public static WXErrCode WX_40026 = new WXErrCode("40026", "无效的子菜单按钮KEY长度");
	public static WXErrCode WX_40027 = new WXErrCode("40027", "无效的子菜单按钮URL长度");
	public static WXErrCode WX_40028 = new WXErrCode("40028", "无效的自定义菜单使用用户");
	public static WXErrCode WX_40029 = new WXErrCode("40029", "无效的oauth_code");
	public static WXErrCode WX_40030 = new WXErrCode("40030","无效的refresh_token");
	public static WXErrCode WX_40031 = new WXErrCode("40031", "无效的openid列表");
	public static WXErrCode WX_40032 = new WXErrCode("40032", "无效的openid列表长度");
	public static WXErrCode WX_40033 = new WXErrCode("40033", "无效的请求字符");

	public static WXErrCode WX_40035 = new WXErrCode("40035", "无效的参数");

	public static WXErrCode WX_40038 = new WXErrCode("40038", "无效的请求格式");
	public static WXErrCode WX_40039 = new WXErrCode("40039", "无效的URL长度");

	public static WXErrCode WX_40050 = new WXErrCode("40050", "无效的的分组id");
	public static WXErrCode WX_40051 = new WXErrCode("40051", "无效的的分组名");

	public static WXErrCode WX_40117 = new WXErrCode("40117", "无效的分组名");
	public static WXErrCode WX_40118 = new WXErrCode("40118", "无效的媒体ID");
	public static WXErrCode WX_40119 = new WXErrCode("40119", "无效的按扭类型");
	public static WXErrCode WX_40120 = new WXErrCode("40120", "无效的按扭类型");
	public static WXErrCode WX_40121 = new WXErrCode("40121", "无效的媒体类型");

	public static WXErrCode WX_40132 = new WXErrCode("40132", "微信号错误");

	public static WXErrCode WX_40137 = new WXErrCode("40137", "不支持改类型图片");

	public static WXErrCode WX_41001 = new WXErrCode("41001","缺少access_token参数 ");
	public static WXErrCode WX_41002 = new WXErrCode("41002", "缺少appid参数");
	public static WXErrCode WX_41003 = new WXErrCode("41003","缺少refresh_token参数");
	public static WXErrCode WX_41004 = new WXErrCode("41004", "缺少secret参数");
	public static WXErrCode WX_41005 = new WXErrCode("41005", "缺少多媒体文件数据");
	public static WXErrCode WX_41006 = new WXErrCode("41006", "缺少媒体id参数");
	public static WXErrCode WX_41007 = new WXErrCode("41007", "缺少子菜单数据");
	public static WXErrCode WX_41008 = new WXErrCode("41008", "缺少oauth code");
	public static WXErrCode WX_41009 = new WXErrCode("41009", "缺少openId");

	public static WXErrCode WX_42001 = new WXErrCode("42001", "连接超时");
	public static WXErrCode WX_42002 = new WXErrCode("42002", "刷新超时");
	public static WXErrCode WX_42003 = new WXErrCode("42003", "oauth_code超时");

	public static WXErrCode WX_42007 = new WXErrCode("42007", "用户修改微信密码，需要重新授权");

	public static WXErrCode WX_43001 = new WXErrCode("43001", "需要GET请求");
	public static WXErrCode WX_43002 = new WXErrCode("43002", "需要POST请求");
	public static WXErrCode WX_43003 = new WXErrCode("43003", "需要HTTPS请求");
	public static WXErrCode WX_43004 = new WXErrCode("43004", "需要先关注公众号");
	public static WXErrCode WX_43005 = new WXErrCode("43005", "他（她）还不是您的好友");

	public static WXErrCode WX_44001 = new WXErrCode("44001", "媒体文件为空");
	public static WXErrCode WX_44002 = new WXErrCode("44002", "POST请求数据包为空");
	public static WXErrCode WX_44003 = new WXErrCode("44003", "图文消息内容为空");
	public static WXErrCode WX_44004 = new WXErrCode("44004", "文本消息内容为空");

	public static WXErrCode WX_45001 = new WXErrCode("45001", "媒体文件过大");
	public static WXErrCode WX_45002 = new WXErrCode("45002", "字数过长");
	public static WXErrCode WX_45003 = new WXErrCode("45003", "标题字数过长");
	public static WXErrCode WX_45004 = new WXErrCode("45004", "描述字段过长");
	public static WXErrCode WX_45005 = new WXErrCode("45005", "链接字段过长");
	public static WXErrCode WX_45006 = new WXErrCode("45006", "图片链接字段过长");
	public static WXErrCode WX_45007 = new WXErrCode("45007", "语音播放时间超过限制");
	public static WXErrCode WX_45008 = new WXErrCode("45008", "图文消息超过限制");
	public static WXErrCode WX_45009 = new WXErrCode("45009", "接口调用超过限制");
	public static WXErrCode WX_45010 = new WXErrCode("45010", "创建菜单个数过多");

	public static WXErrCode WX_45015 = new WXErrCode("45015", "回复时间超过限制");
	public static WXErrCode WX_45016 = new WXErrCode("45016", "系统分组，不允许修改");
	public static WXErrCode WX_45017 = new WXErrCode("45017", "分组名过长");
	public static WXErrCode WX_45018 = new WXErrCode("45018", "分组数量超过上限");

	public static WXErrCode WX_45047 = new WXErrCode("45047", "客服接口下行条数超过上限");

	public static WXErrCode WX_46001 = new WXErrCode("46001", "媒体数据不存在");
	public static WXErrCode WX_46002 = new WXErrCode("46002", "该菜单版本不存在");
	public static WXErrCode WX_46003 = new WXErrCode("46003", "该菜单数据不存在");
	public static WXErrCode WX_46004 = new WXErrCode("46004", "该用户不存在");
	
	public static WXErrCode WX_47001 = new WXErrCode("47001", "数据解析错误");

	public static WXErrCode WX_48001 = new WXErrCode("48001", "您未获得该功能");

	public static WXErrCode WX_48004 = new WXErrCode("48004", "该功能被禁用");

	public static WXErrCode WX_50001 = new WXErrCode("50001", "用户未授权该功能");
	public static WXErrCode WX_50002 = new WXErrCode("50002","用户受限，可能是违规后接口被封禁");

	public static WXErrCode WX_61451 = new WXErrCode("61451", "参数错误");
	public static WXErrCode WX_61452 = new WXErrCode("61452", "无效的客服账号");
	public static WXErrCode WX_61453 = new WXErrCode("61453", "该客服账号已存在");
	public static WXErrCode WX_61454 = new WXErrCode("61454", "客服帐号名长度超过限制");
	public static WXErrCode WX_61455 = new WXErrCode("61455","客服帐号名包含非法字符(仅允许英文+数字)");
	public static WXErrCode WX_61456 = new WXErrCode("61456", "客服帐号个数超过限制");
	public static WXErrCode WX_61457 = new WXErrCode("61457", "头像文件类型无效");
	public static WXErrCode WX_61450 = new WXErrCode("61450", "系统错误");

	public static WXErrCode WX_61500 = new WXErrCode("61500", "日期格式错误");

	public static WXErrCode WX_65301 = new WXErrCode("65301","不存在此菜单id对应的个性化菜单");
	public static WXErrCode WX_65302 = new WXErrCode("65302", "不存此用户");
	public static WXErrCode WX_65303 = new WXErrCode("65303", "没有默认菜单，不能创建个性菜单");
	public static WXErrCode WX_65304 = new WXErrCode("65304", "MatchRule信息为空");
	public static WXErrCode WX_65305 = new WXErrCode("65305", "个性化菜单数量受限");
	public static WXErrCode WX_65306 = new WXErrCode("65306", "该帐号不支持个性化菜单");
	public static WXErrCode WX_65307 = new WXErrCode("65307", "个性化菜单信息为空");
	public static WXErrCode WX_65308 = new WXErrCode("65308", "包含没有响应类型的按钮");
	public static WXErrCode WX_65309 = new WXErrCode("65309", "个性化菜单处于关闭状态");
	public static WXErrCode WX_65310 = new WXErrCode("65310","填写了省份或城市信息，国家信息不能为空");
	public static WXErrCode WX_65311 = new WXErrCode("65311","填写了城市信息，省份信息不能为空");
	public static WXErrCode WX_65312 = new WXErrCode("65312", "无效的国家信息");
	public static WXErrCode WX_65313 = new WXErrCode("65313", "无效的省份信息");
	public static WXErrCode WX_65314 = new WXErrCode("65314", "无效的城市信息");

	public static WXErrCode WX_65316 = new WXErrCode("65316","该公众号的菜单设置了过多的域名外跳（最多跳转到3个域名的链接）");
	public static WXErrCode WX_65317 = new WXErrCode("65317", "无效的链接");

	//上传
	public static WXErrCode WX_9001001 = new WXErrCode("9001001", "POST数据参数不合法");
	public static WXErrCode WX_9001002 = new WXErrCode("9001002", "远端服务不可用");
	public static WXErrCode WX_9001003 = new WXErrCode("9001003", "Ticket不合法");
	public static WXErrCode WX_9001004 = new WXErrCode("9001004", "获取周边用户信息失败");
	public static WXErrCode WX_9001005 = new WXErrCode("9001005", "获取商户信息失败");
	public static WXErrCode WX_9001006 = new WXErrCode("9001006", "获取OpenID失败");
	public static WXErrCode WX_9001007 = new WXErrCode("9001007", "上传文件缺失");
	public static WXErrCode WX_9001008 = new WXErrCode("9001008", "上传素材的文件类型无效");
	public static WXErrCode WX_9001009 = new WXErrCode("9001009", "上传素材的文件尺寸不合法");
	public static WXErrCode WX_9001010 = new WXErrCode("9001010", "上传失败");
	
	public static WXErrCode WX_9001020 = new WXErrCode("9001020", "无效的账号");
	public static WXErrCode WX_9001021 = new WXErrCode("9001021", "已有设备激活率低于50%，不能新增设备");
	public static WXErrCode WX_9001022 = new WXErrCode("9001022", "设备申请数无效，必须为大于0的数字");
	public static WXErrCode WX_9001023 = new WXErrCode("9001023", "已存在审核中的设备ID申请");
	public static WXErrCode WX_9001024 = new WXErrCode("9001024", "次查询设备ID数量不能超过50");
	public static WXErrCode WX_9001025 = new WXErrCode("9001025", "无效的设备ID");
	public static WXErrCode WX_9001026 = new WXErrCode("9001026", "无效的页面ID");
	public static WXErrCode WX_9001027 = new WXErrCode("9001027", "无效的页面参数ID");
	public static WXErrCode WX_9001028 = new WXErrCode("9001028", "一次删除页面ID数量不能超过10");
	public static WXErrCode WX_9001029 = new WXErrCode("9001029", "页面已应用在设备中，请先解除应用关系再删除");
	public static WXErrCode WX_9001030 = new WXErrCode("9001030", "一次查询页面ID数量不能超过50");
	public static WXErrCode WX_9001031 = new WXErrCode("9001031", "无效的时间区间");
	public static WXErrCode WX_9001032 = new WXErrCode("9001032", "保存设备与页面的绑定关系参数错误");
	public static WXErrCode WX_9001033 = new WXErrCode("9001033", "无效的门店ID");
	public static WXErrCode WX_9001034 = new WXErrCode("9001034", "设备备注信息过长");
	public static WXErrCode WX_9001035 = new WXErrCode("9001035", "无效的设备申请参数"); 
	public static WXErrCode WX_9001036 = new WXErrCode("9001036", "查询起始值begin不合法"); 
}
