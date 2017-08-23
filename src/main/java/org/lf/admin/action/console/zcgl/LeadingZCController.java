package org.lf.admin.action.console.zcgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.admin.service.zcgl.ZCSYService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.EasyuiDatagrid;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产使用。 使用人保管人对自己的资产进行管理。
 *      对于状态为“领用中”的资产，提供：“同意领用”和“拒绝领用”两个按钮。
 *      对于状态为“使用中”的资产，提供：“资产上交”、“申请维修”、“申请闲置”、“申请报废”四个按钮
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/leading/")
public class LeadingZCController extends BaseController {
	private final String ROOT = "/console/zcgl/leading";

	@Autowired
	private ZCSYService zcsyService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	/**
	 * 资产界面
	 * @return
	 */
	@RequestMapping("leadingZCListUI.do")
	public String leadingZCListUI(HttpSession session, Model m) {
		ChuUser user = getCurrUser(session);
		m.addAttribute("user", user);
		
		return ROOT + "/leadingZCListUI";
	}
	
	/**
	 * 获取资产信息
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("leadingZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> leadingZCList(HttpSession session, int page, int rows) {
		Integer appId = getAppId(session);
		String syr = getCurrUser(session).getWxUsername();
		
		List<Integer> zcztList = new ArrayList<>();
		zcztList.add(ZCZT.领用中.getValue());
		zcztList.add(ZCZT.使用中.getValue());
		
		return zcService.getPageVZCList(appId, null, null, null, null, syr, null, null, zcztList, page, rows);
	}
	
	@RequestMapping("leadingZC.do")
	@ResponseBody
	public String agreeLeadingZC(HttpSession session, Integer zcid) {
		String syr = getCurrUser(session).getWxUsername();
		
		try {
			zcsyService.agreeLeadingZC(zcid, syr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 拒绝领用资产
	 * @return
	 */
	@RequestMapping("refuseLeadingZCUI.do")
	public String refuseLeadingZCUI(Integer zcid, Model m) {
		m.addAttribute("zcid", zcid);
		return ROOT + "/refuseLeadingZCUI";
	}
	
	@RequestMapping("refuseLeadingZC.do")
	@ResponseBody
	public String refuseLeadingZC(HttpSession session, Integer zcid, String refuseRemark) {
		String syr = getCurrUser(session).getWxUsername();
		
		try {
			zcsyService.refuseLeadingZC(zcid, syr, refuseRemark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请上交资产
	 * @return
	 */
	@RequestMapping("sendbackZCUI.do")
	public String sendbackZCUI(Integer zcid, Model m) {
		m.addAttribute("zcid", zcid);
		return ROOT + "/sendbackZCUI";
	}
	
	@RequestMapping("sendbackZC.do")
	@ResponseBody
	public String sendbackZC(HttpSession session, Integer zcid, String remark) {
		try {
			zcsyService.sendbackZC(zcid, remark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请维修资产
	 * @return
	 */
	@RequestMapping("zcwxSQUI.do")
	public String zcwxSQUI(Integer zcid, Model m) {
		m.addAttribute("zcid", zcid);
		return ROOT + "/zcwxSQUI";
	}
	
	@RequestMapping("zcwxSQ.do")
	@ResponseBody
	public String zcwxSQ(HttpSession session, Integer zcid, String sqRemark,
			             @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			             @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		String sqr = getCurrUser(session).getWxUsername();
		
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			zcsyService.submitSQ(sqr, zcid, ZCZT.申请维修.getValue(), mediaList, sqRemark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请闲置资产
	 * @return
	 */
	@RequestMapping("zcxzSQUI.do")
	public String zcxzSQUI(Integer zcid, Model m) {
		m.addAttribute("zcid", zcid);
		return ROOT + "/zcxzSQUI";
	}
	
	@RequestMapping("zcxzSQ.do")
	@ResponseBody
	public String zcxzSQ(HttpSession session, Integer zcid, String sqRemark,
			             @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			             @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		String sqr = getCurrUser(session).getWxUsername();
		
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			zcsyService.submitSQ(sqr, zcid, ZCZT.申请闲置.getValue(), mediaList, sqRemark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请报废资产
	 * @return
	 */
	@RequestMapping("zcbfSQUI.do")
	public String zcbfSQUI(Integer zcid, Model m) {
		m.addAttribute("zcid", zcid);
		return ROOT + "/zcbfSQUI";
	}
	
	@RequestMapping("zcbfSQ.do")
	@ResponseBody
	public String zcbfSQ(HttpSession session, Integer zcid, String sqRemark,
			             @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			             @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		String sqr = getCurrUser(session).getWxUsername();
		
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			zcsyService.submitSQ(sqr, zcid, ZCZT.申请报废.getValue(), mediaList, sqRemark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
