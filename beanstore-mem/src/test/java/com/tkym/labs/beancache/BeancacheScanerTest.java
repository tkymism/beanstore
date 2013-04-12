package com.tkym.labs.beancache;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheScanerTest {
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final Beancache<Person,Long> PERSON_CACHE = new Beancache<Person, Long>(PERSON); 
	private static final Beancache<Account,String> ACCOUNT_CACHE = new Beancache<Account, String>(ACCOUNT); 
	private static final Beancache<Bill,Integer> BILL_CACHE = new Beancache<Bill, Integer>(BILL); 
	private static final DecimalFormat PF = new DecimalFormat("00000");
	private static final DecimalFormat AF = new DecimalFormat("000");
	@BeforeClass
	public static void setupClass(){
		for (int i=0; i<PERSON_SIZE; i++){
			Person p = person(i);
			Key<Person, Long> pk = 
					KeyBuilder.root().
					meta(PERSON).is(p.getId()).
					build(); 
			PERSON_CACHE.put(pk, p);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = account(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				ACCOUNT_CACHE.put(ak, a);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = bill(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					BILL_CACHE.put(bk, b);
				}
			}
		}
	}
	
	static Person person(int i){
		Person person = new Person();
		person.setId(i);
		person.setName("a"+PF.format(i));
		return person;
	}
	
	static Account account(int i, int j){
		Account account = new Account();
		account.setEmail(PF.format(i)+"@"+AF.format(j)+".com");
		account.setAddress(PF.format(i)+"st."+AF.format(j));
		return account;
	}
	
	static Bill bill(int i, int j, int k){
		Bill bill = new Bill();
		bill.setNo(k);
		bill.setAmount(i+j);
		bill.setItem(j, AF.format(j)+PF.format(i));
		return bill;
	}
	
	@Test
	public void testBeancacheCase001(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.scan().comparable(PERSON.name).index().headIndex("a00001", false).keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00000"));
	}
	
	@Test
	public void testBeancacheCase002(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.scan().comparable(PERSON.name).index().headIndex("a00001", true).keySet();
		assertThat(ret.size(), is(2));
	}

	@Test
	public void testBeancacheCase003(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.scan().comparable(PERSON.name).index().tailIndex("a00098", false).keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00099"));
	}
	
	@Test
	public void testBeancacheCase004(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.scan().comparable(PERSON.name).index().tailIndex("a00098", true).keySet();
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancacheCase005(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).index().subIndex("a00003", false, "a00005", false).
				keySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancacheCase006(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).index().subIndex("a00003", true, "a00005", false).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheCase007(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).index().subIndex("a00003", false, "a00005", true).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheCase008(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).index().subIndex("a00003", true, "a00005", true).
				keySet();
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancacheCase009(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).index().subIndex("a00003", true, "a00005", true).
				keySet();
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancachePropertyCase001(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).head("a00001", false).
				keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00000"));
	}
	
	@Test
	public void testBeancachePropertyCase002(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).head("a00001", true).
				keySet();
		assertThat(ret.size(), is(2));
	}

	@Test
	public void testBeancachePropertyCase003(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).tail("a00098", false).
				keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00099"));
	}
	
	@Test
	public void testBeancachePropertyCase004(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).tail("a00098", true).
				keySet();
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancachePropertyCase005(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).sub("a00003", false, "a00005", false).
				keySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyCase006(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).sub("a00003", true, "a00005", false).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyCase007(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).sub("a00003", false, "a00005", true).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyCase008(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).sub("a00003", true, "a00005", true).
				keySet();
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancachePropertyCase009(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.scan().
				comparable(PERSON.name).sub("a00003", true, "a00005", true).
				keySet();
		assertThat(ret.size(), is(3));
	}
	
	@Test
	public void testBeancachePropertyCase101(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				comparable(ACCOUNT.address).tail("00098", false).
				keySet();
		assertThat(ret.size(), is(2*ACCOUNT_SIZE));
		for (Key<Account,String> k : ret)
			assertTrue(
					k.value().startsWith("00098") ||
					k.value().startsWith("00099")
					);
	}
	
	@Test
	public void testBeancachePropertyCase109(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				comparable(ACCOUNT.address).sub("00003", true, "00005", true).
				comparable(ACCOUNT.address).sub("00003", false, "00005", false).
				keySet();
		assertThat(ret.size(), is(2*ACCOUNT_SIZE));
		for (Key<Account,String> k : ret)
			assertTrue(
					k.value().startsWith("00003") ||
					k.value().startsWith("00004")
					);
	}
	@Test
	public void testBeancacheKeyCase001(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				keySet();
		assertThat(ret.size(), is(1*ACCOUNT_SIZE));
		for (Key<Account, String> k : ret)
			assertTrue(ACCOUNT_CACHE.get(k).getAddress().startsWith("00003st."));
	}
	@Test
	public void testBeancacheKeyCase002(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).tail("00003st.008", true).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase003(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).tail("00003st.008", false).
				keySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancacheKeyCase004(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).head("00003st.001", true).
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase005(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).head("00003st.001", false).
				keySet();
		assertThat(ret.size(), is(1));
	}
	
	@Test
	public void testBeancachePropertyGreaterLessCase001(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).lessThan("00003st.001").
				keySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase002(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).lessEqual("00003st.001").
				keySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase003(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).greaterThan("00003st.008").
				keySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase004(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.scan().
				key().child(KeyBuilder.root().meta(PERSON).is(3L).build()).
				comparable(ACCOUNT.address).greaterEqual("00003st.008").
				keySet();
		assertThat(ret.size(), is(2));
	}
}