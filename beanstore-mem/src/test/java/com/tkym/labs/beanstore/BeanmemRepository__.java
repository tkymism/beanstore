package com.tkym.labs.beanstore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.PersonMeta;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanFilter;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanstore.api.BeanSort;
import com.tkym.labs.beanstore.api.BeanSortCriteria;

public class BeanmemRepository__ {
	private static PersonMeta PERSON = PersonMeta.get();
	private static AccountMeta ACCOUNT = AccountMeta.get();
	private static BillMeta BILL = BillMeta.get();
	private static MemEntityMapProvider REPO = new MemEntityMapProvider();
	private static final DecimalFormat FORMAT00 = new DecimalFormat("00");
	private static final String email(int index){
		return FORMAT00.format(index) + "@mail.com";
	}
	
	static Bill createBill(long id, int idx, int no){
		Bill b = DataProvider.create(id, idx, no);
		b.setAmount(b.getAmount()+10000*no);
		return b;
	}
	
	public static void setupClass() {
		for (long id = 0; id < 1000; id++)
			for (int index = 0; index < 10; index++)
				for (int no = 0; no < 10; no++)
					REPO.get(BILL).put(
							new MemEntity<Bill, Integer>(
								KeyBuilder.root().
									meta(PERSON).is(id).
									meta(ACCOUNT).is(email(index)).
									meta(BILL).is(no).
									build(),
								createBill(id, index, no)));
	}
	
	public void testTraceParant(){
		List<BeanMeta<?,?>> trace = MemEntityMap.traceParant(BILL, PERSON);
		trace.get(0).equals(AccountMeta.get());
		trace.get(0).equals(BillMeta.get());
	}
	public void testBeanMemRepositoryCase001(){
		@SuppressWarnings("unused")
		int count = 0;
		for (MemEntity<Bill,Integer> entity : REPO.get(BILL).memmap.values())
			if (entity.getBean().getAmount() > 30000.0f && entity.getBean().getAmount() < 50000.0f)
				count++;
		REPO.get(BILL).
			sortAs(BILL.amount).
			greaterThan(30000.0f).
			lessThan(50000.0f).
			asSet();
	}
	class BeanQueryExecutorMem<BT, KT> extends AbstractBeanQueryExecutor<BT, KT>{
		@SuppressWarnings("unused")
		private final MemEntityMap<BT, KT> sourceMap;
		private final List<BeanFilter<BT, ?>> filters = new ArrayList<BeanFilter<BT,?>>();
		private final List<BeanSort<BT, ?>> sorts = new ArrayList<BeanSort<BT,?>>();
		BeanQueryExecutorMem(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent, MemEntityMap<BT, KT> sourceMap) {
			super(beanMeta, parent);
			this.sourceMap = sourceMap;
		}
		<PT> BeanQueryExecutorMem<BT, KT> filter(BeanFilter<BT,PT> filter){
			this.filters.add(filter);
			return this;
		}
		<PT> BeanQueryExecutorMem<BT, KT> sort(BeanSort<BT,PT> sort){
			this.sorts.add(sort);
			return this;
		}
		@SuppressWarnings("unchecked")
		@Override
		public QueryResultFetcher<BT> executeQueryAsBean(BeanQuerySource<BT, KT> objects) throws Exception {
			for (BeanFilterCriteria filter : objects.filterList())
				if (filter instanceof BeanFilter) 
					this.filter((BeanFilter<BT, ?>) filter);
				else throw new IllegalArgumentException("unsupport type.");
			for (BeanSortCriteria sort : objects.sortList())
				if (sort instanceof BeanSort) 
					this.sort((BeanSort<BT, ?>) sort);
				else throw new IllegalArgumentException("unsupport type.");
			return null;
		}
		@Override
		public QueryResultFetcher<Key<BT, KT>> executeQueryAsKey(BeanQuerySource<BT, KT> objects) throws Exception {
			return null;
		}
	}
	static class MemEntityMapProvider{
		private Map<BeanMeta<?, ?>, MemEntityMap<?,?>> memmap = 
				new ConcurrentHashMap<BeanMeta<?,?>, MemEntityMap<?,?>>();
		<BT,KT> MemEntityMap<BT,KT> get(BeanMeta<BT, KT> beanMeta){
			@SuppressWarnings("unchecked")
			MemEntityMap<BT,KT> map = (MemEntityMap<BT,KT>) memmap.get(beanMeta);
			if (map == null) {
				map = new MemEntityMap<BT,KT>(beanMeta);
				memmap.put(beanMeta, map);
			}
			return map;
		}
	}
	static class MemEntitySorter<BT,KT,PT extends Comparable<PT>>{
		private final SortedSet<MemEntity<BT,KT>> sortedSet;
		private final PropertyMeta<BT, PT> propertyMeta;
		private final BeanMeta<BT, KT> beanMeta;
		private Key<BT,KT> minkey = null;
		private Key<BT,KT> maxkey = null;
		MemEntitySorter(BeanMeta<BT, KT> beanMeta, final PropertyMeta<BT, PT> propertyMeta){
			this(beanMeta, propertyMeta, new TreeSet<MemEntity<BT,KT>>());
		}
		MemEntitySorter(BeanMeta<BT, KT> beanMeta, final PropertyMeta<BT, PT> propertyMeta, SortedSet<MemEntity<BT,KT>> sortedSet){
			this.propertyMeta = propertyMeta;
			this.beanMeta = beanMeta;
			this.sortedSet = sortedSet;
		}
		Set<MemEntity<BT,KT>> asSet(){
			return sortedSet;
		}
		MemEntitySorter<BT,KT,PT> add(MemEntity<BT,KT> mem){
			this.sortedSet.add(mem);
			return this;
		}
		MemEntitySorter<BT,KT,PT> addAll(Collection<MemEntity<BT,KT>> mems){
			this.sortedSet.addAll(mems);
			return this;
		}
		MemEntitySorter<BT,KT,PT> remove(MemEntity<BT,KT> mem){
			this.sortedSet.remove(mem);
			return this;
		}
		MemEntitySorter<BT,KT,PT> clear(){
			this.sortedSet.clear();
			return this;
		}
		MemEntitySorter<BT,KT,PT> greaterThan(PT value){
			return cloneSorter(sortedSet.headSet(buildMemEntityFor(value, minkey())));
		}
		MemEntitySorter<BT,KT,PT> greaterEquals(PT value){
			return cloneSorter(sortedSet.headSet(buildMemEntityFor(value, maxkey())));
		}
		MemEntitySorter<BT,KT,PT> lessThan(PT value){
			return cloneSorter(sortedSet.tailSet(buildMemEntityFor(value, maxkey())));
		}
		MemEntitySorter<BT,KT,PT> lessEquals(PT value){
			return cloneSorter(sortedSet.tailSet(buildMemEntityFor(value, minkey())));
		}
		MemEntitySorter<BT,KT,PT> equalsTo(PT value){
			return greaterEquals(value).lessEquals(value);
		}
		private MemEntitySorter<BT,KT,PT> cloneSorter(SortedSet<MemEntity<BT,KT>> sortedSet){
			return new MemEntitySorter<BT,KT,PT>(beanMeta, propertyMeta, sortedSet);
		}
		private MemEntity<BT, KT> buildMemEntityFor(PT value, Key<BT,KT> key){
			BT bean = beanMeta.newInstance();
			propertyMeta.access(bean).set(value);
			return new MemEntity<BT, KT>(key, bean);
		}
		private Key<BT,KT> minkey(){
			if (this.minkey == null)
				this.minkey = buildLimitKey(beanMeta, false);
			return this.maxkey;
		}
		private Key<BT,KT> maxkey(){
			if (this.maxkey == null)
				this.maxkey = buildLimitKey(beanMeta, true);
			return this.maxkey;
		}
		@SuppressWarnings({ "rawtypes", "unchecked"})
		static <BT,KT> Key<BT,KT> buildLimitKey(BeanMeta<BT,KT> beanMeta, boolean isMax){
			List<BeanMeta<?,?>> metas = MemEntityMap.traceParant(beanMeta, null);
			KeyBuilder builder = KeyBuilder.root();
			for (BeanMeta meta : metas)
				if (isMax) builder.meta(meta).max();
				else builder.meta(meta).min();
			return (Key<BT,KT>) builder.build();
		}
	}
	static class MemEntityMap<BT,KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private ConcurrentSkipListMap<Key<BT, KT>, MemEntity<BT,KT>> memmap = 
				new ConcurrentSkipListMap<Key<BT, KT>, MemEntity<BT,KT>>();
		MemEntityMap(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
		}
		MemEntityMap(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, MemEntity<BT,KT>> map) {
			this.beanMeta = beanMeta;
			this.memmap.putAll(map);
		}
		MemEntity<BT,KT> get(Key<BT, KT> key){
			return memmap.get(key);
		}
		MemEntity<BT,KT> put(MemEntity<BT,KT> mem){
			return memmap.put(mem.getKey(), mem);
		}
		MemEntity<BT,KT> remove(Key<BT, KT> key){
			return memmap.remove(key);
		}
		<PT extends Comparable<PT>> MemEntitySorter<BT, KT, PT> sortAs(PropertyMeta<BT, PT> property){
			Collection<MemEntity<BT,KT>> values = memmap.values();
			return new MemEntitySorter<BT, KT, PT>(beanMeta, property).addAll(values);
		}
		ConcurrentNavigableMap<Key<BT, KT>, MemEntity<BT,KT>> sameParentMap(Key<?,?> parent){
			Key<BT,KT> max = buildMaxKey(parent, this.beanMeta);
			Key<BT,KT> min = buildMinKey(parent, this.beanMeta);
			return memmap.headMap(max).tailMap(min);
		}
		MemEntityMap<BT,KT> parentOf(Key<?,?> parent){
			return new MemEntityMap<BT,KT>(beanMeta, sameParentMap(parent));
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
	static class MemEntity<BT,KT>{
		private final Key<BT, KT> key;
		private final BT bean;
		MemEntity(Key<BT, KT> key, BT bean) {
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
