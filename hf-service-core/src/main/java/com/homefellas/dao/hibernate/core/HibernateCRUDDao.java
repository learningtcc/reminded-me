package com.homefellas.dao.hibernate.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.homefellas.core.SpringBean;
import com.homefellas.dao.core.Persistable;

public class HibernateCRUDDao extends SpringBean {

	private SessionFactory sessionFactory;
	private SessionFactory brochureSessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	public void setBrochureSessionFactory(SessionFactory brochureSessionFactory)
	{
		this.brochureSessionFactory = brochureSessionFactory;
	}


	public <T> void refresh(T object)
	{
		getSession().refresh(object);
	}

	protected Session getSession()
	{
		 Session session = sessionFactory.getCurrentSession();
//		 session.setFlushMode(FlushMode.COMMIT);
		 
		 return session;
	}
	
	protected Query getQuery(String hqlQuey)
	{
		return getSession().createQuery(hqlQuey);
	}
	
	protected Session getBrochureSession()
	{
		 return brochureSessionFactory.getCurrentSession();
	}
	
	protected Query getBrochureQuery(String hqlQuey)
	{
		return getBrochureSession().createQuery(hqlQuey);
	}
	
	protected Persistable extractSingleElement(List<Persistable> list)
	{
		if (list.isEmpty())
		{
			return null;
		}
		else
		{
			return list.get(0);
		}
	}
	
	protected String buildHQL(String prefix, String query, String suffix)
	{
		StringBuffer buffer = new StringBuffer(prefix);
		if ( !(prefix.endsWith(" ") || query.startsWith(" ")))
			buffer.append(" ");
		buffer.append(query);
		if ( !(query.endsWith(" ") || suffix.startsWith(" ")))
			buffer.append(" ");
		buffer.append(suffix);
		
		return buffer.toString();
	}
	
	public <T>T loadByPrimaryKey(Class<T> clazz, long pk)
	{
		Query q = getQuery("from "+clazz.getSimpleName()+" modelalias where modelalias.id=?");
		q.setLong(0, pk);
		return (T)q.uniqueResult();
	}
	
	public <T>T loadByPrimaryKey(Class<T> clazz, String pk)
	{
		Query q = getQuery("from "+clazz.getSimpleName()+" modelalias where modelalias.id=?");
		q.setString(0, pk);
		return (T)q.uniqueResult();
	}
	
	public <T>T loadObject(Class<T> entityClass, Serializable primaryKey) {
//		return (T)getSession().load(entityClass, primaryKey);
		Query query = getQuery("from "+entityClass.getSimpleName()+" entityClass where entityClass.id=?");
		if (primaryKey instanceof String)
			query.setString(0, (String)primaryKey);
		else
			query.setLong(0, (Long)primaryKey);
		return (T)query.uniqueResult();
			
	}


	public <T> Serializable save(T model)
	{
		return getSession().save(model);
	}


	public <T> void saveOrUpdate(T object)
	{
		getSession().saveOrUpdate(object);
	}


	public <T> void deleteObject(T object)
	{
		getSession().delete(object);
	}



	public <T> void updateObject(T object)
	{
		getSession().update(object);
	}


	public <T> void merge(T object)
	{
		getSession().merge(object);
	}
	

	public void flush() {
		getSession().flush();
		
	}


	public <T> List<T> loadAllObjects(Class<T> entityClass) {
		Query query = getQuery("from "+entityClass.getName());
		return query.list();
				
	}

	public void persist(Object object) {
		getSession().persist(object);
	}

	public <T> void saveAllObjects(Collection<T> objects)
	{
		for(Iterator<T> it = objects.iterator(); it.hasNext();) 
		{
			getSession().save(it.next());
		}
	}

	public <T> void deleteAllObjects(Collection<T> objects)
	{
		for(Iterator<T> it = objects.iterator(); it.hasNext();) 
		{
			getSession().delete(it.next());
		}
	}


	public <T> T loadByPrimaryKey(Class<T> clazz, Serializable pk)
	{
		if (pk instanceof String)
			return loadByPrimaryKey(clazz, (String)pk);
		else
			return loadByPrimaryKey(clazz, (Long)pk);
	}
	
	

}
