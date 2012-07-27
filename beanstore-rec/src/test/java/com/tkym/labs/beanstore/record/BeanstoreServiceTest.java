package com.tkym.labs.beanstore.record;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanmeta.beans.Account;
import com.tkym.labs.beanmeta.beans.AccountMeta;
import com.tkym.labs.beanmeta.beans.Bill;
import com.tkym.labs.beanmeta.beans.BillMeta;
import com.tkym.labs.beanmeta.beans.Person;
import com.tkym.labs.beanmeta.beans.PersonMeta;
import com.tkym.labs.record.SqliteRecordstoreRepository;

public class BeanstoreServiceTest {
	private static BeanstoreServiceRecord service; 
	private static PersonMeta PERSON = PersonMeta.get();
	private static AccountMeta ACCOUNT = AccountMeta.get();
	private static BillMeta BILL = BillMeta.get();
	
	private static Person create(long id){
		Person p = new Person();
		p.setId(id);
		p.setName("hoge+"+id);
		return p;
	}
	
	private static Account create(long id, int index){
		Account account = new Account();
		account.setEmail("hoge"+id+"@email"+index+".com");
		account.setAddress("hogehoge"+index);
		return account; 
	}
	
	private static Bill create(long id, int index, int billNo){
		Bill bill = new Bill();
		bill.setNo(billNo);
		bill.setAmount((float)((id*10)+(index)*0.1));
		return bill;
	}
	
	@BeforeClass
	public static void setupClass() throws Exception{
		SqliteRecordstoreRepository.asMemory();
		service = new BeanstoreServiceRecordFactory(SqliteRecordstoreRepository.asMemory()).create();
		service.create(PersonMeta.get());
		service.create(AccountMeta.get());
		service.create(BillMeta.get());
	}
	
	@AfterClass
	public static void teardownClass() throws Exception {
		service.drop(PersonMeta.get());
		service.drop(AccountMeta.get());
		service.drop(BillMeta.get());
		service.getTransaction().close();
	}
	
	@Test
	public void testPutData() throws BeanstoreException{
		Beanstore<Person, Long> personStore = service.store(PERSON);
		for(long id = 0; id<10; id++){
			Person person = create(id);
			personStore.put(person);
			Beanstore<Account, String> accountStore = 
					personStore.is(person.getId()).store(ACCOUNT);
			for(int emailIdx = 0; emailIdx<10; emailIdx++){
				Account account = create(id, emailIdx);
				accountStore.put(account);
				Beanstore<Bill, Integer> billStore = 
					accountStore.is(account.getEmail()).store(BILL);
				for(int billNo = 0; billNo<5; billNo++)
					billStore.put(create(id, emailIdx, billNo));
			}
		}
	}

	@Test
	public void testQueryDataCase001() throws BeanstoreException{
		List<Key<Person, Long>> personList = 
				service.query(PERSON).key().asList();
		List<Key<Account, String>> accountList = 
				service.query(ACCOUNT).key().asList();
		List<Key<Bill, Integer>> billList = 
				service.query(BILL).key().asList();
		assertThat(personList.size(), is(10));
		assertThat(accountList.size(), is(100));
		assertThat(billList.size(), is(500));
	}

	@Test
	public void testQueryDataCase002() throws BeanstoreException{
		List<Person> personList = 
				service.query(PERSON).bean().asList();
		List<Account> accountList = 
				service.query(ACCOUNT).bean().asList();
		List<Bill> billList = 
				service.query(BILL).bean().asList();
		assertThat(personList.size(), is(10));
		assertThat(accountList.size(), is(100));
		assertThat(billList.size(), is(500));
	}
	
	@Test
	public void testQueryDataCase003() throws BeanstoreException{
		Iterator<Key<Person, Long>> pIte = 
				service.query(PERSON).key().asIterator();
		while(pIte.hasNext()){
			BeanstoreService personService = 
					service.store(PERSON).is(pIte.next().value());
			List<Key<Account, String>> alist = 
					personService.query(ACCOUNT).key().asList();
			assertThat(alist.size(), is(10));
			for(Key<Account, String> akey : alist){
				BeanstoreService accountService = 
					personService.store(ACCOUNT).is(akey.value());
				List<Key<Bill, Integer>> blist = 
					accountService.query(BILL).key().asList();
				assertThat(blist.size(), is(5));
			}
		}
	}
	
	@Test
	public void testQueryDataCase004() throws BeanstoreException{
		Iterator<Key<Bill, Integer>> bIte = 
				service.query(BILL).
				filter(BILL.no).greaterThan(1).
				filter(BILL.no).lessThan(4).
				key().asIterator();
		
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		for(long id = 0; id<10; id++) countMap.put(id, 0);
		while(bIte.hasNext()){
			Key<Bill, Integer> bkey = bIte.next();
			Assert.assertTrue(bkey.value()<4);
			Assert.assertTrue(bkey.value()>1);
			Assert.assertNotNull(bkey.getParent());
			@SuppressWarnings("unchecked")
			Key<Account, String> akey = (Key<Account, String>)bkey.getParent();
			Assert.assertNotNull(akey.getParent());
			@SuppressWarnings("unchecked")
			Key<Person, Long> pkey = (Key<Person, Long>)akey.getParent();
			int count = countMap.get(pkey.value())+1;
			countMap.put(pkey.value(), count);
		}
		for (long id : countMap.keySet())
			Assert.assertThat(countMap.get(id), CoreMatchers.is(20));
	}
}