package com.codegen.service;

import java.util.List;

import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.vo.EadtSysDatabaseSourceQuery;
import com.common.base.BaseService;
import com.common.utils.CountOrder;


public interface EadtSysDatabaseSourceService extends BaseService<EadtSysDatabaseSource, String> {

    List<EadtSysDatabaseSource> searchEadtSysDatabaseSource(EadtSysDatabaseSourceQuery eadtSysDatabaseSource,
            CountOrder countOrder);


    Long countEadtSysDatabaseSource(EadtSysDatabaseSourceQuery eadtSysDatabaseSource);


    public String getDbtype(EadtSysDatabaseSource eadtSysDatabaseSource);

}
