package com.tkym.labs.beanstore.api;

public interface BeanstoreServiceFactory<S extends BeanstoreService> {
	public S create() throws BeanstoreException;
}
