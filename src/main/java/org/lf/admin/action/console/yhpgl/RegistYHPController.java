package org.lf.admin.action.console.yhpgl;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.VYHP;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.utils.WXMediaService;
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
	private static final String ROOT = "/console/yhpgl/yhpdj";
	
	@Autowired
	private YHPService yhpService;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private ZCGLService zcglService;

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
	@RequestMapping("getQYYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHP> getQYYHPList(HttpSession session, String fzr, String lx,int rows,int page) {
		Integer appId=getAppId(session);
		if(StringUtils.isEmpty(lx)||lx.equals("全部")){
			lx=null;
		}
		Integer deptno=yhpService.getDeptNo(fzr);
		return yhpService.getYHPListByDeptNoAndYHPLX(appId, lxId, deptno, rows, page);
	}
	
	/**
	 * 查询v_yhp视图，获得指定appId下的所有易耗品列表。
	 * 
	 */
	@RequestMapping("getBMYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHP> getBMYHPList(HttpSession session, String fzr, String lxId,int rows,int page) {
		fzr=getCurrUser(session).getWxUsername();
		Integer appId=getAppId(session);
		if(StringUtils.isEmpty(lxId)){
			lxId=null;
		}
		Integer deptno=yhpService.getDeptNo(fzr);
		return yhpService.getYHPListByDeptNoAndYHPLX(appId, lxId, deptno, rows, page);
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
	public boolean checkLeftLimit(Integer leftLimit) {
		if(leftLimit==null || leftLimit<0){
			return false;
		}
		return true;
	}
	
	/**
	 * 持有数量不能小于持有下限
	 */
	public boolean compareNumAndLeftLimit(Integer num,Integer leftLimit){
		if(num==null || leftLimit==null){
			return false;
		}
		if(num<leftLimit){
			return false;
		}
		return true;
	}
	
	/**
	 * 易耗品照片（PIC_URL）：上传文件框。可以为空。仅能上传.jpg格式文件，文件大小小于1MB。
	 * @return
	 */
	private boolean checkPIC(MultipartFile file_upload) {
		if (file_upload == null || file_upload.getSize() > WXMediaService.MAX_IMAGE_SIZE) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 插入一个企业易耗品登记记录。这里，持有部门（zcglId）为空。
	 * 1. 向J_YHP表中插入一条记录
	 * 2. 向L_YHP中插入一条记录
	 */
	@RequestMapping("insertQYYHP.do")
	@ResponseBody
	public String insertQYYHP(HttpSession session, 
			Integer lx, String xh, String ccbh, Integer num, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		if(!checkNum(num)){
			return "持有数量不是大于0的正整数！";
		}
		if(!checkLeftLimit(leftLimit)){
			return "持有下限不是大于0的正整数！";
		}
		if(!compareNumAndLeftLimit(num, leftLimit)){
			return "持有数量小于持有下限 "+leftLimit;
		}
		if(!checkPIC(file_upload)){
			return "图片为空或图片文件大小大于1MB！";
		}
		String jlr=getCurrUser(session).getWxUsername();
		Integer appId=getAppId(session);
		String picUrl;
		try {
			picUrl = yhpService.uploadPic(session, file_upload, appId);
			yhpService.insertYHP(appId, jlr, null, lx, picUrl, xh, ccbh, num, leftLimit, cfdd);
		} catch (OperException e) {
			return e.getMessage();
		}
		
		return SUCCESS;
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
	public String insertBMYHP(HttpSession session, 
			Integer lx, String xh, String ccbh, Integer num, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		if(!checkNum(num)){
			return "持有数量不是大于0的正整数！";
		}
		if(!checkLeftLimit(leftLimit)){
			return "持有下限不是大于0的正整数！";
		}
		if(!checkPIC(file_upload)){
			return "图片为空或图片文件大小大于1MB！";
		}
		String jlr=getCurrUser(session).getWxUsername();
		Integer appId=getAppId(session);
		String picUrl;
		Integer czbmId=yhpService.getCZBM_Id(appId, jlr);
		try {
			picUrl = yhpService.uploadPic(session, file_upload, appId);
			yhpService.insertYHP(appId, jlr, czbmId, lx, picUrl, xh, ccbh, num, leftLimit, cfdd);;
		} catch (OperException e) {
			return e.getMessage();
		}
		
		return SUCCESS;
	}
	
	@RequestMapping("updateYHPUI.do")
	public String updateYHPUI(){
		return ROOT +"/updateYHPUI";
	}
	
	
	@RequestMapping("updateYHP.do")
	@ResponseBody
	public String updateQYYHP(HttpSession session,Integer yhpid, String xh, String ccbh, String cfdd, Integer leftLimit, 
			@RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		Integer appId = getAppId(session);
//		if(StringUtils.isEmpty(xh) || StringUtils.isEmpty(ccbh)){
//			return "规格型号和出厂编号不能为空！";
//		}
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
	public boolean checkAllocateNum(Integer num,Integer allocateNum) {
		if(allocateNum>num || allocateNum<1){
			return false;
		}
		return true;
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
	public String allocateQYYHP(HttpSession session,Integer yhpid, Integer allocateNum, Integer allocatebm_deptno, String cfdd) {
		String jlr=getCurrUser(session).getWxUsername();
		int num=yhpService.getYHPByPrimaryKey(yhpid).getNum();
		if(!checkAllocateNum(num,allocateNum)){
			return "调拨数量不是1-"+num+"范围内的正整数！";
		}
		CZCGL record=new CZCGL();
		record.setAppId(getAppId(session));
		record.setDeptNo(allocatebm_deptno);
		Integer zcgl_id=zcglDao.select(record).getId();
		try {
			yhpService.allocateYHP(yhpid, zcgl_id, allocateNum, cfdd,jlr);
		} catch (OperException e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	 * 如果易耗品没有被调拨，则可以删除易耗品
	 */
	@RequestMapping("delQYYHP.do")
	@ResponseBody
	public String deleteYHP(Integer yhpid){
		return null;
	}
}
