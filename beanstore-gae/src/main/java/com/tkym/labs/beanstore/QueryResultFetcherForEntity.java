package com.tkym.labs.beanstore;

import java.util.Iterator;


import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;

class QueryResultFetcherForEntity implements QueryResultFetcher<Entity>{
	private final PreparedQuery preparedQuery;
	QueryResultFetcherForEntity(PreparedQuery preparedQuery){
		this.preparedQuery = preparedQuery;
	}
	
	@Override
	public Iterator<Entity> iterator() throws Exception {
		return preparedQuery.asIterator();
	}

	@Override
	public Entity singleValue() throws Exception {
		return preparedQuery.asSingleEntity();
	}
}