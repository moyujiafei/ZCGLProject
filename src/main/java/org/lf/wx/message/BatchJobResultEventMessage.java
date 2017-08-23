package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 异步任务完成事件推送
 * 本事件是成员在使用异步任务接口时，用于接收任务执行完毕的结果通知。
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class BatchJobResultEventMessage extends MenuEventMessage {

	public static final String JOB_ID = "JobId";
	public static final String JOB_TYPE = "JobType";
	public static final String ERR_CODE = "ErrCode";
	public static final String ERR_MSG = "ErrMsg";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<BatchJob><JobId><![CDATA[%s]]></JobId>"
			+ "<JobType><![CDATA[%s]]></JobType>"
			+ "<ErrCode>%s</ErrCode>"
			+ "<ErrMsg><![CDATA[%s]]></ErrMsg>"
			+ "</BatchJob>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	private String jobId; // 异步任务id，最大长度为64字符
	/**
	 * 操作类型，字符串，目前分别有：
	 * 1. sync_user(增量更新成员)
	 * 2. replace_user(全量覆盖成员)
	 * 3. invite_user(邀请成员关注)
	 * 4. replace_party(全量覆盖部门)
	 */
	private String jobType; 
	private String errCode; // 	返回码
	private String errMsg; // 对返回码的文本描述内容

	/**
	 * 扫码推事件且弹出“消息接收中”提示框的事件推送
	 */
	public BatchJobResultEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		jobId = root.elementText(JOB_ID);
		jobType = root.elementText(JOB_TYPE);
		errCode = root.elementText(ERR_CODE);
		errMsg = root.elementText(ERR_MSG);
	}

	public String getJobId() {
		return jobId;
	}

	public String getJobType() {
		return jobType;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * <xml><ToUserName><![CDATA[wx28dbb14e37208abe]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>1425284517</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[batch_job_result]]></Event>
	 * <BatchJob><JobId><![CDATA[S0MrnndvRG5fadSlLwiBqiDDbM143UqTmKP3152FZk4]]></JobId>
	 * <JobType><![CDATA[sync_user]]></JobType>
	 * <ErrCode>0</ErrCode>
	 * <ErrMsg><![CDATA[ok]]></ErrMsg>
	 * </BatchJob>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), jobId, jobType, errCode, errMsg, agentID);
	}
}
