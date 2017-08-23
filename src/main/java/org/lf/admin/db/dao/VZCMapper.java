package org.lf.admin.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.VZC;

public interface VZCMapper extends BaseMapper<VZC>{
	int countZCList(VZC param);
	//zcmc和存放地点模糊查询
	int countZCListFuzzy(VZC param);
	//zcmc和存放地点模糊查询
	List<VZC> getZCListFuzzy(VZC param);
	
	/**
	 * zcmc和存放地点模糊查询, 资产状态zczt使用List进行存储，可以同时查询多种状态的资产
	 * @param param
	 * @return
	 */
	int countZCListFuzzyByZTList(Map<String, Object> param);
		//zcmc和存放地点模糊查询
	List<VZC> getZCListFuzzyByZTList(Map<String, Object> param);
	
	/**
	 * 根据任务查询资产数量
	 * @param param 其中zcmc和cfdd是模糊查找
	 * @return
	 */
	int countZCListFuzzyByRW(Map<String, Object> param);
	/**
	 * 根据任务查询资产
	 * @param param 其中zcmc和cfdd是模糊查找
	 * @return
	 */
	List<VZC> getZCListFuzzyByRW(Map<String, Object> param);
	
	// 根据appID和glr获取当前资产管理员名下的资产折旧即将到期资产列表
	List<VZC> getDeprecatingZCList(VZC param);
	
	@Select("select * from v_zc where app_id = #{appId,jdbcType=INTEGER} and datediff(now(), gzsj) > zjnx * 365 ")
	@ResultMap(value="BaseResultMap")
	List<VZC> getDeprecatedZCList(@Param("appId")Integer appId);
	
	VZC selectByZcid(@Param("zcid")Integer zcid);
	
	/**
	 * 对资产类型和所属部门增加了多级查找功能
	 * 用户选择上级部门时，将该部门及其子部门都查找出来。（通过调用getWXDeptList数据库函数来实现）
	 * 用户选择一级资产类型时，将子类型的资产也显示出来。（这里约定，父类型与子类型在编码上左侧保持一致，采用模糊查找来实现）
	 * zcmc模糊查询, 资产状态zczt使用List进行存储，可以同时查询多种状态的资产
	 */
	List<VZC> selectByDeptNoListAndZCLXID(Map<String, Object> param);
	
	int countZCListByByDeptNoListAndZCLXID(Map<String, Object> param);
}