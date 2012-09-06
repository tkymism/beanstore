package com.tkym.labs.beanstore.api;


public class BeanFilterComposite {
	public enum BeanFilterCompositeType{
		AND, OR
	}
	private final BeanFilterCompositeType type;
	private final BeanFilterCriteria[] childlen;
	BeanFilterComposite(BeanFilterCompositeType type, BeanFilterCriteria... criteria){
		this.type = type;
		this.childlen = criteria;
		if (criteria.length < 1)
			throw new IllegalArgumentException(
					"composite length is "+criteria.length);
	}
	public BeanFilterCompositeType getType() {
		return type;
	}
	public BeanFilterCriteria[] getChildlen() {
		return childlen;
	}
}