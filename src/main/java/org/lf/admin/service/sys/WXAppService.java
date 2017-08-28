package org.lf.admin.service.sys;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuTagMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.dao.ChuZTActionMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuTag;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.ChuZTAction;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.wx.WXAppEvent;
import org.lf.utils.wx.WXAppHandler;
import org.lf.utils.wx.WXAppListener;
import org.lf.wx.user.Department;
import org.lf.wx.user.DepartmentManager;
import org.lf.wx.user.Tag;
import org.lf.wx.user.TagManager;
import org.lf.wx.user.User;
import org.lf.wx.user.UserManager;
import org.lf.wx.utils.WXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Service("appService")
public class WXAppService {
	private static final Logger logger = LoggerFactory.getLogger(WXAppService.class);
	public static final OperErrCode 应用名不能为空 = new OperErrCode("10201", "应用名不能为空");
	public static final OperErrCode 企业号不能为空 = new OperErrCode("10202", "企业号不能为空");
	public static final OperErrCode 企业名不能为空 = new OperErrCode("10203", "企业名不能为空");
	public static final OperErrCode 企业Lego不能为空 = new OperErrCode("10204", "企业Lego不能为空");
	public static final OperErrCode 应用ID不能为空 = new OperErrCode("10205", "应用ID不能为空");
	public static final OperErrCode SECRET不能为空 = new OperErrCode("10206", "SECRET不能为空");
	public static final OperErrCode TOKEN不能为空 = new OperErrCode("10207", "TOKEN不能为空");
	public static final OperErrCode AESKEY不能为空 = new OperErrCode("10208", "AESKEY不能为空");
	public static final OperErrCode 回调模式URL不能为空 = new OperErrCode("10209", "回调模式URL不能为空");
	public static final OperErrCode 服务器URL不能为空 = new OperErrCode("10210", "服务器URL不能为空");
	
	public static final OperErrCode 微信应用不存在 = new OperErrCode("10211", "微信应用不存在");
	public static final OperErrCode 微信应用已停用 = new OperErrCode("10212", "微信应用已停用");
	public static final OperErrCode 微信服务器无法连接 = new OperErrCode("10213", "微信服务器无法连接");
	public static final OperErrCode 无法获取微信用户标签 = new OperErrCode("10214", "无法获取微信用户标签");
	public static final OperErrCode 无法获取微信用户 = new OperErrCode("10215", "无法获取微信用户");
	public static final OperErrCode 无法获取微信组织架构 = new OperErrCode("10216", "无法获取微信组织架构");
	
	@Autowired
	private ChuAppMapper appDao;
	
	@Autowired
	private ChuTagMapper tagDao;
	
	@Autowired
	private ChuWXDeptMapper wxDeptDao;
	
	@Autowired
	private CZCGLMapper zcglDao;
	
	@Autowired
	private ChuWXUserMapper wxUserDao;
	
	@Autowired
	private ChuZTActionMapper ztActionDao;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	private WXAppHandler handler = new WXAppHandler();
	
	/**
	 * 添加一个微信应用, 
	 * @param app
	 * @param start 如果为true，启动微信应用线程。否则不启动
	 * @throws OperException 应用名不能为空，企业号不能为空，企业名不能为空，企业Lego不能为空，应用ID不能为空，
	 *                       SECRET不能为空，TOKEN不能为空，AESKEY不能为空，回调模式URL不能为空，服务器URL不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertApp(ChuApp app) throws OperException {
		if (StringUtils.isEmpty(app.getAppName())) {
			throw new OperException(应用名不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpId())) {
			throw new OperException(企业号不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpName())) {
			throw new OperException(企业名不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpIcon())) {
			throw new OperException(企业Lego不能为空);
		}
		if (app.getAgentId() == null) {
			throw new OperException(应用ID不能为空);
		}
		if (StringUtils.isEmpty(app.getSecret())) {
			throw new OperException(SECRET不能为空);
		}
		if (StringUtils.isEmpty(app.getToken())) {
			throw new OperException(TOKEN不能为空);
		}
		if (StringUtils.isEmpty(app.getAesKey())) {
			throw new OperException(AESKEY不能为空);
		}
		if (StringUtils.isEmpty(app.getCallbackUrl())) {
			throw new OperException(回调模式URL不能为空);
		}
		if (StringUtils.isEmpty(app.getServerUrl())) {
			throw new OperException(服务器URL不能为空);
		}
		app.setEncrypt(1);  // 企业号总是处于加密模式下
		app.setStop(0);  // 默认情况下，新建的应用是启动的
		
		ChuApp param = new ChuApp();
		param.setCorpId(app.getCorpId());
		param.setAgentId(app.getAgentId());
		ChuApp a = appDao.select(param);
		if (a != null) {
			throw new OperException(new OperErrCode("10299", "微信应用已存在, 企业号：%s，应用号：%s", app.getCorpId(), app.getAgentId()));
		}
		
		appDao.insert(app);
		ChuApp newApp = getApp(app.getCorpId(), app.getAgentId()); 
		if (app.getStop() == 0) {
			handler.addListener(new WXAppListener(new WXAppEvent(newApp)));
		}
	}
	
	/**
	 * 添加一个微信应用
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertApp(HttpSession session, String corpId, String corpName, String appName, Integer agentId, String secret, String token, String aesKey,
			String callbackUrl, String serverUrl,MultipartFile[] FileList) throws OperException{
		ChuApp param = new ChuApp();
		param.setAesKey(aesKey);
		param.setAgentId(agentId);
		param.setAppName(appName);
		param.setCallbackUrl(callbackUrl);
		param.setCorpId(corpId);
		param.setCorpName(corpName);
		param.setServerUrl(serverUrl);
		param.setToken(token);
		param.setSecret(secret);
		
		// 保存文件路径前缀
		String prePath = ZCGLProperties.URL_MEDIA_TARGET_DIR + "/logo";
		// 保存文件类型
		String fileType = WXMediaService.IMAGE_SUFFIX;
		String iconPath = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_LOGO_SIZE, fileType, FileList);
		if (iconPath!=null) {
			param.setCorpIcon(iconPath);
		}
		insertApp(param);
	}
	
	/**
	 * 通过企业号和应用号获取App
	 * @param corpId
	 * @param agentId
	 * @return
	 * @throws OperException 企业号不能为空, 应用ID不能为空, 微信应用不存在
	 */
	public ChuApp getApp(String corpId, Integer agentId) throws OperException {
		if (StringUtils.isEmpty(corpId)) {
			throw new OperException(企业号不能为空);
		}
		if (agentId == null) {
			throw new OperException(应用ID不能为空);
		}
		
		ChuApp param = new ChuApp();
		param.setCorpId(corpId);
		param.setAgentId(agentId);
		ChuApp app = appDao.select(param);
		if (app == null) {
			throw new OperException(微信应用不存在);
		}
		
		return app;
	}
	
	/**
	 * 微信应用状态下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWxAppZtCombo() {
		WXZT[] list = WXZT.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (WXZT wxzt : list) {
				item = new EasyuiComboBoxItem();
				item.setId(wxzt.getValue() + "");
				item.setText(wxzt.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	
	/**
	 * 根据应用号查找
	 * @param appId
	 * @return
	 * @throws OperException 微信应用不存在
	 */
	public ChuApp getApp(Integer appId) throws OperException {
		ChuApp app = appDao.selectByPrimaryKey(appId);
		if (app == null) {
			throw new OperException(微信应用不存在);
		}
		
		return app;
	}
	
	/**
	 * 获取指定页的应用。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 * @throws OperException 
	 */
	public List<ChuApp> getAppList(int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		
		return appDao.selectAppList(pn.getStart(), pn.getOffset());
	}
	
	/**
	 * 根据条件获取所有应用
	 * @return
	 */
	public List<ChuApp> getAppList(ChuApp param) {
		return appDao.selectList(param);
	}
	
	/**
	 * 得到应用数。
	*/
	public Integer countAppList(ChuApp param) {
		return appDao.countAppList(param);
	}
	
	/**
	 * 获取指定页的微信应用
	 * 
	 * @param rows
	 * @param page
	 * @return
	 */
	public EasyuiDatagrid<ChuApp> getPagedAppList(int rows, int page) {
		int total = countAppList(null);

		EasyuiDatagrid<ChuApp> pageDatas = new EasyuiDatagrid<ChuApp>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<ChuApp>());
		} else {
			List<ChuApp> userList = getAppList(rows, page);
			pageDatas.setRows(userList);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 获得所有企业下拉框信息
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWxAppCombo() {
		List<ChuApp> list = getAppList(null);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (ChuApp app : list) {
				item = new EasyuiComboBoxItem();
				item.setId(app.getAppId().toString());
				item.setText(app.getCorpName());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 更新一个微信应用
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateApp(HttpSession session, String appId, String corpId, String corpName, String appName, Integer agentId, String secret,
			String token, String aesKey, String callbackUrl, String serverUrl, String stop, MultipartFile[] FileList) throws OperException {
		ChuApp param = appDao.selectByPrimaryKey(Integer.parseInt(appId));
		param.setAesKey(aesKey);
		param.setAgentId(agentId);
		param.setAppName(appName);
		param.setCallbackUrl(callbackUrl);
		param.setCorpId(corpId);
		param.setCorpName(corpName);
		param.setServerUrl(serverUrl);
		param.setToken(token);
		param.setStop(Integer.parseInt(stop));
		param.setSecret(secret);

		// 保存文件路径前缀
		String prePath = ZCGLProperties.URL_MEDIA_TARGET_DIR + "/logo";
		// 保存文件类型
		String fileType = WXMediaService.IMAGE_SUFFIX;
		String iconPath = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_LOGO_SIZE, fileType, FileList);
		if (iconPath != null) {
			param.setCorpIcon(iconPath);
		}
		updateApp(param);
	}

	
	/**
	 * 更新应用
	 * @param app
	 * @throws OperException 微信应用不存在, 无法连接微信服务器
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateApp(ChuApp app) throws OperException {
		if (StringUtils.isEmpty(app.getAppName())) {
			throw new OperException(应用名不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpId())) {
			throw new OperException(企业号不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpName())) {
			throw new OperException(企业名不能为空);
		}
		if (StringUtils.isEmpty(app.getCorpIcon())) {
			throw new OperException(企业Lego不能为空);
		}
		if (app.getAgentId() == null) {
			throw new OperException(应用ID不能为空);
		}
		if (StringUtils.isEmpty(app.getSecret())) {
			throw new OperException(SECRET不能为空);
		}
		if (StringUtils.isEmpty(app.getToken())) {
			throw new OperException(TOKEN不能为空);
		}
		if (StringUtils.isEmpty(app.getAesKey())) {
			throw new OperException(AESKEY不能为空);
		}
		if (StringUtils.isEmpty(app.getCallbackUrl())) {
			throw new OperException(回调模式URL不能为空);
		}
		if (StringUtils.isEmpty(app.getServerUrl())) {
			throw new OperException(服务器URL不能为空);
		}
		
		appDao.updateByPrimaryKeySelective(app);
		
		// 如果微信应用已经启用则停止，否则不进行操作
		if (handler.threadIsAlive(app.getAppId())) {
			handler.removeListener(app.getAppId());
		}
		// stop为0则启用微信服务
		if (app.getStop() == 0) {
			handler.addListener(new WXAppListener(new WXAppEvent(app)));
			connWXServer(app.getAppId());
		}
		
	}
	
	/**
	 * 删除应用
	 * @param app
	 * @throws OperException 微信应用不存在
	 */
	public void delApp(Integer appId) throws OperException {
		ChuApp app = getApp(appId);
		// 停止微信应用服务 判断应用开启状态
		if (app.getStop() == 0) {
			handler.removeListener(app.getAppId());
		}
		
		appDao.deleteByPrimaryKey(appId);
		
		// 级联删除该应用下属的标签、微信用户和组织架构信息。
		tagDao.deleteByAppId(appId);
		wxUserDao.deleteByAppId(appId);
		wxDeptDao.deleteByAppId(appId);
	}
	
	/**
	 * 启动所有App应用线程
	 */
	public void startAppList() {
		// 得到所有应用
		// 获得stop为0的已启用的应用
		ChuApp tempApp = new ChuApp();
		tempApp.setStop(WXZT.启用.getValue());
		List<ChuApp> appList = getAppList(tempApp);
		
		for (ChuApp app : appList) {
			handler.addListener(new WXAppListener(new WXAppEvent(app)));
		}
	}
	
	/**
	 * 开启一个后台微信应用线程
	 * 
	 * @param appId
	 */
	public void startApp(Integer appId) {
		ChuApp app = appDao.selectByPrimaryKey(appId);
		handler.addListener(new WXAppListener(new WXAppEvent(app)));
	}
	
	/**
	 * 关闭一个微信应用线程
	 * 
	 * @param appId
	 */
	public void stopApp(Integer appId) {
		handler.removeListener(appId);
	}
	
	/**
	 * 连接微信服务器，获取AccessToken。系统会尝试连接三次，每次间隔2秒。如果仍然无法连接成功，报微信服务器无法连接。
	 * @param appId
	 * @exception 微信服务器无法连接
	 */
	public void connWXServer(Integer appId) throws OperException { 
		WXAppEvent event = handler.getListener(appId).getEvent();
		
		int count = 0;
		int totalCount = 3;
		while (event.getAccessToken() == null) {
			// 等待应用线程启动成功，能够读取accessToken。沉睡2s
			try {
				count++;
				Thread.currentThread();
				Thread.sleep(2000);
				
				if (count > totalCount) {
					throw new OperException(微信服务器无法连接);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public String getAccessToken(Integer appId) throws OperException {
		ChuApp app = getApp(appId);
		if (app.getStop() == 1) {
			throw new OperException(微信应用已停用);
		}
		
		connWXServer(appId);
		
		WXAppEvent event = handler.getListener(appId).getEvent();
		return event.getAccessToken();
	}
	
	public String getJsapiTicket(Integer appId) throws OperException {
		ChuApp app = getApp(appId);
		if (app.getStop() == 1) {
			throw new OperException(微信应用已停用);
		}
		
		connWXServer(appId);
		
		WXAppEvent event = handler.getListener(appId).getEvent();
		return event.getJsapiTicket();
	}
	
	/**
	 * 当新增一个应用时，我们需要在chu_zt_action中为他创建一组微信端操作配置记录。
	 * 这里，我们以appId(17)为模板， 在chu_zt_action中插入一组记录。记录的内容与appId(17)相同
	 * 
	 * @param appId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void syncZTAction(Integer appId) {
		ChuZTAction param = new ChuZTAction();
		param.setAppId(Integer.parseInt(ZCGLProperties.TEMPLATE_APPID));
		List<ChuZTAction> ztActionList = ztActionDao.selectList(param);
		
		// 删除appId对应的原有记录
		ztActionDao.deleteByAppID(appId);
		
		ChuZTAction newAction;
		for (ChuZTAction ztAction : ztActionList) {
			newAction = new ChuZTAction();
			newAction.setAppId(appId);
			newAction.setTagName(ztAction.getTagName());
			newAction.setZt(ztAction.getZt());
			newAction.setImgUrl(ztAction.getImgUrl());
			newAction.setActionTitle(ztAction.getActionTitle());
			newAction.setActionName(ztAction.getActionName());
			newAction.setQueryParam(ztAction.getQueryParam());
			ztActionDao.insertSelective(newAction);
		}
	}
	
	/**
	 * 依据app信息连接微信服务器，同步CHU_TAG标签表，
	 * 返回一个Map<String, List<String>>。   key为userid, value为tagNo List。
	 * 
	 * 此操作非常耗费资源。请用户谨慎使用。
	 * @param app
	 * @throws OperException 无法获取微信用户标签，无法获取微信用户， 微信应用已停用
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, List<Integer>> syncTag(Integer appId) throws OperException {
		Map<String, List<Integer>> map = new HashMap<>();
		
		List<Tag> tagList = null;
		JSONArray userList = null;
		String userid;
		
		List<ChuTag> cTagList = new ArrayList<>();
		ChuTag cTag = null;
		try {
			tagList = TagManager.getTagList(getAccessToken(appId));
			
			for (Tag tag : tagList) {
				// 创建cTag
				cTag = new ChuTag();
				cTag.setAppId(appId);
				cTag.setTagNo(new Integer(tag.getTagid()));
				cTag.setTagName(tag.getTagname());
				cTagList.add(cTag);
				
				// 依次获取指定标签对应的微信用户详细信息
				userList = TagManager.getUserList(getAccessToken(appId), tag.getTagid()).getJSONArray(TagManager.USERLIST);
				for (int i = 0; i < userList.size(); i++) {
					userid =userList.getJSONObject(i).getString(User.USERID);
					
					// 判断user是否已存在。
					if (map.containsKey(userid)) {
						//已存在微信用户，更新其tagNo即可。
						map.get(userid).add(new Integer(tag.getTagid()));
					} else {
						//在map中新增用户
						map.put(userid, new ArrayList<Integer>());
						map.get(userid).add(new Integer(tag.getTagid()));
					}
				}
				
			}
		} catch (WXException e) {
			throw new OperException(无法获取微信用户标签);
		}
		
		// 更新CHU_TAG
		tagDao.deleteByAppId(appId);
		tagDao.insertAll(cTagList);
		
		return map;
	}

	/**
	 * 依据app信息连接微信服务器，同步CHU_WXDEPT和CHU_WXUSER表。
	 * 此操作非常耗费资源。请用户谨慎使用。
	 * @param app
	 * @throws OperException 无法获取微信组织架构, 微信应用已停用
	 */
	@Transactional(rollbackFor = Exception.class)
	public void syncDepartmentsAndWXUsers(Integer appId, Map<String, List<Integer>> map) throws OperException {
		List<Department> deptList = null;
		ChuWXUser wxUser = null;
		Map<String, ChuWXUser> wxUserMap = new HashMap<>();
		try {
			// 获取所有部门信息
			deptList = DepartmentManager.getDepartmentList(getAccessToken(appId), 0);
			
			List<ChuWXDept> wxDeptList = new ArrayList<>();
			ChuWXDept wxDept;
			for (Department dept : deptList) {
				wxDept = new ChuWXDept();
				wxDept.setAppId(appId);
				wxDept.setDeptNo(dept.getId());
				wxDept.setDeptName(dept.getName());
				wxDept.setDeptOrder(dept.getOrder());
				wxDept.setDeptPno(dept.getParentid());
				wxDeptList.add(wxDept);
				
				// 查询c_zcgl表，看其中app_id，dept_no是否已存在。如果不存在，插入一条记录。如果存在，修改deptname。
				CZCGL param = new CZCGL();
				param.setAppId(appId);
				param.setDeptNo(dept.getId());
				if (zcglDao.countZcglList(param) == 1) {
					param.setDeptName(dept.getName());
					zcglDao.updateByPrimaryKeySelective(param);
				}else{
					param.setDeptName(dept.getName());
					zcglDao.insert(param);
				}
				
				// 查找部门下属所有的用户
				List<User> userList = UserManager.getUserList(getAccessToken(appId), dept.getId());
				for (User user : userList) {
					wxUser = new ChuWXUser();
					wxUser.setUserid(user.getUserid());
					wxUser.setName(user.getName());
					wxUser.setDepartment(user.getDepartment());
					wxUser.setPosition(user.getPosition());
					wxUser.setMobile(user.getMobile());
					if (user.getGender() != null) {
						wxUser.setGender(new Integer(user.getGender().getValue()));
					}
					wxUser.setEmail(user.getEmail());
					wxUser.setWeixinid(user.getWeixinid());
					wxUser.setAvatar(user.getAvatar());
					if (user.getStatus() != null) {
						wxUser.setStatus(new Integer(user.getStatus().getValue()));
					}
					wxUser.setExtattr(user.getExtattr());
					wxUser.setAppId(appId);
					List<Integer> tagNoList = map.get(user.getUserid());
					wxUser.setTagNo(tagNoList == null ? "" : JSON.toJSONString(tagNoList));
					wxUserMap.put(wxUser.getUserid(), wxUser);
				}
			}
			
			// 删除部门数据和CHU_WXUser表
			wxDeptDao.deleteByAppId(appId);
			wxUserDao.deleteByAppId(appId);
			
			// 批量插入数据库
			wxDeptDao.insertAll(wxDeptList);
			List<ChuWXUser> list = new ArrayList<>();
			list.addAll(wxUserMap.values());
			wxUserDao.insertAll(list);
		} catch (WXException e) {
			throw new OperException(无法获取微信组织架构);
		}
	}
	
	public List<Integer> getAppThreadList() {
		return handler.getAppThreadList();
	}
	
	/**
	 * 生成jssdk的config数据
	 * 
	 * @param chuAppId
	 * @param pageUrl
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getJssdkConfig(Integer chuAppId, String requestUrl) {
		Map<String, Object> ret = null;
		if (chuAppId == null || StringUtils.isEmpty(requestUrl)) {
			return ret;
		}
		try {
			ChuApp app = appDao.selectByPrimaryKey(chuAppId);
			if (app == null || StringUtils.isEmpty(app.getCorpId())) {
				return ret;
			}
			String appId = app.getCorpId(); // 必填，企业微信的cropID
			String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
			String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串

			// 获取签名
			String signature = "";
			// 企业号签名
			String sign = "jsapi_ticket=" + getJsapiTicket(chuAppId) + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url="
					+ requestUrl;
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(sign.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
			ret = new HashMap<String, Object>();
			ret.put("appId", appId);
			ret.put("timestamp", timestamp);
			ret.put("nonceStr", nonceStr);
			ret.put("signature", signature);
			return ret;
		} catch (Exception e) {
			logger.error("获取jssdk配置信息失败", e);
			return null;
		}
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
