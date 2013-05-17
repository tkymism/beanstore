package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilterComposite.BeanFilterCompositeType;

public class BeanQueryUtils {
	public static BeanFilterComposite and(BeanFilterCriteria... criteria){
		return new BeanFilterComposite(BeanFilterCompositeType.AND, criteria);
	}
	public static BeanFilterComposite or(BeanFilterCriteria... criteria){
		return new BeanFilterComposite(BeanFilterCompositeType.OR, criteria);
	}
	public static <BT,PT> BeanCriteriaBuilder<BT,PT> p(PropertyMeta<BT, PT> meta){
		return new BeanCriteriaBuilder<BT, PT>(meta);
	}
}