package org.lf.admin.action.console.yhpgl;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VYHP;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.yhpgl.YHPService;
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
 * 易耗品登记分为企业易耗品登记和部门易耗品登记。
 * 
 */
@Controller
@RequestMapping("/console/yhpgl/yhpdj/")
public class RegistYHPController extends BaseController {
	
	
	@Autowired
	private YHPService yhpService;
	
	@Autowired
	private ZCGLService zcglService;
	
	
	private static final String ROOT = "/console/yhpgl/yhpdj";

	/**
	 * 企业易耗品登记入库
	 */
	@RequestMapping("registQYYHPListUI.do")
	public String registQYYHPListUI(){
		return ROOT +"/registQYYHPListUI";
	}
	
	/**
	 * 部门易耗品登记入库
	 */
	@RequestMapping("registBMYHPListUI.do")
	public String registBMYHPListUI(){
		return ROOT +"/registBMYHPListUI";
	}
	
	/**
	 * 查询v_yhp视图，获得指定appId下的所有易耗品列表。
	 * 
	 */
	@RequestMapping("getYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHP> getYHPList(HttpSession session, String fzr, String lx,int rows,int page) {
		fzr=getCurrUser(session).getWxUsername();
		Integer appId=getAppId(session);
		if(StringUtils.isEmpty(lx)||lx.equals("全部")){
			lx=null;
		}
		return yhpService.getPagedYHPList(appId, lx, fzr, rows, page);
	}
	
	/**
	 * 登记企业低值易耗品
	 */
	@RequestMapping("insertQYYHPUI.do")
	public String insertQYYHPUI(){
		return ROOT +"/insertQYYHPUI";
	}
	
	/**
	 * 登记部门低值易耗品
	 */
	@RequestMapping("insertBMYHPUI.do")
	public String insertBMYHPUI(){
		return ROOT +"/insertBMYHPUI";
	}
	
	/**
	 * 持有数量（NUM）：数值型文本框，必填。默认值为1。大于0的正整数。
	 * 
	 */
	@RequestMapping("checkNum.do")
	@ResponseBody
	public boolean checkNum(Integer num) {
		if(num==null || num<=0){
			return false;
		}
		return true;
	}
	
	/**
	 * 持有下限（LEFT_LIMIT）：数值型文本框，必填。默认值为1。大于0的正整数。
	 * 
	 */
	@RequestMapping("checkLeftLimit.do")
	@ResponseBody
	public boolean checkLeftLimit() {
		return false;
	}
	
	/**
	 * 插入一个企业易耗品登记记录。这里，持有部门（zcglId）为空。
	 * 1. 向J_YHP表中插入一条记录
	 * 2. 向L_YHP中插入一条记录
	 */
	@RequestMapping("insertQYYHP.do")
	@ResponseBody
	public boolean insertQYYHP(HttpSession session, 
			String lx, String xh, String ccbh, Integer num, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		return false;
	}
	
	/**
	 * 插入一个部门易耗品登记记录。这里，持有部门（zcglId）为当前用户作为易耗品管理负责人所在部门。
	 * select dept_no from c_zcgl where appid=? and fzr=?。
	 * 
	 * 1. 向J_YHP表中插入一条记录
	 * 2. 向L_YHP中插入一条记录
	 */
	@RequestMapping("insertBMYHP.do")
	@ResponseBody
	public boolean insertBMYHP(HttpSession session, 
			String lx, String xh, String ccbh, Integer num, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
				return false;
	}
	
	@RequestMapping("updateYHPUI.do")
	public String updateYHPUI(){
		return ROOT +"/updateYHPUI";
	}
	
	@RequestMapping("updateYHP.do")
	@ResponseBody
	public String updateQYYHP(HttpSession session,Integer yhpid, String xh, String ccbh, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload)  {
		Integer appId = getAppId(session);
		String wxr = getCurrUser(session).getWxUsername();
		if(file_upload==null||file_upload.getSize()==0){
			String pic_url = null;
			try {
				yhpService.updateYHP(yhpid, pic_url, xh, ccbh, leftLimit, cfdd);
			} catch (OperException e) {
				return e.getMessage();
			}
			
		}else{
				try {
					String pic_url = yhpService.uploadPic(session, file_upload,appId);
					yhpService.updateYHP(yhpid, pic_url, xh, ccbh, leftLimit, cfdd);
				} catch (OperException e) {
					return e.getMessage();
				}
			}
			return SUCCESS;
		}
	
	/**
	 * 入库
	 * @return
	 */
	@RequestMapping("addYHPUI.do")
	public String addYHPUI(){
			return ROOT +"/addYHPUI";
	}
	
	@RequestMapping("checkAddNum.do")
	@ResponseBody
	public boolean checkAddNum() {
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("addYHP.do")
	@ResponseBody
	public String addYHP(HttpSession session,Integer yhpid, Integer addNum) {
		String jlr=getCurrUser(session).getWxUsername();
		int appid=getAppId(session);
		try {
			yhpService.addYHP(appid,jlr,yhpid, addNum);
		} catch (OperException e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	
	@RequestMapping("isAdmin.do")
	@ResponseBody
	public String isAdmin(HttpSession session){
		String fzr=getCurrUser(session).getWxUsername();
		Integer appId=getAppId(session);
		try {
			zcglService.getFZR(appId, fzr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
		
	}
	
	/**
	 * 调拨
	 * @return
	 */
	@RequestMapping("allocateQYYHPUI.do")
	public String allocateQYYHPUI(HttpSession session, Model m){
		return ROOT +"/allocateQYYHPUI";
	}
	
	@RequestMapping("checkAllocateNum.do")
	@ResponseBody
	public boolean checkAllocateNum() {
		return false;
	}
	
	/**
	 * 1. 向J_YHP表中更新指定记录的
	 * 		NUM（NUM-ALLOCATE_NUM），如果小于LEFT_LIMIT，发送预警消息。
	 * 2. 在J_YHP中插入一条新记录。
	 * 		LX_ID，XH，CCBH，LEFT_LIMIT，PIC_URL，IMG_VERSION延用原有记录；
	 * 		ZCGL_ID，NUM，CFDD为用户新增值。
	 * 3. 在L_YHP中插入一条记录
	 * 	记录人（JLR）：当前用户userid
	 * 	记录时间（JLSJ）：系统时间
	 * 	操作部门（CZBM_ID）：为空。
	 * 	数量（NUM）：调拨数量
	 * 	操作类型（CZLX）：调拨
	 * 	备注：<JLSJ><LX>易耗品由<JLR>调拨给<allocateZCGLID>，数量为<allocateNum>，存放在<CFDD>
	 * @return
	 */
	@RequestMapping("allocateQYYHP.do")
	@ResponseBody
	public boolean allocateQYYHP(Integer yhpid, Integer allocateNum, Integer allocateZCGLId, String cfdd) {
		return false;
	}
}
