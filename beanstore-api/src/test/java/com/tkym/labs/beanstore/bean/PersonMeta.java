package com.tkym.labs.beanstore.bean;

import com.tkym.labs.beanmeta.AbstractBeanMeta;
import com.tkym.labs.beanmeta.PropertyAccessorResolver;
import com.tkym.labs.beanmeta.PropertyMeta;

public class PersonMeta extends AbstractBeanMeta<Person,Long>{
	private static final PersonMeta singleton = new PersonMeta();
	public final static PersonMeta get(){ return singleton; }
	private PersonMeta(){ super("person", Person.class); }
	private PropertyAccessorResolver<Person, Long> _id_ = new PropertyAccessorResolver<Person, Long>(){
		@Override
		public Long get(Person bean) {
			return bean.getId();
		}
		@Override
		public void set(Person bean, Long value) {
			bean.setId(value);
		}
	};
	public final PropertyMeta<Person,Long> id = property("id", Long.class).accessor(_id_);
	private PropertyAccessorResolver<Person, String> _name_ = new PropertyAccessorResolver<Person, String>(){
		@Override
		public String get(Person bean) {
			return bean.getName();
		}
		@Override
		public void set(Person bean, String value) {
			bean.setName(value);
		}
	};
	public final PropertyMeta<Person,String> name = property("name", String.class).accessor(_name_);
	@Override
	public PropertyMeta<Person, Long> getKeyPropertyMeta() { return id; }
	@Override
	public Person newInstance() {
		return new Person();
	}
}