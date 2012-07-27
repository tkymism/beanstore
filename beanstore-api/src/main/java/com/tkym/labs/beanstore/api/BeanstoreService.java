package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.BeanMeta;

public interface BeanstoreService<PB,PK> {
	public <BT,KT> BeanQuery<BT,KT> query(BeanMeta<BT,KT> meta);
	public <BT,KT> Beanstore<BT,KT> store(BeanMeta<BT,KT> meta);
	public BeanstoreTransaction getTransaction();
}