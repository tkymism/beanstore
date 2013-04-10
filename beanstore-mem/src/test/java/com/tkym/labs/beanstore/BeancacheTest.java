package com.tkym.labs.beanstore;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils;
import com.tkym.labs.beanmeta.BeanMetaUtils.PropertyMetaComparator;
import com.tkym.labs.beanmeta.Key.MaxKeyValue;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.KeyComparator;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beans.BeanBuilder.HogeBuilder;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;

public class BeancacheTest {
	private static HogeMeta HOGE = HogeMeta.get();
	@Test
	public void testBeancacheIndexCase001(){
		BeancachePropertyIndex<Hoge,String,Integer> index 
			= new BeancachePropertyIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		BeancachePropertyIndex<Hoge,String,Integer> sub;
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), true);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), true);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), true);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), false);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), false);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.buildIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), false);
		for (Hoge h : sub.navigableMap.values())
			System.out.print("["+h.getKey()+":"+h.getIntValue()+"]");
		System.out.println();
	}
	static Key<Hoge, String> createHogeKeyForID(int i){
		return KeyBuilder.root().meta(HOGE).is(createHogeKeyStringForID(i)).build();
	}
	static String createHogeKeyStringForID(int i){
		return "hoge"+i;
	}
	static Hoge createHogeForID(int i){
		return HogeBuilder.create().key(createHogeKeyStringForID(i)).intValue(99-i).bean();
	}
	
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
			if (o1.key.equals(o2.key)) return 0; // first using by Key#equals();
			int p = propertyComparator.compare(o1.propertyValue, o2.propertyValue);
			if (p != 0) return p;
			int k = keyComparator.compare(o1.key, o2.key);
			return k;
		}
	}
	static class BeancachePropertyIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		private final NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap;
		private final PropertyMeta<BT, PT> propertyMeta;
		private final BeanMeta<BT, KT> beanMeta;
		BeancachePropertyIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			this(beanMeta, propertyMeta, 
					new ConcurrentSkipListMap<BeancacheIndexKey<BT,KT,PT>,BT>(
							comparator(beanMeta, propertyMeta)));
		}
		BeancachePropertyIndex(BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta, NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap){
			this.navigableMap = navigableMap;
			this.propertyMeta = propertyMeta;
			this.beanMeta = beanMeta;
		}
		NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap(){
			return this.navigableMap;
		}
		BeancachePropertyIndex<BT,KT,PT> createIndexFor(NavigableMap<BeancacheIndexKey<BT,KT,PT>,BT> navigableMap){
			return new BeancachePropertyIndex<BT,KT,PT>(beanMeta, propertyMeta, navigableMap);
		}
		BT put(Key<BT, KT> key, BT bean){
			return this.navigableMap.put(buildIndexKey(key,bean), bean);
		}
		BT remove(Key<BT, KT> key){
//			return this.navigableMap.remove(buildIndexKey(key, null));
			return null;
		}
		BeancacheIndexKey<BT,KT,PT> buildIndexKey(Key<BT, KT> key, PT value){
			return new BeancacheIndexKey<BT, KT, PT>(key, value);
		}
		BeancacheIndexKey<BT,KT,PT> buildIndexKey(Key<BT, KT> key, BT bean){
			return buildIndexKey(key, propertyMeta.access(bean).get());
		}
		static <BT,KT extends Comparable<KT>, PT extends Comparable<PT>> 
			BeancacheIndexKeyComparator<BT,KT,PT> comparator(
					BeanMeta<BT, KT> beanMeta, PropertyMeta<BT, PT> propertyMeta){
			return new BeancacheIndexKeyComparator<BT,KT,PT>(beanMeta, propertyMeta); 
		}
		BeancachePropertyIndex<BT,KT,PT> tailIndex(BeancacheIndexKey<BT,KT,PT> fromKey, boolean inclusive){
			return createIndexFor(this.navigableMap.tailMap(fromKey, inclusive));
		}
		BeancachePropertyIndex<BT,KT,PT> headIndex(BeancacheIndexKey<BT,KT,PT> toKey, boolean inclusive){
			return createIndexFor(this.navigableMap.headMap(toKey, inclusive));
		}
		BeancachePropertyIndex<BT,KT,PT> subIndex(
				BeancacheIndexKey<BT,KT,PT> fromKey, boolean fromInclusive,
				BeancacheIndexKey<BT,KT,PT> toKey, boolean toInclusive
				){
			return createIndexFor(this.navigableMap.subMap(fromKey, fromInclusive, toKey, toInclusive));
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
