package com.tkym.labs.beanstore;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class BeanstoreServiceGaeFactory implements BeanstoreServiceFactory{
	@Override
	public BeanstoreService<Void,Void> create() throws BeanstoreException {
    	return new BeanstoreServiceGae<Void,Void>(DatastoreServiceFactory.getDatastoreService());
	}
}