package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;

public class BeanFilter<BT,PT> implements BeanFilterCriteria{
	public enum BeanFilterOperator{
		EQUAL,
		NOT_EQUAL,
		LESS_THAN_OR_EQUAL,
		LESS_THAN,
		GREATER_THAN_OR_EQUAL,
		GREATER_THAN,
		IN,
		START_WITH,
		END_WITH,
		CONTAIN
	}
	private final PropertyMeta<BT, PT> meta;
	private final PT[] values;
	private final BeanFilterOperator operator;
	BeanFilter(PropertyMeta<BT, PT> meta, BeanFilterOperator operator, PT... values) {
		this.meta = meta;
		this.values = values;
		this.operator = operator;
	}
	
	public PropertyMeta<BT, PT> getMeta() {
		return meta;
	}
	public PT[] getValues() {
		return values;
	}
	public BeanFilterOperator getOperator() {
		return operator;
	}
	public PT value(){
		return values[0];
	}
}
