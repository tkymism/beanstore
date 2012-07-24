package com.tkym.labs.beanstore.api;

public interface BeanstoreEventListener<BT, KT> {
	public void onChange(BeanstoreEvent<BT, KT> event);
}
