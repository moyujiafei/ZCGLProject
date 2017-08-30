package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.WXXJWXService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/gzwx/")
public class WXXJWXController extends WXBaseController {

	@Autowired
	private WXXJWXService xjwxService;

	/**
	 * 完成故障维修 维修组
	 * 
	 * @return
	 */
	@RequestMapping("finishGZWX.do")
	@ResponseBody
	public AjaxResultModel finishGZWX(HttpServletRequest request, String imgId, String voiceId, Integer zcId) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.finishGZWX(appId, wxr, imgId, voiceId, zcId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}

	/**
	 * 重新提交维修申请 维修组
	 * 
	 * @return
	 */
	@RequestMapping("resubmitWXSQ.do")
	@ResponseBody
	public AjaxResultModel resubmitWXSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.resubmitWXSQ(appId, wxr, imgId, voiceId, zcId, sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}

	/**
	 * 提交闲置申请 维修组
	 * 
	 * @return
	 */
	@RequestMapping("submitXZSQ.do")
	@ResponseBody
	public AjaxResultModel submitXZSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {

		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitXZSQ(appId, wxr, imgId, voiceId, zcId, sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}

	/**
	 * 提交报废申请 维修组
	 * 
	 * @return
	 */
	@RequestMapping("submitBFSQ.do")
	@ResponseBody
	public AjaxResultModel submitBFSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitBFSQ(appId, wxr, imgId, voiceId, zcId, sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}
	
	/**
	 * 完成日常巡检 巡检组
	 * 
	 * @return
	 */
	@RequestMapping("finishRCXJ.do")
	@ResponseBody
	public AjaxResultModel finishRCXJ(HttpServletRequest request, String imgId, String voiceId, Integer zcId) {
		try {
			Integer appId = getAppId(request);
			String xjr = getUserId(request);
			return xjwxService.finishRCXJ(appId, xjr, imgId, voiceId, zcId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 申请维修资产 巡检组
	 * 
	 * @return
	 */
	@RequestMapping("submitXJWXSQ.do")
	@ResponseBody
	public AjaxResultModel submitXJWXSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitXJWXSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	
	}
	
	/**
	 * 申请闲置资产 巡检组
	 * 
	 * @return
	 */
	@RequestMapping("submitXJXZSQ.do")
	@ResponseBody
	public AjaxResultModel submitXJXZSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitXJWXSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 申请报废资产 巡检组
	 * 
	 * @return
	 */
	@RequestMapping("submitXJBFSQ.do")
	@ResponseBody
	public AjaxResultModel submitXJBFSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitXJWXSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 申请维修资产 使用人组
	 * 
	 * @return
	 */
	@RequestMapping("submitSyrWXSQ.do")
	@ResponseBody
	public AjaxResultModel submitSyrWXSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitSyrWXSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	
	}
	
	/**
	 * 申请闲置资产 使用人组
	 * 
	 * @return
	 */
	@RequestMapping("submitSyrXZSQ.do")
	@ResponseBody
	public AjaxResultModel submitSyrXZSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitSyrXZSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 申请报废资产 使用人组
	 * 
	 * @return
	 */
	@RequestMapping("submitSyrBFSQ.do")
	@ResponseBody
	public AjaxResultModel submitSyrBFSQ(HttpServletRequest request, String imgId, String voiceId, Integer zcId, String sqRemark) {
		try {
			Integer appId = getAppId(request);
			String wxr = getUserId(request);
			return xjwxService.submitSyrBFSQ(appId, wxr, imgId, voiceId, zcId,sqRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}


}
