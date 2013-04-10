package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.tkym.labs.beancache.BeancacheTreeIndex.BeancacheIndexKey;
import com.tkym.labs.beancache.BeancacheTreeIndex.BeancacheIndexKeyComparator;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.BeanBuilder.HogeBuilder;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;

public class BeancacheTest {
	private static HogeMeta HOGE = HogeMeta.get();
	@Test
	public void testBeancacheIndex_headIndex(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		BeancacheTreeIndex<Hoge,String,Integer> sub;
		Iterator<Hoge> ite;
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.next().getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.next().getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is maximum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 3), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.next().getKey(), is("hoge96"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is minimum and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 3), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
		
		// headIndex while key is equals and intValue:3
		sub = index.headIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge96").build(), 3), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge99"));
		assertThat(ite.next().getKey(), is("hoge98"));
		assertThat(ite.next().getKey(), is("hoge97"));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_tailIndex(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		BeancacheTreeIndex<Hoge,String,Integer> sub;
		Iterator<Hoge> ite;

		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 96), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 96), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge3"));
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge3").build(), 96), true);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge3"));
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).max().build(), 96), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).min().build(), 96), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge3"));
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
		
		sub = index.tailIndex(index.createIndexKey(KeyBuilder.root().meta(HOGE).is("hoge3").build(), 96), false);
		ite = sub.navigableMap.values().iterator();
		assertThat(ite.next().getKey(), is("hoge2"));
		assertThat(ite.next().getKey(), is("hoge1"));
		assertThat(ite.next().getKey(), is("hoge0"));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_headIndexForPropertyValueCase001(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Hoge> ite;
		ite = index.headIndex(3, true).navigableMap().values().iterator();
		assertThat(ite.next().getIntValue(), is(0));
		assertThat(ite.next().getIntValue(), is(1));
		assertThat(ite.next().getIntValue(), is(2));
		assertThat(ite.next().getIntValue(), is(3));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_headIndexForPropertyValueCase002(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Hoge> ite;
		ite = index.headIndex(3, false).navigableMap().values().iterator();
		assertThat(ite.next().getIntValue(), is(0));
		assertThat(ite.next().getIntValue(), is(1));
		assertThat(ite.next().getIntValue(), is(2));
		assertThat(ite.hasNext(), is(false));
	}

	@Test
	public void testBeancacheIndex_tailIndexForPropertyValueCase001(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Hoge> ite;
		ite = index.tailIndex(96, true).navigableMap().values().iterator();
		assertThat(ite.next().getIntValue(), is(96));
		assertThat(ite.next().getIntValue(), is(97));
		assertThat(ite.next().getIntValue(), is(98));
		assertThat(ite.next().getIntValue(), is(99));
		assertThat(ite.hasNext(), is(false));
	}
	
	@Test
	public void testBeancacheIndex_tailIndexForPropertyValueCase002(){
		BeancacheTreeIndex<Hoge,String,Integer> index 
			= new BeancacheTreeIndex<Hoge,String,Integer>(HOGE, HOGE.intValue);
		for(int i=0; i<100; i++)
			index.put(HOGE.key(null, createHogeKeyStringForID(i)), createHogeForID(i));
		Iterator<Hoge> ite;
		ite = index.tailIndex(96, false).navigableMap().values().iterator();
		assertThat(ite.next().getIntValue(), is(97));
		assertThat(ite.next().getIntValue(), is(98));
		assertThat(ite.next().getIntValue(), is(99));
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
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								)
							);
		assertThat(ret, is(0));
	}
	@Test
	public void testBeancacheIndexKeyCase002(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase003(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(1));
	}
	@Test
	public void testBeancacheIndexKeyCase004(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase005(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								)
							);
		assertThat(ret, is(1));
	}
	@Test
	public void testBeancacheIndexKeyCase006(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(-1));
	}
	@Test
	public void testBeancacheIndexKeyCase007(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase011(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase012(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase013(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase014(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase015(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase016(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase017(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MinCase018(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								1
								)
							);
		assertThat(ret, is(-1));
	}

	@Test
	public void testBeancacheIndexKey_MinCase019(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MIN,
								0
								)
							);
		assertThat(ret, is(1));
	}
	
	@Test
	public void testBeancacheIndexKey_MaxCase020(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(-1));
	}

	@Test
	public void testBeancacheIndexKey_MaxCase021(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								0
								)
							);
		assertThat(ret, is(1));
	}

	@Test
	public void testBeancacheIndexKey_MaxCase022(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								BeancacheIndexKey.MAX,
								1
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase023(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								1
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase024(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								)
							);
		assertThat(ret, is(0));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase025(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build()
								)
							);
		assertThat(ret, is(-1));
	}
	
	@Test
	public void testBeancacheIndexKey_IgnorePropertyCase026(){
		BeancacheIndexKeyComparator<Hoge,String,Integer> comparator = 
			new BeancacheIndexKeyComparator<Hoge,String,Integer>(HOGE, HOGE.intValue);
		int ret = 
				comparator.compare(
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a1").build(),
								0
								),
						new BeancacheIndexKey<Hoge,String,Integer>(
								KeyBuilder.root().meta(HOGE).is("a0").build()
								)
							);
		assertThat(ret, is(1));
	}
	
	static class BeancacheMap<BT,KT>{
		private Map<Key<BT, KT>, BT> cacheMap;
		BeancacheMap(){
			this(new ConcurrentHashMap<Key<BT, KT>, BT>());
		}
		BeancacheMap(int initCapacity){
			this(new ConcurrentHashMap<Key<BT, KT>, BT>(initCapacity));
		}
		BeancacheMap(Map<Key<BT, KT>, BT> chacheMap) {
			this.cacheMap = chacheMap;
		}
		BT get(Key<BT, KT> key){
			return cacheMap.get(key);
		}
		BT put(Key<BT, KT> key, BT bean){
			return cacheMap.put(key, bean);
		}
		BT remove(Key<BT, KT> key){
			return cacheMap.remove(key);
		}
	}
}