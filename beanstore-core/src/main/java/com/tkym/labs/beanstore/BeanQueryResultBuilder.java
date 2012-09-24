package com.tkym.labs.beanstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tkym.labs.beanstore.api.BeanQueryResult;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanmeta.Key;


public class BeanQueryResultBuilder {
	public static <BT> BeanQueryResult<BT> buildAsBean(QueryResultFetcher<BT> fetcher){
		return new DefaultBeanQueryResult<BT>(fetcher);
	}
	
	public static <BT,KT> BeanQueryResult<Key<BT, KT>> buildAsKey(QueryResultFetcher<Key<BT, KT>> fetcher){
		return new DefaultBeanQueryResult<Key<BT, KT>>(fetcher);
	}
	
	public static <BT,KT,FT> QueryResultFetcher<Key<BT, KT>> buildConvertibleKeyFetcher(QueryResultFetchConverter<Key<BT, KT>,FT> converter, QueryResultFetcher<FT> fetcher){
		return new ConvertBeanQueryFetcher<Key<BT, KT>,FT>(converter, fetcher);
	}
	
	public static <BT,KT,FT> QueryResultFetcher<BT> buildConvertibleBeanFetcher(QueryResultFetchConverter<BT,FT> converter, QueryResultFetcher<FT> fetcher){
		return new ConvertBeanQueryFetcher<BT,FT>(converter, fetcher);
	}
	
	static class ConvertBeanQueryFetcher<QT,FT> implements QueryResultFetcher<QT>{
		private QueryResultFetchConverter<QT,FT> converter;
		private QueryResultFetcher<FT> fetcher;
		ConvertBeanQueryFetcher(QueryResultFetchConverter<QT,FT> converter, QueryResultFetcher<FT> fetcher){
			this.fetcher = fetcher;
			this.converter = converter;
		}
		@Override
		public Iterator<QT> iterator() throws Exception {
			return new AbstractConvertIterator<QT, FT>(fetcher.iterator()) {
				@Override
				protected QT convert(FT value){
					try {
						return converter.convert(value);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
		@Override
		public QT singleValue() throws Exception {
			return converter.convert(fetcher.singleValue());
		}
	} 
	
	public static abstract class AbstractConvertIterator<QT, FT> implements Iterator<QT> {
		private Iterator<FT> iterator;

		AbstractConvertIterator(Iterator<FT> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public QT next() {
			return convert(iterator.next());
		}

		protected abstract QT convert(FT value);

		@Override
		public void remove() {
			iterator.remove();
		}
	}
	
	public static interface QueryResultFetchConverter<QT,ST>{
		public QT convert(ST source) throws Exception;
	}
	
	public static interface QueryResultFetcher<QT>{
		public Iterator<QT> iterator() throws Exception;
		public QT singleValue() throws Exception;
	}
	
	public static class DefaultBeanQueryResult<QT> implements BeanQueryResult<QT>{
		private final QueryResultFetcher<QT> fetcher;
		DefaultBeanQueryResult(QueryResultFetcher<QT> fetcher){
			this.fetcher = fetcher;
		}
		@Override
		public QT asSingle() throws BeanstoreException {
			try {
				return fetcher.singleValue();
			} catch (Exception e) {
				throw new BeanstoreException(e);
			}
		}
		
		@Override
		public Iterator<QT> asIterator() throws BeanstoreException {
			try {
				return fetcher.iterator();
			} catch (Exception e) {
				throw new BeanstoreException(e);
			}
		}
		
		@Override
		public List<QT> asList() throws BeanstoreException {
			List<QT> list = new ArrayList<QT>();
			Iterator<QT> ite = asIterator();
			while (ite.hasNext()) list.add(ite.next());
			return list;
		}
	}
}