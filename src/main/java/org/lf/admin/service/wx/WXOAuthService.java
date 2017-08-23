package org.lf.admin.service.wx;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuTagMapper;
import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.sys.WXAppService;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.StringUtils;
import org.lf.utils.servlet.WXLoginInterceptor;
import org.lf.wx.utils.QyAccessToken4OAuth;
import org.lf.wx.utils.WXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 微信网页授权服务层
 * 
 * @author sunwill
 *
 */
@Service
public class WXOAuthService {
	private static final Logger logger = LoggerFactory.getLogger(WXOAuthService.class);

	/*-
	 *  网页授权第一步获得code
	 *  appid	公众号的唯一标识
	 *	redirect_uri	授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
	 *	response_type	返回类型，请填写code
	 *	scope	应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
	 *			snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 *	state	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 *	#wechat_redirect	无论直接打开还是做页面302重定向时候，必须带此参数
	 */
	private static final String OAUTH_URL_4CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&agentid=%s&state=%s#wechat_redirect";
	/**
	 * 根据code获取成员信息
	 * 
	 * 请求方式：GET（HTTPS）
	 */
	private static final String OAUTH_URL_4TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";

	/*-
	 * 使用user_ticket获取成员详情
	 * 
	 * 请求方式：POST（HTTPS）
		private static final String OAUTH_URL_4PRIVATEINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token=%s";
	 */
	/*-
	 * 以snsapi_base为scope发起的网页授权，是用来获取进入页面的用户的openid的，并且是静默授权并自动跳转到回调页的。
	 * 用户感知的就是直接进入了回调页（往往是业务页面）
		private static final String SCOPE_BASE = "snsapi_base";
	 */
	/*-
	 * 手动授权，可获取成员的详细信息，包含手机、邮箱。
		private static final String SCOPE_PRIVATEINFO = "snsapi_privateinfo";
	 */
	/**
	 * 以snsapi_userinfo为scope发起的网页授权，是用来获取用户的基本信息的。但这种授权需要用户手动同意，并且由于用户同意过，
	 * 所以无须关注，就可在授权后获取该用户的基本信息。
	 */
	private static final String SCOPE_USERINFO = "snsapi_userinfo";

	@Autowired
	private ChuAppMapper appDao;

	@Autowired
	private WXAppService wXAppService;

	@Autowired
	private ChuWXUserMapper chuWXUserMapper;

	@Autowired
	private ChuTagMapper chuTagMapper;

	/**
	 * 根据code获取成员信息
	 * 
	 * @param code
	 * @return 1、当用户为企业成员时，返回用户标识为UserId； 2、非企业成员授权时，返回用户标识为OpenId
	 * @throws OperException
	 */
	private QyAccessToken4OAuth getUserByCode(String code, Integer appId) throws OperException {
		if (StringUtils.isEmpty(code) || appId == null) {
			return null;
		}
		String tockenUrl = String.format(OAUTH_URL_4TOKEN, wXAppService.getAccessToken(appId), code);
		String response = WXUtils.downloadString(tockenUrl, "GET", null);
		JSONObject json = JSON.parseObject(response);
		QyAccessToken4OAuth oAuthToken = null;
		if (json == null || json.getIntValue(QyAccessToken4OAuth.ERRCODE) != 0) {
			logger.error(json.toJSONString());
		} else {
			oAuthToken = new QyAccessToken4OAuth(json);
		}
		return oAuthToken;
	}

	/*-
	 * 根据user_ticket获取用户详细信息，包括手机号和email等
	 * 
	 * @param user_ticket
	 * @return
	 * @throws OperException
	private User getUserPrivateinfo(String user_ticket, Integer appId) throws OperException {
		if (StringUtils.isEmpty(user_ticket)) {
			return null;
		}
		String detailUrl = String.format(OAUTH_URL_4PRIVATEINFO, wXAppService.getAccessToken(appId));
		JSONObject body = new JSONObject();
		body.put(QyAccessToken4OAuth.USER_TICKET, user_ticket);
		String response = WXUtils.downloadString(detailUrl, "POST", body.toJSONString());
		JSONObject json = JSON.parseObject(response);
		User user = null;
		if (json == null || StringUtils.isEmpty(json.getString(User.USERID))) {
			logger.error(json.toJSONString());
		} else {
			user = new User(json);
		}
		return user;
	}
	 */

	/**
	 * 网页授权验证： 重定向到微信服务器获取code，取得code后，将code和state作为请求参数，跳转到redirect_uri指定的路径
	 * 
	 * @param appId
	 * @param response
	 */
	public void getCode(String appIdStr, HttpServletResponse response, String redirectUrl) {
		try {
			if (!StringUtils.isEmpty(appIdStr)) {
				Integer appId = Integer.parseInt(appIdStr);
				ChuApp app = appDao.selectByPrimaryKey(appId);
				if (app != null) {
					response.sendRedirect(String.format(OAUTH_URL_4CODE, app.getCorpId(), URLEncoder.encode(ZCGLProperties.URL_SERVER + redirectUrl + "?appId=" + appId, "UTF-8"),
							SCOPE_USERINFO, app.getAgentId(), "state"));
				}
			}
		} catch (Exception e) {
			logger.error("网页授权失败", e);
		}
	}

	/**
	 * 根据code获取用户数据
	 * 
	 * @param code
	 * @param state
	 * @param appId
	 * @return
	 */
	public String getRedirectUrl(String code, String state, Integer appId) {
		String token = null;
		try {
			QyAccessToken4OAuth user = getUserByCode(code, appId);
			if (user != null && !StringUtils.isEmpty(user.getUserId())) {
				String userId = user.getUserId();
				token = WXLoginInterceptor.encodeToken(userId, appId);
			}
		} catch (Exception e) {
			logger.error("生成授权token失败", e);
		}
		String toWxClient = "redirect:" + ZCGLProperties.URL_WX_CLIENT;// 授权完成后,重定向到前端界面
		if (!StringUtils.isEmpty(token)) {
			toWxClient += ("?doOauth=1&token=" + token + "&appId=" + appId);
		}
		return toWxClient;
	}

	/**
	 * 验证token，获取用户信息
	 * 
	 * @param request
	 * @param appId
	 * @return
	 */
	public AjaxResultModel checkToken(HttpServletRequest request, String appIdStr) {
		AjaxResultModel result = new AjaxResultModel();
		if (!StringUtils.isEmpty(appIdStr)) {
			try {
				result.setCode(WXResultCode.SUCCESS.getCode());
				result.setMsg(WXResultCode.SUCCESS.getMsg());
				Integer appId = null;
				try {
					appId = Integer.parseInt(appIdStr);
				} catch (Exception e) {
					result.setData("应用标识不能为空");
				}
				JSONObject tokenObj = WXLoginInterceptor.getJsonByRequest(request);
				// {"TOKEN_KEY_CREATE_DATE":1499938040210,"TOKEN_KEY_USER_ID":"shangwei","TOKEN_KEY_APP_ID":17}
				if (appId == WXLoginInterceptor.getAppIdFromToken(tokenObj)) {
					if (WXLoginInterceptor.checkTokenDate(tokenObj)) {
						// token未过期
						String userId = WXLoginInterceptor.getUserIdFromToken(tokenObj);
						if (!StringUtils.isEmpty(userId)) {
							ChuWXUser param = new ChuWXUser();
							param.setAppId(appId);
							param.setUserid(userId);
							param = chuWXUserMapper.select(param);
							if (param != null && !StringUtils.isEmpty(param.getTagNo())) {
								JSONArray tagNoArray = JSON.parseArray(param.getTagNo());
								List<String> tagNameList = new ArrayList<String>();
								for (int i = 0; i < tagNoArray.size(); i++) {
									Integer tagNo = tagNoArray.getIntValue(i);
									String tagName = chuTagMapper.getName(tagNo, appId);
									if (!StringUtils.isEmpty(tagName)) {
										tagNameList.add(tagName);
									}
								}
								if (tagNameList.size() > 0) {
									// 验证通过
									result.setData(tagNameList);
								}
							}
							if (result.getData() == null) {
								result.setData("用户不存在,或未分配权限");
							}
							return result;
						}
					}
				}
			} catch (Exception e) {
				result.setCode(WXResultCode.ERROR.getCode());
				result.setMsg("error");
			}
		}
		result.setCode(WXResultCode.ERROR.getCode());
		result.setMsg("error");
		return result;
	}

}
