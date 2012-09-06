package com.tkym.labs.beanstore.api;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilterComposite.BeanFilterCompositeType;

public class BeanQueryUtils {
	public static <BT> BeanFilterComposite<BT> and(BeanFilterCriteria<BT>... criteria){
		return new BeanFilterComposite<BT>(BeanFilterCompositeType.AND, criteria);
	}
	public static <BT> BeanFilterComposite<BT> or(BeanFilterCriteria<BT>... criteria){
		return new BeanFilterComposite<BT>(BeanFilterCompositeType.OR, criteria);
	}
	public static <BT,PT> BeanFilterFactory<BT,PT> filter(PropertyMeta<BT, PT> meta){
		return new BeanFilterFactory<BT, PT>(meta);
	}
	public static <BT,PT> BeanSortFactory<BT,PT> sort(PropertyMeta<BT, PT> meta){
		return new BeanSortFactory<BT, PT>(meta);
	}
}