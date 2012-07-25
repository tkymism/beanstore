package com.tkym.labs.beanstore.sample.bean;

import com.tkym.labs.beanmeta.ano.Arrays;
import com.tkym.labs.beanmeta.ano.Attribute;
import com.tkym.labs.beanmeta.ano.Key;
import com.tkym.labs.beanmeta.ano.Model;
import com.tkym.labs.beanmeta.ano.Parent;

@Model(name="MODEL", parent=Account.class)
@Parent(Account.class)
public class Bill {
	@Key(name = "NO")
	private int no;
	@Attribute(name = "AMOUNT") 
	private float amount;
	@Arrays(length=100)
	private final int[] itemNo = new int[100];
	@Arrays(length=100) 
	private final String[] itemName = new String[100];
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getItemNo(int index){
		return itemNo[index];
	}
	public void setItemNo(int index, int value){
		itemNo[index] = value;
	}
	public String getItemName(int index){
		return itemName[index];
	}
	public void setItemName(int index, String value){
		itemName[index] = value;
	}
}