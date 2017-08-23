package org.lf.admin.action.console.zcgl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后勤管理人员对已分配给部门但尚未“未使用”的资产进行重新调拨。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/reallocate/")
public class ReallocateZCController extends BaseController {
	private final String ROOT = "/console/zcgl/reallocate";

	@Autowired
	private ZCDJService zcdjService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	/**
	 * 资产重新调拨界面
	 * @return
	 */
	@RequestMapping("reallocateZCListUI.do")
	public String reallocateZCListUI() {
		return ROOT + "/reallocateZCListUI";
	}

	/**
	 * 查询v_zc，获取资产状态为“未使用”的资产列表。
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("reallocateZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> reallocateZCList(HttpSession session, String zcmc, String zclx, String deptName, int page, int rows) {
		return null;
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
	 * 资产管理部门下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCBMComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCBMComboWithAll(HttpSession session) {
		return zcglService.getZCBMNameComboWithAll(getAppId(session));
	}
	
	/**
	 * 读取"未分配"的资产，用于重新调拨
	 * @return
	 */
	@RequestMapping("getReallocateZC.do")
	@ResponseBody
	public VZC getReallocateZC(Integer zcid) {
		return null;
	}
	
	/**
	 * 资产管理部门（C_ZCGL）下拉列表框，默认为第一条记录。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCBMCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCBMCombo(HttpSession session) {
		return null;
	}
	
	/**
	 * 读取资产的原有资产管理部门。
	 * @return
	 */
	public CZCGL getZCBM(Integer zcid) {
		return null;
	}	
	
	@RequestMapping("reallocateZC.do")
	@ResponseBody
	public String reallocateZC(HttpSession session, Integer zcid, Integer new_zcglId, String new_cfdd) {
		String djr = getCurrUser(session).getWxUsername();
		return null;
	}
}
