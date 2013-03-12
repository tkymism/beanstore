package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public interface BeanQuery<BT,KT> {
	public BeanQuery<BT,KT> filter(BeanFilterCriteria criteria);
	public <PT> BeanFilterBuilder<BT,KT,PT> filter(PropertyMeta<BT,PT> meta);
	public BeanQuery<BT,KT> sort(BeanSortCriteria criteria);
	public <PT> BeanSortBuilder<BT,KT> sort(PropertyMeta<BT,PT> meta);
	public BeanQueryResult<BT> bean();
	public BeanQueryResult<Key<BT,KT>> key();
}