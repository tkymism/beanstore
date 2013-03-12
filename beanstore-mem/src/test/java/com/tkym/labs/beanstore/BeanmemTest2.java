package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.SuffixBeanMetaRegistory;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.Generation;
import com.tkym.labs.beans.GenerationMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeanmemTest2 {
	private static Map<Long, Person> PERSONS = new HashMap<Long, Person>();
	private static BeanmemRepository REPO = new BeanmemRepository(); 
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final GenerationMeta GENERATION = GenerationMeta.get();
	static {
		for (long i=0; i<1000; i++)
			PERSONS.put(i, DataProvider.create(i));
		
		for (long i=0L; i<100; i++){
			Person p = DataProvider.create(i);
			long id = p.getId();
			REPO.meta(PERSON).put(
					KeyBuilder.root().
					meta(PERSON).is(id).
					build(), p);
			for (int j=0; j<10; j++){
				Account a = DataProvider.create(id, j);
				String index = a.getEmail();
				REPO.meta(ACCOUNT).put(
						KeyBuilder.root().
						meta(PERSON).is(id).
						meta(ACCOUNT).is(index).
						build(), a);
				for (int k=0; k<(j%2+1); k++){
					Bill bill = DataProvider.create(i, j, k);
					int billNo = bill.getNo();
					REPO.meta(BILL).put(
							KeyBuilder.root().
							meta(PERSON).is(id).
							meta(ACCOUNT).is(index).
							meta(BILL).is(billNo).
							build(), bill);
				}
			}
		}
		SuffixBeanMetaRegistory.get().meta(GENERATION).register("aaa","bbb");
		for (int i=0; i<100; i++)
			REPO.meta(GENERATION.s("aaa")).
				put(KeyBuilder.root().
					meta(GENERATION.s("aaa")).is(i).
					build(), DataProvider.gene(i));
		for (int i=100; i<200; i++)
			REPO.meta(GENERATION.s("bbb")).
				put(KeyBuilder.root().
					meta(GENERATION.s("bbb")).is(i).
					build(), DataProvider.gene(i));
	}
	@Test
	public void testBeanmemCase001(){
		for (long i=0; i<1000; i++)
			new Beanmem<Person, Long>(
					KeyBuilder.root().
					meta(PERSON).is(i).
					build(),
					PERSONS.get(i));
	}
	@Test
	public void testBeanmemMapCase001(){
		BeanmemMap<Person, Long> memmap = new BeanmemMap<Person, Long>(PersonMeta.get());
		for (long i=0; i<1000; i++)
			memmap.put(KeyBuilder.root().
					meta(PERSON).is(i).
					build(), PERSONS.get(i));
		Person p300 = memmap.get(PersonMeta.get().key(null, 300L));
		assertThat(p300.getId(), is(300L));
		assertThat(p300.getName(), is("hoge+300"));
	}
	@Test
	public void testBeanmemRepositoryCase001(){
		BeanmemMap<Person, Long> persons = 
				REPO.meta(PERSON);  
		Person p99 = persons.get(
				KeyBuilder.root().
				meta(PERSON).is(99L).
				build());
		assertThat(p99.getId(), is(99L));
		assertThat(p99.getName(), is("hoge+99"));
		assertThat(persons.size(), is(100));
		BeanmemMap<Account, String> accounts = 
				REPO.meta(ACCOUNT);  
		Account a999 = accounts.get(
				KeyBuilder.root().
				meta(PERSON).is(99L).
				meta(ACCOUNT).is("hoge"+99L+"@email"+9+".com").
				build());
		assertThat(a999.getEmail(), is("hoge"+99L+"@email"+9+".com"));
		assertThat(accounts.size(), is(1000));
	}
	@Test
	public void testBeanmemRepositoryCase002(){
		assertThat(REPO.meta(GENERATION.s("aaa")).size(), is(100));
		assertThat(REPO.meta(GENERATION.s("bbb")).size(), is(100));
		Generation g99 =
			REPO.meta(
					GENERATION.s("aaa")).
					get(KeyBuilder.root().
						meta(GENERATION.s("aaa")).is(99).
						build());
		assertThat(g99.getId(), is(99));
		Generation g199 =
				REPO.meta(
						GENERATION.s("bbb")).
						get(KeyBuilder.root().
							meta(GENERATION.s("bbb")).is(199).
							build());
		assertThat(g199.getId(), is(199));
	}
	
	static class BeanmemRepository{
		private final ConcurrentHashMap<BeanMeta<?, ?>, BeanmemMap<?, ?>> memmaps;
		BeanmemRepository(){
			memmaps = new ConcurrentHashMap<BeanMeta<?, ?>, BeanmemMap<?, ?>>();
		}
		<BT, KT> BeanmemMap<BT, KT> meta(BeanMeta<BT, KT> meta){
			BeanmemMap<BT, KT> memmap = getFromMap(meta);
			if (memmap == null){
				memmap = new BeanmemMap<BT, KT>(meta);
				memmaps.put(meta, memmap);
			}
			return memmap;
		}
		@SuppressWarnings("unchecked")
		private <BT, KT> BeanmemMap<BT, KT> getFromMap(BeanMeta<BT, KT> meta){
			return (BeanmemMap<BT, KT>)memmaps.get(meta);
		}
	}
	static class BeanmemMap<BT, KT>{
		private final BeanMeta<BT, KT> beanMeta;
		private final ConcurrentHashMap<Key<BT, KT>, Beanmem<BT,KT>> hashMap;
		BeanmemMap(BeanMeta<BT, KT> beanMeta){
			this.beanMeta = beanMeta;
			hashMap = new ConcurrentHashMap<Key<BT,KT>, Beanmem<BT,KT>>();
		}
		BT get(Key<BT, KT> key){
			Beanmem<BT,KT> element = hashMap.get(key);
			if (element != null) return element.get();
			return null;
		}
		BT put(Key<BT, KT> key, BT bean){
			Beanmem<BT,KT> element = hashMap.get(key);
			if (element == null){
				hashMap.put(key, new Beanmem<BT,KT>(key, bean));
				return null;
			} else {
				return element.replace(bean);
			}
		}
		int size(){
			return hashMap.size();
		}
	}
	/**
	 * 
	 * @author takayama
	 * @param <BT> 
	 * @param <KT>
	 */
	static class Beanmem<BT,KT>{
		private final Key<BT, KT> key;
		private BT bean = null;
		Beanmem(Key<BT, KT> key){
			this(key, null);
		}
		/**
		 * Initializes a newly created Beanmem.
		 * @param key: given Key value.
		 * @param bean: given Bean value.
		 */
		Beanmem(Key<BT, KT> key, BT bean){
			this.bean = bean;
			this.key = key;
		}
		/**
		 * Gets the key of this instance.
		 * @return The key
		 */
		Key<BT, KT> key() {
			return key;
		}
		/**
		 * Gets the bean of this instance.
		 * @return bean
		 */
		BT get() {
			return bean;
		}
		/**
		 * Replace the bean of this instance with the given new bean, and return old bean.
		 * @param bean: new bean
		 * @return The old bean of this Beanmem.
		 */
		BT replace(BT bean) {
			BT old = this.bean;
			this.bean = bean;
			return old;
		}
		/**
		 * @return true if bean is null, otherwise false.
		 */
		boolean isNull(){
			return bean == null;
		}
	}
}
