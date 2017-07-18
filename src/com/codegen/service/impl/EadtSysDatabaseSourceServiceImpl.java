package com.codegen.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codegen.dao.EadtSysDatabaseSourceDao;
import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.service.EadtSysDatabaseSourceService;
import com.codegen.vo.EadtSysDatabaseSourceQuery;
import com.common.base.BaseServiceImpl;
import com.common.base.EntityDao;
import com.common.utils.CountOrder;


@Service("eadtSysDatabaseSourceService")
public class EadtSysDatabaseSourceServiceImpl extends BaseServiceImpl<EadtSysDatabaseSource, String>
        implements EadtSysDatabaseSourceService {

    private EadtSysDatabaseSourceDao eadtSysDatabaseSourceDao;


    /** 增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写 */
    public void setEadtSysDatabaseSourceDao(EadtSysDatabaseSourceDao dao) {
        this.eadtSysDatabaseSourceDao = dao;
    }



    public EntityDao getEntityDao() {
        return this.eadtSysDatabaseSourceDao;
    }


    public List<EadtSysDatabaseSource> searchEadtSysDatabaseSource(
            EadtSysDatabaseSourceQuery eadtSysDatabaseSource, CountOrder countOrder) {
        return eadtSysDatabaseSourceDao.searchEadtSysDatabaseSource(eadtSysDatabaseSource, countOrder);
    }


    public Long countEadtSysDatabaseSource(EadtSysDatabaseSourceQuery eadtSysDatabaseSource) {
        return eadtSysDatabaseSourceDao.countEadtSysDatabaseSource(eadtSysDatabaseSource);
    }


    public String getDbtype(EadtSysDatabaseSource eadtSysDatabaseSource) {
        String driver = eadtSysDatabaseSource.getDbdriver();
        if ("oracle.jdbc.driver.OracleDriver".equals(driver)) {
            return "oracle";
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driver)) {
            return "SQLServer";
        } else if ("com.ibm.db2.jcc.DB2Driver".equals(driver)) {
            return "DB2";
        }
        return null;
    }




}
