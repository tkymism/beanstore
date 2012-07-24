package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.Key;

public class BeanstoreEvent<B,K> {
	public static enum BeanstoreEventType{INSERT, UPDATE, DELETE}
	private final BeanstoreEventType type;
	private final B oldValue;
	private final B newValue;
	private final Key<B, K> key;
	public BeanstoreEvent(BeanstoreEventType type, Key<B,K> key, B oldValue, B newValue){
		this.type = type;
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	public BeanstoreEventType getType() {
		return type;
	}
	public B getOldValue() {
		return oldValue;
	}
	public B getNewValue() {
		return newValue;
	}
	public Key<B, K> getKey() {
		return key;
	}
}
