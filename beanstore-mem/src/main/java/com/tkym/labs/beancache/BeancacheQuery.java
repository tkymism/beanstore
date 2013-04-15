package com.tkym.labs.beancache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancacheQuery<BT,KT extends Comparable<KT>>{
	private final Map<Key<BT, KT>, BT> delegate;
	final BeanMeta<BT, KT> beanMeta;
	private HashSet<Key<BT, KT>> result;
	BeancacheQuery(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> delegate) {
		this.delegate = delegate;
		this.beanMeta = beanMeta;
		this.result = new HashSet<Key<BT,KT>>(delegate.keySet());
	}
	public BeancacheIndex<BT,KT,KT> key(){
		return key(this.beanMeta);
	}
	public <PKT extends Comparable<PKT>, PBT> BeancacheIndex<BT,KT,PKT> key(BeanMeta<PBT, PKT> parent){
		BeancacheKeyIndex<BT, KT, PBT, PKT> idx = 
				new BeancacheKeyIndex<BT, KT, PBT, PKT>(this, parent);
		for (Key<BT,KT> key : this.result) idx.add(key);
		return idx;
	}
	public <PT extends Comparable<PT>> BeancacheIndex<BT,KT,PT> property(PropertyMeta<BT,PT> propertyMeta){
		BeancachePropertyIndex<BT, KT, PT> idx = 
				new BeancachePropertyIndex<BT, KT, PT>(this, propertyMeta);
		for (Key<BT,KT> key : this.result) idx.put(key, delegate.get(key));
		return idx;
	}
	public Set<Key<BT, KT>> keySet(){
		return result;
	}
	public Iterator<BT> asIteratorBean(){
		final Iterator<Key<BT, KT>> keyIterator = result.iterator();
		return new Iterator<BT>() {
			@Override
			public boolean hasNext() {
				return keyIterator.hasNext();
			}
			@Override
			public BT next() {
				return delegate.get(keyIterator.hasNext());
			}
			@Override
			public void remove() {
				keyIterator.remove();
			}
		};
	}
	public List<BT> asListBean(){
		List<BT> list = new ArrayList<BT>();
		for (Key<BT, KT> key : result)
			list.add(delegate.get(key));
		return list;
	}
	BeancacheQuery<BT,KT> intersect(Set<Key<BT, KT>> filtered){
		this.result.retainAll(filtered);
		return this;
	}
	BeancacheQuery<BT,KT> defferent(Set<Key<BT, KT>> filtered){
		this.result.removeAll(filtered);
		return this;
	}
}