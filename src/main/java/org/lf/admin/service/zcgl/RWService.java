package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.JRWMapper;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产管理-- 任务管理 
 * 
 * @author 王登
 */
@Service("rwService")
public class RWService {
	@Autowired
	private JRWMapper rwDao;
	
	public static final OperErrCode 任务类型不能为空 = new OperErrCode("10201", "任务类型不能为空");
	public static final OperErrCode 任务操作人不能为空 = new OperErrCode("10201", "任务操作人不能为空");
	public static final OperErrCode 任务开始时间不能为空 = new OperErrCode("10201", "任务开始时间不能为空");
	public static final OperErrCode 任务结束时间不能为空 = new OperErrCode("10201", "任务结束时间不能为空");
	public static final OperErrCode 任务结束时间不能早于开始时间 = new OperErrCode("10201", "任务结束时间不能早于开始时间");
	public static final OperErrCode 无法删除有资产的任务 = new OperErrCode("10201", "无法删除有资产的任务");

	
	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。
	 * 
	 * @exception 任务类型不能为空, 任务操作人不能为空, 任务开始时间不能为空, 任务结束时间不能为空, 任务结束时间不能早于开始时间
	 * 
	 * 
	 */
	private void checkJRW(JRW jrw) throws OperException {
		if (jrw.getLx() == null) {
			throw new OperException(任务类型不能为空);
		}
		if (StringUtils.isEmpty(jrw.getCzr()) ) {
			throw new OperException(任务操作人不能为空);
		}
		if(jrw.getKssj() == null){
			throw new OperException(任务开始时间不能为空);
		}
		if(jrw.getJssj() == null){
			throw new OperException(任务结束时间不能为空);
		}
		//任务结束时间不能早于开始时间
		if(jrw.getJssj().before(jrw.getKssj())){
			throw new OperException(任务结束时间不能早于开始时间);
		}
	}
	
	
	/**
	 * 插入一个任务记录。
	 * 
	 * @param rw
	 * @throws OperException 任务类型不能为空, 任务操作人不能为空, 任务操作人不存在, 任务验收人不存在, 任务开始时间不能为空, 任务结束时间不能为空, 任务结束时间不能早于开始时间
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertRW(JRW rw) throws OperException {
		checkJRW(rw);
		
		rwDao.insertSelective(rw);
	}
	
	/**
	 * 更新一个任务记录。
	 * 
	 * @param rw
	 * @throws OperException 任务类型不能为空, 任务操作人不能为空, 任务操作人不存在, 任务验收人不存在, 任务开始时间不能为空, 任务结束时间不能为空, 任务结束时间不能早于开始时间
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRW(JRW rw) throws OperException {
		checkJRW(rw);
		rwDao.updateByPrimaryKey(rw);
	}
	
	/**
	 * 删除任务。
	 * 当任务中存在资产时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param rw
	 * @throws OperException 无法删除任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delRW(JRW rw) throws OperException {
		try {
			rwDao.deleteByPrimaryKey(rw.getId());
		} catch (Exception e) {
			throw new OperException(无法删除有资产的任务);
		}
	}
	
	/**
	 * 统计任务列表
	 * @param param
	 * @return
	 */
	public int countRWList(JRW param) {
		return rwDao.countRWList(param);
	}
	
	/**
	 * 获取任务列表
	 * @param param
	 * @return
	 */
	public List<JRW> getRWList(JRW param) {
		return rwDao.selectList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的任务列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<JRW> getRWList(JRW param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getRWList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的任务列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public EasyuiDatagrid<JRW> getPageRWList(JRW param, int rows, int page) {
		int total = countRWList(param);

		EasyuiDatagrid<JRW> pageDatas = new EasyuiDatagrid<JRW>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<JRW>());
		} else {
			List<JRW> list = getRWList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 根据主键获取任务数据
	 * @param id
	 * @return
	 */
	public JRW getRW(Integer id) {
		return rwDao.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据参数获取任务数据
	 * @return
	 */
	public JRW getRW(JRW param) {
		return rwDao.select(param);
	}

	/**
	 * 遍历J_RW中的数据，对FINISH为0，结束时间 - 当前系统时间 < RW_EXPIRE_DATE的任务，向验收人和操作人发送消息：
	 * 
select *  
from j_rw
where app_id = ? and finish = 0 
  and datediff(jssj, now()) < (select env_value from chu_env where env_key = 'RW_EXPIRE_DATE'); 
	 * 
	 * @param appId
	 * @return
	 */
	public List<JRW> getExpiringRWList(Integer appId) {
		return rwDao. getExpiringRWList(appId);
	}


	public List<JRW> getExpiredRWList(Integer appId) {
		return rwDao.getExpiredRWList(appId);
	}
	
}
