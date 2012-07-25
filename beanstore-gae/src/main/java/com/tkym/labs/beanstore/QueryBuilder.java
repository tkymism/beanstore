package com.tkym.labs.beanstore;


import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.tkym.labs.beanstore.BeanFilterItem.BeanFilterOperator;
import com.tkym.labs.beanstore.BeanSortItem.BeanSortOperator;

class QueryBuilder {
	private final Query query;
	private static final String HIGH_VALUE = "\ufffd";

	QueryBuilder(Query query) {
		this.query = query;
	}
	
	<T> QueryBuilder addFilter(BeanFilterItem<T, ?> filter){
		String propertyName = filter.getMeta().getPropertyName();
		Object[] values = filter.getValues();
		BeanFilterOperator ope = filter.getOperator();
		if (ope.equals(BeanFilterOperator.EQUAL)) 
			query.addFilter(propertyName, FilterOperator.EQUAL, values[0]);
		else if (ope.equals(BeanFilterOperator.NOT_EQUAL)) 
			query.addFilter(propertyName, FilterOperator.NOT_EQUAL, values[0]);
		else if (ope.equals(BeanFilterOperator.GREATER_THAN)) 
			query.addFilter(propertyName, FilterOperator.GREATER_THAN, values[0]);
		else if (ope.equals(BeanFilterOperator.GREATER_THAN_OR_EQUAL)) 
			query.addFilter(propertyName, FilterOperator.GREATER_THAN_OR_EQUAL, values[0]);
		else if (ope.equals(BeanFilterOperator.LESS_THAN)) 
			query.addFilter(propertyName, FilterOperator.LESS_THAN, values[0]);
		else if (ope.equals(BeanFilterOperator.LESS_THAN_OR_EQUAL)) 
			query.addFilter(propertyName, FilterOperator.LESS_THAN_OR_EQUAL, values[0]);
		else if (ope.equals(BeanFilterOperator.IN)) 
			query.addFilter(propertyName, FilterOperator.IN, values);
		else if (ope.equals(BeanFilterOperator.START_WITH)){
			String str = values[0].toString();
			query.addFilter(propertyName, FilterOperator.GREATER_THAN_OR_EQUAL, str);
			query.addFilter(propertyName, FilterOperator.LESS_THAN_OR_EQUAL, str+HIGH_VALUE);
		}else 
			throw new IllegalArgumentException("Filter of "+propertyName+" operator is unsupport :"+ope.toString());
		return this;
	}
	
	<T> QueryBuilder addSort(BeanSortItem<T, ?> sort){
		String propertyName = sort.getMeta().getPropertyName();
		BeanSortOperator ope = sort.getOperator();
		if (ope.equals(BeanSortOperator.ASCENDING))
			this.query.addSort(propertyName, SortDirection.ASCENDING);
		else if (ope.equals(BeanSortOperator.DESCENDING))
			this.query.addSort(propertyName, SortDirection.DESCENDING);
		return this;
	}
}