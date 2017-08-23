package org.lf.wx.user;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.service.OperErrCode;
import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 标签管理
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E7%AE%A1%E7%90%86%E6%A0%87%E7%AD%BE
 *
 */
public class TagManager {
	/**
	 * 创建标签
	 */
	public static final String CREATE = "https://qyapi.weixin.qq.com/cgi-bin/tag/create?access_token=%s";
	/**
	 * 删除标签
	 */
	public static final String DELETE = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?access_token=%s&tagid=%s";
	/**
	 * 获取标签成员
	 */
	public static final String GET = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=%s&tagid=%s";
	/**
	 * 获取标签列表
	 */
	public static final String LIST = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=%s";
	/**
	 * 增加标签成员
	 */
	public static final String ADD_TAG_USERS = "https://qyapi.weixin.qq.com/cgi-bin/tag/addtagusers?access_token=%s";
	/**
	 * 删除标签成员
	 */
	public static final String DEL_TAG_USERS = "https://qyapi.weixin.qq.com/cgi-bin/tag/deltagusers?access_token=%s";
	
	
	public static final String USERLIST = "userlist";
	
	/**
	 * 创建标签
	 * 创建的标签属于管理组，默认为加锁状态。加锁状态下只有本管理组才可以增删成员，解锁状态下其它管理组也可以增删成员。
	 * 
	 * @param tagid 标签id，整型，指定此参数时新增的标签会生成对应的标签id，不指定时则以目前最大的id自增。
	 * @param tagName 标签名称，长度限制为32个字（汉字或英文字母），标签名不可与其他标签重名。
	 * @return
{
   "errcode": 0,
   "errmsg": "created"
   "tagid": 1
}
	 * @throws WXException
	 */
	public static boolean createTag(String accessToken, int tagid, String tagName) throws WXException {
		String url = String.format(CREATE, accessToken);
		
		JSONObject body = new JSONObject();
		body.put("tagname", tagName);
		body.put("tagid", tagid);
		
		String jsonString = WXUtils.downloadString(url, "POST", body.toJSONString());
		JSONObject json = JSON.parseObject(jsonString);
		
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		return true;
	}
	
	/**
	 * 获取标签成员
	 * https://work.weixin.qq.com/api/doc#10921
	 * 
	 * 无限制，但返回列表仅包含应用可见范围的成员；第三方仅可获取自己创建的标签
	 * 
	 * @return 
{
   "errcode": 0,
   "errmsg": "ok",
   "userlist": [
         {
             "userid": "zhangsan",
             "name": "李四"
         }
     ],
   "partylist": [2]
}
	 */
	public static JSONObject getUserList(String accessToken, String tagid) throws WXException {
		String url = String.format(GET, accessToken, tagid);
		String jsonString = WXUtils.downloadString(url, "POST", null);
		JSONObject json = JSON.parseObject(jsonString);
		
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		return json;
	}
	
	/**
	 * 增加标签成员
	 * 
请求包示例如下:

{
   "tagid": 1,
   "userlist":[ "user1","user2"],
   "partylist": [4]
}
	 * 
	 * @return 
a)正确时返回

{
   "errcode": 0,
   "errmsg": "ok"
}
b)若部分userid、partylist非法，则返回

{
   "errcode": 0,
   "errmsg": "错误消息",
   "invalidlist"："usr1|usr2|usr",
   "invalidparty"：[2,4]
}
c)当包含userid、partylist全部非法时返回

{
   "errcode": 40070,
   "errmsg": "all list invalid "
}
	 */
	public static boolean addTagUsers(String accessToken, int tagid, List<String> useridList) throws WXException {
		String url = String.format(ADD_TAG_USERS, accessToken);
		JSONObject body = new JSONObject();
		body.put("tagid", tagid);
		JSONArray users = new JSONArray();
		for (String userid : useridList) {
			users.add(userid);
		}
		body.put("userlist", users);
		
		String jsonString = WXUtils.downloadString(url, "POST", body.toJSONString());
		JSONObject json = JSON.parseObject(jsonString);
		
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		return json.getString(OperErrCode.ERR_MSG).equals("ok");
	}
	
	/**
	 * 
	 * 删除标签成员
	 * 
请求包如下

{
   "tagid": 1,
   "userlist":[ "user1","user2"],
   "partylist":[2,4]
}
	 * 
	 * @return
a)正确时返回

{
   "errcode": 0,
   "errmsg": "deleted"
}
b)若部分userid、partylist非法，则返回

{
   "errcode": 0,
   "errmsg": "错误消息",
   "invalidlist"："usr1|usr2|usr",
   "invalidparty": [2,4]
}
其中错误消息视具体出错情况而定，分别为：
invalid userlist and partylist faild
invalid userlist faild
invalid partylist faild
c)当包含的userid、partylist全部非法时返回

{
   "errcode": 40031,
   "errmsg": "all list invalid"
}
	 * @throws WXException
	 */
	public static boolean delTagUsers(String accessToken, int tagid, List<String> useridList) throws WXException {
		String url = String.format(DEL_TAG_USERS, accessToken);
		JSONObject body = new JSONObject();
		body.put("tagid", tagid);
		JSONArray users = new JSONArray();
		for (String userid : useridList) {
			users.add(userid);
		}
		body.put("userlist", users);
		
		String jsonString = WXUtils.downloadString(url, "POST", body.toJSONString());
		JSONObject json = JSON.parseObject(jsonString);
		
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		return json.getString(OperErrCode.ERR_MSG).equals("ok");
	}

	/**
	 * 获取标签列表
	 * 
	 * @return
{
   "errcode": 0,
   "errmsg": "ok",
   "taglist":[
      {"tagid":1,"tagname":"a"},
      {"tagid":2,"tagname":"b"}
   ]
}
	 * @throws WXException
	 */
	public static List<Tag> getTagList(String accessToken) throws WXException {
		String url = String.format(LIST, accessToken);
		String jsonString = WXUtils.downloadString(url, "POST", null);
		JSONObject json = JSON.parseObject(jsonString);
		
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		List<Tag> tagList = new ArrayList<>();
		JSONArray array = json.getJSONArray("taglist");
		for (int i = 0; i < array.size(); i++) {
			tagList.add(new Tag(array.getJSONObject(i)));
		}
		
		return tagList;
	}
}
