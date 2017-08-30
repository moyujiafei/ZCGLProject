package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.ChuMenuMapper;
import org.lf.admin.db.dao.ChuRoleMapper;
import org.lf.admin.db.pojo.ChuMenu;
import org.lf.admin.db.pojo.ChuRole;
import org.lf.admin.db.pojo.ChuRoleMenu;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleService {
	public static final OperErrCode 角色不存在 = new OperErrCode("10101", "角色不存在");
	public static final OperErrCode 角色名重名 = new OperErrCode("10102", "角色名重名");
	public static final OperErrCode 角色名不能为空 = new OperErrCode("10103", "角色名不能为空");
	public static final OperErrCode 未给角色分配权限 = new OperErrCode("10104", "未给角色分配权限");

	@Autowired
	private ChuRoleMapper roleDao;

	@Autowired
	private ChuMenuMapper menuDao;

	/**
	 * 获取指定分页角色列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<ChuRole> getPageRoleList(int rows, int page) {
		int total = countRoleList(null);

		EasyuiDatagrid<ChuRole> pageDatas = new EasyuiDatagrid<ChuRole>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<ChuRole>());
		} else {
			List<ChuRole> roleList = getRoleList(rows, page);
			pageDatas.setRows(roleList);
		}
		pageDatas.setTotal(total);

		return pageDatas;
	}

	/**
	 * key为pid，List为该pid下属的所有菜单记录
	 * 
	 * @param menuList
	 * @return
	 */
	private Map<Integer, List<ChuRoleMenu>> initTree(List<ChuRoleMenu> menuList) {
		Map<Integer, List<ChuRoleMenu>> result = new HashMap<>();

		for (ChuRoleMenu menu : menuList) {
			if (!result.containsKey(menu.getPid())) {
				List<ChuRoleMenu> subMenuList = new ArrayList<>();
				subMenuList.add(menu);
				result.put(menu.getPid(), subMenuList);
			} else {
				result.get(menu.getPid()).add(menu);
			}
		}

		return result;
	}

	/**
	 * 递归函数，创建当前节点Node的子节点菜单
	 * 
	 * @param pid
	 * @param result
	 * @param node
	 */
	private void createSubNode(Integer pid, Map<Integer, List<ChuRoleMenu>> result, EasyuiTree node) {
		EasyuiTree subNode;
		for (ChuRoleMenu subMenu : result.get(pid)) {
			subNode = new EasyuiTree();
			subNode.setId(subMenu.getId().toString());
			subNode.setText(subMenu.getText());
			subNode.setIconCls(subMenu.getIconCls());
			subNode.setChecked(subMenu.getChecked() == 1);
			node.getChildren().add(subNode);

			if (result.containsKey(subMenu.getId())) {
				createSubNode(subMenu.getId(), result, subNode);
			}
		}
	}

	/**
	 * 读取指定角色的两级菜单，构建菜单树
	 * 
	 * @param role_id
	 *            为空时返回所有的菜单的check属性为未选中
	 * @return
	 * @throws OperException
	 *             角色不存在
	 */
	public List<EasyuiTree> getTreeRoleList(Integer roleId, Integer pid) {
		List<EasyuiTree> tree = new ArrayList<EasyuiTree>();

		List<ChuRoleMenu> menuList = roleDao.selectByRoleId(roleId);
		Map<Integer, List<ChuRoleMenu>> result = initTree(menuList);
		// 得到一级节点
		EasyuiTree node;
		for (ChuRoleMenu rootMenu : result.get(pid)) {
			node = new EasyuiTree();
			node.setId(rootMenu.getId().toString());
			node.setText(rootMenu.getText());
			node.setIconCls(rootMenu.getIconCls());
			node.setChecked(rootMenu.getChecked() == 1 && rootMenu.getPid() != pid);

			createSubNode(rootMenu.getId(), result, node);
			tree.add(node);
		}

		return tree;
	}

	/**
	 * 获取指定页的角色。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 * @throws OperException
	 */
	public List<ChuRole> getRoleList(int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);

		return roleDao.selectRoleList(pn.getStart(), pn.getOffset());
	}

	/**
	 * 得到角色数
	 */
	public int countRoleList(ChuRole params) {
		return roleDao.countRoleList(params);
	}

	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws OperException
	 *             角色不存在
	 */
	public ChuRole getRole(Integer roleId) throws OperException {
		ChuRole role = roleDao.selectByPrimaryKey(roleId);
		if (role == null) {
			throw new OperException(角色不存在);
		}
		return role;
	}

	/**
	 * 
	 * @param roleName
	 * @return
	 * @throws OperException
	 *             角色不存在
	 */
	public ChuRole getRole(String roleName) throws OperException {
		ChuRole params = new ChuRole();
		params.setName(roleName);
		ChuRole role = roleDao.select(params);
		if (role == null) {
			throw new OperException(角色不存在);
		}

		return role;
	}

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @throws OperException
	 *             角色不存在, 角色名重名
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRole(ChuRole role) throws OperException {
		ChuRole r = getRole(role.getId());
		if (r == null) {
			throw new OperException(角色不存在);
		}

		ChuRole param = new ChuRole();
		param.setName(role.getName());
		r = roleDao.select(param);
		if (r != null) {
			throw new OperException(角色名重名);
		}

		roleDao.updateByPrimaryKeySelective(role);
	}

	/**
	 * 创建一个角色
	 * 
	 * @param role
	 * @param str_menuIds 角色对应的权限菜单id拼接字符串(以;拼接)
	 * @throws OperException
	 *             角色名重名, 角色名为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertRole(ChuRole role) throws OperException {
		checkRoleInfo(role, null);
		roleDao.insertSelective(role);
		resetMenuList(role.getId(), Arrays.asList(role.getPrivList().split(";")));
	}
	/**
	 * 编辑角色信息
	 * @param role
	 * @param originalName 原角色名称（编辑时需要传入校验）
	 * @throws OperException
	 */
	public void updateRoleInfo(ChuRole role, String originalName) throws OperException {
		checkRoleInfo(role, originalName);
		roleDao.updateByPrimaryKeySelective(role);
		resetMenuList(role.getId(), Arrays.asList(role.getPrivList().split(";")));
	}
	
	
	
	/**
	 * 检查表单提交的参数合法性
	 * @param role
	 * @param originalName 原角色名称（编辑时需要传入校验）
	 * @throws OperException
	 */
	private void checkRoleInfo(ChuRole role,String originalName) throws OperException {
		if (role == null || StringUtils.isEmpty(role.getName())) {
			throw new OperException(角色名不能为空);
		}
		if (!checkRoleName(role.getName(),originalName)) {
			throw new OperException(角色名重名);
		}
		if(StringUtils.isEmpty(role.getPrivList())){
			throw new OperException(未给角色分配权限);
		}
	}

	/**
	 * 为指定角色添加菜单列表。先全部清空用户现有角色菜单，再批量添加。
	 * 
	 * @param roleId
	 * @param menuIdList
	 * @throws OperException
	 *             角色不存在, 未给角色分配权限
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resetMenuList(Integer roleId, List<String> menuIdList) throws OperException {
		if (roleDao.selectByPrimaryKey(roleId) == null) {
			throw new OperException(角色不存在);
		}
		if (menuIdList == null || menuIdList.size() == 0) {
			throw new OperException(未给角色分配权限);
		}

		// 先清空角色权限
		roleDao.deleteMenuList(roleId);
		// 再批量添加
		for (String menuId : menuIdList) {
			roleDao.insertMenu(roleId, Integer.parseInt(menuId));
		}
	}

	/**
	 * 获取指定角色的菜单列表
	 * 
	 * @param roleId
	 * @return
	 * @throws OperException
	 *             角色不存在
	 */
	public List<Integer> getMenuList(Integer roleId) throws OperException {
		if (roleDao.selectByPrimaryKey(roleId) == null) {
			throw new OperException(角色不存在);
		}

		return roleDao.selectMenuList(roleId);
	}

	/**
	 * 删除角色及其菜单信息
	 * 
	 * @param roleId
	 * @throws OperException
	 *             角色不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delRole(Integer roleId) throws OperException {
		if (roleDao.selectByPrimaryKey(roleId) == null) {
			throw new OperException(角色不存在);
		}

		// 先清空角色权限
		roleDao.deleteMenuList(roleId);
		// 再删除角色
		roleDao.deleteByPrimaryKey(roleId);
	}

	/**
	 * key为pid，List为该pid下属的所有菜单记录
	 * 
	 * @param menuList
	 * @return
	 */
	private Map<Integer, List<ChuMenu>> initMenuMap(List<ChuMenu> menuList) {
		Map<Integer, List<ChuMenu>> result = new HashMap<>();

		for (ChuMenu menu : menuList) {
			if (!result.containsKey(menu.getPid())) {
				List<ChuMenu> subMenuList = new ArrayList<ChuMenu>();
				subMenuList.add(menu);
				result.put(menu.getPid(), subMenuList);
			} else {
				result.get(menu.getPid()).add(menu);
			}
		}
		return result;
	}

	/**
	 * 根据角色获得其权限菜单
	 * 
	 * @param roleId
	 * @param pid
	 * @return
	 */
	public List<ChuMenu> getMenusByRole(Integer roleId, Integer pid) {
		List<ChuMenu> menuList = menuDao.selectMenuList(roleId);
		List<ChuMenu> menus = null;
		if (menuList != null && menuList.size() > 0) {
			Map<Integer, List<ChuMenu>> result = initMenuMap(menuList);
			menus = result.get(pid);
			if (menus != null && menus.size() > 0) {
				for (ChuMenu menu : menus) {
					createSubMenu(menu.getId(), result, menu);
				}
			}
		}
		return menus;
	}

	/**
	 * 生成多级菜单
	 * 
	 * @param pid
	 * @param result
	 * @param menu
	 */
	private void createSubMenu(Integer pid, Map<Integer, List<ChuMenu>> result, ChuMenu menu) {
		List<ChuMenu> childMenuList = result.get(pid);
		if (childMenuList != null && childMenuList.size() > 0) {
			menu.setChildren(new ArrayList<ChuMenu>());
			for (ChuMenu childMenu : childMenuList) {
				menu.getChildren().add(childMenu);
				if (result.containsKey(childMenu.getId())) {
					createSubMenu(childMenu.getId(), result, childMenu);
				}
			}
		}
	}

	/**
	 * 获得角色下拉框信息
	 * 
	 * @param role
	 * @return
	 */
	public List<EasyuiComboBoxItem> getRoleCombo(ChuRole role) {
		List<ChuRole> data = roleDao.selectList(role);
		List<EasyuiComboBoxItem> list = null;
		if (data != null && data.size() > 0) {
			list = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (ChuRole roleInfo : data) {
				item = new EasyuiComboBoxItem();
				item.setId(roleInfo.getId().toString());
				item.setText(roleInfo.getName());
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * 检查角色名称是否重复
	 * @param role
	 * @param originalName 原角色名称（编辑时需要传入校验）
	 * @return 不重复返回true，重复返回false
	 */
	public boolean checkRoleName(String newName, String originalName) {
		if (StringUtils.isEmpty(newName) || newName.equals(originalName)) {
			return true;
		}
		ChuRole role = new ChuRole();
		role.setName(newName);
		int count = countRoleList(role);
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	

}
