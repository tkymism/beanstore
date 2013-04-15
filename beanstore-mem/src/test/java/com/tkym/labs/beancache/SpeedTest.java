package com.tkym.labs.beancache;

import java.text.DecimalFormat;

import org.junit.BeforeClass;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class SpeedTest {
	private static final BeancacheRepository REPO = new BeancacheRepository();
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final Beancache<Person,Long> PERSON_CACHE = REPO.get(PERSON);
	private static final Beancache<Account,String> ACCOUNT_CACHE = REPO.get(ACCOUNT); 
	private static final Beancache<Bill,Integer> BILL_CACHE = REPO.get(BILL); 
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
	
//	@Test
//	public void testBeancachePropertyEqualsToCase001(){
//		Set<Key<Bill,Integer>> ret = 
//				BILL_CACHE.scan().
//				key().child(KeyBuilder.root().meta(PERSON).is(99L).build()).
//				keySet();
//		assertThat(ret.size(), is(1000));
//	}
//	@Test
//	public void testBeancachePropertyEqualsToCase002(){
//		Set<Key<Bill,Integer>> ret = 
//				BILL_CACHE.scan().
//				key().child(KeyBuilder.root().meta(PERSON).is(99L).build()).
//				keySet();
//		assertThat(ret.size(), is(1000));
//	}
}
