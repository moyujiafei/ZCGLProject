package org.lf.admin.service.sys;

import org.lf.admin.db.dao.ChuZTActionMapper;
import org.lf.admin.db.pojo.ChuZTAction;
import org.lf.admin.service.zcgl.ZCZT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ztActionService")
public class ZTActionService {
	@Autowired
	private ChuZTActionMapper ztActionDao;
	
	private static final String TAG_SYR = "使用人组";
	private static final String TAG_GLR = "后勤管理人员组";
	private static final String TAG_XJR = "巡检组";
	private static final String TAG_WXR = "维修组";
	private static final String TAG_BMGLR = "部门资产管理员组";
	
	private ChuZTAction createAction(Integer appId, String tagName, ZCZT zt, String imgUrl, String actionTitle, String actionName, String queryParam) {
		ChuZTAction action = new ChuZTAction();
		action.setAppId(appId);
		action.setTagName(tagName);
		action.setZt(zt.getValue());
		action.setImgUrl(imgUrl);
		action.setActionTitle(actionTitle);
		action.setActionName(actionName);
		action.setQueryParam(queryParam);
		return action;
	}

	/**
	 * 如果appId在chu_zt_action中不存在，插入一组记录；
	 * 如果appId在chu_zt_action中已存在，先删除再插入
	 * 
	 * @param appId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void syncZTAction(Integer appId) {
		// 确定appId是否在chu_zt_action中已存在，如果是，先删除
		ChuZTAction param = new ChuZTAction();
		param.setAppId(appId);
		int count = ztActionDao.countChuZTActionList(param);
		if (count > 0) {
			ztActionDao.deleteByAppID(appId);
		}
		
		// 创建一组记录
		ChuZTAction[] actions =new ChuZTAction[31];
		actions[0] = createAction(appId, TAG_SYR, ZCZT.领用中, "/images/icons/agree.png", "同意", "agreeLeadingZC", null);
		actions[1] = createAction(appId, TAG_SYR, ZCZT.领用中, "/images/icons/refuse.png", "拒绝", null, "/wx/zcsy/refuseLeadingZC?zcid=%s");
		actions[2] = createAction(appId, TAG_SYR, ZCZT.使用中, "/images/icons/sendback.png", "上交资产", null, "/wx/zcsy/sendbackZC?zcid=%s");
		actions[3] = createAction(appId, TAG_SYR, ZCZT.使用中, "/images/icons/zcwx.png", "申请维修", null, "/wx/zcsy/zcwxSQ?zcid=%s");
		actions[4] = createAction(appId, TAG_SYR, ZCZT.使用中, "/images/icons/zcxz.png", "申请闲置", null, "/wx/zcsy/zcxzSQ?zcid=%s");
		actions[5] = createAction(appId, TAG_SYR, ZCZT.使用中, "/images/icons/zcbf.png", "申请报废", null, "/wx/zcsy/zcbfSQ?zcid=%s");
		
		actions[6] = createAction(appId, TAG_WXR, ZCZT.维修中, "/images/icons/finishGZWX.png", "完成维修", null, "/wx/gzwx/finishGZWX?zcid=%s");
		actions[7] = createAction(appId, TAG_WXR, ZCZT.维修中, "/images/icons/resubmitWXSQ.png", "再次维修", null, "/wx/gzwx/resubmitWXSQ?zcid=%s");
		actions[8] = createAction(appId, TAG_WXR, ZCZT.维修中, "/images/icons/zcxz.png", "申请闲置", null, "/wx/gzwx/zcxzSQ?zcid=%s");
		actions[9] = createAction(appId, TAG_WXR, ZCZT.维修中, "/images/icons/zcbf.png", "申请报废", null, "/wx/gzwx/zcbfSQ?zcid=%s");
		
		actions[10] = createAction(appId, TAG_XJR, ZCZT.巡检中, "/images/icons/finishRCXJ.png", "设备正常", null, "/wx/rcxj/finishRCXJ?zcid=%s");
		actions[11] = createAction(appId, TAG_XJR, ZCZT.巡检中, "/images/icons/zcwx.png", "申请维修", null, "/wx/rcxj/zcwxSQ?zcid=%s");
		actions[12] = createAction(appId, TAG_XJR, ZCZT.巡检中, "/images/icons/zcxz.png", "申请闲置", null, "/wx/rcxj/zcxzSQ?zcid=%s");
		actions[13] = createAction(appId, TAG_XJR, ZCZT.巡检中, "/images/icons/zcbf.png", "申请报废", null, "/wx/rcxj/zcbfSQ?zcid=%s");
		
		actions[14] = createAction(appId, TAG_BMGLR, ZCZT.未使用, "/images/icons/revert.png", "申请归还", null, "/wx/zcfp/revertZC?zcid=%s");
		actions[15] = createAction(appId, TAG_BMGLR, ZCZT.未使用, "/images/icons/assign.png", "资产分配", null, "/wx/zcfp/assignZC?zcid=%s");
		actions[16] = createAction(appId, TAG_BMGLR, ZCZT.领用中, "/images/icons/reassign.png", "重新分配", null, "/wx/zcfp/reassignZC?zcid=%s");
		actions[17] = createAction(appId, TAG_BMGLR, ZCZT.上交中, "/images/icons/agree.png", "同意", "agreeSendbackZC", null);
		actions[18] = createAction(appId, TAG_BMGLR, ZCZT.上交中, "/images/icons/refuse.png", "拒绝", null, "/wx/zcfp/refuseSendbackZC?zcid=%s");
		
		actions[19] = createAction(appId, TAG_GLR, ZCZT.已登记, "/images/icons/delete.png", "删除", "deleteZC", null);
		actions[20] = createAction(appId, TAG_GLR, ZCZT.已登记, "/images/icons/allocate.png", "调拨", null, "/wx/zcdj/allocateZC?zcid=%s");
		actions[21] = createAction(appId, TAG_GLR, ZCZT.未使用, "/images/icons/reallocate.png", "重新调拨", null, "/wx/zcdj/reallocateZC?zcid=%s");
		actions[22] = createAction(appId, TAG_GLR, ZCZT.归还中, "/images/icons/agree.png", "同意", "agreeRevertZC", null);
		actions[23] = createAction(appId, TAG_GLR, ZCZT.归还中, "/images/icons/refuse.png", "拒绝", null, "/wx/zcdj/refuseRevertZC?zcid=%s");
		actions[24] = createAction(appId, TAG_GLR, ZCZT.申请维修, "/images/icons/agree.png", "同意", "agreeWXSQ", null);
		actions[25] = createAction(appId, TAG_GLR, ZCZT.申请维修, "/images/icons/refuse.png", "拒绝", null, "/wx/zcyw/refuseWXSQ?zcid=%s");
		actions[26] = createAction(appId, TAG_GLR, ZCZT.申请报废, "/images/icons/agree.png", "同意", null, "/wx/zcyw/agreeBFSQ?zcid=%s");
		actions[27] = createAction(appId, TAG_GLR, ZCZT.申请报废, "/images/icons/refuse.png", "拒绝", null, "/wx/zcyw/refuseBFSQ?zcid=%s");
		actions[28] = createAction(appId, TAG_GLR, ZCZT.申请闲置, "/images/icons/agree.png", "同意", null, "/wx/zcyw/agreeXZSQ?zcid=%s");
		actions[29] = createAction(appId, TAG_GLR, ZCZT.申请闲置, "/images/icons/refuse.png", "拒绝", null, "/wx/zcyw/refuseXZSQ?zcid=%s");
		actions[30] = createAction(appId, TAG_GLR, ZCZT.闲置, "/images/icons/agreeBFZC.png", "报废", null, "/wx/zcgl/agreeBFZC?zcid=%s");
		
		// 插入一组记录
		for (ChuZTAction action : actions) {
			ztActionDao.insert(action);
		}
	}
}
