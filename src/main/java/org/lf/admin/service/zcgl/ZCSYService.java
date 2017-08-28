package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.utils.DateUtils;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产使用
 * 
 * 设备使用人（保管人）相关操作
 */
@Service("zcsyService")
public class ZCSYService {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
	@Autowired
	private WXUserService wxUserService;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private VZCMapper vzcDao;
	
	@Autowired
	private JZCMapper jzcDao;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	@Autowired
	private MsgService msgService;
	
	/**
	 * 资产领用后，向资产使用人（保管人）发送信息：
	 * 【系统通知】：<使用人>由于<上交原因>于<系统时间>申请上交<资产名称>(<资产编号>)。点击<查看资产详情>
	 * 
	 * @throws OperException
	 */
	private void sendSendbackZCMsg(JZC zc, String remark) throws OperException {
		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(msgTemplateService.getUser(zc.getAppId(), zc.getSyr())).append("由于").append(msgTemplateService.getRemark(remark));
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("申请上交").append(zc.getMc()).append("（").append(zc.getDm()).append("）。");
		resultSb.append("点击").append(msgTemplateService.getZCXQ(zc));
		
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, zc.getSyr(), resultSb.toString());
	}
	
	/**
	 * 资产使用人（保管人）将自己使用的资产上交给部门资产管理员。
	 * 1. 资产状态更新为：上交中
	 * 2. 在L_ZT中插入一条记录：
	 * 	          记录人：使用人userid
	 * 	原状态：使用中
	 * 	新状态：上交中
	 *      备注：<资产名称>(<资产编号>)由于<上交原因>申请上交给<资产管理员>.   如果remark为空，由于<上交原因>不记录，
	 * 3. 发消息给资产管理员：
	 * @see #sendSendbackZCMsg
	 * 
	 * @param zcId
	 * @param czr
	 * @param syr
	 * @param remark
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendbackZC(Integer zcId, String remark) throws OperException {
		
		//资产状态更新为：上交中
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZt(ZCZT.上交中.getValue());
		jzcDao.updateByPrimaryKeySelective(zc);
		//给zc获取appId;
		zc=jzcDao.selectByPrimaryKey(zcId);
		//获得用户信息
		VZC vzc = vzcDao.selectByZcid(zcId);

		//在L_ZT中插入一条记录
		LZT zt = new LZT();
		zt.setOldZt(ZCZT.使用中.getValue());
		zt.setNewZt(ZCZT.上交中.getValue());
		JZC preZc = zcService.getZC(zcId);
		zt.setJlr(preZc.getSyr());
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		remarkSb.append("由于").append(msgTemplateService.getRemark(remark));
		remarkSb.append("申请上交给").append(msgTemplateService.getUser(zc.getAppId(), vzc.getGlr()));
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);
		
		//发消息给资产管理员
		sendSendbackZCMsg(preZc, remark);
	}
	
	
	/**
	 * 资产领用后，向资产使用人（保管人）发送信息：
	 * 【系统通知】：<使用人>于<系统时间>开始使用<资产名称>(<资产编号>)。点击<查看资产详情>
	 * 
	 * @throws OperException
	 */
	private void sendAgreeLeadingZCMsg(JZC zc, String syr) throws OperException {
		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(msgTemplateService.getUser(zc.getAppId(), syr)).append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("开始使用").append(zc.getMc()).append("（").append(zc.getDm()).append("）。");
		resultSb.append("点击").append(msgTemplateService.getZCXQ(zc));
		
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, syr, resultSb.toString());
	}
	
	/**
	 * 领用资产。设备使用人（保管人）同意领用设备管理员分配给的“分配中”的资产。
	 * 
	 * 1. 更新资产状态为：使用中
	 * 2. 在L_ZT中插入一条记录：
	 * 	          记录人：使用人userid
	 * 	原状态：领用中
	 * 	新状态：使用中
	 *      备注：<资产名称>(<资产编号>)分配给<使用人>使用.
	 * 3. 发消息给使用人（保管人）：
	 * @see #sendAgreeLeadingZCMsg
	 * 
	 * @param zcId
	 * @param syr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeLeadingZC(Integer zcId, String syr) throws OperException {
		
		//更新资产状态为：使用中
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZt(ZCZT.使用中.getValue());
		jzcDao.updateByPrimaryKeySelective(zc);
		zc=jzcDao.selectByPrimaryKey(zcId);
		
		//在L_ZT中插入一条记录
		LZT zt = new LZT();
		zt.setOldZt(ZCZT.领用中.getValue());
		zt.setNewZt(ZCZT.使用中.getValue());
		JZC preZc = zcService.getZC(zcId);
		zt.setJlr(preZc.getSyr());
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		remarkSb.append("分配给").append(msgTemplateService.getUser(zc.getAppId(), syr)).append("使用");
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);
		
		//发消息给使用人（保管人）
		sendAgreeLeadingZCMsg(preZc, syr);
	}
	
	/**
	 * 拒绝领用后，向资产管理员发送信息：
	 * 【系统通知】：<使用人>由于<拒绝原因>于<系统时间>拒绝使用<资产名称>(<资产编号>)。
	 * 
	 * @throws OperException
	 */
	private void sendRefuseLeadingZCMsg(JZC zc, String syr, String refuseRemark) throws OperException {
		
		StringBuilder resultSb = new StringBuilder();
		resultSb.append(msgTemplateService.getUser(zc.getAppId(), syr)).append("由于").append(msgTemplateService.getRemark(refuseRemark));
		resultSb.append("于").append(DateUtils.getLongDate(new Date()));
		resultSb.append("拒绝使用").append(zc.getMc()).append(zc.getDm());
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, syr, resultSb.toString());
	}
	
	/**
	 * 领用资产。设备使用人（保管人）拒绝使用设备管理员分配给的“分配中”的资产。
	 * 
	 * 如果ok为false，设备使用人（保管人）拒绝领用该资产。执行以下操作：
	 * 1. 更新资产状态为：未使用，syr更新为空
	 * 2. 在L_ZT中插入一条记录：
	 * 	          记录人：使用人userid
	 * 	原状态：领用中
	 * 	新状态：使用中
	 *      备注：拒绝使用<资产名称>(<资产编号>)。
	 * 
	 * 3. 发消息给部门资产管理员：
	 * @see #sendRefuseLeadingZCMsg
	 * 
	 * @param zcId
	 * @param syr
	 * @param refuseRemark 拒绝原因
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseLeadingZC(Integer zcId, String syr, String refuseRemark) throws OperException {
		
		//在L_ZT中插入一条记录
		LZT zt = new LZT();
		zt.setOldZt(ZCZT.领用中.getValue());
		zt.setNewZt(ZCZT.未使用.getValue());
		JZC preZc = zcService.getZC(zcId);
		zt.setJlr(preZc.getSyr());
		zt.setAppId(preZc.getAppId());
		zt.setZcdm(preZc.getDm());
		zt.setJlsj(new Date());
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append("拒绝使用").append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
		zt.setRemark(remarkSb.toString());
		ztService.insertZT(zt);
		
		//更新资产状态为：未使用，syr更新为空
		JZC zc = new JZC();
		zc.setId(zcId);
		zc.setZt(ZCZT.未使用.getValue());
		zc.setSyr("");
		jzcDao.updateByPrimaryKeySelective(zc);
		
		//发消息给部门资产管理员
		sendRefuseLeadingZCMsg(preZc, syr, refuseRemark);
	}
	
	/**
	 * 资产使用人（保管人）：提交维修、报废、闲置申请
	 * 
	 * 1.	将选中的资产状态改为申请维修（申请报废、申请闲置）
	 * 2.	在L_ZCZT中创建记录，
	 * 	记录人：当前用户
	 * 	原状态：使用中
	 * 	新状态：申请维修（申请报废、申请闲置）
	 * 3.	通知资产使用人（保管人）：sendCreateSQMsg(String sqr, JZC zc, ZCZT zt, String targetUserId)
	 * 
	 * @param sqr 申请人，即使用人（保管人）的userid
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
		StringBuilder remarkSb = new StringBuilder();
		remarkSb.append(zc.getMc()).append("（").append(zc.getDm()).append("）于").append(DateUtils.getLongDate(new Date()));
		if(zcztId==4){
			remarkSb.append("申请维修");
		}else if(zcztId==6){
			remarkSb.append("申请闲置");
		}else{
			remarkSb.append("申请报废");
		}
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
		
		// 通知资产使用人（保管人）
		String targetUserId = zc.getSyr();
		ZCZT zczt = ZCZT.valueOf(zcztId);
		msgTemplateService.sendCreateSQMsg(sqr, zc, zczt, targetUserId, sqRemark);
	}
	
	/**
	 * 设备使用人（保管人）：批量提交维修、报废、闲置申请
	 * 
	 * 允许选择一批资产，批量提交申请。
	 * 
	 * @param sqr 设备使用人（保管人）userid
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
