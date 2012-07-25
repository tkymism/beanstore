package com.tkym.labs.beanstore;

import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

class BeanQueryGae<BT, KT> extends AbstractBeanQueryConvertible<BT, KT, Entity, Entity>{
	private final DatastoreService datastoreService;
	private final com.google.appengine.api.datastore.Key ancestor;
	BeanQueryGae(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent, DatastoreService datastoreService) {
		super(beanMeta, parent);
		this.datastoreService = datastoreService;
		if (parent != null)
			this.ancestor = KeyConverterFactory.convertKey(parent);
		else
			this.ancestor = null;
	}
	
	private Query createQuery(){
		Query query = new Query(beanMeta.getName(), ancestor);
		QueryBuilder builder = new QueryBuilder(query);
		for (BeanFilterItem<BT, ?> filter : super.filterItemList)
			builder.addFilter(filter);
		for (BeanSortItem<BT, ?> sort : super.sortItemList)
			builder.addSort(sort);
		return query;
	}

	@Override
	protected QueryResultFetcher<Entity> executeQueryAsBeanReal() throws Exception {
		return new QueryResultFetcherForEntity(datastoreService.prepare(createQuery()));
	}

	@Override
	protected QueryResultFetcher<Entity> executeQueryAsKeyReal() throws Exception {
		return new QueryResultFetcherForEntity(datastoreService.prepare(createQuery().setKeysOnly()));
	}

	@Override
	protected BT convertToBean(Entity entity) throws Exception {
		return KeyConverterFactory.create(ancestor, beanMeta).convert(entity);
	}

	@Override
	protected Key<BT, KT> convertToKey(Entity entity) throws Exception {
		return KeyConverterFactory.create(ancestor, beanMeta).convert(entity.getKey());
	}
}