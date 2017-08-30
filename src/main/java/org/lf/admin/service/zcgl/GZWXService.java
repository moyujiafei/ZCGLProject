package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.JRWXZ;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.DateUtils;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产管理-- 故障维修
 */
@Service("gzwxService")
public class GZWXService {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private VZCMapper vzcDao;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	/**
	 * 发送维修完毕消息。信息格式为：
	 * 
	 * 【系统通知】：<操作人>于<系统时间>将<资产名称>(<资产代码>)维修完毕。点击查看<资产详情>。
	 * 
	 * @param wxr：维修人userid
	 */
	private void sendFinishWXMsg(String wxr, JZC zc, String targetUserId) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(msgTemplateService.getUser(zc.getAppId(), wxr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("将");
		sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")维修完毕。点击");
		sb.append(msgTemplateService.getZCXQ(zc));
		
		// 向目标用户发送
		List<String> toList = new ArrayList<>();
		toList.add(targetUserId);
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, toList, sb.toString());
	}
	
	/**
	 * 维修员执行资产故障维修任务流程
	 * 
	 * 点击“修理完毕”：
	 * 	将J_ZC的状态置为：使用中
	 * 	在L_ZCZT中插入记录：
	 * 		记录人：当前用户
	 * 		原状态：维修中
	 * 		新状态：使用中
	 * 		通知使用人（保管人）：<操作人>于<系统时间>将<资产名称>维修完毕。点击<查看资产详情>。
	 * 
	 * 	更新J_RWXZ中相关资产的FINISH，置为1
	 * 	更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
	 * 	判断FINISH_COUNT与TOTAL是否相等，如果相等，向任务验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 
	 * @param appId
	 * @param zcId
	 * @param mediaList 素材media列表
	 * @param wxr 维修人员userid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void finishGZWX(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String wxr) throws OperException {
		// 将J_ZC的状态置为：使用中
		JZC zc = zcService.getZC(zcId);
		zc.setZt(ZCZT.使用中.getValue());
		zcService.updateZC(zc);
		
		// 在L_ZCZT中创建记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(wxr);
		zt.setOldZt(ZCZT.维修中.getValue());
		zt.setNewZt(ZCZT.使用中.getValue());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）完成维修");
		zt.setRemark(remarkSb.toString());
		// 附加状态细则列表
		if (mediaList != null) {
			List<LZTXZ> ztxzList = new ArrayList<>();
			LZTXZ ztxz;
			for (String mediaId : mediaList.keySet()) {
				ztxz = new LZTXZ();
				ztxz.setAppId(appId);
				ztxz.setMediaType(mediaList.get(mediaId).name());
				ztxz.setWxMediaId(mediaId);
				ztxzList.add(ztxz);
			}
			ztService.insertZT(zt, ztxzList);
		}
		
		// 通知使用人（保管人）：<操作人>于<系统时间>将<资产名称>维修完毕。
		sendFinishWXMsg(wxr, zc, zc.getSyr());
		
		//更新J_RWXZ中相关资产的FINISH，置为1
		JRWXZ rwxzParam = new JRWXZ();
		rwxzParam.setZcid(zcId);
		rwxzParam.setFinish(0);
		JRWXZ rwxz = rwxzDao.select(rwxzParam);
		rwxz.setFinish(1);
		rwxzDao.updateByPrimaryKeySelective(rwxz);
		
		// 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
		JRW rw = rwService.getRW(rwxz.getRwid());
		int finishCount = rw.getFinishCount();
		rw.setFinishCount(finishCount + 1);
		
		// FINISH_COUNT与TOTAL是否相等，如果相等，巡检员向验收人（YSR）和操作人（CZR)发消息
		if (rw.getTotal() == rw.getFinishCount()) {
			msgTemplateService.sendFinishRWMsg(wxr, rw);
			rw.setFinish(1);
		}
		
		rwService.updateRW(rw);
	}
	
	/**
	 * 维修员执行资产故障维修任务，维修因故无法完成。再次提交故障维修申请，要求管理人员另派人员来维修
	 * 
	 * 将J_ZC的状态置为：申请维修
	 * 在L_ZCZT中插入记录：
	 * 	记录人：当前用户
	 * 	原状态：维修中
	 * 	新状态：申请维修
	 * 	备注：申请原因
	 *   通知使用人（保管人）：sendCreateSQMsg(String sqr, JZC zc, ZCZT zt, String targetUserId, String sqyy)
	 *   
	 *   更新J_RWXZ中相关资产的FINISH，置为1
	 * 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
	 * 判断FINISH_COUNT与TOTAL是否相等，如果相等，向任务验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 
	 * @param appId
	 * @param zcId
	 * @param mediaList 素材media列表
	 * @param sqRemark 申请原因
	 * @param wxr 维修人员userid
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resubmitWXSQ(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String sqRemark, String wxr) throws OperException {
		// 将J_ZC的状态置为：申请维修
		JZC zc = zcService.getZC(zcId);
		zc.setZt(ZCZT.申请维修.getValue());
		zcService.updateZC(zc);

		// 在L_ZCZT中创建记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(wxr);
		zt.setOldZt(ZCZT.维修中.getValue());
		zt.setNewZt(ZCZT.申请维修.getValue());
		zt.setRemark(sqRemark);
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）申请重新维修");
		zt.setRemark(remarkSb.toString());
		// 附加状态细则列表
		List<LZTXZ> ztxzList = new ArrayList<>();
		LZTXZ ztxz;
		for (String mediaId : mediaList.keySet()) {
			ztxz = new LZTXZ();
			ztxz.setAppId(appId);
			ztxz.setMediaType(mediaList.get(mediaId).name());
			ztxz.setWxMediaId(mediaId);
			ztxzList.add(ztxz);
		}
		ztService.insertZT(zt, ztxzList);

		// 通知使用人（保管人）：sendCreateSQMsg
		msgTemplateService.sendCreateSQMsg(wxr, zc, ZCZT.申请维修, zc.getSyr(), sqRemark);
		
		// 更新J_RWXZ中相关资产的FINISH，置为1
		JRWXZ rwxzParam = new JRWXZ();
		rwxzParam.setZcid(zcId);
		rwxzParam.setFinish(0);
		JRWXZ rwxz = rwxzDao.select(rwxzParam);
		rwxz.setFinish(1);
		rwxzDao.updateByPrimaryKeySelective(rwxz);

		// 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
		JRW rw = rwService.getRW(rwxz.getRwid());
		int finishCount = rw.getFinishCount();
		rw.setFinishCount(finishCount + 1);

		// FINISH_COUNT与TOTAL是否相等，如果相等，巡检员向验收人（YSR）和操作人（CZR)发消息
		if (rw.getTotal() == rw.getFinishCount()) {
			msgTemplateService.sendFinishRWMsg(wxr, rw);
			rw.setFinish(1);
		}
		
		rwService.updateRW(rw);
	}
	
	/**
	 * 维修员执行资产故障维修任务，维修因故无法完成。维修人员提出闲置设备申请
	 *
	 * 将J_ZC的状态置为：申请闲置
	 * 	
	 * 在L_ZCZT中插入记录：
	 * 	记录人：当前用户
	 * 	原状态：维修中
	 * 	新状态：申请闲置
	 * 	备注：申请原因
	 * 	  通知使用人（保管人）：sendCreateSQMsg
	 * 
	 *   更新J_RWXZ中相关资产的FINISH，置为1
	 * 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
	 * 判断FINISH_COUNT与TOTAL是否相等，如果相等，向任务验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 
	 * @param appId
	 * @param zcId
	 * @param mediaList
	 * @param sqRemark 申请原因
	 * @param wxr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void submitXZSQ(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String sqRemark, String wxr) throws OperException {
		// 将J_ZC的状态置为：申请闲置
		JZC zc = zcService.getZC(zcId);
		zc.setZt(ZCZT.申请闲置.getValue());
		zcService.updateZC(zc);

		// 在L_ZCZT中创建记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(wxr);
		zt.setOldZt(ZCZT.维修中.getValue());
		zt.setNewZt(ZCZT.申请闲置.getValue());
		zt.setRemark(sqRemark);
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）申请闲置");
		zt.setRemark(remarkSb.toString());
		// 附加状态细则列表
		List<LZTXZ> ztxzList = new ArrayList<>();
		LZTXZ ztxz;
		for (String mediaId : mediaList.keySet()) {
			ztxz = new LZTXZ();
			ztxz.setAppId(appId);
			ztxz.setMediaType(mediaList.get(mediaId).name());
			ztxz.setWxMediaId(mediaId);
			ztxzList.add(ztxz);
		}
		ztService.insertZT(zt, ztxzList);

		// 通知使用人（保管人）：sendCreateSQMsg
		msgTemplateService.sendCreateSQMsg(wxr, zc, ZCZT.申请闲置, zc.getSyr(), sqRemark);
		
		// 更新J_RWXZ中相关资产的FINISH，置为1
		JRWXZ rwxzParam = new JRWXZ();
		rwxzParam.setZcid(zcId);
		rwxzParam.setFinish(0);
		JRWXZ rwxz = rwxzDao.select(rwxzParam);
		rwxz.setFinish(1);
		rwxzDao.updateByPrimaryKeySelective(rwxz);

		// 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
		JRW rw = rwService.getRW(rwxz.getRwid());
		int finishCount = rw.getFinishCount();
		rw.setFinishCount(finishCount + 1);

		// FINISH_COUNT与TOTAL是否相等，如果相等，巡检员向验收人（YSR）和操作人（CZR)发消息
		if (rw.getTotal() == rw.getFinishCount()) {
			msgTemplateService.sendFinishRWMsg(wxr, rw);
			rw.setFinish(1);
		}
		
		rwService.updateRW(rw);
	}
	
	/**
	 * 维修员执行资产故障维修任务，维修因故无法完成。维修人员提出报废设备申请
	 *
	 * 将J_ZC的状态置为：申请报废
	 * 	
	 * 在L_ZCZT中插入记录：
	 * 	记录人：当前用户
	 * 	原状态：维修中
	 * 	新状态：申请报废
	 * 	备注：申请原因
	 * 	  通知使用人（保管人）：sendCreateSQMsg
	 * 
	 *   更新J_RWXZ中相关资产的FINISH，置为1
	 * 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
	 * 判断FINISH_COUNT与TOTAL是否相等，如果相等，向任务验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 
	 * @param appId
	 * @param zcId
	 * @param mediaList
	 * @param sqRemark 申请原因
	 * @param wxr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void submitBFSQ(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String sqRemark, String wxr) throws OperException {
		// 将J_ZC的状态置为：申请报废
		JZC zc = zcService.getZC(zcId);
		zc.setZt(ZCZT.申请报废.getValue());
		zcService.updateZC(zc);

		// 在L_ZCZT中创建记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(wxr);
		zt.setOldZt(ZCZT.维修中.getValue());
		zt.setNewZt(ZCZT.申请报废.getValue());
		zt.setRemark(sqRemark);
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）申请报废");
		zt.setRemark(remarkSb.toString());
		// 附加状态细则列表
		List<LZTXZ> ztxzList = new ArrayList<>();
		LZTXZ ztxz;
		for (String mediaId : mediaList.keySet()) {
			ztxz = new LZTXZ();
			ztxz.setAppId(appId);
			ztxz.setMediaType(mediaList.get(mediaId).name());
			ztxz.setWxMediaId(mediaId);
			ztxzList.add(ztxz);
		}
		ztService.insertZT(zt, ztxzList);

		// 通知使用人（保管人）：sendCreateSQMsg
		msgTemplateService.sendCreateSQMsg(wxr, zc, ZCZT.申请报废, zc.getSyr(), sqRemark);
		
		// 更新J_RWXZ中相关资产的FINISH，置为1
		JRWXZ rwxzParam = new JRWXZ();
		rwxzParam.setZcid(zcId);
		rwxzParam.setFinish(0);
		JRWXZ rwxz = rwxzDao.select(rwxzParam);
		rwxz.setFinish(1);
		rwxzDao.updateByPrimaryKeySelective(rwxz);

		// 更新J_RW中的已完成细则数（FINISH_COUNT），数量加1。
		JRW rw = rwService.getRW(rwxz.getRwid());
		int finishCount = rw.getFinishCount();
		rw.setFinishCount(finishCount + 1);

		// FINISH_COUNT与TOTAL是否相等，如果相等，巡检员向验收人（YSR）和操作人（CZR)发消息
		if (rw.getTotal() == rw.getFinishCount()) {
			msgTemplateService.sendFinishRWMsg(wxr, rw);
			rw.setFinish(1);
		}
		
		rwService.updateRW(rw);
	}
}
