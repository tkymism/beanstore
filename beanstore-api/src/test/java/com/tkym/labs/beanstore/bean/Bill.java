package com.tkym.labs.beanstore.bean;


public class Bill {
	private int no;
	private float amount;
	private final String[] item = new String[20];

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
	public void setItem(int index, String value){
		this.item[index] = value;
	}
	public String getItem(int index){
		return this.item[index];
	}
}