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

public class BeancacheScaner<BT,KT extends Comparable<KT>>{
	private final Map<Key<BT, KT>, BT> delegate;
	private final BeanMeta<BT, KT> beanMeta;
	private final HashSet<Key<BT, KT>> result;
	BeancacheScaner(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> delegate) {
		this.delegate = delegate;
		this.beanMeta = beanMeta;
		this.result = new HashSet<Key<BT,KT>>();
		this.result.addAll(delegate.keySet());
	}
	public BeancacheScannerKey<BT, KT> key(){
		BeancacheScannerKey<BT, KT> keyIndex = new BeancacheScaner.BeancacheScannerKey<BT, KT>(this);
		for (Key<BT,KT> key : delegate.keySet()) keyIndex.index.add(key);
		return keyIndex;
	}
	public <PT extends Comparable<PT>> BeancacheComparableProperty<BT, KT, PT> comparable(PropertyMeta<BT,PT> propertyMeta){
		BeancacheComparableProperty<BT, KT, PT> property = 
				new BeancacheScaner.BeancacheComparableProperty<BT, KT, PT>(this, propertyMeta);
		for (Key<BT,KT> key : delegate.keySet()) property.index().put(key, delegate.get(key));
		return property;
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
	private BeancacheScaner<BT,KT> join(Set<Key<BT, KT>> join){
		Set<Key<BT, KT>> ret = new HashSet<Key<BT,KT>>();
		for (Key<BT, KT> key : this.result)
			if (join.contains(key))
				ret.add(key);
		this.result.clear();
		this.result.addAll(ret);
		return this;
	}
	public static class BeancacheScannerKey<BT,KT extends Comparable<KT>> {
		private final BeancacheKeyTreeIndex<BT,KT> index;
		private final BeancacheScaner<BT,KT> scaner;
		BeancacheScannerKey(BeancacheScaner<BT,KT> scaner) {
			this.index = new BeancacheKeyTreeIndex<BT,KT>(scaner.beanMeta);
			this.scaner = scaner;
		}
		public BeancacheScaner<BT,KT> greaterThan(Key<BT, KT> key){
			return tail(key, false);
		}
		public BeancacheScaner<BT,KT> greaterEqual(Key<BT, KT> key){
			return tail(key, true);
		}
		public BeancacheScaner<BT,KT> lessThan(Key<BT, KT> key){
			return head(key, false);
		}
		public BeancacheScaner<BT,KT> lessEqual(Key<BT, KT> key){
			return head(key, true);
		}
		public BeancacheScaner<BT,KT> tail(Key<BT, KT> from, boolean inclusive){
			return scaner.join(this.index.tailIndex(from, inclusive).navigableSet());
		}
		public BeancacheScaner<BT,KT> head(Key<BT, KT> to, boolean inclusive){
			return scaner.join(this.index.headIndex(to, inclusive).navigableSet());
		}
		public BeancacheScaner<BT,KT> sub(Key<BT, KT> from, boolean fromInclusive, Key<BT, KT> to, boolean toInclusive){
			return scaner.join(
					this.index.subIndex(from, fromInclusive, to, toInclusive).navigableSet());
		}
		public BeancacheScaner<BT,KT> child(Key<?, ?> parent){
			return scaner.join(this.index.child(parent).navigableSet());
		}
	}
	public static class BeancacheComparableProperty<BT,KT extends Comparable<KT>, PT extends Comparable<PT>>{
		private final BeancacheScaner<BT,KT> scaner;
		private final BeancachePropertyTreeIndex<BT, KT, PT> index;
		BeancacheComparableProperty(BeancacheScaner<BT,KT> scaner, PropertyMeta<BT, PT> propertyMeta){
			this.scaner = scaner;
			this.index = new BeancachePropertyTreeIndex<BT, KT, PT>(scaner.beanMeta, propertyMeta);
		}
		public BeancacheScaner<BT,KT> greaterThan(PT p){
			return tail(p, false);
		}
		public BeancacheScaner<BT,KT> greaterEqual(PT p){
			return tail(p, true);
		}
		public BeancacheScaner<BT,KT> lessThan(PT p){
			return head(p, false);
		}
		public BeancacheScaner<BT,KT> lessEqual(PT p){
			return head(p, true);
		}
		public BeancacheScaner<BT,KT> tail(PT from, boolean inclusive){
			return scaner.join(this.index.tailIndex(from, inclusive).keySet());
		}
		public BeancacheScaner<BT,KT> head(PT to, boolean inclusive){
			return scaner.join(this.index.headIndex(to, inclusive).keySet());
		}
		public BeancacheScaner<BT,KT> sub(PT from, boolean fromInclusive, PT to, boolean toInclusive){
			return scaner.join(this.index.subIndex(from, fromInclusive, to, toInclusive).keySet());
		}
		BeancachePropertyTreeIndex<BT, KT, PT> index(){
			return index;
		}
	}
}