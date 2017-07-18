package com.common.base;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DynamicSessionFactory implements SessionFactory,
		ApplicationContextAware {
	private static final long serialVersionUID = 2064557324203496378L;
	private static final Log log = LogFactory.getLog(DynamicSessionFactory.class);
	private ApplicationContext applicationContext = null;
	private SessionFactory sessionFactory = null;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public SessionFactory getSessionFactory(String sessionFactoryName) {
		try {
			if (StringUtils.isBlank(sessionFactoryName)) {
				return sessionFactory;
			}
			return (SessionFactory) this.getApplicationContext().getBean(
					sessionFactoryName);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new RuntimeException("There is not the sessionFactory <name:"
					+ sessionFactoryName + "> in the applicationContext!");
		}
	}

	public SessionFactory getSessionFactory() {
		String sessionFactoryName = DbContextHolder.getDbType();
		return getSessionFactory(sessionFactoryName);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Reference getReference() throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsFetchProfileDefinition(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void evict(Class arg0) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evict(Class arg0, Serializable arg1) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictCollection(String arg0) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictCollection(String arg0, Serializable arg1)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictEntity(String arg0) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictEntity(String arg0, Serializable arg1)
			throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictQueries() throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evictQueries(String arg0) throws HibernateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, ClassMetadata> getAllClassMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getAllCollectionMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cache getCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetadata getClassMetadata(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionMetadata getCollectionMetadata(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.hibernate.classic.Session getCurrentSession()
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getDefinedFilterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterDefinition getFilterDefinition(String arg0)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statistics getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeHelper getTypeHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public org.hibernate.classic.Session openSession()
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.hibernate.classic.Session openSession(Interceptor arg0)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.hibernate.classic.Session openSession(Connection arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.hibernate.classic.Session openSession(Connection arg0,
			Interceptor arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatelessSession openStatelessSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatelessSession openStatelessSession(Connection arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
