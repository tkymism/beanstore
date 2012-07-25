package com.tkym.labs.beanstore.sample.bean;

import com.tkym.labs.beanmeta.ano.Attribute;
import com.tkym.labs.beanmeta.ano.Key;
import com.tkym.labs.beanmeta.ano.Model;

@Model
public class Person{
	@Key
	private long id;
	@Attribute
	private String name;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}