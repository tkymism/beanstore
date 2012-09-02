package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreService;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;

class BeanstoreServiceGae<PB,PK> extends AbstractBeanstoreService<PB,PK> {
	private DatastoreService datastoreService;
	
	BeanstoreServiceGae(AbstractBeanstoreService<Void,Void> rootService, Key<PB,PK> parent){
		super(rootService, parent);
		this.datastoreService = ((BeanstoreServiceGae<?,?>)rootService).getDatastoreService();
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
			BeanMeta<BT, KT> beanMeta, Key<PB,PK> parent) {
		return new BeanstoreGae<BT, KT>(super.getRoot(), beanMeta, parent, datastoreService);
	}
}