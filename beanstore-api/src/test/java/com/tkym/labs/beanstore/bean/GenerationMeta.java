package com.tkym.labs.beanstore.bean;

import java.util.HashMap;
import java.util.Map;

import com.tkym.labs.beanmeta.AbstractBeanMeta;
import com.tkym.labs.beanmeta.PropertyAccessorResolver;
import com.tkym.labs.beanmeta.PropertyMeta;


public class GenerationMeta extends AbstractBeanMeta<Generation, Integer>{
	private final static Map<String, GenerationMeta> instanceMap = new HashMap<String, GenerationMeta>();
	public static void register(String... keys){
		for (String key : keys)
			if (!instanceMap.containsKey(key))
				instanceMap.put(key, new GenerationMeta(key));
	}
	
	public static GenerationMeta get(String key){
		return instanceMap.get(key);
	}
	
	private GenerationMeta(String suffix) {
		super("Generation"+suffix, Generation.class);
	}
	
	private PropertyAccessorResolver<Generation, Integer> _id_ = new PropertyAccessorResolver<Generation, Integer>(){
		@Override
		public Integer get(Generation bean) {
			return bean.getId();
		}
		@Override
		public void set(Generation bean, Integer value) {
			bean.setId(value);
		}
	};
	
	public final PropertyMeta<Generation, Integer> id = property("id", Integer.class).accessor(_id_);
	
	private PropertyAccessorResolver<Generation, String> _name_ = new PropertyAccessorResolver<Generation, String>(){
		@Override
		public String get(Generation bean) {
			return bean.getName();
		}
		@Override
		public void set(Generation bean, String value) {
			bean.setName(value);
		}
	};
	public final PropertyMeta<Generation,String> name = property("name", String.class).accessor(_name_);
	
	@Override
	public PropertyMeta<Generation, Integer> getKeyPropertyMeta() {
		return id;
	}

	@Override
	public Generation newInstance() {
		return new Generation();
	}
}
