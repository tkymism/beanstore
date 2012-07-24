package com.tkym.labs.beanstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreEvent;
import com.tkym.labs.beanstore.api.BeanstoreEventListener;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreEvent.BeanstoreEventType;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public abstract class AbstractBeanstore<BT, KT> implements Beanstore<BT, KT>{
	protected final AbstractBeanstoreService beanstoreServiceRoot;
	protected final BeanMeta<BT, KT> beanMeta;
	protected final Key<?,?> parent;
	private final BeanstoreChangeFire<BT,KT> eventFire;
	private final Map<KT, BeanstoreService> childServiceMap = new ConcurrentHashMap<KT, BeanstoreService>();
	
	protected AbstractBeanstore(AbstractBeanstoreService rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.beanstoreServiceRoot = rootService;
		this.beanMeta = beanMeta;
		this.parent = parent;
		this.eventFire = new BeanstoreChangeFire<BT, KT>();
	}
	
	@Override
	public final BeanstoreService is(KT key) {
		BeanstoreService service = childServiceMap.get(key);
		if (service == null){
			service = createChildService(this.beanstoreServiceRoot, this.key(key));
			childServiceMap.put(key, service);
		}
		return service;
	}
	
	protected abstract AbstractBeanstoreService createChildService(AbstractBeanstoreService beanstoreServiceRoot, Key<BT, KT> key);
	
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
			BT old = null;
			if (eventFire.hasListener()) 
				old = get(key);
			
			removeDelegate(key);
			
			if (eventFire.hasListener())
				eventFire.delete(this.key(key), old);
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract void removeDelegate(KT key) throws Exception;
	
	public final void put(BT bean) throws BeanstoreException {
		KT key = beanMeta.getKeyPropertyMeta().access(bean).get();
		try {
			BT old = null;
			if (eventFire.hasListener())
				old = getDelegate(key);
			
			putDelegate(key, bean);
			
			if (eventFire.hasListener()){
				if (old == null) 
					eventFire.insert(key(key), bean);
				else 
					eventFire.update(key(key), bean, old);
			}
		} catch (Exception e) {
			throw new BeanstoreException(e);
		}
	}
	
	protected abstract void putDelegate(KT key, BT bean) throws Exception;
	
	@Override
	public void addBeanstoreChangeListener(BeanstoreEventListener<BT, KT> listener) {
		eventFire.addListener(listener);
	}

	@Override
	public void removeBeanstoreChangeListener(BeanstoreEventListener<BT, KT> listener) {
		eventFire.removeListener(listener);
	}

	@Override
	public BeanMeta<BT, KT> getBeanMeta() {
		return beanMeta;
	}
	
	private class BeanstoreChangeFire<B, K>{
		private final List<BeanstoreEventListener<B,K>> 
			listeners = new ArrayList<BeanstoreEventListener<B,K>>();
		
		private void addListener(BeanstoreEventListener<B,K> listener){
			listeners.add(listener);
		}
		private void removeListener(BeanstoreEventListener<B,K> listener){
			listeners.remove(listener);
		}
		private boolean hasListener(){
			if (listeners.size() > 0) return true;
			else return false;
		}
		private void insert(Key<B,K> key, B newBean){
			notifyToListener(new BeanstoreEvent<B,K>(BeanstoreEventType.INSERT, key, null, newBean));
		}
		private void update(Key<B,K> key, B newBean, B oldBean){
			notifyToListener(new BeanstoreEvent<B,K>(BeanstoreEventType.UPDATE, key, oldBean, newBean));
		}
		private void delete(Key<B,K> key, B oldBean){
			notifyToListener(new BeanstoreEvent<B,K>(BeanstoreEventType.DELETE, key, oldBean, null));
		}
		private void notifyToListener(BeanstoreEvent<B,K> event){
			for (BeanstoreEventListener<B,K> l : listeners)  l.onChange(event);
		}
	}
}