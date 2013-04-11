package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.tkym.labs.beancache.BeancachePropertyTreeIndex.BeancacheTreeIndexKey;
import com.tkym.labs.beancache.BeancachePropertyTreeIndex.BeancacheTreeIndexKeyComparator;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.BeanBuilder.HogeBuilder;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;

public class BeancachePropertyTreeIndexTest {
	private static HogeMeta HOGE = HogeMeta.get();
	@Test
	public void test_headIndex(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		BeancachePropertyTreeIndex<Hoge,String,Integer> sub;
		Iterator<Key<Hoge, String>> ite;
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(map.get(ite.next()).getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(map.get(ite.next()).getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(map.get(ite.next()).getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge99"));
		assertThat(map.get(ite.next()).getKey(), is("hoge98"));
		assertThat(map.get(ite.next()).getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void test_tailIndex(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		BeancachePropertyTreeIndex<Hoge,String,Integer> sub;
		Iterator<Key<Hoge,String>> ite;

		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 96), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 96), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge3"));
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge3").build(), 96), true);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge3"));
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 96), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 96), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge3"));
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge3").build(), 96), false);
		ite = sub.navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getKey(), is("hoge2"));
		assertThat(map.get(ite.next()).getKey(), is("hoge1"));
		assertThat(map.get(ite.next()).getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void test_headIndexForPropertyValueCase001(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Key<Hoge,String>> ite;
		ite = ((BeancachePropertyTreeIndex<Hoge,String,Integer>)index.headIndex(3, true)).navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getIntValue(), is(0));
		assertThat(map.get(ite.next()).getIntValue(), is(1));
		assertThat(map.get(ite.next()).getIntValue(), is(2));
		assertThat(map.get(ite.next()).getIntValue(), is(3));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_headIndexForPropertyValueCase002(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Key<Hoge,String>> ite;
		ite = ((BeancachePropertyTreeIndex<Hoge,String,Integer>)index.headIndex(3, false)).navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getIntValue(), is(0));
		assertThat(map.get(ite.next()).getIntValue(), is(1));
		assertThat(map.get(ite.next()).getIntValue(), is(2));
		assertThat(ite.hasNext(), is(false));
	}

	@Test
	public void testBeancacheIndex_tailIndexForPropertyValueCase001(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Key<Hoge,String>> ite;
		ite = ((BeancachePropertyTreeIndex<Hoge,String,Integer>)index.tailIndex(96, true)).navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getIntValue(), is(96));
		assertThat(map.get(ite.next()).getIntValue(), is(97));
		assertThat(map.get(ite.next()).getIntValue(), is(98));
		assertThat(map.get(ite.next()).getIntValue(), is(99));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_tailIndexForPropertyValueCase002(){
		BeancachePropertyTreeIndex<Hoge,String,Integer> index 
			= new BeancachePropertyTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Map<Key<Hoge, String>, Hoge> map = new HashMap<Key<Hoge,String>, Hoge>();
		for(int i=0; i<100; i++)
			map.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Key<Hoge,String>> ite;
		ite = ((BeancachePropertyTreeIndex<Hoge,String,Integer>)index.tailIndex(96, false)).navigableMap().values().iterator();
		assertThat(map.get(ite.next()).getIntValue(), is(97));
		assertThat(map.get(ite.next()).getIntValue(), is(98));
		assertThat(map.get(ite.next()).getIntValue(), is(99));
		assertThat(ite.hasNext(), is(false));
	}
	
	static Key<Hoge, String> createHogeKeyForID(int i){
		return KeyBuilder.root().meta(HOGE).is(createHogeKeyStringForID(i)).build();
	}
	static String createHogeKeyStringForID(int i){
		return "hoge"+i;
	}
	static Hoge createHogeForID(int i){
		return HogeBuilder.create().key(createHogeKeyStringForID(i)).intValue(99-i).bean();
	}
	@Test
	public void testBeancacheIndexKeyCase001(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								)
							);
		assertThat(ret, is(0));
	}
	@Test
	public void testBeancacheIndexKeyCase002(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase003(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(1));
	}
	@Test
	public void testBeancacheIndexKeyCase004(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase005(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								)
							);
		assertThat(ret, is(1));
	}
	@Test
	public void testBeancacheIndexKeyCase006(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase007(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase011(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase012(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase013(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase014(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase015(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase016(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase017(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase018(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								1
								)
							);
		assertThat(ret, is(-1));
	}

	@Test
	public void testBeancacheIndexKey_MinCase019(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase020(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(-1));
	}

	@Test
	public void testBeancacheIndexKey_MaxCase021(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(1));
	}

	@Test
	public void testBeancacheIndexKey_MaxCase022(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								BeancacheTreeIndexKey.MAX,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase023(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase024(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase025(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build()
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase026(){
		BeancacheTreeIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheTreeIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								),
						new BeancacheTreeIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								)
							);
		assertThat(ret, is(1));
	}
}