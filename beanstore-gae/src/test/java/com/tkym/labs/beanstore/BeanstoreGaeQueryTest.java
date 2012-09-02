package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanmeta.Key;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.tkym.labs.beanstore.BeanstoreServiceGaeFactory;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

/**
 * 
 * @author takayama
 */
public class BeanstoreGaeQueryTest {
	private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
    	PersonMeta.get().name.setIndexed(true);
    	AccountMeta.get().address.setIndexed(true);
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    Person person(long id){
    	Person person = new Person();
    	person.setId(id);
    	person.setName("foo "+id);
    	return person;
    }
    
    Account account(String name){
    	Account bean = new Account();
    	bean.setEmail(name+"@yahoo.com");
    	bean.setAddress(name+". st");
    	return bean;
    }
    
    @Test
    public void testBeanstoreServiceCase001() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	service.store(PERSON).put(person(10L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(11L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(12L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(13L));
    	service.getTransaction().commit();
    	List<Key<Person,Long>> keyList = 
    			service.
    			query(PERSON).
    			key().
    			asList();
    	assertThat(keyList.size(), is(4));
    	List<Person> fooList = service.query(PERSON).
    		filter(PERSON.name).greaterEqual("foo").bean().asList();
    	assertThat(fooList.size(), is(4));
    }
    
    @Test
    public void testBeanstoreServiceCase002() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	service.store(PERSON).put(person(10L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(11L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(12L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(23L));
    	service.getTransaction().commit();
    	List<Key<Person,Long>> keyList = service.query(PersonMeta.get()).key().asList();
    	assertThat(keyList.size(), is(4));
    	List<Person> fooList = service.query(PERSON).
    		filter(PERSON.name).startsWith("foo 1").bean().asList();
    	assertThat(fooList.size(), is(3));
    }
    
    @Test
    public void testBeanstoreServiceCase003() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	service.store(PERSON).put(person(10L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(11L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(12L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(23L));
    	service.getTransaction().commit();
    	List<Key<Person,Long>> keyList = service.query(PersonMeta.get()).key().asList();
    	assertThat(keyList.size(), is(4));
    	List<Person> fooList = service.query(PERSON).
    		filter(PERSON.name).startsWith("foo").bean().asList();
    	assertThat(fooList.size(), is(4));
    }
    
    @Test
    public void testBeanstoreServiceCase004() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	service.store(PERSON).put(person(9L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(11L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(12L));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(23L));
    	service.getTransaction().commit();
    	List<Key<Person,Long>> keyList = service.query(PersonMeta.get()).key().asList();
    	assertThat(keyList.size(), is(4));
    	List<Person> fooList = service.query(PERSON).
    		filter(PERSON.name).greaterThan("foo 11").bean().asList();
    	assertThat(fooList.size(), is(3));
    }

    @Test
    public void testBeanstoreServiceCase101() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	AccountMeta ACCOUNT = AccountMeta.get();
    	service.store(PERSON).put(person(1L));
    	service.store(PERSON).is(1L).store(ACCOUNT).put(account("abc"));
    	service.store(PERSON).is(1L).store(ACCOUNT).put(account("def"));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(2L));
    	service.store(PERSON).is(2L).store(ACCOUNT).put(account("abc"));
    	service.store(PERSON).is(2L).store(ACCOUNT).put(account("def"));
    	service.getTransaction().commit();
    	List<Key<Account,String>> keyList = service.query(ACCOUNT).key().asList();
    	assertThat(keyList.size(), is(4));
    	List<Account> aList = service.query(ACCOUNT).
        		filter(ACCOUNT.address).startsWith("abc").bean().asList();
    	assertThat(aList.size(), is(2));
    	List<Person> pList = service.query(PERSON).
    			filter(PERSON.name).startsWith("foo").bean().asList();
    	assertThat(pList.size(), is(2));
    	for(Account a : aList)
    		assertThat(a.getEmail(), is("abc@yahoo.com"));
    }

    @Test
    public void testBeanstoreServiceCase102() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	AccountMeta ACCOUNT = AccountMeta.get();
    	service.store(PERSON).put(person(3L));
    	service.store(PERSON).is(3L).store(ACCOUNT).put(account("abc"));
    	service.store(PERSON).is(3L).store(ACCOUNT).put(account("def"));
    	service.getTransaction().commit();
    	service.store(PERSON).put(person(4L));
    	service.store(PERSON).is(4L).store(ACCOUNT).put(account("abc"));
    	service.store(PERSON).is(4L).store(ACCOUNT).put(account("def"));
    	service.getTransaction().commit();
    	List<Account> list = service.store(PERSON).is(4L).query(ACCOUNT).filter(ACCOUNT.address).startsWith("def").bean().asList();
    	assertThat(list.size(), is(1));
    }

    @Test
    public void testERRORRRRR_BeanstoreServiceCase103() throws BeanstoreException{
    	BeanstoreServiceGaeFactory factory = new BeanstoreServiceGaeFactory();
    	BeanstoreService<Void,Void> service = factory.create();
    	PersonMeta PERSON = PersonMeta.get();
    	AccountMeta ACCOUNT = AccountMeta.get();
    	
    	Beanstore<Account, String> bs3 = 
    			service.store(PERSON).is(3L).store(ACCOUNT);
    	Beanstore<Account, String> bs4 = 
    			service.store(PERSON).is(4L).store(ACCOUNT);
    	
    	bs3.put(account("abc"));
    	bs3.put(account("def"));
    	service.getTransaction().commit();
    	bs4.put(account("abc"));
    	bs4.put(account("def"));
    	service.getTransaction().commit();
    	
    	List<Account> list = 
    			service.query(ACCOUNT).
    			filter(ACCOUNT.address).startsWith("def").
    			bean().asList();
    	
    	assertThat(list.size(), is(2));
    	
    	service.getTransaction().commit();
    	
    	List<Account> list2 = 
    			service.query(ACCOUNT).
    			filter(ACCOUNT.address).startsWith("def").
    			bean().asList();
    	
    	assertThat(list2.size(), is(2));
    	
    }
}