package org.lf.admin.service.wx;

import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.sys.WXAppService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WXCommonService")
public class WXCommonService{

	@Autowired
	private WXAppService wxAppService;

	/**
	 * 根据appid和url返回jssdk config
	 * 
	 * @param chuAppId
	 * @param requestUrl
	 * @return
	 */
	public AjaxResultModel getWxConfig(Integer appId, String requestUrl) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			result.setData(wxAppService.getJssdkConfig(appId, requestUrl));
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

}
