package com.tkym.labs.beanstore.record;

import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.BeanSortBuilder;
import com.tkym.labs.record.QuerySortBuilder;

class BeanSortBuilderRecord<BT, KT> implements BeanSortBuilder<BT, KT> {
	private final BeanQueryRecord<BT, KT> queryImpl;
	private final QuerySortBuilder sortBuilder;

	BeanSortBuilderRecord(BeanQueryRecord<BT, KT> queryImpl, QuerySortBuilder sortBuilder) {
		this.queryImpl = queryImpl;
		this.sortBuilder = sortBuilder;
	}

	@Override
	public BeanQuery<BT, KT> asc() {
		sortBuilder.asc();
		return queryImpl;
	}

	@Override
	public BeanQuery<BT, KT> desc() {
		sortBuilder.desc();
		return queryImpl;
	}
}