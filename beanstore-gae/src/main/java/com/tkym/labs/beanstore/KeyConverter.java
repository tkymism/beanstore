package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

class KeyConverter<B,K>{
	private final Key parent;
	private final BeanMeta<B, K> beanMeta;
	private final Class<K> keyType;
	KeyConverter(Key parent, BeanMeta<B, K> beanMeta) {
		this.parent = parent;
		this.beanMeta = beanMeta;
		this.keyType = beanMeta.getKeyPropertyMeta().getPropertyType();
	}
	
	KeyConverter(BeanMeta<B, K> beanMeta) {
		this(null, beanMeta);
	}
	
	B convert(Entity entity){
		if (entity == null)
			return null;
		B bean = beanMeta.newInstance();
		beanMeta.getKeyPropertyMeta().access(bean).set(convert(entity.getKey()).value());
		for (String propertyName : beanMeta.getPropertyNames()){
			Object value = entity.getProperty(propertyName);
			if (value != null)
				beanMeta.getPropertyMeta(propertyName).access(bean).set(value);
		}
		return bean;
	}
	
	com.tkym.labs.beanmeta.Key<B, K> convert(Key key){
		com.tkym.labs.beanmeta.Key<?, ?> parent = null;
		if (key.getParent() != null)
			parent = KeyConverterFactory.create(beanMeta.parent()).convert(key.getParent());
		return create(parent, key);
	}
	
	private com.tkym.labs.beanmeta.Key<B, K> create(com.tkym.labs.beanmeta.Key<?, ?> parent, Key key){
		if (keyType.equals(String.class))
			return create(parent, key.getName());
		else
			return create(parent, key.getId());
	}
	
	@SuppressWarnings("unchecked")
	private com.tkym.labs.beanmeta.Key<B, K> create(com.tkym.labs.beanmeta.Key<?, ?> parent, long id){
		Long asLong = new Long(id);
		K value = null;
		if (this.keyType.equals(Integer.class)) 
			value = (K) new Integer(asLong.intValue());
		else if (this.keyType.equals(Long.class)) 
			value = (K) asLong;
		else if (this.keyType.equals(Short.class)) 
			value = (K) new Short(asLong.shortValue());
		else if (this.keyType.equals(Byte.class)) 
			value = (K) new Byte(asLong.byteValue());
		return beanMeta.key(parent, value);
	}
	
	@SuppressWarnings("unchecked")
	private com.tkym.labs.beanmeta.Key<B, K> create(com.tkym.labs.beanmeta.Key<?, ?> parent, String name){
		return beanMeta.key(parent, (K)name);
	}
	
	Key convert(K value){
		if (keyType.equals(String.class))
			return KeyFactory.createKey(parent, beanMeta.getName(), (String)value);
		else if (keyType.equals(Integer.class)) 
			return KeyFactory.createKey(parent, beanMeta.getName(), ((Integer)value).longValue());
		else if (keyType.equals(Long.class)) 
			return KeyFactory.createKey(parent, beanMeta.getName(), ((Long)value).longValue());
		else if (keyType.equals(Short.class)) 
			return KeyFactory.createKey(parent, beanMeta.getName(), ((Short)value).longValue());
		else if (keyType.equals(Byte.class)) 
			return KeyFactory.createKey(parent, beanMeta.getName(), ((Byte)value).longValue());
		else 
			throw new IllegalArgumentException("key is illegal type [" + keyType.getName() + "]");
	}
}