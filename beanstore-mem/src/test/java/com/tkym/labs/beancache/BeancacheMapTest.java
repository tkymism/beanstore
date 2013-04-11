package com.tkym.labs.beancache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.Key;

public class BeancacheMapTest {
	
	static class BeancacheMap<BT,KT>{
		private Map<Key<BT, KT>, BT> cacheMap;
		BeancacheMap(){
			this(new ConcurrentHashMap<Key<BT, KT>, BT>());
		}
		BeancacheMap(int initCapacity){
			this(new ConcurrentHashMap<Key<BT, KT>, BT>(initCapacity));
		}
		BeancacheMap(Map<Key<BT, KT>, BT> chacheMap) {
			this.cacheMap = chacheMap;
		}
		BT get(Key<BT, KT> key){
			return cacheMap.get(key);
		}
		BT put(Key<BT, KT> key, BT bean){
			return cacheMap.put(key, bean);
		}
		BT remove(Key<BT, KT> key){
			return cacheMap.remove(key);
		}
	}
}
