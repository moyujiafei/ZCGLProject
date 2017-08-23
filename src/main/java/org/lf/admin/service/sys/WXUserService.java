package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.ChuTagMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.pojo.ChuMenu;
import org.lf.admin.db.pojo.ChuTag;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Service("wxuserService")
public class WXUserService {
	public static final OperErrCode 微信用户不存在 = new OperErrCode("10301", "微信用户不存在");
	
	@Autowired
	private ChuWXUserMapper wxUserDao;
	
	@Autowired
	private ChuWXDeptMapper wxDeptDao;
	
	@Autowired
	private WXDeptService wxDeptService;
	
	@Autowired
	private ChuTagMapper chuTagMapper;
	
	private Map<Integer, ChuWXDept> createDeptMap(Integer appId) {
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> deptList = wxDeptDao.selectList(param);
		
		Map<Integer, ChuWXDept> map = new HashMap<>();
		for (ChuWXDept dept : deptList) {
			map.put(dept.getDeptNo(), dept);
		}
		
		return map;
	}
	
	private Map<Integer, ChuTag> createTagMap(Integer appId) {
		ChuTag param = new ChuTag();
		param.setAppId(appId);
		List<ChuTag> tagtList = chuTagMapper.selectList(param);
		
		Map<Integer, ChuTag> map = new HashMap<Integer, ChuTag>();
		for (ChuTag tag : tagtList) {
			map.put(tag.getTagNo(), tag);
		}
		
		return map;
	}
	
	/**
	 * 注意：从数据库读出ChuWXUser，转换为对应的WXUser。转换方式为:
	 * 1）gender变为sex。其中1-->男；2-->女
	 * 2）ChuWXUser中的department，格式为："department": [1, 2], 需要转换为WXUser中的部门列表数据。
	 * 3)标签号数组转换为标签列表数据
	 * 
	 * @param cUser
	 * @param tagMap 
	 * @return
	 */
	private WXUser toUser(ChuWXUser cUser, Map<Integer, ChuWXDept> deptMap, Map<Integer, ChuTag> tagMap) {
		WXUser wxUser = new WXUser();
		
		wxUser.setAppId(cUser.getAppId());
		wxUser.setId(cUser.getId());
		wxUser.setUserid(cUser.getUserid());
		wxUser.setName(cUser.getName());
		wxUser.setPosition(cUser.getPosition());
		wxUser.setMobile(cUser.getMobile());
		if (cUser.getGender() != null) {
			wxUser.setSex(cUser.getGender() == 1 ? "男" : "女");
		}
		wxUser.setEmail(cUser.getEmail());
		wxUser.setWeixinid(cUser.getWeixinid());
		wxUser.setAvatar(cUser.getAvatar());
		wxUser.setStatus(WXUserStatus.valueOf(cUser.getStatus()).name());
		wxUser.setExtattr(cUser.getExtattr());
		
		String department = cUser.getDepartment();
		if (! StringUtils.isEmpty(department)) {
			JSONArray array = JSON.parseArray(department);
			ChuWXDept wxDept;
			for (int i = 0; i < array.size(); i++) {
				wxDept = deptMap.get(array.getIntValue(i));
				wxUser.addDept(wxDept);
			}
		}
		
		String tagNo = cUser.getTagNo();
		if (! StringUtils.isEmpty(tagNo)) {
			JSONArray tagNoArray = JSON.parseArray(tagNo);
			ChuTag tag;
			for (int i = 0; i < tagNoArray.size(); i++) {
				tag = tagMap.get(tagNoArray.getIntValue(i));
				wxUser.addTag(tag);
			}
		}
		
		return wxUser;
	}
	
	/**
	 * 获取微信用户
	 * 
	 * @param appId
	 * @param userId
	 * @return
	 * @throws OperException 微信用户不存在
	 */
	public WXUser getWXUser(Integer appId, String userId) throws OperException {
		Map<Integer, ChuWXDept> map = createDeptMap(appId);
		Map<Integer, ChuTag> tagMap = createTagMap(appId);
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		param.setUserid(userId);
		
		ChuWXUser cUser = wxUserDao.select(param);
		if (cUser == null) {
			throw new OperException(微信用户不存在);
		}
		
		return toUser(cUser, map, tagMap);
	}
	
	/**
	 * 获取微信用户
	 * 
	 * @return
	 * @throws OperException 微信用户不存在
	 */
	public WXUser getWXUser(ChuWXUser param) throws OperException {
		Map<Integer, ChuWXDept> map = createDeptMap(param.getAppId());
		Map<Integer, ChuTag> tagMap = createTagMap(param.getAppId());
		ChuWXUser cUser = wxUserDao.select(param);
		if (cUser == null) {
			throw new OperException(微信用户不存在);
		}
		
		return toUser(cUser, map, tagMap);
	}
	
	/**
	 * 得到指定应用下的微信用户列表
	 * 
	 * @see #getWXUser(Integer, String)
	 * @param appId
	 * @return
	 */
	public List<WXUser> getWXUserList(ChuWXUser param) {
		Map<Integer, ChuWXDept> map = createDeptMap(param.getAppId());
		Map<Integer, ChuTag> tagMap = createTagMap(param.getAppId());
		List<ChuWXUser> cUserList = wxUserDao.selectList(param);
		List<WXUser> userList = new ArrayList<>();
		for (ChuWXUser cUser : cUserList) {
			userList.add(toUser(cUser, map, tagMap));
		}
			
		return userList;
	}
	
	/**
	 * 统计指定应用下的微信用户数
	 * @param appId
	 * @return
	 * @throws OperException
	 */
	public int countWXUserList(ChuWXUser param) {
		return wxUserDao.countWXUserList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的微信用户。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<WXUser> getWXUserList(ChuWXUser param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		
		Map<Integer, ChuWXDept> map = createDeptMap(param.getAppId());
		Map<Integer, ChuTag> tagMap = createTagMap(param.getAppId());
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		List<ChuWXUser> cUserList = wxUserDao.selectUserList(param);
		List<WXUser> userList = new ArrayList<>();
		for (ChuWXUser cUser : cUserList) {
			userList.add(toUser(cUser, map, tagMap));
		}
			
		return userList;
	}
	
	public EasyuiDatagrid<WXUser> getPageWXUserList(ChuWXUser param, int rows, int page) {
		//根据前台传入的deptId,得到它的子部门列表（包括deptId），再拼成'(2|3|4)'类型的字符串，放入department中，实现部门的多级查找
		Integer appId=param.getAppId();
		String deptId=param.getDepartment();
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
			param.setDepartment(sb.toString());
		}
		
		int total = countWXUserList(param);
		EasyuiDatagrid<WXUser> pageDatas = new EasyuiDatagrid<WXUser>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<WXUser>());
		} else {
			List<WXUser> roleList = getWXUserList(param, rows, page);
			pageDatas.setRows(roleList);
		}
		pageDatas.setTotal(total);

		return pageDatas;
	}
	
	
//	/**
//	 * 依据WXUser中的DEPARTMENT的父子关系（CHU_WXDEPT表）构建一个用户树。上级部门为父节点；下级部门为子节点。
//	 * 注意：easyui中的state、iconCls忽略
//	 * @param userList
//	 * @return
//	 */
//	public EasyuiTree getTreeWXUserList(Integer appId, List<WXUser> userList) {
//		Map<Integer, ChuWXDept> map = createDeptMap(appId);
//		
//		throw new UnsupportedOperationException();
//	}
	
	
	
	/**
	 * 输出指定应用下用户的菜单列表
	 * @param appId
	 * @param userId
	 * @return
	 * @throws OperException
	 */
	public List<ChuMenu> getMenuList(Integer appId, String userId) throws OperException {
//		TODO:
		return null;
	}
	
	/**
	 * 获取用户树列表
	 * @param appId
	 * @param wxuser_name
	 * @return
	 */
	public List<EasyuiTree> getTreeUserList(Integer appId,String wxuser_name){
		List<EasyuiTree> list=new ArrayList<>();
		//获取所有的wxuser列表
		ChuWXUser param=new ChuWXUser();
		param.setAppId(appId);
		List<WXUser> userList=getWXUserList(param);
		//按部门号放入map中保存
		Map<Integer,List<WXUser>> map=new HashMap<>();
		for(WXUser user : userList){
			for(ChuWXDept dept : user.getDeptList()){
				Integer deptno=dept.getDeptNo();
				if(!map.containsKey(deptno)){
					map.put(deptno, new ArrayList<WXUser>());
				}
				if(!StringUtils.isEmpty(wxuser_name) && user.getName().contains(wxuser_name)){
					map.get(deptno).add(user);
				}else if(StringUtils.isEmpty(wxuser_name)){
					map.get(deptno).add(user);
				}
			}
		}
		//获取部门树根节点root
		EasyuiTree root=wxDeptService.getWXDeptTree(appId).get(0);
		//遍历root根节点的所有子节点
		root=traversingRootNode(root,map);
		list.add(root);
		return list;
	}
	
	/**
	 * 递归遍历部门树根节点
	 * 根据子节点的deptNo获取该节点下的所有用户节点
	 * 遍历完成，返回root根节点，它现在是一棵用户树。
	 */
	public EasyuiTree  traversingRootNode(EasyuiTree parent,Map<Integer,List<WXUser>> map){
		EasyuiTree node;
		Iterator<EasyuiTree> it=parent.getChildren().iterator();
		while(it.hasNext()){
			node=it.next();
			if(node.getChildren().size() > 0){
				traversingRootNode(node , map);
			}
			//根据子节点的deptno获取该节点下的用户列表
			ChuWXDept dept=wxDeptService.getChuWXDeptByPrimaryKey(Integer.parseInt(node.getId()));
			Integer deptno=dept.getDeptNo();
			List<WXUser> userList=map.get(deptno);
			if (userList != null) {
				//遍历userlist，将每个user作为该部门节点的子节点。
				for(WXUser user : userList){
					EasyuiTree userNode=new EasyuiTree(user.getId()+"", user.getName(), null, false);
					userNode.setIconCls("icon-newMan");
					node.getChildren().add(0, userNode);
				}
			}
			//如果这个子节点没有孩子节点，则将它从父节点中移除
			if(node.getChildren().size()==0){
				it.remove();
			}
		}
		return parent;
	}
	
	/**
	 * 通过param获取ChuWXUser信息
	 * @param param
	 * @return
	 */
	public ChuWXUser getChuWXUserByPrimaryKey(Integer id){
		return wxUserDao.selectByPrimaryKey(id);
	}
	
	/**
	 *  通过部门代码模糊查询，该部门下所有的ChuWXUser
	 * @return
	 */
	public  List<ChuWXUser> fuzzyChuWXUsers(Integer appId, String dept) {
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		param.setDepartment(dept);
		return wxUserDao.selectUserList(param);
	}
}
