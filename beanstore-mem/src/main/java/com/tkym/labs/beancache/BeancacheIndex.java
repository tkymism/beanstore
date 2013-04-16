package com.tkym.labs.beancache;

import java.util.Set;

import com.tkym.labs.beanmeta.Key;


public interface BeancacheIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> {
	Set<Key<BT,KT>> tail(PT from, boolean inclusive);
	Set<Key<BT,KT>> head(PT to, boolean inclusive);
	Set<Key<BT,KT>> sub(PT from, boolean fromInclusive, PT to, boolean toInclusive);
	Set<Key<BT,KT>> greaterThan(PT p);
	Set<Key<BT,KT>> greaterEqual(PT p);
	Set<Key<BT,KT>> lessThan(PT p);
	Set<Key<BT,KT>> lessEqual(PT p);
	Set<Key<BT,KT>> equalsTo(PT p);
	Set<Key<BT,KT>> match(BeancacheIndexMatcher<PT> matcher);
}
