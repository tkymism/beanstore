package com.tkym.labs.beanstore.api;

public interface BeanstoreTransaction {
	public void commit() throws BeanstoreException;
	public void rollback() throws BeanstoreException;
	public void close() throws BeanstoreException;
}