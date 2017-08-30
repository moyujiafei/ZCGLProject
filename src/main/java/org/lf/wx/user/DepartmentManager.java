package org.lf.wx.user;

import java.util.ArrayList;
import java.util.List;

import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 管理部门
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E7%AE%A1%E7%90%86%E9%83%A8%E9%97%A8
 * 
 */
public class DepartmentManager {
	/**
	 * 创建部门
	 */
	public static final String CREATE = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";
	/**
	 * 获取部门列表
	 */
	public static final String LIST = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s&id=%s";
	/**
	 * 删除部门
	 */
	public static final String DELETE = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";

	
	/**
	 * 创建部门
	 *  
	 * @param 
{
   "id": 2,
   "name": "广州研发中心",
   "parentid": 1,
   "order": 1
}	 
	 * @return  正常时的返回JSON数据包示例：
{
   "errcode": 0,
   "errmsg": "created",
   "id": 1
}
	 */
	public static Department createDepartment(String accessToken, Department department) throws WXException {
		String url = String.format(CREATE, accessToken);
		JSONObject json = department.getJSON();
		
		String jsonString = WXUtils.downloadString(url, "POST", json.toJSONString());
		json = JSON.parseObject(jsonString);

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		int id = json.getIntValue(Department.ID);
		return getDepartmentList(accessToken, id).get(0);
	}
	
	/**
	 * 获取部门列表
	 * 
	 * @param id 部门id。获取指定部门及其下的子部门
	 * @return
{
   "errcode": 0,
   "errmsg": "ok",
   "department": [
       {
           "id": 2,
           "name": "广州研发中心",
           "parentid": 1,
           "order": 10
       },
       {
           "id": 3
           "name": "邮箱产品部",
           "parentid": 2,
           "order": 40
       }
   ]
}
	 * @throws WXException
	 */
	public static List<Department> getDepartmentList(String accessToken, int id) throws WXException {
		String url = String.format(LIST, accessToken, id);
		
		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		List<Department> departmentList = new ArrayList<>();
		JSONArray array = json.getJSONArray("department");
		for (int i = 0; i < array.size(); i++) {
			departmentList.add(new Department(array.getJSONObject(i))); 
		}
		
		return departmentList;
	}
	
	/**
	 * 删除部门 
	 * @param id 	部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）
	 * @return
{
   "errcode": 0,
   "errmsg": "deleted"
}	 
	 * @throws WXException
	 */
	public static boolean delDepartment(String accessToken, int id) throws WXException {
		String url = String.format(DELETE, accessToken, id);
		
		String jsonString = WXUtils.downloadString(url, "GET", null);
		JSONObject json = JSON.parseObject(jsonString);

		// 首先是否创建成功
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		} 
		
		return true;
	}
}
