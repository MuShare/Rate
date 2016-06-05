package edu.ut.softlab.rate.dao.common;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.google.common.base.Preconditions;

public abstract class AbstractHibernateDao<T extends Serializable> implements IOperations<T>{
	
	private Class<T> clazz;
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	protected final void setClass(final Class<T> clazzToSet) {
		this.clazz = Preconditions.checkNotNull(clazzToSet);
	}
	
	protected final Session getCurrentSesstion(){
		return sessionFactory.getCurrentSession();
	}

	@Override
	public final T findOne(String id) {
		return (T)getCurrentSesstion().get(clazz, id);
	}

	@Override
	public List<T> findAll() {
		return getCurrentSesstion().createQuery("from " + clazz.getName()).list();
	}

	@Override
	public void create(T entity) {
		Preconditions.checkNotNull(entity);
		getCurrentSesstion().saveOrUpdate(entity);
	}

	@Override
	public T update(T entity) {
		Preconditions.checkNotNull(entity);
		getCurrentSesstion().update(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		Preconditions.checkNotNull(entity);
		getCurrentSesstion().delete(entity);
		
	}

	@Override
	public void deleteById(String id) {
		T entity = findOne(id);
		Preconditions.checkState(entity != null);
		delete(entity);
	}

	@Override
	public List<T> queryList(String para, String value){
		String hql = "from " + clazz.getName()+" where "+para+" = :value";
		return getCurrentSesstion().createQuery(hql).setParameter("value", value).list();
	}
	
}
