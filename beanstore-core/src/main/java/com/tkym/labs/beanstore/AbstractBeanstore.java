package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.api.BeanstoreService;

public abstract class AbstractBeanstore<BT, KT extends Comparable<KT>> implements Beanstore<BT, KT>{
	protected final AbstractBeanstoreRootService beanstoreServiceRoot;
	protected final BeanMeta<BT, KT> beanMeta;
	protected final Key<?,?> parent;
	protected AbstractBeanstore(AbstractBeanstoreRootService rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
		this.beanstoreServiceRoot = rootService;
		this.beanMeta = beanMeta;
		this.parent = parent;
	}
	
	@Override
	public final BeanstoreService<BT, KT> is(KT key) {
		return new DefaultBeanstoreService<BT, KT>(this.beanstoreServiceRoot, this.key(key));
	}
	
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