package com.tkym.labs.beanstore;

import java.util.List;

import com.tkym.labs.beanstore.BeanSortItem.BeanSortOperator;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.beanmeta.PropertyMeta;

public class DefaultBeanSortBuilder<BT, KT, PT> implements BeanSortBuilder<BT, KT>{
	private final BeanQuery<BT, KT> query;
	private final PropertyMeta<BT, PT> propMeta;
	private final List<BeanSortItem<BT, ?>> sortList;
	DefaultBeanSortBuilder(BeanQuery<BT, KT> query, PropertyMeta<BT, PT> propMeta, List<BeanSortItem<BT, ?>> sortList){
		this.propMeta = propMeta;
		this.query = query;
		this.sortList = sortList;
	}
	
	BeanQuery<BT, KT> filter(BeanSortOperator operator){
		sortList.add(new BeanSortItem<BT, PT>(propMeta, operator));
		return query;
	}
	
	@Override
	public BeanQuery<BT, KT> asc() {
		return filter(BeanSortOperator.ASCENDING);
	}

	@Override
	public BeanQuery<BT, KT> desc() {
		return filter(BeanSortOperator.DESCENDING);
	}
}
