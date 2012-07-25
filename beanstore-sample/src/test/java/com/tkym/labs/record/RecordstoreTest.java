package com.tkym.labs.record;

import com.tkym.labs.beanstore.record.BeanstoreServiceRecordFactory;
import com.tkym.labs.beanstore.record.BeanstoreServiceRecord;

import com.tkym.labs.uke.IyakuhinMeta;

public class RecordstoreTest {
	private static BeanstoreServiceRecord service; 
	
//	@Test
	public void setupClass() throws Exception{
		RecordstoreServiceFactory factory = new SqliteRecordstoreServiceFactory();
		service = new BeanstoreServiceRecordFactory(factory).create();
		service.create(IyakuhinMeta.get());
		service.drop(IyakuhinMeta.get());
		service.getTransaction().close();
	}
}
