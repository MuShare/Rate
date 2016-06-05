package edu.ut.softlab.rate.dao.common;

import java.io.Serializable;

import java.util.List;

public interface IOperations<T extends Serializable> {
	T findOne(String id);

	List<T> findAll();

	void create(T entity);

	T update(T entity);

	List<T> queryList(String para, String value);

	void delete(T entity);

	void deleteById(String id);

}
