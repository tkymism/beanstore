package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheFilterTest {
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
//	private static final BillMeta BILL = BillMeta.get();

	@Test
	public void testAscPersonCase001(){
		BeancacheFilterMap<String, Person, Long> idx =
				new BeancacheFilterMap<String, Person, Long>(PERSON);
		idx.put("2", KeyBuilder.root().meta(PERSON).is(0L).build());
		idx.put("2", KeyBuilder.root().meta(PERSON).is(1L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(2L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(3L).build());
		Iterator<Key<Person, Long>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is(2L));
		assertThat(ite.next().value(), is(3L));
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(1L));
	}
	
	@Test
	public void testAscPersonCase002(){
		BeancacheFilterMap<String, Person, Long> idx =
				new BeancacheFilterMap<String, Person, Long>(PERSON);
		idx.put("1", KeyBuilder.root().meta(PERSON).is(0L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(1L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(2L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(3L).build());
		Iterator<Key<Person, Long>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(1L));
		assertThat(ite.next().value(), is(2L));
		assertThat(ite.next().value(), is(3L));
	}

	@Test
	public void testDescPersonCase001(){
		BeancacheFilterMap<String, Person, Long> idx =
				new BeancacheFilterMap<String, Person, Long>(PERSON);
		idx.put("0", KeyBuilder.root().meta(PERSON).is(0L).build());
		idx.put("0", KeyBuilder.root().meta(PERSON).is(1L).build());
		idx.put("0", KeyBuilder.root().meta(PERSON).is(2L).build());
		idx.put("0", KeyBuilder.root().meta(PERSON).is(3L).build());
		Iterator<Key<Person, Long>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is(3L));
		assertThat(ite.next().value(), is(2L));
		assertThat(ite.next().value(), is(1L));
		assertThat(ite.next().value(), is(0L));
	}

	@Test
	public void testDescPersonCase002(){
		BeancacheFilterMap<String, Person, Long> idx =
				new BeancacheFilterMap<String, Person, Long>(PERSON);
		idx.put("1", KeyBuilder.root().meta(PERSON).is(0L).build());
		idx.put("1", KeyBuilder.root().meta(PERSON).is(1L).build());
		idx.put("0", KeyBuilder.root().meta(PERSON).is(2L).build());
		idx.put("0", KeyBuilder.root().meta(PERSON).is(3L).build());
		Iterator<Key<Person, Long>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is(1L));
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(3L));
		assertThat(ite.next().value(), is(2L));
	}

	@Test
	public void testAscAccountCase001(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("d", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("e", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
	}

	@Test
	public void testAscAccountCase002(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
	}

	@Test
	public void testAscAccountCase003(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("e", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("d", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("a", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}

	@Test
	public void testAscAccountCase004(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
	}

	@Test
	public void testDescAccountCase001(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("d", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("e", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}

	@Test
	public void testDescAccountCase002(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}

	@Test
	public void testDescAccountCase003(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}

	@Test
	public void testDescAccountCase004(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("e", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("d", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("a", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("2"));
	}

	@Test
	public void testDescAccountCase005(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put("f", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}
	
	@Test
	public void testNullLastAscAccountCase001(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a" , KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put(null, KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("b" , KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put(null, KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("c" , KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put(null, KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.asc().all().iterator();	
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
	}
	
	@Test
	public void testDescAccountCase006(){
		BeancacheFilterMap<String, Account, String> idx =
				new BeancacheFilterMap<String, Account, String>(ACCOUNT);
		idx.put("a", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("0").build());
		idx.put("b", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("1").build());
		idx.put("c", KeyBuilder.root().meta(PERSON).is(0L).meta(ACCOUNT).is("2").build());
		idx.put("d", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("0").build());
		idx.put("e", KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("1").build());
		idx.put(null, KeyBuilder.root().meta(PERSON).is(1L).meta(ACCOUNT).is("2").build());
		Iterator<Key<Account, String>> ite = idx.desc().all().iterator();	
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
		assertThat(ite.next().value(), is("2"));
		assertThat(ite.next().value(), is("1"));
		assertThat(ite.next().value(), is("0"));
	}
}
