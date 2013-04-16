package com.tkym.labs.beanstore;

import java.util.LinkedHashSet;
import java.util.Set;

import com.tkym.labs.beancache.BeancacheIndexMatcher;
import com.tkym.labs.beancache.BeancacheQuery;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanFilter;
import com.tkym.labs.beanstore.api.BeanFilterComposite;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;

class BeancacheFilterProcesser<BT, KT extends Comparable<KT>>{
	private final BeancacheQuery<BT, KT> beancacheQuery;
	BeancacheFilterProcesser(BeancacheQuery<BT, KT> beancacheQuery){
		this.beancacheQuery = beancacheQuery;
	}
	static <BT, KT extends Comparable<KT>> 
		BeancacheFilterProcesser<BT, KT> target(BeancacheQuery<BT, KT> beancacheQuery){
			return new BeancacheFilterProcesser<BT,KT>(beancacheQuery);
		}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) 
	Set<Key<BT,KT>> criteria(BeanFilterCriteria criteria){
		if (criteria instanceof BeanFilter){
			return filter((BeanFilter) criteria);
		}else if (criteria instanceof BeanFilterComposite){
			return composite((BeanFilterComposite) criteria);
		}else{
			throw new IllegalArgumentException("unsupport criteria type");
		}
	}
	private Set<Key<BT,KT>> composite(BeanFilterComposite composite){
		switch(composite.getType()){
		case AND: return compositeAsAnd(composite.getChildlen());
		case OR: return compositeAsOr(composite.getChildlen());
		default:
			throw new IllegalArgumentException("unsupport compsite type:"+composite.getType());
		}
	}
	private Set<Key<BT,KT>> compositeAsOr(BeanFilterCriteria... criteriaList){
		Set<Key<BT,KT>> set = new LinkedHashSet<Key<BT,KT>>();
		for (BeanFilterCriteria criteria : criteriaList)
			set.addAll(criteria(criteria));
		return set;
	}
	private Set<Key<BT,KT>> compositeAsAnd(BeanFilterCriteria... criteriaList){
		Set<Key<BT,KT>> set = null;
		for (BeanFilterCriteria criteria : criteriaList)
			if (set == null) 
				set = new LinkedHashSet<Key<BT,KT>>(criteria(criteria));
			else 
				set.retainAll(criteria(criteria));
		return set;
	}
	<PT extends Comparable<PT>> Set<Key<BT,KT>> filter(BeanFilter<BT, PT> filter){
		switch(filter.getOperator()){
		case EQUAL: 				return equalTo(filter);
		case NOT_EQUAL:				return notEqual(filter);
		case GREATER_THAN:			return greaterThan(filter);
		case GREATER_THAN_OR_EQUAL:	return greaterEqual(filter);
		case LESS_THAN:				return lessThan(filter);
		case LESS_THAN_OR_EQUAL:	return lessEqual(filter);
		case IN:					return in(filter);
		case START_WITH: 			return startWith(asString(filter));
		case END_WITH: 				return endWith(asString(filter));
		case CONTAIN:				return contain(asString(filter));
		default:
			throw new IllegalArgumentException("unsupport Operation:"+filter.getOperator());
		}
	}
	@SuppressWarnings("unchecked")
	private <PT extends Comparable<PT>> BeanFilter<BT,String> asString(BeanFilter<BT, PT> filter){
		return (BeanFilter<BT,String>)filter;
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> greaterThan(BeanFilter<BT, PT> filter){
		return beancacheQuery.property(filter.getMeta()).greaterThan(filter.value());
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> greaterEqual(BeanFilter<BT, PT> filter){
		return beancacheQuery.property(filter.getMeta()).greaterEqual(filter.value());
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> lessThan(BeanFilter<BT, PT> filter){
		return beancacheQuery.property(filter.getMeta()).lessThan(filter.value());
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> lessEqual(BeanFilter<BT, PT> filter){
		return beancacheQuery.property(filter.getMeta()).lessEqual(filter.value());
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> equalTo(BeanFilter<BT, PT> filter){
		return beancacheQuery.property(filter.getMeta()).equalsTo(filter.value());
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> notEqual(BeanFilter<BT, PT> filter){
		Set<Key<BT, KT>> def = new LinkedHashSet<Key<BT,KT>>(beancacheQuery.asKeySet());
		def.removeAll(beancacheQuery.property(filter.getMeta()).equalsTo(filter.value()));
		return def;
	}
	private <PT extends Comparable<PT>> Set<Key<BT,KT>> in(BeanFilter<BT, PT> filter){
		Set<Key<BT, KT>> union = new LinkedHashSet<Key<BT,KT>>();
		for (PT p : filter.getValues()) 
			union.addAll(beancacheQuery.property(filter.getMeta()).equalsTo(p));
		return union;
	}
	private Set<Key<BT,KT>> startWith(BeanFilter<BT, String> filter){
		return beancacheQuery.property(filter.getMeta()).match(
				BeancacheIndexMatcher.Matchers.startWith(filter.value()));
	}
	private Set<Key<BT,KT>> endWith(BeanFilter<BT, String> filter){
		return beancacheQuery.property(filter.getMeta()).match(
				BeancacheIndexMatcher.Matchers.endWith(filter.value()));
	}
	private Set<Key<BT,KT>> contain(BeanFilter<BT, String> filter){
		return beancacheQuery.property(filter.getMeta()).match(
				BeancacheIndexMatcher.Matchers.contain(filter.value()));
	}
}