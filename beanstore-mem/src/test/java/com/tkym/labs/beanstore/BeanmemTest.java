package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.PersonMeta;

public class BeanmemTest {
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
	
	@Test
	public void testRead_TEST_MAP003(){
		ConcurrentNavigableMap<Key<Bill, Integer>, Bill> sub = 
				TEST_MAP.tailMap(
						KeyBuilder.root().
						meta(PERSON).is(500L).
						meta(ACCOUNT).is(null).
						meta(BILL).is(null).
					build(), true).
				headMap(
						KeyBuilder.root().
						meta(PERSON).is(501L).
						meta(ACCOUNT).is(null).
						meta(BILL).is(null).
					build(), true);
		assertThat(sub.size(), is(TEST_MAP.size()/1000));
		for (Key<Bill, Integer> key : sub.keySet())
			assertThat((Long) key.getParent().getParent().value(), is(500L));
	}
	
	@Test
	public void testRead_TEST_MAP004(){
		ConcurrentNavigableMap<Key<Bill, Integer>, Bill> sub = 
				TEST_MAP.tailMap(
						KeyBuilder.root().
						meta(PERSON).is(500L).
						meta(ACCOUNT).min().
						meta(BILL).min().
						build(), false).
				headMap(
						KeyBuilder.root().
						meta(PERSON).is(500L).
						meta(ACCOUNT).max().
						meta(BILL).max().
					build(), false);
		assertThat(sub.size(), is(TEST_MAP.size()/1000));
		for (Key<Bill, Integer> key : sub.keySet())
			assertThat((Long) key.getParent().getParent().value(), is(500L));
	}
	
	@Test
	public void testRead_TEST_MAP005(){
		Bill bill = TEST_MAP.get(
				KeyBuilder.root().
				meta(PERSON).is(500L).
				meta(ACCOUNT).is(email(1)).
				meta(BILL).is(0).build());
		assertThat(bill.getNo(), is(0));
	}
	
	@Test
	public void testSubMap001(){
		List<BeanMeta<?,?>> trace = BeanmemMap.traceParant(BILL, PERSON);
		for (BeanMeta<?,?> meta : trace)
			System.out.println(meta.getName());
	}
	
	@Test
	public void testComparator(){
		Comparator<Account> comparator = new Comparator<Account>() {
			@Override
			public int compare(Account o1, Account o2) {
				int ret = o1.getAddress().compareTo(o2.getAddress());
				if (ret == 0) ret = o1.getEmail().compareTo(o2.getEmail());
				return ret;
			}
		};
		TreeSet<Account> treeSet = new TreeSet<Account>(comparator);
		treeSet.add(DataProvider.create(3L, 0));
		treeSet.add(DataProvider.create(3L, 1));
		treeSet.add(DataProvider.create(3L, 2));
		treeSet.add(DataProvider.create(2L, 0));
		treeSet.add(DataProvider.create(2L, 1));
		treeSet.add(DataProvider.create(2L, 2));
		assertThat(treeSet.size(), is(6));
	}
	
	class BeanMemRepository{
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
		BeanmemMap<BT,KT> sub(Key<?,?> parent){
			Key<BT,KT> max = buildMaxKey(parent, this.beanMeta);
			Key<BT,KT> min = buildMinKey(parent, this.beanMeta);
			return new BeanmemMap<BT,KT>(beanMeta, memmap.headMap(max).tailMap(min));
		}
		static List<BeanMeta<?,?>> traceParant(BeanMeta<?,?> from, BeanMeta<?,?> to){
			LinkedList<BeanMeta<?,?>> array = new LinkedList<BeanMeta<?,?>>();
			BeanMeta<?,?> current = from;
			while(current != null){
				if (current.equals(to)) break;
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
	class Beanmem<BT,KT>{
		private final Key<BT, KT> key;
		private final BT value;
		Beanmem(Key<BT, KT> key, BT value) {
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
	class BeanmemComparator<BT,KT,PT extends Comparable<PT>> implements Comparator<Beanmem<BT,KT>>{
		private final PropertyMeta<BT, PT> propertyMeta;
		BeanmemComparator(PropertyMeta<BT, PT> propertyMeta){
			this.propertyMeta = propertyMeta;
			if (!Comparable.class.isAssignableFrom(propertyMeta.getPropertyType()))
				throw new IllegalArgumentException(
								propertyMeta.getBeanType().getName()+"."+
										propertyMeta.getPropertyName()+
								" is not support Comparator. type is"+
										propertyMeta.getPropertyType().getName());
		}
		@Override
		public int compare(Beanmem<BT, KT> o1, Beanmem<BT, KT> o2) {
			PT p1 = propertyMeta.access(o1.getValue()).get();
			PT p2 = propertyMeta.access(o2.getValue()).get();
			return p1.compareTo(p2);
		}
	}
}