package org.lf.admin.service.zcgl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.lf.admin.db.dao.BMZC_ZCFLStatMapper;
import org.lf.admin.db.dao.CZCLXMapper;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bmzc_ZCFLStatService")
public class BMZC_ZCFLStatService {
	@Autowired
	private BMZC_ZCFLStatMapper bmzc_zcflDao;
	
	@Autowired
	private CZCLXMapper zclxDao;
	
	@Autowired
	private BMZCStatMapper bmzcStatDao;
	
	@Autowired
	private WXDeptService wxDeptService;
	
	public static final OperErrCode 文件导出异常 = new OperErrCode("10107", "文件导出异常");
	
	public static final OperErrCode 资产分类统计视图为空 = new OperErrCode("10108", "资产分类统计视图为空");
	
	/**
	 * 根据app_id和dept_no获取一级分类资产
	 * @param param
	 * @return
	 * @throws OperException 
	 */
	public ConcurrentHashMap<String, Object> getZCFLStat(Integer appId, Integer deptNo) throws OperException {
		    List<String> lxIdList =  bmzc_zcflDao.getLxidByLxpid(appId);
		    List<String> deptNoList = wxDeptService.getSubDeptmentByDeptNo(appId, deptNo);
			 Map<String, Object> param = new HashMap<String, Object>();
				if (deptNoList.size() > 1 || !StringUtils.isEmpty(deptNoList.get(0))) {
					param.put("deptNoList", deptNoList);
				}
				param.put("lxIdList", lxIdList);
				param.put("appId", appId);			
			ConcurrentHashMap<String, Object> map=bmzc_zcflDao.zcflOneByAppidAndDeptNo(param);
			if(map==null){
				throw new OperException(资产分类统计视图为空);
			}
			CZCLX record=new CZCLX();
			record.setAppId(appId);
			List<CZCLX> zclxList=zclxDao.selectLevel1List(record);
			Object temp;
			for(CZCLX zclx:zclxList){
				for(String key:map.keySet()){
					if(zclx.getLxId().equals(key)){
						temp = map.get(key);
						map.remove(key);
						map.put(zclx.getMc(), temp);
					}
				}
			}
			return map;
	}
	
	/**
	 * 获取一级分类资产列表
	 * @param zc
	 * @return
	 */
//	public List<ConcurrentHashMap<String, Object>> getZCFLStatList(CZCGL zcgl){
//		zcgl.setLxIdList(bmzc_zcflDao.getLxidByLxpid(zcgl.getAppId()));
//		List<ConcurrentHashMap<String, Object>> mapList=bmzc_zcflDao.selectZCFLOneList(zcgl);
//		List<CZCLX> zclxList=zclxDao.selectLevel1List(null);
//		Object temp;
//		for (ConcurrentHashMap<String, Object> map : mapList) {
//			for (CZCLX zclx : zclxList) {
//				for (Object key : map.keySet()) {
//					if (zclx.getLxId().equals(key.toString())) {
//						temp = map.get(key.toString());
//						map.remove(key.toString());
//						map.put(zclx.getMc(), temp);
//					}
//				}
//
//			}
//		}
//		return mapList;
//	}
	
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
	public HSSFWorkbook exportZcflStatList(List<ConcurrentHashMap<String, Object>> zcflStatList) throws OperException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short)20);
		sheet.setDefaultRowHeightInPoints(18);
		try {	
			HSSFRow row = sheet.createRow((short)0);
			setHeader(wb, row.createCell(0), "部门名", true);		
			ConcurrentHashMap<String, Object> map = zcflStatList.get(0);		
			int k=1;
			for(String key : map.keySet()){
				if(key.equals("dept_name")){
				    continue;
				}else{
					setHeader(wb, row.createCell(k), key, true);
					k++;
				}	
				
			}
			HSSFCell cell;			
			for (int i = 0; i < zcflStatList.size(); i++) {
				int j=1;
				ConcurrentHashMap<String, Object> map1 = zcflStatList.get(i);
			    row= sheet.createRow((short)i+1);			    
    		    for(Object key : map1.keySet()){ 
    		    	if(key.equals("dept_name")){
    		    		String value =  map1.get(key.toString()).toString(); 
    		    		cell = row.createCell(0);
    		    		cell.setCellStyle(setCellStyle(wb));
    		    		cell.setCellValue(value);	                    
    		    	}else{
				    	String value =  map1.get(key.toString()).toString(); 				    
		                cell = row.createCell(j);
					    cell.setCellStyle(setCellStyle(wb));
						cell.setCellValue(value);
						j++;
    		    	}
	         }			    		   																	
		  }
		} catch (Exception e) {
	     		throw new OperException(文件导出异常);
		}
		return wb;
	}
}
