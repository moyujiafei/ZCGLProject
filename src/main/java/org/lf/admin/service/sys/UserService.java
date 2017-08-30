package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuUserMapper;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.NumberUtils;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.lf.utils.servlet.LoginInterceptor;
import org.lf.wx.utils.WXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service("userService")
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	public static final OperErrCode 用户不存在 = new OperErrCode("10001", "用户不存在");
	public static final OperErrCode 用户账号不存在 = new OperErrCode("10002", "用户账号不存在");
	public static final OperErrCode 用户账号或密码错误 = new OperErrCode("10003", "用户账号或密码错误");
	public static final OperErrCode 用户帐号重名 = new OperErrCode("10004", "用户帐号重名");
	public static final OperErrCode 用户帐号不能为空 = new OperErrCode("10005", "用户帐号不能为空");
	public static final OperErrCode 用户密码不能为空 = new OperErrCode("10006", "用户密码不能为空");
	public static final OperErrCode 用户名不能为空 = new OperErrCode("10007", "用户名不能为空");
	public static final OperErrCode 老密码不正确 = new OperErrCode("10008", "老密码不正确");
	public static final OperErrCode 重置密码失败 = new OperErrCode("10009", "重置密码失败");
	public static final OperErrCode 企业号主体不能为空 = new OperErrCode("10010", "企业号主体不能为空");
	public static final OperErrCode 绑定微信账号失败 = new OperErrCode("10011", "绑定微信账号失败");
	@Autowired
	private ChuUserMapper userDao;
	@Autowired
	private ChuAppMapper chuAppDao;

	@Autowired
	private WXAppService wXAppService;

	public static final String DEFAULT_PASSWD = "123456";

	/**
	 * 微信登录验证state，用于防止csrf攻击（跨站请求伪造攻击）
	 */
	public static final String WX_LOGIN_STATE = "wx_login_state";

	/**
	 * 用于绑定微信号到后台管理账户，userId
	 */
	public static final String BIND_WXUSER_NAME = "bind_wxUser_name";
	/**
	 * 用于绑定微信号到后台管理账户，appId
	 */
	public static final String BIND_WXUSER_APPID = "bind_wxUser_appid";
	/**
	 * 获取微信用户信息
	 */
	private static final String WX_LOGIN_USERINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";

	/**
	 * 获取角色role_id对应的用户列表
	 */
	public List<ChuUser> getUserList(Integer role_id) throws OperException {
		ChuUser params = new ChuUser();
		params.setRoleId(role_id);
		return userDao.selectList(params);
	}

	/**
	 * 获取指定分页用户列表
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 * @throws OperException
	 */
	public EasyuiDatagrid<ChuUser> getPageUserList(int rows, int page) {
		int total = countUserList(null);

		EasyuiDatagrid<ChuUser> pageDatas = new EasyuiDatagrid<ChuUser>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<ChuUser>());
		} else {
			List<ChuUser> userList = getUserList(rows, page);
			pageDatas.setRows(userList);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}

	/**
	 * 获取指定页的用户。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 * @throws OperException
	 */
	public List<ChuUser> getUserList(int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);

		return userDao.selectUserList(pn.getStart(), pn.getOffset());
	}

	/**
	 * 根据条件查询用户数量
	 * 
	 * @param params
	 *            为null时表示查询所有用户数量
	 * @return
	 */
	public Integer countUserList(ChuUser params) {
		return userDao.countUserList(params);
	}

	/**
	 * 查询当前用户信息,存放入session中
	 * 
	 * @param session
	 * @return
	 */
	public void getUser(HttpSession session) throws OperException {
		ChuUser user = (ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
		if (user != null) {
			user = getUser(user.getUname());
			session.setAttribute(LoginInterceptor.LOGIN_INFO, user);
		}
	}

	/**
	 * 通过ID找到某个ChuUser
	 * 
	 * @param userId
	 * @return
	 * @throws OperException
	 *             用户不存在
	 */
	public ChuUser getUser(Integer userId) throws OperException {
		ChuUser user = userDao.selectByPrimaryKey(userId);
		if (user == null) {
			throw new OperException(用户不存在);
		}
		return user;
	}

	/**
	 * 
	 * @param uname
	 * @return
	 * @throws OperException
	 *             用户账号不存在
	 */
	public ChuUser getUser(String uname) throws OperException {
		ChuUser params = new ChuUser();
		params.setUname(uname);
		ChuUser user = userDao.select(params);

		if (user == null) {
			throw new OperException(用户账号不存在);
		}

		return user;
	}

	/**
	 * 通过用户账号和密码找到某个ChuUser
	 * 
	 * @param uname
	 * @param password
	 * @param appId
	 * @param isSuperAdmin
	 * @return
	 * @throws OperException
	 *             用户账号不能为空， 用户密码不能为空，用户账号不存在，用户密码不正确
	 */
	public ChuUser getUser(String uname, String password, Integer appId, Boolean isSuperAdmin) throws OperException {
		if (StringUtils.isEmpty(uname)) {
			throw new OperException(用户帐号不能为空);
		}
		if (StringUtils.isEmpty(password)) {
			throw new OperException(用户密码不能为空);
		}
		ChuUser cu = null;
		if (isSuperAdmin == null || !isSuperAdmin) {
			// 普通用户
			if (appId == null) {
				throw new OperException(企业号主体不能为空);// 登录时，appId不能为空
			}
			ChuUser params = new ChuUser();
			params.setUname(uname);
			params.setAppId(appId);
			cu = userDao.selectAllInfo(params);
		} else {
			// 超级用户
			cu = userDao.selectSuperAdminInfo(uname);
		}

		if (cu == null) {
			throw new OperException(用户账号或密码错误);
		}
		String salt = cu.getSalt();
		String upw = StringUtils.toMD5(password + salt);
		if (!cu.getUpw().equals(upw)) {
			throw new OperException(用户账号或密码错误);
		}

		return cu;
	}

	/**
	 * 实现ChuUser的更新
	 * 
	 * @param newuser
	 * @param originalUname
	 * @param isSuperAdmin
	 * @param loginUserAppid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(ChuUser newuser, String originalUname, Boolean isSuperAdmin, Integer loginUserAppid) throws OperException {
		newuser = checkUserInfo(newuser, originalUname, isSuperAdmin, loginUserAppid);
		userDao.updateByPrimaryKeySelective(newuser);
	}

	/**
	 * 向chu_user中添加一条数据
	 * 
	 * @param newUser
	 * @param isSuperAdmin
	 * @param loginUserAppid
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertUser(ChuUser newUser, Boolean isSuperAdmin, Integer loginUserAppid) throws OperException {
		newUser = checkUserInfo(newUser, null, isSuperAdmin, loginUserAppid);
		if (StringUtils.isEmpty(newUser.getUpw())) {
			throw new OperException(用户密码不能为空);
		}
		String salt = String.valueOf(NumberUtils.getRandomNum(100000, 999999));
		newUser.setUpw(StringUtils.toMD5(newUser.getUpw() + salt));
		newUser.setSalt(salt);
		userDao.insert(newUser);
	}

	/**
	 * 用户表单信息验证
	 * 
	 * @param newUser
	 * @param originalUname
	 *            原用户账号（编辑时需要传入校验）
	 * @param isSuperAdmin
	 * @param loginUserAppid
	 * @throws OperException
	 */
	private ChuUser checkUserInfo(ChuUser newUser, String originalUname, Boolean isSuperAdmin, Integer loginUserAppid) throws OperException {
		if (newUser == null || StringUtils.isEmpty(newUser.getUname())) {
			throw new OperException(用户帐号不能为空);
		}
		if (newUser.getAppId() == null) {
			if (isSuperAdmin != null && isSuperAdmin) {
				// 超级管理员appId不能为空
				throw new OperException(企业号主体不能为空);
			} else {
				// 普通用户,appId从session中取
				if (loginUserAppid == null) {
					throw new OperException(企业号主体不能为空);
				}
				newUser.setAppId(loginUserAppid);
			}
		}
		if (!checkUname(newUser.getUname(), originalUname)) {
			throw new OperException(用户帐号重名);
		}
		if (StringUtils.isEmpty(newUser.getName())) {
			throw new OperException(用户名不能为空);
		}
		return newUser;
	}

	/**
	 * 功能：在chu_user中删除一条记录
	 * 
	 * @exception 用户不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delUser(Integer uid) throws OperException {
		if (userDao.deleteByPrimaryKey(uid) != 1) {
			throw new OperException(用户不存在);
		}
	}

	/**
	 * 功能： 重置密码
	 * 
	 * @exception 用户不存在
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resetPasswd(Integer uid) throws OperException {
		ChuUser user = getUser(uid);
		if (user == null) {
			throw new OperException(用户不存在);
		}
		String newpwd = StringUtils.toMD5(DEFAULT_PASSWD + user.getSalt());

		user.setUpw(newpwd);
		if (userDao.updateByPrimaryKeySelective(user) == 0) {
			throw new OperException(重置密码失败);
		}

	}

	/**
	 * 功能：修改密码
	 * 
	 * @param user
	 * @param old_password
	 * @param new_password
	 * @exception 用户不存在
	 *                ，老密码不正确，用户密码不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changePasswd(ChuUser user, String old_password, String new_password) throws OperException {
		if (user == null) {
			throw new OperException(用户不存在);
		}
		if (StringUtils.isEmpty(new_password)) {
			throw new OperException(用户密码不能为空);
		}

		String salt = user.getSalt();
		String oldpwd = StringUtils.toMD5(old_password + salt);
		if (!oldpwd.equalsIgnoreCase(user.getUpw())) {
			throw new OperException(老密码不正确);
		}

		String newpwd = StringUtils.toMD5(new_password + salt);
		user.setUpw(newpwd);
		userDao.updateByPrimaryKeySelective(user);
	}

	/**
	 * 根据uid修改用户密码
	 * 
	 * @param uid
	 * @param oldpwd
	 * @param newpwd
	 * @throws OperException
	 */
	public void changePasswdByUid(Integer uid, String oldpwd, String newpwd) throws OperException {
		ChuUser user = userDao.selectByPrimaryKey(uid);
		changePasswd(user, oldpwd, newpwd);
	}

	/**
	 * 检查用户账号是否存在
	 * 
	 * @param newUname
	 * @param originalUname
	 *            原用户账号（编辑时需要传入校验）
	 * @return 不重复返回true，重复返回false
	 */
	public boolean checkUname(String newUname, String originalUname) {
		if (StringUtils.isEmpty(newUname) || newUname.equals(originalUname)) {
			return true;
		}
		ChuUser params = new ChuUser();
		params.setUname(newUname);
		Integer count = countUserList(params);
		if (count == null || count == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 登录成功后保存登录信息
	 * 
	 * @param session
	 * @param user
	 */
	public void saveLoginInfo(HttpSession session, ChuUser user) {
		session.setAttribute(LoginInterceptor.LOGIN_INFO, user);
	}

	/**
	 * 获取企业号信息用于二维码登录
	 * 
	 * @param flag
	 *            如果flag不存在则不执行操作，防止盗链查询
	 * @param appId
	 * @param state
	 * @return
	 */
	public JSONObject getCorpInfo(String flag, Integer appId, String state) {
		JSONObject result = new JSONObject();
		if (!StringUtils.isEmpty(flag) && appId != null) {
			ChuApp app = chuAppDao.selectByPrimaryKey(appId);
			if (app != null) {
				result.put("appid", app.getCorpId());
				result.put("agentid", app.getAgentId());
				result.put("state", state);
				return result;
			}
		}
		result.put("error", "error");
		return result;
	}

	/**
	 * 企业微信登录，成功后重定向到home
	 * 
	 * @param response
	 * @param session
	 * @param code
	 * @param state
	 * @param appId
	 */
	public void wxLogin(HttpServletResponse response, HttpSession session, String code, String state, Integer appId) {
		if (!StringUtils.isEmpty(code) && state != null && appId != null) {
			if (!state.equals((String) session.getAttribute(WX_LOGIN_STATE))) {
				return;
			}
			try {
				String tockenUrl = String.format(WX_LOGIN_USERINFO, wXAppService.getAccessToken(appId), code);
				String result = WXUtils.downloadString(tockenUrl, "GET", null);
				JSONObject json = JSON.parseObject(result);
				String wxUsername = json.getString("UserId");
				if (!StringUtils.isEmpty(wxUsername)) {
					ChuUser user = new ChuUser();
					user.setAppId(appId);
					user.setWxUsername(wxUsername);
					user = userDao.select(user);
					if (user != null) {
						saveLoginInfo(session, user);
						response.sendRedirect(ZCGLProperties.URL_SERVER + "/console/home.do");// 重定向到home
					} else {
						// 绑定账号
						session.setAttribute(BIND_WXUSER_NAME, wxUsername);
						session.setAttribute(BIND_WXUSER_APPID, appId);
						response.sendRedirect(ZCGLProperties.URL_SERVER + "/console/user/bindWxUserUI.do");// 重定向到账号绑定界面
					}
				}
			} catch (Exception e) {
				logger.error("微信登录失败", e);
			}
		}
	}

	/**
	 * 绑定微信账号到对应的管理后台账号
	 * 
	 * @param session
	 * @param uname
	 * @param upw
	 * @return
	 * @throws OperException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void bindWxUser(HttpSession session, String uname, String upw) throws OperException {
		String wxUserName = (String) session.getAttribute(BIND_WXUSER_NAME);
		// wxUserName = "shangwei";
		if (StringUtils.isEmpty(wxUserName)) {
			throw new OperException(绑定微信账号失败);
		}
		Integer appId = (Integer) session.getAttribute(BIND_WXUSER_APPID);
		// appId = 17;
		ChuUser user = getUser(uname, upw, appId, (Boolean) session.getAttribute(LoginInterceptor.IS_SUPER_ADMIN));
		if (user != null) {
			try {
				user.setWxUsername(wxUserName);
				userDao.updateByPrimaryKeySelective(user);
				saveLoginInfo(session, user);
			} catch (Exception e) {
				throw new OperException(绑定微信账号失败);
			}
		}
	}

}
