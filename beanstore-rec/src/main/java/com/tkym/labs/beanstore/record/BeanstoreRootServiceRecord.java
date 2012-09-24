package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.AbstractBeanQueryExecutor;
import com.tkym.labs.beanstore.AbstractBeanstore;
import com.tkym.labs.beanstore.AbstractBeanstoreRootService;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;
import com.tkym.labs.record.RecordstoreService;

public class BeanstoreRootServiceRecord extends AbstractBeanstoreRootService{
	private final RecordstoreService recordstoreService;
	private final BeanstoreTransaction transaction;
	BeanstoreRootServiceRecord(RecordstoreService recordstoreService){
		this.recordstoreService = recordstoreService;
		this.transaction = new BeanstoreTransactionRecord(
				this.recordstoreService.getTransaction());
	}
	RecordstoreService getRecordstoreService(){
		return recordstoreService;
	}
	public <BT, KT> void create(BeanMeta<BT, KT> beanMeta) throws Exception {
		recordstoreService.create(BeanMetaResolverProvider.getInstance().get(beanMeta).getTableMeta(), false);
	}
	public <BT, KT> void drop(BeanMeta<BT, KT> beanMeta) throws Exception {
		recordstoreService.drop(BeanMetaResolverProvider.getInstance().get(beanMeta).getTableMeta(), false);
	}
	@Override
	public BeanstoreTransaction getTransaction() {
		return transaction;
	}
	
	@Override
	protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
			BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		return new BeanstoreRecord<BT, KT>(this, beanMeta, parent);
	}
	@Override
	protected <BT, KT> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
			BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		return new BeanQueryExecutorRecord<BT, KT>(recordstoreService, beanMeta, parent);
	}
}
