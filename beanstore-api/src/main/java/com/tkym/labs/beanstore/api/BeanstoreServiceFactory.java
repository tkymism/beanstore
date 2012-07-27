package com.tkym.labs.beanstore.api;

public interface BeanstoreServiceFactory {
	public BeanstoreService<Void,Void> create() throws BeanstoreException;
}
