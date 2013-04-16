package com.tkym.labs.beancache;

import java.util.Set;

import com.tkym.labs.beanmeta.Key;

abstract class AbstractBeancacheIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> implements BeancacheIndex<BT,KT,PT>{
	protected final BeancacheIndexMap<PT, BT, KT> index;
	AbstractBeancacheIndex(){
		this.index = new BeancacheIndexMap<PT, BT, KT>();
	}
	@Override
	public Set<Key<BT,KT>> tail(PT from, boolean inclusive){
		return index.tail(from, inclusive);
	}
	@Override
	public Set<Key<BT,KT>> head(PT to, boolean inclusive){
		return index.head(to, inclusive);
	}
	@Override
	public Set<Key<BT,KT>> sub(PT from, boolean fromInclusive, PT to, boolean toInclusive){
		return index.sub(from, fromInclusive, to, toInclusive);
	}
	@Override
	public Set<Key<BT,KT>> greaterThan(PT p){
		return index.greaterThan(p);
	}
	@Override
	public Set<Key<BT,KT>> greaterEqual(PT p){
		return index.greaterEqual(p);
	}
	@Override
	public Set<Key<BT,KT>> lessThan(PT p){
		return index.lessThan(p);
	}
	@Override
	public Set<Key<BT,KT>> lessEqual(PT p){
		return index.lessEqual(p);
	}
	@Override
	public Set<Key<BT,KT>> equalsTo(PT p){
		return index.equalsTo(p);
	}
	@Override
	public Set<Key<BT, KT>> match(BeancacheIndexMatcher<PT> matcher) {
		return index.match(matcher);
	}
}