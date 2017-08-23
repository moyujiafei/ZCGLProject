package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.ChuBackup;

public interface ChuBackupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuBackup record);

    int insertSelective(ChuBackup record);

    ChuBackup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuBackup record);

    int updateByPrimaryKey(ChuBackup record);
    
    List<ChuBackup> selectBackupList(ChuBackup param);
    
    int countBackupList(ChuBackup param);
}