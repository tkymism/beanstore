package com.tkym.labs.beanstore;

import java.util.HashMap;
import java.util.Map;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public abstract class AbstractBeanstoreService<PB,PK> implements BeanstoreService<PB,PK>{
	private final AbstractBeanstoreService<Void,Void> rootService;
	private BeanstoreTransaction transaction; 
	protected final Key<PB,PK> parent;
	private Map<BeanMeta<?, ?>, Beanstore<?, ?>> storeMap = new HashMap<BeanMeta<?, ?>, Beanstore<?, ?>>();
	
	@SuppressWarnings("unchecked")
	protected AbstractBeanstoreService(BeanstoreTransaction transaction){
		this.parent = null;
		this.rootService = (AbstractBeanstoreService<Void,Void>)this;
		this.transaction = transaction;
	}
	
	protected AbstractBeanstoreService(AbstractBeanstoreService<Void,Void> rootService, Key<PB,PK> parent){
		this.rootService = rootService;
		this.parent = parent;
		this.transaction = rootService.transaction;
	}
	
	protected AbstractBeanstoreService<Void,Void> getRoot(){
		return rootService;
	}
	
	<BT, KT> boolean isSupport(BeanMeta<BT, KT> meta){
		if(meta.parent() == null && parent == null) return true;
		if(meta.parent() != null && parent != null) 
			return meta.parent().getName().equals(parent.getBeanMeta().getName());
		return false;
	}
	
	String metaName(BeanMeta<?,?> meta){
		if (meta == null) return "meta[null]";
		else return "Parent's Meta["+meta.getName()+"]";
	}
	
	@Override
	public final <BT, KT> Beanstore<BT, KT> store(BeanMeta<BT, KT> meta) {
		if(!isSupport(meta))
			throw new IllegalArgumentException("meta.parent="+metaName(meta.parent())+" is unsuport service="+metaName(parent.getBeanMeta()));
		return getBeanStore(meta);
	}
	
	@SuppressWarnings("unchecked")
	private final <BT, KT> Beanstore<BT, KT> getBeanStore(BeanMeta<BT, KT> meta){
		Beanstore<BT, KT> store = (Beanstore<BT, KT>) storeMap.get(meta);
		if (store == null) {
			store = createBeanstore(meta, parent);
			storeMap.put(meta, store);
		}
		return store;
	}
	
	protected abstract <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(BeanMeta<BT, KT> beanMeta, Key<PB, PK> parent);
	
	@Override
	public BeanstoreTransaction getTransaction() {
		return this.transaction;
	}
}