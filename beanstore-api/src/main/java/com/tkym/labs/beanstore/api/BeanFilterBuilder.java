package com.tkym.labs.beanstore.api;


public interface BeanFilterBuilder<BT,KT,PT> {
	public BeanQuery<BT,KT> equalsTo(PT value);
	public BeanQuery<BT,KT> greaterThan(PT value);
	public BeanQuery<BT,KT> greaterEqual(PT value);
	public BeanQuery<BT,KT> lessThan(PT value);
	public BeanQuery<BT,KT> lessEqual(PT value);
	public BeanQuery<BT,KT> notEquals(PT value);
	public BeanQuery<BT,KT> in(PT... value);
	public BeanQuery<BT,KT> startsWith(PT value);
	public BeanQuery<BT,KT> endsWith(PT value);
	public BeanQuery<BT,KT> contains(PT value);
}