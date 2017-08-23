package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统预警
 * 
 * @author 彭潇
 */
@Service("xtyjService")
public class XTYJService {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
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
	 * 系统发送任务即将到期预警信息。
	 * 遍历J_RW中的数据，对FINISH为0，结束时间 - 当前系统时间 < RW_EXPIRE_DATE的任务，向验收人和操作人发送消息：
	 * 
select *
from j_rw
where app_id = 17 and finish = 0 
  and datediff(jssj, now()) between 0 and (select env_value from chu_env where env_key = 'RW_EXPIRE_DATE'); 
	 * 
	 * 【系统预警】：一个从<开始时间>开始到<结束时间>结束的<任务类型>任务，任务既定验收时间为<验收时间>。该任务即将到期，点击<查看任务详情>。
	 * 
	 * @throws OperException
	 */
	public void sendExpiringMsg(Integer appId) throws OperException {
		List<JRW> rwList = rwService.getExpiringRWList(appId);
		
		StringBuilder sb;
		for (JRW rw : rwList) {
			sb = new StringBuilder();
			sb.append("一个从").append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
			sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
			sb.append(RWLX.valueOf(rw.getLx()).name()).append("任务，任务既定验收时间为");
			sb.append(DateUtils.getShortDate(rw.getYssj())).append("。该任务即将到期，点击");
			sb.append(msgTemplateService.getRWXQ(rw));
			
			// 验收人和操作人发送消息
			List<String> jsrList = new ArrayList<>();
			jsrList.add(rw.getYsr());
			jsrList.add(rw.getCzr());
			msgService.sendUserMsg(appId, MsgLX.系统预警, jsrList, sb.toString());
		}
	}
	
	/**
	 * 系统发送任务已经过期预警信息。
	 * 遍历J_RW中的数据，对FINISH为0，(结束时间 < 当前系统时间) 的任务，
	 * 
	 * select * from j_rw where app_id = ? and finish = 0 and jssj < now() 
	 * 
	 * 向验收人和操作人发送消息：
	 * 
	 * 【系统预警】：一个从<开始时间>开始到<结束时间>结束的<任务类型>任务，任务既定验收时间为<验收时间>。该任务已经过期，点击<查看任务详情>。
	 * 
	 * @throws OperException
	 */
	public void sendExpiredMsg(Integer appId) throws OperException {
		List<JRW> rwList = rwService.getExpiredRWList(appId);
		
		StringBuilder sb;
		for (JRW rw : rwList) {
			sb = new StringBuilder();
			sb.append("一个从").append(DateUtils.getShortDate(rw.getKssj())).append("开始到");
			sb.append(DateUtils.getShortDate(rw.getJssj())).append("结束的");
			sb.append(RWLX.valueOf(rw.getLx()).name()).append("任务，任务既定验收时间为");
			sb.append(DateUtils.getShortDate(rw.getYssj())).append("。该任务已经过期，点击");
			sb.append(msgTemplateService.getRWXQ(rw));
			
			// 验收人和操作人发送消息
			List<String> jsrList = new ArrayList<>();
			jsrList.add(rw.getYsr());
			jsrList.add(rw.getCzr());
			msgService.sendUserMsg(appId, MsgLX.系统预警, jsrList, sb.toString());
		}
	}
	
	/**
	 * 发送资产折旧到期预警
	 * 
	 * 
	 * 遍历所有V_ZC中的数据，对于：当前系统时间 - 购置时间（gzsj）> 折旧年限（ZJNX）的资产，向资产管理员和资产使用人（保管人）发送消息：
	 * 
	 * select * from v_zc where app_id = ? and datediff(now(), gzsj) > zjnx * 365 ;
	 * 
	 * 【系统预警】：您管理的<资产名称>(<资产代码>)购置时间为<购置时间>，现已经超过的折旧年限（<折旧年限>年），请尽快提交维修（报废、闲置）申请。点击<查看资产详情>。
	 * 
	 * @throws OperException
	 */
	public void sendDeprecatedZCMsg(Integer appId) throws OperException {
		List<VZC> zcList = zcService.getDeprecatedZCList(appId);
		
		StringBuilder sb;
		for (VZC zc : zcList) {
			sb = new StringBuilder();
			sb.append("您管理的").append(zc.getZc()).append("(");
			sb.append(zc.getZcdm()).append(")购置时间为");
			sb.append(DateUtils.getShortDate(zc.getGzsj())).append(",现已经超过的折旧年限(");
			sb.append(zc.getZjnx().toString()).append("年),请尽快提交维修（报废、闲置）申请。点击");
			sb.append(msgTemplateService.getVZCXQ(zc));
			
			// 向资产管理员和资产使用人（保管人）发送消息
			List<String> jsrList = new ArrayList<>();
			jsrList.add(zc.getGlr());
			jsrList.add(zc.getSyr());
			msgService.sendUserMsg(appId, MsgLX.系统预警, jsrList, sb.toString());
		}
	}
}
