package com.tkym.labs.beanstore.api;

public interface BeanstoreTransaction {
	public void commit();
	public void rollback();
	public void close();
}