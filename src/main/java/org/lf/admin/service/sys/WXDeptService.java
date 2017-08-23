package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.WxDept;
import org.lf.admin.service.wx.vue.picker.PickerData;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
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
	 * 获取部门poppick列表(包含全部)
	 * 
	 * @param appId
	 * @return
	 */
	public PickerData getWXDeptPickWithAll(Integer appId) {
		
		List<WxDept> deptList = deptDao.getWxDeptsLevel(appId);

		PickerData pd = new PickerData();
		List<PickerDataElement> pickList = pd.getElementList();
		Set<String> lvOneMap = new HashSet<>();
		Set<String> lvTwoMap = new HashSet<>();
		Set<String> lvThreeMap = new HashSet<>();
		PickerDataElement firstPickers = new PickerDataElement();
		firstPickers.setName("");
		firstPickers.setParent("0");
		firstPickers.setValue("null1qq");

		PickerDataElement sencondPickers = new PickerDataElement();
		sencondPickers.setName("");
		sencondPickers.setParent("null1qq");
		sencondPickers.setValue("null2qq");

		PickerDataElement thirdPickers = new PickerDataElement();
		thirdPickers.setName("");
		thirdPickers.setParent("null2qq");
		thirdPickers.setValue("null3qq");

		pickList.add(firstPickers);
		pickList.add(sencondPickers);
		pickList.add(thirdPickers);
		boolean LvOne = false;
		boolean LvTwo = false;
		boolean LvThree = false;

		for (WxDept wxDept : deptList) {
			if (wxDept.getLvOneId() != null) {
				LvOne = true;
				if (lvOneMap.add(wxDept.getLvOneId()) == true) {
					PickerDataElement deptPickers = new PickerDataElement();
					deptPickers.setName(wxDept.getLvOne());
					deptPickers.setValue(wxDept.getLvOneId());
					deptPickers.setParent("0");
					PickerDataElement nullDept = new PickerDataElement();
					nullDept.setName("");
					nullDept.setValue("null"+wxDept.getLvOneId());
					nullDept.setParent(wxDept.getLvOneId());
					PickerDataElement nullDept2 = new PickerDataElement();
					nullDept2.setName("");
					nullDept2.setValue("null2"+wxDept.getLvOneId());
					nullDept2.setParent("null"+wxDept.getLvOneId());
					pickList.add(deptPickers);
					pickList.add(nullDept);
					pickList.add(nullDept2);
				}
			}
			if (wxDept.getLvTwoId() != null) {
				LvTwo = true;
				if (lvTwoMap.add(wxDept.getLvTwoId()) == true) {
					PickerDataElement deptPickers = new PickerDataElement();
					deptPickers.setName(wxDept.getLvTwo());
					deptPickers.setValue(wxDept.getLvTwoId());
					deptPickers.setParent(wxDept.getLvOneId());
					PickerDataElement nullDept = new PickerDataElement();
					nullDept.setName("");
					nullDept.setValue("null"+wxDept.getLvTwoId());
					nullDept.setParent(wxDept.getLvTwoId());
					pickList.add(deptPickers);
					pickList.add(nullDept);					
				}
			}
			if (wxDept.getLvThreeId() != null) {
				LvThree = true;
				if (lvThreeMap.add(wxDept.getLvThreeId()) == true) {
					PickerDataElement deptPickers = new PickerDataElement();
					deptPickers.setName(wxDept.getLvThree());
					deptPickers.setValue(wxDept.getLvThreeId());
					deptPickers.setParent(wxDept.getLvTwoId());
					pickList.add(deptPickers);
				}
			}
		}

		if (LvThree == true) {
			pd.setLevel(3);
		} else if (LvTwo == true) {
			pd.setLevel(2);
		} else if (LvOne == true) {
			pd.setLevel(1);
		}
		pd.setRes(pd.getLinkArray());
		return pd;
	}
	
	
	/**
	 * 获取部门poppick列表
	 * 
	 * @param appId
	 * @return
	 */
	public PickerData getWXDeptPick(Integer appId) {
		ChuWXDept param = new ChuWXDept();
		param.setAppId(appId);
		List<ChuWXDept> list = deptDao.selectList(param);
		PickerData pick = null;
		if (list != null && list.size() > 0) {
			pick = new PickerData();
			List<PickerDataElement> pickList = pick.getElementList();
			PickerDataElement item = new PickerDataElement();
			for (ChuWXDept xq : list) {
				item = new PickerDataElement();
				item.setValue(xq.getDeptNo().toString());
				item.setName(xq.getDeptName());
				pickList.add(item);
			}
		}
		return pick;
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
	
	
	/**
	 * 获取部门树列表
	 * @param appId
	 * @return
	 */
	public List<EasyuiTree> getWXDeptTre(Integer appId){
		List<EasyuiTree> list=new ArrayList<>();
		Map<Integer,List<ChuWXDept>> map=initDeptTree(appId);
		int dept_root=appDao.selectByPrimaryKey(appId).getDeptRoot();
		

		System.out.println(depth(appId,dept_root,0));
		return list;
	}
	
	public int depth(Integer appId, Integer deptno, Integer depth){
		
		++depth;
		List<String> dList = getSubDeptmentByDeptNo(appId, deptno);
		if (dList.size() == 0) {
			return depth;
		}
		
		for (String string : dList) {
			int indexDepth = depth(appId,Integer.parseInt(string),depth);
			depth = indexDepth > depth ? indexDepth : depth;
		}
		
		return depth;
		
	}
	
	
}
