package com.tkym.labs.beanstore;

import static com.tkym.labs.beanstore.api.BeanQueryUtils.and;
import static com.tkym.labs.beanstore.api.BeanQueryUtils.or;
import static com.tkym.labs.beanstore.api.BeanQueryUtils.property;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beancache.BeancacheRepository;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheFilterProcesserTest {
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final DecimalFormat PF = new DecimalFormat("00000");
	private static final DecimalFormat AF = new DecimalFormat("000");
	private static final BeancacheRepository REPO = new BeancacheRepository();
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
	@BeforeClass
	public static void setupClass(){
		for (int i=0; i<PERSON_SIZE; i++){
			Person p = person(i);
			Key<Person, Long> pk = 
					KeyBuilder.root().
					meta(PERSON).is(p.getId()).
					build(); 
			REPO.get(PERSON).put(pk, p);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = account(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				REPO.get(ACCOUNT).put(ak, a);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = bill(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					REPO.get(BILL).put(bk, b);
				}
			}
		}
	}
	
	@Test
	public void testFilterCase001(){
		Set<Key<Person, Long>> r = 
			BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).equalsTo("a00001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testFilterCase002(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).lessThan("a00001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testFilterCase003(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).lessEqual("a00001"));
		assertThat(r.size(), is(2));
	}
	
	@Test
	public void testFilterCase004(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).greaterThan("a00001"));
		assertThat(r.size(), is(98));
	}
	
	@Test
	public void testFilterCase005(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).greaterEqual("a00001"));
		assertThat(r.size(), is(99));
	}
	
	@Test
	public void testFilterCase006(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).notEquals("a00001"));
		assertThat(r.size(), is(99));
	}
	
	@Test
	public void testFilterCase007(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).in("a00001", "a00002", "a00003"));
		assertThat(r.size(), is(3));
	}
	
	@Test
	public void testFilterCase008(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).startsWith("a00"));
		assertThat(r.size(), is(100));
	}

	@Test
	public void testFilterCase009(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).startsWith("b00"));
		assertThat(r.size(), is(0));
	}
	
	@Test
	public void testFilterCase010(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).endsWith("001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testFilterCase011(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				filter(property(PERSON.name).contains("00000"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testFilterCase101(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).equalsTo("00003st.001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testFilterCase102(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).lessThan("00003st.001"));
		assertThat(r.size(), is(31));
	}
	
	@Test
	public void testFilterCase103(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).lessEqual("00003st.001"));
		assertThat(r.size(), is(32));
	}
	
	@Test
	public void testFilterCase104(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).greaterThan("00003st.001"));
		assertThat(r.size(), is(1000-32));
	}
	
	@Test
	public void testFilterCase105(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).greaterEqual("00003st.001"));
		assertThat(r.size(), is(1000-31));
	}
	
	@Test
	public void testFilterCase106(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).notEquals("00003st.001"));
		assertThat(r.size(), is(999));
	}
	
	@Test
	public void testFilterCase107(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).in("00003st.001", "00001st.003", "00002st.003"));
		assertThat(r.size(), is(3));
	}
	
	@Test
	public void testFilterCase108(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).startsWith("0000"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testFilterCase109(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).endsWith("1st.001"));
		assertThat(r.size(), is(10));
	}
	
	@Test
	public void testFilterCase110(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).contains("1st.0"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testFilterCase111(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				filter(property(ACCOUNT.address).contains("1st.0"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testCriteriaCase001(){
		Set<Key<Person, Long>> r = 
			BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).equalsTo("a00001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testCriteriaCase002(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).lessThan("a00001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testCriteriaCase003(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).lessEqual("a00001"));
		assertThat(r.size(), is(2));
	}
	
	@Test
	public void testCriteriaCase004(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).greaterThan("a00001"));
		assertThat(r.size(), is(98));
	}
	
	@Test
	public void testCriteriaCase005(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).greaterEqual("a00001"));
		assertThat(r.size(), is(99));
	}
	
	@Test
	public void testCriteriaCase006(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).notEquals("a00001"));
		assertThat(r.size(), is(99));
	}
	
	@Test
	public void testCriteriaCase007(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).in("a00001", "a00002", "a00003"));
		assertThat(r.size(), is(3));
	}
	
	@Test
	public void testCriteriaCase008(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).startsWith("a00"));
		assertThat(r.size(), is(100));
	}

	@Test
	public void testCriteriaCase009(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).startsWith("b00"));
		assertThat(r.size(), is(0));
	}
	
	@Test
	public void testCriteriaCase010(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).endsWith("001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testCriteriaCase011(){
		Set<Key<Person, Long>> r = 
				BeancacheFilterProcesser.target(REPO.get(PERSON).queryAll()).
				criteria(property(PERSON.name).contains("00000"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testCriteriaCase101(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).equalsTo("00003st.001"));
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testCriteriaCase102(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).lessThan("00003st.001"));
		assertThat(r.size(), is(31));
	}
	
	@Test
	public void testCriteriaCase103(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).lessEqual("00003st.001"));
		assertThat(r.size(), is(32));
	}
	
	@Test
	public void testCriteriaCase104(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).greaterThan("00003st.001"));
		assertThat(r.size(), is(1000-32));
	}
	
	@Test
	public void testCriteriaCase105(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).greaterEqual("00003st.001"));
		assertThat(r.size(), is(1000-31));
	}
	
	@Test
	public void testCriteriaCase106(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).notEquals("00003st.001"));
		assertThat(r.size(), is(999));
	}
	
	@Test
	public void testCriteriaCase107(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).in("00003st.001", "00001st.003", "00002st.003"));
		assertThat(r.size(), is(3));
	}
	
	@Test
	public void testCriteriaCase108(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).startsWith("0000"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testCriteriaCase109(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).endsWith("1st.001"));
		assertThat(r.size(), is(10));
	}
	
	@Test
	public void testCriteriaCase110(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).contains("1st.0"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testCriteriaCase111(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(property(ACCOUNT.address).contains("1st.0"));
		assertThat(r.size(), is(100));
	}
	
	@Test
	public void testCompositeCase001(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(and(
						property(ACCOUNT.address).contains("st.0"),
						property(ACCOUNT.address).startsWith("0000"),
						property(ACCOUNT.address).endsWith("1")
				));
		assertThat(r.size(), is(10));
	}
	@Test
	public void testCompositeCase002(){
		Set<Key<Account, String>> r = 
				BeancacheFilterProcesser.target(REPO.get(ACCOUNT).queryAll()).
				criteria(or(
						property(ACCOUNT.address).contains("st.001"),
						property(ACCOUNT.address).endsWith("st.002"),
						property(ACCOUNT.address).startsWith("00001")
				));
//		for (Key<Account, String> key : r)
//			System.out.println(REPO.get(ACCOUNT).get(key).getAddress());
		assertThat(r.size(), is(208));
	}
}