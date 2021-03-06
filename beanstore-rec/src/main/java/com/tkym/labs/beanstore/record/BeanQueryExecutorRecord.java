package com.tkym.labs.beanstore.record;

import static com.tkym.labs.beanstore.record.BeanCriteriaConverter.convert;

import java.util.Iterator;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.AbstractBeanQueryConvertible;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.beanstore.record.BeanMetaResolver.AncestorKeyStack;
import com.tkym.labs.record.QueryBuilder;
import com.tkym.labs.record.QueryResult;
import com.tkym.labs.record.Record;
import com.tkym.labs.record.RecordKey;
import com.tkym.labs.record.RecordstoreService;

class BeanQueryExecutorRecord<BT, KT> extends AbstractBeanQueryConvertible<BT, KT, Record, RecordKey> {
	private QueryBuilder queryBuilder;
	private final BeanMetaResolver<BT,KT> beanMetaResolver;
	
	BeanQueryExecutorRecord(RecordstoreService service, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		super(beanMeta, parent);
		this.beanMetaResolver = 
				BeanMetaResolverProvider.
				getInstance().
				get(beanMeta);
		this.queryBuilder = service.query(beanMetaResolver.getTableMeta());
		AncestorKeyStack stack = new AncestorKeyStack(parent);
		Object[] values = stack.getKeyValuesArray();
		for (int i=0; i<values.length; i++)
			this.queryBuilder.is(beanMetaResolver.getTableMeta().keyNames()[i], values[i]);
	}
	
	private QueryBuilder buildFilter(BeanFilterCriteria criteria){
		return this.queryBuilder.filter(convert(criteria));
	}
	
	private QueryBuilder buildSort(BeanSortCriteria criteria){
		return this.queryBuilder.sort(convert(criteria));
	}
	
	QueryBuilder preparedQuery(BeanQuerySource<BT, KT> objects){
		for (BeanFilterCriteria item : objects.filterList()) buildFilter(item);
		for (BeanSortCriteria item : objects.sortList()) buildSort(item);
		return this.queryBuilder;
	}
	
	@Override
	protected QueryResultFetcher<Record> executeQueryAsBeanReal(BeanQuerySource<BT, KT> objects) throws Exception {
		final QueryResult<Record> result = preparedQuery(objects).record();
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
	protected QueryResultFetcher<RecordKey> executeQueryAsKeyReal(BeanQuerySource<BT, KT> objects) throws Exception {
		final QueryResult<RecordKey> result = preparedQuery(objects).key();
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