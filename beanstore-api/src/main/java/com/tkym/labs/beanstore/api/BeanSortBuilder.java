package com.tkym.labs.beanstore.api;


public interface BeanSortBuilder<BT,KT> {
	public BeanQuery<BT,KT> asc();
	public BeanQuery<BT,KT> desc();
}