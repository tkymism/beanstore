package com.tkym.labs.beanstore;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils;
import com.tkym.labs.beanmeta.BeanMetaUtils.PropertyMetaComparator;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyComparator;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancacheTest {
	
	static class BeancacheIndexKey<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		private final PT propertyValue;
		private final Key<BT,KT> key;
		BeancacheIndexKey(Key<BT,KT> key, PT propertyValue){
			this.key = key;
			this.propertyValue = propertyValue;
		}
	}
	static class BeancacheIndexKeyComparator<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>
		implements Comparator<BeancacheIndexKey<BT,KT,PT>>{
		private PropertyMetaComparator<BT,PT> propertyComparator;
		private Comparator<Key<BT,KT>> keyComparator;
		BeancacheIndexKeyComparator(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			propertyComparator = BeanMetaUtils.get().propertyComparator(propertyMeta).nullFirst();
			keyComparator = KeyComparator.comparator(beanMeta);
		}
		BeancacheIndexKeyComparator<BT,KT,PT> nullFirst(){
			propertyComparator.nullFirst();
			return this;
		}
		BeancacheIndexKeyComparator<BT,KT,PT> nullLast(){
			propertyComparator.nullLast();
			return this;
		}
		@Override
		public int compare(BeancacheIndexKey<BT, KT, PT> o1, BeancacheIndexKey<BT, KT, PT> o2) {
			int p = propertyComparator.compare(o1.propertyValue, o2.propertyValue);
			if (p != 0) return p;
			int k = keyComparator.compare(o1.key, o2.key);
			return k;
		}
	}
	static class BeancachePropertyIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		private NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap;
		BeancachePropertyIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			this(new ConcurrentSkipListMap<BeancacheIndexKey<BT,KT,PT>,BT>(comparator(beanMeta, propertyMeta)));
		}
		BeancachePropertyIndex(NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap){
			this.navigableMap = navigableMap;
		}
		static <BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
			BeancacheIndexKeyComparator<BT,KT,PT> comparator(
					BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			return new BeancacheIndexKeyComparator<BT,KT,PT>(beanMeta, propertyMeta); 
		}
	}
	class BeancacheKeyIndex<BT,KT extends Comparable<KT>>{
		private NavigableMap<Key<BT,KT>, BT> navigableMap; 
		BeancacheKeyIndex(BeanMeta<BT, KT> beanMeta){
			this(new ConcurrentSkipListMap<Key<BT,KT>, BT>(
					KeyComparator.comparator(beanMeta)
					));
		}
		BeancacheKeyIndex(NavigableMap<Key<BT,KT>, BT> navigableMap){
			this.navigableMap = navigableMap;
		}
		BeancacheKeyIndex<BT,KT> tailIndex(Key<BT,KT> fromKey, boolean inclusive){
			return new BeancacheKeyIndex<BT,KT>(this.navigableMap.tailMap(fromKey, inclusive));
		}
		BeancacheKeyIndex<BT,KT> headIndex(Key<BT,KT> toKey, boolean inclusive){
			return new BeancacheKeyIndex<BT,KT>(this.navigableMap.headMap(toKey, inclusive));
		}
	}
	class BeancacheMap<BT,KT>{
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
