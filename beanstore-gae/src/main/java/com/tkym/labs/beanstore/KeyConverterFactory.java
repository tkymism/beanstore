package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;

import com.google.appengine.api.datastore.Key;

class KeyConverterFactory{
	
	static <B,K> KeyConverter<B,K> create(com.tkym.labs.beanmeta.Key<?, ?> parent, BeanMeta<B, K> beanMeta){
		Key convertedParent = null;
		if (parent != null)
			convertedParent = convertKey(parent); 
		return create(convertedParent, beanMeta);
	}
	
	static <B,K> KeyConverter<B,K> create(Key parent, BeanMeta<B, K> beanMeta){
		if (parent == null)
			return create(beanMeta);
		else
			return new KeyConverter<B, K>(parent, beanMeta);
	}

	static <B,K> KeyConverter<B,K> create(BeanMeta<B, K> beanMeta){
		return new KeyConverter<B, K>(beanMeta);
	}
	
	static <BT, KT> Key convertKey(com.tkym.labs.beanmeta.Key<BT, KT> key){
		BeanMeta<BT, KT> beanMeta = key.getBeanMeta();
		Key parent = null;
		if (key.getParent() != null)
			parent = convertKey(key.getParent());
		return create(parent, beanMeta).convert(key.value());
	}
}