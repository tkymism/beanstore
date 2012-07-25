package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreService;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;

class BeanstoreServiceGae extends AbstractBeanstoreService {
	private DatastoreService datastoreService;
	
	BeanstoreServiceGae(AbstractBeanstoreService rootService, Key<?,?> parent){
		super(rootService, parent);
		this.datastoreService = ((BeanstoreServiceGae)rootService).getDatastoreService();
	}
	
	BeanstoreServiceGae(DatastoreService datastoreService){
		super(new BeanstoreTransactionGae(datastoreService));
		this.datastoreService = datastoreService;
	}
	
	DatastoreService getDatastoreService(){
		return datastoreService;
	}
	
	public <BT, KT> BeanQuery<BT, KT> query(BeanMeta<BT, KT> meta) {
		return new BeanQueryGae<BT, KT>(meta, parent, datastoreService);
	}

	@Override
	protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
			BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		return new BeanstoreGae<BT, KT>(super.getRoot(), beanMeta, parent, datastoreService);
	}
}