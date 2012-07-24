package com.tkym.labs.beanstore.record;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.BeanMeta;

class BeanMetaResolverProvider {
	private static final BeanMetaResolverProvider singleton = new BeanMetaResolverProvider();
	private Map<String, BeanMetaResolver<?, ?>> map = new ConcurrentHashMap<String, BeanMetaResolver<?,?>>();
	private BeanMetaResolverProvider(){}
	static BeanMetaResolverProvider getInstance(){
		return singleton;
	}
	<B, K> BeanMetaResolver<B, K> get(BeanMeta<B, K> meta){
		@SuppressWarnings("unchecked")
		BeanMetaResolver<B, K> resolver = (BeanMetaResolver<B, K>) map.get(meta.getName());
		if (resolver == null){
			resolver = new BeanMetaResolver<B, K>(meta);
			map.put(meta.getName(), resolver);
		}
		return resolver;
	}
}
