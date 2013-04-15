package com.tkym.labs.beancache;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import com.tkym.labs.beanmeta.Key;

public class BeancacheIndexMap<P extends Comparable<P>,B,K extends Comparable<K>>{
	private final NavigableMap<P, Set<Key<B,K>>> navigableMap = new TreeMap<P, Set<Key<B,K>>>();
	void put(P p, Key<B,K> key){
		Set<Key<B,K>> set = navigableMap.get(p);
		if (set == null){
			set = new HashSet<Key<B,K>>();
			this.navigableMap.put(p, set);
		} 
		set.add(key);
	}
	Set<Key<B, K>> tail(P from, boolean inclusive) {
		return unionSet(navigableMap.tailMap(from, inclusive));
	}
	Set<Key<B, K>> head(P to, boolean inclusive) {
		return unionSet(navigableMap.headMap(to, inclusive));
	}
	Set<Key<B, K>> sub(P from, boolean fromInclusive, P to, boolean toInclusive) {
		return unionSet(navigableMap.subMap(from, fromInclusive, to, toInclusive));
	}
	Set<Key<B, K>> greaterThan(P p) {
		return unionSet(navigableMap.tailMap(p, false));
	}
	Set<Key<B, K>> greaterEqual(P p) {
		return unionSet(navigableMap.tailMap(p, true));
	}
	Set<Key<B, K>> lessThan(P p) {
		return unionSet(navigableMap.headMap(p, false));
	}
	Set<Key<B, K>> lessEqual(P p) {
		return unionSet(navigableMap.headMap(p, true));
	}
	Set<Key<B, K>> equalsTo(P p) {
		return unionSet(navigableMap.subMap(p, true, p, true));
	}
	Set<Key<B, K>> unionSet(NavigableMap<P, Set<Key<B, K>>> m){
		Set<Key<B, K>> ret = new LinkedHashSet<Key<B, K>>();
		for (Set<Key<B,K>> set : m.values()) ret.addAll(set);
		return ret;
	}
}