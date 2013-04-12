package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;


public interface Beanstore<BT,KT extends Comparable<KT>> {
	public BeanstoreService<BT,KT> is(KT key);
	public BT get(KT key);
	public void remove(KT key);
	public void put(BT bean);
	public Key<BT,KT> key(KT key);
	public BeanMeta<BT, KT> getBeanMeta();
}