package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.After;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Hoge;
import com.tkym.labs.beans.HogeMeta;

public class BeancacheQuery2Test {
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
	private static final Beancache<Hoge, String> CACHE = 
			new Beancache<Hoge, String>(HOGE);
	static void put(Hoge h){ 
		CACHE.put(key(h.getKey()), h);
	}
	static void clear(){
		CACHE.clear();
	}
	@After
	public void teardown(){ clear(); }
	@Test
	public void testBeancacheQueryCase001(){
		put( h("a", 9, 0.1f, 2.0d, "e"));
		put( h("b", 8, 0.2f, 2.0d, "d"));
		put( h("c", 7, 0.3f, 2.0d, "c"));
		put( h("d", 6, 0.1f, 1.0d, "b"));
		put( h("e", 5, 0.2f, 1.0d, "a"));
		put( h("f", 4, 0.3f, 1.0d, "e"));
		put( h("g", 3, 0.4f, 1.0d, "d"));
		BeancacheQuery<Hoge, String> q = CACHE.queryAll();
		Iterator<Key<Hoge, String>> ite = 
				q.clone(q.property(HOGE.doubleValue).asc().all()).property(HOGE.floatValue).asc().all().iterator();
		assertThat(ite.next().value(), is("d"));
		assertThat(ite.next().value(), is("e"));
		assertThat(ite.next().value(), is("f"));
		assertThat(ite.next().value(), is("g"));
		assertThat(ite.next().value(), is("a"));
		assertThat(ite.next().value(), is("b"));
		assertThat(ite.next().value(), is("c"));
		assertFalse(ite.hasNext());
	}
}
