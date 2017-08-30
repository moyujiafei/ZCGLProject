package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.JRWXZ;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.DateUtils;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务管理：任务创建（日常巡检任务、故障维修任务），任务验收等
 * 
 * @author 杨靖
 */
@Service("rwglService")
public class RWGLService {
	public static final OperErrCode 任务已经开始无法删除 =  new OperErrCode("200001", "任务已经开始, 无法删除");
	
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
	@Autowired
	private JRWMapper rwDao;
	
	@Autowired
	private JZCMapper zcDao;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	/**
	 * 当后勤管理人员创建一个任务，系统将向任务所在的操作人、验收人推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<创建人>于<系统时间>创建了一个从<开始时间>开始到<结束时间>结束的<任务类型>任务，任务操作人为<操作人>，验收人为<验收人>。点击<查看详情>。
	 * 
	 * 查看详情：查看任务详情
	 * 
	 * @param cjr 创建人的userid
	 */
	public void sendCreateRWMsg(String cjr, JRW rw) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(msgTemplateService.getUser(rw.getAppId(), cjr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("创建了一个从");
		sb.append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
		sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
		
		RWLX rwlx = RWLX.valueOf(rw.getLx());
		sb.append(rwlx).append("任务，任务操作人为");
		
		sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getCzr())).append("，验收人为");
		sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getYsr())).append("。点击");
		sb.append(msgTemplateService.getRWXQ(rw));
		
		// 给任务操作人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
		
		// 给任务验收人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getYsr(), sb.toString());
	}
	
	/**
	 * 后勤管理人员，创建“日常巡检”任务
	 * 
	 * 1.	资产列表中列出所有“使用中”的资产，选中多个资产，在J_RW中创建巡检任务。
	 * 	任务类型LX：日常巡检
	 * 	开始时间KSSJ：默认为系统时间
	 * 	结束时间JSSJ：默认为系统时间
	 * 	操作人CZR：不能为空，默认为当前用户。用户可以从部门名为“后勤集团”中选择
	 * 	操作备注：可以为空
	 * 	验收人：能为空，默认为当前用户。用户可以从部门名为“后勤集团”中选择
	 * 	更新巡检任务中的总细则数（TOTAL）：用户选择的资产数
	 * 2.	在J_RWXZ中批量添加设备，创建任务细则信息
	 * 	是否完成FINISH为0.
	 * 3.	在J_ZC中:
	 * 	将选中设备状态置为“巡检中”。
	 * 4.	向L_ZT中创建记录
	 * 	记录时间：当前系统时间
	 * 	记录人：创建人
	 * 	原状态：“使用中”
	 * 	新状态：“巡检中”
	 * 5.	分别向操作人和验收人发消息。sendCreateRWMsg(JRW rw)
	 *
	 * @param cjr 创建人的userid，一般为当前用户
	 * 
	 * @throws OperException
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public JRW createRCXJ(Integer appId, Date kssj, Date jssj, String czr, String czRemark, String ysr, 
			               Date yssj,String cjr, List<Integer> zcidList) throws OperException {
		// 创建巡检任务
		JRW rw = new JRW();
		rw.setAppId(appId);
		rw.setLx(RWLX.日常巡检.getValue());
		rw.setKssj(kssj);
		rw.setJssj(jssj);
		rw.setCzr(czr);
		rw.setCzRemark(czRemark);
		rw.setYsr(ysr);
		rw.setYssj(yssj);
		rw.setTotal(zcidList.size());
		rwService.insertRW(rw);
		
		JRWXZ xz;
		JZC zc;
		for (Integer zcid : zcidList) {			
			zc = zcService.getZC(zcid);
			// 在J_RWXZ中批量添加设备
			xz = new JRWXZ();
			xz.setRwid(rw.getId());
			xz.setZcid(zcid);
			xz.setFinish(0);
			rwxzDao.insert(xz);
			
			// 在J_ZC中将选中设备状态置为“巡检中”
			zc.setZt(ZCZT.巡检中.getValue());
			zcService.updateZC(zc);
			
			// 向L_ZT中创建记录
			LZT zt = new LZT();
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.使用中.getValue());
			zt.setNewZt(ZCZT.巡检中.getValue());
			
			StringBuilder remark = new StringBuilder();
			remark.append("后勤管理人员创建了一个日常巡检任务");
			zt.setRemark(remark.toString());
			ztService.insertZT(zt);
		}
		
		// 分别向任务操作人和验收人发消息。
		sendCreateRWMsg(cjr, rw);
		
		return rw;
	}
	
	/**
	 * 当后勤管理人员删除一个未开始的任务，系统将向任务所在的操作人、验收人推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<创建人>于<系统时间>删除了一个从<开始时间>开始到<结束时间>结束的<任务类型>任务。
	 * 
	 * 查看详情：查看任务详情
	 * 
	 * @param cjr 创建人的userid
	 */
	public void sendDelRWMsg(String cjr, JRW rw) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(msgTemplateService.getUser(rw.getAppId(), cjr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("删除了一个从");
		sb.append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
		sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
		
		RWLX rwlx = RWLX.valueOf(rw.getLx());
		sb.append(rwlx).append("任务。");
		
		// 给任务操作人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
		
		// 给任务验收人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getYsr(), sb.toString());
	}
	
	/**
	 * 删除一个日常巡检任务。注意：这里仅能删除FINISH_COUNT为0（即任务尚未开始）的任务。否则，抛“任务已经开始，无法删除”异常。
	 * 
	 * 1. 更新日常巡检任务中涉及到的资产状态，重新置为“使用中”
	 * 2. 向L_ZT中创建记录
	 * 	          记录时间：当前系统时间
	 * 	记录人：创建人
	 * 	原状态：“巡检中”
	 * 	新状态：“使用中”
	 *      备注：后勤管理人员删除了一个尚未开始的日常巡检任务。
	 * 2. 分别向操作人和验收人发消息。sendDelRWMsg(JRW rw)
	 * 
	 * @param rwid
	 * @param cjr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delRCXJ(Integer rwid, String cjr) throws OperException {
		JRW rw = rwService.getRW(rwid);
		if (rw.getFinishCount() != 0) {
			throw new OperException(任务已经开始无法删除);
		}
		
//		获取当前任务下所有资产信息
		List<JZC> zcList = zcDao.getZCList(rwid);
		JRWXZ param;
		for (JZC zc : zcList) {
			// 删除任务细则
			param = new JRWXZ();
			param.setRwid(rw.getId());
			param.setZcid(zc.getId());
			rwxzDao.deleteByPrimaryKey(rwxzDao.select(param).getId());
			
			// 重新置为“使用中”
			zc.setZt(ZCZT.使用中.getValue());
			zcService.updateZC(zc);
			
			// 向L_ZT中创建记录
			LZT zt = new LZT();
			zt.setAppId(rw.getAppId());
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.巡检中.getValue());
			zt.setNewZt(ZCZT.使用中.getValue());
			StringBuilder remark = new StringBuilder();
			remark.append("后勤管理人员删除了一个尚未开始的日常巡检任务");
			zt.setRemark(remark.toString());
			ztService.insertZT(zt);
		}
		
		rwDao.deleteByPrimaryKey(rwid);
		// 分别向任务操作人和验收人发消息。
		sendDelRWMsg(cjr, rw);
	}
	
	/**
	 * 获取当前任务列表。即当前系统时间 <= 任务结束时间
	 * 
	 * select * from j_rw where lx = ? and czr = ? and ysr = ? and finish = ?
	 * and CURDATE() <= jssj;
	 * 
	 * @param appId
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 * @throws OperException
	 */
	public List<JRW> getCurrRWList(Integer appId, Integer rwlx, String czr, String ysr, Integer finish, int rows, int page) {
		JRW param = new JRW();
		param.setAppId(appId);
		param.setLx(rwlx);
		param.setCzr(czr);
		param.setYsr(ysr);
		param.setFinish(finish);
		
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		
		return rwDao.selectCurRWList(param);
	}
	
	public int countCurrRWList(Integer appId, Integer rwlx, String czr, String ysr, Integer finish) {
		JRW param = new JRW();
		param.setAppId(appId);
		param.setLx(rwlx);
		param.setCzr(czr);
		param.setYsr(ysr);
		param.setFinish(finish);
		
		return rwDao.countCurRWList(param);
	}
	
	public EasyuiDatagrid<JRW> getPageCurrRWList(Integer appId, Integer rwlx, String czr, String ysr, Integer finish, int rows, int page) {
		List<JRW> list=getCurrRWList(appId, rwlx, czr, ysr, finish, rows, page);
		EasyuiDatagrid<JRW> result=new EasyuiDatagrid<>();
		int total=countCurrRWList(appId, rwlx, czr, ysr, 0);
		if(total==0){
			result.setRows(new ArrayList<JRW>());
		}else{
			result.setRows(list);
		}
		result.setTotal(total);
		return result;
	}
	
	/**
	 * 获取所有任务列表。
	 * 
	 * select * from j_rw where lx = ? and czr = ? and ysr = ? and finish = ?;
	 * 
	 * @param appId
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 * @throws OperException
	 */
	public List<JRW> getRWList(Integer appId, Integer rwlx, String czr, String ysr, Integer finish, int rows, int page) {
		JRW param = new JRW();
		param.setAppId(appId);
		param.setLx(rwlx);
		param.setCzr(czr);
		param.setYsr(ysr);
		param.setFinish(finish);
		
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		
		return rwDao.selectList(param);
	}
	
	public int countRWList(Integer appId, Integer rwlx, String czr, String ysr, Integer finish) {
		JRW param = new JRW();
		param.setAppId(appId);
		param.setLx(rwlx);
		param.setCzr(czr);
		param.setYsr(ysr);
		param.setFinish(finish);
		
		return rwDao.countRWList(param);
	}
	
	/**
	 * 后勤管理人员，创建“故障维修”任务
	 * 
	 * 1.	在J_RW中创建维修任务
	 * 		任务类型为：故障维修。
	 * 		开始时间：默认为当前系统时间
	 * 		结束时间：默认为当前系统时间
	 * 		操作人：不能为空。在维修人员中选择
	 * 		操作备注：可以为空
	 * 		验收人：默认为当前用户
	 * 		总细则数：为选中的资产数
	 * 		验收备注，验收时间为空；已完成数为0；是否完成为0.
	 * 2.   在J_RWXZ中批量插入选择的资产
	 * 3.   分别向操作人和验收人发消息。sendCreateRWMsg(JRW rw)
	 * 
	 * @param cjr 任务创建人userid
	 * @param zcidList 需要维修的设备列表
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public JRW createGZWX(Integer appId, Date kssj, Date jssj, String czr, String czRemark, String ysr, 
			               Date yssj,String cjr, List<Integer> zcidList) throws OperException {
		// 创建故障维修任务
		JRW rw = new JRW();
		rw.setAppId(appId);
		rw.setLx(RWLX.故障维修.getValue());
		rw.setKssj(kssj);
		rw.setJssj(jssj);
		rw.setYssj(yssj);
		rw.setCzr(czr);
		rw.setCzRemark(czRemark);
		rw.setYsr(ysr);
		rw.setTotal(zcidList.size());
		rwService.insertRW(rw);
		
		JRWXZ xz;
		for (Integer zcid : zcidList) {			
			// 在J_RWXZ中批量添加设备
			xz = new JRWXZ();
			xz.setRwid(rw.getId());
			xz.setZcid(zcid);
			xz.setFinish(0);
			rwxzDao.insert(xz);
			
			// 分别向任务操作人和验收人发创建"故障维修"任务的消息。
			sendCreateRWMsg(cjr, rw);
		}
		
		return rw;
	}
	
	/**
	 * 删除一个尚未开始的故障维修任务。注意：这里仅能删除FINISH_COUNT为0（即任务尚未开始）的任务。否则，抛“任务已经开始，无法删除”异常。
	 * 
	 * 1. 删除任务，及任务细则
	 * 2. 分别向操作人和验收人发消息。sendDelRWMsg(JRW rw)
	 * 
	 * @param rwid
	 * @param cjr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delGZWX(Integer rwid, String cjr) throws OperException {
		JRW rw = rwService.getRW(rwid);
		if (rw.getFinishCount() != 0) {
			throw new OperException(任务已经开始无法删除);
		}
		
//		获取当前任务下所有资产信息
		List<JZC> zcList = zcDao.getZCList(rwid);
		JRWXZ param;
		for (JZC zc : zcList) {
			// 删除任务细则
			param = new JRWXZ();
			param.setRwid(rw.getId());
			param.setZcid(zc.getId());
			rwxzDao.deleteByPrimaryKey(rwxzDao.select(param).getId());
			
			// 重新置为“申请维修”
			zc.setZt(ZCZT.申请维修.getValue());
			zcService.updateZC(zc);
			
			// 向L_ZT中创建记录
			LZT zt = new LZT();
			zt.setAppId(rw.getAppId());
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.维修中.getValue());
			zt.setNewZt(ZCZT.申请维修.getValue());
			StringBuilder remark = new StringBuilder();
			remark.append("后勤管理人员删除了一个尚未开始的故障维修任务");
			zt.setRemark(remark.toString());
			ztService.insertZT(zt);
		}
		
		rwDao.deleteByPrimaryKey(rwid);
		// 分别向任务操作人和验收人发消息。
		sendDelRWMsg(cjr, rw);
	}
	
	/**
	 * 当后勤管理人员更新了一个任务，系统将向任务所在的操作人、验收人推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<创建人>于<系统时间>更新了一个从<开始时间>开始到<结束时间>结束的<任务类型>任务，任务操作人为<操作人>，验收人为<验收人>。点击<查看详情>。
	 * 
	 * 查看详情：查看任务详情
	 * 
	 * @param cjr 创建人的userid
	 */
	private void sendUpdateRWMsg(String cjr, JRW rw) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(msgTemplateService.getUser(rw.getAppId(), cjr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("更新了一个从");
		sb.append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
		sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
		
		RWLX rwlx = RWLX.valueOf(rw.getLx());
		sb.append(rwlx).append("任务，任务操作人为");
		
		sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getCzr())).append("，验收人为");
		sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getYsr())).append("。点击");
		sb.append(msgTemplateService.getRWXQ(rw));
		
		// 给任务操作人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
		
		// 给任务验收人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getYsr(), sb.toString());
	}
	
	/**
	 * 后勤管理人员：更新一个“尚未完成”的任务的相关信息，如开始时间、结束时间，验收人等。
	 * 注意：这里不能改变任务类型，验收时间，等其他信息。
	 * 
	 * 1. 更新任务相关信息
	 * 2. 向任务操作人，验收人发消息：sendUpdateRWMsg
	 * 
	 * @param rwId
	 * @param kssj
	 * @param jssj
	 * @param czr
	 * @param czRemark
	 * @param ysr
	 * @param cjr 执行更新任务的userid
	 * @throws OperException
	 */
	public void updateRW(Integer rwId, Date kssj, Date jssj, String czr, String czRemark, String ysr, String cjr) throws OperException {
		JRW rw = rwDao.selectByPrimaryKey(rwId);
		rw.setKssj(kssj);
		rw.setJssj(jssj);
		rw.setCzr(czr);
		rw.setCzRemark(czRemark);
		rw.setYsr(ysr);
		
		rwDao.updateByPrimaryKey(rw);
		sendUpdateRWMsg(cjr, rw);
	}
	
	/**
	 * 当后勤管理人员延期（提前）了一个任务，系统将向任务所在的操作人、验收人推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：由于<原因>，<创建人>于<系统时间>将从<开始时间>开始到<结束时间>结束的<任务类型>任务的验收时间由<原有验收时间>变更为<新的验收时间>。点击<查看详情>。
	 * 
	 * 查看详情：查看任务详情
	 * 
	 * @param cjr 创建人的userid
	 * @param remark 原因
	 */
	private void sendUpdateYSSJMsg(String cjr, JRW rw, Date old_yssj, Date new_yssj, String remark) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append("由于").append(msgTemplateService.getRemark(remark)).append(", ");
		sb.append(msgTemplateService.getUser(rw.getAppId(), cjr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("将从");
		sb.append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
		sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
		RWLX rwlx = RWLX.valueOf(rw.getLx());
		sb.append(rwlx).append("任务的验收时间由");
		sb.append(old_yssj == null ? "未知" : DateUtils.getShortDate(old_yssj)).append("变更为");
		sb.append(DateUtils.getShortDate(new_yssj)).append("。点击");
		sb.append(msgTemplateService.getRWXQ(rw));
		
		// 给任务操作人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
		
		// 给任务验收人发消息
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getYsr(), sb.toString());
	}
	
	/**
	 * 后勤管理人员：延期（提前）验收一个尚未完成的任务。即修改任务验收日期。
	 * 
	 * 1. 更新任务的验收时间。
	 * 2. 向任务操作人，验收人发消息：sendUpdateYSSJMsg
	 * 
	 * @param rwId
	 * @param remark 更新原因
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateYSSJ(String cjr, Integer rwId, Date new_yssj, String remark) throws OperException {
		JRW rw = rwDao.selectByPrimaryKey(rwId);
		Date old_yssj = rw.getYssj();
		
		rw.setYssj(new_yssj);
		rwDao.updateByPrimaryKey(rw);
		sendUpdateYSSJMsg(cjr, rw, old_yssj, new_yssj, remark);
	}
	
	/**
	 * 当验收人员验收通过时，系统将向任务所在的"操作人"推送一条信息。信息格式为：
	 * 【系统通知】：<验收人>于<系统时间>完成<任务类型>的验收工作。点击<查看任务详情>。
	 * 
	 * 当验收人员验收通过时，系统将向任务中的资产列表的“使用人（保管人）”推送一条信息。信息格式为：
	 * 【系统通知】：<验收人>于<系统时间>完成<资产名称>(<资产编号>)的验收工作。点击<查看资产详情>。
	 * 
	 * 当验收人员验收不通过，系统将向任务所在的"操作人"推送一条信息。信息格式为：
	 * 【系统通知】：<验收人>于<系统时间>对<任务类型>进行验收，验收没有通过！点击<查看任务详情>。
	 * 
	 * 查看详情：查看任务详情
	 */
	private void sendFinishRWMsg(JRW rw, boolean ok) throws OperException {
		if (ok) {
			StringBuilder sb = new StringBuilder();
			sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getYsr())).append("于");
			sb.append(DateUtils.getLongDate(new Date())).append("完成");
			sb.append(RWLX.valueOf(rw.getLx()).name()).append("的验收工作。点击");
			sb.append(msgTemplateService.getRWXQ(rw));
			// 给任务操作人发消息
			msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
			
			List<JZC> zcList = zcDao.getZCList(rw.getId());
			for (JZC zc : zcList) {
				sb = new StringBuilder();
				sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getYsr())).append("于");
				sb.append(DateUtils.getLongDate(new Date())).append("完成");
				sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")").append("的验收工作。点击");
				sb.append(msgTemplateService.getZCXQ(zc));
				// 资产列表的“使用人（保管人）”推送一条信息
				msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, zc.getSyr(), sb.toString());
			}
		} else {
			// 任务所在的"操作人"推送一条信息
			StringBuilder sb = new StringBuilder();
			sb.append(msgTemplateService.getUser(rw.getAppId(), rw.getYsr())).append("于");
			sb.append(DateUtils.getLongDate(new Date())).append("对");
			sb.append(RWLX.valueOf(rw.getLx()).name()).append("进行验收，验收没有通过！点击");
			sb.append(msgTemplateService.getRWXQ(rw));
			// 给任务操作人发消息
			msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, rw.getCzr(), sb.toString());
		}
	}
	
	
	/**
	 * 任务验收人进行任务验收：包括对巡检、维修、购置等任务的验收工作。仅仅用于确认任务完成，不改变资产状态。
	 * 
	 * 1.	查询J_RW中验收人（YSR）为当前用户，FINISH为0，且（FINISH_COUNT与TOTAL相等）的任务，输出任务列表。每个任务有“通过”、“不通过”两个选择
	 * 2.	点击“通过”：修改J_RW表相应任务信息：
	 * 	验收备注：允许为空
	 * 	验收时间：当前系统时间。
	 * 	FINISH设置为1（完成）
	 * 	通知操作人，验收工作完成。sendFinishRWMsg(JRW rw,  true)
	 * 3.	点击“不通过”：
	 * 	通知操作人，验收工作不通过。sendFinishRWMsg(JRW rw,  false) 
	 * 
	 * @param ok 是否通过
	 * @param ysRemark 验收备注
	 * 
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void finishRW(Integer rwId, String ysRemark, boolean ok) throws OperException {
		JRW rw = rwService.getRW(rwId);
		
		if (ok) {
			// 验收通过
			rw.setYssj(new Date());
			rw.setYsRemark(ysRemark);
			rw.setFinish(1);
			rwService.updateRW(rw);
		}
		
		// 无论是否通过验收，都要验收人都要发送消息给任务操作人.
		sendFinishRWMsg(rw, ok);
	}
	
	
}
