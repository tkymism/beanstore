package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;
import com.tkym.labs.record.RecordstoreException;
import com.tkym.labs.record.RecordstoreTransaction;

public class BeanstoreTransactionRecord implements BeanstoreTransaction{
	private RecordstoreTransaction datastoreTransaction;
	BeanstoreTransactionRecord(RecordstoreTransaction datastoreTransaction){
		this.datastoreTransaction = datastoreTransaction;
	}
	@Override
	public void commit() throws BeanstoreException {
		try {
			this.datastoreTransaction.commit();
		} catch (RecordstoreException e) {
			throw new BeanstoreException(e);
		}
	}
	@Override
	public void rollback() throws BeanstoreException {
		try {
			this.datastoreTransaction.rollback();
		} catch (RecordstoreException e) {
			throw new BeanstoreException(e);
		}
	}
	@Override
	public void close() throws BeanstoreException {
		try {
			this.datastoreTransaction.close();
		} catch (RecordstoreException e) {
			throw new BeanstoreException(e);
		}
	}
}
