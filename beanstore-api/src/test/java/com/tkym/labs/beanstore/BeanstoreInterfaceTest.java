package com.tkym.labs.beanstore;


import java.util.Iterator;
import java.util.List;


import org.junit.Test;

import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;
import com.tkym.labs.beanmeta.beans.Account;
import com.tkym.labs.beanmeta.beans.AccountMeta;
import com.tkym.labs.beanmeta.beans.Person;
import com.tkym.labs.beanmeta.beans.PersonMeta;
import com.tkym.labs.beanmeta.Key;

public class BeanstoreInterfaceTest {
	private BeanstoreServiceFactory<BeanstoreService> factory = new BeanstoreServiceFactory<BeanstoreService>(){
		@Override
		public BeanstoreService create() {
			return null;
		}
	};
	
	@Test(expected=NullPointerException.class)
	public void interfaceTest() throws BeanstoreException{
		BeanstoreService service = factory.create();
		
		PersonMeta PERSON = PersonMeta.get();
		AccountMeta ACCOUNT = AccountMeta.get();
		Iterator<Key<Person,Long>> ite = 
				service.query(PERSON).
				filter(PERSON.name).startsWith("foo").
				sort(PERSON.name).asc().
				key().asIterator();
		
		while(ite.hasNext()){
			BeanstoreService personService = 
					service.store(PERSON).is(ite.next().value());
			
			List<Account> list = 
					personService.query(ACCOUNT).
					filter(ACCOUNT.email).endsWith("yahoo.co.jp").
					sort(ACCOUNT.address).asc().
					bean().asList();
			
			for (Account account : list){
				account.setEmail("--");
				personService.store(ACCOUNT).put(account);
			}
		}
	}
}