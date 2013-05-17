package com.tkym.labs.beancache;

import java.util.Set;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

abstract class AbstractBeancacheFilter<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> implements BeancacheFilter<BT,KT,PT>{
	protected final BeancacheFilterMap<PT, BT, KT> index;
	AbstractBeancacheFilter(BeanMeta<BT, KT> beanMeta){
		this.index = new BeancacheFilterMap<PT, BT, KT>(beanMeta);
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
	public Set<Key<BT, KT>> match(BeancacheFilterMatcher<PT> matcher) {
		return index.match(matcher);
	}
	@Override
	public BeancacheFilter<BT, KT, PT> asc() {
		index.asc();
		return this;
	}
	@Override
	public BeancacheFilter<BT, KT, PT> desc() {
		index.desc();
		return this;
	}
	@Override
	public Set<Key<BT, KT>> all() {
		return index.all();
	}
}