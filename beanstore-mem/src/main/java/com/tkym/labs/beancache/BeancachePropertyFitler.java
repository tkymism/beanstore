package com.tkym.labs.beancache;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancachePropertyFitler<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
	extends AbstractBeancacheFilter<BT,KT,PT>{
	private final PropertyMeta<BT, PT> propertyMeta;
	BeancachePropertyFitler(BeanMeta<BT,KT> beanMeta,  PropertyMeta<BT, PT> propertyMeta){
		super(beanMeta);
		this.propertyMeta = propertyMeta;
	}
	void put(Key<BT, KT> key, BT bean){
		index.put(propertyMeta.access(bean).get(), key);
	}
}