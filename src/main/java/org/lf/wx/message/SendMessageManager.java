package org.lf.wx.message;

import org.lf.utils.StringUtils;
import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SendMessageManager {
	private SendMessageManager() {
	}

	public final static String SEND = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
	
	/**
	 * 发送消息
	 * @param accessToken
	 * @param message
	 * @return
	 * @throws WXException 如果无权限或收件人不存在，则本次发送失败，返回无效的userid列表（注：由于userid不区分大小写，返回的列表都统一转为小写）；如果未关注，发送仍然执行。
{
   "errcode": 0,
   "errmsg": "ok",
   "invaliduser": "UserID1",
   "invalidparty":"PartyID1",
   "invalidtag":"TagID1"
}	
	 */
	public static boolean sendMessage(String accessToken, String message) throws WXException {
		String url = String.format(SEND, accessToken);
		
		String jsonString = WXUtils.downloadString(url, "GET", message);
		if (StringUtils.isEmpty(jsonString)) {
			throw new WXException(WXErrCode.WX_42001);
		}
		
		JSONObject json = JSON.parseObject(jsonString);	

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return true;
	}
}
