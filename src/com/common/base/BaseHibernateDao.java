package com.common.base;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.common.utils.CountOrder;
import com.common.utils.G4Utils;


public abstract class BaseHibernateDao<E, PK extends Serializable> extends HibernateDaoSupport implements
        EntityDao<E, PK> {
    private static final String SysConstants = null;
	/**
     * Logger for subclass
     */
    protected Log log = LogFactory.getLog(getClass());


    public long queryForLong(final String queryString) {
        return queryForLong(queryString, new Object[] {});
    }


    public long queryForLong(final String queryString, Object value) {
        return queryForLong(queryString, new Object[] { value });
    }


    public long queryForLong(final String queryString, Object[] values) {
        return DataAccessUtils.longResult(getHibernateTemplate().find(queryString, values));
    }

    /**
     * sql查询列表
     * 
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryByNativeSql(final String sql) {
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                // query.setProperties(bean)
                List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

                // List result = query.list();
                return result;
            }
        });
        return list;
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
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                // query.setProperties(bean)
                if (bindValue != null && bindValue.length >= 1) {
                    Type[] types = typesFactory(bindValue);
                    query.setParameters(bindValue, types);
                }
                if (G4Utils.isNotEmpty(countOrder)) {
                    query.setFirstResult(countOrder.getStart());
                    query.setMaxResults(countOrder.getLimit());
                }

                List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

                // List result = query.list();
                return result;
            }
        });
        return list;
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
    @SuppressWarnings("deprecation")
    public int countQueryByNativeSqlPage(final String hql, final Object[] bindValue) {
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(hql);
                // query.setProperties(bean)
                if (bindValue != null && bindValue.length >= 1) {
                    Type[] types = typesFactory(bindValue);
                    query.setParameters(bindValue, types);
                }
                List<Integer> list1 = new ArrayList<Integer>();
                list1.add(new Integer(query.uniqueResult().toString()));
                return list1;
            }
        });
        return (int) list.get(0);
    }


    /**
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @see select new Map(a) from u
     * @param bindValue
     *            对象数组
     * 
     * @param countOrder
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> queryByHqlPage(final String hql, final Object bean,
            final CountOrder countOrder) {
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (G4Utils.isNotEmpty(bean)) {
                    query.setProperties(bean);
                }

                if (G4Utils.isNotEmpty(countOrder)) {
                    query.setFirstResult(countOrder.getStart());
                    query.setMaxResults(countOrder.getLimit());
                }
                List result = query.list();
                return result;
            }
        });
        return list;
    }


    /**
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @see select new Map(a) from u
     * @param bindValue
     *            对象数组
     * 
     * @param countOrder
     * @return List<Map<String,Object>>
     */
    public List queryByHqlPage1(final String hql, final Object bean, final CountOrder countOrder) {
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (G4Utils.isNotEmpty(bean)) {
                    query.setProperties(bean);
                }

                if (G4Utils.isNotEmpty(countOrder)) {
                    query.setFirstResult(countOrder.getStart());
                    query.setMaxResults(countOrder.getLimit());
                }
                List result = query.list();
                return result;
            }
        });
        return list;
    }


    /**
     * 根据原生sql生成分页数据，
     * 
     * @param sql
     * @param bindValue
     *            对象数组
     * @param countOrder
     * @return long
     */
    public long queryCountByHql(final String hql, final Object bean) {
        List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                query.setProperties(bean);
                List result = query.list();
                return result;
            }
        });
        return (long) list.get(0);
    }


    /**
     * 根据hql执行修改或删除
     * 
     * @param sql
     * @param bindValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public int updateOrDel(final String hql, final Object[] bindValue) {
        return (int) this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (bindValue != null && bindValue.length >= 1) {
                    Type[] types = typesFactory(bindValue);
                    query.setParameters(bindValue, types);
                }
                return query.executeUpdate();
            }
        });
    }


    public void save(E entity) {
        getHibernateTemplate().save(entity);
    }


    public List<E> findAll() {
        return getHibernateTemplate().loadAll(getEntityClass());
    }


    public E getById(PK id) {
        return (E) getHibernateTemplate().get(getEntityClass(), id);
    }


    public void delete(Object entity) {
        getHibernateTemplate().delete(entity);
    }


    public void delete(Serializable entity) {
        getHibernateTemplate().delete(entity);
    }


    public void deleteById(PK id) {
        Object entity = getById(id);
        if (entity == null) {
            throw new ObjectRetrievalFailureException(getEntityClass(), id);
        }
        getHibernateTemplate().delete(entity);
    }


    public void update(E entity) {
        getHibernateTemplate().update(entity);
    }


    public void saveOrUpdate(E entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }


    public void refresh(Object entity) {
        getHibernateTemplate().refresh(entity);
    }


    public void flush() {
        getHibernateTemplate().flush();
    }


    public void evict(Object entity) {
        getHibernateTemplate().evict(entity);
    }


    public void saveAll(Collection<E> entities) {
        for (Iterator<E> it = entities.iterator(); it.hasNext();) {
            save(it.next());
        }
    }


    public void deleteAll(Collection entities) {
        getHibernateTemplate().deleteAll(entities);
    }


    public void delete(final String obj, final Class table, final String field) {
        getHibernateTemplate().execute(new HibernateCallback() {

            public E doInHibernate(Session session) throws HibernateException, SQLException {
                String hql = "delete from " + table.getName() + " where " + field + " = ?";
                session.createQuery(hql).setString(0, obj).executeUpdate();
                return null;
            }
        });
    }


    public E findByProperty(final String propertyName, final Object value) {

        return (E) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(getEntityClass()).add(Restrictions.eq(propertyName, value))
                    .uniqueResult();
            }
        });
    }


    public Object findByPropertyList(final String propertyName, final Object value) {
        return getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(getEntityClass()).add(Restrictions.eq(propertyName, value))
                    .list();
            }
        });
    }


	public E findPropertyByParms(final String[] propertyNames, final Object[] values) {
		return (E) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria cr = session.createCriteria(getEntityClass());
				if(null != propertyNames && propertyNames.length > 0){
					for(int i=0;i<propertyNames.length;i++){
						cr.add(Restrictions.eq(propertyNames[i], values[i]));
					}
				}
				return cr.setMaxResults(1).uniqueResult();
			}
		});
	}
	
    public E findByField(final String propertyName, final String propertyName1, final String propertyName2,
            final Object value) {

        return (E) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(getEntityClass()).add(Restrictions.eq(propertyName, value))
                    .add(Restrictions.eq(propertyName1, value)).add(Restrictions.eq(propertyName2, value))
                    .uniqueResult();
            }
        });
    }

    public List<E> findAllBy(final String propertyName, final Object value) {
        return (List<E>) getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(getEntityClass()).add(Restrictions.eq(propertyName, value))
                    .list();
            }
        });
    }


    public List<E> findAllByOrderAsc(final String propertyName, final Object value, final String field) {
        return (List<E>) getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(getEntityClass()).add(Restrictions.eq(propertyName, value))
                    .addOrder(Order.asc(field)).list();
            }
        });
    }





    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     * 
     * @param uniquePropertyNames
     *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(E entity, String uniquePropertyNames) {
        Assert.hasText(uniquePropertyNames);
        Criteria criteria =
                getSession().createCriteria(getEntityClass()).setProjection(Projections.rowCount());
        String[] nameList = uniquePropertyNames.split(",");
        try {
            // 循环加入唯一列
            for (int i = 0; i < nameList.length; i++) {
                criteria.add(Restrictions.eq(nameList[i], PropertyUtils.getProperty(entity, nameList[i])));
            }

            // 以下代码为了如果是update的情况,排除entity自身.

            String idName =
                    getSessionFactory().getClassMetadata(entity.getClass()).getIdentifierPropertyName();
            if (idName != null) {
                // 取得entity的主键值
                Serializable id = (Serializable) PropertyUtils.getProperty(entity, idName);

                // 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
                if (id != null)
                    criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return ((Number) criteria.uniqueResult()).intValue() == 0;
    }


    @SuppressWarnings("unchecked")
    public Boolean isUnique(final String table, final String field, final String value) {
        return (Boolean) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                boolean flag = false;
                String sql = "select * from " + table + " where " + field + " = '" + value + "'";
                Query query = session.createSQLQuery(sql);
                if (query.list().size() == 0) {
                    flag = true;
                }
                return flag;
            }

        });
    }


    public abstract Class getEntityClass();


    /**
     * 获取对象对应参数的类型
     * 
     * @param bindValue
     * @return
     */
    private final Type[] typesFactory(Object[] bindValue) {
        int count = bindValue.length;
        Type[] types = new Type[count];
        for (int i = 0; i < count; i++) {
            if (bindValue[i].getClass().getName().endsWith("String")) {
                types[i] = new StringType();
            } else if (bindValue[i].getClass().getName().endsWith("Integer")) {
                types[i] = new IntegerType();
            } else if (bindValue[i].getClass().getName().endsWith("Float")) {
                types[i] = new FloatType();
            } else if (bindValue[i].getClass().getName().endsWith("Date")) {
                types[i] = new DateType();
            } else if (bindValue[i].getClass().getName().endsWith("BigDecimal")) {
                types[i] = new BigDecimalType();
            }
        }
        return types;
    }
}
