package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheTest {
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
			PERSON_CACHE.map().put(pk, p);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = account(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				ACCOUNT_CACHE.map().put(ak, a);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = bill(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					BILL_CACHE.map().put(bk, b);
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
		Set<Key<Person,Long>> ret = PERSON_CACHE.index(PERSON.name).headIndex("a00001", false).keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00000"));
	}
	
	@Test
	public void testBeancacheCase002(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.index(PERSON.name).headIndex("a00001", true).keySet();
		assertThat(ret.size(), is(2));
//		Iterator<Key<Person,Long>> ite = ret.iterator();
//		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00001"));
//		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00000"));
	}

	@Test
	public void testBeancacheCase003(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.index(PERSON.name).tailIndex("a00098", false).keySet();
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00099"));
	}
	
	@Test
	public void testBeancacheCase004(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.index(PERSON.name).tailIndex("a00098", true).keySet();
		assertThat(ret.size(), is(2));
//		Iterator<Key<Person,Long>> ite = ret.iterator();
//		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00098"));
//		assertThat(PERSON_CACHE.map().get(ite.next()).getName(), is("a00099"));
	}
	
	static class Beancache<BT,KT extends Comparable<KT>> {
		private final Map<Key<BT, KT>, BT> hashMap = new ConcurrentHashMap<Key<BT,KT>, BT>();
		private final BeanMeta<BT, KT> beanMeta;
		Beancache(BeanMeta<BT, KT> beanMeta) {
			this.beanMeta = beanMeta;
		}
		Map<Key<BT, KT>, BT> map() {
			return this.hashMap;
		}
		<PT extends Comparable<PT>> BeancachePropertyTreeIndex<BT, KT, PT> index(PropertyMeta<BT,PT> propertyMeta){
			BeancachePropertyTreeIndex<BT, KT, PT> index = new BeancachePropertyTreeIndex<BT, KT, PT>(beanMeta, propertyMeta);
			for (Key<BT,KT> key : hashMap.keySet()) index.put(key, hashMap.get(key));
			return index;
		}
		<PT extends Comparable<PT>> BeancachePropertyTreeIndex<BT, KT, PT> index(PropertyMeta<BT,PT> propertyMeta, Set<Key<BT,KT>> join){
			BeancachePropertyTreeIndex<BT, KT, PT> index = new BeancachePropertyTreeIndex<BT, KT, PT>(beanMeta, propertyMeta);
			for (Key<BT,KT> key : hashMap.keySet()) 
				if (join.contains(key)) 
					index.put(key, hashMap.get(key));
			return index;
		}
	}
}