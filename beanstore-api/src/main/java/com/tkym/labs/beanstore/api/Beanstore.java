package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;


public interface Beanstore<BT,KT> {
	public BeanstoreService is(KT key);
	public BT get(KT key) throws BeanstoreException;
	public void remove(KT key) throws BeanstoreException;
	public void put(BT bean) throws BeanstoreException;
	public Key<BT,KT> key(KT key);
	public void addBeanstoreChangeListener(BeanstoreEventListener<BT, KT> listener);
	public void removeBeanstoreChangeListener(BeanstoreEventListener<BT, KT> listener);
	public BeanMeta<BT, KT> getBeanMeta();
}