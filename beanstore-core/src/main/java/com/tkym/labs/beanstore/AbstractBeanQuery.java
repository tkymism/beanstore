package com.tkym.labs.beanstore;

import java.util.ArrayList;
import java.util.List;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanFilterBuilder;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryResult;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.beanstore.api.BeanstoreException;

public abstract class AbstractBeanQuery<BT, KT> implements BeanQuery<BT, KT>{
	protected final BeanMeta<BT, KT> beanMeta;
	protected final Key<?, ?> parent;
	protected final List<BeanFilterCriteria<BT>> filterList = new ArrayList<BeanFilterCriteria<BT>>();
	protected final List<BeanSortCriteria<BT>> sortList = new ArrayList<BeanSortCriteria<BT>>();
	protected final BeanQueryResultBuilder<BT, KT> resultBuilder = new BeanQueryResultBuilder<BT, KT>(); 
	public AbstractBeanQuery(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.beanMeta = beanMeta;
		this.parent = parent;
	}
	@Override
	public final BeanQuery<BT, KT> filter(BeanFilterCriteria<BT> filter){
		filterList.add(filter);
		return this;
	}
	
	@Override
	public final <PT> BeanFilterBuilder<BT, KT, PT> filter(PropertyMeta<BT, PT> meta) {
		return new DefaultBeanFilterBuilder<BT, KT, PT>(this, meta);
	}
	
	@Override
	public final BeanQuery<BT, KT> sort(BeanSortCriteria<BT> sort){
		sortList.add(sort);
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