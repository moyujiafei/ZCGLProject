package org.lf.admin.service.zcgl;

import java.util.Calendar;
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
import org.lf.admin.db.dao.BMZC_GZSJStatMapper;
import org.lf.admin.db.pojo.BMZC_GZSJStat;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bmzc_GZSJStatService")
public class BMZC_GZSJStatService {
	@Autowired
	private BMZC_GZSJStatMapper bmzc_gzsjDao;
	
	@Autowired
	private BMZCStatMapper bmzcStatDao;
	
	@Autowired
	private WXDeptService wxDeptService;
	
	public static final OperErrCode 文件导出异常 = new OperErrCode("10107", "文件导出异常");
	
	/**
	 * 根据app_id和dept_name获取购置时间
	 * @param param
	 * @return
	 */	
	public BMZC_GZSJStat getGZSJStat(Integer appId, Integer deptNo) {
		List<String> deptNoList = wxDeptService.getSubDeptmentByDeptNo(appId, deptNo);
		Map<String, Object> param = new HashMap<String, Object>();
		if (deptNoList.size() > 1 || !StringUtils.isEmpty(deptNoList.get(0))) {
			param.put("deptNoList", deptNoList);
		}
		param.put("appId", appId);
		return bmzc_gzsjDao.gzsjByAppidAndDeptNo(param);
	}
	
	/**
	 * 获取资产购置时间列表
	 * @param zc
	 * @return
	 */
	public List<BMZC_GZSJStat> getGZSJStatList(VZC zc){
		return bmzc_gzsjDao.selectList(zc);
	}
	
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
	public HSSFWorkbook exportGzsjStatList(List<BMZC_GZSJStat> gzsjStatList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		Calendar a=Calendar.getInstance();
		a.get(Calendar.YEAR);//得到年
		try {
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "部门名", true);
			setHeader(wb, row.createCell(1), a.get(Calendar.YEAR)+"年", true);
			setHeader(wb, row.createCell(2), a.get(Calendar.YEAR)-1+"年", true);
			setHeader(wb, row.createCell(3), a.get(Calendar.YEAR)-2+"年", true);
			setHeader(wb, row.createCell(4), a.get(Calendar.YEAR)-3+"年", true);
			setHeader(wb, row.createCell(5), a.get(Calendar.YEAR)-4+"年", true);
			setHeader(wb, row.createCell(6), a.get(Calendar.YEAR)-4+"年前", true);
			setHeader(wb, row.createCell(7), "小计", true);			
			HSSFCell cell;
			for (int i = 0; i < gzsjStatList.size(); i++) {
				BMZC_GZSJStat gzsjStat = gzsjStatList.get(i);
				row = sheet.createRow((short)i+1);
				cell = row.createCell(0);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getDeptName());
				cell = row.createCell(1);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getZero());
				cell = row.createCell(2);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getOne());
				cell = row.createCell(3);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getTwo());
				cell = row.createCell(4);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getThree());
				cell = row.createCell(5);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getFour());
				cell = row.createCell(6);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getFive());
				cell = row.createCell(7);
				cell.setCellStyle(setCellStyle(wb));
				cell.setCellValue(gzsjStat.getTotal());								
			}
		} catch (Exception e) {
			throw new OperException(文件导出异常);
		}
		return wb;
	}
}
