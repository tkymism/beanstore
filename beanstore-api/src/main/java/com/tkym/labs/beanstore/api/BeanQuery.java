package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public interface BeanQuery<BT,KT> {
	public <PT> BeanFilterBuilder<BT,KT,PT> filter(PropertyMeta<BT,PT> meta);
	public <PT> BeanSortBuilder<BT,KT> sort(PropertyMeta<BT,PT> meta);
	public BeanQueryResult<BT> bean() throws BeanstoreException;
	public BeanQueryResult<Key<BT,KT>> key() throws BeanstoreException;
}