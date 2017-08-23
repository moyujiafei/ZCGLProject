package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wxdeptService")
public class WXDeptService {
	@Autowired
	private ChuWXDeptMapper deptDao;
	
	@Autowired
	private ChuAppMapper appDao;
	
	@Autowired
	private WXUserService wxUserService;
	
	/**
	 * 获得部门下拉框信息(包含全部)
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWXDeptComboWithAll(Integer appId) {
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> list = deptDao.selectList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (ChuWXDept xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getDeptNo().toString());
				item.setText(xq.getDeptName());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得部门下拉框信息
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWXDeptCombo(Integer appId) {
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> list = deptDao.selectList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (ChuWXDept xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getDeptNo().toString());
				item.setText(xq.getDeptName());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获取部门树列表
	 * @param appId
	 * @return
	 */
	public List<EasyuiTree> getWXDeptTree(Integer appId){
		List<EasyuiTree> list=new ArrayList<>();
		Map<Integer,List<ChuWXDept>> map=initDeptTree(appId);
		//生成"全部"节点作为根节点
		EasyuiTree root=new EasyuiTree("全部",new ArrayList<EasyuiTree>(),"open","icon-dept",false);
		list.add(root);
		int dept_root=appDao.selectByPrimaryKey(appId).getDeptRoot();
		//生成一级部门节点
		for(ChuWXDept dept : map.get(dept_root)){
			EasyuiTree node=new EasyuiTree(dept.getId()+"", dept.getDeptName(), new ArrayList<EasyuiTree>(), false);
			node.setIconCls("icon-dept");
			//生成一级部门节点的所有子节点菜单
			if(map.containsKey(dept.getDeptNo())){
				addSubNode(dept.getDeptNo(),map,node);
			}
			root.getChildren().add(node);
		}
		
		return list;
	}
	
	/**
	 * 递归调用此方法，生成当前pid下属的子节点菜单
	 * @param pid
	 * @param map
	 * @return
	 */
	public void addSubNode(Integer pid,Map<Integer,List<ChuWXDept>> map,EasyuiTree node){
		for(ChuWXDept dept : map.get(pid)){
			EasyuiTree subnode=new EasyuiTree(dept.getId()+"", dept.getDeptName(), new ArrayList<EasyuiTree>(), false);
			subnode.setIconCls("icon-dept");
			if(map.containsKey(dept.getDeptNo())){
				addSubNode(dept.getDeptNo(), map,subnode);
			}
			node.getChildren().add(subnode);
		}
	}
	/**
	 * 生成key为pid,值为该pid下属的所有部门列表
	 * @param appId
	 * @return
	 */
	public Map<Integer,List<ChuWXDept>> initDeptTree(Integer appId){
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> deptList = deptDao.selectList(param);
		Map<Integer,List<ChuWXDept>> map=new HashMap<>();
		for(ChuWXDept dept : deptList){
			Integer pid=dept.getDeptPno();
			if(!map.containsKey(pid)){
				List<ChuWXDept> list=new ArrayList<>();
				list.add(dept);
				map.put(pid, list);
			}else{
				map.get(pid).add(dept);
			}
		}
		return map;
	}
	
	/**
	 * 根据param获取ChuWXDept
	 * @param param
	 * @return
	 */
	public ChuWXDept getChuWXDeptByPrimaryKey(Integer id){
		return deptDao.selectByPrimaryKey(id);
	}
	
	public List<ChuWXDept> getWXDeptList(ChuWXDept param){
		return deptDao.selectList(param);
	}
	
	/**
	 * 获取指定部门编号的所有直属子节点列表（包括指定部门编号）
	 * @param deptno
	 * @return
	 */
	public List<String> getSubDeptmentByDeptNo(Integer appId,Integer deptno){
		String deptStr=deptDao.getSubDeptStrByDeptNo(appId, deptno);
		String[] arr=deptStr.split(",");
		return Arrays.asList(arr);
	}
}
