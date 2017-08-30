package org.lf.wx.user;

import java.util.ArrayList;
import java.util.List;

import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E7%AE%A1%E7%90%86%E6%88%90%E5%91%98
 *
 */
public class UserManager {
	/**
	 * 获取成员基本信息
	 */
	public static final String GET = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
	/**
	 * 创建成员
	 */
	public static final String CREATE = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";
	/**
	 * 更新成员基本信息
	 */
	public static final String UPDATE = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=%s";
	/**
	 * 获取部门成员(详情)
	 */
	public static final String LIST = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=%s&status=%s";
	
	
	/**
	 * 获取成员基本信息
	 * 
	 * @param userId 成员UserID。对应管理端的帐号
	 * @return 正常情况下，微信会返回下述JSON数据包给公众号：
{
   "errcode": 0,
   "errmsg": "ok",
   "userid": "zhangsan",
   "name": "李四",
   "department": [1, 2],
   "position": "后台工程师",
   "mobile": "15913215421",
   "gender": "1",
   "email": "zhangsan@gzdev.com",
   "weixinid": "lisifordev",  
   "avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
   "status": 1,
   "extattr": {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
}
	 */
	public static User getUser(String accessToken, String userId) throws WXException {
		if (StringUtils.isEmpty(userId)) {
			throw new NullPointerException("openId is empty!");
		}
		
		String url = String.format(GET, accessToken, userId);
		
		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);	

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return new User(json);
	}
	
	/**
	 * 创建成员
	 * 
请求包体：

{
   "userid": "zhangsan",
   "name": "张三",
   "mobile": "15913215421",
   "department": [1, 2],
   "enable":1
}
	 * 
	 * @return
返回结果：

{
   "errcode": 0,
   "errmsg": "created"
}
	 * 
	 */
	public static User createUser(String accessToken, String userId, String name, String mobile, List<Integer> deptList) throws WXException {
		String url = String.format(CREATE, accessToken);
		
		JSONObject body = new JSONObject();
		body.put("userid", userId);
		body.put("name", name);
		body.put("mobile", mobile);
		JSONArray array = new JSONArray();
		for (Integer dept : deptList) {
			array.add(dept);
		}
		body.put("department", array);
		body.put("enable", 1);
		
		String jsonString = WXUtils.downloadString(url, "POST", body.toJSONString());
		JSONObject json = JSON.parseObject(jsonString);	

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return getUser(accessToken, userId);
	}
	
	/**
	 * 更新成员
	 * 
请求包体：

{
   "userid": "zhangsan",
   "name": "李四",
   "department": [1],
   "order"：[10],
   "position": "后台工程师",
   "mobile": "15913215421",
   "gender": "1",
   "email": "zhangsan@gzdev.com",
   "isleader": 0,
   "enable": 1,
   "avatar_mediaid": "2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0",
   "telephone": "020-123456",
   "english_name": "jackzhang",
   "extattr": {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
}
	 * 
	 * @return
返回结果：

{
   "errcode": 0,
   "errmsg": "updated"
}
	 */
	public static boolean updateUser(String accessToken, User user) throws WXException {
		String url = String.format(UPDATE, accessToken);
		
		String jsonString = WXUtils.downloadString(url, "GET", user.toString());
		JSONObject json = JSON.parseObject(jsonString);	

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return true;
	}
	
	/**
	 * 获取部门成员(详情)
	 * @param departmentId 	获取的部门id
	 * @param fetchChild 1/0：是否递归获取子部门下面的成员
	 * @param status 	0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加,未填写则默认为4
	 * @return
{
   "errcode": 0,
   "errmsg": "ok",
   "userlist": [
           {
                  "userid": "zhangsan",
                  "name": "李四",
                  "department": [1, 2],
                  "position": "后台工程师",
                  "mobile": "15913215421",
                  "gender": "1",
                  "email": "zhangsan@gzdev.com",
                  "weixinid": "lisifordev",  
                  "avatar":           "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
                  "status": 1,
                  "extattr": {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
           }
     ]
}
	 * @throws WXException
	 */
	public static List<User> getUserList(String accessToken, int departmentId, boolean fetchChild, UserStatus status) throws WXException {
		int fetch = fetchChild ? 1 : 0;
		status = status == null ? UserStatus.未关注 : status;
		
		String url = String.format(LIST, accessToken, departmentId, fetch, status.getValue());
		
		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);	

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		List<User> userList = new ArrayList<>();
		JSONArray array = json.getJSONArray("userlist");
		for (int i = 0; i < array.size(); i++) {
			userList.add(new User(array.getJSONObject(i))); 
		}
		
		return userList;
	}
	
	public static List<User> getUserList(String accessToken, int departmentId) throws WXException {
		return getUserList(accessToken, departmentId, true, UserStatus.已关注);
	}
}
