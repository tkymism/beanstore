package com.tkym.labs.beancache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
	BeancacheQuery(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> delegate, HashSet<Key<BT,KT>> keyset) {
		this.delegate = delegate;
		this.beanMeta = beanMeta;
		this.result = keyset;
	}
	BeancacheQuery(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> delegate) {
		this(beanMeta, delegate, new HashSet<Key<BT,KT>>(delegate.keySet()));
	}
	public BeancacheIndex<BT,KT,KT> key(){
		return key(this.beanMeta);
	}
	public <PKT extends Comparable<PKT>, PBT> BeancacheIndex<BT,KT,PKT> key(BeanMeta<PBT, PKT> parent){
		BeancacheKeyIndex<BT, KT, PBT, PKT> idx = 
				new BeancacheKeyIndex<BT, KT, PBT, PKT>(this.beanMeta, parent);
		for (Key<BT,KT> key : this.result) idx.add(key);
		return idx;
	}
	public <PT extends Comparable<PT>> BeancacheIndex<BT,KT,PT> property(PropertyMeta<BT,PT> propertyMeta){
		BeancachePropertyIndex<BT, KT, PT> idx = 
				new BeancachePropertyIndex<BT, KT, PT>(propertyMeta);
		for (Key<BT,KT> key : this.result) idx.put(key, delegate.get(key));
		return idx;
	}
	public Set<Key<BT, KT>> asKeySet(){
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
				return delegate.get(keyIterator.next());
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
	public static <B,K extends Comparable<K>> HashSet<Key<B, K>> createHashSetForUnion(Set<Key<B, K>>... sets){
		HashSet<Key<B, K>> ret = new HashSet<Key<B,K>>();
		for (Set<Key<B, K>> set : sets) union(ret, set);
		return ret;
	}
	public static <B,K extends Comparable<K>> HashSet<Key<B, K>> createHashSetForIntersect(Set<Key<B, K>>... sets){
		HashSet<Key<B, K>> ret = new HashSet<Key<B,K>>();
		for (Set<Key<B, K>> set : sets) intersect(ret, set);
		return ret;
	}
	public static <B,K extends Comparable<K>> LinkedHashSet<Key<B, K>> createLinkedHashSetForUnion(Set<Key<B, K>>... sets){
		LinkedHashSet<Key<B, K>> ret = new LinkedHashSet<Key<B,K>>();
		for (Set<Key<B, K>> set : sets) union(ret, set);
		return ret;
	}
	public static <BT,KT extends Comparable<KT>> LinkedHashSet<Key<BT, KT>> createLinkedHashSetForIntersect(Set<Key<BT, KT>>... sets){
		LinkedHashSet<Key<BT, KT>> ret = new LinkedHashSet<Key<BT,KT>>();
		for (Set<Key<BT, KT>> set : sets) intersect(ret, set);
		return ret;
	}
	public static <BT,KT extends Comparable<KT>> void union(Set<Key<BT, KT>> source, Set<Key<BT, KT>> target){
		source.addAll(target);
	}
	public static <BT,KT extends Comparable<KT>> void intersect(Set<Key<BT, KT>> source, Set<Key<BT, KT>> target){
		source.retainAll(target);
	}
	public static <BT,KT extends Comparable<KT>> void defferent(Set<Key<BT, KT>> source, Set<Key<BT, KT>> target){
		source.removeAll(target);
	}
	public BeancacheQuery<BT,KT> union(Set<Key<BT, KT>> filtered){
		union(this.result, filtered);
		return this;
	}
	public BeancacheQuery<BT,KT> intersect(Set<Key<BT, KT>> filtered){
		intersect(this.result, filtered);
		return this;
	}
	public BeancacheQuery<BT,KT> defferent(Set<Key<BT, KT>> filtered){
		defferent(this.result, filtered);
		return this;
	}
}