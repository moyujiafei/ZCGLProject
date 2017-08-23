package org.lf.admin.service.wx.my;

import org.lf.admin.db.pojo.VZT;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WXMyActionService")
public class WXMyActionService {

	@Autowired
	private ZTService ztService;
	
	public AjaxResultModel getVZTList(Integer appId, String jlr, Integer rows, Integer page) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			VZT param = new VZT();
			param.setAppId(appId);
			param.setJlr(jlr);
			result.setData(ztService.getZTList(param, rows, page));
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	
}
