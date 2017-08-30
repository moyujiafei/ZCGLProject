package org.lf.admin.db.dao;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.Map;
>>>>>>> upstream/master

import org.lf.admin.db.pojo.VYHP;

public interface VYHPMapper {
    int insert(VYHP record);

    int insertSelective(VYHP record);
    
<<<<<<< HEAD
    int countYhpList(VYHP record);
    
    List<VYHP> selectYhpList(VYHP record);
=======
    int countYhpList(VYHP param);
    
    List<VYHP> selectYhpList(VYHP param);
    
    /**
     * 对部门编号和易耗品类型实现多级查询功能
     * @param param
     * @return
     */
    List<VYHP> selectYHPListByDeptNoAndYHPLX(Map<String, Object> param);
    
    int countYHPListByDeptNoAndYHPLX(Map<String, Object> param);
>>>>>>> upstream/master
}