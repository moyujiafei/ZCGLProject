package org.lf.admin.service.logs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.zcgl.RWLX;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

/**
 * 发送消息模板服务
 * 
 * 这里针对系统运行不同阶段发送消息进行统一管理。
 * 
 * @author 许庆炜
 */
@Service("msgTemplateService")
public class MsgTemplateService {
	@Autowired
	private WXUserService userService;
	
	@Autowired
	private ZCService zcService;    // 资产管理服务
	
	@Autowired
	private MsgService msgService;
	
	/**
	 * 查询微信用户表，拼接成用户信息：
	 * 
	 * @param userid
	 * @return
	 */
	public String getUser(Integer appId, String userId) throws OperException {
		StringBuilder sb = new StringBuilder();
		
		WXUser user = userService.getWXUser(appId, userId);
		String name = user.getName();
		String serverUrl = ZCGLProperties.URL_WX_CLIENT;
		String userInfoUrl = ZCGLProperties.URL_USER_INFO;
		String userInfo = String.format(userInfoUrl, appId, userId);
		
		sb.append("<a href='").append(serverUrl).append(userInfo).append("'>");
		sb.append(name);
		sb.append("</a>");
		
		return sb.toString();
	}
	
	/**
	 * 获取任务详情
	 * @param rw
	 * @return
	 * @throws OperException
	 */
	public String getRWXQ(JRW rw) throws OperException {
		StringBuilder sb = new StringBuilder();
		
		String serverUrl = ZCGLProperties.URL_WX_CLIENT;
		String rwInfoUrl = ZCGLProperties.URL_RW_INFO;
		String rwInfo = String.format(rwInfoUrl, rw.getId());
		
		sb.append("<a href='").append(serverUrl).append(rwInfo).append("'>");
		sb.append("查看任务详情");
		sb.append("</a>");
		
		return sb.toString();
	}
	
	/**
	 * 获取资产详情
	 * @param rw
	 * @return
	 * @throws OperException
	 */
	public String getZCXQ(JZC zc) throws OperException {
		StringBuilder sb = new StringBuilder();
		
		String serverUrl = ZCGLProperties.URL_WX_CLIENT;
		String zcInfoUrl = ZCGLProperties.URL_ZC_INFO;
		String zcInfo = String.format(zcInfoUrl, zc.getId());
		
		sb.append("<a href='").append(serverUrl).append(zcInfo).append("'>");
		sb.append("查看资产详情");
		sb.append("</a>");
		
		return sb.toString();
	}
	
	public String getVZCXQ(VZC zc) throws OperException {
		StringBuilder sb = new StringBuilder();
		
		String serverUrl = ZCGLProperties.URL_WX_CLIENT;
		String zcInfoUrl = ZCGLProperties.URL_ZC_INFO;
		String zcInfo = String.format(zcInfoUrl, zc.getZcid());
		
		sb.append("<a href='").append(serverUrl).append(zcInfo).append("'>");
		sb.append("查看资产详情");
		sb.append("</a>");
		
		return sb.toString();
	}
	
	public String getRemark(String remark) {
		return StringUtils.isEmpty(remark) ? "未知原因" : remark;
	}

	/**
	 * 当用户完成一个任务（TOTAL == FINISH_COUNT），系统将向任务所在的"验收人"推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<操作人>已经完成从<开始时间>到<结束时间>期间的<任务类型>工作。点击<查看任务详情>。
	 * 
	 * 查看详情：查看任务详情
	 */
	public void sendFinishRWMsg(String cjr, JRW rw) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(getUser(rw.getAppId(), cjr)).append("已经完成从");
		sb.append(DateUtils.getShortDate(rw.getKssj())).append("到");
		sb.append(DateUtils.getShortDate(rw.getJssj())).append("期间的");
		sb.append(RWLX.valueOf(rw.getLx()).name()).append("工作。点击");
		sb.append(getRWXQ(rw));
		String content = sb.toString();
		
		List<String> jsrList = new ArrayList<>();
		jsrList.add(rw.getYsr());
		msgService.sendUserMsg(rw.getAppId(), MsgLX.系统通知, jsrList, content);
	}
	
	/**
	 * 提交申请。例如：设备使用人（保管人）可以提交申请维修（报废、闲置），资产管理员可以提交申请维修（报废、闲置）。
	 * 系统向targetUserId推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<申请人>于<系统时间><状态名称><资产名称>（<资产代码>）。申请原因为：<申请原因>。点击<查看资产详情>
	 * 
	 * @param sqr 申请人userid
	 * @param sqRemark 申请原因。如果为空，不显示后面的申请原因信息
	 */
	public void sendCreateSQMsg(String sqr, JZC zc, ZCZT zt, String targetUserId, String sqRemark) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(getUser(zc.getAppId(), sqr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append(zt.name());
		sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")");
		sb.append("。申请原因为：").append(getRemark(sqRemark)).append("。");
		sb.append("点击").append(getZCXQ(zc));
		String content = sb.toString();
		
		List<String> jsrList = new ArrayList<>();
		jsrList.add(targetUserId);
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, jsrList, content);
	}
	
	/**
	 * 拒绝申请。对于提交的申请，审核人员可以拒绝该申请。拒绝后，系统向targetUserId推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<审批人>于<系统时间>拒绝您提交的<状态名称><资产名称>（<资产代码>）。点击<查看资产详情>
	 * 
	 * @param spr：审批人userid
	 */
	public void sendRefuseSQMsg(String spr, JZC zc, ZCZT zt, String targetUserId) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(getUser(zc.getAppId(), spr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("拒绝您提交的").append(zt.name());
		sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")。");
		sb.append("点击").append(getZCXQ(zc));
		String content = sb.toString();
		
		List<String> jsrList = new ArrayList<>();
		jsrList.add(targetUserId);
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, jsrList, content);
	}
	
	/**
	 * 同意申请。对于提交的申请，审核人员可以同意该申请。同意后，系统向targetUserId推送一条信息。信息格式为：
	 * 
	 * 【系统通知】：<审批人>于<系统时间>同意您提交的<状态名称><资产名称>（<资产代码>）。点击<查看资产详情>
	 * 
	 * 查看详情：查看任务详情
	 * 
	 * @param spr：审批人userid
	 */
	public void sendAgreeSQMsg(String spr, JZC zc, ZCZT zt, String targetUserId) throws OperException {
		StringBuilder sb = new StringBuilder();
		sb.append(getUser(zc.getAppId(), spr)).append("于");
		sb.append(DateUtils.getLongDate(new Date())).append("同意您提交的").append(zt.name());
		sb.append(zc.getMc()).append("(").append(zc.getDm()).append(")。");
		sb.append("点击").append(getZCXQ(zc));
		String content = sb.toString();
		
		List<String> jsrList = new ArrayList<>();
		jsrList.add(targetUserId);
		msgService.sendUserMsg(zc.getAppId(), MsgLX.系统通知, jsrList, content);
	}
	
}
