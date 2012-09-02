package com.tkym.labs.beanstore;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.tkym.labs.beanstore.KeyConverter;
import com.tkym.labs.beanstore.KeyConverterFactory;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class KeyConverterTest {
	private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

	@Test
	public void testKeyConverterCase001(){
		KeyConverter<Person, Long> personConverter = new KeyConverter<Person, Long>(PersonMeta.get());
		Key key = personConverter.convert(1L);
		assertThat(key.getKind(), is("person"));
		assertThat(key.getId(), is(1L));
		assertNull(key.getName());
		assertNull(key.getParent());
		com.tkym.labs.beanmeta.Key<Person, Long> bKey = personConverter.convert(key);
		assertNull(bKey.getParent());
		assertThat(bKey.value(),is(1L));
		assertThat(bKey.getBeanMeta().getName(),is("person"));
	}
	
	@Test
	public void testKeyConverterCase002(){
		KeyConverter<Person, Long> personConverter = new KeyConverter<Person, Long>(PersonMeta.get());
		Key personKey = personConverter.convert(1L);
		KeyConverter<Account, String> accountConverter = new KeyConverter<Account, String>(personKey, AccountMeta.get());
		Key accountKey = accountConverter.convert("foo@email.com");
		assertThat(accountKey.getKind(), is("ACCOUNT"));
		assertThat(accountKey.getName(), is("foo@email.com"));
		assertThat(accountKey.getId(), is(0L));
		Key parent = accountKey.getParent();
		assertThat(parent.getKind(), is("person"));
		com.tkym.labs.beanmeta.Key<Account, String> bAccountKey = accountConverter.convert(accountKey);
		assertThat(bAccountKey.getParent().getBeanMeta().getName(),is("person"));
		assertThat(bAccountKey.getParent().value(),is((Object)1L));
	}
	
	@Test
	public void testKeyConverterCase003(){
		KeyConverter<Person, Long> personConverter = new KeyConverter<Person, Long>(PersonMeta.get());
		Key personKey = personConverter.convert(1L);
		KeyConverter<Account, String> accountConverter = new KeyConverter<Account, String>(personKey, AccountMeta.get());
		Key accountKey = accountConverter.convert("foo@email.com");
		KeyConverter<Bill, Integer> billConverter = new KeyConverter<Bill, Integer>(accountKey, BillMeta.get());
		Key billKey = billConverter.convert(102);
		assertThat(billKey.getKind(), is("bill"));
		assertNull(billKey.getName());
		assertThat(billKey.getId(), is(102L));
		com.tkym.labs.beanmeta.Key<Bill, Integer> bBillKey =
				billConverter.convert(billKey);
		assertThat(bBillKey.value(), is(102));
		assertThat(bBillKey.getBeanMeta().getName(), is("bill"));
		com.tkym.labs.beanmeta.Key<?, ?> bAccountKey = bBillKey.getParent(); 
		assertThat(bAccountKey.value(), is((Object)"foo@email.com"));
		assertThat(bAccountKey.getBeanMeta().getName(), is("ACCOUNT"));
	}
	
	@Test
	public void testKeyConverterFactoryCase001(){
		com.tkym.labs.beanmeta.Key<Account, String> parent = 
				AccountMeta.get().key(
						PersonMeta.get().key(null, 11L), 
						"foo@email.com");
		Key billKey = KeyConverterFactory.create(parent, BillMeta.get()).convert(103);
		assertThat(billKey.getKind(), is("bill"));
		assertNull(billKey.getName());
		assertThat(billKey.getId(), is(103L));
		Key accountKey = billKey.getParent();
		assertThat(accountKey.getKind(), is("ACCOUNT"));
		assertThat(accountKey.getName(), is("foo@email.com"));
		assertThat(accountKey.getId(), is(0L));
		Key personKey = accountKey.getParent();
		assertThat(personKey.getKind(), is("person"));
		assertNull(personKey.getName());
		assertThat(personKey.getId(), is(11L));
	}
}