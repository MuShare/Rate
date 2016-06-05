package edu.ut.softlab.rate.service.common;

import java.io.Serializable;
import java.util.List;

import edu.ut.softlab.rate.dao.common.IOperations;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {
	
	protected abstract IOperations<T> getDao();

	@Override
	public T findOne(String id) {
		
		return getDao().findOne(id);
	}

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	@Override
	public void create(T entity) {
		getDao().create(entity);
	}

	@Override
	public T update(T entity) {
		return getDao().update(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().delete(entity);
	}

	@Override
	public void deleteById(String id) {
		getDao().deleteById(id);
	}

	@Override
	public List<T> queryList(String para, String value){return getDao().queryList(para, value);}

}
