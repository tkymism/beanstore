package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;


public interface Beanstore<BT,KT> {
	public BeanstoreService<BT,KT> is(KT key);
	public BT get(KT key) throws BeanstoreException;
	public void remove(KT key) throws BeanstoreException;
	public void put(BT bean) throws BeanstoreException;
	public Key<BT,KT> key(KT key);
	public BeanMeta<BT, KT> getBeanMeta();
}