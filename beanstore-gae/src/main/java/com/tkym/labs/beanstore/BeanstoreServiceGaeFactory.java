package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreRootService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;

public class BeanstoreServiceGaeFactory implements BeanstoreServiceFactory{
	@Override
	public BeanstoreRootService create() throws BeanstoreException {
    	return new BeanstoreServiceGae(DatastoreServiceFactory.getDatastoreService());
	}
}