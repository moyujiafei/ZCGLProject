package org.lf.admin.service.catalog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.ChuWXUser;
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
 * 资产管理
 * 
 * @author 李润方
 */
@Service("zcglService")
public class ZCGLService {
	@Autowired
	private ChuWXDeptMapper deptDao;
	@Autowired
	private ChuWXUserMapper userDao;
	@Autowired
	private CZCGLMapper czcglDao;

	public static final OperErrCode 资产所属部门不能为空 = new OperErrCode("10201", "资产所属部门不能为空");
	public static final OperErrCode 资产所属部门不存在 = new OperErrCode("10201", "资产所属部门不存在");
	public static final OperErrCode 资产管理员不存在 = new OperErrCode("10201", "资产管理员不存在");
	public static final OperErrCode 资产所属部门负责人不存在 = new OperErrCode("10201", "资产所属部门负责人不存在");
	public static final OperErrCode 资产管理记录不存在 = new OperErrCode("10201", "资产管理记录不存在");
	public static final OperErrCode 无法删除有资产的部门 = new OperErrCode("10201", "无法删除有资产的部门");
	public static final OperErrCode 文件导出异常 = new OperErrCode("10201", "文件导出异常");
	public static final OperErrCode 不允许同一个用户同时做两个以上部门的易耗品负责人 = new OperErrCode("10201", "不允许同一个用户同时做两个以上部门的易耗品负责人");
	public static final OperErrCode 不允许同一个用户同时做两个以上部门的固定资产管理人 = new OperErrCode("10201", "不允许同一个用户同时做两个以上部门的固定资产管理人");
	public static final OperErrCode 当前用户不是固定资产管理人 = new OperErrCode("10201", "当前用户不是固定资产管理人");
	public static final OperErrCode 当前用户不是易耗品负责人 = new OperErrCode("10201", "当前用户不是易耗品负责人");


	/**
	 * 这是一个公共方法，用于检查要插入的资产格式是否正确。 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 资产所属部门不能为空
	 *                , 资产所属部门不存在, 资产管理员不能为空， 资产管理员不存在， 资产所属部门负责人不存在
	 * 
	 *                部门名不存在：CHU_WXDEPT表
	 */
	private void checkZCGL(CZCGL zcgl, Integer lineNumber) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";

		if (StringUtils.isEmpty(zcgl.getDeptName())) {
			throw new OperException(new OperErrCode("10201", "%s资产所属部门不能为空", line));
		  }

		ChuWXDept deptParam = new ChuWXDept();
		deptParam.setDeptName(zcgl.getDeptName());
		if (deptDao.countDeptList(deptParam) < 1) {
			throw new OperException(new OperErrCode("10201", "%s资产所属部门不存在", line));
		}

		if (StringUtils.isEmpty(zcgl.getGlr())) {
			throw new OperException(new OperErrCode("10201", "%s资产管理员不能为空", line));
		}

		ChuWXUser userParam = new ChuWXUser();
		userParam.setName(zcgl.getGlr());
		if (userDao.countWXUserList(userParam) < 1) {
			throw new OperException(new OperErrCode("10201", "%s资产管理员不存在", line));
		}

		userParam.setName(zcgl.getFzr());
		if (userDao.countWXUserList(userParam) < 1) {
			throw new OperException(new OperErrCode("10201", "%s资产所属部门负责人不存在", line));
		}
	}

	/**
	 * 插入一个资产管理记录。
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             资产所属部门不能为空, 资产所属部门不存在, 资产管理员不能为空， 资产管理员不存在， 资产所属部门负责人不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCGL(CZCGL zcgl) throws OperException {
		checkZCGL(zcgl, null);
		czcglDao.insertSelective(zcgl);
	}

	/**
	 * 使用POI解析xml文件，生成一个Map。key为行号，value为一个记录
	 * 
	 * @param zcglFile
	 * @return
	 * 
	 * @exception 读取资产管理文件异常
	 */
	Map<Integer, CZCGL> parseFile(InputStream in) throws OperException {
		return null;
	}

	/**
	 * 插入一组资产管理记录。记录来自一个Excel表 错误时，显示：第XX行，资产所属部门不能为空
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             资产所属部门不能为空, 资产所属部门不存在, 资产管理员不能为空， 资产管理员不存在， 资产所属部门负责人不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	void insertZCGLList(Integer appId, InputStream in) throws OperException {
		Map<Integer, CZCGL> zcglMap = parseFile(in);

		for (Integer lineNumber : zcglMap.keySet()) {
			checkZCGL(zcglMap.get(lineNumber), lineNumber);
		}

		// TODO: 批量插入资产

	}

	/**
	 * 插入一组资产管理记录。记录来自一个Excel表 错误时，显示：第XX行，资产所属部门不能为空
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             资产所属部门不能为空, 资产所属部门不存在, 资产管理员不能为空， 资产管理员不存在， 资产所属部门负责人不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	void insertZCGLList(Integer appId, MultipartFile file) throws OperException {
		InputStream in = ExcelFileUtils.importExcel(file);
		insertZCGLList(appId, in);
	}
	
	/***
	 * 导出EXCEL文件
	 * @param zcglList
	 * @return HSSFWorkbook
	 * @throws OperException 
	 */
	public HSSFWorkbook exportZCGLList(List<CZCGL> zcglList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		try {
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "部门名", true);
			setHeader(wb, row.createCell(1), "部门负责人", true);
			setHeader(wb, row.createCell(2), "部门资产管理人员", true);
			
			HSSFCell cell;
			for (int i = 0; i < zcglList.size(); i++) {
				CZCGL zcgl = zcglList.get(i);
				row = sheet.createRow((short)i+1);
				cell = row.createCell(0);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(zcgl.getDeptName());
				cell = row.createCell(1);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(zcgl.getFzr());
				cell = row.createCell(2);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(zcgl.getGlr());
			}
		} catch (Exception e) {
			throw new OperException(文件导出异常);
		}
		return wb;

	}
	
	
	
	/**
	 * 查看当前用户是否为部门资产管理人
	 */
	public CZCGL getGLR(Integer appId, String glr) throws OperException {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		param.setGlr(glr);
		CZCGL zcgl = getZCGL(param);
		if (zcgl == null) {
			throw new OperException(当前用户不是固定资产管理人);
		}

		return zcgl;
	}
	
	/**
	 * 查看当前用户是否为部门资产管理人
	 */
	public CZCGL getFZR(Integer appId, String fzr) throws OperException {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		param.setFzr(fzr);
		CZCGL zcgl = getZCGL(param);
		if (zcgl == null) {
			throw new OperException(当前用户不是易耗品负责人);
		}

		return zcgl;
	}

	/**
	 * 更新一个资产管理记录。
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             资产管理编号不能为空, 资产所属部门不存在, 资产管理员不存在， 资产所属部门负责人不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateZCGL(CZCGL zcgl) throws OperException {
		czcglDao.updateByPrimaryKeySelective(zcgl);
	}
	
	/**
	 * 在执行更新操作之前做检查
	 * 1、同一个appId下，不允许同一个用户同时做两个以上部门的易耗品负责人。
   * 2、  同一个appId下，不允许同一个用户同时做两个以上部门的固定资产管理人。
	 */
	public void checkFzrAndGlr(Integer appId, Integer id, String fzr, String glr) throws OperException{
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		param.setFzr(fzr);
		if(countZCGLList(param) > 0 && !getZCGL(id).getFzr().equals(fzr)){
			throw new OperException(不允许同一个用户同时做两个以上部门的易耗品负责人);
		}
		param.setFzr(null);
		param.setGlr(glr);
		if(countZCGLList(param) > 0 && !getZCGL(id).getGlr().equals(glr)){
			throw new OperException(不允许同一个用户同时做两个以上部门的固定资产管理人);
		}
	}
	
	/**
	 * 更新一个资产管理记录。
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             资产管理编号不能为空, 资产所属部门不存在, 资产管理员不存在， 资产所属部门负责人不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateZCGL(Integer id, String fzr, String glr) throws OperException {
		CZCGL zcgl = getZCGL(id);
		zcgl.setFzr(fzr);
		zcgl.setGlr(glr);
		updateZCGL(zcgl);
	}

	/**
	 * 删除资产管理。 当资产管理中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param zcgl
	 * @throws OperException
	 *             无法删除有资产的部门
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delZCGL(CZCGL zcgl) throws OperException {
		try {
			czcglDao.deleteByPrimaryKey(zcgl.getId());
		} catch (Exception e) {
			throw new OperException(无法删除有资产的部门);
		}
	}

	/**
	 * 统计资产管理列表
	 * 
	 * @param param
	 * @return
	 */
	public int countZCGLList(CZCGL param) {
		return czcglDao.countZcglList(param);
	}

	/**
	 * 获取资产管理列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CZCGL> getZCGLList(CZCGL param) {
		return czcglDao.selectList(param);
	}

	/**
	 * 获取指定应用下的指定页的资产管理列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<CZCGL> getZCGLList(CZCGL param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return czcglDao.selectZCGLList(param);
	}
	
	/**
	 * 返回当前节点到根节点所有部门的名字，使用--进行分隔。
	 */
	public String getWXDeptRootName(int appId, int rootId) {
		String s1 = czcglDao.getWXDeptRootList(appId, rootId);
		String[] s2 = s1.split(",");
		ChuWXDept param = null;
		String s3 = "";
		for(int i = 1; i< s2.length ; i++) {
			param = new ChuWXDept();
			param.setDeptNo(Integer.parseInt(s2[i]));
			param = deptDao.select(param);
			s3 += param.getDeptName()+"--";
		}
		return s3.substring(0,s3.length()-2);
	}

	/**
	 * 获取指定应用下的指定页的资产管理列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<CZCGL> getPageZCGLList(CZCGL param, int rows, int page) {
		int total = countZCGLList(param);
		EasyuiDatagrid<CZCGL> pageDatas = new EasyuiDatagrid<CZCGL>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<CZCGL>());
		} else {
			List<CZCGL> list = getZCGLList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 根据主键获取资产管理数据
	 * 
	 * @param id
	 * @exception 资产管理记录不存在
	 * @return
	 */
	public CZCGL getZCGL(Integer id) {
		CZCGL zcgl = czcglDao.selectByPrimaryKey(id);
		return zcgl;
	}

	/**
	 * 根据pojo获取资产管理数据
	 * 
	 * @param VZCLX
	 * @exception 资产管理记录不存在
	 * @return
	 */
	public CZCGL getZCGL(CZCGL param) throws OperException {
		CZCGL zcgl = czcglDao.select(param);
		if (zcgl == null) {
			throw new OperException(资产管理记录不存在);
		}
		return zcgl;
	}
	
	/**
	 * 获得资产管理部门下拉框信息，id和text均为部门名
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCBMNameCombo(Integer appId) {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		List<CZCGL> list = getZCGLList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (CZCGL zcgl : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zcgl.getDeptName());
				item.setText(zcgl.getDeptName());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得资产管理部门下拉框信息(包含全部)，id和text均为部门名
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCBMNameComboWithAll(Integer appId) {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		List<CZCGL> list = getZCGLList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCGL zcgl : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zcgl.getDeptName());
				item.setText(zcgl.getDeptName());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得资产管理部门下拉框信息，id和text均为部门名
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCBMCombo(Integer appId) {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		List<CZCGL> list = getZCGLList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (CZCGL zcgl : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zcgl.getId() + "");
				item.setText(zcgl.getDeptName());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得资产管理部门下拉框信息(包含全部)，id和text均为部门名
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCBMComboWithAll(Integer appId) {
		CZCGL param = new CZCGL();
		param.setAppId(appId);
		List<CZCGL> list = getZCGLList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCGL zcgl : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zcgl.getId() + "");
				item.setText(zcgl.getDeptName());
				combo.add(item);
			}
		}
		return combo;
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

}
