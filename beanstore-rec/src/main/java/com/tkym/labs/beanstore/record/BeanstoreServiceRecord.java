package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanstore.AbstractBeanstore;
import com.tkym.labs.beanstore.AbstractBeanstoreService;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.record.RecordstoreService;

public class BeanstoreServiceRecord<PB, PK> extends AbstractBeanstoreService<PB, PK>{
	private final RecordstoreService recordstoreService;
	
	BeanstoreServiceRecord(RecordstoreService recordstoreService){
		super(new BeanstoreTransactionRecord(recordstoreService.getTransaction()));
		this.recordstoreService = recordstoreService;
	}
	
	BeanstoreServiceRecord(AbstractBeanstoreService<?, ?> root, Key<PB, PK> parent){
		super(root, parent);
		this.recordstoreService = ((BeanstoreServiceRecord<?,?>)root).recordstoreService;
	}
	
	RecordstoreService getRecordstoreService(){
		return this.recordstoreService;
	}
	
	@Override
	protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(BeanMeta<BT, KT> beanMeta, Key<PB, PK> parent) {
		return new BeanstoreRecord<BT, KT>(getRoot(), beanMeta, parent);
	}
	
	public <BT, KT> void create(BeanMeta<BT, KT> beanMeta) throws Exception {
		recordstoreService.create(BeanMetaResolverProvider.getInstance().get(beanMeta).getTableMeta(), false);
	}

	public <BT, KT> void drop(BeanMeta<BT, KT> beanMeta) throws Exception {
		recordstoreService.drop(BeanMetaResolverProvider.getInstance().get(beanMeta).getTableMeta(), false);
	}
	
	@Override
	public <BT, KT> BeanQuery<BT, KT> query(BeanMeta<BT,KT> beanMeta) {
		return new BeanQueryRecord<BT, KT>(recordstoreService, beanMeta, parent);
	}
}