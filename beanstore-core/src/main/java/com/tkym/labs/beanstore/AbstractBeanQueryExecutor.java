package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanQuerySource;

public abstract class AbstractBeanQueryExecutor<BT, KT> {
	protected final Key<?, ?> parent;
	protected final BeanMeta<BT, KT> beanMeta;
	AbstractBeanQueryExecutor(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.parent = parent;
		this.beanMeta = beanMeta;
	}
	Key<?, ?> getParent() {
		return parent;
	}
	BeanMeta<BT, KT> getBeanMeta() {
		return beanMeta;
	}
	public abstract QueryResultFetcher<BT> executeQueryAsBean(BeanQuerySource<BT, KT> objects) throws Exception;
	public abstract QueryResultFetcher<Key<BT, KT>> executeQueryAsKey(BeanQuerySource<BT, KT> objects) throws Exception;
}
