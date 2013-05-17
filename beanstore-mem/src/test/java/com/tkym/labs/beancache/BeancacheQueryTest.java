package com.tkym.labs.beancache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.tkym.labs.beancache.BeancacheQuery.BeancacheQueryBuilder;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheQueryTest {
	private static final PersonMeta PERSON = PersonMeta.get(); 
	@Test
	public void testBeancacheQueryCase001(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "a3"));
		put(map, person(4L, "a4"));
		put(map, person(5L, "a5"));
		put(map, person(0L, "a0"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();
		Iterator<Key<Person, Long>> ite = 
				q.property(PERSON.name).asc().all().iterator();
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(1L));
		assertThat(ite.next().value(), is(2L));
		assertThat(ite.next().value(), is(3L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.hasNext(), is(false));
	}
	@Test
	public void testBeancacheQueryCase002(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "b0"));
		put(map, person(4L, "b1"));
		put(map, person(5L, "b2"));
		put(map, person(0L, "a0"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();
		Iterator<Key<Person, Long>> ite = 
				q.property(PERSON.name).asc().all().iterator();
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(1L));
		assertThat(ite.next().value(), is(2L));
		assertThat(ite.next().value(), is(3L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.hasNext(), is(false));
	}
	@Test
	public void testBeancacheQueryPersonCase003(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "a3"));
		put(map, person(4L, "b2"));
		put(map, person(5L, "b3"));
		put(map, person(0L, "b1"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();
		Iterator<Key<Person, Long>> ite = 
				q.clone(q.property(PERSON.name).greaterThan("b0")
						).property(PERSON.name).asc().all().iterator();
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.hasNext(), is(false));
	}
	@Test
	public void testBeancacheQueryPersonCase004(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "a3"));
		put(map, person(4L, "b2"));
		put(map, person(5L, "b3"));
		put(map, person(0L, "b1"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();		
		Iterator<Key<Person, Long>> ite = 
				q.clone(q.property(PERSON.name).greaterThan("b1")).property(PERSON.name).asc().all().iterator();
//		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.hasNext(), is(false));
	}
	@Test
	public void testBeancacheQueryPersonCase005(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "a3"));
		put(map, person(4L, "b2"));
		put(map, person(5L, "b3"));
		put(map, person(0L, "b1"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();
		Iterator<Key<Person, Long>> ite = 
				q.clone(q.property(PERSON.name).greaterThan("b0")
						).property(PERSON.name).desc().all().iterator();
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.hasNext(), is(false));
	}
	@Test
	public void testBeancacheQueryPersonCase006(){
		Map<Key<Person,Long>, Person> map = new HashMap<Key<Person,Long>, Person>();
		put(map, person(0L, "aX"));
		put(map, person(1L, "a1"));
		put(map, person(2L, "a2"));
		put(map, person(3L, "a3"));
		put(map, person(4L, "b2"));
		put(map, person(5L, "b3"));
		put(map, person(0L, "b1"));
		BeancacheQuery<Person, Long> q = new BeancacheQueryBuilder<Person, Long>(map,PERSON).build();
		Iterator<Key<Person, Long>> ite = 
				q.clone(
						q.property(PERSON.name).greaterThan("b0")
						).property(PERSON.name).asc().all().iterator();
		assertThat(ite.next().value(), is(0L));
		assertThat(ite.next().value(), is(4L));
		assertThat(ite.next().value(), is(5L));
		assertThat(ite.hasNext(), is(false));
	}
	private static void put(Map<Key<Person,Long>, Person> map, Person p){
		map.put(key(p), p);
	}
	private static Person person(long id, String name){
		Person person = new Person();
		person.setId(id);
		person.setName(name);
		return person;
	}
	private static Key<Person, Long> key(Person p){
		return KeyBuilder.root().meta(PERSON).is(p.getId()).build();
	}
}
