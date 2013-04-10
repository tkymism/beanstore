package com.tkym.labs.beancache;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyComparator;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils.PropertyMetaComparator;

class BeancacheTreeIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
	final NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap;
	private final PropertyMeta<BT, PT> propertyMeta;
	private final BeanMeta<BT, KT> beanMeta;
	BeancacheTreeIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
		this(beanMeta, propertyMeta, 
				new ConcurrentSkipListMap<BeancacheIndexKey<BT,KT,PT>,BT>(
						comparator(beanMeta, propertyMeta)));
	}
	BeancacheTreeIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta, NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap){
		this.navigableMap = navigableMap;
		this.propertyMeta = propertyMeta;
		this.beanMeta = beanMeta;
	}
	NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap(){
		return this.navigableMap;
	}
	BeancacheTreeIndex<BT,KT,PT> createIndexFor(NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap){
		return new BeancacheTreeIndex<BT,KT,PT>(beanMeta, propertyMeta, navigableMap);
	}
	BT put(Key<BT, KT> key, BT bean){
		return this.navigableMap.put(createIndexKey(key,bean), bean);
	}
	BT remove(Key<BT, KT> key){
		return this.navigableMap.remove(createIndexKeyIgnoreProperty(key));
	}
	BT get(Key<BT, KT> key){
		return this.navigableMap.get(createIndexKeyIgnoreProperty(key));
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKeyIgnoreProperty(Key<BT, KT> key){
		return new BeancacheIndexKey<BT, KT, PT>(key);
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKey(Key<BT, KT> key, PT value){
		return new BeancacheIndexKey<BT, KT, PT>(key, value);
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKey(Key<BT, KT> key, BT bean){
		return createIndexKey(key, propertyMeta.access(bean).get());
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKeyAsKeyMax(PT value){
		return new BeancacheIndexKey<BT, KT, PT>(BeancacheIndexKey.MAX, value);
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKeyAsKeyMin(PT value){
		return new BeancacheIndexKey<BT, KT, PT>(BeancacheIndexKey.MIN, value);
	}
	static <BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
		BeancacheIndexKeyComparator<BT,KT,PT> comparator(
				BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
		return new BeancacheIndexKeyComparator<BT,KT,PT>(beanMeta, propertyMeta); 
	}
	// create tailIndex, headIndex, subIndex by BeancacheIndexKey
	BeancacheTreeIndex<BT,KT,PT> tailIndex(BeancacheIndexKey<BT,KT,PT> fromKey, boolean inclusive){
		return createIndexFor(this.navigableMap.tailMap(fromKey, inclusive));
	}
	BeancacheTreeIndex<BT,KT,PT> headIndex(BeancacheIndexKey<BT,KT,PT> toKey, boolean inclusive){
		return createIndexFor(this.navigableMap.headMap(toKey, inclusive));
	}
	BeancacheTreeIndex<BT,KT,PT> subIndex(
			BeancacheIndexKey<BT,KT,PT> fromKey, boolean fromInclusive,
			BeancacheIndexKey<BT,KT,PT> toKey, boolean toInclusive
			){
		return createIndexFor(this.navigableMap.subMap(fromKey, fromInclusive, toKey, toInclusive));
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKeyAsTail(PT value, boolean inclusive){
		if (inclusive)
			return createIndexKeyAsKeyMin(value);
		else
			return createIndexKeyAsKeyMax(value);
	}
	BeancacheIndexKey<BT,KT,PT> createIndexKeyAsHead(PT value, boolean inclusive){
		if (inclusive)
			return createIndexKeyAsKeyMax(value);
		else
			return createIndexKeyAsKeyMin(value);
	}
	// create tailIndex, headIndex, subIndex by Property Value
	BeancacheTreeIndex<BT,KT,PT> tailIndex(PT from, boolean inclusive){
		return this.tailIndex(createIndexKeyAsTail(from, inclusive), inclusive);
	}
	BeancacheTreeIndex<BT,KT,PT> headIndex(PT to, boolean inclusive){
		return this.headIndex(createIndexKeyAsHead(to, inclusive), inclusive);
	}
	BeancacheTreeIndex<BT,KT,PT> subIndex(
				PT from, boolean fromInclusive,
				PT to, boolean toInclusive
			){
		return this.subIndex(
				createIndexKeyAsTail(from, fromInclusive), fromInclusive, 
				createIndexKeyAsHead(to, toInclusive), toInclusive);
	}
	static class BeancacheIndexKey<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		static final int AS_KEY = 0;
		static final int MAX = 1;
		static final int MIN = -1;
		private final PT propertyValue;
		private final Key<BT,KT> key;
		private final Integer parent;
		private final boolean ignoreProperty;
		BeancacheIndexKey(int parent, PT propertyValue){
			this.key = null;
			this.propertyValue = propertyValue;
			this.ignoreProperty = false;
			this.parent = parent;
		}
		BeancacheIndexKey(Key<BT,KT> key){
			this.propertyValue = null;
			this.ignoreProperty = true;
			this.key = key;
			this.parent = AS_KEY;
		}
		BeancacheIndexKey(Key<BT,KT> key, PT propertyValue){
			this.key = key;
			this.propertyValue = propertyValue;
			this.ignoreProperty = false;
			this.parent = AS_KEY;
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