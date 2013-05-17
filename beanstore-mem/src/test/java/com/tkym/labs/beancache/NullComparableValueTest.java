package com.tkym.labs.beancache;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

import com.tkym.labs.beancache.NullComparableValue.NullComparableValueComparator;

public class NullComparableValueTest {
	static NullComparableValue<Integer> v(Integer i){
		return new NullComparableValue<Integer>(i);
	}
	@Test
	public void testComparatorCase001(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(0), v(1)), is(-1));
	}
	@Test
	public void testComparatorCase002(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(1), v(0)), is(1));
	}
	@Test
	public void testComparatorCase003(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(1), v(1)), is(0));
	}
	@Test
	public void testComparatorCase004(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(null), v(1)), is(1));
	}
	@Test
	public void testComparatorCase005(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(1), v(null)), is(-1));
	}
	@Test
	public void testComparatorCase006(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.compare(v(null), v(null)), is(0));
	}
	@Test
	public void testComparatorCase011(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(0), v(1)), is(-1));
	}
	@Test
	public void testComparatorCase012(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(1), v(0)), is(1));
	}
	@Test
	public void testComparatorCase013(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(1), v(1)), is(0));
	}
	@Test
	public void testComparatorCase014(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(null), v(1)), is(-1));
	}
	@Test
	public void testComparatorCase015(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(1), v(null)), is(1));
	}
	@Test
	public void testComparatorCase016(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		assertThat(comparator.nullFirst().compare(v(null), v(null)), is(0));
	}
	@Test
	public void testNullLastComaprableCase001(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		TreeSet<NullComparableValue<Integer>> treeset = 
				new TreeSet<NullComparableValue<Integer>>(comparator);
		treeset.add(v(3));
		treeset.add(v(2));
		treeset.add(v(1));
		treeset.add(v(0));
		Iterator<NullComparableValue<Integer>> ite = treeset.iterator();
		assertThat(ite.next().value, is(0));
		assertThat(ite.next().value, is(1));
		assertThat(ite.next().value, is(2));
		assertThat(ite.next().value, is(3));
		assertFalse(ite.hasNext());
	}

	@Test
	public void testNullLastComaprableCase002(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		TreeSet<NullComparableValue<Integer>> treeset = 
				new TreeSet<NullComparableValue<Integer>>(comparator);
		treeset.add(v(3));
		treeset.add(v(null));
		treeset.add(v(1));
		treeset.add(v(0));
		Iterator<NullComparableValue<Integer>> ite = treeset.iterator();
		assertThat(ite.next().value, is(0));
		assertThat(ite.next().value, is(1));
		assertThat(ite.next().value, is(3));
		assertNull(ite.next().value);
		assertFalse(ite.hasNext());
	}

	@Test
	public void testNullFistComaprableCase001(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		TreeSet<NullComparableValue<Integer>> treeset = 
				new TreeSet<NullComparableValue<Integer>>(comparator.nullFirst());
		treeset.add(v(3));
		treeset.add(v(null));
		treeset.add(v(1));
		treeset.add(v(0));
		Iterator<NullComparableValue<Integer>> ite = treeset.iterator();
		assertNull(ite.next().value);
		assertThat(ite.next().value, is(0));
		assertThat(ite.next().value, is(1));
		assertThat(ite.next().value, is(3));
		assertFalse(ite.hasNext());
	}

	@Test
	public void testNullFisrtComaprableCase001(){
		NullComparableValueComparator<Integer> comparator = 
				new NullComparableValueComparator<Integer>();
		TreeSet<NullComparableValue<Integer>> treeset = 
				new TreeSet<NullComparableValue<Integer>>(comparator.nullFirst());
		treeset.add(v(3));
		treeset.add(v(2));
		treeset.add(v(1));
		treeset.add(v(0));
		Iterator<NullComparableValue<Integer>> ite = treeset.iterator();
		assertThat(ite.next().value, is(0));
		assertThat(ite.next().value, is(1));
		assertThat(ite.next().value, is(2));
		assertThat(ite.next().value, is(3));
		assertFalse(ite.hasNext());
	}
}