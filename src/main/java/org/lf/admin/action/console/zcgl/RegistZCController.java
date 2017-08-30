package org.lf.admin.action.console.zcgl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.VFJ;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产登记
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/regist/")
public class RegistZCController extends BaseController {
	private final String ROOT = "/console/zcgl/regist";
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private ZCDJService zcdjService;
	
	@Autowired
	private FJService fjService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 资产登记界面
	 * @return
	 */
	@RequestMapping("registZCListUI.do")
	public String registZCListUI() {
		return ROOT + "/registZCListUI";
	}

	/**
	 * 查询v_zc，获取资产状态为“已登记”的资产列表。
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("zcList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> zcList(HttpSession session, String zcmc, String zclx, Integer deptNo, int page, int rows) {
		
		Integer appId = getAppId(session);
		if (StringUtils.isEmpty(zcmc)) {
			zcmc = null;
		}
		if (StringUtils.isEmpty(zclx)) {
			zclx = null;
		}
		List<Integer> zcztList = new ArrayList<>();
		zcztList.add(ZCZT.已登记.getValue());
		zcztList.add(ZCZT.未使用.getValue());
		zcztList.add(ZCZT.未审核.getValue());
		
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclx, null, null, deptNo, null, zcztList, page, rows);
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
	
	@RequestMapping("delRegistZC.do")
	@ResponseBody
	public String delRegistZC(Integer id,HttpSession session) {
		String djr = getCurrUser(session).getWxUsername();
		String resultStr = SUCCESS;
		try {
			zcdjService.delRegistedZC(id, djr);
		} catch (OperException e) {
			resultStr = e.getMessage();
		}
		return resultStr;
	}
	
	@RequestMapping("insertRegistZCUI.do")
	public String insertRegistZCUI() {
		return ROOT + "/insertRegistZCUI";
	}
	
	/**
	 * 非空。在相同appId下唯一检查。
	 * @param session
	 * @param zcdm
	 * @return
	 */
	@RequestMapping("checkRegistZCByDM.do")
	@ResponseBody
	public boolean checkRegistZCByDM(HttpSession session, String zcdm) {
		Integer appId = getAppId(session);
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZcdm(zcdm);
		return zcService.countZCList(param) != 0;
	}
	
	/**
	 * 资产名称非空。
	 * @param session
	 * @param zcmc
	 * @return
	 */
	@RequestMapping("checkRegistZCByMC.do")
	@ResponseBody
	public boolean checkRegistZCByMC(HttpSession session, String zcmc) {
		if (StringUtils.isEmpty(zcmc)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 资产类型下拉列表框，默认为第一条记录。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCLXCombo(HttpSession session) {
		Integer appId = getAppId(session);
		return zclxService.getLevel2ZCLXCombo(appId);
	}
	
	/**
	 * 折旧年限，数值型（可以是小数）, 只要大于0.
	 * @param session
	 * @param zcmc
	 * @return
	 */
	@RequestMapping("checkRegistZCByZJNX.do")
	@ResponseBody
	public boolean checkRegistZCByZJNX(HttpSession session, BigDecimal zjnx) {
		BigDecimal compareNum = new BigDecimal(0);
		if (zjnx.compareTo(compareNum)==1) {
			return false;
		} else {
			return true;
		}
	}
	
	//这里的gzsj从前台来的数据只能是string
	@RequestMapping("insertRegistZC.do")
	@ResponseBody
	public String insertRegistZC(HttpSession session, String dm, String mc, Integer lxid, String xh, String ccbh, String gzsj, BigDecimal cost,
			BigDecimal zjnx,Integer num,@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		String djr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String resultStr = SUCCESS;
		if (checkRegistZCByDM(session, dm)) {
			resultStr = "ZCDM_ERROR";
			return resultStr;
		} else if (checkRegistZCByMC(session, mc)){
			resultStr = "ZCMC_ERROR";
			return resultStr;
		} else if (checkRegistZCByZJNX(session, zjnx)) {
			resultStr = "ZJNX_ERROR";
			return resultStr;
		} else if (checkNum(num)) {
			resultStr = "NUM_ERROR";
			return resultStr;
		} else if (checkPIC(file_upload)) {
			resultStr="PIC_ERROR";
			return resultStr;
		} else if (checkCost(cost)) {
			resultStr="COST_ERROR";
			return resultStr;
		} else {
			try {
				String Url = null;
				if (file_upload !=null) {
					Url = zcdjService.uploadPic(session, file_upload,getAppId(session));
				}
				zcdjService.registZC(appId, dm, mc, lxid, cost, num, xh, ccbh, sdf.parse(gzsj), zjnx, djr, Url);
			} catch (OperException e) {
				resultStr = e.getMessage();
			} catch (ParseException e) {
				resultStr="您刚刚输入的日期无效，请重试";
			}
		}
		
		return resultStr;
	}

	@RequestMapping("updateRegistZCUI.do")
	public String updateRegistZCUI(Integer id, Model m) {
		VZC param = new VZC();
		param.setZcid(id);
		VZC resultZC = zcService.getZC(param);
		m.addAttribute("zcInfo",resultZC);

		return ROOT + "/updateRegistZCUI";
	}
	//这里的gzsj从前台来的数据只能是string
	@RequestMapping("updateRegistZC.do")
	@ResponseBody
	public String updateRegistZC(HttpSession session, Integer id, String mc, Integer lxid, String xh, String ccbh,Integer num,BigDecimal cost,
			String gzsj, BigDecimal zjnx,@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		String resultStr = SUCCESS;
		
		if (checkRegistZCByMC(session, mc)){
			resultStr = "ZCMC_ERROR";
			return resultStr;
		} else if (checkRegistZCByZJNX(session, zjnx)) {
			resultStr = "ZJNX_ERROR";
			return resultStr;
		} else if (checkNum(num)) {
			resultStr = "NUM_ERROR";
			return resultStr;
		} else if (checkPIC(file_upload)) {
			resultStr="PIC_ERROR";
			return resultStr;
		} else if (checkCost(cost)) {
			resultStr="COST_ERROR";
			return resultStr;
		} else {
			try {
				String Url = null;
				if (file_upload!=null) {
					Url = zcdjService.uploadPic(session, file_upload,getAppId(session));
				}
				zcdjService.updateRegistedZC(id, mc, lxid, cost, xh, ccbh,sdf.parse(gzsj), zjnx,Url);
			} catch (OperException e) {
				resultStr = e.getMessage();
			} catch (ParseException e) {
				resultStr = "您刚刚输入的日期无效，请重试";
			}
		}
		
		return resultStr;
	}
	
	/**
	 * 资产调拨
	 * @return
	 */
	@RequestMapping("allocateZCUI.do")
	public String allocateZCUI() {
		return ROOT + "/allocateZCUI";
	}
	
	/**
	 * 读取已登记的资产，用于调拨
	 * @return
	 */
	@RequestMapping("getRegistedZC.do")
	@ResponseBody
	public VZC getRegistedZC(Integer zcid) {
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
		return zcglService.getZCBMCombo(getAppId(session));
	}
	
	/**
	 * 资产调拨
	 * @return
	 */
	@RequestMapping("allocateZC.do")
	@ResponseBody
	public String allocateZC(HttpSession session, Integer zcid, Integer wxdeptNo, String cfdd) {
		String djr = getCurrUser(session).getWxUsername();
		String resultStr = SUCCESS;
		try {
			CZCGL record=new CZCGL();
			record.setAppId(getAppId(session));
			record.setDeptNo(wxdeptNo);
			Integer zcglId=zcglDao.select(record).getId();
			zcdjService.allocateZC(zcid, zcglId, cfdd, djr);
		} catch (OperException e) {
			resultStr = e.getMessage();
		}
		
		return resultStr;
	}
	
	/**
	 * 资产重新调拨
	 * @return
	 */
	@RequestMapping("reallocateZCUI.do")
	public String reassignZCUI() {
		return ROOT + "/reallocateZCUI";
	}
	
	@RequestMapping("reallocateZC.do")
	@ResponseBody
	public String reallocateZC(HttpSession session, Integer zcid, Integer wxdeptNo, String new_cfdd) {
 		String djr = getCurrUser(session).getWxUsername();
		String resultStr = SUCCESS;
		try {
			CZCGL record=new CZCGL();
			record.setAppId(getAppId(session));
			record.setDeptNo(wxdeptNo);
			Integer new_zcglId=zcglDao.select(record).getId();
			zcdjService.reallocateZC(zcid, new_zcglId, new_cfdd, djr);
		} catch (OperException e) {
			resultStr = e.getMessage();
		}
		return resultStr;
	}
	
	/**
	 * 在更新时显示在存放地点的信息查询
	 * 
	 * @param session
	 * @param fjid
	 * @param m
	 * @return
	 */
	@RequestMapping("getCFDDTextbox.do")
	@ResponseBody
	public VFJ  getCFDDTextbox(HttpSession session,String fjid){
		if (StringUtils.isEmpty(fjid)) {
			return new VFJ();
		}
		VFJ param = new VFJ();
		Integer appId = getAppId(session);
		param.setAppId(appId);
		param.setFjId(Integer.parseInt(fjid));
		return fjService.getFJ(param);
	}
	
	@RequestMapping("importZCExcelUI.do")
	public String importZCExcelUI() {
		return ROOT + "/importZCExcelUI";
	}
	
	@RequestMapping("importZCExcel.do")
	@ResponseBody
	public String importZCExcel(HttpSession session, @RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		try {
			Integer appId = getAppId(session);
			String djr = getCurrUser(session).getWxUsername();
			zcdjService.insertZCListByExcel(appId, file_upload, djr);
			return SUCCESS;
		}catch(OperException e){
			return e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统错误";
		}
	}
	
	/**
	 * 资产数量（NUM）：可编辑数值框，默认值为1. 检查输入值是大于1的正整数
	 * @return
	 */
	@RequestMapping("checkNum.do")
	@ResponseBody
	public boolean checkNum(Integer num) {
		if (num == null) {
			return true;
		}
		if (num <= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 资产照片（PIC_URL）：上传文件框。可以为空。仅能上传.jpg格式文件，文件大小小于1MB。
	 * @return
	 */
	private boolean checkPIC(MultipartFile file_upload) {
		if (file_upload == null) {
			return false;
		}
		if (file_upload.getSize() > WXMediaService.MAX_IMAGE_SIZE) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 检查是否是输入的正数
	 * @return
	 */
	@RequestMapping("checkCost.do")
	@ResponseBody
	public Boolean checkCost(BigDecimal cost){
		BigDecimal compareNum = new BigDecimal("0");
		if (cost.compareTo(compareNum)==1) {
			return false;
		}else {
			return true;
		}
	}
	
	@RequestMapping("checkZCUI.do")
	public String checkZCUI (Integer id,Model m) {
		VZC param = new VZC();
		param.setZcid(id);
		VZC resultZC = zcService.getZC(param);
		m.addAttribute("zcInfo",resultZC);
		return ROOT+"/checkZCUI";
	}

	/**
	 * 对通过Excel批量导入的资产进行逐条审核
	 */
	@RequestMapping("checkZC.do")
	@ResponseBody
	public String ckeckZC(HttpSession session, Integer id, String mc, Integer lxid, String xh, String ccbh,Integer num,BigDecimal cost,
			String gzsj, BigDecimal zjnx,@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		String resultStr = SUCCESS;
		String djr=getCurrUser(session).getWxUsername();
		if (checkRegistZCByMC(session, mc)){
			resultStr = "ZCMC_ERROR";
			return resultStr;
		} else if (checkRegistZCByZJNX(session, zjnx)) {
			resultStr = "ZJNX_ERROR";
			return resultStr;
		} else if (checkNum(num)) {
			resultStr = "NUM_ERROR";
			return resultStr;
		} else if (checkPIC(file_upload)) {
			resultStr="PIC_ERROR";
			return resultStr;
		} else if (checkCost(cost)) {
			resultStr="COST_ERROR";
			return resultStr;
		}else {
			try {
				String Url = null;
				if (file_upload!=null) {
					Url = zcdjService.uploadPic(session, file_upload,getAppId(session));
				}
				zcdjService.checkUnregistedZC(djr,id, mc, lxid, cost, xh, ccbh, sdf.parse(gzsj), zjnx, Url);
			} catch (OperException e) {
				resultStr = e.getMessage();
			} catch (ParseException e) {
				resultStr = "您刚刚输入的日期无效，请重试";
			}
		}
		
		return resultStr;
	}
	
}
