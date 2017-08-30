package org.lf   .admin.service.catalog;

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
import org.lf.admin.db.dao.CFJMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.dao.VFJMapper;
import org.lf.admin.db.pojo.CFJ;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.VFJ;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
/**
 * 数据字典——房间管理
 * 
 * 
 */
@Service("fjService")
public class FJService {
	
	@Autowired
	private CFJMapper fjDao;
	
	@Autowired
	private VFJMapper vfjDao;
	
	@Autowired
	private ChuWXDeptMapper wxDeptDao;
	
	@Autowired
	private JZWService jzwService;
	
	public static final OperErrCode 楼层不能为空 = new OperErrCode("10201", "楼层不能为空");
	public static final OperErrCode 房间所属部门名不能为空 = new OperErrCode("10202", "房间所属部门名不能为空");
	public static final OperErrCode 房间所属部门名不存在 = new OperErrCode("10203", "房间所属部门名不存在");
	public static final OperErrCode 房间号不能为空 = new OperErrCode("10204", "房间号不能为空");
	public static final OperErrCode 房间号不能重复 = new OperErrCode("10205", "房间号不能重复");
	public static final OperErrCode 文件导出异常 = new OperErrCode("10206", "文件导出异常");
	public static final OperErrCode 读取Excel文件异常 = new OperErrCode("10207", "读取Excel文件异常");
	public static final OperErrCode 无法删除房间 = new OperErrCode("10208", "无法删除房间");
	public static final OperErrCode 房间记录不存在 = new OperErrCode("10209", "房间记录不存在");

	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。
	 * 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 楼层不能为空， 房间号不能为空，房间所属部门名不存在，房间号不能重复
	 ×
	 × 房间号不能重复：同一个JSWID下不能重复
	 */
	private void checkFJ(CFJ fj, Integer lineNumber,boolean isInsert) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";
		if(StringUtils.isEmpty(fj.getFloor())){
			throw new OperException(new OperErrCode("10201", "%s楼层不能为空",line));
		}
		
		if(StringUtils.isEmpty(fj.getDeptName())){
			throw new OperException(new OperErrCode("10202", "%s房间所属部门名不能为空",line));
		}else {
			ChuWXDept param = new ChuWXDept();
			param.setDeptName(fj.getDeptName());
			if(wxDeptDao.countDeptList(param) <= 0){
				throw new OperException(new OperErrCode("10203", "%s房间所属部门名不存在",line));
			}
		}
		if(StringUtils.isEmpty(fj.getRoom())){
			throw new OperException(new OperErrCode("10204", "%s房间号不能为空",line));
		}else {
			if(isInsert){
				CFJ param = new CFJ();
				param.setJzwId(fj.getJzwId());
				param.setRoom(fj.getRoom());
				if(fjDao.countFJList(param) > 0){
					throw new OperException(new OperErrCode("10205", "%s房间号不能重复",line));
				}
			}
		}
	}
	
	/**
	 * 插入一个房间记录。
	 * 
	 * @param fj
	 * @throws OperException 楼层不能为空， 房间号不能为空，房间所属部门名不存在，房间管理员不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertFJ(CFJ fj) throws OperException {
		checkFJ(fj, null,true);
		fjDao.insert(fj);
	}
	
	/**
	 * 使用POI解析excel文件，生成一个Map。key为行号，value为一个记录
	 * @param fjFile
	 * @return
	 * 
	 * @exception 读取Excel文件异常
	 */
	Map<Integer, VFJ> parseFile(InputStream fjFile) throws OperException {
		Map<Integer, VFJ> map = new HashMap<Integer, VFJ>();
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try {
		fs = new POIFSFileSystem(fjFile);
		wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			VFJ vfj = null;
			int lastRow = sheet.getLastRowNum();
			if (lastRow <= 1) {
				wb.close();
				throw new OperException(读取Excel文件异常);
			}
			for (int i = 1; i <= lastRow; i++) {
				row = sheet.getRow(i);
				vfj = new VFJ();
				String temp = row.getCell(0).getStringCellValue() + row.getCell(1).getStringCellValue() +
						row.getCell(2).getStringCellValue() + row.getCell(3).getStringCellValue() + 
						row.getCell(4).getStringCellValue();
				if(StringUtils.isEmpty(temp)){
					continue;
				}
				vfj.setJzw(row.getCell(0).getStringCellValue());
				vfj.setFloor(row.getCell(1).getStringCellValue());
				vfj.setRoom(row.getCell(2).getStringCellValue());
				vfj.setDeptName(row.getCell(3).getStringCellValue());
				vfj.setGlr(row.getCell(4).getStringCellValue());
				map.put(row.getRowNum() + 1, vfj);
			}
		} catch (Exception e) {
			throw new OperException(读取Excel文件异常);
		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				throw new OperException(读取Excel文件异常);
			}

		}
		return map;
	}
	
	
	/**
	 * 
	 * 将VFJ转换为CFJ
	 * 
	 * @param vjzw
	 * @return
	 * @throws OperException
	 */
	private CFJ translate(VFJ vfj, Integer line) throws OperException {
		CFJ cfj = new CFJ();
		cfj.setFloor(vfj.getFloor());
		cfj.setRoom(vfj.getRoom());
		cfj.setDeptName(vfj.getDeptName());
		cfj.setGlr(vfj.getGlr());
		//获取建筑物ID和AppId
		CJZW param = new CJZW();
		if (StringUtils.isEmpty(vfj.getJzw())) {
			throw new OperException(new OperErrCode("10202", "%s建筑物名称不能为空",line));
		}
		param.setMc(vfj.getJzw());
		CJZW tempJzw = jzwService.getJZW(param);
		cfj.setAppId(tempJzw.getAppId());
		cfj.setJzwId(tempJzw.getId());
		return cfj;
	}
	
	
	/**
	 * 插入一组房间记录。记录来自一个Excel表
	 * 错误时，显示：第XX行，资产分类号不能为空
	 * 
	 * @param fj
	 * @throws OperException 资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	void insertFJList(Integer appId, InputStream in) throws OperException {
		Map<Integer, VFJ> fjMap = parseFile(in);
		CFJ cfj = null;
		for (Integer lineNumber : fjMap.keySet()) {
			cfj = translate(fjMap.get(lineNumber), lineNumber);
			checkFJ(cfj, lineNumber,true);
			fjDao.insert(cfj);
		}

	}

	/**
	 * 插入一组房间记录。记录来自一个Excel表
	 * 错误时，显示：第XX行，资产分类号不能为空
	 * 
	 * @param fj
	 * @throws OperException 资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertFJList(Integer appId, MultipartFile fjFile) throws OperException {
		InputStream in = ExcelFileUtils.importExcel(fjFile);
		insertFJList(appId, in);
	}
	
	/***
	 * 设置表头的风格
	 * 
	 * 
	 * @param name    表头内容
	 * @param required  是否为必填
 	 */
	@SuppressWarnings("deprecation")
	public void setHeader(HSSFWorkbook wb, HSSFCell cell, String name, boolean required) {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 14);
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
	public HSSFWorkbook exportFJList(List<VFJ> fjList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		try {
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "建筑物名称", true);
			setHeader(wb, row.createCell(1), "楼层", true);
			setHeader(wb, row.createCell(2), "房间号", true);
			setHeader(wb, row.createCell(3), "所属部门", false);
			setHeader(wb, row.createCell(4), "管理员", false);
			HSSFCell cell;
			for (int i = 0 ; i<fjList.size();i++ ) {
				VFJ fj = fjList.get(i);
				row = sheet.createRow((short)i+1);
				cell = row.createCell(0);
				cell.setCellValue(fj.getJzw());
				cell.setCellStyle(setCellStyle(wb));
				cell = row.createCell(1);
				cell.setCellValue(fj.getFloor());
				cell.setCellStyle(setCellStyle(wb));
				cell = row.createCell(2);
				cell.setCellValue(fj.getRoom());
				cell.setCellStyle(setCellStyle(wb));
				cell = row.createCell(3);
				cell.setCellValue(fj.getDeptName());
				cell.setCellStyle(setCellStyle(wb));
				cell = row.createCell(4);
				cell.setCellValue(fj.getGlr());
				cell.setCellStyle(setCellStyle(wb));
			}
		} catch (Exception e) {
			throw new OperException(文件导出异常);
		}
		return wb;
	}
	
	
	/**
	 * 更新一个房间记录。
	 * 
	 * @param 房间 所有属性
	 * @throws OperException
	 *     ，
	 */ 
	@Transactional(rollbackFor = Exception.class)
	public void updateFJ(Integer id, Integer jzwId, String floor, String room,String deptName,String glr,Integer tybz) throws OperException {
		CFJ oldFj = getFJ(id);
		CFJ newFj = new CFJ();
		newFj.setId(id);
		newFj.setAppId(oldFj.getAppId());
		newFj.setJzwId(jzwId);
		newFj.setFloor(floor);
		newFj.setRoom(room);
		newFj.setDeptName(deptName);
		newFj.setGlr(glr);
		newFj.setTybz(tybz);
		// 修改了房间号
		if (oldFj.getRoom().equals(room)) {
			checkFJ(newFj, null,false);
		} else {
			checkFJ(newFj, null, true);
		}
		
		fjDao.updateByPrimaryKeySelective(newFj);
	}

	
	/**
	 * 删除房间。
	 * 当房间中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param fj
	 * @throws OperException 无法删除房间
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delFJ(Integer id) throws OperException {
		try{
			fjDao.deleteByPrimaryKey(id);
		}catch(Exception e){
			throw new OperException(无法删除房间);
		}
	}
	
	/**
	 * 删除房间。
	 * 当房间中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param fj
	 * @throws OperException 无法删除房间
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delFJ(CFJ fj) throws OperException {
		try{
			fjDao.deleteByPrimaryKey(fj.getId());
		}catch(Exception e){
			throw new OperException(无法删除房间);
		}
	}

	
	/**
	 * 统计房间列表
	 * @param param
	 * @return
	 */
	public int countFJList(VFJ param) {
		return vfjDao.countVFJList(param);	
	}
	
	/**
	 * 获取房间列表
	 * @param param
	 * @return
	 */
	public List<VFJ> getFJList(VFJ param) {
		return vfjDao.selectList(param);	
	}
	
	/**
	 * 获取指定应用下的指定页的房间列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<VFJ> getFJList(VFJ param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getFJList(param);	
	}
	
	/**
	 * 获取指定应用下的指定页的房间列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public EasyuiDatagrid<VFJ> getPageFJList(VFJ param, int page, int rows) {
		int total = countFJList(param);
		EasyuiDatagrid<VFJ> pageDatas = new EasyuiDatagrid<VFJ>();
		pageDatas.setTotal(total);
		if(total == 0){
			pageDatas.setRows(new ArrayList<VFJ>());
		}else{
			List<VFJ> list = getFJList(param, page, rows);
			pageDatas.setRows(list);
		}
		return pageDatas;
	}
	
	/**
	 * 根据主键获取房间数据
	 * @param id
	 * @exception 房间记录不存在
	 * @return
	 */
	public CFJ getFJ(Integer id) throws OperException {
		CFJ fj = fjDao.selectByPrimaryKey(id);
		if(fj == null){
			throw new OperException(房间记录不存在);
		}
		return fj;
	}
	
	/**
	 * 根据主键获取存放地点的详细信息
	 * @param id
	 * @exception 房间记录不存在
	 * @return
	 */
	public String getCFDD(Integer id) throws OperException {
		VFJ param = new VFJ();
		param.setFjId(id);
		VFJ vfj = getFJ(param);
		if(vfj == null){
			throw new OperException(房间记录不存在);
		}
		return vfj.getXqmc() + vfj.getJzw() + vfj.getFloor() + vfj.getRoom();
	}
	
	/**
	 * 根据CFJ对象中的属性获取房间数据
	 * @param id
	 * @exception 房间记录不存在
	 * @return
	 */
	public CFJ getFJ(CFJ param) throws OperException {
		CFJ fj = fjDao.select(param);
		if(fj == null){
			throw new OperException(房间记录不存在);
		}
		return fj;
	}

	/**
	 * 根据建筑物名称进行模糊查询
	 * @param jzw
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyuiDatagrid<VFJ> queryFJ(String jzw,int page, int rows) {
		EasyuiDatagrid<VFJ> pageDatas = new EasyuiDatagrid<VFJ>();
		PageNavigator pn = new PageNavigator(rows, page);
		VFJ param = new VFJ();
		param.setJzw(jzw);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		List<VFJ> fjList = vfjDao.getFJListFuzzy(param);
		pageDatas.setRows(fjList);
		pageDatas.setTotal(vfjDao.countVFJListFuzzy(param));
		return pageDatas;
	}

	public void insertFJ(Integer appId, Integer jzwid, String floor, String room, String deptName, String glr, Integer tybz) {
		CFJ param = new CFJ();
		param.setAppId(appId);
		param.setJzwId(jzwid);
		param.setFloor(floor);
		param.setRoom(room);
		param.setDeptName(deptName);
		param.setGlr(glr);
		param.setTybz(tybz);
		fjDao.insert(param);
	}

	/**
	 * 获取所有房间的并且将它转换为EasyuiComboBoxItem列表
	 * @param appId
	 * @return
	 */
	public List<EasyuiComboBoxItem> getFloorCombo(Integer appId) {
		CFJ param = new CFJ();
		param.setAppId(appId);
		List<EasyuiComboBoxItem> combo = null;
		List<String> list = fjDao.selectFloorList(param);
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (int i = 0;i<list.size(); i++) {
				item = new EasyuiComboBoxItem();
				item.setId(list.get(i));
				item.setText(list.get(i));
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 插入时检测房间号是否重复
	 * @param appId
	 * @param jzwid
	 * @param room
	 * @return
	 */
	public boolean checkFJByRoom(Integer appId, Integer jzwid, String room) {
		CFJ param = new CFJ();
		param.setAppId(appId);
		param.setJzwId(jzwid);
		param.setRoom(room);
		if(fjDao.countFJList(param) > 0){
			return false;
		}
		return true;
	}
	/**
	 * 在更新是检测房间号是否重复
	 * 
	 * @param appId
	 * @param jzwid
	 * @param newRoom
	 * @param oldRoom
	 * @return
	 */
	public boolean checkFJByUpdateRoom(Integer appId, Integer jzwid, String newRoom, String oldRoom) {
		if(newRoom.equals(oldRoom)){
			return true;
		}
		CFJ param = new CFJ();
		param.setAppId(appId);
		param.setJzwId(jzwid);
		param.setRoom(newRoom);
		if(fjDao.countFJList(param) > 0){
			return false;
		}
		return true;
	}
	/**
	 * 获取楼层树
	 * 
	 * @param param
	 * @return
	 */
	public List<EasyuiTree> queryJZW(VFJ param) {
		List<EasyuiTree> etList = new ArrayList<EasyuiTree>();
		List<String> floorList = vfjDao.selectFloorList(param);  //获取指定建筑物下不重复的楼层(v_fj)
		Map<String,List<VFJ>> map = new HashMap<String, List<VFJ>>();
		for (String temp : floorList) {
			param.setFloor(temp);
			List<VFJ> fjList = getFJList(param);
			map.put(temp, new ArrayList<VFJ>());
			for (VFJ vfj : fjList) {
				map.get(temp).add(vfj);
			}
		}
		EasyuiTree node;
		for (String key : map.keySet()) {
			node = new EasyuiTree(key, key, new ArrayList<EasyuiTree>(), false);
			node.setIconCls("icon-dept");
			EasyuiTree childNode;
			for (VFJ vfj : map.get(key)) {
				childNode = new EasyuiTree(vfj.getFjId().toString(), vfj.getRoom(), null, false);
				childNode.setIconCls("icon-man");
				node.getChildren().add(childNode);
			}
			if(node.getChildren().size() > 0){
				etList.add(node);
			}
		}
		return etList;
	}

	public VFJ getFJ(VFJ param) {
		return vfjDao.select(param);
	}
}
