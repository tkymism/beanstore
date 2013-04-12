package com.tkym.labs.beancache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.BeanMeta;

public class BeancacheRepository {
	private Map<BeanMeta<?, ?>, Beancache<?, ?>> beancacheMap = new ConcurrentHashMap<BeanMeta<?,?>, Beancache<?,?>>();
	public <BT,KT extends Comparable<KT>> Beancache<BT, KT> get(BeanMeta<BT, KT> meta){
		@SuppressWarnings("unchecked")
		Beancache<BT, KT> cache = (Beancache<BT, KT>)beancacheMap.get(meta);
		if (cache == null){
			cache = new Beancache<BT, KT>(meta);
			beancacheMap.put(meta, cache);
		}
		return cache;
	} 
}
