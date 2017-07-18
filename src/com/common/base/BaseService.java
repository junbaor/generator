package com.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.common.utils.CountOrder;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-6-22 Time: 下午6:07 desc:
 */
public interface BaseService<E, PK extends Serializable> {

    public E getById(PK id) throws DataAccessException;


    public List<E> findAll() throws DataAccessException;


    /**
     * 根据id检查是否插入或是更新数据
     */
    public void saveOrUpdate(E entity) throws DataAccessException;


    /**
     * 插入数据
     */
    public void save(E entity) throws DataAccessException;


    public void removeById(PK id) throws DataAccessException;


    void update(E entity) throws DataAccessException;


    boolean isUnique(E entity, String uniquePropertyNames) throws DataAccessException;


    String isUnique(E entity, String uniquePropertyCodes, String uniquePropertyNames)
            throws DataAccessException;


    boolean isUniqueVerify(String table, String field, String value);


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
            final CountOrder countOrder);


    /**
     * 根据原生sql查询总条数
     * 
     * @param sql
     * @param bindValue
     *            对象数组
     * @param countOrder
     * @return int
     */
    public int countQueryByNativeSqlPage(final String sql, final Object[] bindValue);


    /**
     * 根据hql执行更新或删除
     * 
     * @param hql
     * @param bindValue
     * @return int
     */
    public int updateOrDel(final String hql, final Object[] bindValue);
}
