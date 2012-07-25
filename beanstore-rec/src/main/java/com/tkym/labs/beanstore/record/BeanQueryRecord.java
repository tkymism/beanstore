package com.tkym.labs.beanstore.record;

import java.util.Iterator;

import com.tkym.labs.beanstore.AbstractBeanQueryConvertible;
import com.tkym.labs.beanstore.BeanFilterItem;
import com.tkym.labs.beanstore.BeanSortItem;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.record.BeanMetaResolver.AncestorKeyStack;
import com.tkym.labs.record.QueryBuilder;
import com.tkym.labs.record.QueryFilterBuilder;
import com.tkym.labs.record.QueryResult;
import com.tkym.labs.record.QuerySortBuilder;
import com.tkym.labs.record.Record;
import com.tkym.labs.record.RecordKey;
import com.tkym.labs.record.RecordstoreService;

class BeanQueryRecord<BT, KT> extends AbstractBeanQueryConvertible<BT, KT, Record, RecordKey> {
	private QueryBuilder queryBuilder;
	private final BeanMetaResolver<BT,KT> beanMetaResolver;
	
	BeanQueryRecord(RecordstoreService service, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		super(beanMeta, parent);
		this.beanMetaResolver = BeanMetaResolverProvider.getInstance().get(beanMeta);
		this.queryBuilder = service.query(beanMetaResolver.getTableMeta());
		AncestorKeyStack stack = new AncestorKeyStack(parent);
		Object[] values = stack.getKeyValuesArray();
		for (int i=0; i<values.length; i++)
			this.queryBuilder.is(beanMetaResolver.getTableMeta().keyNames()[i], values[i]);
	}
	
	<PT> QueryBuilder putItem(BeanSortItem<BT, PT> item){
		QuerySortBuilder builder = this.queryBuilder.sort(item.getMeta().getPropertyName());
		switch (item.getOperator()) {
		case ASCENDING:
			return builder.asc();
		case DESCENDING:
			return builder.desc();
		default:
			throw new IllegalArgumentException(
					"BeanSortOperator is illegal type :" + item.getOperator());
		}
	}
	
	<PT> QueryBuilder putItem(BeanFilterItem<BT, PT> item){
		QueryFilterBuilder<PT> builder = this.queryBuilder.filter(item.getMeta().getPropertyName(), item.getMeta().getPropertyType());
		switch(item.getOperator()){
		case EQUAL: 
			return builder.equalsTo(item.getValues()[0]);
		case NOT_EQUAL: 
			return builder.notEquals(item.getValues()[0]);
		case LESS_THAN_OR_EQUAL: 
			return builder.lessEqual(item.getValues()[0]);
		case LESS_THAN: 
			return builder.lessThan(item.getValues()[0]);
		case GREATER_THAN_OR_EQUAL:
			return builder.greaterEqual(item.getValues()[0]);
		case GREATER_THAN: 
			return builder.greaterThan(item.getValues()[0]);
		case IN: 
			return builder.in(item.getValues());
		case START_WITH: 
			return builder.startsWith(item.getValues()[0]);
		case END_WITH: 
			return builder.endsWith(item.getValues()[0]);
		case CONTAIN: 
			return builder.contains(item.getValues()[0]);
		default:
			throw new IllegalArgumentException(
					"BeanFilterOperator is illegal type :" + item.getOperator());
		}
	}
	
	QueryBuilder preparedQuery(){
		for (BeanFilterItem<BT, ?> item : filterItemList) putItem(item);
		for (BeanSortItem<BT, ?> item : sortItemList) putItem(item);
		return this.queryBuilder;
	}
	
	@Override
	protected QueryResultFetcher<Record> executeQueryAsBeanReal() throws Exception {
		final QueryResult<Record> result = preparedQuery().record();
		return new QueryResultFetcher<Record>() {
			@Override
			public Iterator<Record> iterator() throws Exception {
				return result.asIterator();
			}
			@Override
			public Record singleValue() throws Exception {
				return result.asSingleValue();
			}
		};
	}

	@Override
	protected QueryResultFetcher<RecordKey> executeQueryAsKeyReal() throws Exception {
		final QueryResult<RecordKey> result = preparedQuery().key();
		return new QueryResultFetcher<RecordKey>() {
			@Override
			public Iterator<RecordKey> iterator() throws Exception{
				return result.asIterator();
			}

			@Override
			public RecordKey singleValue() throws Exception{
				return result.asSingleValue();
			}
		};
	}

	@Override
	protected BT convertToBean(Record record) throws Exception {
		BT bean = beanMeta.newInstance();
		for (String propertyName : beanMeta.getPropertyNames()) {
			Object value;
			if (beanMeta.getKeyPropertyMeta().getPropertyName()
					.equals(propertyName))
				value = convertToKey(record.key()).value();
			else
				value = record.get(propertyName);
			beanMeta.getPropertyMeta(propertyName).access(bean).set(value);
		}
		return bean;
	}
	
	@Override
	protected Key<BT, KT> convertToKey(RecordKey recordKey) throws Exception {
		return beanMetaResolver.getKeyConverter().convert(recordKey);
	}
}