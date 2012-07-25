package com.tkym.labs.beanstore.sample.bean;

import java.io.Serializable;

import com.tkym.labs.beanmeta.ano.Attribute;
import com.tkym.labs.beanmeta.ano.Index;
import com.tkym.labs.beanmeta.ano.Key;
import com.tkym.labs.beanmeta.ano.Model;


@Model(name="ACCOUNT",parent=Person.class)
@SuppressWarnings("serial")
public class Account implements Serializable{
	@Key(name = "EMAIL")
	private String email;
	@Index
	@Attribute(name = "ADDRESS")
	private String address;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress(){
		return this.address;
	}
	public void setAddress(String address){
		this.address = address;
	}
}