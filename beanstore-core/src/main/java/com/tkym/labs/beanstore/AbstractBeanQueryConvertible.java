package com.tkym.labs.beanstore;

import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetchConverter;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public abstract class AbstractBeanQueryConvertible<BT, KT, RBT, RKT> extends AbstractBeanQuery<BT, KT>{
	public AbstractBeanQueryConvertible(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		super(beanMeta, parent);
	}
	@Override
	protected final QueryResultFetcher<BT> executeQueryAsBean() throws Exception {
		return resultBuilder.buildConvertibleBeanFetcher(convertRealToBean(), executeQueryAsBeanReal());
	}
	protected abstract QueryResultFetcher<RBT> executeQueryAsBeanReal() throws Exception;
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
	protected final QueryResultFetcher<Key<BT, KT>> executeQueryAsKey() throws Exception {
		return resultBuilder.buildConvertibleKeyFetcher(convertRealToKey(), executeQueryAsKeyReal());
	}
	protected abstract QueryResultFetcher<RKT> executeQueryAsKeyReal() throws Exception;
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