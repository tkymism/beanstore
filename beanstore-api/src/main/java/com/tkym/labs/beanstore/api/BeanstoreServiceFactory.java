package com.tkym.labs.beanstore.api;

public interface BeanstoreServiceFactory {
	public BeanstoreRootService create() throws BeanstoreException;
}
