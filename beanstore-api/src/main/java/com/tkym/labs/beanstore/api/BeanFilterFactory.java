package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilter.BeanFilterOperator;

public class BeanFilterFactory<BT,PT> {
	private PropertyMeta<BT, PT> meta;
	BeanFilterFactory(PropertyMeta<BT, PT> meta){
		this.meta = meta;
	}
	public BeanFilter<BT,PT> equalsTo(PT value){
		return create(BeanFilterOperator.EQUAL, value);
	}
	public BeanFilter<BT,PT> greaterThan(PT value){
		return create(BeanFilterOperator.GREATER_THAN, value);
	}
	public BeanFilter<BT,PT> greaterEqual(PT value){
		return create(BeanFilterOperator.GREATER_THAN_OR_EQUAL, value);
	}
	public BeanFilter<BT,PT> lessThan(PT value){
		return create(BeanFilterOperator.LESS_THAN, value);
	}
	public BeanFilter<BT,PT> lessEqual(PT value){
		return create(BeanFilterOperator.LESS_THAN_OR_EQUAL, value);
	}
	public BeanFilter<BT,PT> notEquals(PT value){
		return create(BeanFilterOperator.NOT_EQUAL, value);
	}
	public BeanFilter<BT,PT> in(PT... value){
		return new BeanFilter<BT, PT>(meta, BeanFilterOperator.IN, value);
	}
	public BeanFilter<BT,PT> startsWith(PT value){
		return create(BeanFilterOperator.START_WITH, value);
	}
	public BeanFilter<BT,PT> endsWith(PT value){
		return create(BeanFilterOperator.END_WITH, value);
	}
	public BeanFilter<BT,PT> contains(PT value){
		return create(BeanFilterOperator.CONTAIN, value);
	}
	@SuppressWarnings("unchecked")
	BeanFilter<BT,PT> create(BeanFilterOperator ope, PT value){
		return new BeanFilter<BT, PT>(meta, ope, value);
	}
}