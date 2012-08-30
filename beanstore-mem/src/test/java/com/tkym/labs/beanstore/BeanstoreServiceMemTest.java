package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.beans.DataProvider;
import com.tkym.labs.beanmeta.beans.Person;
import com.tkym.labs.beanmeta.beans.PersonMeta;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class BeanstoreServiceMemTest {
	
	@Test
	public void testCreateInstanceCase001() throws BeanstoreException{
		BeanstoreServiceMem<Void, Void> service = new BeanstoreServiceMem<Void, Void>();
		assertThat((BeanstoreServiceMem<Void, Void>)service.getRoot(), is(service));
		Beanstore<Person, Long> store = service.createBeanstore(PersonMeta.get(), null);
		store.put(DataProvider.create(1L));
		store.put(DataProvider.create(2L));
		Person p3 = DataProvider.create(3L);
		store.put(p3);
		store.put(DataProvider.create(4L));
		Assert.assertThat(store.get(3L), CoreMatchers.is(p3));
	}
	
	@Test
	public void testCreateInstanceCase002(){
		BeanstoreServiceMem<Void, Void> service = new BeanstoreServiceMem<Void, Void>();
		assertThat((BeanstoreServiceMem<Void, Void>)service.getRoot(), is(service));
	}
	
	class BeanstoreMem<B,K> extends AbstractBeanstore<B, K>{
		private Map<K, B> cachemap = new ConcurrentHashMap<K, B>();
		protected BeanstoreMem(
				AbstractBeanstoreService<Void, Void> rootService,
				BeanMeta<B, K> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
		}

		@Override
		protected AbstractBeanstoreService<B, K> createChildService(
				AbstractBeanstoreService<Void, Void> beanstoreServiceRoot,
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
		protected BeanstoreServiceMem(){
			super(new BeanstoreTransactionMem());
		}
		
		protected BeanstoreServiceMem(
				AbstractBeanstoreService<Void, Void> rootService, Key<B, K> parent) {
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
	
	class BeanstoreTransactionMem implements BeanstoreTransaction{
		@Override public void commit() throws BeanstoreException { throwUOE();}
		@Override public void rollback() throws BeanstoreException { throwUOE();}
		@Override public void close() throws BeanstoreException { throwUOE();}
		void throwUOE(){ throw new UnsupportedOperationException(); }
	}
}