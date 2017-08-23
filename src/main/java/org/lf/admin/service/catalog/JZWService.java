package org.lf.admin.service.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.lf.admin.db.dao.CJZWMapper;
import org.lf.admin.db.dao.VJZWMapper;
import org.lf.admin.db.pojo.CJZLX;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.db.pojo.VJZW;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数据字典——建筑物管理
 * 
 * 
 */
@Service("jzwService")
public class JZWService {
	@Autowired
	private CJZWMapper jzwDao;
	
	@Autowired
	private VJZWMapper vjzwDao;
	
	@Autowired
	private XQService xqService;
	
	@Autowired
	private JZLXService jzlxService;
	
	public static final OperErrCode 建筑物类型不能为空 = new OperErrCode("10101", "建筑物类型不能为空");
	public static final OperErrCode 建筑物名称不能为空 = new OperErrCode("10102", "建筑物名称不能为空");
	public static final OperErrCode 建筑物名称不能重复 = new OperErrCode("10103", "建筑物名称不能重复");
	public static final OperErrCode 建筑物所在校区不能为空 = new OperErrCode("10104", "建筑物所在校区不能为空");
	public static final OperErrCode 无法删除有部门的建筑物 = new OperErrCode("10105", "无法删除有部门的建筑物");
	public static final OperErrCode 建筑物记录不存在 = new OperErrCode("10106", "建筑物记录不存在");
	public static final OperErrCode 文件导出异常 = new OperErrCode("10107", "文件导出异常");	
	public static final OperErrCode 读取Excel文件异常 = new OperErrCode("10108", "读取Excel文件异常");
	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。
	 * 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 建筑物类型不能为空， 建筑物名称不能为空，建筑物名称不能重复，建筑物所在校区不能为空
	 * 
	 * 建筑物名称不能重复：指的是在指定的app_Id和校区xq_id下，建筑物命名不能重名。
	 */
	private void checkJZW(CJZW jzw, Integer lineNumber,boolean isInsert) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";
		
		if (jzw.getLxId() == null) {
			throw new OperException(new OperErrCode("10101", "%s建筑物类型不能为空", line));
		}   
		if (StringUtils.isEmpty(jzw.getMc())) {
			throw new OperException(new OperErrCode("10102", "%s建筑物名称不能为空", line));
		}
		if (jzw.getXqId() == null) {
			throw new OperException(new OperErrCode("10104", "%s建筑物所在校区不能为空", line));
		}
		
		if(isInsert){
			// 建筑物名称不能重复
			VJZW param = new VJZW();
			param.setAppId(jzw.getAppId());
			param.setJzw(jzw.getMc());
			if (countJZWList(param) > 0) {
				throw new OperException(new OperErrCode("10103", "%s建筑物名称不能重复", line));
			}
		}
	}
	
	/**
	 * 插入一个建筑物记录。
	 * 
	 * @param jzw
	 * @throws OperException 建筑物类型不能为空， 建筑物名称不能为空，建筑物名称不能重复，建筑物所在校区不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertJZW(CJZW jzw) throws OperException {
		checkJZW(jzw, null,true);
		jzwDao.insert(jzw);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void insertJZW(Integer appId, Integer xqId,Integer lxId, String jzw, String dz, Integer tybz) throws OperException {
		CJZW param = new CJZW();
		param.setAppId(appId);
		param.setXqId(xqId);
		param.setLxId(lxId);
		param.setMc(jzw);
		param.setDz(dz);
		param.setTybz(tybz);
		
		jzwDao.insert(param);
	}
	
	/**
	 * 使用POI解析excel文件，生成一个Map。key为行号，value为一个记录
	 * @param jzwFile
	 * @return
	 * 
	 * @exception 读取Excel文件异常
	 */
	Map<Integer, VJZW> parseFile(InputStream jzwFile) throws OperException {
		Map<Integer, VJZW> map = new HashMap<Integer, VJZW>();
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try {
			fs = new POIFSFileSystem(jzwFile);
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			VJZW vjzw = null;
			int lastRow = sheet.getLastRowNum();
			if (lastRow <= 1) {
				wb.close();
				throw new OperException(读取Excel文件异常);
			}
			for(int i = 1; i <= lastRow; i++){
				vjzw = new VJZW();
				row = sheet.getRow(i);
				String temp = row.getCell(0).getStringCellValue() + row.getCell(1).getStringCellValue() + 
						row.getCell(2).getStringCellValue() + row.getCell(2).getStringCellValue();
				if(StringUtils.isEmpty(temp)){
					continue;
				}
				vjzw.setXqmc(row.getCell(0).getStringCellValue());
				vjzw.setLxmc(row.getCell(1).getStringCellValue());
				vjzw.setJzw(row.getCell(2).getStringCellValue());
				vjzw.setDz(row.getCell(3).getStringCellValue());
				map.put(row.getRowNum() + 1, vjzw);
			}
		} catch (IOException e) {
			throw new OperException(读取Excel文件异常);
		}finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
			}

		}
		return map;
	}
	/**
	 * 将VJZW转换为CJZW
	 * @param vjzw
	 * @param line
	 * @return
	 * @throws OperException
	 */
	private CJZW translate(VJZW vjzw,Integer line) throws OperException {
		CJZW cjzw = new CJZW();
		cjzw.setMc(vjzw.getJzw());
		cjzw.setDz(vjzw.getDz());
		// 获取校区
		CXQ param = new CXQ();
		if (StringUtils.isEmpty(vjzw.getXqmc())) {
			throw new OperException(new OperErrCode("10104", "第：%s行建筑物所在校区不能为空", line));
		}
		param.setXqmc(vjzw.getXqmc());
		CXQ xq = xqService.getXQ(param);
		cjzw.setXqId(xq.getId());
		cjzw.setAppId(xq.getAppId());
		
		// 获取建筑类型
		CJZLX jzlx = new CJZLX();
		if (StringUtils.isEmpty(vjzw.getLxmc())) {
			throw new OperException(new OperErrCode("10101", "第：%s行建筑物类型不能为空", line));
		}
		jzlx.setMc(vjzw.getLxmc());
		CJZLX temp = jzlxService.getJZLX(jzlx);
		cjzw.setLxId(temp.getId());
		return cjzw;
	}

	/**
	 * 插入一组建筑物记录。记录来自一个Excel表
	 * 错误时，显示：第XX行，建筑物分类号不能为空
	 * 
	 * @param jzw
	 * @throws OperException 建筑物分类号不能为空， 建筑物分类名称不能为空，建筑物分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	void insertJZWList(Integer appId, InputStream in) throws OperException {
		Map<Integer, VJZW> jzwMap = parseFile(in);
		
		CJZW cjzw;
		for (Integer lineNumber : jzwMap.keySet()) {
			cjzw = translate(jzwMap.get(lineNumber),lineNumber);
			checkJZW(cjzw, lineNumber,true);
			jzwDao.insert(cjzw);
		}
	}
	
	/**
	 * 插入一组建筑物记录。记录来自一个Excel表
	 * 错误时，显示：第XX行，建筑物分类号不能为空
	 * 
	 * @param jzw
	 * @throws OperException 建筑物分类号不能为空， 建筑物分类名称不能为空，建筑物分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertJZWList(Integer appId, MultipartFile file) throws OperException {
		InputStream in = ExcelFileUtils.importExcel(file);
		insertJZWList(appId, in);
	}
	
	/***
	 * 设置表头的风格
	 * 
	 * 
	 * @param name    表头内容
	 * @param required  是否为必填
 	 */
	@SuppressWarnings("deprecation")
	public void setHeader(HSSFWorkbook wb,HSSFCell cell,String name,boolean required){
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)14);
		font.setBold(true);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		if (required) {
			font.setColor(HSSFColor.RED.index);
		}
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(name);
	}
	
	/***
	 * 
	 *   返回单元格内容的风格 
	 * @param wb
	 * @return  style
	 */
	public HSSFCellStyle setCellStyle(HSSFWorkbook wb){
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)11);
		style.setFont(font);
		return style;
	}
	
	/***
	 * 导出EXCEL文件
	 * @param fjList
	 * @return HSSFWorkbook
	 * @throws OperException 
	 */
	public HSSFWorkbook exportJZWList(List<VJZW> jzwList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		try {
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "校区", true);
			setHeader(wb, row.createCell(1), "建筑物类型", true);
			setHeader(wb, row.createCell(2), "建筑物名称", true);
			setHeader(wb, row.createCell(3), "地址", false);
			HSSFCell cell;
			for (int i = 0; i < jzwList.size(); i++) {
				VJZW jzw = jzwList.get(i);
				row = sheet.createRow((short)i+1);
				cell = row.createCell(0);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(jzw.getXqmc());
				cell = row.createCell(1);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(jzw.getLxmc());
				cell = row.createCell(2);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(jzw.getJzw());
				cell = row.createCell(3);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(jzw.getDz());
			}
		} catch (Exception e) {
			throw new OperException(文件导出异常);
		}
		return wb;
	}
	
	/**
	 * 更新一个建筑物记录。
	 * 
	 * @param jzw
	 * @throws OperException 建筑物类型不能为空， 建筑物名称不能为空，建筑物名称不能重复，建筑物所在校区不能为空
	 * 
	 * 建筑物名称不能重复": 同一个appId下建筑物不能重复。
	 */
	public void updateJZW(Integer id,Integer xqId,Integer lxId ,String mc,String dz,String poi,Integer tybz) throws OperException {
		CJZW oldJZW = getCJZW(id);
		CJZW newJZW = new CJZW();
		newJZW.setAppId(oldJZW.getAppId());
		newJZW.setId(id);
		newJZW.setXqId(xqId);
		newJZW.setLxId(lxId);
		newJZW.setMc(mc);
		newJZW.setDz(dz);
		newJZW.setPoi(poi);
		newJZW.setTybz(tybz);
		if(oldJZW.getMc().equals(mc)){
			checkJZW(newJZW, null, false);
		}else{
			checkJZW(newJZW, null, true);
		}
		jzwDao.updateByPrimaryKeySelective(newJZW);
	}
	
	/**
	 * 删除建筑物。
	 * 当建筑物中存在房间时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param jzw
	 * @throws OperException 无法删除有部门的建筑物
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delJZW(Integer id) throws OperException {
		try {
			jzwDao.deleteByPrimaryKey(id);
		} catch (Exception e) {
			throw new OperException(无法删除有部门的建筑物);
		}
	}

	/**
	 * 删除建筑物。
	 * 当建筑物中存在房间时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param jzw
	 * @throws OperException 无法删除有部门的建筑物
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delJZW(CJZW jzw) throws OperException {
		try {
			jzwDao.deleteByPrimaryKey(jzw.getId());
		} catch (Exception e) {
			throw new OperException(无法删除有部门的建筑物);
		}
	}
	/**
	 * 统计建筑物列表
	 * @param param
	 * @return
	 */
	public int countJZWList(VJZW param) {
		return vjzwDao.countJZWList(param);
	}
	
	/**
	 * 获取建筑物列表(v_jzw)
	 * @param param
	 * @return
	 */
	public List<VJZW> getJZWList(VJZW param) {
		return vjzwDao.selectList(param);
	}
	
	/**
	 * 获取建筑物列表(c_jzw)
	 * @param param
	 * @return
	 */
	public List<CJZW> getJZWList(CJZW param) {
		return jzwDao.selectList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的建筑物列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<VJZW> getJZWList(VJZW param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getJZWList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的建筑物列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public EasyuiDatagrid<VJZW> getPageJZWList(VJZW param, int rows, int page) {
		int total = countJZWList(param);
		EasyuiDatagrid<VJZW> pageDatas = new EasyuiDatagrid<VJZW>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VJZW>());
		} else {
			List<VJZW> list = getJZWList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 根据主键获取建筑物数据
	 * @param id
	 * @exception 建筑物记录不存在
	 * @return
	 */
	public VJZW getVJZW(Integer id) throws OperException {
		VJZW vjzw = vjzwDao.selectByPrimaryKey(id);
		if (vjzw == null) {
			throw new OperException(建筑物记录不存在);
		}
		return vjzw;
	}

	/**
	 * 根据主键获取建筑物数据
	 * @param id
	 * @exception 建筑物记录不存在
	 * @return
	 */
	public CJZW getCJZW(Integer id) throws OperException {
		CJZW jzw = jzwDao.selectByPrimaryKey(id);
		if (jzw == null) {
			throw new OperException(建筑物记录不存在);
		}
		return jzw;
	}
	/**
	 * 根据参数获取建筑物数据
	 * @param id
	 * @exception 建筑物记录不存在
	 * @return
	 */
	public CJZW getJZW(CJZW param) throws OperException {
		CJZW jzw = jzwDao.select(param);
		if (jzw == null) {
			throw new OperException(建筑物记录不存在);
		}
		return jzw;
	}
	/**
	 * 对建筑物进行模糊查询
	 * select * from c_jzw where xq_id = ? and lx_id = ? and mc like '%...%';
	 * @param appId
	 * @param xqid
	 * @param lxid
	 * @param mc
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyuiDatagrid<VJZW> queryJZW(Integer appId, String xqmc, String lxmc, String mc,int page, int rows) {
		VJZW param = new VJZW();
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		param.setAppId(appId);
		param.setXqmc(xqmc);
		param.setLxmc(lxmc);
		param.setJzw(mc);
		List<VJZW> jzwList = vjzwDao.getJZWListFuzzy(param);
		EasyuiDatagrid<VJZW> eDatagrid = new EasyuiDatagrid<VJZW>();
		eDatagrid.setTotal(vjzwDao.countJZWListFuzzy(param));
		eDatagrid.setRows(jzwList);
		return eDatagrid;
	} 
	
	/**
	 * 检查更新时是否有重名
	 * @param id
	 * @param newJZWMC
	 * @return
	 * @throws OperException
	 */
	public boolean checkJZWByUpdateMC(Integer appId, String oldJZWMC, String newJZWMC) throws OperException{
		
		if (oldJZWMC.equals(newJZWMC)) {
			return true;
		}
		
		VJZW param = new VJZW();
		param.setJzw(newJZWMC);
		param.setAppId(appId);
		if (countJZWList(param) > 0) {
			return false;
		}
		
		return true;
	}
	/**
	 * 插入操作中，对名称进行重复性检测
	 * @param newJZWMC
	 * @return
	 */
	public boolean checkJZWByMC(Integer appId,String newJZWMC){
		VJZW param = new VJZW();
		param.setJzw(newJZWMC);
		param.setAppId(appId);
		if (countJZWList(param) > 0) {
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 获取所有的建筑物并且将他转换为EasyuiComboBoxItem类型
	 * @param appId
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZWCombo(Integer appId){
		CJZW param = new CJZW();
		param.setAppId(appId);
		List<EasyuiComboBoxItem> combo = null;
		List<CJZW> list = getJZWList(param);
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CJZW jzw : list) {
				item = new EasyuiComboBoxItem();
				item.setId(jzw.getId().toString());
				item.setText(jzw.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	/**
	 * 获取所有的建筑物并且将他转换为EasyuiComboBoxItem类型
	 * @param appId
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZWCombo(Integer appId,Integer xqid){
		CJZW param = new CJZW();
		param.setAppId(appId);
		param.setXqId(xqid);
		List<EasyuiComboBoxItem> combo = new ArrayList<EasyuiComboBoxItem>();
		List<CJZW> list = getJZWList(param);
		EasyuiComboBoxItem item = null;
		for (CJZW jzw : list) {
			item = new EasyuiComboBoxItem();
			item.setId(jzw.getId().toString());
			item.setText(jzw.getMc());
			combo.add(item);
		}
		return combo;
	}
}
