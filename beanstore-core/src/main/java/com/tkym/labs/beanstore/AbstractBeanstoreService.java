package com.tkym.labs.beanstore;

import java.util.HashMap;
import java.util.Map;

import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;


public abstract class AbstractBeanstoreService implements BeanstoreService{
	private final AbstractBeanstoreService rootService;
	private BeanstoreTransaction transaction; 
	protected final Key<?, ?> parent;
	private Map<BeanMeta<?, ?>, Beanstore<?, ?>> storeMap = new HashMap<BeanMeta<?, ?>, Beanstore<?, ?>>();
	
	protected AbstractBeanstoreService(BeanstoreTransaction transaction){
		this.parent = null;
		this.rootService = this;
		this.transaction = transaction;
	}
	
	protected AbstractBeanstoreService(AbstractBeanstoreService rootService, Key<?, ?> parent){
		this.rootService = rootService;
		this.parent = parent;
		this.transaction = rootService.transaction;
	}
	
	protected AbstractBeanstoreService getRoot(){
		return rootService;
	}
	
	<BT, KT> boolean isSupport(BeanMeta<BT, KT> meta){
		if(meta.parent() == null && parent == null) return true;
		if(meta.parent() != null && parent != null) return meta.parent().getName().equals(parent.getBeanMeta().getName());
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
	
	@Override
	public final BeanstoreService is(Key<?, ?> parentKey) throws BeanstoreException{
		if (this.parent != null && !this.parent.isAncestorOf(parentKey))
			throw new BeanstoreException("parentKey is not Descendant "+parentKey.getBeanMeta().getName());
		return search(parentKey);
	}
	
	final BeanstoreService search(Key<?,?> key){
		if (isSibling(key)) return child(key);
		if (key.getParent() != null){
			BeanstoreService service = search(key.getParent());
			if (service == null)
				throw new IllegalArgumentException("key is not Descendant"+key.getBeanMeta().getName());
			return ((AbstractBeanstoreService)service).child(key);
		}
		throw new IllegalArgumentException("key is not Descendant"+key.getBeanMeta().getName());
	}
	
	final boolean isSibling(Key<?,?> key) {
		if (this.parent == null && key.getParent() == null) return true;
		else if (this.parent != null && key.getParent() != null) return key.getParent().equals(this.parent);
		return false;
	}
	
	final <B,K> BeanstoreService child(Key<B,K> key){
		return this.store(key.getBeanMeta()).is(key.value());
	}
	
	protected abstract <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent);
	
	@Override
	public BeanstoreTransaction getTransaction() {
		return this.transaction;
	}
}