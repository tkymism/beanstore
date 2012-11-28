package com.tkym.labs.beanstore;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.PersonMeta;

public class BeanmemRepositoryTest {
	private static PersonMeta PERSON = PersonMeta.get();
	private static AccountMeta ACCOUNT = AccountMeta.get();
	private static BillMeta BILL = BillMeta.get();
	private static BeanMemRepository REPO = new BeanMemRepository();
	private static final DecimalFormat FORMAT00 = new DecimalFormat("00");
	private static final String email(int index){
		return FORMAT00.format(index) + "@mail.com";
	}
	
	@BeforeClass
	public static void setupClass() {
		for (long id = 0; id < 1000; id++)
			for (int index = 0; index < 10; index++)
				for (int no = 0; no < 10; no++)
					REPO.get(BILL).put(
							new Beanmem<Bill, Integer>(
								KeyBuilder.root()
								.meta(PERSON).is(id)
								.meta(ACCOUNT).is(email(index))
								.meta(BILL).is(no).
								build(),
								DataProvider.create(id, index, no)));
	}
	
	@Test
	public void testTraceParant(){
		List<BeanMeta<?,?>> trace = BeanmemMap.traceParant(BILL, PERSON);
		trace.get(0).equals(AccountMeta.get());
		trace.get(0).equals(BillMeta.get());
	}
	
	@Test
	public void testBeanMemRepositoryCase001(){

	}
	static class BeanMemRepository{
		private Map<BeanMeta<?, ?>, BeanmemMap<?,?>> cachemap = 
				new ConcurrentHashMap<BeanMeta<?,?>, BeanmemMap<?,?>>();
		<BT,KT> BeanmemMap<BT,KT> get(BeanMeta<BT, KT> beanMeta){
			@SuppressWarnings("unchecked")
			BeanmemMap<BT,KT> map = (BeanmemMap<BT,KT>) cachemap.get(beanMeta);
			if (map == null) {
				map = new BeanmemMap<BT,KT>(beanMeta);
				cachemap.put(beanMeta, map);
			}
			return map;
		}
	}
	static class BeanmemSorter<BT,KT,PT extends Comparable<PT>>{
		private final SortedSet<Beanmem<BT,KT>> sortedSet;
		private final PropertyMeta<BT, PT> propertyMeta;
		private final BeanMeta<BT, KT> beanMeta;
		private final Key<BT,KT> minkey;
		private final Key<BT,KT> maxkey;
		BeanmemSorter(BeanMeta<BT, KT> beanMeta, final PropertyMeta<BT, PT> propertyMeta){
			this(beanMeta, propertyMeta, new TreeSet<Beanmem<BT,KT>>());
		}
		BeanmemSorter(BeanMeta<BT, KT> beanMeta, final PropertyMeta<BT, PT> propertyMeta, SortedSet<Beanmem<BT,KT>> sortedSet){
			this.propertyMeta = propertyMeta;
			this.beanMeta = beanMeta;
			this.sortedSet = sortedSet;
			this.minkey = buildLimitKey(beanMeta, false);
			this.maxkey = buildLimitKey(beanMeta, true);
		}
		BeanmemSorter<BT,KT,PT> add(Beanmem<BT,KT> mem){
			this.sortedSet.add(mem);
			return this;
		}
		BeanmemSorter<BT,KT,PT> addAll(Collection<Beanmem<BT,KT>> mems){
			this.sortedSet.addAll(mems);
			return this;
		}
		BeanmemSorter<BT,KT,PT> remove(Beanmem<BT,KT> mem){
			this.sortedSet.remove(mem);
			return this;
		}
		BeanmemSorter<BT,KT,PT> clear(){
			this.sortedSet.clear();
			return this;
		}
		BeanmemSorter<BT,KT,PT> greaterThan(PT value){
			return cloneSorter(sortedSet.headSet(createParamBeanmem(value, minkey)));
		}
		BeanmemSorter<BT,KT,PT> greaterEquals(PT value){
			return cloneSorter(sortedSet.headSet(createParamBeanmem(value, maxkey)));
		}
		BeanmemSorter<BT,KT,PT> lessThan(PT value){
			return cloneSorter(sortedSet.tailSet(createParamBeanmem(value, maxkey)));
		}
		BeanmemSorter<BT,KT,PT> lessEquals(PT value){
			return cloneSorter(sortedSet.tailSet(createParamBeanmem(value, minkey)));
		}
		BeanmemSorter<BT,KT,PT> equalsTo(PT value){
			return greaterEquals(value).lessEquals(value);
		}
		private BeanmemSorter<BT,KT,PT> cloneSorter(SortedSet<Beanmem<BT,KT>> sortedSet){
			return new BeanmemSorter<BT,KT,PT>(beanMeta, propertyMeta, sortedSet);
		}
		private Beanmem<BT, KT> createParamBeanmem(PT value, Key<BT,KT> key){
			BT bean = beanMeta.newInstance();
			propertyMeta.access(bean).set(value);
			return new Beanmem<BT, KT>(key, bean);
		}
		@SuppressWarnings({ "rawtypes", "unchecked"})
		static <BT,KT> Key<BT,KT> buildLimitKey(BeanMeta<BT,KT> beanMeta, boolean isMax){
			List<BeanMeta<?,?>> metas = BeanmemMap.traceParant(beanMeta, null);
			KeyBuilder builder = KeyBuilder.root();
			for (BeanMeta meta : metas)
				if (isMax) builder.meta(meta).max();
				else builder.meta(meta).min();
			return (Key<BT,KT>) builder.build();
		}
	}
	static class BeanmemMap<BT,KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private ConcurrentSkipListMap<Key<BT, KT>, Beanmem<BT,KT>> memmap = 
				new ConcurrentSkipListMap<Key<BT, KT>, Beanmem<BT,KT>>();
		BeanmemMap(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
		}
		BeanmemMap(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, Beanmem<BT,KT>> map) {
			this.beanMeta = beanMeta;
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
		<PT extends Comparable<PT>> BeanmemSorter<BT, KT, PT> sorter(PropertyMeta<BT, PT> property){
			return new BeanmemSorter<BT, KT, PT>(beanMeta, property).addAll(memmap.values());
		}
		ConcurrentNavigableMap<Key<BT, KT>, Beanmem<BT,KT>> sameParentMap(Key<?,?> parent){
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
//	class BeanmemComparator<BT,KT,PT extends Comparable<PT>> implements Comparator<Beanmem<BT,KT>>{
//		private final PropertyMeta<BT, PT> propertyMeta;
//		BeanmemComparator(PropertyMeta<BT, PT> propertyMeta){
//			this.propertyMeta = propertyMeta;
//			if (!Comparable.class.isAssignableFrom(propertyMeta.getPropertyType()))
//				throw new IllegalArgumentException(
//								propertyMeta.getBeanType().getName()+"."+
//										propertyMeta.getPropertyName()+
//								" is not support Comparator. type is"+
//										propertyMeta.getPropertyType().getName());
//		}
//		@Override
//		public int compare(Beanmem<BT, KT> o1, Beanmem<BT, KT> o2) {
//			PT p1 = propertyMeta.access(o1.getValue()).get();
//			PT p2 = propertyMeta.access(o2.getValue()).get();
//			return p1.compareTo(p2);
//		}
//	}

}
