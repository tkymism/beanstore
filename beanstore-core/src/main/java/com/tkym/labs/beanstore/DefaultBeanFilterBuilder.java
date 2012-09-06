package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilterBuilder;
import com.tkym.labs.beanstore.api.BeanCriteriaBuilder;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanQueryUtils;

public class DefaultBeanFilterBuilder<BT, KT, PT> implements BeanFilterBuilder<BT, KT, PT>{
	private final BeanQuery<BT, KT> query;
	private final BeanCriteriaBuilder<BT, PT> builder;
	public DefaultBeanFilterBuilder(BeanQuery<BT, KT> query, PropertyMeta<BT, PT> propertyMeta){
		this.query = query;
		builder = BeanQueryUtils.property(propertyMeta);
	}
	@Override
	public BeanQuery<BT, KT> equalsTo(PT value) {
		return query.filter(builder.equalsTo(value));
	}
	@Override
	public BeanQuery<BT, KT> greaterThan(PT value) {
		return query.filter(builder.greaterThan(value));
	}
	@Override
	public BeanQuery<BT, KT> greaterEqual(PT value) {
		return query.filter(builder.greaterEqual(value));
	}
	@Override
	public BeanQuery<BT, KT> lessThan(PT value) {
		return query.filter(builder.lessThan(value));
	}
	@Override
	public BeanQuery<BT, KT> lessEqual(PT value) {
		return query.filter(builder.lessEqual(value));
	}
	@Override
	public BeanQuery<BT, KT> notEquals(PT value) {
		return query.filter(builder.notEquals(value));
	}
	@Override
	public BeanQuery<BT, KT> in(PT... value) {
		return query.filter(builder.in(value));
	}
	@Override
	public BeanQuery<BT, KT> startsWith(PT value) {
		return query.filter(builder.startsWith(value));
	}
	@Override
	public BeanQuery<BT, KT> endsWith(PT value) {
		return query.filter(builder.endsWith(value));
	}
	@Override
	public BeanQuery<BT, KT> contains(PT value) {
		return query.filter(builder.contains(value));
	}
}