package org.lf.admin.service.zcgl;

import java.util.Date;
import java.util.List;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.JRWXZMapper;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.logs.MsgTemplateService;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产运维
 * 后勤管理员相关操作
 */
@Service("zcywService")
public class ZCYWService {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private JRWXZMapper rwxzDao;
	
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
	private FJService fjService;
	
	@Autowired
	private MsgTemplateService msgTemplateService;
	
	@Autowired
	private MsgService msgService;
	
	/**
	 * 后勤管理人员，同意提交的维修申请
	 * 
	 * 1.	后勤管理人员查询J_ZC中处于“申请维修”状态的资产列表。选中一个或多个资产，有两个操作：“拒绝”、“同意”
	 * 2.	选择“同意”：
	 * 	将选中资产状态置为“维修中”。在L_ZCZT中添加记录。
	 * 	记录人：创建人（cjr）
	 * 	原状态：申请维修
	 * 	新状态：维修中
	 *      备注：同意维修<资产名称>(<资产编号>)
	 *  3.  通知使用人（保管人）：sendAgreeSQMsg	(其中Spr：审批人，为创建人cjr）
	 * 
	 * @param appId
	 * @param cjr
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeWXSQ(Integer appId, String cjr, List<Integer> zcidList) throws OperException {
		
		JZC zc;
		LZT zt;
		StringBuilder remarkSb;
		for (Integer zcid : zcidList) {
			//将选中资产状态置为“使用中”
			zc = new JZC();
			zc.setId(zcid);
			zc.setZt(ZCZT.维修中.getValue());
			jzcDao.updateByPrimaryKeySelective(zc);
			
			//在L_ZCZT中添加记录
			JZC preZc = zcService.getZC(zcid);
			zt = new LZT();
			zt.setAppId(preZc.getAppId());
			zt.setZcdm(preZc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.申请维修.getValue());
			zt.setNewZt(ZCZT.维修中.getValue());
			remarkSb = new StringBuilder();
			remarkSb.append("同意维修").append(preZc.getMc()).append("（").append(preZc.getDm()).append("）");
			zt.setRemark(remarkSb.toString());
			ztService.insertZT(zt);
			
			//通知原有设备使用人（保管人）
			msgTemplateService.sendAgreeSQMsg(cjr, preZc, ZCZT.维修中, preZc.getSyr());
		}
	}
	
	/**
	 * 后勤管理人员，拒绝提交的维修申请
	 * 
	 * 1.	后勤管理人员查询J_ZC中处于“申请维修”状态的资产列表。选中一个或多个资产，有两个操作：“拒绝”、“同意”
	 * 2.	选择“拒绝”：
	 * 	添加“拒绝原因”
	 * 	将选中资产状态置为“使用中”。在L_ZCZT中添加记录。
	 * 	记录人：当前用户
	 * 	原状态：申请维修
	 * 	新状态：使用中
	 * 	备注：由于<拒绝原因>拒绝维修<资产名称>(<资产编号>)的申请。
	 * 	通知使用人（保管人）：sendRefuseSQMsg(String spr, JZC zc, ZCZT zt, String targetUserId)
	 * 	Spr：审批人Userid号
	 * 
	 * @see MsgTemplateService#sendRefuseSQMsg(String, JZC, ZCZT, String) 
	 * 
	 * @param refuseRemark 拒绝原因
	 * @param cjr 任务创建人userid
	 * @param zcidList
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseWXSQ(Integer appId, String refuseRemark, String cjr, List<Integer> zcidList) throws OperException {
		JZC zc;
		for (Integer zcid : zcidList) {			
			zc = zcService.getZC(zcid);
			
			// 在J_ZC中将选中设备状态置为“使用中”
			zc.setZt(ZCZT.使用中.getValue());
			jzcDao.updateByPrimaryKeySelective(zc);
			
			// 向L_ZT中创建记录
			LZT zt = new LZT();
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(cjr);
			zt.setOldZt(ZCZT.申请维修.getValue());
			zt.setNewZt(ZCZT.使用中.getValue());
			StringBuilder sb = new StringBuilder();
			sb.append("由于").append(msgTemplateService.getRemark(refuseRemark)).append("拒绝维修").append(zc.getMc()).append("(").append(zc.getDm()).append(")的申请。");
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
			
			// 通知使用人（保管人）拒绝维修申请
			msgTemplateService.sendRefuseSQMsg(cjr, zc, ZCZT.申请维修, zc.getSyr());
		}
	}
	
	/**
	 * 后勤管理人员同意报废资产。报废资产收回后勤仓库。
	 * 1. 更新资产信息
	 *    资产状态：“报废”
	 *    资产使用人：空
	 *    资产存放地点：newCFDD
	 *    资产管理编号：空
	 * 2. 将选中资产状态置为“报废”。在L_ZCZT中添加记录。
	 * 	记录人：当前用户
	 * 	原状态：申请报废
	 * 	新状态：报废
	 *      备注：同意报废<资产名称>(<资产编号>)，存放地点为<新存放地点>
	 * 3. 通知使用人（保管人）：sendAgreeSQMsg(String spr, JZC zc, ZCZT zt, JRW rw, String targetUserId)
	 * 
	 * @param appId
	 * @param zcId 资产
	 * @param spr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeBFSQ(Integer appId, List<Integer> zcidList, String newCFDD, String spr) throws OperException {
		JZC zc;
		
		for (Integer zcid : zcidList) {
			// 将选中资产状态置为“报废”
			zc = zcService.getZC(zcid);
			zc.setZt(ZCZT.报废.getValue());
			zc.setSyr(null);
			zc.setCfdd(newCFDD);
			zc.setZcglId(null);
			jzcDao.updateByPrimaryKeySelective(zc);
			
			// 在L_ZCZT中添加记录
			LZT zt = new LZT();
			JZC preZC = zcService.getZC(zcid);
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(spr);
			zt.setOldZt(ZCZT.申请报废.getValue());
			zt.setNewZt(ZCZT.报废.getValue());
			
			StringBuilder sb = new StringBuilder();
			sb.append("同意报废").append(zc.getMc()).append("(").append(zc.getDm()).append(")");
			if (! StringUtils.isEmpty(newCFDD)) {
				sb.append("，存放地点为").append(fjService.getCFDD(Integer.parseInt(newCFDD)));
			}
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
			
			// 通知使用人（保管人）
			msgTemplateService.sendAgreeSQMsg(spr, zc, ZCZT.申请报废, preZC.getSyr());
		}
	}
	
	/**
	 * 后勤管理人员拒绝报废资产
	 * 
	 * 添加“拒绝原因”
	 * 将选中资产状态置为“使用中”。
	 * 在L_ZCZT中添加记录。 
	 * 	记录人：当前用户
	 * 	原状态：申请报废
	 * 	新状态：使用中
	 * 	备注：由于<拒绝原因>拒绝报废<资产名称>(<资产编号>)的申请。
	 * 通知使用人（保管人）和通知资产管理员：sendDenySQMsg
	 * 
	 * @param appId
	 * @param zcId
	 * @param spr 审批人userid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseBFSQ(Integer appId, List<Integer> zcidList, String refuseRemark, String spr) throws OperException {
		JZC zc;
		
		for (Integer zcid : zcidList) {
			// 将选中资产状态置为“使用中”。
			zc = zcService.getZC(zcid);
			zc.setZt(ZCZT.使用中.getValue());
			jzcDao.updateByPrimaryKeySelective(zc);
			
			// 在L_ZCZT中添加记录
			LZT zt = new LZT();
			JZC preZC = zcService.getZC(zcid);
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(spr);
			zt.setOldZt(ZCZT.申请报废.getValue());
			zt.setNewZt(ZCZT.使用中.getValue());
			StringBuilder sb = new StringBuilder();
			sb.append("由于").append(msgTemplateService.getRemark(refuseRemark)).append("拒绝报废").append(zc.getMc()).append("(").append(zc.getDm()).append(")的申请。");
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
			
			// 通知使用人（保管人）
			msgTemplateService.sendRefuseSQMsg(spr, zc, ZCZT.申请报废, preZC.getSyr());
		}
	}
	
	/**
	 * 后勤管理人员同意闲置资产。与报废资产一样，闲置资产收回后勤仓库。
	 * 1. 更新资产信息
	 *    资产状态：“闲置”
	 *    资产使用人：空
	 *    资产存放地点：newCFDD
	 *    资产管理编号：空
	 * 2. 在L_ZCZT中添加记录。
	 * 	记录人：当前用户
	 * 	原状态：申请闲置
	 * 	新状态：闲置
	 *      备注：同意闲置<资产名称>(<资产编号>)，存放地点为<新存放地点>
	 * 通知使用人（保管人）：sendAgreeSQMsg
	 * 
	 * @param appId
	 * @param zcId 资产
	 * @param spr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeXZSQ(Integer appId, List<Integer> zcidList, String newCFDD, String spr) throws OperException {
		JZC zc;
		
		for (Integer zcid : zcidList) {
			// 将选中资产状态置为“闲置”
			zc = zcService.getZC(zcid);
			zc.setZt(ZCZT.闲置.getValue());
			zc.setSyr(null);
			zc.setCfdd(newCFDD);
			zc.setZcglId(null);
			jzcDao.updateByPrimaryKeySelective(zc);
			
			// 在L_ZCZT中添加记录
			LZT zt = new LZT();
			JZC preZC = zcService.getZC(zcid);
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(spr);
			zt.setOldZt(ZCZT.申请闲置.getValue());
			zt.setNewZt(ZCZT.闲置.getValue());
			
			StringBuilder sb = new StringBuilder();
			sb.append("同意闲置").append(zc.getMc()).append("(").append(zc.getDm()).append(")");
			if (! StringUtils.isEmpty(newCFDD)) {
				sb.append("，存放地点为").append(fjService.getCFDD(Integer.parseInt(newCFDD)));
			}
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
			
			// 通知使用人（保管人）
			msgTemplateService.sendAgreeSQMsg(spr, zc, ZCZT.申请闲置, preZC.getSyr());
		}
		
	}
	
	/**
	 * 后勤管理人员拒绝闲置资产
	 * 
	 * 添加“拒绝原因”
	 * 将选中资产状态置为“使用中”。
	 * 在L_ZCZT中添加记录。
	 * 	记录人：当前用户
	 * 	原状态：申请闲置
	 * 	新状态：使用中
	 * 	备注：由于<拒绝原因>拒绝闲置<资产名称>(<资产编号>)的申请。
	 * 通知使用人（保管人）：sendDenySQMsg
	 * 
	 * @param appId
	 * @param zcId
	 * @param refuseRemark 拒绝原因
	 * @param spr 审批人
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refuseXZSQ(Integer appId, List<Integer> zcidList, String refuseRemark, String spr) throws OperException {
		// 将选中资产状态置为“使用中”。
		JZC zc;
		
		for (Integer zcid : zcidList) {
			zc = zcService.getZC(zcid);
			zc.setZt(ZCZT.使用中.getValue());
			zcService.updateZC(zc);
			
			// 在L_ZCZT中添加记录
			LZT zt = new LZT();
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(spr);
			zt.setOldZt(ZCZT.申请闲置.getValue());
			zt.setNewZt(ZCZT.使用中.getValue());
			StringBuilder sb = new StringBuilder();
			sb.append("由于").append(msgTemplateService.getRemark(refuseRemark)).append("拒绝闲置").append(zc.getMc()).append("(").append(zc.getDm()).append(")的申请。");
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
			
			// 通知使用人（保管人）
			msgTemplateService.sendRefuseSQMsg(spr, zc, ZCZT.申请闲置, zc.getSyr());
		}
	}
	
	/**
	 * 后勤管理人员同意对闲置资产进行报废
	 * 
	 * 将选中资产状态置为“报废”。在L_ZCZT中添加记录。
	 * 	记录人：审批人userid
	 * 	原状态：闲置
	 * 	新状态：报废
	 *      备注：由于<原因>，报废<资产名称>(<资产编号>)。
	 * 
	 * @param appId
	 * @param zcId 资产
	 * @param spr
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeBFZC(Integer appId, List<Integer> zcidList, String remark, String spr) throws OperException {
		// 将选中资产状态置为“报废”
		JZC zc;
		
		for (Integer zcid : zcidList) {
			zc = zcService.getZC(zcid);
			zc.setZt(ZCZT.报废.getValue());
			zcService.updateZC(zc);

			// 在L_ZCZT中添加记录
			LZT zt = new LZT();
			zt.setAppId(appId);
			zt.setZcdm(zc.getDm());
			zt.setJlsj(new Date());
			zt.setJlr(spr);
			zt.setOldZt(ZCZT.闲置.getValue());
			zt.setNewZt(ZCZT.报废.getValue());
			StringBuilder sb = new StringBuilder();
			if (! StringUtils.isEmpty(remark)) {
				sb.append("由于").append(msgTemplateService.getRemark(remark)).append(", ");
			}
			sb.append("报废").append(zc.getMc()).append("(").append(zc.getDm()).append(")");
			zt.setRemark(sb.toString());
			ztService.insertZT(zt);
		}
		
	}

}
