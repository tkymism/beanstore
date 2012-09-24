package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreService;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

class BeanstoreServiceGae extends AbstractBeanstoreRootService {
	private final DatastoreService datastoreService;
	private final BeanstoreTransaction transaction;
	BeanstoreServiceGae(DatastoreService datastoreService) {
		this.datastoreService = datastoreService;
		this.transaction = new BeanstoreTransactionGae(datastoreService);
	}

	@Override
	public BeanstoreTransaction getTransaction() {
		return this.transaction;
	}

	@Override
	protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
			BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		return new BeanstoreGae<BT, KT>(this, beanMeta, parent, datastoreService);
	}
	DatastoreService getDatastoreService(){
		return datastoreService;
	}
	
	@Override
	protected <BT, KT> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
			BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		return new BeanQueryExecutorGae<BT, KT>(beanMeta, parent, datastoreService);
	}
}