package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.WXResultCode;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.servlet.WXLoginInterceptor;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信端公共方法类
 *
 */
public class WXBaseController {
	
	/**
	 * token无效
	 */
	protected static final AjaxResultModel INVALID_TOKEN = new AjaxResultModel(WXResultCode.ERROR.getCode(), "invalid_token", null);


	/**
	 * 根据token获取appId
	 * 
	 * @param token
	 * @return
	 * @throws OperException
	 */
	protected Integer getAppId(HttpServletRequest request) throws OperException {
		return WXLoginInterceptor.getAppIdFromToken(getDataByToken(request));
	}

	/**
	 * 根据token获取userId
	 * 
	 * @param token
	 * @return
	 * @throws OperException
	 */
	protected String getUserId(HttpServletRequest request) throws OperException {
		return WXLoginInterceptor.getUserIdFromToken(getDataByToken(request));
	}

	/**
	 * 根据token获取json对象
	 * 
	 * @param token
	 * @return
	 * @throws OperException
	 */
	protected JSONObject getDataByToken(HttpServletRequest request) throws OperException {
		return WXLoginInterceptor.getJsonByRequest(request);
	}
	

}
