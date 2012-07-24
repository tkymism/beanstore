package com.tkym.labs.beanstore.bean;

import com.tkym.labs.beanmeta.AbstractBeanMeta;
import com.tkym.labs.beanmeta.IndexedAccessorResolver;
import com.tkym.labs.beanmeta.PropertyAccessorResolver;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BillMeta extends AbstractBeanMeta<Bill, Integer>{
	private PropertyAccessorResolver<Bill, Integer> _no_ = new PropertyAccessorResolver<Bill, Integer>(){
		@Override
		public Integer get(Bill bean) {
			return bean.getNo();
		}
		@Override
		public void set(Bill bean, Integer value) {
			bean.setNo(value);
		}
	};
	public final PropertyMeta<Bill,Integer> no = property("no", Integer.class).accessor(_no_);
	private PropertyAccessorResolver<Bill, Float> _amount_ = new PropertyAccessorResolver<Bill, Float>(){
		@Override
		public Float get(Bill bean) {
			return bean.getAmount();
		}
		@Override
		public void set(Bill bean, Float value) {
			bean.setAmount(value);
		}
	};
	public final PropertyMeta <Bill,Float> amount = property("amount", Float.class).accessor(_amount_);
	
	private IndexedAccessorResolver<Bill, String> _item_ = new IndexedAccessorResolver<Bill, String>() {
		@Override
		public void set(Bill bean, int index, String value) {
			bean.setItem(index, value);
		}
		@Override
		public String get(Bill bean, int index) {
			return bean.getItem(index);
		}
	};
	public final PropertyMeta<Bill,String>[] item = arrays("item", String.class).start(1).length(20).accessor(_item_);
	
	private static final BillMeta singleton = new BillMeta();
	public final static BillMeta get(){ return singleton; }
	private BillMeta() { super("bill", Bill.class); }
	@Override
	public PropertyMeta<Bill, Integer> getKeyPropertyMeta() {
		return this.no;
	}
	@SuppressWarnings("unchecked")
	@Override
	public AccountMeta parent() { return AccountMeta.get(); }
	@Override
	public Bill newInstance() {
		return new Bill();
	}
}
