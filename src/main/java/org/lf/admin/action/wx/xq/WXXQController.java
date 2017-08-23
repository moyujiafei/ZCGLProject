package org.lf.admin.action.wx.xq;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.wx.WXXQService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 详情控制层
 * 
 * @author sunwill
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/wx/wxxq/")
public class WXXQController extends WXBaseController {
	@Autowired
	private WXXQService wxxqService;

	/**
	 * 用户详情
	 * 
	 * @param appId
	 * @param userId
	 * @return
	 */
	@RequestMapping("userXQ.do")
	@ResponseBody
	public AjaxResultModel userXQ(String appId, String userId) {
		return wxxqService.userXQ(appId, userId);
	}

	/**
	 * 通过企业号消息，查询任务详情
	 * 
	 * @return
	 */
	@RequestMapping("rwXQ.do")
	@ResponseBody
	public AjaxResultModel rwXQ(Integer rwid) {
		return wxxqService.getWXRW(rwid);
	}
	
	/**
	 * 根据任务id查询资产信息
	 * 
	 * @param rwid
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("rwzcInfo.do")
	@ResponseBody
	public AjaxResultModel rwzcInfo(Integer rwid, Integer rows, Integer page, Integer finish) {
		return wxxqService.getWXRWZC(rwid, rows, page, finish);
	}
	
	/**
	 * 根据状态id查询状态记录细则信息
	 * 
	 * @param ztid
	 * @return
	 */
	@RequestMapping("ztxzInfo.do")
	@ResponseBody
	public AjaxResultModel ztxzInfo(Integer ztid, String mediaType) {
		return wxxqService.getWXZCXZ(ztid, mediaType);
	}
	
	/**
	 * 资产详情
	 * 
	 * @return
	 */
	@RequestMapping("zcXQ.do")
	@ResponseBody
	public AjaxResultModel zcXQ(Integer zcid, String pageUrl) {
		return wxxqService.zcXQ(zcid, pageUrl);
	}

}
