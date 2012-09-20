package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

class BeanstoreUtils {
	static <BT, KT> boolean isSupport(Key<?,?> parent, BeanMeta<BT, KT> meta){
		if(meta.parent() == null && parent == null) return true;
		if(meta.parent() != null && parent != null) 
			return meta.parent().getName().equals(parent.getBeanMeta().getName());
		return false;
	}
}
