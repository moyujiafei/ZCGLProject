package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.LZTXZMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.dao.VZTMapper;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产管理-- 资产管理
 * 
 * @author 王登
 */
@Service("zcService")
public class ZCService {
	@Autowired
	private JZCMapper zcDao;
	@Autowired
	private VZCMapper vzcDao;
	@Autowired
	private VZTMapper vZTDao;
	@Autowired
	private LZTXZMapper lZTXZDao;

	@Autowired
	private WXAppService wXAppService;
	
	@Autowired
	private ZCZTService zcztService;
	
	@Autowired
	private WXDeptService wxDeptService;

	public static final OperErrCode 资产编号不能为空 = new OperErrCode("10201", "资产编号不能为空 ");
	public static final OperErrCode 资产名称不能为空 = new OperErrCode("10201", "资产名称不能为空");
	public static final OperErrCode 资产类型不能为空 = new OperErrCode("10201", "资产类型不能为空");
	public static final OperErrCode 资产保管人不能为空 = new OperErrCode("10201", "资产保管人不能为空");
	public static final OperErrCode 资产编号不能重复 = new OperErrCode("10201", "资产编号不能重复");
	public static final OperErrCode 无法删除有资产的资产 = new OperErrCode("10201", "无法删除有资产的资产");

	private void checkZC(JZC zc, boolean isInsert) throws OperException {
		if (StringUtils.isEmpty(zc.getDm())) {
			throw new OperException(资产编号不能为空);
		}

		if (StringUtils.isEmpty(zc.getMc())) {
			throw new OperException(资产名称不能为空);
		}

		if (zc.getLxId() == null) {
			throw new OperException(资产类型不能为空);
		}

		if (StringUtils.isEmpty(zc.getSyr())) {
			throw new OperException(资产保管人不能为空);
		}

		// 插入新资产时，资产编号不能重复。更新操作不检查
		if (isInsert) {
			VZC param = new VZC();
			param.setAppId(zc.getAppId());
			param.setZcdm(zc.getDm());

			// select count(*) from v_zc where appId= ? and zcdm = ?;
			if (countZCList(param) > 0) {
				throw new OperException(资产编号不能重复);
			}
		}
	}

	/**
	 * 插入一个资产记录。
	 * 
	 * @param rw
	 * @throws OperException
	 *             资产编号不能为空, 资产名称不能为空, 资产类型不能为空, 资产保管人不能为空, 资产编号不能重复
	 * 
	 *             资产编号不能重复：指的是相同appId下，资产编号不能重复。
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZC(JZC zc) throws OperException {
		checkZC(zc, true);
		zcDao.insertSelective(zc);
	}

	/**
	 * 更新一个资产记录。
	 * 
	 * @param rw
	 * @throws OperException
	 *             资产编号不能为空, 资产名称不能为空, 资产类型不能为空, 资产保管人不能为空, 资产编号不能重复
	 * 
	 *             资产编号不能重复：指的是相同appId下，资产编号不能重复。
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateZC(JZC zc) throws OperException {
		checkZC(zc, false);
		zcDao.updateByPrimaryKeySelective(zc);
	}

	/**
	 * 删除资产。 当资产中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param rw
	 * @throws OperException
	 *             无法删除资产
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delZC(JZC zc) throws OperException {
		try {
			zcDao.deleteByPrimaryKey(zc.getId());
		} catch (Exception e) {
			throw new OperException(无法删除有资产的资产);
		}

	}

	/**
	 * 统计资产列表
	 * 
	 * @param param
	 * @return
	 */
	public int countZCList(VZC param) {
		return vzcDao.countZCList(param);
	}

	/**
	 * 获取资产列表
	 * 
	 * @param param
	 * @return
	 */
	public List<VZC> getZCList(VZC param) {
		return vzcDao.selectList(param);
	}

	/**
	 * 获取指定应用下的指定页的资产列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<VZC> getZCList(VZC param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getZCList(param);
	}

	/**
	 * 获取指定应用下的指定页的资产列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<VZC> getPageZCList(VZC param, int rows, int page) {
		int total = countZCList(param);

		EasyuiDatagrid<VZC> pageDatas = new EasyuiDatagrid<VZC>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZC>());
		} else {
			List<VZC> list = getZCList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 获取指定资产状态下的一组资产列表
	 * 
	 * @param appId
	 * @param zczt
	 * @return
	 */
	public List<VZC> getZCList(Integer appId, ZCZT zczt) {
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZczt(zczt.getValue());

		return getZCList(param);
	}

	/**
	 * 根据主键获取资产数据
	 * 
	 * @param id
	 * @exception 资产记录不存在
	 * @return
	 */
	public JZC getZC(Integer id) {
		return zcDao.selectByPrimaryKey(id);
	}

	/**
	 * 根据参数获取资产数据
	 * 
	 * @param id
	 * @return
	 */
	public JZC getZC(JZC param) {
		return zcDao.select(param);
	}

	/**
	 * 根据参数获取资产数据
	 * 
	 * @return
	 */
	public VZC getZC(VZC param) {
		return vzcDao.select(param);
	}

	/**
	 * 获取指定任务下的资产列表
	 * 
	 * @param rwid
	 * @param rows
	 * @param page
	 * @return
	 */
	public List<VZC> getRWZCList(Integer rwid, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rwid", rwid);
		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());
		return vzcDao.getZCListFuzzyByRW(param);
	}
	
	/**
	 * 根据任务id和是否完成查询对应资产信息
	 * @param rwid
	 * @param rows
	 * @param page
	 * @param finish
	 * @return
	 */
	public List<VZC> getRWZCList(Integer rwid, int rows, int page, Integer finish) {
		PageNavigator pn = new PageNavigator(rows, page);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rwid", rwid);
		param.put("finish", finish);
		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());
		return vzcDao.getZCListFuzzyByRW(param);
	}

	public int countRWZCList(Integer rwid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rwid", rwid);
		return vzcDao.countZCListFuzzyByRW(param);
	}

	/**
	 * 获取指定任务下的资产列表
	 * 
	 * @param rwid
	 * @param rows
	 * @param page
	 * @return
	 */
	public List<VZC> getRWZCList(Integer rwid, String czr, String zcdm, String zcmc, String zclx, String cfdd, String syr, Integer zczt,
			Integer zcxzfinish, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rwid", rwid);
		param.put("zcdm", zcdm);
		param.put("zcmc", zcmc);
		param.put("zclx", zclx);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zczt", zczt);
		param.put("finish", zcxzfinish);

		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());
		return vzcDao.getZCListFuzzyByRW(param);
	}

	public int countRWZCList(Integer rwid, String czr, String zcdm, String zcmc, String zclx, String cfdd, String syr, Integer zczt,
			Integer zcxzfinish) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rwid", rwid);
		param.put("zcdm", zcdm);
		param.put("zcmc", zcmc);
		param.put("zclx", zclx);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zczt", zczt);
		param.put("finish", zcxzfinish);

		return vzcDao.countZCListFuzzyByRW(param);
	}

	public List<VZC> getRWZCList(Integer appId, String czr, Integer rwlx, String zcdm, String zcmc, String zclxid, String cfdd, String syr,
			Integer zczt, Integer zcxzfinish, int rows, int page) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appId", appId);
		param.put("czr", czr);
		param.put("zcdm", zcdm);
		param.put("zcmc", zcmc);
		param.put("zclxId", zclxid);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zczt", zczt);
		param.put("finish", zcxzfinish);
		param.put("lx", rwlx); // 任务类型

		PageNavigator pn = new PageNavigator(rows, page);
		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());

		return vzcDao.getZCListFuzzyByRW(param);
	}

	public int countRWZCList(Integer appId, String czr, Integer rwlx, String zcdm, String zcmc, String zclxId, String cfdd, String syr, Integer zczt,
			Integer zcxzfinish) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appId", appId);
		param.put("czr", czr);
		param.put("zcdm", zcdm);
		param.put("zcmc", zcmc);
		param.put("zclxId", zclxId);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zczt", zczt);
		param.put("finish", zcxzfinish);
		param.put("lx", rwlx); // 任务类型

		return vzcDao.countZCListFuzzyByRW(param);
	}

	public EasyuiDatagrid<VZC> getPageRWZCList(Integer rwid, int rows, int page) {
		int total = countRWZCList(rwid);
		EasyuiDatagrid<VZC> result = new EasyuiDatagrid<>();
		if (total == 0) {
			result.setRows(new ArrayList<VZC>());
		} else {
			result.setRows(getRWZCList(rwid, rows, page));
		}
		result.setTotal(total);
		return result;
	}

	public EasyuiDatagrid<VZC> getPageRWZCList(Integer appId, String czr, Integer rwlx, String zcdm, String zcmc, String zclx, String cfdd,
			String syr, Integer zczt, Integer zcxzfinish, int rows, int page) {

		int total = countRWZCList(appId, czr, rwlx, zcdm, zcmc, zclx, cfdd, syr, zczt, zcxzfinish);
		List<VZC> elements = getRWZCList(appId, czr, rwlx, zcdm, zcmc, zclx, cfdd, syr, zczt, zcxzfinish, rows, page);

		EasyuiDatagrid<VZC> pageDatas = new EasyuiDatagrid<VZC>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZC>());
		} else {
			pageDatas.setRows(elements);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 查询v_zc，获取资产列表。
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<VZC> getVZCList(Integer appId, String zcdm, String zc,
			String zclx, String cfdd, String syr, String deptName, String glr,
			Integer zczt, int page, int rows) {
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZcdm(zcdm);
		// 如果不为空，需要进行大小写忽略的模糊查找
		param.setZc(zc);
		// 找到值对应的名称
		param.setZclx(zclx);
		// 如果不为空，需要进行大小写忽略的模糊查找
		param.setCfdd(cfdd);
		param.setSyr(syr);
		param.setDeptName(deptName);
		param.setGlr(glr);
		param.setZczt(zczt);

		PageNavigator pageNavigator = new PageNavigator(rows, page);
		param.setStart(pageNavigator.getStart());
		param.setOffset(pageNavigator.getOffset());

		return vzcDao.getZCListFuzzy(param);
	}

	/**
	 * 查询v_zc，获取资产数量。
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	public int countVZCList(Integer appId, String zcdm, String zc, String zclx, String cfdd, String syr, String deptName, String glr, Integer zczt) {
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZcdm(zcdm);
		// 如果不为空，需要进行大小写忽略的模糊查找
		param.setZc(zc);
		// 找到值对应的名称
		param.setZclx(zclx);
		// 如果不为空，需要进行大小写忽略的模糊查找
		param.setCfdd(cfdd);
		param.setSyr(syr);
		param.setDeptName(deptName);
		param.setGlr(glr);
		param.setZczt(zczt);

		return vzcDao.countZCListFuzzy(param);
	}

	/**
	 * 查询v_zc，获取资产列表。
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<VZC> getVZCList(Integer appId, String zcdm, String zcmc,
			String zclx, String cfdd, String syr, String deptName, String glr,
			List<Integer> zcztList, int page, int rows) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appId", appId);
		param.put("zcdm", zcdm);
		param.put("zc", zcmc);
		param.put("zclx", zclx);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("deptName", deptName);
		param.put("glr", glr);
		param.put("zcztList", zcztList);

		PageNavigator pn = new PageNavigator(rows, page);
		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());

		return vzcDao.getZCListFuzzyByZTList(param);
	}

	/**
	 * 查询v_zc，获取资产数量。
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	public int countVZCList(Integer appId, String zcdm, String zcmc, String zclx, String cfdd, String syr, String deptName, String glr,
			List<Integer> zcztList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appId", appId);
		param.put("zcdm", zcdm);
		param.put("zcmc", zcmc);
		param.put("zclx", zclx);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("deptName", deptName);
		param.put("glr", glr);
		param.put("zcztList", zcztList);

		return vzcDao.countZCListFuzzyByZTList(param);
	}

	public EasyuiDatagrid<VZC> getPageVZCList(Integer appId, String zcdm, String zcmc, String zclx, String cfdd, String syr, String deptName,
			String glr, List<Integer> zcztList, int page, int rows) {

		int total = countVZCList(appId, zcdm, zcmc, zclx, cfdd, syr, deptName, glr, zcztList);
		List<VZC> elements = getVZCList(appId, zcdm, zcmc, zclx, cfdd, syr, deptName, glr, zcztList, page, rows);

		EasyuiDatagrid<VZC> pageDatas = new EasyuiDatagrid<VZC>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZC>());
		} else {
			pageDatas.setRows(elements);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	public EasyuiDatagrid<VZC> getPageVZCList(Integer appId, String zcdm, String zcmc, String zclx, String cfdd, String syr, String deptName,
			String glr, Integer zczt, int page, int rows) {

		int total = countVZCList(appId, zcdm, zcmc, zclx, cfdd, syr, deptName, glr, zczt);
		List<VZC> elements = getVZCList(appId, zcdm, zcmc, zclx, cfdd, syr, deptName, glr, zczt, page, rows);

		EasyuiDatagrid<VZC> pageDatas = new EasyuiDatagrid<VZC>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZC>());
		} else {
			pageDatas.setRows(elements);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 获取指定部门下的资产列表，包括其所有直属子节点的资产。
	 * 
	 * @return
	 */
	public EasyuiDatagrid<VZC> getPageVZCListByDeptNoAndZCLX(Integer appId, String zcdm, String zcmc, String zclxid, String cfdd, String syr, Integer deptno,
			String glr, List<Integer> zcztList, int page, int rows) {
		//获取指定部门的所有子部门列表(包括指定部门)
		List<String> deptNoList=wxDeptService.getSubDeptmentByDeptNo(appId, deptno);
		//查询指定部门列表对应的资产列表
		Map<String, Object> param = new HashMap<String, Object>();
		if(deptNoList.size() > 1 || !StringUtils.isEmpty(deptNoList.get(0)) ){
			param.put("list", deptNoList);
		}
		param.put("appId", appId);
		param.put("zcdm", zcdm);
		param.put("zc", zcmc);
		param.put("zclxId", zclxid);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zcztList", zcztList);
		param.put("glr", glr);
		PageNavigator pn = new PageNavigator(rows, page);
		param.put("start", pn.getStart());
		param.put("offset", pn.getOffset());
		
		int total=vzcDao.countZCListByByDeptNoListAndZCLXID(param);
		List<VZC> zcList=vzcDao.selectByDeptNoListAndZCLXID(param);
		
		return new EasyuiDatagrid<>(zcList, total);
	}
	
	/**
	 * 获取指定部门和资产类型下的资产列表，包括其所有直属子节点的资产。
	 */
	public List<VZC> getVZCListByDeptNoAndZCLX(Integer appId, String zcdm, String zcmc, String zclxid, String cfdd, String syr, Integer deptno,
			String glr, List<Integer> zcztList) {
		//获取指定部门的所有子部门列表(包括指定部门)
		List<String> deptNoList=wxDeptService.getSubDeptmentByDeptNo(appId, deptno);
		//查询指定部门列表对应的资产列表
		Map<String, Object> param = new HashMap<String, Object>();
		if(deptNoList.size() > 1 || !StringUtils.isEmpty(deptNoList.get(0)) ){
			param.put("list", deptNoList);
		}
		param.put("appId", appId);
		param.put("zcdm", zcdm);
		param.put("zc", zcmc);
		param.put("zclxId", zclxid);
		param.put("cfdd", cfdd);
		param.put("syr", syr);
		param.put("zcztList", zcztList);
		param.put("glr", glr);
		
		List<VZC> zcList=vzcDao.selectByDeptNoListAndZCLXID(param);
		
		return zcList;
	}

	/**
	 * 获取已经过期的资产列表
	 * 
	 * @param appId
	 * @return
	 */
	public List<VZC> getDeprecatedZCList(Integer appId) {
		return vzcDao.getDeprecatedZCList(appId);
	}

	


	

	/**
	 * 获得图片完整访问路径
	 * 
	 * @param picUrl
	 * @param imgVersion
	 * @return
	 */
	public String getSmallImgUrl(String picUrl, Integer imgVersion) {
		if (StringUtils.isEmpty(picUrl)) {
			return null;
		}
		return ZCGLProperties.URL_SERVER + picUrl + "_m.jpg?imgVersion=" + (imgVersion == null ? 0 : imgVersion);
	}

	/**
	 * 获取原图地址
	 * 
	 * @param picUrl
	 * @param imgVersion
	 * @return
	 */
	public String getOriginalImgUrl(String picUrl, Integer imgVersion) {
		if (StringUtils.isEmpty(picUrl)) {
			return null;
		}
		return ZCGLProperties.URL_SERVER + picUrl + ".jpg?imgVersion=" + (imgVersion == null ? 0 : imgVersion);
	}


}
