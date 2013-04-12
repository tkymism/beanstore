package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreRootService;

public abstract class AbstractBeanstoreRootService implements BeanstoreRootService{
	@Override
	public <BT, KT extends Comparable<KT>> Beanstore<BT, KT> store(BeanMeta<BT, KT> meta) {
		return createBeanstore(meta,null);
	}
	@Override
	public <BT, KT extends Comparable<KT>> BeanQuery<BT, KT> query(BeanMeta<BT, KT> meta) {
		return createBeanQuery(meta,null);
	}
	<BT, KT extends Comparable<KT>> DefaultBeanQuery<BT, KT> createBeanQuery(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent){
		return new DefaultBeanQuery<BT, KT>(createBeanQueryExecutor(beanMeta, parent));
	}
	protected abstract <BT, KT extends Comparable<KT>> AbstractBeanstore<BT, KT> createBeanstore(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent);
	protected abstract <BT, KT extends Comparable<KT>> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent);
}