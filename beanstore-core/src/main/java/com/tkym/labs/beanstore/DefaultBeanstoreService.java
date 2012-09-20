package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class DefaultBeanstoreService<PB,PK> implements BeanstoreService<PB,PK>{
	private final AbstractBeanstoreRootService rootService;
	protected final Key<PB,PK> parent;
	protected DefaultBeanstoreService(AbstractBeanstoreRootService rootService, Key<PB,PK> parent){
		this.rootService = rootService;
		this.parent = parent;
	}
	@Override
	public <BT, KT> BeanQuery<BT, KT> query(BeanMeta<BT, KT> meta) {
		checkMeta(meta);
		return rootService.createBeanQuery(meta, parent);
	}
	@Override
	public final <BT, KT> Beanstore<BT, KT> store(BeanMeta<BT, KT> meta) {
		checkMeta(meta);
		return rootService.createBeanstore(meta, parent);
	}
	private <BT, KT> void checkMeta(BeanMeta<BT, KT> meta){
		if(!BeanstoreUtils.isSupport(parent, meta))
			throw new IllegalArgumentException(
					"meta.parent="+
							meta.parent().getBeanType().getName()+
							" is unsuport service="+
							parent.getBeanMeta().getBeanType().getName());
	}
	@Override
	public BeanstoreTransaction getTransaction() {
		return rootService.getTransaction();
	}
}