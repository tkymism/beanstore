package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;
import com.tkym.labs.record.RecordstoreException;
import com.tkym.labs.record.RecordstoreServiceFactory;

public class BeanstoreServiceRecordFactory implements BeanstoreServiceFactory{
	private RecordstoreServiceFactory factory;
	public BeanstoreServiceRecordFactory(RecordstoreServiceFactory factory){
		this.factory = factory;
	}
	@Override
	public BeanstoreServiceRecord<Void,Void> create() throws BeanstoreException{
		try {
			return new BeanstoreServiceRecord<Void,Void>(factory.create());
		} catch (RecordstoreException e) {
			throw new BeanstoreException(e);
		}
	}
}