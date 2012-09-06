package com.tkym.labs.beanstore.api;

import java.util.Arrays;
import java.util.List;

public class BeanFilterComposite<BT> {
	enum BeanFilterCompositeType{
		AND, OR
	}
	private final BeanFilterCompositeType type;
	private final List<BeanFilterCriteria<BT>> childlen;
	BeanFilterComposite(BeanFilterCompositeType type, List<BeanFilterCriteria<BT>> childlen){
		this.type = type;
		this.childlen = childlen;
	}
	BeanFilterComposite(BeanFilterCompositeType type, BeanFilterCriteria<BT>... criteria){
		this(type, Arrays.asList(criteria));
	}
	public BeanFilterCompositeType getType() {
		return type;
	}
	public List<BeanFilterCriteria<BT>> getChildlen() {
		return childlen;
	}
}
