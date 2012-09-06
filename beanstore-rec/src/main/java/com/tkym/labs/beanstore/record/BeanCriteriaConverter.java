package com.tkym.labs.beanstore.record;

import static com.tkym.labs.record.QueryUtils.and;
import static com.tkym.labs.record.QueryUtils.or;
import static com.tkym.labs.record.QueryUtils.property;

import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanstore.api.BeanFilter;
import com.tkym.labs.beanstore.api.BeanFilterComposite;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;
import com.tkym.labs.beanstore.api.BeanSort;
import com.tkym.labs.beanstore.api.BeanSortCriteria;
import com.tkym.labs.record.QueryCriteriaBuilder;
import com.tkym.labs.record.QueryFilter;
import com.tkym.labs.record.QueryFilterComposite;
import com.tkym.labs.record.QueryFilterCriteria;
import com.tkym.labs.record.QuerySorter;
import com.tkym.labs.record.QuerySorterCriteria;

class BeanCriteriaConverter {
	private BeanCriteriaConverter(){};
	static <BT> QueryFilterComposite convert(BeanFilterComposite composite) {
		switch(composite.getType()){
		case AND:
			return and(convertArray(composite.getChildlen()));
		case OR:
			return or(convertArray(composite.getChildlen()));
		default:
			throw new IllegalArgumentException(
					"BeanFilterCoposite is illegal type :" + composite.getType());
		}
	}
	static <BT> QueryFilterCriteria[] convertArray(BeanFilterCriteria... criteria){
		QueryFilterCriteria[] ret = new QueryFilterCriteria[criteria.length];
		for (int i=0; i<ret.length; i++)
			ret[i] = convert(criteria[i]);
		return ret;
	}
	@SuppressWarnings("unchecked")
	static <BT> QueryFilterCriteria convert(BeanFilterCriteria criteria){
		if (criteria instanceof BeanFilter)
			return convert((BeanFilter<BT,?>) criteria);
		else if (criteria instanceof BeanFilterComposite)
			return convert((BeanFilterComposite) criteria);
		else
			throw new IllegalArgumentException("criteria is not support type");
	}
	static <BT, PT> QueryFilter<PT> convert(BeanFilter<BT, PT> filter) {
		PropertyMeta<BT, PT> meta = filter.getMeta();
		QueryCriteriaBuilder<PT> builder = 
				property(meta.getPropertyName(), meta.getPropertyType());
		switch(filter.getOperator()){
		case EQUAL: 
			return builder.equalsTo(filter.getValues()[0]);
		case NOT_EQUAL: 
			return builder.notEquals(filter.getValues()[0]);
		case LESS_THAN_OR_EQUAL: 
			return builder.lessEqual(filter.getValues()[0]);
		case LESS_THAN: 
			return builder.lessThan(filter.getValues()[0]);
		case GREATER_THAN_OR_EQUAL:
			return builder.greaterEqual(filter.getValues()[0]);
		case GREATER_THAN: 
			return builder.greaterThan(filter.getValues()[0]);
		case IN: 
			return builder.in(filter.getValues());
		case START_WITH: 
			return builder.startsWith(filter.getValues()[0]);
		case END_WITH: 
			return builder.endsWith(filter.getValues()[0]);
		case CONTAIN: 
			return builder.contains(filter.getValues()[0]);
		default:
			throw new IllegalArgumentException(
					"BeanFilterOperator is illegal type :" + filter.getOperator());
		}
	}
	
	@SuppressWarnings("unchecked")
	static <BT, PT> QuerySorterCriteria convert(BeanSortCriteria sort) {
		return convert((BeanSort<BT, PT>) sort);
	}

	static <BT, PT> QuerySorter convert(BeanSort<BT, PT> sort) {
		PropertyMeta<BT, PT> meta = sort.getMeta();
		QueryCriteriaBuilder<PT> builder = property(
				meta.getPropertyName(), meta.getPropertyType());
		switch (sort.getOperator()) {
		case ASCENDING:
			return builder.asc();
		case DESCENDING:
			return builder.desc();
		default:
			throw new IllegalArgumentException(
					"BeanSortOperator is illegal type :"
							+ sort.getOperator());
		}
	}
}