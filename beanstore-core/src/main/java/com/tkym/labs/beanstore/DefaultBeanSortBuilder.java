package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanCriteriaBuilder;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryUtils;
import com.tkym.labs.beanstore.api.BeanSortBuilder;

public class DefaultBeanSortBuilder<BT, KT, PT> implements BeanSortBuilder<BT, KT>{
	private final BeanQuery<BT, KT> query;
	private final BeanCriteriaBuilder<BT, PT> builder;
	DefaultBeanSortBuilder(BeanQuery<BT, KT> query, PropertyMeta<BT, PT> propertyMeta){
		builder = BeanQueryUtils.property(propertyMeta);
		this.query = query;
	}
	@Override
	public BeanQuery<BT, KT> asc() {
		return query.sort(builder.asc());
	}

	@Override
	public BeanQuery<BT, KT> desc() {
		return query.sort(builder.desc());
	}
}
