package org.lf.admin.service.wx.my;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.lf.admin.db.pojo.LMsg;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.logs.LMsgService;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.wx.vue.picker.PickerData;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WXMyMsgService")
public class WXMyMsgService {

	@Autowired
	private LMsgService lmsgService;

	/**
	 * 获取消息列表
	 * 
	 * @param appId
	 * @param msgLx 消息类型 空表示全部 其他参数是消息类型对应的
	 * @param userid
	 * @return
	 */
	public AjaxResultModel getMsgList(Integer appId, Integer msgLx, String userid) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			SortedSet<LMsg> t = lmsgService.getMsgList(appId, msgLx, userid);
			List<LMsg> tList = new ArrayList<>();
			for (LMsg lMsg : t) {
				String nr = lMsg.getNr();
				nr = "【" + MsgLX.valueOf(lMsg.getLx()) + "】" + nr;
				lMsg.setNr(nr);
				tList.add(lMsg);
			}
			result.setData(tList);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 返回消息类型列表(包含全部)
	 * 
	 * @return
	 */
	public AjaxResultModel getLxPickListWithAll() {
		AjaxResultModel result = new AjaxResultModel();
		try {
			PickerData pickerData = new PickerData();
			List<PickerDataElement> elementlist = pickerData.getElementList();
			PickerDataElement element;
			element = new PickerDataElement();
			element.setName("全部");
			element.setValue("");
			elementlist.add(element);
			for (MsgLX m : MsgLX.values()) {
				element = new PickerDataElement();
				element.setName(m.name());
				element.setValue(m.ordinal() + "");
				elementlist.add(element);
			}

			result.setData(pickerData.getUnlinkNameAndValue());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 返回消息类型列表
	 * 
	 * @return
	 */
	public AjaxResultModel getLxPickList() {
		AjaxResultModel result = new AjaxResultModel();
		try {
			PickerData pickerData = new PickerData();
			List<PickerDataElement> elementlist = pickerData.getElementList();
			PickerDataElement element;
			for (MsgLX m : MsgLX.values()) {
				element = new PickerDataElement();
				element.setName(m.name());
				element.setValue(m.ordinal() + "");
				elementlist.add(element);
			}

			result.setData(pickerData.getUnlinkNameAndValue());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

}
