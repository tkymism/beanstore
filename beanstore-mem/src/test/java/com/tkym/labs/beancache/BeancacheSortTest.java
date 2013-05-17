package com.tkym.labs.beancache;

import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;

public class BeancacheSortTest {
	private static final HogeMeta HOGE = HogeMeta.get();
	static Hoge h(String key, int i, float f, double d, String str){
		Hoge hoge = new Hoge();
		hoge.setKey(key);
		hoge.setIntValue(i);
		hoge.setFloatValue(f);
		hoge.setDoubleValue(d);
		hoge.setStringValue(str);
		return hoge;
	}
	static Key<Hoge, String> key(String k){
		return KeyBuilder.root().meta(HOGE).is(k).build();
	}
	class BeancacheSort<BT, KT extends Comparable<KT>>{
		private final Key<BT, KT> key;
		private final Comparable<?>[] c;
		BeancacheSort(Key<BT, KT> key, Comparable<?>... c){
			this.key = key;
			this.c = c;
		}
	}
	
	@Test
	public void testSortCase001(){
		
		BeancacheFilterMap<Integer, Hoge, String> idx1 
			= new BeancacheFilterMap<Integer, Hoge, String>(HOGE);
		Key<Hoge, String> a = key("a"); 
		Key<Hoge, String> b = key("b"); 
		Key<Hoge, String> c = key("c"); 
		Key<Hoge, String> d = key("d"); 
		idx1.put(2, a);
		idx1.put(2, b);
		idx1.put(1, c);
		idx1.put(1, d);
		BeancacheFilterMap<Double, Hoge, String> idx2 
			= new BeancacheFilterMap<Double, Hoge, String>(HOGE);
		idx2.put(0.1, a);
		idx2.put(0.2, b);
		idx2.put(0.1, c);
		idx2.put(0.2, d);
	}
}
