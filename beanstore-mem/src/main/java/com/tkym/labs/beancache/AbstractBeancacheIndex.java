package com.tkym.labs.beancache;

import java.util.HashSet;

import com.tkym.labs.beanmeta.Key;

abstract class AbstractBeancacheIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> implements BeancacheIndex<BT,KT,PT>{
	protected final BeancacheQuery<BT,KT> scaner;
	protected final BeancacheIndexMap<PT, BT, KT> index;
	AbstractBeancacheIndex(BeancacheQuery<BT,KT> scaner){
		this.scaner = scaner;
		this.index = new BeancacheIndexMap<PT, BT, KT>();
	}
	@Override
	public BeancacheQuery<BT,KT> tail(PT from, boolean inclusive){
		return scaner.intersect(index.tail(from, inclusive));
	}
	@Override
	public BeancacheQuery<BT,KT> head(PT to, boolean inclusive){
		return scaner.intersect(index.head(to, inclusive));
	}
	@Override
	public BeancacheQuery<BT,KT> sub(PT from, boolean fromInclusive, PT to, boolean toInclusive){
		return scaner.intersect(index.sub(from, fromInclusive, to, toInclusive));
	}
	@Override
	public BeancacheQuery<BT,KT> greaterThan(PT p){
		return scaner.intersect(index.greaterThan(p));
	}
	@Override
	public BeancacheQuery<BT,KT> greaterEqual(PT p){
		return scaner.intersect(index.greaterEqual(p));
	}
	@Override
	public BeancacheQuery<BT,KT> lessThan(PT p){
		return scaner.intersect(index.lessThan(p));
	}
	@Override
	public BeancacheQuery<BT,KT> lessEqual(PT p){
		return scaner.intersect(index.lessEqual(p));
	}
	@Override
	public BeancacheQuery<BT,KT> equalsTo(PT p){
		return scaner.intersect(index.equalsTo(p));
	}
	@Override
	public BeancacheQuery<BT,KT> in(PT... ps){
		HashSet<Key<BT, KT>> union = new HashSet<Key<BT,KT>>();
		for (PT p : ps) union.addAll(index.equalsTo(p));
		return scaner.intersect(union);
	}
	@Override
	public BeancacheQuery<BT,KT> notEqualsTo(PT p){
		return scaner.defferent(index.equalsTo(p));
	}
	@Override
	public BeancacheQuery<BT,KT> notIn(PT... ps){
		HashSet<Key<BT, KT>> union = new HashSet<Key<BT,KT>>();
		for (PT p : ps) union.addAll(index.equalsTo(p));
		return scaner.defferent(union);
	}
}