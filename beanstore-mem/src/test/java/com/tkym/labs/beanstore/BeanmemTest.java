package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
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
}