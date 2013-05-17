package com.tkym.labs.beancache;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeancacheQuery<BT,KT extends Comparable<KT>>{
	private final Map<Key<BT, KT>, BT> sourceMap;
	private final BeanMeta<BT, KT> beanMeta;
	private final Set<Key<BT, KT>> source;
	private BeancacheQuery(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> sourceMap, Set<Key<BT,KT>> keyset) {
		this.sourceMap = sourceMap;
		this.beanMeta = beanMeta;
		this.source = keyset;
	}
	private BeancacheQuery(BeanMeta<BT, KT> beanMeta, Map<Key<BT, KT>, BT> sourceMap) {
		this(beanMeta, sourceMap, new HashSet<Key<BT,KT>>(sourceMap.keySet()));
	}
	public Map<Key<BT, KT>, BT> sourceMap(){
		return this.sourceMap;
	}
	Set<Key<BT, KT>> source(){
		return this.source;
	}
	public BeancacheFilter<BT,KT,KT> key(){
		return key(this.beanMeta);
	}
	public <PKT extends Comparable<PKT>, PBT> BeancacheFilter<BT,KT,PKT> key(BeanMeta<PBT, PKT> parent){
		BeancacheKeyFilter<BT, KT, PBT, PKT> idx = new BeancacheKeyFilter<BT, KT, PBT, PKT>(this.beanMeta, parent);
		for (Key<BT,KT> key : this.source) idx.add(key);
		return idx;
	}
	public <PT extends Comparable<PT>> BeancacheFilter<BT,KT,PT> property(PropertyMeta<BT,PT> propertyMeta){
		BeancachePropertyFitler<BT, KT, PT> idx = new BeancachePropertyFitler<BT, KT, PT>(beanMeta, propertyMeta);
		for (Key<BT,KT> key : this.source) idx.put(key, sourceMap.get(key));
		return idx;
	}
	public Set<Key<BT, KT>> defferent(Set<Key<BT,KT>> keyset){
		Set<Key<BT,KT>> ret = new LinkedHashSet<Key<BT,KT>>();
		ret.addAll(source);
		ret.removeAll(keyset);
		return ret;
	}
	public BeancacheQuery<BT,KT> clone(Set<Key<BT,KT>> keyset){
		return new BeancacheQuery<BT, KT>(this.beanMeta, this.sourceMap, keyset);
	}
	static class BeancacheQueryBuilder<BT,KT extends Comparable<KT>>{
		private final Map<Key<BT, KT>, BT> sourceMap;
		private final BeanMeta<BT, KT> beanMeta;
		BeancacheQueryBuilder(Map<Key<BT, KT>, BT> sourceMap, BeanMeta<BT, KT> beanMeta) {
			this.sourceMap = sourceMap;
			this.beanMeta = beanMeta;
		}
		BeancacheQuery<BT,KT> build(){
			return new BeancacheQuery<BT, KT>(this.beanMeta, this.sourceMap);
		}
		BeancacheQuery<BT,KT> build(Set<Key<BT,KT>> source){
			return new BeancacheQuery<BT, KT>(this.beanMeta, this.sourceMap, source);
		}
	}
}