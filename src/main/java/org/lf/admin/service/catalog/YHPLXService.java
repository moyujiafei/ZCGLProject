package org.lf.admin.service.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.lf.admin.db.dao.CYHPLXMapper;
import org.lf.admin.db.dao.VYHPLXMapper;
import org.lf.admin.db.pojo.CYHPLX;
import org.lf.admin.db.pojo.VYHPLX;
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
 * 数据字典——易耗品类型
 * 
 * @author 李润方
 */
@Service("yhplxService")
public class YHPLXService {
	public static final OperErrCode 读取易耗品类型文件异常 = new OperErrCode("10406", "读取易耗品类型文件异常");

	@Autowired
	private CYHPLXMapper cyhpDao;
	@Autowired
	private VYHPLXMapper vyhpDao;

	// 易耗品Excel表格类型
	public enum YHPEXCELTYPE {
		易耗品类型, 易耗品品种
	};

	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 资产分类号不能为空,资产分类名称不能为空，资产分类名称不能重复,资产分类名称不能重复:
	 *                指的是同一个appId下资产分类名称不能重名
	 */
	private void checkYHPLX(Integer appId, CYHPLX yhplx, Integer lineNumber, boolean isInsert) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";

		if (StringUtils.isEmpty(yhplx.getLxId())) {
			throw new OperException(new OperErrCode("10401", "%s易耗品分类号不能为空", line));
		}

		if (StringUtils.isEmpty(yhplx.getMc())) {
			throw new OperException(new OperErrCode("10402", "%s易耗品分类名称不能为空", line));
		}

		if (isInsert) {
			VYHPLX param = new VYHPLX();
			param.setAppId(appId);
			param.setLx(yhplx.getMc());
			if (countYHPLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s易耗品分类名称不能重复", line));
			}
		}

	}

	/**
	 * 验证excel批量插入的数据是否合法
	 * 
	 * @param appId
	 * @param yhplx
	 * @param lineNumber
	 * @param type
	 * @throws OperException
	 */
	private void checkYHPLX(Integer appId, CYHPLX yhplx, Integer lineNumber, YHPEXCELTYPE type) throws OperException {
		String line = lineNumber == null ? "" : "第" + lineNumber + "行：";
		// 声明一个临时参数变量
		VYHPLX param;
		switch (type) {
		case 易耗品类型: {
			// LX_ID不能为空
			if (StringUtils.isEmpty(yhplx.getLxId())) {
				throw new OperException(new OperErrCode("10401", "%s易耗品分类号不能为空", line));
			}
			// LX_ID不能重复
			param = new VYHPLX();
			param.setAppId(appId);
			param.setLx(yhplx.getLxId());
			if (countYHPLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s易耗品分类号称不能重复", line));
			}
			// LX_MC不能为空
			if (StringUtils.isEmpty(yhplx.getMc())) {
				throw new OperException(new OperErrCode("10402", "%s易耗品分类名称不能为空", line));
			}
			// LX_MC不能重复
			param = new VYHPLX();
			param.setAppId(appId);
			param.setLx(yhplx.getMc());
			if (countYHPLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s易耗品分类名称不能重复", line));
			}
			break;
		}
		case 易耗品品种: {
			// LX_PID不能为空
			if (StringUtils.isEmpty(yhplx.getLxPid())) {
				throw new OperException(new OperErrCode("10401", "%s所属易耗品分类号不能为空", line));
			}
			// LX_MC不能为空
			if (StringUtils.isEmpty(yhplx.getMc())) {
				throw new OperException(new OperErrCode("10402", "%s易耗品品种名称不能为空", line));
			}
			// LX_MC不能重复
			param = new VYHPLX();
			param.setAppId(appId);
			param.setLx(yhplx.getMc());
			if (countYHPLXList(param) > 0) {
				throw new OperException(new OperErrCode("10403", "%s易耗品品种名称不能重复", line));
			}
			break;
		}
		}
	}

	/**
	 * 统计一级易耗品类型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countLevel1YHPLXList(VYHPLX param) {
		return vyhpDao.countLevel1YHPLXList(param);
	}

	/**
	 * 统计二级易耗品类型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countLevel2YHPLXList(VYHPLX param) {
		return vyhpDao.countLevel2YHPLXList(param);
	}

	/**
	 * 统计资易耗品型列表
	 * 
	 * @param param
	 * @return
	 */
	public int countYHPLXList(VYHPLX param) {
		return vyhpDao.countYHPLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的 一级 易耗品类型列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VYHPLX> getLevel1YHPLXList(VYHPLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return vyhpDao.selectLevel1YHPLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的 二级 易耗品类型列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VYHPLX> getLevel2YHPLXList(VYHPLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return vyhpDao.selectLevel2YHPLXList(param);
	}

	/**
	 * 获取指定应用下的指定页的 一级 易耗品类型列表。
	 * 
	 * @param param
	 *            模糊查询
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VYHPLX> getLevel1PageYHPLXList(VYHPLX param, int rows, int page) {
		int total = countLevel1YHPLXList(param);
		EasyuiDatagrid<VYHPLX> pageDatas = new EasyuiDatagrid<VYHPLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VYHPLX>());
		} else {
			List<VYHPLX> list = getLevel1YHPLXList(param, rows, page);
			for(VYHPLX yhplx : list){
				String remark = StringUtils.nullToEmpty(yhplx.getRemark());
				yhplx.setRemark(remark);
			}
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 获取指定应用下的指定页的 一级 易耗品类型列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VYHPLX> getLevel1PageYHPLXList(Integer appId, int rows, int page) {
		VYHPLX param = new VYHPLX();
		param.setAppId(appId);
		return getLevel1PageYHPLXList(param, rows, page);
	}

	/**
	 * 获取指定应用下的指定页的 二级 易耗品类型列表。
	 * 
	 * @param param
	 *            模糊查询
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VYHPLX> getLevel2PageZCLXList(VYHPLX param, int rows, int page) {
		int total = countLevel2YHPLXList(param);
		EasyuiDatagrid<VYHPLX> pageDatas = new EasyuiDatagrid<VYHPLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VYHPLX>());
		} else {
			List<VYHPLX> list = getLevel2YHPLXList(param, rows, page);
			for(VYHPLX yhplx : list){
				String remark = StringUtils.nullToEmpty(yhplx.getRemark());
				yhplx.setRemark(remark);
			}
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 检查类型ID
	 * 
	 * @param appId
	 * @param lxid
	 * @return
	 */
	public boolean checkYHPLXByLXID(Integer appId, String lxid) {
		if (StringUtils.isEmpty(lxid)) {
			return false;
		}
		if (lxid.length() > 4) {
			return false;
		}
		CYHPLX param = new CYHPLX();
		param.setAppId(appId);
		param.setLxId(lxid);
		if (getYHPLX(param) != null) {
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
	public boolean checkYHPLXByMC(Integer appId, String mc) {
		if (StringUtils.isEmpty(mc)) {
			return false;
		}
		VYHPLX param = new VYHPLX();
		param.setAppId(appId);
		param.setLx(mc);
		if (countYHPLXList(param) > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查更新时名称
	 * 
	 * @param appId
	 * @param oldMC
	 * @param newMC
	 * @return
	 */
	public boolean checkYHPLXByUpdateMC(Integer appId, String oldMC, String newMC) {
		if (StringUtils.isEmpty(newMC)) {
			return true;
		}
		if (oldMC.equals(newMC)) {
			return true;
		}

		VYHPLX param = new VYHPLX();
		param.setAppId(appId);
		param.setLx(newMC);
		if (countYHPLXList(param) > 0) {
			return false;
		}

		return true;
	}

	/**
	 * 根据pojo获取资易耗品型数据
	 * 
	 * @param CYHPLX
	 * @exception 易耗品类型记录不存在
	 * @return
	 */
	public CYHPLX getYHPLX(CYHPLX param) {
		CYHPLX yhplx = cyhpDao.select(param);
		return yhplx;
	}

	/**
	 * 插入一个易耗品类型记录
	 * 
	 * @param yhplx
	 * @throws OperException
	 *             易耗品分类号不能为空， 易耗品分类名称不能为空，易耗品分类名称不能重复，
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHPLX(CYHPLX yhplx) throws OperException {
		checkYHPLX(yhplx.getAppId(), yhplx, null, true);

		cyhpDao.insertSelective(yhplx);
	}

	/**
	 * 插入一组易耗品记录。记录来自一个Excel表 错误时，显示：第XX行，易耗品分类号不能为空
	 * 
	 * @param appId
	 * @param in
	 * @param type
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHPLXList(Integer appId, InputStream in, YHPEXCELTYPE type) throws OperException {
		Map<Integer, CYHPLX> yhplxMap = null;

		switch (type) {
		case 易耗品类型:
			yhplxMap = parseFile(in, YHPEXCELTYPE.易耗品类型, appId);
			CYHPLX yhplx;
			for (Integer lineNumber : yhplxMap.keySet()) {
				yhplx = yhplxMap.get(lineNumber);
				yhplx.setAppId(appId);

				cyhpDao.insertSelective(yhplx);
			}
			break;
		case 易耗品品种:
			yhplxMap = parseFile(in, YHPEXCELTYPE.易耗品品种, appId);
			CYHPLX yhplx2;
			for (Integer lineNumber : yhplxMap.keySet()) {
				yhplx2 = yhplxMap.get(lineNumber);
				yhplx2.setAppId(appId);
				yhplx2.setLxId(yhplx2.getLxPid());// lxid不能为空，所以先暂时设置为pid,后面再更新
				cyhpDao.insertSelective(yhplx2);
				// 根据LXPID生成LXID
				CYHPLX param = cyhpDao.select(yhplx2);
				String lxId = generateLXID(param.getLxPid(), param.getId() + "");
				// 更新LXID
				param.setLxId(lxId);
				cyhpDao.updateByPrimaryKeySelective(param);
			}
			break;
		}
	}

	/**
	 * 插入一组易耗品类型记录。记录来自一个Excel表 错误时，显示：第XX行，易耗品分类号不能为空
	 * 
	 * @param appId
	 * @param file
	 * @param type
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHPLXList(Integer appId, MultipartFile file, YHPEXCELTYPE type) throws OperException {
		InputStream in = ExcelFileUtils.importExcel(file);
		insertYHPLXList(appId, in, type);
	}

	/**
	 * 插入一个易耗品类型记录
	 * 
	 * @param appId
	 * @param lxid
	 * @param lxPid
	 * @param mc
	 * @param remark
	 * @param pic_url
	 * @param img_version
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHPLX(Integer appId, String lxid, String lxPid, String mc, String remark, String pic_url, Integer img_version) throws OperException {
		CYHPLX param = new CYHPLX();
		param.setAppId(appId);
		param.setLxId(lxid);
		param.setLxPid(lxPid);
		param.setMc(mc);
		param.setRemark(remark);
		param.setPicUrl(pic_url);
		param.setImgVersion(img_version);

		insertYHPLX(param);
	}

	/**
	 * 更新一个易耗品类型记录。
	 * 
	 * @param id
	 * @param lxid
	 * @param lxPid
	 * @param mc
	 * @param remark
	 * @param pic_url
	 * @param img_version
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateYHPLX(Integer id, String lxid, String lxPid, String mc, String remark, String pic_url, Integer img_version) throws OperException {
		CYHPLX param = new CYHPLX();
		param.setId(id);
		CYHPLX oldYHPLX = getYHPLX(param);

		CYHPLX newYHPLX = new CYHPLX();
		newYHPLX.setId(id);
		newYHPLX.setAppId(oldYHPLX.getAppId());
		newYHPLX.setLxId(lxid);
		newYHPLX.setLxPid(lxPid);
		newYHPLX.setMc(mc);
		newYHPLX.setRemark(remark);
		newYHPLX.setPicUrl(pic_url);
		newYHPLX.setImgVersion(img_version);

		// 修改了类型名
		if (oldYHPLX.getMc().equals(mc)) {
			checkYHPLX(newYHPLX.getAppId(), newYHPLX, null, false);
		} else {
			checkYHPLX(newYHPLX.getAppId(), newYHPLX, null, true);
		}

		cyhpDao.updateByPrimaryKeySelective(newYHPLX);
	}

	/**
	 * 1.使用POI解析excel文件，生成一个Map。key为行号，value为一个记录 2.对excel文件的预处理
	 * 
	 * @param in
	 * @param type
	 * @param appId
	 * @return
	 * @throws OperException
	 */
	private Map<Integer, CYHPLX> parseFile(InputStream in, YHPEXCELTYPE type, Integer appId) throws OperException {
		// 通过in获得表格工作簿sheet
		Map<Integer, CYHPLX> content = new HashMap<Integer, CYHPLX>();
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
			case 易耗品类型:
				// 解析资产类型表格，从表格的第二行开始迭代
				for (int i = 1; i <= rowNum; i++) {
					Row row = sheet.getRow(i);
					if (ExcelFileUtils.isRowEmpty(row)) {
						continue;// 如果此行为空（不包含任何数据和样式）。跳过。
					} else {
						// 将此行记录转换成CYHPLX的pojo
						CYHPLX CYHPLX = new CYHPLX();
						// 判断在此表格中LX_ID是否重复
						String lxid = row.getCell(0).toString().trim();
						if (lxidSet.contains((String) lxid)) {
							String line = "第" + (i + 1) + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的易耗品分类号 " + lxid, line));
						} else {
							lxidSet.add(lxid);
							CYHPLX.setLxId(lxid);
						}
						// 判断在此表格中MC是否重复
						String mc = row.getCell(1).toString().trim();
						if (mcSet.contains((String) mc)) {
							String line = "第" + i + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的易耗品类型名称 " + mc, line));
						} else {
							mcSet.add(mc);
							CYHPLX.setMc(mc);
						}
						CYHPLX.setRemark(row.getCell(2).toString());
						// 对此pojo做资产类型check
						checkYHPLX(appId, CYHPLX, i + 1, type);
						// 将pojo放入map
						content.put(i + 1, CYHPLX);
					}

				}
				break;
			case 易耗品品种:
				// 解析资产类型表格，从表格的第二行开始迭代
				for (int i = 1; i <= rowNum; i++) {
					Row row = sheet.getRow(i);
					if (ExcelFileUtils.isRowEmpty(row)) {
						continue;// 如果此行为空（不包含任何数据和样式）。跳过。
					} else {
						// 将此行记录转换成CYHPLX的pojo
						CYHPLX CYHPLX = new CYHPLX();
						CYHPLX.setLxPid(row.getCell(0).toString().trim());
						// 判断在此表格中MC是否重复
						String mc = row.getCell(1).toString().trim();
						if (mcSet.contains((String) mc)) {
							String line = "第" + (i + 1) + "行：";
							throw new OperException(new OperErrCode("10401", "%s表格中含有相同的易耗品品种名称 " + mc, line));
						} else {
							mcSet.add(mc);
							CYHPLX.setMc(mc);
						}
						CYHPLX.setRemark(row.getCell(2).toString());
						// 对此pojo做资产类型check
						checkYHPLX(appId, CYHPLX, i + 1, type);
						// 将pojo放入map
						content.put(i + 1, CYHPLX);
					}
				}
				break;
			}
		} catch (IOException e) {
			throw new OperException(读取易耗品类型文件异常);
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

	/**
	 * 获得一级易耗品类型名称列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CYHPLX> getLevel1YHPLXList(CYHPLX param) {
		return cyhpDao.selectLevel1List(param);
	}

	/**
	 * 获得二级易耗品类型名称列表
	 * 
	 * @param param
	 * @return
	 */
	public List<CYHPLX> getLevel2YHPLXList(CYHPLX param) {
		return cyhpDao.selectLevel2List(param);
	}

	/**
	 * 获得一级易耗品类型下拉框信息(包含全部)，id和text均为资产类型名 and lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1YHPLXMCComboWithAll(Integer appId) {
		CYHPLX param = new CYHPLX();
		param.setAppId(appId);
		List<CYHPLX> list = getLevel1YHPLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			// 没有ID，导致全部不可选
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CYHPLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getMc());
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得一级易耗品类型下拉框信息，text为易耗品类型名，id是id lx_pid is null;
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getLevel1YHPLXMCCombo(Integer appId) {
		CYHPLX param = new CYHPLX();
		param.setAppId(appId);
		List<CYHPLX> list = getLevel1YHPLXList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CYHPLX zclx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zclx.getLxId() + "");
				item.setText(zclx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 生成易耗品类型 easyuiTree
	 */
	public List<EasyuiTree> getYHPLXTree(Integer appId,String yhplx_mc){
		//获取一级易耗品类型列表
		CYHPLX param=new CYHPLX();
		param.setAppId(appId);
		List<CYHPLX> list1=getLevel1YHPLXList(param);
		//创建根节点
		EasyuiTree root=new EasyuiTree("", "全部", new ArrayList<EasyuiTree>(), "open", "icon-deviceGroup", false);
		EasyuiTree node;
		EasyuiTree subnode;
		//遍历一级易耗品类型，生成一级树节点
		for (CYHPLX yhplx : list1) {
			node=new EasyuiTree(yhplx.getId()+"", yhplx.getMc(), new ArrayList<EasyuiTree>(), "open", "icon-deviceGroup",false);
			root.getChildren().add(node);
			//获取二级易耗品类型列表
			param.setLxPid(yhplx.getLxId());
			List<CYHPLX> list2=getLevel2YHPLXList(param);
			//遍历二级易耗品类型，将二级节点添加到一级节点中
			for (CYHPLX yhplx2 : list2) {
				subnode=new EasyuiTree(yhplx2.getId()+"", yhplx2.getMc(), new ArrayList<EasyuiTree>(), "open", "icon-device",false);
				//只添加包含yhplx_mc的易耗品类型
				if(!StringUtils.isEmpty(yhplx_mc) && yhplx2.getMc().contains(yhplx_mc)){
					node.getChildren().add(subnode);
				}else if(StringUtils.isEmpty(yhplx_mc)){
					node.getChildren().add(subnode);
				}
			}
		}
		List<EasyuiTree> result=new ArrayList<>();
		result.add(root);
		return result;
	}

}
