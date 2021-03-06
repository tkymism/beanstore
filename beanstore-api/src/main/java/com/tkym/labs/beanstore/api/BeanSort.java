package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;

public class BeanSort<BT,PT> implements BeanSortCriteria{
	public enum BeanSortOperator{
		ASCENDING,
		DESCENDING
	}
	private final PropertyMeta<BT, PT> meta;
	private final BeanSortOperator operator;
	BeanSort(PropertyMeta<BT, PT> meta, BeanSortOperator operator) {
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
