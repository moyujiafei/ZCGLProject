package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.admin.service.sys.EnvService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.utils.DateUtils;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产分配：由部门资产管理员操作。
 * 
 * @author 文臣
 */
@Service("zcfpService")
public class ZCFPService {
	@Autowired
	private EnvService envService;
	
	@Autowired
	private WXUserService wxUserService;
	
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private JZCMapper jzcDao;
	
	@Autowired
	private VZCMapper vzcDao;
	
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	/**
	 * 申请资产归还后，向后勤管理人员发送信息：
	 * 【系统通知】：<操作人>于<系统时间>将<资产名称>(<资产编号>)由于<归还原因>申请归还。点击<查看资产详情>
	 * 
	 * 注意：这里使用 sendTagMsg()函数给标记为后勤管理员的用户群发消息。tagNo号来自EnvService。
	 * @see MsgService#sendTagMsg 
	 * 
	 * @throws OperException
	 */
	private void sendRevertZCMsg(JZC zc, String czr, String remark) throws OperException {

		//向后勤管理人员发送信息
		int tagNo = Integer.parseInt(envService.getEnvValue(EnvService.TAG_HQ_ADMIN)); 
		StringBuilder resultSb = new StringBuilder();
		resultSb.append("【系统通知】：").append(msgTemplateService.getUser(zc.getAppId(), czr));
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("将").append(zc.getMc()).append("（").append(zc.getDm()).append("）");
		resultSb.append("由于").append(msgTemplateService.getRemark(remark)).append("申请归还。").append("点击").append(msgTemplateService.getZCXQ(zc));
		
		msgService.sendTagMsg(zc.getAppId(), MsgLX.系统通知, tagNo, resultSb.toString());
	}
	
	/**
	 * 对于未使用的资产（尚未分配给个人使用），部门资产管理员可以申请将它归还给后勤管理员。
	 * 
	 * 1. 更新J_ZC中的资产状态变为“归还中”
	 * 2. 向L_ZT中插入一条记录：
	 * 	          记录人：部门资产管理员userid
	 * 	原状态：未使用
	 * 	新状态：归还中
	 *      备注：<资产名称>(<资产编号>)由于<归还原因>申请归还.
	 * 3. 发消息给后勤管理员：
	 * @see #sendRevertZCMsg
	 * 
	 * @param zcId
	 * @param czr 部门资产管理员userid
	 * @param remark 申请归还原因。 注意：如果remark为空，备注部分“由于<归还原因>”不记录。
	 * @throws OperException
	 */
	public void revertZC(Integer zcId, String czr, String remark) throws OperException {
		
		//更新J_ZC中的资产状态变为“归还中”
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZt(ZCZT.归还中.getValue());
		jzcDao.updateByPrimaryKeySelective(zc);
		
		//向L_ZT中插入一条记录
		LZT zt = new LZT();
		zt.setJlr(czr);
		zt.setOldZt(ZCZT.未使用.getValue());
		zt.setNewZt(ZCZT.归还中.getValue());
		StringBuilder remarkSb = new StringBuilder();
		JZC preZc = zcService.getZC(zcId);
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		remarkSb.append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		//如果remark为空，备注部分“由于<归还原因>”不记录
//		if (!StringUtils.isEmpty(remark)) {
			remarkSb.append("由于").append(msgTemplateService.getRemark(remark));
//		}
		remarkSb.append("申请归还");
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);
		
		//发消息给后勤管理员
		sendRevertZCMsg(preZc, czr, remark);
	}

	
	/**
	 * 资产领用后，向资产使用人（保管人）发送信息：
	 * 【系统通知】：<操作人>于<系统时间>将<资产名称>(<资产编号>)分配<使用人>使用。点击<查看资产详情>
	 * 
	 * @throws OperException
	 */
	private void sendAssignZCMsg(JZC zc, String czr, String syr) throws OperException {
		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(msgTemplateService.getUser(zc.getAppId(), czr));
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("将").append(zc.getMc()).append("（").append(zc.getDm()).append("）");
		resultSb.append("分配").append(msgTemplateService.getUser(zc.getAppId(), syr)).append("使用");
		
		//向资产使用人（保管人）发送信息
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, syr, resultSb.toString());
	}
	
	/**
	 * 资产分配。资产管理员将已调拨的资产分配给使用人（保管人）。这里，使用人（保管人）可以是本部门的某个人；
	 * 也可以是单位所有。如果是后者，使用人（保管人）设为本部门的资产管理员。
	 * 
	 * 1. 更新J_ZC中的使用人（保管人）
	 * 2. 中L_ZT中插入一条记录：
	 * 	          记录人：操作人userid
	 * 	原状态：未使用
	 * 	新状态：领用中
	 *      备注：<资产名称>(<资产编号>)分配<使用人>使用.
	 * 3. 发消息给使用人（保管人）：
	 * @see #sendAssignZCMsg
	 * 
	 * @param czr 部门资产管理员userid
	 * @param zcId
	 * @param syr 新的资产使用人userid
	 * @throws OperException
	 */
	public void assignZC(Integer zcId, String czr, String syr) throws OperException {
		
		//更新J_ZC中的使用人（保管人）
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setSyr(syr);
		zc.setZt(ZCZT.领用中.getValue());
		jzcDao.updateByPrimaryKeySelective(zc);
		zc=jzcDao.selectByPrimaryKey(zcId);
		
		//获得用户信息
		ChuWXUser param = new ChuWXUser();
		param.setAppId(zc.getAppId());
		param.setUserid(syr);
		
		//中L_ZT中插入一条记录
		JZC preZc = zcService.getZC(zcId);
		LZT zt = new LZT();
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(czr);
		zt.setOldZt(ZCZT.未使用.getValue());
		zt.setNewZt(ZCZT.领用中.getValue());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		remarkSb.append("分配给").append(wxUserService.getWXUser(param).getName()).append("使用");
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);	
		
		//发消息给使用人（保管人）
		sendAssignZCMsg(preZc, czr, syr);
	}
	
	/**
	 * 资产重新分配
	 * 对已经将资产分配给用户（状态为领用中），但用户尚未使用的资产，重新分配给其它使用人。
	 * 
	 * 1. 更新J_ZC中的使用人（保管人）
	 * 2. 在L_ZT中插入一条记录：
	 * 	          记录人：操作人userid
	 * 	原状态：领用中
	 * 	新状态：领用中
	 *      备注：<资产名称>(<资产编号>)由于<原因备注>重新分配给<使用人>使用.
	 * 3. 发消息给使用人（保管人）：
	 * @see #sendAssignZCMsg
	 * 
	 * @param czr 部门资产管理员userid
	 * @param zcId
	 * @param syr 资产使用人（保管人）userid
	 * @param remark 重新分配使用人原因备注
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void reassignZC(Integer zcId, String czr, String syr, String remark) throws OperException {
		
		//更新J_ZC中的使用人（保管人）
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setSyr(syr);
		zc.setZt(ZCZT.领用中.getValue());
		jzcDao.updateByPrimaryKeySelective(zc);
		zc=jzcDao.selectByPrimaryKey(zcId);
		
		//获得用户信息
		ChuWXUser param = new ChuWXUser();
		param.setAppId(zc.getAppId());
		param.setUserid(syr);
		
		//在L_ZT中插入一条记录
		JZC preZc = zcService.getZC(zcId);
		LZT zt = new LZT();
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(czr);
		zt.setOldZt(ZCZT.领用中.getValue());
		zt.setNewZt(ZCZT.领用中.getValue());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		remarkSb.append("由于").append(msgTemplateService.getRemark(remark)).append("重新分配给").append(wxUserService.getWXUser(param).getName()).append("使用");
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);
		
		//发消息给使用人（保管人）
		sendAssignZCMsg(preZc, czr, syr);
	}
	
	/**
	 * 部门资产管理员：同意上交资产申请。上交资产，即将资产重新上交给部门资产管理员。
	 * 
	 * 1. 部门资产管理员查询J_ZC中处于“上交中”状态的资产列表。选中一个或多个资产，有两个操作：“拒绝”、“同意”
	 * 2.	选择“同意”：
	 * 	将选中资产状态置为“未使用”，将资产使用人（保管人）置为空。在L_ZT中添加记录。
	 * 	记录人：创建人（cjr），部门资产管理员userid
	 * 	原状态：上交中
	 * 	新状态：未使用
	 *      备注：同意上交<资产名称>(<资产编号>)
	 * 3.   通知原有设备使用人（保管人）：sendAgreeSQMsg
	 * 
	 * @see MsgTemplateService#sendAgreeSQMsg(String, JZC, ZCZT, String)
	 * 
	 * @param appId
	 * @param cjr
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeSendbackSQ(Integer appId, String cjr, List<Integer> zcidList) throws OperException {
		
		JZC zc ;
		LZT zt;
		StringBuilder remarkSb;
		for (Integer zcid : zcidList) {
			//将选中资产状态置为“未使用”，将资产使用人（保管人）置为空。
			JZC beforeZc = zcService.getZC(zcid);
			zc = new JZC();
			zc.setId(zcid);
			zc.setZt(ZCZT.未使用.getValue());
			zc.setSyr("");
			jzcDao.updateByPrimaryKeySelective(zc);
			
			//在L_ZCZT中添加记录
			JZC preZc = zcService.getZC(zcid);
			zt = new LZT();
			zt.setAppId(preZc.getAppId());
			zt.setZcdm(preZc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.上交中.getValue());
			zt.setNewZt(ZCZT.未使用.getValue());
			remarkSb = new StringBuilder();
			remarkSb.append("同意上交").append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
			zt.setRemark(remarkSb.toString());
			ztService.insertZT(zt);
			
			//通知原有设备使用人（保管人）
			msgTemplateService.sendAgreeSQMsg(cjr, preZc, ZCZT.未使用, beforeZc.getSyr());
		}
		
	}
	
	/**
	 * 部门资产管理人员，拒绝使用人（保管人）提交的上交设备的申请。
	 * 
	 * 1.	后勤管理人员查询J_ZC中处于“申请维修”状态的资产列表。选中一个或多个资产，有两个操作：“拒绝”、“同意”
	 * 2.	选择“拒绝”：
	 * 	添加“拒绝原因”
	 * 	将选中资产状态置为“使用中”。在L_ZCZT中添加记录。
	 * 	记录人：创建人（cjr），部门资产管理员userid
	 * 	原状态：上交中
	 * 	新状态：使用中
	 * 	备注：由于<拒绝原因>拒绝您上交<资产名称>(<资产编号>)的申请。
	 *  3.  通知使用人（保管人）：sendRefuseSQMsg(String spr, JZC zc, ZCZT zt, String targetUserId)
	 * 	Spr：审批人，部门资产管理员userid
	 * 
	 * @see MsgTemplateService#sendRefuseSQMsg(String, JZC, ZCZT, String) 
	 * 
	 * @param appId
	 * @param refuseRemark 拒绝理由
	 * @param cjr 创建人，部门资产管理员userid
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseSendbackSQ(Integer appId, String refuseRemark, String cjr, List<Integer> zcidList) throws OperException {
		
		JZC zc;
		LZT zt;
		StringBuilder remarkSb ;
		for (Integer zcid : zcidList) {
			//将选中资产状态置为“使用中”
			zc = new JZC();
			zc.setId(zcid);
			zc.setZt(ZCZT.使用中.getValue());
			jzcDao.updateByPrimaryKeySelective(zc);
			
			//在L_ZCZT中添加记录
			JZC preZc = zcService.getZC(zcid);
			zt = new LZT();
			zt.setId(null);
			zt.setAppId(preZc.getAppId());
			zt.setZcdm(preZc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.上交中.getValue());
			zt.setNewZt(ZCZT.使用中.getValue());
			remarkSb = new StringBuilder();
			remarkSb.append("由于").append(msgTemplateService.getRemark(refuseRemark)).append("拒绝您上交").append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
			zt.setRemark(remarkSb.toString());
			ztService.insertZT(zt);
			
			//通知原有设备使用人（保管人）
			msgTemplateService.sendRefuseSQMsg(cjr, preZc, ZCZT.使用中, preZc.getSyr());
		}
	}
	
	/**
	 * 获取当前资产管理员名下的资产折旧即将到期资产列表，用以批量提交申请维修（报废、闲置）
	 * 
	 * 查找当前资产管理员名下的J_ZC中那些：当前系统时间 - 购置时间（gzsj）< 折旧年限（ZJNX）+ ZC_EXPIRE_DATE的资产，
	 * 
	 * select *
from v_zc  
where datediff(curdate(), gzsj) > zjnx * 365 - CONVERT((select env_value from chu_env where env_key = 'ZC_EXPIRE_DATE'), SIGNED)
  and app_id = 17 and glr = 'XuQingWei';
	 * 
	 * 注意：ZC_EXPIRE_DATE为环境参数，单位为天
	 * 
	 * @throws OperException
	 */
	public List<VZC> getDeprecatingZCList(Integer appId, String glr) throws OperException {
		VZC zc = new VZC();
		zc.setAppId(appId);
		zc.setGlr(glr);
		return vzcDao.getDeprecatingZCList(zc);
	}
	
	/**
	 * 资产管理员：提交维修、报废、闲置申请
	 * 
	 * 1.	将选中的资产状态改为申请维修（申请报废、申请闲置）
	 * 2.	在L_ZCZT中创建记录，
	 * 	记录人：当前用户
	 * 	原状态：使用中
	 * 	新状态：申请维修（申请报废、申请闲置）
	 * 3.	通知资产使用人（保管人）：sendCreateSQMsg(String sqr, JZC zc, ZCZT zt, String targetUserId)
	 * 
	 * @param sqr 申请人，即资产管理员的userid
	 * @param zc 资产
	 * @param zcztId 申请维修（申请报废、申请闲置）
	 * @param mediaList 提交状态改变时，用户附加的微信素材。key为mediaId，value为素材类型。
	 * @param sqRemark 申请原因
	 * @throws OperException
	 */
	public void submitSQ(String sqr, Integer zcId, Integer zcztId, Map<String, MediaType> mediaList, String sqRemark) throws OperException {
		// 将选中的资产状态改为申请维修（申请报废、申请闲置）
		JZC zc = zcService.getZC(zcId);
		zc.setZt(zcztId);
		zcService.updateZC(zc);
		
		// 在L_ZCZT中创建记录
		Integer appId = zc.getAppId();
		LZT zt = new LZT();
		zt.setAppId(appId);
		zt.setZcdm(zc.getDm());
		zt.setJlsj(new Date());
		zt.setJlr(sqr);
		zt.setOldZt(ZCZT.使用中.getValue());
		zt.setNewZt(zcztId);
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
		
		// 通知资产使用人（保管人）
		String targetUserId = zc.getSyr();
		ZCZT zczt = ZCZT.valueOf(zcztId);
		msgTemplateService.sendCreateSQMsg(sqr, zc, zczt, targetUserId, sqRemark);
	}
	
	/**
	 * 资产管理员：批量提交维修、报废、闲置申请
	 * 
	 * 允许选择一批资产，批量提交申请。
	 * 
	 * @param sqr
	 * @param zcId
	 * @param zcztId
	 * @param mediaList
	 * @param sqRemark
	 * @throws OperException
	 */
	public void submitSQ(String sqr, List<Integer> zcidList, Integer zcztId, Map<String, MediaType> mediaList, String sqRemark) throws OperException {
		for (Integer zcid : zcidList) {
			submitSQ(sqr, zcid, zcztId, mediaList, sqRemark);
		}
	}
	
}
