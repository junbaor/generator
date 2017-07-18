package com.codegen.dao;

import static com.common.utils.G4Utils.isNotEmpty;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.vo.EadtSysDatabaseSourceQuery;
import com.common.base.BaseHibernateDao;
import com.common.utils.CountOrder;

@Repository
public class EadtSysDatabaseSourceDao extends BaseHibernateDao<EadtSysDatabaseSource, String> {

    public Class getEntityClass() {

        return EadtSysDatabaseSource.class;
    }


    public List searchEadtSysDatabaseSource(final EadtSysDatabaseSourceQuery eadtSysDatabaseSource,
            final CountOrder countOrder) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(getEntityClass());
                if (isNotEmpty(eadtSysDatabaseSource.getConfigname())) {
                    criteria.add(Restrictions.like("configname", "%" + eadtSysDatabaseSource.getConfigname()
                            + "%"));
                }
                if (isNotEmpty(eadtSysDatabaseSource.getDbversion())) {
                    criteria.add(Restrictions.like("dbversion", "%" + eadtSysDatabaseSource.getDbversion()
                            + "%"));
                }
                if (isNotEmpty(eadtSysDatabaseSource.getConnecturl())) {
                    criteria.add(Restrictions.like("connecturl", "%" + eadtSysDatabaseSource.getConnecturl()
                            + "%"));
                }
                if (isNotEmpty(countOrder)) {
                    criteria.setFirstResult(countOrder.getStart());
                    criteria.setMaxResults(countOrder.getLimit());
                    if (isNotEmpty(countOrder.getOrderby())) {
                        if ("desc".equalsIgnoreCase(countOrder.getDir()))
                            criteria.addOrder(Order.desc(countOrder.getOrderby()));
                        else
                            criteria.addOrder(Order.asc(countOrder.getOrderby()));
                    }
                }
                return criteria.list();
            }
        });
    }


    public Long countEadtSysDatabaseSource(final EadtSysDatabaseSourceQuery eadtSysDatabaseSource) {
        return (Long) getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(getEntityClass());
                if (isNotEmpty(eadtSysDatabaseSource.getConfigname())) {
                    criteria.add(Restrictions.like("configname", "%" + eadtSysDatabaseSource.getConfigname()
                            + "%"));
                }
                if (isNotEmpty(eadtSysDatabaseSource.getDbversion())) {
                    criteria.add(Restrictions.like("dbversion", "%" + eadtSysDatabaseSource.getDbversion()
                            + "%"));
                }
                if (isNotEmpty(eadtSysDatabaseSource.getConnecturl())) {
                    criteria.add(Restrictions.like("connecturl", "%" + eadtSysDatabaseSource.getConnecturl()
                            + "%"));
                }
                criteria.setProjection(Projections.rowCount());
                return criteria.list();
            }
        }).iterator().next();
    }

}
