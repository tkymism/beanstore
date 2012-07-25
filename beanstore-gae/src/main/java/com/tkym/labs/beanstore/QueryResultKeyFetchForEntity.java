package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

import com.google.appengine.api.datastore.Entity;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetchConverter;

class QueryResultKeyFetchForEntity<BT,KT> implements QueryResultFetchConverter<Key<BT,KT>, Entity>{
	private final BeanMeta<BT, KT> beanMeta;
	private final com.google.appengine.api.datastore.Key ancestor;
	public QueryResultKeyFetchForEntity(BeanMeta<BT, KT> beanMeta, com.google.appengine.api.datastore.Key ancestor) {
		this.beanMeta = beanMeta;
		this.ancestor = ancestor;
	}
	@Override
	public Key<BT, KT> convert(Entity entity) throws Exception {
		return KeyConverterFactory.create(ancestor, this.beanMeta).convert(entity.getKey());
	}
}