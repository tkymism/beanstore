package com.tkym.labs.beancache;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils;
import com.tkym.labs.beanmeta.BeanMetaUtils.PropertyMetaComparator;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyComparator;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancachePropertyTreeIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
	private final NavigableMap<BeancacheTreeIndexKey<BT,KT,PT>,Key<BT, KT>> navigableMap;
	private final PropertyMeta<BT, PT> propertyMeta;
	private final BeanMeta<BT, KT> beanMeta;
	public BeancachePropertyTreeIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
		this(beanMeta, propertyMeta, 
				new ConcurrentSkipListMap<BeancacheTreeIndexKey<BT,KT,PT>,Key<BT, KT>>(
						comparator(beanMeta, propertyMeta)));
	}
	BeancachePropertyTreeIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta, NavigableMap<BeancacheTreeIndexKey<BT,KT,PT>,Key<BT, KT>> navigableMap){
		this.navigableMap = navigableMap;
		this.propertyMeta = propertyMeta;
		this.beanMeta = beanMeta;
	}
	NavigableMap<BeancacheTreeIndexKey<BT,KT,PT>,Key<BT, KT>> navigableMap(){
		return this.navigableMap;
	}
	BeancachePropertyTreeIndex<BT,KT,PT> createIndexFor(NavigableMap<BeancacheTreeIndexKey<BT,KT,PT>,Key<BT, KT>> navigableMap){
		return new BeancachePropertyTreeIndex<BT,KT,PT>(beanMeta, propertyMeta, navigableMap);
	}
	Key<BT, KT> put(Key<BT, KT> key, PT value){
		return this.navigableMap.put(createIndexKey(key,value), key);
	}
	Key<BT, KT> put(Key<BT, KT> key, BT bean){
		return this.navigableMap.put(createIndexKey(key,bean), key);
	}
	Key<BT, KT> remove(Key<BT, KT> key){
		return this.navigableMap.remove(createIndexKeyIgnoreProperty(key));
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKeyIgnoreProperty(Key<BT, KT> key){
		return new BeancacheTreeIndexKey<BT, KT, PT>(key);
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKey(Key<BT, KT> key, PT value){
		return new BeancacheTreeIndexKey<BT, KT, PT>(key, value);
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKey(Key<BT, KT> key, BT bean){
		return createIndexKey(key, propertyMeta.access(bean).get());
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKeyAsKeyMax(PT value){
		return new BeancachePropertyTreeIndex.BeancacheTreeIndexKey<BT, KT, PT>(BeancacheTreeIndexKey.MAX, value);
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKeyAsKeyMin(PT value){
		return new BeancacheTreeIndexKey<BT, KT, PT>(BeancacheTreeIndexKey.MIN, value);
	}
	static <BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
		BeancacheTreeIndexKeyComparator<BT,KT,PT> comparator(
				BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
		return new BeancacheTreeIndexKeyComparator<BT,KT,PT>(beanMeta, propertyMeta); 
	}
	// create tailIndex, headIndex, subIndex by BeancacheIndexKey
	BeancachePropertyTreeIndex<BT,KT,PT> tailIndex(BeancachePropertyTreeIndex.BeancacheTreeIndexKey<BT,KT,PT> fromKey, boolean inclusive){
		return createIndexFor(this.navigableMap.tailMap(fromKey, inclusive));
	}
	BeancachePropertyTreeIndex<BT,KT,PT> headIndex(BeancachePropertyTreeIndex.BeancacheTreeIndexKey<BT,KT,PT> toKey, boolean inclusive){
		return createIndexFor(this.navigableMap.headMap(toKey, inclusive));
	}
	BeancachePropertyTreeIndex<BT,KT,PT> subIndex(
			BeancacheTreeIndexKey<BT,KT,PT> fromKey, boolean fromInclusive,
			BeancacheTreeIndexKey<BT,KT,PT> toKey, boolean toInclusive
			){
		return createIndexFor(this.navigableMap.subMap(fromKey, fromInclusive, toKey, toInclusive));
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKeyAsTail(PT value, boolean inclusive){
		if (inclusive)
			return createIndexKeyAsKeyMin(value);
		else
			return createIndexKeyAsKeyMax(value);
	}
	BeancacheTreeIndexKey<BT,KT,PT> createIndexKeyAsHead(PT value, boolean inclusive){
		if (inclusive)
			return createIndexKeyAsKeyMax(value);
		else
			return createIndexKeyAsKeyMin(value);
	}
	// create tailIndex, headIndex, subIndex by Property Value
	public BeancachePropertyTreeIndex<BT,KT,PT> tailIndex(PT from, boolean inclusive){
		return this.tailIndex(createIndexKeyAsTail(from, inclusive), inclusive);
	}
	public Set<Key<BT, KT>> keySet(){
		return new HashSet<Key<BT,KT>>(this.navigableMap.values());
	}
	public BeancachePropertyTreeIndex<BT,KT,PT> headIndex(PT to, boolean inclusive){
		return this.headIndex(createIndexKeyAsHead(to, inclusive), inclusive);
	}
	public BeancachePropertyTreeIndex<BT,KT,PT> subIndex(
				PT from, boolean fromInclusive,
				PT to, boolean toInclusive
			){
		return this.subIndex(
				createIndexKeyAsTail(from, fromInclusive), fromInclusive, 
				createIndexKeyAsHead(to, toInclusive), toInclusive);
	}
	static class BeancacheTreeIndexKey<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		static final int AS_KEY = 0;
		static final int MAX = 1;
		static final int MIN = -1;
		private final PT propertyValue;
		private final Key<BT,KT> key;
		private final Integer parent;
		private final boolean ignoreProperty;
		BeancacheTreeIndexKey(int parent, PT propertyValue){
			this.key = null;
			this.propertyValue = propertyValue;
			this.ignoreProperty = false;
			this.parent = parent;
		}
		BeancacheTreeIndexKey(Key<BT,KT> key){
			this.propertyValue = null;
			this.ignoreProperty = true;
			this.key = key;
			this.parent = AS_KEY;
		}
		BeancacheTreeIndexKey(Key<BT,KT> key, PT propertyValue){
			this.key = key;
			this.propertyValue = propertyValue;
			this.ignoreProperty = false;
			this.parent = AS_KEY;
		}
	}
	
	static class BeancacheTreeIndexKeyComparator<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>
			implements Comparator<BeancacheTreeIndexKey<BT,KT,PT>>{
		private PropertyMetaComparator<BT,PT> propertyComparator;
		private Comparator<Key<BT,KT>> keyComparator;
		BeancacheTreeIndexKeyComparator(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			propertyComparator = BeanMetaUtils.get().propertyComparator(propertyMeta).nullFirst();
			keyComparator = KeyComparator.comparator(beanMeta);
		}
		BeancachePropertyTreeIndex.BeancacheTreeIndexKeyComparator<BT,KT,PT> nullFirst(){
			propertyComparator.nullFirst();
			return this;
		}
		BeancachePropertyTreeIndex.BeancacheTreeIndexKeyComparator<BT,KT,PT> nullLast(){
			propertyComparator.nullLast();
			return this;
		}
		@Override
		public int compare(BeancacheTreeIndexKey<BT, KT, PT> o1, BeancacheTreeIndexKey<BT, KT, PT> o2) {
			if (!(o1.ignoreProperty || o2.ignoreProperty)){
				int p = propertyComparator.compare(o1.propertyValue, o2.propertyValue);
				if (p != 0) return p;
			}
			
			if (o1.parent != 0 || o2.parent != 0)
				return o1.parent.compareTo(o2.parent);
			
			return keyComparator.compare(o1.key, o2.key);
		}
	}
}