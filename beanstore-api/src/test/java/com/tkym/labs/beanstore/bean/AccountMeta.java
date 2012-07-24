package com.tkym.labs.beanstore.bean;

import com.tkym.labs.beanmeta.AbstractBeanMeta;
import com.tkym.labs.beanmeta.PropertyAccessorResolver;
import com.tkym.labs.beanmeta.PropertyMeta;

/**
 * auto generated java source
 */
public class AccountMeta extends AbstractBeanMeta<Account, String>{
	private static final AccountMeta singleton = new AccountMeta();
	
	public static AccountMeta get(){ return singleton; }
	
	private AccountMeta(){ super("ACCOUNT", Account.class); }
	
			
	private PropertyAccessorResolver<Account, String> _email_ = new PropertyAccessorResolver<Account, String>(){
		@Override public String get(Account bean) { return bean.getEmail(); }
		@Override public void set(Account bean, String value) { bean.setEmail(value); }
	};
	public final PropertyMeta<Account, String> email = property("EMAIL",String.class).accessor(_email_);
			
	private PropertyAccessorResolver<Account, String> _address_ = new PropertyAccessorResolver<Account, String>(){
		@Override public String get(Account bean) { return bean.getAddress(); }
		@Override public void set(Account bean, String value) { bean.setAddress(value); }
	};
	public final PropertyMeta<Account, String> address = property("ADDRESS", String.class).accessor(_address_);
		
	@Override public PropertyMeta<Account, String> getKeyPropertyMeta() { return email; }
	
	@Override public Account newInstance() { return new Account(); }

	@SuppressWarnings("unchecked")
	@Override public PersonMeta parent() { return PersonMeta.get(); }
	
}
