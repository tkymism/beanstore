package com.tkym.labs.beanstore;

import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetchConverter;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public abstract class AbstractBeanQueryConvertible<BT, KT, RBT, RKT> extends AbstractBeanQueryExecutor<BT, KT>{
	public AbstractBeanQueryConvertible(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		super(beanMeta, parent);
	}
	@Override
	public final QueryResultFetcher<BT> executeQueryAsBean(BeanQuerySource<BT, KT> objects) throws Exception {
		return BeanQueryResultBuilder.buildConvertibleBeanFetcher(convertRealToBean(), executeQueryAsBeanReal(objects));
	}
	protected abstract QueryResultFetcher<RBT> executeQueryAsBeanReal(BeanQuerySource<BT, KT> objects) throws Exception;
	protected abstract BT convertToBean(RBT source) throws Exception;
	private QueryResultFetchConverter<BT, RBT> convertRealToBean() throws Exception{
		return new QueryResultFetchConverter<BT, RBT>() {
			@Override
			public BT convert(RBT source) throws Exception{
				return convertToBean(source);
			}
		};
	}
	@Override
	public final QueryResultFetcher<Key<BT, KT>> executeQueryAsKey(BeanQuerySource<BT, KT> objects) throws Exception {
		return BeanQueryResultBuilder.buildConvertibleKeyFetcher(convertRealToKey(), executeQueryAsKeyReal(objects));
	}
	protected abstract QueryResultFetcher<RKT> executeQueryAsKeyReal(BeanQuerySource<BT, KT> objects) throws Exception;
	protected abstract Key<BT, KT> convertToKey(RKT source) throws Exception;
	private QueryResultFetchConverter<Key<BT, KT>, RKT> convertRealToKey() throws Exception{
		return new QueryResultFetchConverter<Key<BT, KT>, RKT>() {
			@Override
			public Key<BT, KT> convert(RKT source) throws Exception{
				return convertToKey(source);
			}
		};
	}
}