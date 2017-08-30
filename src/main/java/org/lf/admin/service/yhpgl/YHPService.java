package org.lf.admin.service.yhpgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JYHPMapper;
import org.lf.admin.db.dao.LYHPMapper;
import org.lf.admin.db.dao.VYHPMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.JYHP;
import org.lf.admin.db.pojo.LYHP;
import org.lf.admin.db.pojo.VYHP;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 易耗品管理
 * 
 */
@Service("yhpService")
public class YHPService {
	
	@Autowired
	private VYHPMapper vyhpDao;
	
	@Autowired
	private JYHPMapper jyhpDao;
	
	@Autowired 
	private LYHPMapper lyhpDao;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	@Autowired
	private CZCGLMapper czcglDao;
	
	public static final OperErrCode 补货易耗品失败 = new OperErrCode("11203", "补货易耗品失败！ ");
	public static final OperErrCode 编辑易耗品失败 = new OperErrCode("11204", "编辑易耗品失败！ ");
	
	public int countYHPList(Integer appId, String lx, String fzr) {
		return -1;
	}
	
	public List<VYHP> getYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		VYHP param=new VYHP();
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		param.setAppId(appId);
		param.setLx(lx);
		param.setDeptNo(getDeptNo(fzr));
		return vyhpDao.selectYhpList(param);
	}
	
	
	public Integer getDeptNo(String fzr){
		CZCGL record=new CZCGL();
		record.setFzr(fzr);
		CZCGL c=new CZCGL();
		c=czcglDao.select(record);
		return c.getDeptNo();
	}
	
	
	public EasyuiDatagrid<VYHP> getPagedYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		VYHP param=new VYHP();
		param.setAppId(appId);
		param.setLx(lx);
		param.setDeptNo(getDeptNo(fzr));
		int total=vyhpDao.countYhpList(param);
		EasyuiDatagrid<VYHP> pageDatas=new EasyuiDatagrid<>();
		pageDatas.setTotal(total);
		if(total == 0){
			pageDatas.setRows(new ArrayList<VYHP>());
		}else{
			List<VYHP> list = getYHPList(appId, lx, fzr, rows, page);
			pageDatas.setRows(list);
		}
 		return pageDatas;
	}
	
	
public String uploadPic(HttpSession session, MultipartFile file_upload, Integer appid) throws OperException {
		
		String returnUrl = "";
		// 根据appid生成文件前缀
		String prePath = ZCGLProperties.URL_YHP_TARGET_DIR + "/" + appid;
		MultipartFile[] FileList = new MultipartFile[]{file_upload};
		String FileName = file_upload.getOriginalFilename();
		String fileType=FileName.substring(FileName.lastIndexOf('.'),FileName.length());
		returnUrl = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, fileType, FileList);
		return returnUrl;
	}
	
	
	/**
	 * 企业易耗品登记，czbmId为空；部门易耗品登记，操作部门为用户所在部门id号。
	 * select id from c_zcgl where appid=? and fzr=?
	 * 这里，负责人（fzr）为当前用户userid。
	 *  
	 * 
	 * 	向J_YHP表中插入一条记录。
	 * 	向L_YHP表中插入一条记录：
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHP(Integer appId, String jlr, Integer czbmId, Integer lxId, String picUrl, String xh, String ccbh, Integer num,
			Integer leftLimit, String cfdd) throws OperException {

	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateYHP(Integer yhpId, String picUrl, String xh, String ccbh, Integer leftLimit, String cfdd) throws OperException {
		JYHP param=jyhpDao.selectByPrimaryKey(yhpId);
		if(param.getImgVersion()==null){
			param.setImgVersion(1);
		}else{
			param.setImgVersion(param.getImgVersion()+1);
		}
		param.setId(yhpId);
		param.setCfdd(cfdd);
		param.setPicUrl(picUrl);
		param.setXh(xh);
		param.setCcbh(ccbh);
		param.setLeftLimit(leftLimit);
		jyhpDao.updateByPrimaryKeySelective(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delYHP(Integer yhpId) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addYHP(Integer appId,String jlr,Integer yhpId, Integer addNum) throws OperException {
		JYHP param=jyhpDao.selectByPrimaryKey(yhpId);
		param.setNum(param.getNum()+addNum);
		int i=jyhpDao.updateByPrimaryKey(param);
		if(i<=0){
			throw new OperException(补货易耗品失败);
		}
		
		CZCGL c=new CZCGL();
		c.setFzr(jlr);
		c.setAppId(appId);
		List<CZCGL> temp=czcglDao.selectZCGLList(c);
		
		
		LYHP record=new LYHP();
		record.setNum(addNum);
		record.setCzlx(5);             //登记（0）、调拨（1）、领用（2）、报损（3）、入库（4）、补货（5）
		record.setJlr(jlr);
		record.setCzbmId(temp.get(0).getId());
		record.setJlsj(new Date());
		record.setYhpId(yhpId);
		record.setCzr(temp.get(0).getFzr());
		int m=lyhpDao.insertSelective(record);
		if(m<=0){
			throw new OperException(补货易耗品失败);
		}
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void allocateYHP(Integer yhpId, Integer allocateZCGLId, Integer allocateNum, String cfdd) throws OperException {
		
	}

	public JYHP getYHP(Integer id) {
		return null;
	}
}
