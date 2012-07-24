package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.PropertyMeta;


public class BeanSortItem<BT, PT> {
	public enum BeanSortOperator{
		ASCENDING,
		DESCENDING
	}
	private final PropertyMeta<BT, PT> meta;
	private final BeanSortOperator operator;
	public BeanSortItem(PropertyMeta<BT, PT> meta, BeanSortOperator operator) {
		this.meta = meta;
		this.operator = operator;
	}
	public PropertyMeta<BT, PT> getMeta() {
		return meta;
	}
	public BeanSortOperator getOperator() {
		return operator;
	}
}
