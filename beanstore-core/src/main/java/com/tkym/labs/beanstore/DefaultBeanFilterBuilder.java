package com.tkym.labs.beanstore;

import java.util.List;

import com.tkym.labs.beanstore.BeanFilterItem.BeanFilterOperator;
import com.tkym.labs.beanstore.api.BeanFilterBuilder;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanmeta.PropertyMeta;

public class DefaultBeanFilterBuilder<BT, KT, PT> implements BeanFilterBuilder<BT, KT, PT>{
	private final List<BeanFilterItem<BT,?>> filterList;
	private final BeanQuery<BT, KT> query;
	private final PropertyMeta<BT, PT> propertyMeta;
	
	public DefaultBeanFilterBuilder(BeanQuery<BT, KT> query, PropertyMeta<BT, PT> propertyMeta, List<BeanFilterItem<BT,?>> filterList){
		this.propertyMeta = propertyMeta;
		this.query = query;
		this.filterList = filterList;
	}
	
	BeanQuery<BT, KT> filter(BeanFilterOperator ope, PT value){
		filterList.add(create(ope, propertyMeta, value));
		return query;
	}
	
	BeanQuery<BT, KT> filterAsArray(BeanFilterOperator ope, PT[] value){
		filterList.add(create(ope, propertyMeta, value));
		return query;
	}
	
	@SuppressWarnings("unchecked")
	BeanFilterItem<BT, PT> create(BeanFilterOperator ope, PropertyMeta<BT, PT> propertyMeta, PT value){
		return new BeanFilterItem<BT, PT>(propertyMeta, ope, value);
	}
	
	BeanFilterItem<BT, PT> create(BeanFilterOperator ope, PropertyMeta<BT, PT> propertyMeta, PT[] value){
		return new BeanFilterItem<BT, PT>(propertyMeta, ope, value);
	}
	
	@Override
	public BeanQuery<BT, KT> equalsTo(PT value) {
		return filter(BeanFilterOperator.EQUAL, value);
	}

	@Override
	public BeanQuery<BT, KT> greaterThan(PT value) {
		return filter(BeanFilterOperator.GREATER_THAN, value);
	}

	@Override
	public BeanQuery<BT, KT> greaterEqual(PT value) {
		return filter(BeanFilterOperator.GREATER_THAN_OR_EQUAL, value);
	}

	@Override
	public BeanQuery<BT, KT> lessThan(PT value) {
		return filter(BeanFilterOperator.LESS_THAN, value);
	}

	@Override
	public BeanQuery<BT, KT> lessEqual(PT value) {
		return filter(BeanFilterOperator.LESS_THAN_OR_EQUAL, value);
	}

	@Override
	public BeanQuery<BT, KT> notEquals(PT value) {
		return filter(BeanFilterOperator.NOT_EQUAL, value);
	}

	@Override
	public BeanQuery<BT, KT> in(PT... value) {
		return filterAsArray(BeanFilterOperator.IN, value);
	}

	@Override
	public BeanQuery<BT, KT> startsWith(PT value) {
		return filter(BeanFilterOperator.START_WITH, value);
	}

	@Override
	public BeanQuery<BT, KT> endsWith(PT value) {
		return filter(BeanFilterOperator.END_WITH, value);
	}

	@Override
	public BeanQuery<BT, KT> contains(PT value) {
		return filter(BeanFilterOperator.CONTAIN, value);
	}
}