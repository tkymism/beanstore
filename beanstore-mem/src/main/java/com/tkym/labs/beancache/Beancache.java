package com.tkym.labs.beancache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public class Beancache<BT,KT extends Comparable<KT>> implements Map<Key<BT, KT>, BT>{
	private final BeanMeta<BT, KT> beanMeta;
	private final Map<Key<BT, KT>, BT> delegate;
	Beancache(BeanMeta<BT, KT> beanMeta){
		this(beanMeta, new ConcurrentHashMap<Key<BT, KT>, BT>());
	}
	Beancache(BeanMeta<BT, KT> beanMeta, int initialCapacity){
		this(beanMeta, new ConcurrentHashMap<Key<BT, KT>, BT>(initialCapacity));
	}
	Beancache(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> delegate){
		this.beanMeta = beanMeta;
		this.delegate = delegate;
	}
	public BeancacheScaner<BT,KT> scan(){
		return new BeancacheScaner<BT,KT>(beanMeta, delegate);
	}
	@Override
	public int size() {
		return delegate.size();
	}
	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}
	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}
	public BT get(Key<BT, KT> key) {
		return delegate.get(key);
	}
	@Override
	public BT get(Object key) {
		return delegate.get(key);
	}
	@Override
	public BT put(Key<BT, KT> key, BT value) {
		return delegate.put(key, value);
	}
	public BT remove(Key<BT, KT> key) {
		return delegate.remove(key);
	}
	@Override
	public BT remove(Object key) {
		return delegate.remove(key);
	}
	@Override
	public void putAll(Map<? extends Key<BT, KT>, ? extends BT> m) {
		delegate.putAll(m);
	}
	@Override
	public void clear() {
		delegate.clear();
	}
	@Override
	public Set<Key<BT, KT>> keySet() {
		return delegate.keySet();
	}
	@Override
	public Collection<BT> values() {
		return delegate.values();
	}
	@Override
	public Set<java.util.Map.Entry<Key<BT, KT>, BT>> entrySet() {
		return delegate.entrySet();
	}
}