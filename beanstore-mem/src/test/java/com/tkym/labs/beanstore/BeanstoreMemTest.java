package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.PersonMeta;

public class BeanstoreMemTest {
	private static final ConcurrentSkipListMap<Key<Bill, Integer>, Bill> TEST_MAP = 
			new ConcurrentSkipListMap<Key<Bill, Integer>, Bill>();
	private static PersonMeta PERSON = PersonMeta.get();
	private static AccountMeta ACCOUNT = AccountMeta.get();
	private static BillMeta BILL = BillMeta.get();
	private static final DecimalFormat FORMAT00 = new DecimalFormat("00");
	private static final String email(int index){
		return FORMAT00.format(index) + "@mail.com";
	}
	
	@BeforeClass
	public static void setupClass() {
		for (long id = 0; id < 1000; id++)
			for (int index = 0; index < 10; index++)
				for (int no = 0; no < 10; no++)
					TEST_MAP.put(
							KeyBuilder.root()
							.meta(PERSON).is(id)
							.meta(ACCOUNT).is(email(index))
							.meta(BILL).is(no).
							build(),
							DataProvider.create(id, index, no));
	}
	
	@Test
	public void testCountData(){
		assertThat(TEST_MAP.size(), is(100000));
	}
	
	@Test
	public void testContainsSpeedTest(){
		boolean error = false;
		for (long id = 0; id < 1000; id++)
			for (int index = 0; index < 10; index++)
				for (int no = 0; no < 10; no++)
					if (!TEST_MAP.containsKey(
							KeyBuilder.root()
							.meta(PERSON).is(id)
							.meta(ACCOUNT).is(email(index))
							.meta(BILL).is(no).
							build()
							)) error = true;
		assertFalse(error);
	}
	
	@Test
	public void testGetSpeedTest(){
		boolean error = false;
		for (long id = 0; id < 1000; id++)
			for (int index = 0; index < 10; index++)
				for (int no = 0; no < 10; no++)
					if (TEST_MAP.get(
							KeyBuilder.root()
							.meta(PERSON).is(id)
							.meta(ACCOUNT).is(email(index))
							.meta(BILL).is(no).
							build()
							) == null) error = true;
		assertFalse(error);
	}
	
	@Test
	public void testRead_TEST_MAP(){
		ConcurrentNavigableMap<Key<Bill, Integer>, Bill> head = 
				TEST_MAP.headMap(
				KeyBuilder.root().
				meta(PERSON).is(500L).
				meta(ACCOUNT).is(null).
				meta(BILL).is(null).build());
		assertThat(head.size(), is(TEST_MAP.size()/2));
		for (Key<Bill, Integer> key : head.keySet())
			assertTrue(500 > (Long) key.getParent().getParent().value());
	}
	
	@Test
	public void testRead_TEST_MAP002(){
		ConcurrentNavigableMap<Key<Bill, Integer>, Bill> tail = 
				TEST_MAP.tailMap(
				KeyBuilder.root().
				meta(PERSON).is(500L).
				meta(ACCOUNT).is(null).
				meta(BILL).is(null).build());
		assertThat(tail.size(), is(TEST_MAP.size()/2));
		for (Key<Bill, Integer> key : tail.keySet())
			assertTrue(500 <= (Long) key.getParent().getParent().value());
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
