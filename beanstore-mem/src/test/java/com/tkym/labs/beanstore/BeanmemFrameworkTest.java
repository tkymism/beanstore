package com.tkym.labs.beanstore;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.PropertyMeta;


public class BeanmemFrameworkTest {
	static class BeanChainedComparator<BT,PT extends Comparable<PT>> implements Comparator<BT>{
		private final BeanChainedComparator<BT,?> parentComparator;
		private final PropertyMeta<BT, PT> propertyMeta;
		BeanChainedComparator(PropertyMeta<BT, PT> propertyMeta){
			this(null, propertyMeta);
		}
		BeanChainedComparator(BeanChainedComparator<BT, ?> parentComparator, PropertyMeta<BT, PT> propertyMeta){
			this.parentComparator = parentComparator;
			this.propertyMeta = propertyMeta;
		}
		@Override
		public int compare(BT o1, BT o2) {
			int ret = 0;
			if (parentComparator != null)
				ret = parentComparator.compare(o1, o2);
			if (ret == 0)
				ret = valueOf(o1).compareTo(valueOf(o2));
			return ret;
		}
		PT valueOf(BT bean){
			return propertyMeta.access(bean).get();
		}
	}
	static class BeanmemMap<BT,KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private ConcurrentSkipListMap<Key<BT, KT>, Beanmem<BT,KT>> memmap; 
		BeanmemMap(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
			this.memmap = 
					new ConcurrentSkipListMap<Key<BT, KT>, Beanmem<BT,KT>>(); 
		}
		BeanmemMap(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, Beanmem<BT,KT>> map) {
			this(beanMeta);
			this.memmap.putAll(map);
		}
		Beanmem<BT,KT> get(Key<BT, KT> key){
			return memmap.get(key);
		}
		Beanmem<BT,KT> put(Beanmem<BT,KT> mem){
			return memmap.put(mem.getKey(), mem);
		}
		Beanmem<BT,KT> remove(Key<BT, KT> key){
			return memmap.remove(key);
		}
//		<PT extends Comparable<PT>> BeanmemSorter<BT, KT, PT> sorter(PropertyMeta<BT, PT> property){
//			return new BeanmemSorter<BT, KT, PT>(beanMeta, property).addAll(memmap.values());
//		}
		private ConcurrentNavigableMap<Key<BT, KT>, Beanmem<BT,KT>> sameParentMap(Key<?,?> parent){
			Key<BT,KT> max = buildMaxKey(parent, this.beanMeta);
			Key<BT,KT> min = buildMinKey(parent, this.beanMeta);
			return memmap.headMap(max).tailMap(min);
		}
		BeanmemMap<BT,KT> parentOf(Key<?,?> parent){
			return new BeanmemMap<BT,KT>(beanMeta, sameParentMap(parent));
		}
		static List<BeanMeta<?,?>> traceParant(BeanMeta<?,?> from, BeanMeta<?,?> to){
			LinkedList<BeanMeta<?,?>> array = new LinkedList<BeanMeta<?,?>>();
			BeanMeta<?,?> current = from;
			while(current != null){
				if (to != null && current.equals(to)) break;
				array.addFirst(current);
				current = current.parent();
			}
			return array;
		}
		@SuppressWarnings("unchecked")
		static <BT, KT> Key<BT, KT> buildMaxKey(Key<?,?> parent, BeanMeta<BT, KT> meta){
			List<BeanMeta<?,?>> metas = traceParant(meta, parent.getBeanMeta());
			KeyBuilder<?,?> builder = KeyBuilder.parent(parent);
			for (BeanMeta<?,?> m : metas)
				builder.meta(m).max();
			return (Key<BT, KT>) builder.build();
		}
		@SuppressWarnings("unchecked")
		static <BT, KT> Key<BT, KT> buildMinKey(Key<?,?> parent, BeanMeta<BT, KT> meta){
			List<BeanMeta<?,?>> metas = traceParant(meta, parent.getBeanMeta());
			KeyBuilder<?,?> builder = KeyBuilder.parent(parent);
			for (BeanMeta<?,?> m : metas)
				builder.meta(m).min();
			return (Key<BT, KT>) builder.build();
		}
	}
	static class Beanmem<BT,KT>{
		private final Key<BT, KT> key;
		private final BT bean;
		Beanmem(Key<BT, KT> key, BT bean) {
			this.key = key;
			this.bean = bean;
		}
		Key<BT, KT> getKey() {
			return key;
		}
		BT getBean() {
			return bean;
		}
	}
}
