package org.lf.utils.servlet;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WXLoginInterceptor implements HandlerInterceptor {
	/**
	 * 登录成功后的，微信端请求参数
	 */
	public static final String WX_LOGIN_TOKEN = "token";
	/**
	 * 授权成功后，生成token的key：userId
	 */
	public static final String TOKEN_KEY_USER_ID = "TOKEN_KEY_USER_ID";
	/**
	 * 授权成功后，生成token的key：appId
	 */
	public static final String TOKEN_KEY_APP_ID = "TOKEN_KEY_APP_ID";
	/**
	 * token创建时间
	 */
	public static final String TOKEN_KEY_CREATE_DATE = "TOKEN_KEY_CREATE_DATE";
	/**
	 * 网页授权获得token的过期时间，单位：月。-3表示三个月的有效期
	 */
	public static final int TOKEN_EXPIRED_DATE = -3;

	/**
	 * 非法的token
	 */
	private static final OperErrCode ILLEGAL_TOKEN = new OperErrCode("10001", "非法的token");
	/**
	 * 加密/解密拆分位数
	 */
	private static final int ENCODE_SPILT = 59;

	/**
	 * 验证token
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			JSONObject tokenObj = getJsonByRequest(request);
			getAppIdFromToken(tokenObj);
			getUserIdFromToken(tokenObj);
			getCreateDateFromToken(tokenObj);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 从请求参数中提取token，并解析为json对象
	 * 
	 * @param request
	 * @return
	 * @throws OperException
	 */
	public static JSONObject getJsonByRequest(HttpServletRequest request) throws OperException {
		String token = request.getParameter(WX_LOGIN_TOKEN);
		if (StringUtils.isEmpty(token))
			throw new OperException(ILLEGAL_TOKEN);
		else
			return getJsonByToken(token);
	}

	/**
	 * 根据token字符串解析生成json对象
	 * 
	 * @param token
	 * @return
	 * @throws OperException
	 */
	private static JSONObject getJsonByToken(String token) throws OperException {
		try {
			// TODO 上线时跟换解密算法
			// JSONObject tokenObj = JSON.parseObject(decodeToken(token));
			JSONObject tokenObj = JSON.parseObject(StringUtils.base64Decode(token));
			if (tokenObj == null)
				throw new OperException(ILLEGAL_TOKEN);
			else
				return tokenObj;
		} catch (Exception e) {
			throw new OperException(ILLEGAL_TOKEN);
		}
	}

	/**
	 * 获得userId
	 * 
	 * @param tokenObj
	 * @return
	 * @throws OperException
	 */
	public static String getUserIdFromToken(JSONObject tokenObj) throws OperException {
		try {
			String userId = tokenObj.getString(TOKEN_KEY_USER_ID);
			if (StringUtils.isEmpty(userId))
				throw new OperException(ILLEGAL_TOKEN);
			else
				return userId;
		} catch (Exception e) {
			throw new OperException(ILLEGAL_TOKEN);
		}
	}

	/**
	 * 获得appId
	 * 
	 * @param tokenObj
	 * @return
	 * @throws OperException
	 */
	public static Integer getAppIdFromToken(JSONObject tokenObj) throws OperException {
		try {
			int appId = tokenObj.getIntValue(TOKEN_KEY_APP_ID);
			if (appId == 0)
				throw new OperException(ILLEGAL_TOKEN);
			else
				return appId;
		} catch (Exception e) {
			throw new OperException(ILLEGAL_TOKEN);
		}
	}

	/**
	 * 获得token的创建时间
	 * 
	 * @param tokenObj
	 * @return
	 * @throws OperException
	 */
	public static Date getCreateDateFromToken(JSONObject tokenObj) throws OperException {
		try {
			Date date = tokenObj.getDate(TOKEN_KEY_CREATE_DATE);
			if (date == null)
				throw new OperException(ILLEGAL_TOKEN);
			else
				return date;
		} catch (Exception e) {
			throw new OperException(ILLEGAL_TOKEN);
		}
	}

	/**
	 * token在有效期内，则返回true,过期则返回false
	 * 
	 * @param tokenObj
	 * @return
	 */
	public static boolean checkTokenDate(JSONObject tokenObj) {
		try {
			Date createDate = getCreateDateFromToken(tokenObj);
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MONTH, TOKEN_EXPIRED_DATE);// 减去三个月
			return createDate.after(now.getTime());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	/**
	 * 字符串逆向
	 * 
	 * @param str
	 * @return
	 */
	private static String exchangeString(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		StringBuffer sb = new StringBuffer(str);
		sb.reverse();
		return sb.toString();
	}

	/**
	 * token加密算法
	 * 
	 * @param str
	 * @return
	 */
	private static String encodeToken(String str) {
		try {
			String code = exchangeString(StringUtils.base64Encode(str));// base64加密后，字符反转
			return code.substring(ENCODE_SPILT) + code.substring(0, ENCODE_SPILT);// 拆分后逆向拼接
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * token解密算法
	 * 
	 * @param str
	 * @return
	 */
	private static String decodeToken(String str) {
		try {
			return StringUtils.base64Decode(exchangeString(str.substring(str.length() - ENCODE_SPILT) + str.substring(0, str.length() - ENCODE_SPILT)));
		} catch (Exception e) {
			return null;
		}
	}

	public static String encodeToken(String userId, Integer appId) throws Exception {
		JSONObject json = new JSONObject();
		json.put(TOKEN_KEY_USER_ID, userId);
		json.put(TOKEN_KEY_APP_ID, appId);
		json.put(TOKEN_KEY_CREATE_DATE, new Date());
		return StringUtils.base64Encode(JSON.toJSONString(json));
		// TODO 上线时跟换加密算法
		// return encodeToken(JSON.toJSONString(json));
	}

}
