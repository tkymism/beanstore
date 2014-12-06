package com.tkym.labs.beanstore;

import java.util.Date;

import com.tkym.labs.beanmeta.Key;

public class BeancacheStats<BT,KT>{
	private final Key<BT, KT> key;
	private final BT bean;
	private Date attachTime = new Date();
	private Date usingTime = new Date();
	BeancacheStats(Key<BT, KT> key, BT bean){
		this.key = key;
		this.bean = bean;
	}
	public Key<BT, KT> getKey() {
		return key;
	}
	public BT use() {
		return bean;
	}
	BT getBean(){
		return bean;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		BeancacheStats<BT,KT> other = (BeancacheStats<BT,KT>) obj;
		if (bean == null) {
			if (other.bean != null)
				return false;
		} else if (!bean.equals(other.bean))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	Date whenAttach(){
		return this.attachTime;
	}
	Date whenUsing(){
		return this.usingTime;
	}
}