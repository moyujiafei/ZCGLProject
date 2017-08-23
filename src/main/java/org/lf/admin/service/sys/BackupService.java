package org.lf.admin.service.sys;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lf.admin.db.dao.ChuAppMapper;
import org.lf.admin.db.dao.ChuBackupMapper;
import org.lf.admin.db.dao.ChuUserMapper;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuBackup;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("backupService")
public class BackupService {
	
	private static final Logger logger = LoggerFactory.getLogger(BackupService.class);
	
	@Autowired
	private ChuBackupMapper chuBackupDao;
	
	@Autowired
	private ChuAppMapper chuAppDao;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private ChuUserMapper chuUserDao;
		
	public static final OperErrCode 普通用户仅能保留一个备份 = new OperErrCode("10201", "普通用户仅能保留一个备份");
	public static final OperErrCode 普通用户每月仅能备份一次 = new OperErrCode("10201", "普通用户每月仅能备份一次");
	public static final OperErrCode 高级用户每月仅能备份一次 = new OperErrCode("10201", "高级用户每月仅能备份一次");
	public static final OperErrCode 备份操作失败 = new OperErrCode("10201", "备份操作失败");
	public static final OperErrCode 文件下载异常 = new OperErrCode("10201", "文件下载异常");
		
	/**
	 * 依据ZCGLProperties，生成.bat文件
	 * @throws OperException 
	 */
	private void generateBackupScript(Integer appId) throws OperException {
		ChuUser chuUser = new ChuUser();
		chuUser.setAppId(appId);
		String baseUrl = getRootPath();
    	String db_ip = ZCGLProperties.DB_IP;
    	String db_port = ZCGLProperties.DB_PORT;
		String db_username = ZCGLProperties.DB_USERNAME; 
		String db_password = ZCGLProperties.DB_PASSWORD;
		String db_name = ZCGLProperties.DB_NAME;
		String corp_name = appService.getApp(appId).getCorpName(); //从数据库中chu_app表里取 select corp_name from chu_app where app_id=17	
		String url_backup_path =  ZCGLProperties.URL_BACKUP_PATH;
		String url_zclx_target_dir = ZCGLProperties.URL_ZCLX_TARGET_DIR.replace("/", "\\");
		String url_zc_target_dir = ZCGLProperties.URL_ZC_TARGET_DIR.replace("/", "\\");
		String url_media_target_dir = ZCGLProperties.URL_MEDIA_TARGET_DIR.replace("/", "\\");
		
		String cmd="REM 备份数据，及其相关图片数据" + "\r\n" + "\r\n" +
				   "REM 读取系统日期" + "\r\n" +
				   "set ip=" + db_ip + "\r\n" + 
		           "set port=" + db_port + "\r\n" +
				   "set username=" + db_username + "\r\n" +
				   "set password=" + db_password + "\r\n" +
				   "set database=" + db_name + "\r\n" +
				   "set today=%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%" + "\r\n" +
				   "set corp_name=" + corp_name + "\r\n" +
				   "set app_id=" + appId + "\r\n" +
				   "set backup_path=" + baseUrl + "\\" + url_backup_path + "\r\n" + 
				   "set zclx_img_backup_path=" + baseUrl + url_zclx_target_dir + "\r\n" +
				   "set zc_img_backup_path=" + baseUrl + url_zc_target_dir + "\r\n" +
				   "set zczt_medias_backup_path=" + baseUrl + url_media_target_dir + "\r\n" + "\r\n" +
				   "mkdir %backup_path%\\" + chuUserDao.select(chuUser).getWxUsername() + "\r\n" + "\r\n" + 
				   "mysql -h%ip% -P%port% -u%username% -p%password% -D%database% < backup_%app_id%.sql" + "\r\n" + "\r\n" +
				   "REM 压缩备份指定目录下的图片" + "\r\n" +
				   "REM 分卷压缩，每个文件大小不超过2GB" + "\r\n" +
				   "..\\rar a -m0 -r -v2g %backup_path%/%app_id%/%corp_name%_%today%_数据备份  %backup_path%/" + chuUserDao.select(chuUser).getWxUsername() + "\r\n" + "\r\n" +
				   "REM 清理操作" + "\r\n" +
				   "del /q %backup_path%\\" + chuUserDao.select(chuUser).getWxUsername() + "\\*.*" + "\r\n" +
				   "rmdir /q %backup_path%\\" + chuUserDao.select(chuUser).getWxUsername() + "\r\n" +
				   "exit"; 			
		String realUrl = baseUrl + "/" + url_backup_path + "/" + appId + "/backup_" + appId + ".bat";
		creatBat(cmd,realUrl);
		try{
			Process process = Runtime.getRuntime().exec(realUrl);
            InputStream in = process.getInputStream(); 
            in.close();
            process.waitFor();   
		}catch(Exception e){
			throw new OperException(备份操作失败);
		}
	}
	
	/**
	 * 根据cmd指令在指定路径生成.bat文件
	 * @param command
	 * @param batURL
	 */
	private void creatBat(String command, String batURL) throws OperException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(batURL);
			fw.write(command);
		} catch (IOException e) {
			throw new OperException(备份操作失败);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					throw new OperException(备份操作失败);
				}
			}
		}
	}
	
	/**
	 * 封装.sql文件里面根据appId来查询的sql语句
	 * @param session
	 * @param appId
	 * @param tableName
	 *        需要备份的表名
	 * @return
	 */
	private String selectByAppId(Integer appId, String tableName){
		ChuUser chuUser = new ChuUser();
		chuUser.setAppId(appId);
		String baseUrl = getRootPath().replace("\\", "/");;
		String url_backup_path = ZCGLProperties.URL_BACKUP_PATH; 
		return "select * from " + tableName + " where app_id=" + appId + " into outfile" + " \"" +  baseUrl + "/" + url_backup_path + "/" + chuUserDao.select(chuUser).getWxUsername() + "/" + tableName + ".xls" + "\"" + ";";
	}
	
	/**
	 * 封装.sql文件里面不需要根据appId来查询的sql语句
	 * @param session
	 * @param appId
	 * @param tableName
	 *        需要备份的表名
	 * @return
	 */
	private String selectNoAppId(Integer appId, String tableName){
		ChuUser chuUser = new ChuUser();
		chuUser.setAppId(appId);
		String baseUrl = getRootPath().replace("\\", "/");;
		String url_backup_path = ZCGLProperties.URL_BACKUP_PATH; 
		return "select * from " + tableName + " into outfile" + " \"" +  baseUrl + "/" + url_backup_path + "/" + chuUserDao.select(chuUser).getWxUsername() + "/" + tableName + ".xls" + "\"" + ";";
	}
	
	/**
	 * 依据ZCGLProperties，生成.sql文件
	 * @throws OperException 
	 */
	private void generateBackupSQL(Integer appId) throws OperException {
		String baseUrl = getRootPath().replace("\\", "/");
		String url_backup_path = ZCGLProperties.URL_BACKUP_PATH; 
		String content[] = {selectNoAppId(appId, "c_jzlx"), 
				            selectByAppId(appId, "c_fj"),
				            selectByAppId(appId, "c_jzw"),
				            selectByAppId(appId, "c_xq"),
				            selectByAppId(appId, "c_zcgl"),
				            selectByAppId(appId, "c_zclx"),
				            selectByAppId(appId, "chu_app"),
				            selectByAppId(appId, "chu_backup"),
				            selectNoAppId(appId, "chu_env"),
				            selectNoAppId(appId, "chu_menu"),
				            selectNoAppId(appId, "chu_role"),
				            selectNoAppId(appId, "chu_role_menu"),
				            selectByAppId(appId, "chu_tag"),
				            selectByAppId(appId, "chu_user"),
				            selectByAppId(appId, "chu_wxdept"),
				            selectByAppId(appId, "chu_wxuser"),
				            selectByAppId(appId, "chu_zt_action"),
				            selectByAppId(appId, "j_rw"),
				            selectNoAppId(appId, "j_rwxz"),
				            selectByAppId(appId, "j_zc"),
				            selectByAppId(appId, "l_msg"),
				            selectByAppId(appId, "l_zt"),
				            selectByAppId(appId, "l_ztxz"),
				            selectNoAppId(appId, "test")};
		String url = baseUrl + "/" + url_backup_path + "/" + appId + "/" + "backup_" + appId + ".sql";
		File file = new File(url);
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bufw=new BufferedWriter(fw);
			for(int k=0; k < content.length; k++){
				bufw.write(content[k]);
				bufw.newLine();
			}
			bufw.close();
			fw.close();
		} catch (IOException e) {
			throw new OperException(备份操作失败);
		}		
	}

	/**
	 * 调用generateBackupScript(session,appId)和generateBackupSQL(session, appId)这两个私有方法来生成相应的.bat和.sql文件，并执行该.bat文件
	 * @param session
	 * @param appId
	 * @throws OperException
	 */
	@SuppressWarnings("deprecation")
	public void backup(Integer appId, String czr) throws OperException {
		String destDirName = getRootPath() + "\\" + ZCGLProperties.URL_BACKUP_PATH + "\\" + appId;
		createDir(destDirName);
		ChuApp app = chuAppDao.selectByPrimaryKey(appId);
		ChuBackup param = new ChuBackup();
		param.setAppId(appId);
		List<ChuBackup> backupListByAppId = chuBackupDao.selectBackupList(param);
		param.setStatus(0);
		List<ChuBackup> backupListByAppIdAndStatus = chuBackupDao.selectBackupList(param);
		Calendar now = Calendar.getInstance(); 
		if(app.getLevel() == 0){
			// 普通用户
			for(ChuBackup backup : backupListByAppId){ 
				if((backup.getBfsj().getMonth()+1) == now.get(Calendar.MONTH) + 1){
					throw new OperException(普通用户每月仅能备份一次);
				}
			 }
				if(backupListByAppIdAndStatus.size() != 0){
					throw new OperException(普通用户仅能保留一个备份);	
				}else{
					 startBackup(appId, czr);
			     }	
		}else if(app.getLevel() == 1){
			// 高级用户
			for(ChuBackup backup : backupListByAppId){ 
				if((backup.getBfsj().getMonth()+1) == now.get(Calendar.MONTH) + 1){
					throw new OperException(高级用户每月仅能备份一次);
				}
			 }
			 startBackup(appId, czr);
		}else if(app.getLevel() == 2){
			// 专业级用户
			startBackup(appId, czr);
		}else{
			throw new OperException(备份操作失败);
		}
	}
	
	/**
	 * 获取项目根目录的绝对路径
	 * @return
	 */
	private String getRootPath(){
		String nodepath = this.getClass().getClassLoader().getResource("/").getPath();
		String baseUrl = "";
		baseUrl = nodepath.substring(1,nodepath.indexOf("/WEB-INF/classes"));
		baseUrl = baseUrl.replace("/", "\\");
		return baseUrl;
	}
	
	/**
	 * 开始备份
	 * @param session
	 * @param appId
	 * @throws OperException
	 */
	private void startBackup(Integer appId, String czr) throws OperException{
		generateBackupScript(appId);
		generateBackupSQL(appId);		
		String baseUrl = getRootPath();		
		String url_backup_path = ZCGLProperties.URL_BACKUP_PATH;
		String strcmd = "cmd /c cd " + baseUrl + "\\" + url_backup_path + "\\" + appId + "&&start backup_" + appId + ".bat";	 
	    run_cmd(strcmd);
	    ChuBackup param = new ChuBackup();
	    param.setAppId(appId);
	    Date day=new Date();   	    
		param.setBfsj(day);
		param.setCzr(czr); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		String backupset = appService.getApp(appId).getCorpName() + "_" + df.format(day).replace("-", "") + "_数据备份";
		param.setBackupset(backupset);
		param.setBackuptype("数据");
		param.setStatus(0);
		chuBackupDao.insertSelective(param);
		logger.info("成功进行一次备份");
	}
	
	/**
	 * 执行.bat文件的函数
	 * @param strcmd
	 * @throws OperException 
	 */
	private void run_cmd(String strcmd) throws OperException {
		        Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
		        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
		        try {		        	
		            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
		            ps.waitFor(); 
		        	//等待子进程完成再往下执行。
		        } catch (IOException e1) {
		        	throw new OperException(备份操作失败);
		        } catch (InterruptedException e) {		            
		        	throw new OperException(备份操作失败);
		        }
		        int i = ps.exitValue();  //接收执行完毕的返回值
		        if (i == 0) {
		        	logger.debug("执行完成");
		        } else {
		        	logger.debug("执行失败");
		        }
		        ps.destroy();  //销毁子进程
		        ps = null;   
		    }
	
	/**
	 * 统计备份列表
	 * 
	 * @param param
	 * @return
	 */
	public int countBackupList(ChuBackup param){
		return chuBackupDao.countBackupList(param);
	}
	
	/**
	 * 获取指定status下的指定页的备份列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<ChuBackup> getBackupList(ChuBackup param, int rows, int page){
		PageNavigator pn=new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return chuBackupDao.selectBackupList(param);
	}
	
	/**
	 * 获取指定status下的指定页的备份列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<ChuBackup> getPageZCGLList(ChuBackup param, int rows, int page) {
		int total = countBackupList(param);
		EasyuiDatagrid<ChuBackup> pageDatas = new EasyuiDatagrid<ChuBackup>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<ChuBackup>());
		} else {
			List<ChuBackup> list = getBackupList(param, rows, page);
			pageDatas.setRows(list);
			pageDatas.setTotal(total);
		}		
		return pageDatas;
	}
	
	/**
	 * 根据主键得到相应的备份
	 * @param id
	 * @return
	 */
	public ChuBackup selectById(Integer id){	
	     return chuBackupDao.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据主键更新备份
	 * @param record
	 * @return
	 */
	public int updateById(ChuBackup record){
		return chuBackupDao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 根据主键来删除记录（将相关记录status置为已删除（1））
	 * @param id
	 * @return
	 */
	public boolean deleteById(Integer id, HttpSession session, Integer appId){
		 String baseUrl = session.getServletContext().getRealPath("");	 
		 String url_backup_path = ZCGLProperties.URL_BACKUP_PATH; 
		 if(id!=null){
			  ChuBackup param = selectById(id);			 
			  String fileName = baseUrl + "\\" + url_backup_path  + "\\" + appId + "\\" + param.getBackupset() + ".rar";
			  param.setStatus(1);
			  updateById(param);  
//			  deleteFile(fileName);
			  return true;
		}else {
			return false;
		}
	}
		
	/**
	 * 删除单个文件	
	 * 
	 * @param fileName
	 *    要删除的文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	private boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if(file.exists() && file.isFile()){
			if(file.delete()){
				logger.info("删除单个文件" + fileName + "成功！");
				return true;
			}else{
				logger.info("删除单个文件" + fileName + "失败！");
			    return false;
			}		 
		}else{
			logger.info("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}
		
	/**
	 * 下载指定文件
	 * @param fileName
	 * @param filePath
	 * @param response
	 */
	private void downLoadFile(String fileName, String filePath, HttpServletResponse response) throws OperException{
		 BufferedInputStream br = null;
		 OutputStream out = null;
		 try{
			    File f = new File(filePath);
				if (!f.exists()) {
					response.sendError(404, "File not found!");
					return;
				}
				br = new BufferedInputStream(new FileInputStream(f));
				byte[] buf = new byte[1024];
				int len = 0;
				response.setCharacterEncoding("utf-8");
				response.setContentType("multipart/form-data");
				response.reset(); // 非常重要
				// 纯下载方式
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment; filename="
						+   new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".rar");
				out = response.getOutputStream();
				while ((len = br.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.flush();
		 }catch(IOException e){
			 throw new OperException(文件下载异常);
		 }finally{
			 if (br != null)
					try {
						br.close();
					} catch (IOException e) {
						throw new OperException(文件下载异常);
					}
			 if (out != null)
					try {
						out.close();
					} catch (IOException e) {
						throw new OperException(文件下载异常);
					}
		 }
	}
	
	public void downLoadBackup(Integer id, HttpSession session, HttpServletResponse response, Integer appId) throws OperException{
		String baseUrl = session.getServletContext().getRealPath("");
		String url_backup_path =  ZCGLProperties.URL_BACKUP_PATH;
		String backupset = chuBackupDao.selectByPrimaryKey(id).getBackupset();
		String filePath = baseUrl + "\\" + url_backup_path + "\\" + appId + "\\" +backupset + ".rar";
		String fileName = backupset;
		downLoadFile(fileName, filePath, response);	
	}

	// 根据appId创建目录
	private boolean createDir(String destDirName) {
		File dir = new File(destDirName);
			if (dir.exists()) {// 判断目录是否存在
				logger.debug("创建目录失败，目标目录已存在！");
				return false;
			}
			if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
					destDirName = destDirName + File.separator;
			}
			if (dir.mkdirs()) {// 创建目标目录
				logger.debug("创建目录成功！" + destDirName);
				return true;
			} else {
				logger.debug("创建目录失败！");
				return false;
			}
		}
}
