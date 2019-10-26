package com.project1.dao;

import java.util.List;

public interface GenericDao <T> {
	List<T> getList();
	T getById(int id);
	List<T> getByUserId(int id);
	T getByUsername(String username);
	void insert(T t);
	void delete(T t);
}
