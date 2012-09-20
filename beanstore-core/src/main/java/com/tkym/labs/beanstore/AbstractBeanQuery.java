package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanFilterBuilder;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryBuilder;
import com.tkym.labs.beanstore.api.BeanQueryResult;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.beanstore.api.BeanstoreException;

public abstract class AbstractBeanQuery<BT, KT> implements BeanQuery<BT, KT>{
	private final BeanQueryBuilder<BT,KT> builder;
	protected final BeanMeta<BT, KT> beanMeta;
	protected final Key<?, ?> parent;
	protected final BeanQueryResultBuilder<BT, KT> resultBuilder = new BeanQueryResultBuilder<BT, KT>(); 
	public AbstractBeanQuery(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.beanMeta = beanMeta;
		this.builder = BeanQueryBuilder.create(beanMeta);
		this.parent = parent;
	}
	protected final BeanQuerySource<BT, KT> getQuerySource(){
		return builder.getQuerySource();
	}
	@Override
	public final BeanQuery<BT, KT> filter(BeanFilterCriteria filter){
		builder.filter(filter);
		return this;
	}
	
	@Override
	public final <PT> BeanFilterBuilder<BT, KT, PT> filter(PropertyMeta<BT, PT> meta) {
		return new DefaultBeanFilterBuilder<BT, KT, PT>(this, meta);
	}
	
	@Override
	public final BeanQuery<BT, KT> sort(BeanSortCriteria sort){
		builder.sort(sort);
		return this;
	}
	
	@Override
	public final <PT> BeanSortBuilder<BT, KT> sort(PropertyMeta<BT, PT> meta) {
		return new DefaultBeanSortBuilder<BT, KT, PT>(this, meta);
	}
	
	@Override
	public final BeanQueryResult<BT> bean() throws BeanstoreException {
		try {
			return resultBuilder.buildAsBean(executeQueryAsBean());
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract QueryResultFetcher<BT> executeQueryAsBean() throws Exception;
	
	@Override
	public BeanQueryResult<Key<BT, KT>> key() throws BeanstoreException {
		try {
			return resultBuilder.buildAsKey(executeQueryAsKey());
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	protected abstract QueryResultFetcher<Key<BT, KT>> executeQueryAsKey() throws Exception;
}