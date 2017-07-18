package com.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.common.utils.CountOrder;


public abstract class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {

    protected Log log = LogFactory.getLog(getClass());


    protected abstract EntityDao getEntityDao();


    public E getById(PK id) throws DataAccessException {
        return (E) getEntityDao().getById(id);
    }


    public List<E> findAll() throws DataAccessException {
        return getEntityDao().findAll();
    }


    /**
     * 根据id检查是否插入或是更新数据
     */
    public void saveOrUpdate(E entity) throws DataAccessException {
        getEntityDao().saveOrUpdate(entity);
    }


    /**
     * 插入数据
     */
    public void save(E entity) throws DataAccessException {
        getEntityDao().save(entity);
    }


    public void removeById(PK id) throws DataAccessException {
        getEntityDao().deleteById(id);
    }


    public void update(E entity) throws DataAccessException {
        getEntityDao().update(entity);
    }


    public boolean isUnique(E entity, String uniquePropertyNames) throws DataAccessException {
        return getEntityDao().isUnique(entity, uniquePropertyNames);
    }


    /**
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @param bindValue
     *            对象数组
     * @param countOrder
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> queryByNativeSqlPage(final String sql, final Object[] bindValue,
            final CountOrder countOrder) {
        return getEntityDao().queryByNativeSqlPage(sql, bindValue, countOrder);
    }


    /**
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @param bindValue
     *            对象数组
     * @param countOrder
     * @return List<Map<String,Object>>
     */
    public int countQueryByNativeSqlPage(final String hql, final Object[] bindValue) {
        return getEntityDao().countQueryByNativeSqlPage(hql, bindValue);
    }


    /**
     * 根据hql执行更新或删除
     * 
     * @param hql
     * @param bindValue
     * @return int
     */
    public int updateOrDel(final String hql, final Object[] bindValue) {
        return getEntityDao().updateOrDel(hql, bindValue);
    }


    public String isUnique(E entity, String uniquePropertyCodes, String uniquePropertyNames)
            throws DataAccessException {
        boolean flag = isUnique(entity, uniquePropertyCodes);
        if (!flag) {
            String names = uniquePropertyNames;
            int i = uniquePropertyCodes.indexOf(",");
            if (i > 0) {
                names = "[" + names + "]组合";
            } else {
                names = "[" + names + "]";
            }
            return "违反唯一约束," + names + "不能重复.";
        } else
            return null;
    }


    public boolean isUniqueVerify(String table, String field, String value) {
        return getEntityDao().isUnique(table, field, value);
    }

}
