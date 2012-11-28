package com.tkym.labs.beanstore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class BeanstoreMemTest {
	class BeanstoreServiceMem extends AbstractBeanstoreRootService{
		private final BeanMemRepository repository;
		BeanstoreServiceMem(BeanMemRepository repository) {
			this.repository = repository;
		}
		@Override
		public BeanstoreTransaction getTransaction() {
			return null;
		}
		@Override
		protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return new BeanstoreMem<BT,KT>(this, beanMeta, parent);
		}
		BeanMemRepository getBeanMemRepository(){
			return this.repository;
		}
		@Override
		protected <BT, KT> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return null;
		}
	}
	class BeanstoreMem<BT,KT> extends AbstractBeanstore<BT, KT>{
		private final BeanMemMap<BT,KT> memmap;
		BeanstoreMem(BeanstoreServiceMem rootService,
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
			memmap = rootService.
				getBeanMemRepository().
				get(beanMeta);
		}
		@Override
		protected BT getDelegate(KT value) throws Exception {
			BeanMem<BT,KT> mem = memmap.get(key(value));
			if (mem == null) 
				return null;
			else 
				return mem.getValue();
		}
		@Override
		protected void removeDelegate(KT key) throws Exception {
			memmap.remove(key(key));
		}
		@Override
		protected void putDelegate(KT key, BT bean) throws Exception {
			memmap.put(new BeanMem<BT,KT>(key(key), bean));
		}
	}
	class BeanMemRepository{
		private Map<BeanMeta<?, ?>, BeanMemMap<?,?>> cachemap = 
				new ConcurrentHashMap<BeanMeta<?,?>, BeanMemMap<?,?>>();
		<BT,KT> BeanMemMap<BT,KT> get(BeanMeta<BT, KT> beanMeta){
			@SuppressWarnings("unchecked")
			BeanMemMap<BT,KT> map = (BeanMemMap<BT,KT>) cachemap.get(beanMeta);
			if (map == null) {
				map = new BeanMemMap<BT,KT>(beanMeta);
				cachemap.put(beanMeta, map);
			}
			return map;
		}
	}
	class BeanMemMap<BT,KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private ConcurrentSkipListMap<Key<BT, KT>, BeanMem<BT,KT>> memmap = 
				new ConcurrentSkipListMap<Key<BT, KT>, BeanMem<BT,KT>>();
		BeanMemMap(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
		}
		BeanMem<BT,KT> get(Key<BT, KT> key){
			return memmap.get(key);
		}
		BeanMem<BT,KT> put(BeanMem<BT,KT> mem){
			return memmap.put(mem.getKey(), mem);
		}
		BeanMem<BT,KT> remove(Key<BT, KT> key){
			return memmap.remove(key);
		}
		public BeanMeta<BT, KT> getBeanMeta() {
			return beanMeta;
		}
	}
	class BeanMem<BT,KT>{
		private final Key<BT, KT> key;
		private final BT value;
		BeanMem(Key<BT, KT> key, BT value) {
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
