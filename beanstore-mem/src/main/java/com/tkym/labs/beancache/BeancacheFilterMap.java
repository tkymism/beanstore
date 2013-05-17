package com.tkym.labs.beancache;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.tkym.labs.beancache.NullComparableValue.NullComparableValueComparator;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyComparator;

class BeancacheFilterMap<P extends Comparable<P>,B,K extends Comparable<K>>{
	static int IN_NO_PARTICULAR_ORDER = 0;
	static int ASCENDING = 1;
	static int DESCENDING = -1;
	private int order = IN_NO_PARTICULAR_ORDER;
	private NullComparableValueComparator<P> comparator;
	private final NavigableMap<NullComparableValue<P>, Set<Key<B,K>>> navigableMap;
	private final Comparator<Key<B, K>> keyComparator;
	BeancacheFilterMap(BeanMeta<B, K> meta){
		this(meta, false);
	}
	BeancacheFilterMap(BeanMeta<B, K> meta, boolean isNullFirst){
		this.keyComparator =	KeyComparator.comparator(meta);
		this.comparator = new NullComparableValueComparator<P>();
		if (isNullFirst) this.comparator.nullFirst();
		this.navigableMap = new TreeMap<NullComparableValue<P>, Set<Key<B,K>>>(comparator);
	}
	void put(P p, Key<B,K> key){
		Set<Key<B,K>> set = navigableMap.get(wrap(p));
		if (set == null){
			set = createHashSet(key);
			this.navigableMap.put(wrap(p), set);
		} 
		set.add(key);
	}
	private Set<Key<B,K>> createHashSet(Key<B,K> key){
		return new HashSet<Key<B,K>>();
	}
	Set<Key<B, K>> tail(P from, boolean inclusive) {
		return createKeySetFor(navigableMap.tailMap(wrap(from), inclusive));
	}
	Set<Key<B, K>> head(P to, boolean inclusive) {
		return createKeySetFor(navigableMap.headMap(wrap(to), inclusive));
	}
	Set<Key<B, K>> sub(P from, boolean fromInclusive, P to, boolean toInclusive) {
		return createKeySetFor(navigableMap.subMap(wrap(from), fromInclusive, wrap(to), toInclusive));
	}
	Set<Key<B, K>> greaterThan(P p) {
		return createKeySetFor(navigableMap.tailMap(wrap(p), false));
	}
	Set<Key<B, K>> greaterEqual(P p) {
		return createKeySetFor(navigableMap.tailMap(wrap(p), true));
	}
	Set<Key<B, K>> lessThan(P p) {
		return createKeySetFor(navigableMap.headMap(wrap(p), false));
	}
	Set<Key<B, K>> lessEqual(P p) {
		return createKeySetFor(navigableMap.headMap(wrap(p), true));
	}
	Set<Key<B, K>> equalsTo(P p) {
		return createKeySetFor(navigableMap.subMap(wrap(p), true, wrap(p), true));
	}
	Set<Key<B, K>> match(BeancacheFilterMatcher<P> matcher) {
		Set<Key<B, K>> ret = new LinkedHashSet<Key<B, K>>();
		for (NullComparableValue<P> p : navigableMap.keySet())
			if (matcher.match(p.value))
				ret.addAll(navigableMap.get(p));
		return ret;
	}
	Set<Key<B, K>> all() {
		return createKeySetFor(this.navigableMap);
	}
	BeancacheFilterMap<P,B,K> asc(){
		this.order = ASCENDING;
		return this;
	}
	BeancacheFilterMap<P,B,K> desc(){
		this.order = DESCENDING;
		return this;
	}
	private Set<Key<B, K>> createKeySetFor(NavigableMap<NullComparableValue<P>, Set<Key<B, K>>> m){
		Set<Key<B, K>> ret = new LinkedHashSet<Key<B, K>>();
		Collection<Set<Key<B,K>>> entry = null;
		if (order == DESCENDING) entry = m.descendingMap().values();
		else entry = m.values();
		for (Set<Key<B,K>> set : entry) ret.addAll(sort(set));
		return ret;
	}
	private Set<Key<B, K>> sort(Set<Key<B, K>> source){
		if (order == IN_NO_PARTICULAR_ORDER) return source;
		else if (order == ASCENDING) return buildTreeSet(source);
		else return buildTreeSet(source).descendingSet();
	}
	private TreeSet<Key<B,K>> buildTreeSet(Set<Key<B, K>> source){
		TreeSet<Key<B,K>> treeset = new TreeSet<Key<B,K>>(this.keyComparator);
		treeset.addAll(source);
		return treeset;
	}
	private static <T extends Comparable<T>> NullComparableValue<T> wrap(T value){
		return new NullComparableValue<T>(value);
	}
}