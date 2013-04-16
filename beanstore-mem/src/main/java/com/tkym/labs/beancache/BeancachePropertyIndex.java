package com.tkym.labs.beancache;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancachePropertyIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
	extends AbstractBeancacheIndex<BT,KT,PT>{
	private final PropertyMeta<BT, PT> propertyMeta;
	BeancachePropertyIndex(PropertyMeta<BT, PT> propertyMeta){
		this.propertyMeta = propertyMeta;
	}
	void put(Key<BT, KT> key, BT bean){
		index.put(propertyMeta.access(bean).get(), key);
	}
}