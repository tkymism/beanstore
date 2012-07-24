package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;
import com.tkym.labs.record.RecordstoreException;
import com.tkym.labs.record.RecordstoreServiceFactory;

public class BeanstoreServiceFactoryByRecord implements BeanstoreServiceFactory<BeanstoreServiceRecord>{
	private RecordstoreServiceFactory factory;
	public BeanstoreServiceFactoryByRecord(RecordstoreServiceFactory factory){
		this.factory = factory;
	}
	@Override
	public BeanstoreServiceRecord create() throws BeanstoreException{
		try {
			return new BeanstoreServiceRecord(factory.create());
		} catch (RecordstoreException e) {
			throw new BeanstoreException(e);
		}
	}
}