package org.lf.admin.action.console.zcpd;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.db.pojo.VZTXZ;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.logs.ZTService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZTService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 资产查询
 * 
 * 部门资产管理员查询本部门资产。
 * 后勤管理人员查询企业资产。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcpd/zccx/")
public class ZCCXController extends BaseController {
	private Logger logger = Logger.getLogger(ZCCXController.class);
	
	private final String ROOT = "/console/zcpd/zccx";

	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCZTService zcztService;
	
	@Autowired
	private ZTService ztService;
	
	/**
	 * 部门资产查询
	 * @return
	 */
	@RequestMapping("BMZCListUI.do")
	public String BMZCListUI() {
		return ROOT + "/BMZCListUI";
	}

	/**
	 * 查询v_zc，获取当前资产管理员名下所有资产列表
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("getBMZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getBMZCList(HttpSession session, String zcmc, String zclx, Integer zczt, int page, int rows) {
		Integer appId = getAppId(session);
		String glr = getCurrUser(session).getWxUsername();
		if(StringUtils.isEmpty(zcmc)){
			zcmc=null;
		}
		if(StringUtils.isEmpty(zclx)){
			zclx=null;
		}
		return zcService.getPageVZCList(appId, null, null, null, null, null, null, glr, new ArrayList<Integer>(), page, rows);
	}
	
	/**
	 * 资产查询，后勤管理人员，查看整个企业的资产信息
	 * @return
	 */
	@RequestMapping("ZCListUI.do")
	public String ZCListUI() {
		return ROOT + "/ZCListUI";
	}

	/**
	 * 查询v_zc，获取所有资产列表
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("getZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getZCList(HttpSession session, String zcmc, String zclx, String syr,Integer zczt, int page, int rows) {
		Integer appId = getAppId(session);
		if(StringUtils.isEmpty(zcmc)){
			zcmc=null;
		}
		if(StringUtils.isEmpty(zclx)){
			zclx=null;
		}
		if(StringUtils.isEmpty(syr)){
			syr=null;
		}
		return zcService.getPageVZCList(appId, null, zcmc, zclx, null, syr, null, null, zczt, page, rows);
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
	 * 资产状态：下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCZTComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCZTComboWithAll() {
		return zcztService.getZCZTComboWithAll();
	}
	
	/**
	 * 查询v_zc，获取指定条件下的部门资产列表。
	 * 
select zc.*
from v_zc zc
where app_id = ? and glr = ?
  and zc like '%...%' and zclx = ? and syr = ? and zczt = ?
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("queryBMZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> queryBMZCList(HttpSession session, String zc, String zclx, String syr, Integer zczt, int page, int rows) {
		Integer appId = getAppId(session);
		String glr = getCurrUser(session).getWxUsername();
		if (StringUtils.isEmpty(zc)) {
			zc=null;
		}
		if (StringUtils.isEmpty(zclx)) {
			zclx=null;
		}
		if(StringUtils.isEmpty(syr)){
			syr=null;
		}
		List<Integer> zcztList=null;
		if(zczt!=null){
			zcztList=new ArrayList<>();
			zcztList.add(zczt);
		}
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zc, zclx, null, syr, null, glr, zcztList, page, rows);
	}
	
	/**
	 * 查询v_zc，获取指定条件下的所有资产列表。
	 * 
select zc.*
from v_zc zc
where app_id = ? 
  and zc like '%...%' and zclx = ? and syr = ? and zczt = ?
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("queryZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> queryZCList(HttpSession session, String zc, String zclx, String syr, Integer zczt, int page, int rows) {
		Integer appId = getAppId(session);
		if (StringUtils.isEmpty(zc)) {
			zc=null;
		}
		if (StringUtils.isEmpty(zclx)) {
			zclx=null;
		}
		if (StringUtils.isEmpty(syr)) {
			syr=null;
		}
		List<Integer> zcztList=null;
		if(zczt!=null){
			zcztList=new ArrayList<>();
			zcztList.add(zczt);
		}
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zc, zclx, null, syr, null, null, zcztList, page, rows);
	}
	
	/**
	 * 资产详情。
	 * @param zcid
	 * @param ztid 用户在左侧选中某个状态日志，根据ztid刷新右侧的页面
	 * @param m
	 * @return
	 */
	@RequestMapping("detailZCUI.do")
	public String detailZCUI(Integer zcid, Model m) {
		VZC zcParam = new VZC();
		zcParam.setZcid(zcid);
		VZC zc = zcService.getZC(zcParam);
		m.addAttribute("zc", zc);
		
		LZT ztParam = new LZT();
		ztParam.setAppId(zc.getAppId());
		ztParam.setZcdm(zc.getZcdm());
		List<LZT> ztList = ztService.getZTList(ztParam);
		JSONArray arr = new JSONArray();
		for (LZT zt : ztList) {
			arr.add(JSON.toJSON(zt));
		}
		JSONObject ztObj = new JSONObject();
		ztObj.put("ztList", arr);
		m.addAttribute("ztObj", ztObj);
		
		return ROOT + "/detailZCUI";
	}
	
	private List<VZTXZ> translate(LZT zt, List<LZTXZ> lxzList) throws OperException {
		List<VZTXZ> vxzList = new ArrayList<>();
		
		for (LZTXZ lxz : lxzList) {
			vxzList.add(ztService.translateZTXZ(zt, lxz));
		}
		
		return vxzList;
	}
	
	/**
	 * 资产详情。
	 * @param zcid
	 * @param ztid 用户在左侧选中某个状态日志，根据ztid刷新右侧的页面
	 * @param m
	 * @return
	 */
	@RequestMapping("detailZTXZUI.do")
	public String detailZTXZUI(Integer ztid, Model m) {
		// 用户选择左侧某个状态（如果未选，默认选中第一个）
		LZT zt = ztService.getZT(ztid);
		m.addAttribute("zt", zt);
		
		// 右侧状态详情
		try {
			List<LZTXZ> imageList = ztService.getZTXZList(ztid, MediaType.image);
			m.addAttribute("imageList", translate(zt, imageList));
			List<LZTXZ> voiceList = ztService.getZTXZList(ztid, MediaType.voice);
			m.addAttribute("voiceList", translate(zt, voiceList));
		} catch (OperException e) {
			logger.error(e.getMessage(), e);
		}
		
		return ROOT + "/detailZTXZUI";
	}
	
}
