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
	private LYHPMapper l_yhpDao;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	public static final OperErrCode 调拨易耗品失败 = new OperErrCode("11201", "调拨易耗品失败！ ");
	public static final OperErrCode 登记易耗品失败 = new OperErrCode("11202", "登记易耗品失败！ ");
	
	public int countYHPList(Integer appId, String lx, String fzr) {
		VYHP param=new VYHP();
		param.setAppId(appId);
		param.setLx(lx);
		param.setFzr(fzr);
		return vyhpDao.countYhpList(param);
	}
	
	public List<VYHP> getYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		return null;
	}
	
	public EasyuiDatagrid<VYHP> getPagedYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		int total=countYHPList(appId, lx, fzr);
		EasyuiDatagrid<VYHP> result=new EasyuiDatagrid<>();
		if(total>0){
			VYHP param=new VYHP();
			param.setAppId(appId);
			param.setLx(lx);
			param.setFzr(fzr);
			PageNavigator pageNav=new PageNavigator(rows, page);
			param.setStart(pageNav.getStart());
			param.setOffset(pageNav.getOffset());
			result.setRows(vyhpDao.selectYhpList(param));
		}else{
			result.setRows(new ArrayList<VYHP>());
		}
		result.setTotal(total);
		return result;
	}
	
	public JYHP getYHPByPrimaryKey(Integer yhpid){
		return jyhpDao.selectByPrimaryKey(yhpid);
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
			//向J_YHP表中插入一条记录。
			JYHP record=new JYHP();
			record.setAppId(appId);
			record.setZcglId(czbmId);
			record.setLxId(lxId);
			record.setPicUrl(picUrl);
			record.setXh(xh);
			record.setCcbh(ccbh);
			record.setNum(num);
			record.setLeftLimit(leftLimit);
			record.setCfdd(cfdd);
			record.setImgVersion(1);
			int i=jyhpDao.insertSelective(record);
			if(i<=0){
				throw new OperException(登记易耗品失败);
			}
			//向L_YHP表中插入一条记录
			LYHP record1=new LYHP();
			record1.setJlr(jlr);
			record1.setJlsj(new Date());
			record1.setCzbmId(czbmId);
			record1.setNum(num);
			record1.setCzlx(0);		//登记（0）、调拨（1）、领用（2）、报损（3）、入库（4）
			i=l_yhpDao.insertSelective(record1);
			if(i<=0){
				throw new OperException(登记易耗品失败);
			}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateYHP(Integer yhpId, String picUrl, String xh, String ccbh, Integer leftLimit, String cfdd) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delYHP(Integer yhpId) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addYHP(Integer yhpId, Integer addNum) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void allocateYHP(Integer yhpId, Integer allocateZCGLId, Integer allocateNum, String cfdd,String jlr) throws OperException {
		//向J_YHP表中更新指定记录的NUM（NUM-ALLOCATE_NUM），如果小于LEFT_LIMIT，发送预警消息
		JYHP record=jyhpDao.selectByPrimaryKey(yhpId);
		int num=record.getNum()-allocateNum;	//现在的持有数量
		record.setNum(num);
		jyhpDao.updateByPrimaryKeySelective(record);
		//NUM如果小于LEFT_LIMIT，发送预警消息
		//....do something
		//在J_YHP中插入一条新记录。LX_ID，XH，CCBH，LEFT_LIMIT，PIC_URL，IMG_VERSION延用原有记录；
		//ZCGL_ID，NUM，CFDD为用户新增值。
		record.setId(null);
		record.setZcglId(allocateZCGLId);
		record.setNum(allocateNum);
		record.setCfdd(cfdd);
		int i=jyhpDao.insertSelective(record);
		if(i<=0){
			throw new OperException(调拨易耗品失败);
		}
		//在L_YHP中插入一条调拨记录
		LYHP record1=new LYHP();
		record1.setJlr(jlr);
		record1.setJlsj(new Date());
		record1.setCzbmId(null);
		record1.setCzr(null);
		record1.setNum(allocateNum);
		record1.setCzlx(1);		//登记（0）、调拨（1）、领用（2）、报损（3）、入库（4）
		i=l_yhpDao.insertSelective(record1);
		if(i<=0){
			throw new OperException(调拨易耗品失败);
		}
	}

	public JYHP getYHP(Integer id) {
		return null;
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
	
	public Integer getCZBM_Id(Integer appId,String fzr){
		CZCGL c=new CZCGL();
		c.setAppId(appId);
		c.setFzr(fzr);
		c=zcglDao.select(c);
		return c.getId();
	}
}
