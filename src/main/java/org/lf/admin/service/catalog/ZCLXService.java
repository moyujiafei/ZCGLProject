package org.lf.admin.service.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.lf.admin.db.dao.CZCLXMapper;
import org.lf.admin.db.dao.VZCLXMapper;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.db.pojo.VZCLX;
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
 * 数据字典——资产类型
 * 
 * @author 李润方
 */
@Service("zclxService")
public class ZCLXService {
	@Autowired
	VZCLXMapper vzclxDao;
	@Autowired
	CZCLXMapper czclxDao;

	public static final OperErrCode 资产分类号不能为空 = new OperErrCode("10401", "资产分类号不能为空");
	public static final OperErrCode 资产分类名称不能为空 = new OperErrCode("10402", "资产分类名称不能为空");
	public static final OperErrCode 资产分类名称不能重复 = new OperErrCode("10403", "资产分类名称不能重复");
	public static final OperErrCode 无法删除有资产的部门 = new OperErrCode("10404", "无法删除有资产的部门");
	public static final OperErrCode 资产类型记录不存在 = new OperErrCode("10405", "资产类型记录不存在");
	public static final OperErrCode 读取资产类型文件异常 = new OperErrCode("10406", "读取资产类型文件异常");

	public enum EXCELTYPE {
		资产类型, 资产品种
	};

	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 资产分类号不能为空
	 *                ， 资产分类名称不能为空，资产分类名称不能重复，
	 * 
	 *                资产分类名称不能重复: 指的是同一个appId下资产分类名称不能重名
	 */
	private void checkZCLX(Integer appId, CZCLX zclx, Integer lineNumber, boolean isInsert) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";

		if (StringUtils.isEmpty(zclx.getLxId())) {
			throw new OperException(new OperErrCode("10401", "%s资产分类号不能为空", line));
		}

		if (StringUtils.isEmpty(zclx.getMc())) {
			throw new OperException(new OperErrCode("10402", "%s资产分类名称不能为空", line));
		}

		if (isInsert) {
			VZCLX param = new VZCLX();
			param.setAppId(appId);
			param.setLx(zclx.getMc());
			if (countZCLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s资产分类名称不能重复", line));
			}
		}

	}

	/**
	 * 验证excel批量插入的数据是否合法
	 * 
	 * @param appId
	 * @param zclx
	 * @param lineNumber
	 * @param flag
	 * @throws OperException
	 */
	private void checkZCLX(Integer appId, CZCLX zclx, Integer lineNumber, EXCELTYPE type) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";
		// 声明一个临时参数变量
		VZCLX param;

		if (type.equals(EXCELTYPE.资产类型)) {// 验证资产类型
			// LX_ID不能为空
			if (StringUtils.isEmpty(zclx.getLxId())) {
				throw new OperException(new OperErrCode("10401", "%s资产分类号不能为空", line));
			}
			// LX_ID不能重复
			param = new VZCLX();
			param.setAppId(appId);
			param.setLx(zclx.getLxId());
			if (countZCLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s资产分类号称不能重复", line));
			}
			// LX_MC不能为空
			if (StringUtils.isEmpty(zclx.getMc())) {
				throw new OperException(new OperErrCode("10402", "%s资产分类名称不能为空", line));
			}
			// LX_MC不能重复
			param = new VZCLX();
			param.setAppId(appId);
			param.setLx(zclx.getMc());
			if (countZCLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s资产分类名称不能重复", line));
			}
			// 折旧年限大于0
			if (!checkZCPZByZJNX(appId, zclx.getZjnx())) {
				throw new OperException(new OperErrCode("10403", "%s折旧年限要大于0", line));
			}
		} else if (type.equals(EXCELTYPE.资产品种)) {// 验证资产品种
			// LX_PID不能为空
			if (StringUtils.isEmpty(zclx.getLxPid())) {
				throw new OperException(new OperErrCode("10401", "%s所属资产分类号不能为空", line));
			}
			// LX_MC不能为空
			if (StringUtils.isEmpty(zclx.getMc())) {
				throw new OperException(new OperErrCode("10402", "%s资产品种名称不能为空", line));
			}
			// LX_MC不能重复
			param = new VZCLX();
			param.setAppId(appId);
			param.setLx(zclx.getMc());
			if (countZCLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s资产品种名称不能重复", line));
			}
			// 折旧年限大于0
			if (!checkZCPZByZJNX(appId, zclx.getZjnx())) {
				throw new OperException(new OperErrCode("10403", "%s折旧年限要大于0", line));
			}
		}
	}

	/**
	 * 插入一个资产类型记录。
	 * 
	 * @param zclx
	 * @throws OperException
	 *             资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCLX(CZCLX zclx) throws OperException {
		checkZCLX(zclx.getAppId(), zclx, null, true);

		czclxDao.insertSelective(zclx);
	}

	/**
	 * 插入一个资产类型记录。
	 * 
	 * @param i
	 * @param pic_url
	 * 
	 * @param zclx
	 * @throws OperException
	 *             资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCLX(Integer appId, String lxid, String lxPid, String mc, BigDecimal zjnx, String remark, String pic_url, Integer img_version) throws OperException {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		param.setLxId(lxid);
		param.setLxPid(lxPid);
		param.setMc(mc);
		param.setZjnx(zjnx);
		param.setRemark(remark);
		param.setPicUrl(pic_url);
		param.setImgVersion(img_version);

		insertZCLX(param);
	}

	/**
	 * 插入一组资产类型记录。记录来自一个Excel表 错误时，显示：第XX行，资产分类号不能为空
	 * 
	 * @param zclx
	 * @throws OperException
	 *             资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCLXList(Integer appId, InputStream in, EXCELTYPE type) throws OperException {
		Map<Integer, CZCLX> zclxMap = null;

		switch (type) {
		case 资产类型:
			zclxMap = parseFile(in, EXCELTYPE.资产类型, appId);
			CZCLX zclx;
			for (Integer lineNumber : zclxMap.keySet()) {
				zclx = zclxMap.get(lineNumber);
				zclx.setAppId(appId);
				czclxDao.insertSelective(zclx);
			}
			break;
		case 资产品种:
			zclxMap = parseFile(in, EXCELTYPE.资产品种, appId);
			CZCLX zclx2;
			for (Integer lineNumber : zclxMap.keySet()) {
				zclx2 = zclxMap.get(lineNumber);
				zclx2.setAppId(appId);
				zclx2.setLxId(zclx2.getLxPid());// lxid不能为空，所以先暂时设置为pid,后面再更新
				czclxDao.insertSelective(zclx2);
				// 根据LXPID生成LXID
				CZCLX param = czclxDao.select(zclx2);
				String lxId = generateLXID(param.getLxPid(), param.getId() + "");
				// 更新LXID
				param.setLxId(lxId);
				czclxDao.updateByPrimaryKeySelective(param);
			}
			break;
		}
	}

	/**
	 * 插入一组资产类型记录。记录来自一个Excel表 错误时，显示：第XX行，资产分类号不能为空
	 * 
	 * @param zclx
	 * @throws OperException
	 *             资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZCLXList(Integer appId, MultipartFile file, EXCELTYPE type) throws OperException {
		InputStream in = ExcelFileUtils.importExcel(file);
		insertZCLXList(appId, in, type);
	}

	/**
	 * 1.使用POI解析excel文件，生成一个Map。key为行号，value为一个记录
	 * 2.对excel文件的预处理：检查表格中的空行，表格中的重复行 3.对每行记录做check，检查数据是否合法：
	 * 如果是资产类型:检查LX_ID是否为空、是否重复，检查名称是否为空、是否重复,折旧年限大于1
	 * 如果是资产品种:检查LX_PID是否为空、是否存在，检查品种名称是否为空是否重复,折旧年限大于1
	 * 4.对资产类型excel做整体扫描，判断文件中是否有多个同LX_ID,同MC的行 对资产品种excel做整体扫描，判断文件中是否有多个同MC的行
	 * 
	 * @param in
	 *            excel文件流 flag 资产类型表格(flag==1) 资产品种表格(flag==2)
	 * @return
	 * 
	 * @exception 读取资产类型文件异常
	 */
	private Map<Integer, CZCLX> parseFile(InputStream in, EXCELTYPE type, Integer appId) throws OperException {
		// 通过in获得表格工作簿sheet
		Map<Integer, CZCLX> content = new HashMap<Integer, CZCLX>();
		Workbook wb = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(in);
			wb = new HSSFWorkbook(fs);
			Sheet sheet = wb.getSheetAt(0);

			// 获取最后一行的索引
			int rowNum = sheet.getLastRowNum();

			// 申明两个用来存储LX_ID和MC的Set
			Set<String> lxidSet = new HashSet<String>();
			Set<String> mcSet = new HashSet<String>();

			// 判断解析的是资产类型表格还是资产品种表格
			switch (type) {
			case 资产类型:
				// 解析资产类型表格，从表格的第二行开始迭代
				for (int i = 1; i <= rowNum; i++) {
					Row row = sheet.getRow(i);
					if (ExcelFileUtils.isRowEmpty(row)) {
						continue;// 如果此行为空（不包含任何数据和样式）。跳过。
					} else {
						// 将此行记录转换成CZCLX的pojo
						CZCLX czclx = new CZCLX();
						// 判断在此表格中LX_ID是否重复
						String lxid = row.getCell(0).toString().trim();
						if (lxidSet.contains((String) lxid)) {
							String line = "第" + (i + 1) + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的资产分类号 " + lxid, line));
						} else {
							lxidSet.add(lxid);
							czclx.setLxId(lxid);
						}
						// 判断在此表格中MC是否重复
						String mc = row.getCell(1).toString().trim();
						if (mcSet.contains((String) mc)) {
							String line = "第" + i + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的资产类型名称 " + mc, line));
						} else {
							mcSet.add(mc);
							czclx.setMc(mc);
						}
						String zjnx = row.getCell(2).toString().trim().equals("") ? "0" : row.getCell(2).toString().trim();
						czclx.setZjnx(new BigDecimal(Double.parseDouble(zjnx)));
						czclx.setRemark(row.getCell(3).toString());
						// 对此pojo做资产类型check
						checkZCLX(appId, czclx, i + 1, type);
						// 将pojo放入map
						content.put(i + 1, czclx);
					}

				}
				break;
			case 资产品种:
				// 解析资产类型表格，从表格的第二行开始迭代
				for (int i = 1; i <= rowNum; i++) {
					Row row = sheet.getRow(i);
					if (ExcelFileUtils.isRowEmpty(row)) {
						continue;// 如果此行为空（不包含任何数据和样式）。跳过。
					} else {
						// 将此行记录转换成CZCLX的pojo
						CZCLX czclx = new CZCLX();
						czclx.setLxPid(row.getCell(0).toString().trim());
						// 判断在此表格中MC是否重复
						String mc = row.getCell(1).toString().trim();
						if (mcSet.contains((String) mc)) {
							String line = "第" + (i + 1) + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的资产品种名称 " + mc, line));
						} else {
							mcSet.add(mc);
							czclx.setMc(mc);
						}
						String zjnx = row.getCell(2).toString().trim().equals("") ? "0" : row.getCell(2).toString().trim();
						czclx.setZjnx(new BigDecimal(Double.parseDouble(zjnx)));
						czclx.setRemark(row.getCell(3).toString());
						// 对此pojo做资产类型check
						checkZCLX(appId, czclx, i + 1, type);
						// 将pojo放入map
						content.put(i + 1, czclx);
					}
				}
				break;
			}
		} catch (IOException e) {
			throw new OperException(读取资产类型文件异常);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return content;
	}

	public HSSFWorkbook exportZCLXList(List<CZCLX> zclxList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		sheet.setDefaultRowHeightInPoints(18); // 设置缺省列高
		sheet.setDefaultColumnWidth(20); // 设置缺省列宽
		Row row = sheet.createRow(0);
		// 创建标题字体
		HSSFFont ssfTitle1 = createFont(wb, (short) 14, HSSFColor.RED.index, "宋体", true);
		HSSFFont ssfTitle2 = createFont(wb, (short) 14, HSSFColor.BLACK.index, "宋体", true);
		// 创建文本字体
		HSSFFont ssfText = createFont(wb, (short) 14, HSSFColor.BLACK.index, "宋体", false);
		// 创建标题单元格样式
		CellStyle csTitle1 = createCellStyle(wb, ssfTitle1, HSSFColor.GREY_25_PERCENT.index, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		CellStyle csTitle2 = createCellStyle(wb, ssfTitle2, HSSFColor.GREY_25_PERCENT.index, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		// 创建文本单元格样式
		CellStyle csTex1 = createCellStyle(wb, ssfText, HSSFColor.WHITE.index, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
		CellStyle csTex2 = createCellStyle(wb, ssfText, HSSFColor.WHITE.index, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER);
		createCell(row, (short) 0, csTitle1).setCellValue("资产分类号");
		createCell(row, (short) 1, csTitle2).setCellValue("上级资产分类号");
		createCell(row, (short) 2, csTitle1).setCellValue("资产名称");
		createCell(row, (short) 3, csTitle2).setCellValue("折旧年限（年）");
		createCell(row, (short) 4, csTitle2).setCellValue("备注");
		int i = 1;
		for (CZCLX zclx : zclxList) {
			row = sheet.createRow(i++);
			createCell(row, (short) 0, csTex1).setCellValue(zclx.getLxId());
			createCell(row, (short) 1, csTex1).setCellValue(zclx.getLxPid());
			createCell(row, (short) 2, csTex1).setCellValue(zclx.getMc());
			createCell(row, (short) 3, csTex2).setCellValue(zclx.getZjnx().doubleValue());
			createCell(row, (short) 4, csTex1).setCellValue(zclx.getRemark());
		}
		return wb;
	}

	/**
	 * 创建单元格
	 */
	private Cell createCell(Row row, short column, CellStyle cs) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		return cell;
	}

	/**
	 * 创建单元格样式
	 * 
	 * @param wb
	 *            工作簿
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 * @param fgColor
	 *            背景色
	 * @param fontSize
	 *            字体大小
	 * @param fontColor
	 *            字体颜色
	 * @param isBold
	 *            字体加粗
	 * @param isXs
	 *            是否为系数
	 * @param ha
	 *            水平对齐
	 * @return Cell
	 */
	private CellStyle createCellStyle(HSSFWorkbook wb, HSSFFont font, short fgColor, HorizontalAlignment ha, VerticalAlignment va) {
		CellStyle cs = wb.createCellStyle();// 创建单元格样式
		cs.setFillForegroundColor(fgColor); // 设置单元格样式
		cs.setAlignment(ha);
		cs.setVerticalAlignment(va);
		cs.setWrapText(true);
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setFont(font);
		return cs;
	}

	/**
	 * NPOI的字体数目有限制，所以不要无限制的创建字体，字体一样的单元格公用一个字体
	 * 
	 * @param HSSFWorkbook
	 * @param fontSize
	 * @param fontColor
	 * @param fontName
	 * @param isBold
	 * @return
	 */
	private HSSFFont createFont(HSSFWorkbook wb, short fontSize, short fontColor, String fontName, boolean isBold) {
		HSSFFont ssf = wb.createFont();
		ssf.setBold(isBold);// 设置单元格字体样式
		ssf.setFontName(fontName);
		ssf.setColor(fontColor);
		ssf.setFontHeightInPoints(fontSize);
		return ssf;
	}

	/**
	 * 更新一个资产类型记录。
	 * 
	 * @param i
	 * @param pic_url
	 * 
	 * @param zclx
	 * @throws OperException
	 *             资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateZCLX(Integer id, String lxid, String lxPid, String mc, BigDecimal zjnx, String remark, String pic_url, Integer img_version) throws OperException {
		CZCLX param = new CZCLX();
		param.setId(id);
		CZCLX oldZCLX = getZCLX(param);

		CZCLX newZCLX = new CZCLX();
		newZCLX.setId(id);
		newZCLX.setAppId(oldZCLX.getAppId());
		newZCLX.setLxId(lxid);
		newZCLX.setLxPid(lxPid);
		newZCLX.setMc(mc);
		newZCLX.setZjnx(zjnx);
		newZCLX.setRemark(remark);
		newZCLX.setPicUrl(pic_url);
		newZCLX.setImgVersion(oldZCLX.getImgVersion() + 1);// 每次更新，版本号+1

		// 修改了类型名
		if (oldZCLX.getMc().equals(mc)) {
			checkZCLX(newZCLX.getAppId(), newZCLX, null, false);
		} else {
			checkZCLX(newZCLX.getAppId(), newZCLX, null, true);
		}

		czclxDao.updateByPrimaryKeySelective(newZCLX);
	}

	/**
	 * 删除资产类型。 当资产类型中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param zclx
	 * @throws OperException
	 *             无法删除有资产的部门
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delZCLX(Integer id) throws OperException {
		try {
			czclxDao.deleteByPrimaryKey(id);
		} catch (Exception e) {
			throw new OperException(无法删除有资产的部门);
		}
	}

	/**
	 * 统计资产类型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countZCLXList(VZCLX param) {
		return vzclxDao.countZCLXList(param);
	}

	/**
	 * 统计一级资产类型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countLevel1ZCLXList(VZCLX param) {
		return vzclxDao.countLevel1ZCLXList(param);
	}

	/**
	 * 统计二级资产类型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countLevel2ZCLXList(VZCLX param) {
		return vzclxDao.countLevel2ZCLXList(param);
	}

	/**
	 * 获取资产类型列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CZCLX> getZCLXList(CZCLX param) {
		return czclxDao.selectList(param);
	}

	/**
	 * 获得一级资产类型名称列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CZCLX> getLevel1ZCLXList(CZCLX param) {
		return czclxDao.selectLevel1List(param);
	}

	/**
	 * 获得二级资产类型名称列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CZCLX> getLevel2ZCLXList(CZCLX param) {
		return czclxDao.selectLevel2List(param);
	}

	/**
	 * 获取资产类型列表
	 * 
	 * @param param
	 * @return
	 */
	public List<VZCLX> getZCLXList(VZCLX param) {
		return vzclxDao.selectList(param);
	}

	/**
	 * 获取指定应用下的指定页的资产类型列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VZCLX> getZCLXList(VZCLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return vzclxDao.selectZCLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的资产类型列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VZCLX> getPageZCLXList(VZCLX param, int rows, int page) {
		int total = countZCLXList(param);
		EasyuiDatagrid<VZCLX> pageDatas = new EasyuiDatagrid<VZCLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZCLX>());
		} else {
			List<VZCLX> list = getZCLXList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	public EasyuiDatagrid<VZCLX> getPageZCLXList(Integer appId, int rows, int page) {
		VZCLX param = new VZCLX();
		param.setAppId(appId);
		return getPageZCLXList(param, rows, page);
	}

	/**
	 * 获取指定应用下的指定页的 一级 资产类型列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VZCLX> getLevel1ZCLXList(VZCLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return vzclxDao.selectLevel1ZCLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的 二级 资产类型列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VZCLX> getLevel2ZCLXList(VZCLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return vzclxDao.selectLevel2ZCLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的 一级 资产类型列表。
	 * 
	 * @param param
	 *            模糊查询
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VZCLX> getLevel1PageZCLXList(VZCLX param, int rows, int page) {
		int total = countLevel1ZCLXList(param);
		EasyuiDatagrid<VZCLX> pageDatas = new EasyuiDatagrid<VZCLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZCLX>());
		} else {
			List<VZCLX> list = getLevel1ZCLXList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 获取指定应用下的指定页的 一级 资产类型列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VZCLX> getLevel1PageZCLXList(Integer appId, int rows, int page) {
		VZCLX param = new VZCLX();
		param.setAppId(appId);
		return getLevel1PageZCLXList(param, rows, page);
	}

	/**
	 * 获取指定应用下的指定页的 二级 资产类型列表。
	 * 
	 * @param param
	 *            模糊查询
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VZCLX> getLevel2PageZCLXList(VZCLX param, int rows, int page) {
		int total = countLevel2ZCLXList(param);
		EasyuiDatagrid<VZCLX> pageDatas = new EasyuiDatagrid<VZCLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZCLX>());
		} else {
			List<VZCLX> list = getLevel2ZCLXList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 根据主键获取资产类型数据
	 * 
	 * @param id
	 * @exception 资产类型记录不存在
	 * @return
	 */
	public VZCLX getZCLX(Integer id) {
		VZCLX zclx = vzclxDao.selectByPrimaryKey(id);
		return zclx;
	}

	/**
	 * 根据pojo获取资产类型数据
	 * 
	 * @param CZCLX
	 * @exception 资产类型记录不存在
	 * @return
	 */
	public CZCLX getZCLX(CZCLX param) {
		CZCLX zclx = czclxDao.select(param);
		return zclx;
	}

	/**
	 * 获得资产类型下拉框信息，id；text为资产类型名
	 * 
	 * select * from c_zclx where app_id = ?;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCLXCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得资产类型下拉框信息(包含全部)，id；text为资产类型名 select * from c_zclx where app_id = ?;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCLXComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得资产类型下拉框信息，id和text均为资产类型名
	 * 
	 * select * from c_zclx where app_id = ?;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCLXMCCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得资产类型下拉框信息(包含全部)，id和text均为资产类型名 select * from c_zclx where app_id = ?;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCLXMCComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得一级资产类型下拉框信息，text为资产类型名，id是id select * from c_zclx where app_id = ? and
	 * lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1ZCLXCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel1ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得一级资产类型下拉框信息(包含全部)，text为资产类型名，id是id select * from c_zclx where app_id =
	 * ? and lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1ZCLXComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel1ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			// 没有ID，导致全部不可选
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得一级资产类型下拉框信息，id和text均为资产类型名 select * from c_zclx where app_id = ? and
	 * lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1ZCLXMCCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel1ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getLxId());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得一级资产类型下拉框信息(包含全部)，id和text均为资产类型名 select * from c_zclx where app_id = ?
	 * and lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1ZCLXMCComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel1ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			// 没有ID，导致全部不可选
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得二级资产类型下拉框信息，id；text为资产类型名 select * from c_zclx where app_id = ? and
	 * lx_pid is not null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel2ZCLXCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel2ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得二级资产类型下拉框信息(包含全部)，id；text为资产类型名 select * from c_zclx where app_id = ?
	 * and lx_pid is not null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel2ZCLXComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel2ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			// 没有ID，导致全部不可选
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得二级资产类型下拉框信息，id和text均为资产类型名 select * from c_zclx where app_id = ? and
	 * lx_pid is not null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel2ZCLXMCCombo(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel2ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得二级资产类型下拉框信息(包含全部)，id和text均为资产类型名 select * from c_zclx where app_id = ?
	 * and lx_pid is not null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel2ZCLXMCComboWithAll(Integer appId) {
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = getLevel2ZCLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			// 没有ID，导致全部不可选
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CZCLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 检查类型ID
	 * 
	 * @param appId
	 * @param lxid
	 * @return
	 */
	public boolean checkZCLXByLXID(Integer appId, String lxid) {
		if (StringUtils.isEmpty(lxid)) {
			return false;
		}
		if (lxid.length() > 4) {
			return false;
		}
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		param.setLxId(lxid);
		if (getZCLX(param) != null) {
			return false;
		}
		return true;
	}

	/**
	 * 检查更新时名称
	 * 
	 * @param appId
	 * @param oldMC
	 * @param newMC
	 * @return
	 */
	public boolean checkZCLXByUpdateMC(Integer appId, String oldMC, String newMC) {
		if (StringUtils.isEmpty(newMC)) {
			return true;
		}
		if (oldMC.equals(newMC)) {
			return true;
		}

		VZCLX param = new VZCLX();
		param.setAppId(appId);
		param.setLx(newMC);
		if (countZCLXList(param) > 0) {
			return false;
		}

		return true;
	}

	/**
	 * 检查插入时名称
	 * 
	 * @param appId
	 * @param mc
	 * @return
	 */
	public boolean checkZCLXByMC(Integer appId, String mc) {
		if (StringUtils.isEmpty(mc)) {
			return false;
		}
		VZCLX param = new VZCLX();
		param.setAppId(appId);
		param.setLx(mc);
		if (countZCLXList(param) > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查折旧年限，必须大于0
	 * 
	 * @param appId
	 * @param zjnx
	 * @return
	 */
	public boolean checkZCPZByZJNX(Integer appId, BigDecimal zjnx) {
		if (zjnx.compareTo(new BigDecimal(0)) <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取资产类型树
	 */
	public List<EasyuiTree> getZCLXTree(Integer appId, String zclx_mc) {
		// 创建“全部”根节点
		EasyuiTree root = new EasyuiTree("", "全部", new ArrayList<EasyuiTree>(), false);
		root.setIconCls("icon-deviceGroup");
		// 获取一级资产类型节点
		CZCLX param = new CZCLX();
		param.setAppId(appId);
		List<CZCLX> list = czclxDao.selectLevel1List(param);
		List<CZCLX> tempList;
		EasyuiTree node; // 一级节点
		EasyuiTree subnode; // 二级节点
		for (CZCLX zclx : list) {
			node = new EasyuiTree(zclx.getId() + "", zclx.getMc(), new ArrayList<EasyuiTree>(), false);
			node.setIconCls("icon-deviceGroup");
			root.getChildren().add(node);
			param.setLxPid(zclx.getLxId());
			// 获取此一级节点的二级节点
			tempList = czclxDao.selectLevel2List(param);
			for (CZCLX zclx2 : tempList) {
				subnode = new EasyuiTree(zclx2.getId() + "", zclx2.getMc(), new ArrayList<EasyuiTree>(), false);
				subnode.setIconCls("icon-device");
				if (zclx_mc == null) {
					node.getChildren().add(subnode);
				} else if (zclx2.getMc().contains(zclx_mc)) {
					node.getChildren().add(subnode);
				} else {
					continue;
				}
			}
		}
		List<EasyuiTree> result = new ArrayList<>();
		result.add(root);
		return result;

	}

	/**
	 * 生成8位的品种类型ID
	 * 
	 * @param lxid
	 * @return
	 */
	private String generateLXID(String lxpid, String id) {
		int length = 8 - id.length();
		lxpid = StringUtils.rpad(lxpid, '0', length);
		String formatLXID = lxpid + id;
		return formatLXID;
	}
}
