package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

class BeanstoreGae<T,K> extends AbstractBeanstore<T,K> {
	private final DatastoreService datastoreService;
	private final KeyConverter<T,K> converter;
	protected BeanstoreGae(AbstractBeanstoreService rootService, BeanMeta<T, K> beanMeta, Key<?, ?> parent, DatastoreService datastoreService) {
		super(rootService, beanMeta, parent);
		this.datastoreService = datastoreService;
		this.converter = KeyConverterFactory.create(parent, beanMeta);
	}

	@Override
	protected AbstractBeanstoreService createChildService(AbstractBeanstoreService beanstoreServiceRoot, Key<T, K> key) {
		return new BeanstoreServiceGae(beanstoreServiceRoot, key);
	}

	@Override
	protected T getDelegate(K key) throws Exception {
		return converter.convert(datastoreService.get(converter.convert(key)));
	}

	@Override
	protected void removeDelegate(K key) throws Exception {
		datastoreService.delete(converter.convert(key));
	}

	@Override
	protected void putDelegate(K key, T bean) throws Exception {
		Entity entity = new Entity(converter.convert(key));
		for (String propertyName : beanMeta.getPropertyNames()){
			if(beanMeta.getKeyPropertyMeta().getPropertyName().equals(propertyName)) continue;
			PropertyMeta<T, ?> meta = beanMeta.getPropertyMeta(propertyName);
			if (meta.isIndexed())
				entity.setProperty(propertyName, meta.access(bean).get());
			else
				entity.setUnindexedProperty(propertyName, meta.access(bean).get());
		}
		@SuppressWarnings("unused")
		com.google.appengine.api.datastore.Key existed =
			datastoreService.put(entity);
		
		System.out.println(entity);
	}
}