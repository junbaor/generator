package com.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.common.utils.CountOrder;


public interface EntityDao<E, PK extends Serializable> {

    public Object getById(PK id) throws DataAccessException;


    public void deleteById(PK id) throws DataAccessException;


    /**
     * 插入数据
     */
    public void save(E entity) throws DataAccessException;


    /**
     * 更新数据
     */
    public void update(E entity) throws DataAccessException;


    /**
     * 根据id检查是否插入或是更新数据
     */
    public void saveOrUpdate(E entity) throws DataAccessException;


    public boolean isUnique(E entity, String uniquePropertyNames) throws DataAccessException;


    /**
     * 用于hibernate.flush() 有些dao实现不需要实现此类
     */
    public void flush() throws DataAccessException;


    public List<E> findAll() throws DataAccessException;


    public Boolean isUnique(String table, String field, String value);


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
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @param bindValue
     *            对象数组
     * @param countOrder
     * @return List<Map<String,Object>>
     */
    public int countQueryByNativeSqlPage(final String hql, final Object[] bindValue);


    /**
     * 根据hql执行更新或删除
     * 
     * @param hql
     * @param bindValue
     * @return int
     */
    public int updateOrDel(final String hql, final Object[] bindValue);
}
