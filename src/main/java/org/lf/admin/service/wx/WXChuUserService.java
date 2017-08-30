package org.lf.admin.service.wx;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXChuUserService {
	
	@Autowired
	private WXDeptService wxDeptService;
	
	@Autowired
	private WXUserService wxUserService;
	
	/**
	 * 根据部门id返回微信用户列表
	 * 
	 * @param deptId 如果部门id为空则返回全部用户
	 * @param appId
	 * @param rows
	 * @param page
	 * @return
	 */
	public AjaxResultModel getWXUserListByDeptId(String deptId ,Integer appId, Integer rows, Integer page) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			ChuWXUser user = new ChuWXUser();
			user.setAppId(appId);
			if (!StringUtils.isEmpty(deptId)) {
				List<String> deptList = wxDeptService.getSubDeptmentByDeptNo(appId,Integer.parseInt(deptId));
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (int i = 0; i < deptList.size(); i++) {
					sb.append(deptList.get(i));
					if (i != deptList.size()-1) {
						sb.append("|");
					}
				}
				sb.append(")");
				user.setDepartment(sb.toString());
			}
			result.setData(wxUserService.getWXUserList(user, rows, page));
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 返回前台需要的二级poppicker控件数据
	 * 一级:部门名
	 * 二级:用户名
	 * 
	 * @param request
	 * @return
	 */
	public AjaxResultModel getUserPicker(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		List<PickerDataElement> pickerList = new ArrayList<PickerDataElement>();
		PickerDataElement deptPicker = null;
		PickerDataElement userPicker = null;
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> deptList = wxDeptService.getWXDeptList(param);
		if (deptList.size() == 0 || deptList == null) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg("没有数据");
			return result;
		}
		for (ChuWXDept wxDept : deptList) {
			deptPicker = new PickerDataElement();
			deptPicker.setName(wxDept.getDeptName());
			deptPicker.setParent("0");
			deptPicker.setValue(wxDept.getDeptNo().toString());

			ChuWXUser temp = new ChuWXUser();
			temp.setAppId(appId);
			temp.setDepartment(wxDept.getDeptNo().toString());
			List<WXUser> userList = wxUserService.getWXUserList(temp);
			if (userList.size() == 0 || userList == null) {
				result.setCode(WXResultCode.ERROR.getCode());
				result.setMsg("没有数据");
				return result;
			}

			pickerList.add(deptPicker);
			for (WXUser wxUser : userList) {
				userPicker = new PickerDataElement();
				userPicker.setName(wxUser.getName());
				userPicker.setParent(deptPicker.getValue());
				userPicker.setValue(wxUser.getUserid());
				pickerList.add(userPicker);
			}
		}
		result.setData(pickerList);
		result.setCode(WXResultCode.SUCCESS.getCode());
		result.setMsg(WXResultCode.SUCCESS.getMsg());
		return result;
	}
}
