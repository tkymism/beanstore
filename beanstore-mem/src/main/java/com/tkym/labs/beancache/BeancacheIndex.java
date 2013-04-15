package com.tkym.labs.beancache;


public interface BeancacheIndex<BT,KT extends Comparable<KT>, PT extends Comparable<PT>> {
	BeancacheQuery<BT,KT> tail(PT from, boolean inclusive);
	BeancacheQuery<BT,KT> head(PT to, boolean inclusive);
	BeancacheQuery<BT,KT> sub(PT from, boolean fromInclusive, PT to, boolean toInclusive);
	BeancacheQuery<BT,KT> greaterThan(PT p);
	BeancacheQuery<BT,KT> greaterEqual(PT p);
	BeancacheQuery<BT,KT> lessThan(PT p);
	BeancacheQuery<BT,KT> lessEqual(PT p);
	BeancacheQuery<BT,KT> equalsTo(PT p);
	BeancacheQuery<BT,KT> in(PT... ps);
	BeancacheQuery<BT,KT> notEqualsTo(PT p);
	BeancacheQuery<BT,KT> notIn(PT... ps);
}
