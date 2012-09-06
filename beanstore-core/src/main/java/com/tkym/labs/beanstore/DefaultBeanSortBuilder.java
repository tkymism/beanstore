package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryUtils;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.beanstore.api.BeanSortFactory;

public class DefaultBeanSortBuilder<BT, KT, PT> implements BeanSortBuilder<BT, KT>{
	private final BeanQuery<BT, KT> query;
	private final BeanSortFactory<BT, PT> factory;
	DefaultBeanSortBuilder(BeanQuery<BT, KT> query, PropertyMeta<BT, PT> propertyMeta){
		factory = BeanQueryUtils.sort(propertyMeta);
		this.query = query;
	}
	@Override
	public BeanQuery<BT, KT> asc() {
		return query.sort(factory.asc());
	}

	@Override
	public BeanQuery<BT, KT> desc() {
		return query.sort(factory.desc());
	}
}
