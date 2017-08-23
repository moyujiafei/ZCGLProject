package org.lf.wx.menu;

import org.lf.wx.WXProperties;
import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 应用菜单
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E5%88%9B%E5%BB%BA%E5%BA%94%E7%94%A8%E8%8F%9C%E5%8D%95
 *
 */
public class MenuManager {
	/**
	 * 自定义菜单创建接口
	 */
	public static final String MENU_CREATE = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=%s&agentid=" + WXProperties.AGENT_ID;
	/**
	 * 自定义菜单查询接口
	 */
	public static final String MENU_GET = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?access_token=%s&agentid=" + WXProperties.AGENT_ID;
	/**
	 * 自定义菜单删除接口
	 */
	public static final String MENU_DEL = "https://qyapi.weixin.qq.com/cgi-bin/menu/delete?access_token=%s&agentid=" + WXProperties.AGENT_ID;
	
	/**
	 * 自定义菜单创建接口.
	 * http://qydev.weixin.qq.com/wiki/index.php?title=%E5%88%9B%E5%BB%BA%E5%BA%94%E7%94%A8%E8%8F%9C%E5%8D%95
	 * 
	 * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
	 * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
	 * 3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
	 *   如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
	 *   测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
	 *   
	 * @param mb 创建菜单，向服务器发送JSON字符串
	 * @return {"errcode":0,"errmsg":"ok"}
	 * @throws WXException 错误时的返回JSON数据包如下{"errcode":40018,"errmsg":"invalid button name size"}
	 */
	public static boolean createMenu(String accessToken, Menu mb) throws WXException {
		if (mb == null) {
			throw new NullPointerException();
		}
		
		String url = String.format(MENU_CREATE, accessToken);
		
		String jsonString = WXUtils.downloadString(url, "POST", mb.toString());
		JSONObject json = JSON.parseObject(jsonString);

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return true;
	}
	
	/**
	 * 获取菜单列表
	 * 管理组须拥有应用的使用权限，并且应用必须设置在回调模式。 
	 * http://qydev.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96%E8%8F%9C%E5%8D%95%E5%88%97%E8%A1%A8
	 * 
	 * @return
	 */
	public static JSONObject getMenu(String accessToken) throws WXException {
		String url = String.format(MENU_GET, accessToken);

		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);
		
		// 首先判断下载是否出错
		if (json.containsKey(WXErrCode.ERR_CODE)) {
			throw new WXException(json);
		}
		
		return json;
	}
	
	/**
	 * 删除菜单
	 * 管理组须拥有应用的管理权限，并且应用必须设置在回调模式。
	 * http://qydev.weixin.qq.com/wiki/index.php?title=%E5%88%A0%E9%99%A4%E8%8F%9C%E5%8D%95
	 * 
	 * @return 正确的Json返回结果: {"errcode":0,"errmsg":"ok"}
	 * @throws WXException 
	 */
	public static boolean delMenu(String accessToken) throws WXException {
		String url = String.format(MENU_DEL, accessToken);
		
		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}
		
		return true;
	}

}
