package com.tkym.labs.beanstore.record;


import com.tkym.labs.beanstore.AbstractBeanstore;
import com.tkym.labs.beanstore.AbstractBeanstoreService;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.record.Record;
import com.tkym.labs.record.RecordKey;
import com.tkym.labs.record.RecordKey.RecordKeyBuilder;
import com.tkym.labs.record.RecordstoreService;

class BeanstoreRecord<BT, KT> extends AbstractBeanstore<BT, KT>{
	private final RecordstoreService recordstoreService;
	private final RecordKeyBuilder keyBuilder;
	private final BeanMetaResolver<BT,KT> beanMetaResolver;

	BeanstoreRecord(AbstractBeanstoreService<?,?> root, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent){
		super(root, beanMeta, parent);
		this.recordstoreService = ((BeanstoreServiceRecord<?,?>) root).getRecordstoreService();
		this.beanMetaResolver = BeanMetaResolverProvider.getInstance().get(beanMeta);
		this.keyBuilder = this.beanMetaResolver.createRecordKeyBuilder(parent);
	}
	
	@Override
	protected AbstractBeanstoreService<BT, KT> createChildService(AbstractBeanstoreService<?,?> root, Key<BT, KT> key) {
		return new BeanstoreServiceRecord<BT, KT>(root, key);
	}

	@Override
	protected BT getDelegate(KT key) throws Exception{
		BT bean = beanMeta.newInstance();
		PropertyMeta<BT, KT> keyMeta = beanMeta.getKeyPropertyMeta();
		keyMeta.access(bean).set(key);
		RecordKey recordkey = this.keyBuilder.build(key);
		Record record = recordstoreService.get(recordkey);
		for(String columnName : beanMeta.getPropertyNames())
			if(!keyMeta.getPropertyName().equals(columnName))
				beanMeta.getPropertyMeta(columnName).access(bean).set(record.get(columnName));
		return bean;
	}
	
	@Override
	protected void removeDelegate(KT key) throws Exception {
		recordstoreService.deleteIfExists(this.keyBuilder.build(key));
	};
	
	@Override
	protected void putDelegate(KT keyValue, BT bean) throws Exception {
		RecordKey recordKey = this.keyBuilder.build(keyValue);
		Record record = new Record(recordKey);
		for(String propertyName : beanMeta.getPropertyNames())
			if(!beanMeta.getKeyPropertyMeta().getPropertyName().equals(propertyName))
				record.put(propertyName, beanMeta.getPropertyMeta(propertyName).access(bean).get());
		recordstoreService.put(record);
	};
}