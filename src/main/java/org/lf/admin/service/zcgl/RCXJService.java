package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
 * 资产管理-- 日常巡检
 */
@Service("rcxjService")
public class RCXJService {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
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
	 * 【系统通知】：<操作人>于<系统时间>将<资产名称>(<资产代码>)巡检完毕。点击<查看资产详情>。
	 * 
	 * @param wxr：维修人userid
	 */
	private void sendFinishXJMsg(String wxr, JZC zc, String targetUserId) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(msgTemplateService.getUser(zc.getAppId(), wxr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("将");
		sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")巡检完毕。点击");
		sb.append(msgTemplateService.getZCXQ(zc));
		
		// 向目标用户发送
		List<String> toList = new ArrayList<>();
		toList.add(targetUserId);
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, toList, sb.toString());
	}
	
	/**
	 * 巡检员执行资产日常巡检任务流程
	 * 
	 * 由巡检员对所负责的资产进行检查，确定其是否要进入到维修、报废流程中。
	 * =================================================
	 * 1.	查找J_RW表，操作人为当前用户，FINISH为0，任务类型为“日常巡检”的任务。
	 * 2.	点击其中一个任务，进入到任务细则页面。
	 * 3.	每一个任务细则，为一条资产信息。用户有三个选择：“状态正常”、“申请维修”、“申请报废”，“申请闲置”。
	 * 4.	选择“状态正常”：
	 * 	将任务细则（J_RWXZ）的FINISH置为1。
	 * 	将J_ZC的状态置为“使用中”
	 * 	更新J_RW表相关巡检任务的已完成细则数（FINISH_COUNT）增加1。
	 * 	判断FINISH_COUNT与TOTAL是否相等，如果相等，向验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 	在L_ZCZT中创建记录，原状态：“巡检中”，新状态为“使用中”
	 * 5.	选择“申请维修”：
	 * 	将任务细则（J_RWXZ）的FINISH置为1。
	 * 	将J_ZC的状态置为“维修申请”
	 * 	更新J_RW表相关巡检任务的已完成细则数（FINISH_COUNT）增加1。
	 * 	判断FINISH_COUNT与TOTAL是否相等，如果相等，向验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 	在L_ZCZT中创建记录，原状态：“巡检中”，新状态为“维修申请”
	 * 6.	选择“申请报废”：
	 * 	将任务细则（J_RWXZ）的FINISH置为1。
	 * 	将J_ZC的状态置为“报废申请”
	 * 	更新J_RW表相关巡检任务的已完成细则数（FINISH_COUNT）增加1。
	 * 	判断FINISH_COUNT与TOTAL是否相等，如果相等，向验收人（YSR）发消息。sendFinishRWMsg(JRW rw)
	 * 	在L_ZCZT中创建记录，原状态：“巡检中”，新状态为“报废申请”
	 * 
	 * @param xjr 巡检员的userid 
	 * @param zczt 使用中，申请维修、申请报废
	 * @param remark 巡检备注信息。
	 * @param mediaList 提交状态改变时，用户附加的微信素材。key为mediaId，value为素材类型
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void finishRCXJ(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String xjr) throws OperException {
		// 将J_ZC的状态置为：使用中
		JZC zc = zcService.getZC(zcId);
		zc.setZt(ZCZT.使用中.getValue());
		zcService.updateZC(zc);
		
		// 在L_ZCZT中创建记录
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(xjr);
		zt.setOldZt(ZCZT.巡检中.getValue());
		zt.setNewZt(ZCZT.使用中.getValue());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）状态正常");
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
		
		// 通知使用人（保管人）：<操作人>于<系统时间>将<资产名称>巡检完毕。
		sendFinishXJMsg(xjr, zc, zc.getSyr());

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
			msgTemplateService.sendFinishRWMsg(xjr, rw);
			// 将任务状态修改为结束
			rw.setFinish(1);
		}
		rwService.updateRW(rw);
		
	}

	
	/**
	 * 巡检人员提交故障维修申请，要求管理人员另派人员来维修
	 * 
	 * 将J_ZC的状态置为：申请维修
	 * 在L_ZCZT中插入记录：
	 * 	记录人：巡检员
	 * 	原状态：巡检中
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
	public void submitWXSQ(Integer appId, Integer zcId, Map<String, MediaType> mediaList, String sqRemark, String wxr) throws OperException {
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
		zt.setOldZt(ZCZT.巡检中.getValue());
		zt.setNewZt(ZCZT.申请维修.getValue());
		zt.setRemark(sqRemark);
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）申请维修");
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
			// 将任务状态修改为结束
			rw.setFinish(1);
		}
		
		rwService.updateRW(rw);
	}
	
	/**
	 * 巡检人员提出闲置设备申请
	 *
	 * 将J_ZC的状态置为：申请闲置
	 * 	
	 * 在L_ZCZT中插入记录：
	 * 	记录人：当前用户
	 * 	原状态：巡检中
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
		zt.setOldZt(ZCZT.巡检中.getValue());
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
	 * 	记录人：巡检人员
	 * 	原状态：巡检中
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
		zt.setOldZt(ZCZT.巡检中.getValue());
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
