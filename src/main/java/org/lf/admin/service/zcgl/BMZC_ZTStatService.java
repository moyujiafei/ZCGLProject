package org.lf.admin.service.zcgl;

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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.lf.admin.db.dao.BMZCStatMapper;
import org.lf.admin.db.pojo.BMZC_ZTStat;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部门资产统计
 */
@Service("bmzcStatService")
public class BMZC_ZTStatService {
	@Autowired
	private BMZCStatMapper bmzcStatDao;
	
	@Autowired
	private WXDeptService wxDeptService;
	
	public static final OperErrCode 文件导出异常 = new OperErrCode("10107", "文件导出异常");
	
	/**
	 * 获取资产状态
	 * @param param
	 * @return
	 */	
	public BMZC_ZTStat getZCZTStat(Integer appId, Integer deptNo) {
		 List<String> deptNoList = wxDeptService.getSubDeptmentByDeptNo(appId, deptNo);
		 Map<String, Object> param = new HashMap<String, Object>();
			if (deptNoList.size() > 1 || !StringUtils.isEmpty(deptNoList.get(0))) {
				param.put("deptNoList", deptNoList);
			}
			param.put("appId", appId);	
		return 	bmzcStatDao.statByAppidAndDeptNo(param);
	}
	
	/**
	 * 获取资产状态列表
	 * @param zc
	 * @return
	 */
//	public List<BMZC_ZTStat> getZCZTStatList(VZC zc){
//		return zcztDao.selectList(zc);
//	}
//	
	/***
	 * 设置表头的风格
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
	 * @param bmzcStatList
	 * @return HSSFWorkbook
	 * @throws OperException 
	 */
	public HSSFWorkbook exportBmzcStatList(List<BMZC_ZTStat> bmzcStatList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		try {
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "部门名", true);
			setHeader(wb, row.createCell(1), "未使用", true);
			setHeader(wb, row.createCell(2), "使用中", true);
			setHeader(wb, row.createCell(3), "维修中", true);
			setHeader(wb, row.createCell(4), "闲置", true);
			setHeader(wb, row.createCell(5), "申请维修", true);
			setHeader(wb, row.createCell(6), "申请报废", true);
			setHeader(wb, row.createCell(7), "申请闲置", true);
			setHeader(wb, row.createCell(8), "巡检中", true);
			setHeader(wb, row.createCell(9), "报废", true);
			setHeader(wb, row.createCell(10), "已登记", true);
			setHeader(wb, row.createCell(11), "领用中", true);
			setHeader(wb, row.createCell(12), "归还中", true);
			setHeader(wb, row.createCell(13), "上交中", true);
			setHeader(wb, row.createCell(14), "未登记", true);
			setHeader(wb, row.createCell(15), "小计", true);
			HSSFCell cell;
			for (int i = 0; i < bmzcStatList.size(); i++) {
				BMZC_ZTStat bmzcStat = bmzcStatList.get(i);
				row = sheet.createRow((short)i+1);
				cell = row.createCell(0);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getDeptName());
				cell = row.createCell(1);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getWsy());
				cell = row.createCell(2);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getSyz());
				cell = row.createCell(3);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getWx());
				cell = row.createCell(4);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getXz());
				cell = row.createCell(5);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getSqwx());
				cell = row.createCell(6);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getSqbf());
				cell = row.createCell(7);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getSqxz());				
				cell = row.createCell(8);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getXjz());
				cell = row.createCell(9);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getBf());
				cell = row.createCell(10);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getYdj());
				cell = row.createCell(11);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getLyz());
				cell = row.createCell(12);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getGhz());
				cell = row.createCell(13);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getSjz());
				cell = row.createCell(14);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getWdj());
				cell = row.createCell(15);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(bmzcStat.getXj());
			}
		} catch (Exception e) {
			throw new OperException(文件导出异常);
		}
		return wb;
	}
	
}
