package com.tkym.labs.beanstore;

import static com.tkym.labs.beanstore.api.BeanQueryUtils.property;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanFilter;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanstore.api.BeanSort;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class BeanstoreMemTest {
	private static MemMap<Person, Long> PERSON_MEM_MAP = new MemMap<Person, Long>(PersonMeta.get());
	@BeforeClass
	public static void setupClass(){
		for (long id=0L; id<1000; id++)
			PERSON_MEM_MAP.put(createPersonMemEntity(id));
	}
	private static DecimalFormat FORMATTER = new DecimalFormat("000000");
	private static MemEntity<Person, Long> createPersonMemEntity(long id){
		Person p = new Person();
		p.setId(id);
		p.setName("name " + FORMATTER.format(id));
		Key<Person, Long> key = 
			KeyBuilder.root().
			meta(PersonMeta.get()).is(id).
			build();
		return new MemEntity<Person, Long>(key, p);
	}
	@Test
	public void testBeanQueryExecutorMemCase001(){
		PersonMeta PERSON = PersonMeta.get();
		BeanQueryExecutorMem<Person, Long> executor = 
			new BeanQueryExecutorMem<Person, Long>(PERSON, null, PERSON_MEM_MAP);
		executor.
			filter(property(PERSON.name).greaterEqual("name 000001")).
			filter(property(PERSON.name).lessEqual("name 000005")).
			sort(property(PERSON.name).asc());
	}
	class BeanQueryExecutorMem<BT, KT> extends AbstractBeanQueryExecutor<BT, KT>{
		private final MemMap<BT, KT> map;
		private final List<BeanFilter<BT, ?>> filters = new ArrayList<BeanFilter<BT,?>>();
		private final List<BeanSort<BT, ?>> sorts = new ArrayList<BeanSort<BT,?>>();
		BeanQueryExecutorMem(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent, MemMap<BT, KT> map) {
			super(beanMeta, parent);
			this.map = map;
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
	class BeanstoreServiceMem extends AbstractBeanstoreRootService{
		private final MemMapStore memmaps;
		BeanstoreServiceMem(MemMapStore memmapstore) {
			this.memmaps = memmapstore;
		}
		@Override
		public BeanstoreTransaction transaction() {
			return null;
		}
		@Override
		protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return new BeanstoreMem<BT,KT>(this, beanMeta, parent);
		}
		@Override
		protected <BT, KT> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return null;
		}
		MemMapStore getMemMapStore(){
			return this.memmaps;
		}
	}
	class BeanstoreMem<BT,KT> extends AbstractBeanstore<BT, KT>{
		private final MemMap<BT,KT> memmap;
		BeanstoreMem(BeanstoreServiceMem rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
			memmap = rootService.getMemMapStore().get(beanMeta);
		}
		@Override
		protected BT getDelegate(KT value) throws Exception {
			MemEntity<BT,KT> mem = memmap.get(super.key(value));
			if (mem == null) return null;
			else return mem.getValue();
		}
		@Override
		protected void removeDelegate(KT key) throws Exception {
			memmap.remove(super.key(key));
		}
		@Override
		protected void putDelegate(KT key, BT bean) throws Exception {
			memmap.put(new MemEntity<BT,KT>(super.key(key), bean));
		}
	}
	class MemMapStore{
		private Map<BeanMeta<?, ?>, MemMap<?,?>> cachemap = 
				new ConcurrentHashMap<BeanMeta<?,?>, MemMap<?,?>>();
		<BT,KT> MemMap<BT,KT> get(BeanMeta<BT, KT> beanMeta){
			@SuppressWarnings("unchecked")
			MemMap<BT,KT> map = (MemMap<BT,KT>) cachemap.get(beanMeta);
			if (map == null) {
				map = new MemMap<BT,KT>(beanMeta);
				cachemap.put(beanMeta, map);
			}
			return map;
		}
	}
	static class MemMap<BT,KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private ConcurrentSkipListMap<Key<BT, KT>, MemEntity<BT,KT>> memmap = 
				new ConcurrentSkipListMap<Key<BT, KT>, MemEntity<BT,KT>>();
		MemMap(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
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
		public BeanMeta<BT, KT> getBeanMeta() {
			return beanMeta;
		}
	}
	static class MemEntity<BT,KT>{
		private final Key<BT, KT> key;
		private final BT value;
		MemEntity(Key<BT, KT> key, BT value) {
			this.key = key;
			this.value = value;
		}
		Key<BT, KT> getKey() {
			return key;
		}
		BT getValue() {
			return value;
		}
	}
//	public static class BeancacheRepository<PBT,PKT> {
//		private final Key<PBT, PKT> parent;
//		BeancacheRepository(Key<PBT, PKT> parent) {
//			this.parent = parent;
//		}
//		public Key<PBT, PKT> getParent() {
//			return parent;
//		}
//		public <BT,KT> Beancache<BT,KT> meta(BeanMeta<BT, KT> beanMeta){
//			return new Beancache<BT,KT>(parent, beanMeta);
//		}
//	}
	
	class BeancacheStoreRepository{
		private Map<BeanMeta<?, ?>, BeancacheStore<?, ?>> mapstore = 
			new ConcurrentHashMap<BeanMeta<?,?>, BeancacheStore<?,?>>();
		<BT, KT> BeancacheStore<BT, KT> getMap(BeanMeta<BT, KT> meta){
			@SuppressWarnings("unchecked")
			BeancacheStore<BT, KT> cachemap = 
					(BeancacheStore<BT, KT>) mapstore.get(meta);
			if (cachemap == null){
				cachemap = new BeancacheStore<BT, KT>(meta);
				mapstore.put(meta,cachemap);
			}
			return cachemap;
		}
	}
	class BeancacheStore<BT, KT> {
		private ConcurrentSkipListMap<Key<BT, KT>, BeancacheStats<BT, KT>> keymap = 
				new ConcurrentSkipListMap<Key<BT, KT>, BeancacheStats<BT, KT>>();
		private BeanMeta<BT, KT> meta;
		BeancacheStore(BeanMeta<BT, KT> meta){
			this.meta = meta;
		}
		BeanMeta<BT, KT> getBeanMeta(){
			return meta;
		}
		BT get(Key<BT, KT> key){
			return keymap.get(key).use();
		}
		BT put(Key<BT,KT> key, BT bean){
			BeancacheStats<BT, KT> cache = 
					keymap.put(key, BeancacheFactory.create(key, bean));
			BT ret = null;
			if (cache != null) ret = cache.getBean();
			return ret;
		}
		BT remove(Key<BT,KT> key){
			BeancacheStats<BT, KT> cache = 
				keymap.remove(key);
			BT ret = null;
			if (cache != null) ret = cache.getBean();
			return ret;
		}
		boolean isManaged(Key<BT,KT> key){
			return keymap.containsKey(key);
		}
	}
	static class BeancacheFactory{
		static <BT,KT> BeancacheStats<BT, KT> create(Key<BT, KT> key, BT bean){
			return new BeancacheStats<BT, KT>(key, bean);
		}
	}
}
