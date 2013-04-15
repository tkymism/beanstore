package com.tkym.labs.beancache;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;

public class BeancacheKeyIndex<BT,KT extends Comparable<KT>, PBT, PKT extends Comparable<PKT>>
	extends AbstractBeancacheIndex<BT,KT,PKT>{
	private final int count;
	BeancacheKeyIndex(BeancacheQuery<BT,KT> scaner, BeanMeta<PBT, PKT> parent){
		super(scaner);
		this.count = countRank(scaner.beanMeta, parent);
		if (this.count < 0)
			throw new IllegalArgumentException(
					parent.getBeanType().getName()+
					" is not parent class of " + 
					scaner.beanMeta.getBeanType().getName());
	}
	void add(Key<BT, KT> key){
		this.index.put(typeSafeValueIfHasParent(key), key);
	}
	@SuppressWarnings("unchecked")
	PKT typeSafeValueIfHasParent(Key<BT, KT> key){
		return (PKT) valueIfHasParent(key);
	}
	Object valueIfHasParent(Key<BT, KT> key){
		Key<?, ?> k = key;
		for (int i=0; i<count; i++) k = k.getParent();
		return k.value();
	}
	static int countRank(BeanMeta<?, ?> from, BeanMeta<?, ?> to){
		return countRank(from, to, 0);
	}
	private static int countRank(BeanMeta<?, ?> from, BeanMeta<?, ?> to, int count){
		if (from.equals(to)) return count;
		BeanMeta<?,?> p = from.parent();
		if (p == null) return -1;
		else return countRank(p, to, count+1);
	}
}