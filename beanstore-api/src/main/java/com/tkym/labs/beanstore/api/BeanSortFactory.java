package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanSort.BeanSortOperator;

public class BeanSortFactory<BT,PT> {
	private PropertyMeta<BT, PT> meta;
	BeanSortFactory(PropertyMeta<BT, PT> meta){
		this.meta = meta;
	}
	public BeanSort<BT,PT> asc(){
		return new BeanSort<BT, PT>(meta, BeanSortOperator.ASCENDING);
	}
	public BeanSort<BT,PT> desc(){
		return new BeanSort<BT, PT>(meta, BeanSortOperator.DESCENDING);
	}
}