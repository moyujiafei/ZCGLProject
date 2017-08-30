package org.lf.admin.service.zcgl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.LZTMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.utils.DateUtils;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产登记：资产登记、资产调拨
 * 
 * @author 文臣
 */
@Service("zcdjService")
public class ZCDJService {
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private FJService fjService;
	
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private JZCMapper jzcMapper;
	
	@Autowired
	private LZTMapper lztMapper;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	/**
	 * 后勤管理人员：资产登记
	 * 对新购资产进行登记入库。 所谓新购资产，指的是该资产尚未分配给相关部门使用（资产管理为空）。
	 * 
	 * 1. 创建一个资产，将资产状态设为“已登记”
	 * 2. 将资产插入到J_ZC表中。
	 * 3. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：空
	 * 	新状态：已登记
	 * 	备注：<资产名称>(<资产代码>)于<系统时间>登记入库
	 * 
	 * @param djr 资产登记人userid
	 * 
	 * 
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public JZC registZC(Integer appId, String zcdm, String zcmc, Integer zcLx, BigDecimal cost, Integer num,
			            String xh, String ccbh, Date gzsj, BigDecimal zjnx, String djr,String picUrl) throws OperException {
		
		//在J_ZC中插入一条记录
		JZC zc = new JZC();
		zc.setAppId(appId);
		zc.setDm(zcdm);
		zc.setMc(zcmc);
		zc.setLxId(zcLx);
		zc.setXh(xh);
		zc.setCcbh(ccbh);
		zc.setZjnx(zjnx);
		zc.setGzsj(gzsj);
		zc.setCost(cost);
		zc.setZt(ZCZT.已登记.getValue());
		zc.setPicUrl(picUrl);
		jzcMapper.insertSelective(zc);
		
		//在L_ZT表中插入一条记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zcdm);
		zt.setJlsj(new Date());
		zt.setJlr(djr);
		zt.setOldZt(null);
		zt.setNewZt(ZCZT.已登记.getValue());
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(zcmc).append("（").append(zcdm).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date())).append("登记入库");
		zt.setRemark(resultSb.toString());
		ztService.insertZT(zt);
		
		return zc;
	}
	
	/**
	 * 后勤管理人员：资产未登记
	 * 对新购资产进行批量入库。这些资产尚未提供资产照片。
	 * 
	 * 1. 创建一个资产，将资产状态设为“未登记”
	 * 2. 将资产插入到J_ZC表中。
	 * 3. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：空
	 * 	新状态：未登记
	 * 	备注：<资产名称>(<资产代码>)于<系统时间>批量入库
	 * 
	 * @param djr 资产登记人userid
	 * 
	 * 
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public JZC unregistZC(Integer appId, String zcdm, String zcmc, Integer zcLx, 
			              String xh, String ccbh, Date gzsj, BigDecimal zjnx, String djr,BigDecimal cost,Integer num) throws OperException {
		
		//在J_ZC中插入一条记录
		JZC zc = new JZC();
		zc.setAppId(appId);
		zc.setDm(zcdm);
		zc.setMc(zcmc);
		zc.setLxId(zcLx);
		zc.setXh(xh);
		zc.setCcbh(ccbh);
		zc.setZjnx(zjnx);
		zc.setGzsj(gzsj);
		zc.setCost(cost);
		zc.setNum(num);
		zc.setZt(ZCZT.未审核.getValue());
		jzcMapper.insertSelective(zc);
		
		//在L_ZT表中插入一条记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zcdm);
		zt.setJlsj(new Date());
		zt.setJlr(djr);
		zt.setOldZt(null);
		zt.setNewZt(ZCZT.未审核.getValue());
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(zcmc).append("（").append(zcdm).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date())).append("批量入库");
		zt.setRemark(resultSb.toString());
		ztService.insertZT(zt);
		
		return zc;
	}
	
	/**
	 * 根据导入的excel表格导入数据
	 * @param appId
	 * @param file
	 * @param djr
	 * @throws OperException
	 * @throws IOException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCListByExcel (Integer appId,MultipartFile file,String djr) throws OperException, IOException {
		InputStream fileStream = ExcelFileUtils.importExcel(file);
		List<JZC> zcList = parseFileToJZCList(fileStream,appId);
		for (JZC zc : zcList) {
			unregistZC(appId, zc.getDm(), zc.getMc(), zc.getLxId(), zc.getXh(), zc.getCcbh(), zc.getGzsj(),zc.getZjnx(), djr,zc.getCost(),zc.getNum());
		}
	}
	/**
	 * 验证导入的Excel表中的各个字段是否合法
	 * @param appId
	 * @param sheet
	 * @param row
	 * @param lastRow
	 * @return
	 * @throws OperException
	 */
	private List<JZC> validExcelRow (Integer appId,HSSFSheet sheet,HSSFRow row,int lastRow) throws OperException {
		List<JZC> zcList = new ArrayList<JZC>();
		JZC zc = null;
		for (int i=1;i<lastRow;i++) {
			row = sheet.getRow(i);
			
			String temp = ExcelFileUtils.getCellValue(row.getCell(0))+ ExcelFileUtils.getCellValue(row.getCell(1)) +
					ExcelFileUtils.getCellValue(row.getCell(2)) + ExcelFileUtils.getCellValue(row.getCell(3)) + 
					ExcelFileUtils.getCellValue(row.getCell(4)) + ExcelFileUtils.getCellValue(row.getCell(5)) +
					ExcelFileUtils.getCellValue(row.getCell(6)) + ExcelFileUtils.getCellValue(row.getCell(7)) +
					ExcelFileUtils.getCellValue(row.getCell(8));
			if(StringUtils.isEmpty(temp)){
				continue;
			}
			String requiredField;
			Boolean isRight = true;
			int lastCell = 9;
			for (int j =0;j<lastCell;j++) {
				if (j>4&&j<7) {
					continue;
				} else {
					requiredField = ExcelFileUtils.getCellValue(row.getCell(j));
				}
				if (StringUtils.isEmpty(requiredField)) {
					isRight = false;
					break;
				}
			}
			if (!isRight) {
				throw new OperException(new OperErrCode("00000", "第%d行，红色部分为必填项，必须全部填写",row.getRowNum()+1));
			} else {
				zc = new JZC();
				String zcdm = ExcelFileUtils.getCellValue(row.getCell(0));
				JZC zcparam = new JZC();
				zcparam.setDm(zcdm);
				zcparam.setAppId(appId);
				if (jzcMapper.countZCList(zcparam) > 0) {
					throw new OperException(new OperErrCode("00001", "第%d行，该资产代码已经存在", row.getRowNum()+1));
				}
				zc.setDm(zcdm);
				zc.setMc(ExcelFileUtils.getCellValue(row.getCell(1)));
				CZCLX zclxparam = new CZCLX();
				zclxparam.setMc(ExcelFileUtils.getCellValue(row.getCell(2)));
				CZCLX zclx = zclxService.getZCLX(zclxparam);
				if (zclx == null) {
					throw new OperException(new OperErrCode("00002", "第%d行，该资产类型不存在", row.getRowNum()+1));
				}
				zc.setLxId(zclx.getId());
				//验证cost>0
				BigDecimal compareCost = new BigDecimal("0");
				Double costDouble = null;
				try {
					costDouble = row.getCell(3).getNumericCellValue();
				} catch (Exception e) {
					throw new OperException(new OperErrCode("00003", "第%d行，单价只能为正数", row.getRowNum()+1));
				}
				BigDecimal cost = new BigDecimal(costDouble);
				if (!(cost.compareTo(compareCost)==1)) {
					throw new OperException(new OperErrCode("00003", "第%d行，单价只能为正数", row.getRowNum()+1));
				}
				zc.setCost(cost);
				Integer num = null;
				Double doubleNum = null;
				try {
					doubleNum = row.getCell(4).getNumericCellValue();
					
				} catch (Exception e) {
					throw new OperException(new OperErrCode("00003", "第%d行，资产数量只能为正整数", row.getRowNum()+1));
				}
				DecimalFormat df = new DecimalFormat("0");
				Double newNum = doubleNum - new Double(df.format(doubleNum));
				if (newNum > 0.0) {
					throw new OperException(new OperErrCode("00004", "第%d行，资产数量只能为正整数", row.getRowNum()+1));
				}
				num = doubleNum.intValue();
				if (num<=0) {
					throw new OperException(new OperErrCode("00005", "第%d行，资产数量不能小于或等于零", row.getRowNum()+1));
				}
				zc.setNum(num);
				zc.setXh(ExcelFileUtils.getCellValue(row.getCell(5)));
				zc.setCcbh(ExcelFileUtils.getCellValue(row.getCell(6)));
				Date date = null;
				try {
					date = row.getCell(7).getDateCellValue();
					
				} catch (Exception e) {
					throw new OperException(new OperErrCode("00006", "第%d行，购置时间的格式为xxxx年/xx月/xx日", row.getRowNum()+1));
				}
				if (date != null) {
					if ((date.getYear()+1900) > 9999) {
						throw new OperException(new OperErrCode("00007", "第%d行，购置时间不能大于9999年12月31日", row.getRowNum()+1));
					}
					zc.setGzsj(date);
				}
				String zjnxStr = null;
				BigDecimal zjnx = null;
				zjnxStr = ExcelFileUtils.getCellValue(row.getCell(8));
				try {
					zjnx = new BigDecimal(zjnxStr);
				} catch (Exception e) {
					throw new OperException(new OperErrCode("00009", "第%d行，折旧年限必须是数值", row.getRowNum()+1));
				}
				BigDecimal compareNum = new BigDecimal("0");
				if (!(zjnx.compareTo(compareNum) == 1)) {
					throw new OperException(new OperErrCode("00008", "第%d行，折旧年限必须是正数", row.getRowNum()+1));
				}
				zc.setZjnx(zjnx);
				
				zcList.add(zc);
			}
		}
		return zcList;
	}
	
	/**
	 * 将导入的Excel表格变成的数据变成java对象，存放在List<JZC>里面
	 * @param inputStream
	 * @return
	 * @throws OperException
	 * @throws IOException 
	 */
	private List<JZC> parseFileToJZCList(InputStream inputStream,Integer appId) throws OperException, IOException {
		POIFSFileSystem poifsFileSystem = null;
		HSSFWorkbook hssfWorkbook = null;
		List<JZC>  resultList= new ArrayList<JZC>(); 
		try {
			poifsFileSystem = new POIFSFileSystem(inputStream);
			hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			HSSFRow row = null;
			int lastRow = sheet.getLastRowNum();
			if (lastRow <= 1) {
				hssfWorkbook.close();
				throw new OperException(FJService.读取Excel文件异常);
			}
			resultList = validExcelRow(appId, sheet, row, lastRow);
			
		} finally {
			
			if (hssfWorkbook !=null) {
				try {
					hssfWorkbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (poifsFileSystem != null) {
				try {
					poifsFileSystem.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 对未审核资产进行审核。
	 * 
	 * 1. 资产状态设为“已登记”
	 * 3. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：未审核
	 * 	新状态：已登记
	 * 	备注：<资产名称>(<资产代码>)于<系统时间>进行资产登记审核
	 * 
	 * @param djr 资产登记人userid
	 * 
	 * 
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void checkUnregistedZC(String djr,Integer zcId, String zcmc, Integer zcLx, BigDecimal cost,
                                 String xh, String ccbh, Date gzsj, BigDecimal zjnx,String picUrl) throws OperException {
		JZC param = jzcMapper.selectByPrimaryKey(zcId);
		
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setMc(zcmc);
		zc.setLxId(zcLx);
		zc.setXh(xh);
		zc.setCcbh(ccbh);
		zc.setGzsj(gzsj);
		zc.setZjnx(zjnx);
		zc.setPicUrl(picUrl);
		zc.setImgVersion(param.getImgVersion()+1);
		zc.setZt(ZCZT.已登记.getValue());
		
		jzcMapper.updateByPrimaryKeySelective(zc);
		
		//在L_ZT表中插入一条记录。
		LZT zt = new LZT();
		JZC resultZc = zcService.getZC(zcId);
		zt.setAppId(resultZc.getAppId());
		zt.setZcdm(resultZc.getDm());
		zt.setJlr(djr);
		zt.setJlsj(new Date());
		zt.setOldZt(ZCZT.未审核.getValue());
		zt.setNewZt(ZCZT.已登记.getValue());		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(resultZc.getMc()).append("（").append(resultZc.getDm()).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date())).append("进行资产登记审核");
		zt.setRemark(resultSb.toString());
		lztMapper.insertSelective(zt);
	}
	
	/**
	 * 后勤管理人员：更新已登记的资产信息。此处，资产代码（zcdm）不能更新。
	 * 已登记资产，能够更新的字段如下：
	 * @param zcmc 资产名称 
	 * @param zcLx 资产类型
	 * @param xh 型号
	 * @param ccbh 出厂编号
	 * @param gzsj 购置时间
	 * @param zjnx 折旧年限
	 * 
	 * 1. 依据zcId更新J_ZC相应记录
	 * 
	 * @param djr 记录人userid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRegistedZC(Integer zcId, String zcmc, Integer zcLx,BigDecimal cost, 
                                 String xh, String ccbh, Date gzsj, BigDecimal zjnx,String picUrl) throws OperException {
		
		JZC param = jzcMapper.selectByPrimaryKey(zcId);
		
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setMc(zcmc);
		zc.setLxId(zcLx);
		zc.setXh(xh);
		zc.setCcbh(ccbh);
		zc.setGzsj(gzsj);
		zc.setZjnx(zjnx);
		zc.setPicUrl(picUrl);
		zc.setCost(cost);
		zc.setImgVersion(param.getImgVersion()+1);
		
		jzcMapper.updateByPrimaryKeySelective(zc);
	}
	
	/**
	 * 后勤管理人员：删除已登记的资产信息。
	 * 1. 依据zcId删除J_ZC相应记录
	 * 2. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：已登记
	 * 	新状态：空
	 * 	备注：<资产名称>(<资产代码>)于<系统时间>从已登记资产库中删除
	 * 
	 * @param zcId
	 * @param djr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delRegistedZC(Integer zcId, String djr) throws OperException {
		//先查相应的数据
		JZC resultZc = zcService.getZC(zcId);
		//依据zcId删除J_ZC相应记录
		JZC zc = new JZC();
		zc.setId(zcId);
		zcService.delZC(zc);
		
		//在L_ZT表中插入一条记录
		LZT zt = new LZT();
		zt.setAppId(resultZc.getAppId());
		zt.setZcdm(resultZc.getDm());
		zt.setJlr(djr);
		zt.setJlsj(new Date());
		zt.setOldZt(ZCZT.已登记.getValue());
		zt.setNewZt(null);		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(resultZc.getMc()).append("（").append(resultZc.getDm()).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date())).append("从已登记资产库中删除");
		zt.setRemark(resultSb.toString());
		lztMapper.insertSelective(zt);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delRegistedZC(List<Integer> zcidList, String djr) throws OperException {
		for (Integer zcid : zcidList) {
			delRegistedZC(zcid, djr);
		}
	}
	/**
	 * 资产调拨后，向资产管理员发送信息：
	 * 【系统通知】：<资产名称>（<资产代码>）于<系统时间>调拨给<资产所属部门>使用，存放地点为<存放地点>。点击查看<资产详情>
	 * 
	 * @throws OperException
	 */
	private void sendAllocateZCMsg(JZC zc, CZCGL zcgl) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(zc.getMc()).append("（").append(zc.getDm()).append("）于");
		sb.append(DateUtils.getLongDate(new Date())).append("调拨给");
		sb.append(zcgl.getDeptName()).append("使用");
		
		if (! StringUtils.isEmpty(zc.getCfdd())) {
			sb.append("，存放地点为").append(fjService.getCFDD(Integer.parseInt(zc.getCfdd())));
		}
		sb.append("点击").append(msgTemplateService.getZCXQ(zc));

		// 给资产管理员发消息
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, zcgl.getGlr(), sb.toString());
	}
	
	/**
	 * 后勤管理人员：资产调拨
	 * 
	 * 将已购置的资产，调拨给下属部门使用。即为资产分配资产管理部门。
	 * 
	 * 1. 设置资产的资产管理编号，存放地点
	 * 2. 将资产状态设为“未使用”
	 * 3. 更新J_ZC表
	 * 4. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：已登记
	 * 	新状态：未使用
	 * 	备注：资产名称(资产代码)于<系统时间>分配给<资产所属部门>使用，存放地点为<存放地点>。  （注：如果cfdd为空，不显示后半部分信息）
	 * 4. 向资产管理员发送消息：sendAllocateZCMsg(JZC zc, CZCGL zcgl)
	 * 
	 * @param zcglId 资产管理编号
	 * @param cfdd 存放地点
	 * @param djr 资产登记人userid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void allocateZC(Integer zcId, Integer zcglId, String cfdd, String djr) throws OperException {
		//更新J_ZC表
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZcglId(zcglId);
		zc.setCfdd(cfdd);
		zc.setZt(ZCZT.未使用.getValue());
		jzcMapper.updateByPrimaryKeySelective(zc);
		
		//在L_ZT表中插入一条记录
		JZC resultZc = zcService.getZC(zcId); 
		CZCGL zcgl = zcglService.getZCGL(zcglId);
		LZT zt = new LZT();
		zt.setAppId(resultZc.getAppId());
		zt.setJlr(djr);
		zt.setJlsj(new Date());
		zt.setOldZt(ZCZT.已登记.getValue());
		zt.setNewZt(ZCZT.未使用.getValue());
		zt.setZcdm(resultZc.getDm());
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(resultZc.getMc()).append("（").append(resultZc.getDm()).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("分配给").append(zcgl.getDeptName()).append("使用");
		if(!StringUtils.isEmpty(resultZc.getCfdd())) {
			resultSb.append("，存放地点为").append(fjService.getCFDD(Integer.parseInt(cfdd)));
		}
		zt.setRemark(resultSb.toString());
		ztService.insertZT(zt);
		sendAllocateZCMsg(resultZc,zcgl);
	}
	
	/**
	 * 后勤管理人员：对一组资产批量进行调拨
	 * 
	 * 将已购置的资产，调拨给下属部门使用。即为资产分配资产管理部门。
	 * 
	 * 1. 设置资产的资产管理编号
	 * 2. 将资产状态设为“未使用”
	 * 3. 每一个资产，在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：已登记
	 * 	新状态：未使用
	 * 	备注：资产于<系统时间>分配给<资产所属部门>使用，存放地点为<存放地点>。  （注：如果cfdd为空，不显示后半部分信息）
	 * 4. 每一个资产，向资产管理员发送消息： 
	 * 
	 * @see #sendAllocateZCMsg(org.lf.admin.db.pojo.JZC, org.lf.admin.db.pojo.CZCGL)
	 * 
	 * @param djr 资产登记人userid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void allocateZC(List<Integer> zcIdList, Integer zcglId, String cfdd, String djr) throws OperException {
		for (Integer zcId : zcIdList) {
			allocateZC(zcId, zcglId, cfdd, djr);
		}
	}
	
	/**
	 * <资产名称>（<资产编码>）于<系统时间>由<原有资产所属部门>使用，存放地点为<原有存放地点>；变更为<新的资产所属部门>使用，存放地点为<新的存放地点>。点击查看<资产详情>
	 * 
	 * @param zc
	 * @param old_zcgl
	 * @param new_zcgl
	 * @throws OperException
	 */
	private void sendReallocateZCMsg(JZC zc, CZCGL old_zcgl, String old_cfdd, CZCGL new_zcgl, String new_cfdd) throws OperException {
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）");
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		if (StringUtils.isEmpty(old_cfdd)) {
			resultSb.append("由").append(old_zcgl.getDeptName()).append("使用，存放地点为").append("未知").append("；");
		} else {
			resultSb.append("由").append(old_zcgl.getDeptName()).append("使用，存放地点为").append(fjService.getCFDD(Integer.parseInt(old_cfdd))).append("；");
		}
		if (StringUtils.isEmpty(new_cfdd)) {
			resultSb.append("变更为").append(new_zcgl.getDeptName()).append("使用，存放地点为").append("未知");
		} else {
			resultSb.append("变更为").append(new_zcgl.getDeptName()).append("使用，存放地点为").append(fjService.getCFDD(Integer.parseInt(new_cfdd)));
		}
		resultSb.append("。点击").append(msgTemplateService.getZCXQ(zc));
		
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, old_zcgl.getGlr(), resultSb.toString());
		if (old_zcgl.getId() != new_zcgl.getId()) {
			msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, new_zcgl.getGlr(), resultSb.toString());
		}
	}

	/**
	 * 对已调拨的资产，进行更新更新。
	 * @param zcId
	 * @param old_zcglId 原有资产管理部门  数据来自zcId对应的值。
	 * @param old_cfdd 原有存放地点
	 * @param new_zcglId 新的资产管理部门
	 * @param new_cfdd 新的存放地点
	 * @param djr 登记人userid
	 * 
	 * 1. 依据zcId更新zcglId和cfdd
	 * 2. 在L_ZT表中插入一条记录。
	 * 		记录人：djr
	 * 	原状态：未使用
	 * 	新状态：未使用
	 * 	备注：资产于<系统时间>由<原有资产所属部门>使用，存放地点为<原有存放地点>；变更为<新的资产所属部门>使用，存放地点为<新的存放地点>。
	 * 3. 如果old_zcglId和new_zcglId相同（仅仅修改存放地点），向资产管理员发送消息：sendReallocateZCMsg
	 *    如果old_zcglId和new_zcglId不同，分别向两个资产管理员发送消息：sendReallocateZCMsg
	 *    @see #sendUpdateAllocateZCMsg
	 * 
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void reallocateZC(Integer zcId, Integer new_zcglId, String new_cfdd, String djr) throws OperException {
		
		StringBuilder resultSb = new StringBuilder();
		
		//先查询原有数据
		JZC oldZc = zcService.getZC(zcId);
		CZCGL oldZcgl = zcglService.getZCGL(oldZc.getZcglId());
		
		//再根据zcid更新zcglid和cfdd
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZcglId(new_zcglId);
		zc.setCfdd(new_cfdd);
		jzcMapper.updateByPrimaryKeySelective(zc);
		
		//查询数据可得到对应的所属部门和存放地点
		CZCGL newZcgl = zcglService.getZCGL(new_zcglId);
		LZT zt = new LZT();
		zt.setZcdm(oldZc.getDm());
		zt.setAppId(oldZc.getAppId());
		zt.setJlr(djr);
		zt.setJlsj(new Date());
		zt.setOldZt(ZCZT.未使用.getValue());
		zt.setNewZt(ZCZT.未使用.getValue());
		resultSb.append("资产于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("由").append(oldZcgl.getDeptName()).append("使用，存放地点为");
		if (StringUtils.isEmpty(oldZc.getCfdd())) {
			oldZc.setCfdd("");
			resultSb.append("未知");
		} else {
			resultSb.append(fjService.getCFDD(Integer.parseInt(oldZc.getCfdd())));
		}
		if (StringUtils.isEmpty(new_cfdd)) {
			resultSb.append("；变更为").append(newZcgl.getDeptName()).append("使用，存放地点为").append("未知");
		} else {
			resultSb.append("；变更为").append(newZcgl.getDeptName()).append("使用，存放地点为").append(fjService.getCFDD(Integer.parseInt(new_cfdd)));
		}
		
		zt.setRemark(resultSb.toString());
		ztService.insertZT(zt);
		
		//发送消息给对应的管理员
		JZC newZc = zcService.getZC(zcId);
		sendReallocateZCMsg(newZc, oldZcgl, oldZc.getCfdd(), newZcgl, new_cfdd);
	}
	
	/**
	 * 批量更新一组资产已调拨的资产
	 * @param zcId
	 * @param new_zcglId
	 * @param new_cfdd
	 * @param djr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void reallocateZC(List<Integer> zcIdList, Integer new_zcglId, String new_cfdd, String djr) throws OperException {
		for (Integer zcId : zcIdList) {
			reallocateZC(zcId, new_zcglId, new_cfdd, djr);
		}
	}
	
	/**
	 * 后勤管理人员：同意部门资产管理员提交的归还资产申请。上交资产，即将资产重新上交给后勤仓库。
	 * 
	 * 1. 更新J_ZC状态
	 * 	将选中资产状态置为“已登记”
	 *      将资产管理编号置为空。
	 * 2.   在L_ZCZT中添加记录。
	 * 	记录人：创建人（cjr），后勤管理人员userid
	 * 	原状态：归还中
	 * 	新状态：已登记
	 *      备注：同意归还<资产名称>(<资产编号>)
	 * 3.   通知部门资产管理员：sendAgreeSQMsg
	 * 
	 * @see MsgTemplateService#sendAgreeSQMsg(String, JZC, ZCZT, String)
	 * 
	 * @param appId
	 * @param cjr
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeRevertSQ(Integer appId, String cjr, List<Integer> zcidList) throws OperException {
		for (Integer zcid : zcidList) {
			//更新J_ZC状态
			JZC zc = new JZC();
			zc.setId(zcid);
			zc.setAppId(appId);
			zc.setZt(ZCZT.已登记.getValue());
			zc.setZcglId(null);
			jzcMapper.updateByPrimaryKeySelective(zc);
			
			//在L_ZCZT中添加记录。
			LZT zt = new LZT();
			JZC newZc = zcService.getZC(zcid);
			CZCGL zcgl = zcglService.getZCGL(newZc.getZcglId());
			zt.setZcdm(newZc.getDm());
			zt.setAppId(appId);
			zt.setJlr(cjr);
			zt.setJlsj(new Date());
			zt.setOldZt(ZCZT.归还中.getValue());
			zt.setNewZt(ZCZT.已登记.getValue());
			StringBuilder resultSb = new StringBuilder();
			resultSb.append("同意归还").append(newZc.getMc()).append("(").append(newZc.getDm()).append(")");
			zt.setRemark(resultSb.toString());
			ztService.insertZT(zt);
			
			//通知部门资产管理员：sendAgreeSQMsg
			msgTemplateService.sendAgreeSQMsg(cjr, newZc, ZCZT.归还中, zcgl.getGlr());
		}
	}
	
	/**
	 * 后勤管理人员：拒绝部门资产管理员提交的归还资产申请。
	 * 
	 * 1.	更新J_ZC状态
	 * 	将选中资产状态置为“使用中”。
	 * 2.   在L_ZCZT中添加记录。
	 * 	记录人：创建人（cjr），部门资产管理员userid
	 * 	原状态：归还中
	 * 	新状态：未使用
	 * 	备注：由于<拒绝原因>拒绝您归还<资产名称>(<资产编号>)的申请。
	 *  3.  通知部门资产管理员：sendRefuseSQMsg
	 * 
	 * @see MsgTemplateService#sendRefuseSQMsg
	 * 
	 * @param appId
	 * @param refuseRemark 拒绝理由
	 * @param cjr 创建人，部门资产管理员userid
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseRevertSQ(Integer appId, String refuseRemark, String cjr, List<Integer> zcidList) throws OperException {
		
		for (Integer zcid : zcidList) {
			//更新J_ZC状态
			JZC zc = new JZC();
			zc.setId(zcid);
			zc.setAppId(appId);
			zc.setZt(ZCZT.使用中.getValue());
			jzcMapper.updateByPrimaryKeySelective(zc);
			
			//在L_ZCZT中添加记录。
			LZT zt = new LZT();
			JZC newZc = zcService.getZC(zcid);
			CZCGL zcgl = zcglService.getZCGL(newZc.getZcglId());
			zt.setZcdm(newZc.getDm());
			zt.setAppId(appId);
			zt.setJlr(cjr);
			zt.setJlsj(new Date());
			zt.setOldZt(ZCZT.归还中.getValue());
			zt.setNewZt(ZCZT.未使用.getValue());
			StringBuilder resultSb = new StringBuilder();
			resultSb.append("由于").append(msgTemplateService.getRemark(refuseRemark));
			resultSb.append("拒绝您归还").append(newZc.getMc()).append("(").append(newZc.getDm()).append(")").append("的申请");
			zt.setRemark(resultSb.toString());
			ztService.insertZT(zt);
			
			//通知部门资产管理员：sendRefuseSQMsg
			msgTemplateService.sendRefuseSQMsg(cjr, newZc, ZCZT.归还中, zcgl.getGlr());
		}
	}
	
	public String uploadPic(HttpSession session, MultipartFile file_upload, Integer appid) {
		
		String returnUrl = "";
		// 根据appid生成文件前缀
		String prePath = ZCGLProperties.URL_ZC_TARGET_DIR + "/" + appid;
		MultipartFile[] FileList = new MultipartFile[]{file_upload};
		String FileName = file_upload.getOriginalFilename();
		String fileType=FileName.substring(FileName.lastIndexOf('.'),FileName.length());
		try {
			returnUrl = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, fileType, FileList);
		} catch (OperException e) {
			e.printStackTrace();
		}
		return returnUrl;
	}
}
