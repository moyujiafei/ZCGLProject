package org.lf.admin.service.logs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.dao.LMsgMapper;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.LMsg;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.lf.wx.message.SendMessage;
import org.lf.wx.message.SendMessageManager;
import org.lf.wx.utils.WXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 消息日志服务 
 * 
 * @author 文臣
 */
@Service("msgLogService")
public class MsgService {
	public static final OperErrCode 接收用户不能为空 = new OperErrCode("10201", "接收用户不能为空");
	public static final OperErrCode 消息类型不能为空 = new OperErrCode("10201", "消息类型不能为空");
	public static final OperErrCode 消息内容不能为空 = new OperErrCode("10201", "消息内容不能为空");
	public static final OperErrCode 消息发送失败 = new OperErrCode("10201", "消息发送失败");
	
	public static final String TO_USER = "toUser";
	public static final String TO_PARTY = "toParty";  // 部门
	public static final String TO_TAG = "toTag";     
	public static final String TO_ALL = "@all";     

	@Autowired
	private LMsgMapper msgDao;
	
	@Autowired
	private ChuWXUserMapper wxUserDao;
	
	@Autowired
	private WXAppService appService;
	
	/**
	 * 将消息发送到指定对象列表中。如果target为all，即全体人员，则toList内容将被忽略
	 * @param appId
	 * @param target
	 * @param toList
	 * @param content
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendMsg(Integer appId, SendMessage.SendTarget target, List<String> toList, String content) throws OperException {
		ChuApp app = appService.getApp(appId);
		String message = SendMessage.textMessage(app.getAgentId(), content, target, toList);
	
		try {
			SendMessageManager.sendMessage(appService.getAccessToken(appId), message);
		} catch (WXException e) {
			throw new OperException(消息发送失败);
		}
	}
	
	private String getMsgTypeTitle(MsgLX lx) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("【");
		sb.append(lx.toString());
		sb.append("】：");
		
		return sb.toString();
	}
	
	private void validMessage (MsgLX lx, List<String> jsrList, String nr) throws OperException {
		if (StringUtils.isEmpty(nr)) {
			throw new OperException(消息内容不能为空);
		}
		if (lx == null) {
			throw new OperException(消息类型不能为空);
		}
	}
	
	/**
	 * 将该消息发送给指定人员，同时向L_MSG中插入一条记录.该记录的jsr为一个JSON字符串。"toUser":"userid"
	 * @param appId
	 * @param lx 消息类型
	 * @param jsr 消息接收人
	 * @param nr
	 * @throws OperException 接收用户不能为空， 消息内容不能为空, 消息类型不能为空, 消息发送失败
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendUserMsg(Integer appId, MsgLX lx, String jsr, String nr) throws OperException {
		//验证消息是否正确
		List<String> jsrList = new ArrayList<String>(); 
		jsrList.add(jsr);
		validMessage(lx, jsrList, nr);
		
		// 设置消息内容，例如：【系统通知】：XXXXXX
		String title = getMsgTypeTitle(lx);
		String content = title + nr;
		
		// 在L_MSG中插入一条记录
		LMsg record = new LMsg();
		record.setAppId(appId);
		record.setFssj(new Date());
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		array.add(jsr);
		json.put(TO_USER, array);
		record.setJsr(json.toJSONString());
		record.setLx(lx.getValue());
		record.setNr(nr);
		msgDao.insert(record);
		
		// 给指定用户列表发消息
		List<String> toList = new ArrayList<>();
		toList.add(jsr);
		sendMsg(appId, SendMessage.SendTarget.toUserList, toList, content);
	}
	
	/**
	 * 将该消息发送给一组指定用户，同时向L_MSG中插入一组记录
	 * 注意：发送消息时，所有指定用户一起发送。插入记录时，每个用户插入一条记录。
	 * @param appId
	 * @param lx 消息类型
	 * @param jsrList 消息接收人列表
	 * @param nr
	 * @throws OperException 接收用户不能为空， 消息内容不能为空, 消息类型不能为空, 消息发送失败
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendUserMsg(Integer appId, MsgLX lx, List<String> jsrList, String nr) throws OperException {
		//验证消息是否正确
		validMessage(lx, jsrList, nr);
		
		// 设置消息内容，例如：【系统通知】：XXXXXX
		String title = getMsgTypeTitle(lx);
		String content = title + nr;
		
		// 在L_MSG中插入多条记录
		LMsg record = null;
		for (String jsr : jsrList) {//每个用户插入一条记录
			record = new LMsg();
			record.setAppId(appId);
			record.setFssj(new Date());
			JSONObject json = new JSONObject();
			JSONArray array = new JSONArray();
			array.add(jsr);
			json.put(TO_USER, array);
			record.setJsr(json.toJSONString());
			record.setLx(lx.getValue());
			record.setNr(nr);
			msgDao.insert(record);
		}
		
		// 给指定用户列表发消息
		sendMsg(appId, SendMessage.SendTarget.toUserList, jsrList, content);
	}
	
	/**
	 * 将该消息发送给所有用户，同时向L_MSG中插入"一条"记录。该记录的jsr为一个JSON字符串。"toUser":"@all"
	 * @param appId
	 * @param lx 消息类型
	 * @param nr
	 * @throws OperException 消息内容不能为空, 消息类型不能为空, 消息发送失败
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendAllUserMsg(Integer appId, MsgLX lx, String nr) throws OperException {
		//验证消息是否正确
		List<String> jsrList = new ArrayList<String>();
		jsrList.add("@all");
		validMessage(lx, jsrList, nr);
		
		// 设置消息内容，例如：【系统通知】：XXXXXX
		String title = getMsgTypeTitle(lx);
		String content = title + nr;
		
		// 在L_MSG中插入一条记录
		LMsg record = new LMsg();
		record.setAppId(appId);
		record.setFssj(new Date());
		JSONObject json = new JSONObject();
		json.put(TO_USER, TO_ALL);
		record.setJsr(json.toJSONString());
		record.setLx(lx.getValue());
		record.setNr(nr);
		msgDao.insert(record);
		
		// 给指定用户列表发消息
		sendMsg(appId, SendMessage.SendTarget.all, null, content);
	}
	
	/**
	 * 将该消息发送给标签所属的所有用户，同时向L_MSG中插入"一条"记录。该记录的jsr为一个JSON字符串。"toTag":"[tagNo]"
	 * @param appId
	 * @param lx 消息类型
	 * @param tagNo 标签号所在的用户组发送消息
	 * @param nr
	 * @throws OperException 消息内容不能为空, 消息类型不能为空, 消息发送失败
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendTagMsg(Integer appId, MsgLX lx, Integer tagNo, String nr) throws OperException {
		//验证消息是否正确
		List<String> jsrList = new ArrayList<String>();
		jsrList.add("toTag");
		validMessage(lx, jsrList, nr);
		
		// 设置消息内容，例如：【系统通知】：XXXXXX
		String title = getMsgTypeTitle(lx);
		String content = title + nr;
		
		// 在L_MSG中插入一条记录
		LMsg record = new LMsg();
		record.setAppId(appId);
		record.setFssj(new Date());
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		array.add(tagNo);
		json.put(TO_TAG, array);
		record.setJsr(json.toJSONString());
		record.setLx(lx.getValue());
		record.setNr(nr);
		msgDao.insert(record);
		
		// 给指定标签发消息
		List<String> toList = new ArrayList<>();
		toList.add(tagNo.toString());
		sendMsg(appId, SendMessage.SendTarget.toTagList, toList, content);
	}
	
	/**
	 * 将该消息发送给部门所属的所有用户，同时向L_MSG中插入"一条"记录。该记录的jsr为一个JSON字符串。"toParty":"[deptNo]"
	 * @param appId
	 * @param lx 消息类型
	 * @param partyNo 部门号所在的用户组发送消息
	 * @param nr
	 * @throws OperException 消息内容不能为空, 消息类型不能为空, 消息发送失败
	 */
	public void sendDeptMsg(Integer appId, MsgLX lx, Integer partyNo, String nr) throws OperException {
		//验证消息是否正确
		List<String> jsrList = new ArrayList<String>();
		jsrList.add("toTag");
		validMessage(lx, jsrList, nr);
				
		// 设置消息内容，例如：【系统通知】：XXXXXX
		String title = getMsgTypeTitle(lx);
		String content = title + nr;
		
		// 在L_MSG中插入一条记录
		LMsg record = new LMsg();
		record.setAppId(appId);
		record.setFssj(new Date());
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		array.add(partyNo);
		json.put(TO_PARTY, array);
		record.setJsr(json.toJSONString());
		record.setLx(lx.getValue());
		record.setNr(nr);
		msgDao.insert(record);
		
		// 给指定标签发消息
		List<String> toList = new ArrayList<>();
		toList.add(partyNo.toString());
		sendMsg(appId, SendMessage.SendTarget.toPartyList, toList, content);
	}
	
	/**
	 * 统计消息数量
	 * @param param
	 * @return
	 */
	public int countMsgList(LMsg param) {
		return msgDao.countMsgList(param);
	}
	
	/**
	 * 查询消息列表
	 * @param param
	 * @return
	 */
	public List<LMsg> getMsgList(LMsg param, int rows, int page) {
		param.setStart((page-1)*rows);
		param.setOffset(rows);
		return msgDao.getMsgList(param);
	}
	
	//这个效率比起查询一次数据库得到所有的数据来回怎么样？
	public EasyuiDatagrid<LMsg> getPageMsgList(LMsg param, int rows, int page) {
		int total = countMsgList(param);
		List<LMsg> prePageList = getMsgList(param, rows, page);
		EasyuiDatagrid<LMsg> resultList = new EasyuiDatagrid<>(prePageList, total);
		return resultList;
	}
	
	/**
	 * 查询消息列表
	 * @param param
	 * @return
	 */
	public List<LMsg> getMsgList(LMsg param) {
		return msgDao.getMsgList(param);
	}
	
	/**
	 * 查询发送给指定用户消息列表
	 * 依据L_MSG表中的接收人（JSR）字段。该字段为一个JSON字符串，需要查询其中toUser对应的值。
	 * 其值可以是JSONArray，如{"toUser":["XuQingWei"]}，或者{"toUser":["XuQingWei", "shangwei"]}
	 * 如果userId为XuQingWei时，要将这两条记录都取出来。
	 * 
	 * @param param
	 * @return
	 */
	public List<LMsg> getToUserMsgList(Integer appId, String userId) {
		LMsg param = new LMsg();
		param.setAppId(appId);
		String jsr="{\"toUser\":\\[(.*,)*"+userId+"(,.*)*\\]}";
		param.setJsr(jsr);
		return msgDao.getMsgListByJsr(param);
	}
	
	/**
	 * 查询发送给指定标签消息列表
	 * 依据L_MSG表中的接收人（JSR）字段。该字段为一个JSON字符串，需要查询其中toTag对应的值。
	 * 其值可以是JSONArray，如{"toTag":[1]}，或者{"toTag":[1, 2]}
	 * 如果tagNo为1时，要将这两条记录都取出来。
	 * 
	 * @param param
	 * @return
	 */
	public List<LMsg> getToTagMsgList(Integer appId, Integer tagNo) {
		LMsg param = new LMsg();
		param.setAppId(appId);
		String jsr="{\"toTag\":\\[(.*,)*"+tagNo+"(,.*)*\\]}";
		param.setJsr(jsr);
		return msgDao.getMsgListByJsr(param);
	}
	
	/**
	 * 查询发送给指定部门消息列表
	 * 依据L_MSG表中的接收人（JSR）字段。该字段为一个JSON字符串，需要查询其中toTag对应的值。
	 * 其值可以是JSONArray，如{"toParty":[1]}，或者{"toParty":[1, 2]}
	 * 如果deptNo为1时，要将这两条记录都取出来。
	 * 
	 * @param param
	 * @return
	 */
	public List<LMsg> getToPartyMsgList(Integer appId, Integer deptNo) {
		LMsg param = new LMsg();
		param.setAppId(appId);
		String jsr="{\"toParty\":\\[(.*,)*"+deptNo+"(,.*)*\\]}";
		param.setJsr(jsr);
		return msgDao.getMsgListByJsr(param);
	}
}
