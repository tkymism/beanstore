package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilterBuilder;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryBuilder;
import com.tkym.labs.beanstore.api.BeanQueryResult;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.beanstore.api.BeanstoreException;

public class DefaultBeanQuery<BT, KT> implements BeanQuery<BT, KT>{
	private final BeanQueryBuilder<BT,KT> builder;
	private final AbstractBeanQueryExecutor<BT, KT> executor;
	protected final BeanMeta<BT, KT> beanMeta;
	public DefaultBeanQuery(AbstractBeanQueryExecutor<BT, KT> executor) {
		this.executor = executor;
		this.beanMeta = executor.getBeanMeta();
		this.builder = BeanQueryBuilder.create(beanMeta);
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
			return BeanQueryResultBuilder.buildAsBean(executor.executeQueryAsBean(builder.getQuerySource()));
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	@Override
	public BeanQueryResult<Key<BT, KT>> key() throws BeanstoreException {
		try {
			return BeanQueryResultBuilder.buildAsKey(executor.executeQueryAsKey(builder.getQuerySource()));
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
}