package com.tkym.labs.beanstore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;

public abstract class AbstractBeanstore<BT, KT> implements Beanstore<BT, KT>{
	protected final AbstractBeanstoreService<?,?> beanstoreServiceRoot;
	protected final BeanMeta<BT, KT> beanMeta;
	protected final Key<?,?> parent;
	private final Map<KT, BeanstoreService<BT, KT>> childServiceMap = 
			new ConcurrentHashMap<KT, BeanstoreService<BT, KT>>();
	
	protected AbstractBeanstore(AbstractBeanstoreService<?,?> rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.beanstoreServiceRoot = rootService;
		this.beanMeta = beanMeta;
		this.parent = parent;
	}
	
	@Override
	public final BeanstoreService<BT, KT> is(KT key) {
		BeanstoreService<BT, KT> service = childServiceMap.get(key);
		if (service == null){
			service = createChildService(this.beanstoreServiceRoot, this.key(key));
			childServiceMap.put(key, service);
		}
		return service;
	}
	
	protected abstract AbstractBeanstoreService<BT, KT> createChildService(AbstractBeanstoreService<?, ?> beanstoreServiceRoot, Key<BT, KT> key);
	
	@Override
	public Key<BT, KT> key(KT key) {
		return beanMeta.key(parent, key);
	}
	
	@Override
	public final BT get(KT key) throws BeanstoreException {
		try {
			return getDelegate(key); 
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract BT getDelegate(KT key) throws Exception;
	
	@Override
	public final void remove(KT key) throws BeanstoreException {
		try {
			removeDelegate(key);
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract void removeDelegate(KT key) throws Exception;
	
	public final void put(BT bean) throws BeanstoreException {
		KT key = beanMeta.getKeyPropertyMeta().access(bean).get();
		try {
			putDelegate(key, bean);
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract void putDelegate(KT key, BT bean) throws Exception;
	
	@Override
	public BeanMeta<BT, KT> getBeanMeta() {
		return beanMeta;
	}
}