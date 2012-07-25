package com.tkym.labs.beanstore;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class BeanstoreServiceGaeFactory implements BeanstoreServiceFactory<BeanstoreService>{
	@Override
	public BeanstoreService create() throws BeanstoreException {
    	return new BeanstoreServiceGae(DatastoreServiceFactory.getDatastoreService());
	}
}