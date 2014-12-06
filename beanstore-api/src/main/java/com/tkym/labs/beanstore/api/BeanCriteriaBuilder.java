package com.tkym.labs.beanstore.api;

import static com.tkym.labs.beanstore.api.BeanSort.BeanSortOperator.ASCENDING;
import static com.tkym.labs.beanstore.api.BeanSort.BeanSortOperator.DESCENDING;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilter.BeanFilterOperator;

public class BeanCriteriaBuilder<BT,PT> {
	private PropertyMeta<BT, PT> meta;
	BeanCriteriaBuilder(PropertyMeta<BT, PT> meta){
		this.meta = meta;
	}
	public BeanFilter<BT,PT> equalsTo(PT value){
		return filter(BeanFilterOperator.EQUAL, value);
	}
	public BeanFilter<BT,PT> greaterThan(PT value){
		return filter(BeanFilterOperator.GREATER_THAN, value);
	}
	public BeanFilter<BT,PT> greaterEqual(PT value){
		return filter(BeanFilterOperator.GREATER_THAN_OR_EQUAL, value);
	}
	public BeanFilter<BT,PT> lessThan(PT value){
		return filter(BeanFilterOperator.LESS_THAN, value);
	}
	public BeanFilter<BT,PT> lessEqual(PT value){
		return filter(BeanFilterOperator.LESS_THAN_OR_EQUAL, value);
	}
	public BeanFilter<BT,PT> notEquals(PT value){
		return filter(BeanFilterOperator.NOT_EQUAL, value);
	}
	@SuppressWarnings("unchecked") 
	public BeanFilter<BT,PT> in(PT... value){
		return new BeanFilter<BT, PT>(meta, BeanFilterOperator.IN, value);
	}
	public BeanFilter<BT,PT> startsWith(PT value){
		return filter(BeanFilterOperator.START_WITH, value);
	}
	public BeanFilter<BT,PT> endsWith(PT value){
		return filter(BeanFilterOperator.END_WITH, value);
	}
	public BeanFilter<BT,PT> contains(PT value){
		return filter(BeanFilterOperator.CONTAIN, value);
	}
	@SuppressWarnings("unchecked")
	BeanFilter<BT,PT> filter(BeanFilterOperator ope, PT value){
		return new BeanFilter<BT, PT>(meta, ope, value);
	}
	public BeanSort<BT,PT> asc(){
		return new BeanSort<BT, PT>(meta, ASCENDING);
	}
	public BeanSort<BT,PT> desc(){
		return new BeanSort<BT, PT>(meta, DESCENDING);
	}
}