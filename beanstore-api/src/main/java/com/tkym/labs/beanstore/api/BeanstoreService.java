package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public interface BeanstoreService {
	public <BT,KT> BeanQuery<BT,KT> query(BeanMeta<BT,KT> meta);
	public <BT,KT> Beanstore<BT,KT> store(BeanMeta<BT,KT> meta);
	public BeanstoreService is(Key<?,?> parentKey) throws BeanstoreException;
	public BeanstoreTransaction getTransaction();
}