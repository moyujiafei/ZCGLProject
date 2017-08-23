package org.lf.wx.message;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lf.wx.WXProperties;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

public class SendMessageManagerTest {

	@Test
	public void test() throws WXException {
		List<String> toTagList = new ArrayList<>();
		toTagList.add("1");
		
		String msg = SendMessage.textMessage(WXProperties.AGENT_ID, "test", SendMessage.SendTarget.toTagList, toTagList);
		SendMessageManager.sendMessage(WXUtils.getLocalToken(), msg);
		
//		String picUrl = "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png";
//		String url = WXProperties.WX_SERVER_URL + "/wx/today.do";
//		SendArticle news = new SendArticle("欢迎", "简介", picUrl, url);
//		msg = SendMessage.newsMessage(toTagList, WXProperties.AGENT_ID, news);
//		SendMessageManager.sendMessage(WXUtils.getLocalToken(), msg);
	}

}
