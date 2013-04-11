package com.tkym.labs.beancache;

import static com.tkym.labs.beancache.BeancacheKeyTreeIndex.chainKeyAsMax;
import static com.tkym.labs.beancache.BeancacheKeyTreeIndex.chainKeyAsMin;
import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.Key.MaxKeyValue;
import com.tkym.labs.beanmeta.Key.MinKeyValue;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.DataProvider;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheKeyTreeIndexTest {
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final BeancacheKeyTreeIndex<Person,Long> PERSON_IDX 
		= new BeancacheKeyTreeIndex<Person, Long>(PERSON); 
	private static final BeancacheKeyTreeIndex<Account,String> ACCOUNT_IDX
		= new BeancacheKeyTreeIndex<Account, String>(ACCOUNT); 
	private static final BeancacheKeyTreeIndex<Bill,Integer> BILL_IDX
		= new BeancacheKeyTreeIndex<Bill, Integer>(BILL); 
	@BeforeClass
	public static void setupClass(){
		for (int i=0; i<PERSON_SIZE; i++){
			Person p = DataProvider.create(i);
			Key<Person, Long> pk = 
					KeyBuilder.root().
					meta(PERSON).is(p.getId()).
					build(); 
			PERSON_IDX.add(pk);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = DataProvider.create(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				ACCOUNT_IDX.add(ak);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = DataProvider.create(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					BILL_IDX.add(bk);
				}
			}
		}
	}
	
	@Test
	public void testSubIndexPERSON_Case001(){
		BeancacheKeyTreeIndex<Person, Long> sub =
				PERSON_IDX.subIndex(
				KeyBuilder.root().meta(PERSON).is(1L).build(), true, 
				KeyBuilder.root().meta(PERSON).is(2L).build(), true);
		assertThat(sub.navigableSet().size(), is(2));
	}
	
	@Test
	public void testSubIndexACCOUNT_Case001(){
		BeancacheKeyTreeIndex<Account, String> sub =
				ACCOUNT_IDX.subIndex(
				KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).min().build(), true, 
				KeyBuilder.root().meta(PERSON).is(2L).meta(ACCOUNT).max().build(), true);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE));
	}
	
	@Test
	public void testSubIndexBILL_Case001(){
		BeancacheKeyTreeIndex<Bill, Integer> sub =
				BILL_IDX.subIndex(
				KeyBuilder.root().
					meta(PERSON).is(1L).
					meta(ACCOUNT).min().
					meta(BILL).min().
					build(), true, 
				KeyBuilder.root().
					meta(PERSON).is(2L).
					meta(ACCOUNT).max().
					meta(BILL).max().
					build(), true);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE*BILL_SIZE));
	}
	
	@Test
	public void testMaxKeyCase001(){
		Key<Bill, Integer> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				meta(BILL).is(3).
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMax(BILL, arg);
		assertSame(key, arg);
	}
	@Test
	public void testMaxKeyCase002(){
		Key<Account, String> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMax(BILL, arg);
		assertThat(key, is(MaxKeyValue.class));
		assertThat(key.getBeanMeta().equals(BILL), is(true));
	}
	
	@Test
	public void testMaxKeyCase003(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMax(BILL, arg);
		assertThat(key, is(MaxKeyValue.class));
		assertThat(key.getBeanMeta().equals(BILL), is(true));
	}
	
	@Test
	public void testMaxKeyCase004(){
		Key<Account, String> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				build(); 
		Key<Account, String> key = 
				chainKeyAsMax(ACCOUNT, arg);
		assertSame(key, arg);
	}
	
	@Test
	public void testMaxKeyCase005(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Account, String> key = 
				chainKeyAsMax(ACCOUNT, arg);
		assertThat(key, is(MaxKeyValue.class));
		assertThat(key.getBeanMeta().equals(ACCOUNT), is(true));
	}
	
	@Test
	public void testMaxKeyCase006(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Person, Long> key = 
				BeancacheKeyTreeIndex.chainKeyAsMax(PERSON, arg);
		assertSame(key, arg);
	}
	
	@Test
	public void testMinKeyCase001(){
		Key<Bill, Integer> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				meta(BILL).is(3).
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMin(BILL, arg);
		assertSame(key, arg);
	}
	@Test
	public void testMinKeyCase002(){
		Key<Account, String> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMin(BILL, arg);
		assertThat(key, is(MinKeyValue.class));
		assertThat(key.getBeanMeta().equals(BILL), is(true));
	}
	
	@Test
	public void testMinKeyCase003(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Bill, Integer> key = 
				chainKeyAsMin(BILL, arg);
		assertThat(key, is(MinKeyValue.class));
		assertThat(key.getBeanMeta().equals(BILL), is(true));
	}
	
	@Test
	public void testMinKeyCase004(){
		Key<Account, String> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				meta(ACCOUNT).is("aa").
				build(); 
		Key<Account, String> key = 
				chainKeyAsMin(ACCOUNT, arg);
		assertSame(key, arg);
	}
	
	@Test
	public void testMinKeyCase005(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Account, String> key = 
				chainKeyAsMin(ACCOUNT, arg);
		assertThat(key, is(MinKeyValue.class));
		assertThat(key.getBeanMeta().equals(ACCOUNT), is(true));
	}
	
	@Test
	public void testMinKeyCase006(){
		Key<Person, Long> arg = 
				KeyBuilder.root().
				meta(PERSON).is(1L).
				build(); 
		Key<Person, Long> key = 
				chainKeyAsMin(PERSON, arg);
		assertSame(key, arg);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMinKeyCaseException(){
		Key<Hoge, String> arg = 
				KeyBuilder.root().
				meta(HogeMeta.get()).is("hogehoge").
				build(); 
		chainKeyAsMin(ACCOUNT, arg);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHeadKeyIndexCase001(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.headKeyIndex(KeyBuilder.root().meta(PERSON).is(1L).build(), false);
		assertThat(sub.navigableSet().size(), is(1*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >= 0L);
			assertTrue(v <  1L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHeadKeyIndexCase002(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.headKeyIndex(KeyBuilder.root().meta(PERSON).is(1L).build(), true);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >= 0L);
			assertTrue(v <= 1L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTailKeyIndexCase001(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.tailKeyIndex(KeyBuilder.root().meta(PERSON).is(98L).build(), false);
		assertThat(sub.navigableSet().size(), is(1*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >  98L);
			assertTrue(v <= 99L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTailKeyIndexCase002(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.tailKeyIndex(KeyBuilder.root().meta(PERSON).is(98L).build(), true);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >= 98L);
			assertTrue(v <= 99L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSubKeyIndexCase001(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.subKeyIndex(
						KeyBuilder.root().meta(PERSON).is(1L).build(), false,
						KeyBuilder.root().meta(PERSON).is(3L).build(), false
						);
		assertThat(sub.navigableSet().size(), is(1*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v > 1L);
			assertTrue(v < 3L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSubKeyIndexCase002(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.subKeyIndex(
						KeyBuilder.root().meta(PERSON).is(1L).build(), true,
						KeyBuilder.root().meta(PERSON).is(3L).build(), false
						);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >= 1L);
			assertTrue(v < 3L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSubKeyIndexCase003(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.subKeyIndex(
						KeyBuilder.root().meta(PERSON).is(1L).build(), false,
						KeyBuilder.root().meta(PERSON).is(3L).build(), true
						);
		assertThat(sub.navigableSet().size(), is(2*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v > 1L);
			assertTrue(v <= 3L);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSubKeyIndexCase004(){
		BeancacheKeyTreeIndex<Bill,Integer> sub = 
				BILL_IDX.subKeyIndex(
						KeyBuilder.root().meta(PERSON).is(1L).build(), true,
						KeyBuilder.root().meta(PERSON).is(3L).build(), true
						);
		assertThat(sub.navigableSet().size(), is(3*ACCOUNT_SIZE*BILL_SIZE));
		for (Key<Bill, Integer> b : sub.navigableSet()){
			long v = ((Key<Person,Long>) b.getParent().getParent()).value(); 
			assertTrue(v >= 1L);
			assertTrue(v <= 3L);
		}
	}
}