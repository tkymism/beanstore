package com.tkym.labs.beanstore.record;

import static com.tkym.labs.beanstore.api.BeanQueryUtils.and;
import static com.tkym.labs.beanstore.api.BeanQueryUtils.p;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.tkym.labs.beans.PersonMeta;
import com.tkym.labs.beanstore.api.BeanFilterComposite;
import com.tkym.labs.record.QueryFilter;
import com.tkym.labs.record.QueryFilter.QueryFilterOperator;
import com.tkym.labs.record.QueryFilterComposite;
import com.tkym.labs.record.QueryFilterComposite.QueryFilterCompositeType;
import com.tkym.labs.record.QueryFilterCriteria;
import com.tkym.labs.record.QuerySorter;
import com.tkym.labs.record.QuerySorter.QuerySortDirection;
import com.tkym.labs.record.QuerySorterCriteria;

public class BeanCriteriaConverterTest {
	@Test
	public void testBeanCriteriaConverter_SorterCase001() {
		PersonMeta PERSON = PersonMeta.get();
		QuerySorterCriteria criteria = 
				BeanCriteriaConverter.convert(p(PERSON.id).asc());
		assertThat(criteria, is(QuerySorter.class));
		QuerySorter sorter = (QuerySorter) criteria;
		assertThat(sorter.getProperty(), is("id"));
		assertThat(sorter.getDirection(), is(QuerySortDirection.ASCENDING));
	}
	@Test
	public void testBeanCriteriaConverter_SorterCase002() {
		PersonMeta PERSON = PersonMeta.get();
		QuerySorterCriteria criteria = 
				BeanCriteriaConverter.convert(p(PERSON.id).desc());
		assertThat(criteria, is(QuerySorter.class));
		QuerySorter sorter = (QuerySorter) criteria;
		assertThat(sorter.getProperty(), is("id"));
		assertThat(sorter.getDirection(), is(QuerySortDirection.DESCENDING));
	}
	@Test
	public void testBeanCriteriaConverter_FilterCase001() {
		PersonMeta PERSON = PersonMeta.get();
		QueryFilterCriteria criteria = 
				BeanCriteriaConverter.convert(p(PERSON.id).equalsTo(1L));
		assertThat(criteria, is(QueryFilter.class));
		@SuppressWarnings("unchecked")
		QueryFilter<Long> filter = (QueryFilter<Long>) criteria;
		assertThat(filter.getProperty(), is("id"));
		assertThat(filter.getOperator(), is(QueryFilterOperator.EQUAL));
		assertThat(filter.getValue(), is(1L));
	}
	@Test
	public void testBeanCriteriaConverter_FilterCase002() {
		PersonMeta PERSON = PersonMeta.get();
		QueryFilterCriteria criteria = 
				BeanCriteriaConverter.convert(p(PERSON.id).lessEqual(1L));
		assertThat(criteria, is(QueryFilter.class));
		@SuppressWarnings("unchecked")
		QueryFilter<Long> filter = (QueryFilter<Long>) criteria;
		assertThat(filter.getProperty(), is("id"));
		assertThat(filter.getOperator(), is(QueryFilterOperator.LESS_THAN_OR_EQUAL));
		assertThat(filter.getValue(), is(1L));
	}
	@Test
	public void testBeanCriteriaConverter_FilterCase003() {
		PersonMeta PERSON = PersonMeta.get();
		QueryFilterCriteria criteria = 
				BeanCriteriaConverter.convert(p(PERSON.id).in(1L,2L));
		assertThat(criteria, is(QueryFilter.class));
		@SuppressWarnings("unchecked")
		QueryFilter<Long> filter = (QueryFilter<Long>) criteria;
		assertThat(filter.getProperty(), is("id"));
		assertThat(filter.getOperator(), is(QueryFilterOperator.IN));
		assertThat(filter.getValues()[0], is(1L));
		assertThat(filter.getValues()[1], is(2L));
		assertThat(filter.getValues().length, is(2));
	}
	@Test
	public void testBeanCriteriaConverter_CopositeCase001() {
		PersonMeta PERSON = PersonMeta.get();
		BeanFilterComposite composite = 
				and(
					p(PERSON.id).in(1L,2L),
					p(PERSON.name).equalsTo("hogehoge")
				);
		QueryFilterCriteria criteria = BeanCriteriaConverter.convert(composite);
		assertThat(criteria, is(QueryFilterComposite.class));
		QueryFilterComposite converted = (QueryFilterComposite) criteria;
		assertThat(converted.getType(), is(QueryFilterCompositeType.AND));
		assertThat(converted.getChildlen().size(), is(2));
		QueryFilterCriteria c1 = converted.getChildlen().get(0);
		QueryFilterCriteria c2 = converted.getChildlen().get(1);
		assertThat(c1, is(QueryFilter.class));
		assertThat(c2, is(QueryFilter.class));
		@SuppressWarnings("unchecked")
		QueryFilter<Long> id = (QueryFilter<Long>)c1;
		@SuppressWarnings("unchecked")
		QueryFilter<String> name = (QueryFilter<String>)c2;
		assertThat(id.getProperty(), is("id"));
		assertThat(id.getOperator(), is(QueryFilterOperator.IN));
		assertThat(id.getValues()[0], is(1L));
		assertThat(id.getValues()[1], is(2L));
		assertThat(id.getValues().length, is(2));
		assertThat(name.getProperty(), is("name"));
		assertThat(name.getOperator(), is(QueryFilterOperator.EQUAL));
		assertThat(name.getValue(), is("hogehoge"));
	}
}