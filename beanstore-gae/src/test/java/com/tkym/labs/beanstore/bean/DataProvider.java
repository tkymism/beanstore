package com.tkym.labs.beanstore.bean;

public class DataProvider {
	public static Person create(long id){
		Person p = new Person();
		p.setId(id);
		p.setName("hoge+"+id);
		return p;
	}
	
	public static Account create(long id, int index){
		Account account = new Account();
		account.setEmail("hoge"+id+"@email"+index+".com");
		account.setAddress("hogehoge"+index);
		return account; 
	}
	
	public static Bill create(long id, int index, int billNo){
		Bill bill = new Bill();
		bill.setNo(billNo);
		bill.setAmount((float)((id*10)+(index)*0.1));
		return bill;
	}
}
