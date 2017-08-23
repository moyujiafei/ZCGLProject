package org.lf.admin.service.sys;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.zcgl.ZCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;

/**
 * 二维码打印输出
 */
@Service("ewmPrintService")
public class EWMPrintService {
	@Autowired
	private ZCService zcService;
	
	/**
	 * 二维码行数
	 */
	private static final int EWM_ROW_NUM = 10;
	/**
	 * 二维码列数
	 */
	private static final int EWM_COL_NUM = 8;
	
	public static final OperErrCode 文件下载异常 = new OperErrCode("11201", "pdf文档下载失败");
	
	public static final OperErrCode 资产列表为空 = new OperErrCode("11202", "资产列表为空！");
	
	private JSONObject getEWMString(VZC zc) {
		JSONObject json=new JSONObject();
		json.put("app_id", zc.getAppId());
		json.put("zcdm", zc.getZcdm());
		json.put("zclxId", zc.getZclxId());
		json.put("cfdd", zc.getCfdd());
		json.put("dept_no", zc.getDeptNo());
		return json;
	}
	
	public List<VZC> getEVMStringList(Integer appId, String zclxId, Integer deptNo, String cfdd, String syr, String glr) {
		return zcService.getVZCListByDeptNoAndZCLX(appId, null, null, zclxId, cfdd, syr, deptNo, glr, null);
	}
	
	/**
	 * 获取 二维码阵列pdf文档，并以流的形式输出
	 */
	public void getEwmPdf(Integer appId, String zclxId, Integer deptNo, String cfdd, String syr, String glr,HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<VZC> zcList=getEVMStringList(appId, zclxId, deptNo, cfdd, syr, glr);
		if(zcList!=null && zcList.size()>0){
			int zcNum=zcList.size();
			int pageNumber;
			if(zcNum % (EWM_ROW_NUM*EWM_COL_NUM) == 0){
				pageNumber=zcNum / (EWM_ROW_NUM*EWM_COL_NUM);
			}else{
				pageNumber=zcNum / (EWM_ROW_NUM*EWM_COL_NUM) + 1;
			}
			int ewmTotalNum=getewmTotalNum(pageNumber);
			createPdfFile(zcList, ewmTotalNum, new Date(),request, response);
		}else{
			throw new OperException(资产列表为空);
		}
	}
	
	public int getewmTotalNum(int pageNumber){
		return pageNumber*EWM_ROW_NUM*EWM_COL_NUM;
	}
	
	/**
	 * 生成pdf文件
	 * @param zcList
	 * @param ewmTotalNum
	 * @param currDate
	 * @param request
	 * @param response
	 * @return
	 * @throws OperException 
	 */
	private synchronized void createPdfFile(List<VZC> zcList, int ewmTotalNum,Date currDate,
			HttpServletRequest request, HttpServletResponse response) throws OperException {
		Document doc = new Document();// 创建文档对象
		// 页边空白
		doc.setMargins(0, 0, 0, 0);
		// pdf文件临时存放目录
		String tmpPdfDir = request.getServletContext().getRealPath("/")
				+ "ewmTempPdf.pdf";
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(tmpPdfDir);
			// 定义输出文件的位置
			PdfWriter.getInstance(doc, fos);
			// 开启文档
			doc.open();
			// 设置二维码字符集和容错级别
			Map<com.itextpdf.text.pdf.qrcode.EncodeHintType, Object> hints = new HashMap<com.itextpdf.text.pdf.qrcode.EncodeHintType, Object>();
			hints.put(
					com.itextpdf.text.pdf.qrcode.EncodeHintType.CHARACTER_SET,
					"UTF-8");
			hints.put(
					com.itextpdf.text.pdf.qrcode.EncodeHintType.ERROR_CORRECTION,
					ErrorCorrectionLevel.H);
			// 最外层table，8列
			PdfPTable table = new PdfPTable(EWM_COL_NUM);
			table.setSpacingBefore(0);
			table.setSpacingAfter(0);
			// 无边框
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table.setWidthPercentage(100);
			PdfPCell cell = null;
			VZC zc;
			String beginId=zcList.get(0).getZcdm();
			String lastId=beginId;
			int size=zcList.size();
			for (int i = 0; i < ewmTotalNum; i++) {
				PdfPTable nestedTab;
				if(i<size){
					zc=zcList.get(i);
					lastId=zc.getZcdm();
					String ewmInfo = getEWMString(zc).toJSONString();
					// 单元格内层嵌套table，1列
				    nestedTab = new PdfPTable(1);
					nestedTab.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					// 生成二维码
					BarcodeQRCode qrcode = new BarcodeQRCode(ewmInfo.trim(), 73,
							73, hints);
					// 转换为图片对象
					Image qrcodeImage = qrcode.getImage();
					qrcodeImage.setAlignment(Image.MIDDLE | Image.TEXTWRAP);
					cell = new PdfPCell(qrcodeImage);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					nestedTab.addCell(cell);// 添加图片到单元格
					// 添加二维码值到单元格
					Phrase ewmVal = new Phrase(zc.getZcdm(), new Font(
							Font.FontFamily.TIMES_ROMAN, 6));// 设置字体和大小
					cell = new PdfPCell(ewmVal);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setFixedHeight(10);
					nestedTab.addCell(cell);
					cell=new PdfPCell();
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setFixedHeight(10);
					nestedTab.addCell(cell);
				}else{
					// 单元格内层嵌套table，1列
					nestedTab = new PdfPTable(1);
					nestedTab.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				}
				// 将内层嵌套table加入到外层table的单元格中
				cell = new PdfPCell(nestedTab);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中
				cell.setVerticalAlignment(Element.ALIGN_CENTER);// 垂直居中
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}
			doc.add(table);
			doc.close();// 完成pdf生成
			DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String currDateStr = sdf.format(currDate);
			// 下载pdf文件
			String fileName = "资产二维码列表-" + currDateStr + "(" + beginId + "--"
					+ lastId + ")";
			downLoadPdfFile(fileName, tmpPdfDir, response);
		}catch(Exception e){
			throw new OperException(文件下载异常);
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new OperException(文件下载异常);
				}
			}
		}
	}

	/**
	 * 下载指定文件
	 * 
	 * @param fileName
	 * @param filePath
	 * @param response
	 * @throws OperException 
	 */
	public void downLoadPdfFile(String fileName, String filePath,
			HttpServletResponse response) throws OperException  {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.reset(); // 非常重要
			// 纯下载方式
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="
					+   new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".pdf");
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch(IOException e){
			throw new OperException(文件下载异常);
		}finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					throw new OperException(文件下载异常);
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					throw new OperException(文件下载异常);
				}
		}
	}
}
