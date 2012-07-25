package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Transaction;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

class BeanstoreTransactionGae implements BeanstoreTransaction {
	private DatastoreService datastoreService;
	private Transaction transaction;
	BeanstoreTransactionGae(DatastoreService datastoreService){
		this.datastoreService = datastoreService;
		this.transaction = datastoreService.beginTransaction();
	}
	@Override
	public void commit() throws BeanstoreException {
		this.transaction.commit();
		this.transaction = datastoreService.beginTransaction();
	}

	@Override
	public void rollback() throws BeanstoreException {
		this.transaction.rollback();
		this.transaction = datastoreService.beginTransaction();
	}

	@Override
	public void close() throws BeanstoreException {
		throw new UnsupportedOperationException("#close is unsupported");
	}
}
