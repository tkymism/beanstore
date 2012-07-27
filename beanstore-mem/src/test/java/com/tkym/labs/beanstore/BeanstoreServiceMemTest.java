package com.tkym.labs.beanstore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;

public class BeanstoreServiceMemTest {
	
	class BeanstoreMem<B,K> extends AbstractBeanstore<B, K>{
		private Map<K, B> cachemap = new ConcurrentHashMap<K, B>();
		protected BeanstoreMem(
				AbstractBeanstoreService<?, ?> rootService,
				BeanMeta<B, K> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
		}

		@Override
		protected AbstractBeanstoreService<B, K> createChildService(
				AbstractBeanstoreService<?, ?> beanstoreServiceRoot,
				Key<B, K> key) {
			return new BeanstoreServiceMem<B, K>(beanstoreServiceRoot, key);
		}
		
		@Override
		protected B getDelegate(K key) throws Exception {
			return cachemap.get(key);
		}

		@Override
		protected void removeDelegate(K key) throws Exception {
			cachemap.remove(key);
		}

		@Override
		protected void putDelegate(K key, B bean) throws Exception {
			cachemap.put(key, bean);
		}
	}
	
	class BeanstoreServiceMem<B, K> extends AbstractBeanstoreService<B, K>{
		protected BeanstoreServiceMem(
				AbstractBeanstoreService<?, ?> rootService, Key<B, K> parent) {
			super(rootService, parent);
		}

		@Override
		public <BT, KT> BeanQuery<BT, KT> query(BeanMeta<BT, KT> meta) {
			return null;
		}

		@Override
		protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
				BeanMeta<BT, KT> beanMeta, Key<B, K> parent) {
			return new BeanstoreMem<BT, KT>(super.getRoot(), beanMeta, parent);
		}
	}
}