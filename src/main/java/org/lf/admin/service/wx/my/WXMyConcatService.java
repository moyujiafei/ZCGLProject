package org.lf.admin.service.wx.my;

import java.util.List;

import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.wx.vue.picker.PickerData;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("WXMyConcatService")
public class WXMyConcatService {

	@Autowired
	private WXUserService wxUserService;
	
	@Autowired
	private WXDeptService wxDeptService;
	
	@Autowired
	private ChuWXDeptMapper wxDeptDao;
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
	 * 获取微信部门列表(包含全部)
	 * 
	 * @param appId
	 * @return
	 */
	public AjaxResultModel getWXDeptPickWithAll(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			PickerData data = wxDeptService.getWXDeptPickWithAll(appId);
			result.setData(data);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

}
