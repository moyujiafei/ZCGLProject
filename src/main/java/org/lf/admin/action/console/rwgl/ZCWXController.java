package org.lf.admin.action.console.rwgl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.admin.service.zcgl.GZWXService;
import org.lf.admin.service.zcgl.RWLX;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.utils.EasyuiComboBoxItem;
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
 * 维修人员进行资产的维修工作。可以提交：完成维修、再次维修、申请闲置、申请报废。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/rwgl/zcwx/")
public class ZCWXController extends BaseController {
	private final String ROOT = "/console/rwgl/zcwx";
	
	@Autowired
	private GZWXService gzwxService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	@RequestMapping("zcwxListUI.do")
	public String zcwxListUI() {
		return ROOT + "/zcwxListUI";
	}

	/**
	 * 获取需要维修的资产信息
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("zcwxList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> zcwxList(HttpSession session, String zcmc, String zclx, String syr, int page, int rows) {
		Integer appId = getAppId(session);
		String czr = getCurrUser(session).getWxUsername();
		if(zcmc==null || zcmc.length()<1)
			zcmc = null;
		if(zclx==null || zclx.length()<1)
			zclx = null;
		if(syr==null || syr.length()<1)
			syr = null;
		return zcService.getPageRWZCList(appId, czr, RWLX.故障维修.getValue(), null, zcmc, zclx, null, syr, null, 0, rows, page);
	}
	
	/**
	 * 资产类型下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCLXComboWithAll(HttpSession session) {
		return zclxService.getZCLXMCComboWithAll(getAppId(session));
	}
	
	/**
	 * 设备正常
	 * @return
	 */
	@RequestMapping("finishGZWXUI.do")
	public String finishGZWXUI(Integer zcid,Model m) {
		JZC jzc = new JZC();
		jzc = zcService.getZC(zcid);
		m.addAttribute("jzc", jzc);
		return ROOT + "/finishGZWXUI";
	}
	
	@RequestMapping("finishGZWX.do")
	@ResponseBody
	public String finishGZWX(HttpSession session, Integer zcid, 
			                 @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			                 @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		Integer appId = getAppId(session);
		if(imageFileList==null||imageFileList[0].getSize()==0)
			imageFileList = null;
		if(voiceFileList==null||voiceFileList[0].getSize()==0)
			voiceFileList = null;
		String wxr = getCurrUser(session).getWxUsername();
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			gzwxService.finishGZWX(appId, zcid, mediaList, wxr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请重新维修
	 * @return
	 */
	@RequestMapping("resubmitWXSQUI.do")
	public String resubmitWXSQUI(Integer zcid,Model m) {
		JZC jzc = new JZC();
		jzc = zcService.getZC(zcid);
		m.addAttribute("jzc", jzc);
		return ROOT + "/resubmitWXSQUI";
	}
	
	@RequestMapping("resubmitWXSQ.do")
	@ResponseBody
	public String resubmitWXSQ(HttpSession session, Integer zcid, String sqRemark,
			                   @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			                   @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		Integer appId = getAppId(session);
		if(imageFileList==null||imageFileList[0].getSize()==0)
			imageFileList = null;
		if(voiceFileList==null||voiceFileList[0].getSize()==0)
			voiceFileList = null;
		String wxr = getCurrUser(session).getWxUsername();
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			gzwxService.resubmitWXSQ(appId, zcid, mediaList, sqRemark, wxr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请报废
	 * @return
	 */
	@RequestMapping("submitBFSQUI.do")
	public String submitBFSQUI(Integer zcid,Model m) {
		JZC jzc = new JZC();
		jzc = zcService.getZC(zcid);
		m.addAttribute("jzc", jzc);
		return ROOT + "/submitBFSQUI";
	}
	
	@RequestMapping("submitBFSQ.do")
	@ResponseBody
	public String submitBFSQ(HttpSession session, Integer zcid, String sqRemark,
			                 @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			                 @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		Integer appId = getAppId(session);
		if(imageFileList==null||imageFileList[0].getSize()==0)
			imageFileList = null;
		if(voiceFileList==null||voiceFileList[0].getSize()==0)
			voiceFileList = null;
		String wxr = getCurrUser(session).getWxUsername();
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			gzwxService.submitBFSQ(appId, zcid, mediaList, sqRemark, wxr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 申请闲置
	 * @return
	 */
	@RequestMapping("submitXZSQUI.do")
	public String submitXZSQUI(Integer zcid,Model m) {
		JZC jzc = new JZC();
		jzc = zcService.getZC(zcid);
		m.addAttribute("jzc", jzc);
		return ROOT + "/submitXZSQUI";
	}
	
	@RequestMapping("submitXZSQ.do")
	@ResponseBody
	public String submitXZSQ(HttpSession session, Integer zcid, String sqRemark,
			                 @RequestParam(value = "image_upload", required = false)  MultipartFile[] imageFileList,
			                 @RequestParam(value = "voice_upload", required = false)  MultipartFile[] voiceFileList) {
		Integer appId = getAppId(session);
		if(imageFileList==null||imageFileList[0].getSize()==0)
			imageFileList = null;
		if(voiceFileList==null||voiceFileList[0].getSize()==0)
			voiceFileList = null;
		String wxr = getCurrUser(session).getWxUsername();
		try {
			Map<String, MediaType> mediaList = wxMediaService.uploadMediaList(session,imageFileList, voiceFileList);
			gzwxService.submitXZSQ(appId, zcid, mediaList, sqRemark, wxr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

}
