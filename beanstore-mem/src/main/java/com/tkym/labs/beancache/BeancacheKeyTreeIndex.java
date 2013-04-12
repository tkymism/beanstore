package com.tkym.labs.beancache;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beanmeta.KeyComparator;

class BeancacheKeyTreeIndex<BT,KT extends Comparable<KT>>{
	private final NavigableSet<Key<BT, KT>> navigableSet;
	private final BeanMeta<BT, KT> beanMeta;
	private BeancacheKeyTreeIndex(BeanMeta<BT, KT> beanMeta, NavigableSet<Key<BT, KT>> navigableSet) {
		this.beanMeta = beanMeta;
		this.navigableSet = navigableSet;
	}
	BeancacheKeyTreeIndex(BeanMeta<BT, KT> beanMeta) {
		this(beanMeta, new ConcurrentSkipListSet<Key<BT, KT>>(KeyComparator.comparator(beanMeta)));
	}
	BeancacheKeyTreeIndex<BT,KT> copy(NavigableSet<Key<BT, KT>> navigableMap){
		return new BeancacheKeyTreeIndex<BT, KT>(this.beanMeta, navigableMap);
	}
	NavigableSet<Key<BT, KT>> navigableSet(){
		return this.navigableSet;
	}
	BeancacheKeyTreeIndex<BT,KT> tailIndex(Key<BT,KT> from,
			boolean inclusive) {
		return copy(navigableSet.tailSet(from, inclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> headIndex(Key<BT,KT> to,
			boolean inclusive) {
		return copy(navigableSet.headSet(to, inclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> subIndex(Key<BT,KT> from,
			boolean fromInclusive, Key<BT,KT> to, boolean toInclusive) {
		return copy(navigableSet.subSet((Key<BT, KT>) from, fromInclusive, (Key<BT, KT>) to, toInclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> tailKeyIndex(Key<?,?> from, boolean inclusive) {
		return copy(navigableSet.tailSet(chainForTail(from, inclusive), inclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> headKeyIndex(Key<?,?> to, boolean inclusive) {
		return copy(navigableSet.headSet(chainForHead(to, inclusive), inclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> subKeyIndex(Key<?,?> from, boolean fromInclusive, Key<?,?> to, boolean toInclusive) {
		return copy(navigableSet.subSet(
				chainForTail(from, fromInclusive), fromInclusive, 
				chainForHead(to, toInclusive), toInclusive));
	}
	BeancacheKeyTreeIndex<BT,KT> child(Key<?,?> parent) {
		return subKeyIndex(parent, true, parent, true);
	}
	private Key<BT,KT> chainForHead(Key<?,?> parent, boolean inclusive){
		if (inclusive) return chainMaxKeyFor(parent);
		else return chainMinKeyFor(parent);
	}
	private Key<BT,KT> chainForTail(Key<?,?> parent, boolean inclusive){
		if (inclusive) return chainMinKeyFor(parent);
		else return chainMaxKeyFor(parent);
	}
	private Key<BT,KT> chainMinKeyFor(Key<?,?> parent){
		return chainKeyAsMin(this.beanMeta, parent);
	}
	private Key<BT,KT> chainMaxKeyFor(Key<?,?> parent){
		return chainKeyAsMax(this.beanMeta, parent);
	}
	boolean add(Key<BT, KT> key) {
		return navigableSet.add(key);
	}
	boolean remove(Key<BT, KT> key) {
		return navigableSet.remove(key);
	}
	static <BT,KT> Key<BT,KT> chainKeyAsMin(BeanMeta<BT,KT> meta, Key<?,?> key){
		return chainKeyAsBorder(meta, key, false);
	}
	static <BT,KT> Key<BT,KT> chainKeyAsMax(BeanMeta<BT,KT> meta, Key<?,?> key){
		return chainKeyAsBorder(meta, key, true);
	}
	@SuppressWarnings("unchecked")
	static <BT,KT> Key<BT,KT> chainKeyAsBorder(BeanMeta<BT,KT> meta, Key<?,?> parent, boolean isMax){
		if (parent.getBeanMeta().equals(meta))
			return (Key<BT,KT>) parent;
		if (meta.parent() == null)
			throw new IllegalArgumentException("parent is not exists");
		KeyBuilder<?,?> builder = KeyBuilder.parent(chainKeyAsBorder(meta.parent(), parent, isMax));
		if (isMax) return builder.meta(meta).max().build();
		else return builder.meta(meta).min().build();
	}
}