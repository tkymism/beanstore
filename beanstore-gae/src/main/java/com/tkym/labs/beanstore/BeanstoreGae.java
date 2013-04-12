package com.tkym.labs.beanstore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

class BeanstoreGae<BT,KT extends Comparable<KT>> extends AbstractBeanstore<BT,KT> {
	private final DatastoreService datastoreService;
	private final KeyConverter<BT,KT> converter;
	protected BeanstoreGae(AbstractBeanstoreRootService rootService, BeanMeta<BT,KT> beanMeta, Key<?, ?> parent, DatastoreService datastoreService) {
		super(rootService, beanMeta, parent);
		this.datastoreService = datastoreService;
		this.converter = KeyConverterFactory.create(parent, beanMeta);
	}

	@Override
	protected BT getDelegate(KT key) throws Exception {
		return converter.convert(datastoreService.get(converter.convert(key)));
	}

	@Override
	protected void removeDelegate(KT key) throws Exception {
		datastoreService.delete(converter.convert(key));
	}

	@Override
	protected void putDelegate(KT key, BT bean) throws Exception {
		Entity entity = new Entity(converter.convert(key));
		for (String propertyName : beanMeta.getPropertyNames()){
			if(beanMeta.getKeyPropertyMeta().getPropertyName().equals(propertyName)) continue;
			PropertyMeta<BT, ?> meta = beanMeta.getPropertyMeta(propertyName);
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