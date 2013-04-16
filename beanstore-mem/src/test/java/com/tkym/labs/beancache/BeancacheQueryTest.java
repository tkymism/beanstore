package com.tkym.labs.beancache;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.HogeMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;

public class BeancacheQueryTest {
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final Beancache<Person,Long> PERSON_CACHE = new Beancache<Person, Long>(PERSON); 
	private static final Beancache<Account,String> ACCOUNT_CACHE = new Beancache<Account, String>(ACCOUNT); 
	private static final Beancache<Bill,Integer> BILL_CACHE = new Beancache<Bill, Integer>(BILL); 
	private static final DecimalFormat PF = new DecimalFormat("00000");
	private static final DecimalFormat AF = new DecimalFormat("000");
	@BeforeClass
	public static void setupClass(){
		for (int i=0; i<PERSON_SIZE; i++){
			Person p = person(i);
			Key<Person, Long> pk = 
					KeyBuilder.root().
					meta(PERSON).is(p.getId()).
					build(); 
			PERSON_CACHE.put(pk, p);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = account(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				ACCOUNT_CACHE.put(ak, a);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = bill(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					BILL_CACHE.put(bk, b);
				}
			}
		}
	}
	
	static Person person(int i){
		Person person = new Person();
		person.setId(i);
		person.setName("a"+PF.format(i));
		return person;
	}
	
	static Account account(int i, int j){
		Account account = new Account();
		account.setEmail(PF.format(i)+"@"+AF.format(j)+".com");
		account.setAddress(PF.format(i)+"st."+AF.format(j));
		return account;
	}
	
	static Bill bill(int i, int j, int k){
		Bill bill = new Bill();
		bill.setNo(k);
		bill.setAmount(i+j);
		bill.setItem(j, AF.format(j)+PF.format(i));
		return bill;
	}
	
	@Test
	public void testBeancacheCase001(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.queryAll().property(PERSON.name).head("a00001", false);
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00000"));
	}
	
	@Test
	public void testBeancacheCase002(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.queryAll().property(PERSON.name).head("a00001", true);
		assertThat(ret.size(), is(2));
	}

	@Test
	public void testBeancacheCase003(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.queryAll().property(PERSON.name).tail("a00098", false);
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00099"));
	}
	
	@Test
	public void testBeancacheCase004(){
		Set<Key<Person,Long>> ret = PERSON_CACHE.queryAll().property(PERSON.name).tail("a00098", true);
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancacheCase005(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", false, "a00005", false);
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancacheCase006(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", false);
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheCase007(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", false, "a00005", true);
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheCase008(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", true);
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancacheCase009(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", true);
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancachePropertyCase001(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).head("a00001", false);
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00000"));
	}
	
	@Test
	public void testBeancachePropertyCase002(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).head("a00001", true);
		assertThat(ret.size(), is(2));
	}

	@Test
	public void testBeancachePropertyCase003(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).tail("a00098", false);
		assertThat(ret.size(), is(1));
		Iterator<Key<Person,Long>> ite = ret.iterator();
		assertThat(PERSON_CACHE.get(ite.next()).getName(), is("a00099"));
	}
	
	@Test
	public void testBeancachePropertyCase004(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).tail("a00098", true);
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancachePropertyCase005(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", false, "a00005", false);
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyCase006(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", false);
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyCase007(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", false, "a00005", true);
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyCase008(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", true);
		assertThat(ret.size(), is(3));
	}
	@Test
	public void testBeancachePropertyCase009(){
		Set<Key<Person,Long>> ret = 
				PERSON_CACHE.queryAll().
				property(PERSON.name).sub("a00003", true, "a00005", true);
		assertThat(ret.size(), is(3));
	}
	
	@Test
	public void testBeancachePropertyCase101(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.queryAll().
				property(ACCOUNT.address).tail("00098", false);
		assertThat(ret.size(), is(2*ACCOUNT_SIZE));
		for (Key<Account,String> k : ret)
			assertTrue(
					k.value().startsWith("00098") ||
					k.value().startsWith("00099")
					);
	}
	
	@Test
	public void testBeancachePropertyCase109(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).sub("00003", true, "00005", true)).
				intersect(q.property(ACCOUNT.address).sub("00003", false, "00005", false)).
				asKeySet();
		assertThat(ret.size(), is(2*ACCOUNT_SIZE));
		for (Key<Account,String> k : ret)
			assertTrue(
					k.value().startsWith("00003") ||
					k.value().startsWith("00004")
					);
	}
	
	@Test
	public void testCountRankCase001(){
		assertThat(BeancacheKeyIndex.countRank(BILL, PERSON), is(2));
	}
	@Test
	public void testCountRankCase002(){
		assertThat(BeancacheKeyIndex.countRank(BILL, ACCOUNT), is(1));
	}
	@Test
	public void testCountRankCase003(){
		assertThat(BeancacheKeyIndex.countRank(BILL, BILL), is(0));
	}
	@Test
	public void testCountRankCase004(){
		assertThat(BeancacheKeyIndex.countRank(ACCOUNT, PERSON), is(1));
	}
	@Test
	public void testCountRankCase005(){
		assertThat(BeancacheKeyIndex.countRank(ACCOUNT, ACCOUNT), is(0));
	}
	@Test
	public void testCountRankCase006(){
		assertThat(BeancacheKeyIndex.countRank(PERSON, PERSON), is(0));
	}
	@Test
	public void testCountRankCase007(){
		assertThat(BeancacheKeyIndex.countRank(PERSON, HogeMeta.get()), is(-1));
	}
	@Test
	public void testCountRankCase008(){
		assertThat(BeancacheKeyIndex.countRank(HogeMeta.get(), PERSON), is(-1));
	}
	@Test
	public void testCountRankCase009(){
		assertThat(BeancacheKeyIndex.countRank(PERSON, BILL), is(-1));
	}
	@Test
	public void testBeancacheKeyCase001(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.queryAll().
				key(PERSON).equalsTo(3L);
		assertThat(ret.size(), is(1*ACCOUNT_SIZE));
		for (Key<Account, String> k : ret)
			assertTrue(ACCOUNT_CACHE.get(k).getAddress().startsWith("00003st."));
	}
	@Test
	public void testBeancacheKeyCase002(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).tail("00003st.008", true)).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase003(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).tail("00003st.008", false)).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancacheKeyCase004(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).head("00003st.001", true)).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase005(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).head("00003st.001", false)).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	
	@Test
	public void testBeancachePropertyGreaterLessCase001(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).lessThan("00003st.001")).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase002(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).lessEqual("00003st.001")).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase003(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).greaterThan("00003st.008")).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase004(){
		BeancacheQuery<Account,String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account,String>> ret = q. 
				intersect(q.key(PERSON).equalsTo(3L)).
				intersect(q.property(ACCOUNT.address).greaterEqual("00003st.008")).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancacheKeyCase101(){
		Set<Key<Account,String>> ret = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build()).asKeySet();
		assertThat(ret.size(), is(1*ACCOUNT_SIZE));
		for (Key<Account, String> k : ret)
			assertTrue(ACCOUNT_CACHE.get(k).getAddress().startsWith("00003st."));
	}
	@Test
	public void testBeancacheKeyCase102(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).tail("00003st.008", true)).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase103(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).tail("00003st.008", false)).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancacheKeyCase104(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).head("00003st.001", true)).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancacheKeyCase105(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).head("00003st.001", false)).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	
	@Test
	public void testBeancachePropertyGreaterLessCase101(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).lessThan("00003st.001")).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase102(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).lessEqual("00003st.001")).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase103(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).greaterThan("00003st.008")).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyGreaterLessCase104(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).greaterEqual("00003st.008")).
				asKeySet();
		assertThat(ret.size(), is(2));
	}
	
	@Test
	public void testBeancachePropertyEqualsToCase001(){
		BeancacheQuery<Account,String> q = 
				ACCOUNT_CACHE.queryFor(KeyBuilder.root().meta(PERSON).is(3L).build());
		Set<Key<Account,String>> ret = q. 
				intersect(q.property(ACCOUNT.address).equalsTo("00003st.008")).
				asKeySet();
		assertThat(ret.size(), is(1));
	}
	@Test
	public void testBeancachePropertyEqualsToCase002(){
		Set<Key<Bill,Integer>> ret = 
				BILL_CACHE.queryAll().
				property(BILL.amount).equalsTo(15.0f);
		assertThat(ret.size(), is(1000));
	}
	@Test
	public void testBeancachePropertyEqualsToCase003(){
		Set<Key<Bill,Integer>> ret = 
				BILL_CACHE.queryAll().
				property(BILL.amount).equalsTo(3.0f);
		assertThat(ret.size(), is(400));
	}
	@Test
	public void testBeancachePropertyEqualsToCase004(){
		Set<Key<Bill,Integer>> ret = 
				BILL_CACHE.queryAll().
				property(BILL.amount).greaterThan(96.0f);
		assertThat(ret.size(), is(7500));
	}
	@Test
	public void testBeancachePropertyEqualsToCase004_(){
		Set<Key<Bill,Integer>> ret = 
				BILL_CACHE.queryAll().
				property(BILL.amount).greaterThan(96.0f);
		assertThat(ret.size(), is(7500));
	}
	@Test
	public void testBeancachePropertyEqualsToCase005(){
		BeancacheQuery<Bill,Integer> q = BILL_CACHE.queryAll();
		Set<Key<Bill,Integer>> ret = q. 
				intersect(q.property(BILL.amount).greaterThan(96.0f)).
				intersect(q.property(BILL.amount).greaterThan(101.0f)).
				intersect(q.property(BILL.amount).greaterThan(103.0f)).
				asKeySet();
		assertThat(ret.size(), is(1500));
	}
	
	@Test
	public void testBeancachePropertyEqualsToCase006(){
		Set<Key<Bill,Integer>> ret = 
				BILL_CACHE.queryAll().
				key(PERSON).equalsTo(99L);
		assertThat(ret.size(), is(1000));
	}
	
	private static BeancacheQuery<Bill, Integer> TEMP;
	@Test
	public void testBeancachePropertyEqualsToCase007_001(){
		TEMP = BILL_CACHE.queryAll();
		TEMP.intersect(TEMP.key(PERSON).equalsTo(99L));
		assertThat(TEMP.asKeySet().size(), is(1000));
	}
	
	@Test
	public void testBeancachePropertyInCase001(){
		BeancacheQuery<Bill, Integer> q = BILL_CACHE.queryAll();
		@SuppressWarnings("unchecked")
		Set<Key<Bill,Integer>> ret = q. 
				intersect(
						BeancacheQuery.createLinkedHashSetForUnion(
								q.property(BILL.amount).equalsTo(0.0f),
								q.property(BILL.amount).equalsTo(29.0f)
								)
						).
				asKeySet();
		assertThat(ret.size(), is(1100));
	}

	@Test
	public void testBeancachePropertyEqualsToCase007_002(){
		BeancacheQuery<Bill, Integer> q = BILL_CACHE.queryAll();
		@SuppressWarnings("unchecked")
		Set<Key<Bill,Integer>> ret = q.
				intersect(
						BeancacheQuery.createLinkedHashSetForUnion(
								q.property(BILL.amount).equalsTo(100.0f),
								q.property(BILL.amount).equalsTo(2.0f),
								q.property(BILL.amount).equalsTo(76.0f)
								)
						).
				asKeySet();
		assertThat(ret.size(), is(2200));
	}
	
	@Test
	public void testBeancachePropertyNotEqualsToCase(){
		BeancacheQuery<Bill, Integer> q = BILL_CACHE.queryAll();
		Set<Key<Bill,Integer>> ret = q. 
				defferent(q.property(BILL.amount).equalsTo(105.0f)).
				asKeySet();
		assertThat(ret.size(), is(99600));
	}
	
	@Test
	public void testBeancacheIndexMatcherCase001(){
		BeancacheQuery<Account, String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account, String>> ret = q. 
			key().match(BeancacheIndexMatcher.Matchers.endWith("001.com"));
		assertThat(ret.size(), is(100));
	}
	
	@Test
	public void testBeancacheIndexMatcherCase002(){
		BeancacheQuery<Account, String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account, String>> ret = q. 
			property(ACCOUNT.address).match(
					BeancacheIndexMatcher.Matchers.startWith("00000")
					);
		assertThat(ret.size(), is(1*ACCOUNT_SIZE));
	}

	@Test
	public void testBeancacheIndexMatcherCase003(){
		BeancacheQuery<Account, String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account, String>> ret = q. 
			property(ACCOUNT.address).match(
					BeancacheIndexMatcher.Matchers.contain("00st")
					);
		assertThat(ret.size(), is(1*ACCOUNT_SIZE));
	}
	@Test
	public void testBeancacheIndexMatcherCase004(){
		BeancacheQuery<Account, String> q = ACCOUNT_CACHE.queryAll();
		Set<Key<Account, String>> ret = q. 
			property(ACCOUNT.address).match(
					BeancacheIndexMatcher.Matchers.contain("st.001")
					);
		assertThat(ret.size(), is(10*ACCOUNT_SIZE));
	}
}
/*
 *
		Map<Float, Integer> c = new HashMap<Float, Integer>();
		for (Bill b : BILL_CACHE.values())
			if (c.containsKey(b.getAmount()))
				c.put(b.getAmount(), c.get(b.getAmount())+1);
			else
				c.put(b.getAmount(), 1);
		for (float f : c.keySet())
			System.out.println(f+":"+c.get(f));
--------
0.0:100
15.0:1000
29.0:1000
3.0:400
57.0:1000
77.0:1000
40.0:1000
92.0:1000
31.0:1000
103.0:600
14.0:1000
61.0:1000
69.0:1000
84.0:1000
44.0:1000
49.0:1000
25.0:1000
13.0:1000
76.0:1000
32.0:1000
93.0:1000
53.0:1000
102.0:700
12.0:1000
27.0:1000
36.0:1000
68.0:1000
85.0:1000
56.0:1000
2.0:300
41.0:1000
94.0:1000
11.0:1000
21.0:1000
79.0:1000
60.0:1000
101.0:800
86.0:1000
45.0:1000
23.0:1000
71.0:1000
10.0:1000
108.0:100
48.0:1000
95.0:1000
17.0:1000
9.0:1000
78.0:1000
33.0:1000
52.0:1000
100.0:900
8.0:900
19.0:1000
87.0:1000
37.0:1000
70.0:1000
73.0:1000
42.0:1000
88.0:1000
28.0:1000
107.0:200
59.0:1000
65.0:1000
80.0:1000
46.0:1000
30.0:1000
99.0:1000
6.0:700
63.0:1000
72.0:1000
34.0:1000
89.0:1000
106.0:300
51.0:1000
24.0:1000
1.0:200
38.0:1000
64.0:1000
81.0:1000
55.0:1000
98.0:1000
7.0:800
26.0:1000
43.0:1000
90.0:1000
75.0:1000
20.0:1000
58.0:1000
105.0:400
82.0:1000
4.0:500
47.0:1000
22.0:1000
67.0:1000
62.0:1000
97.0:1000
16.0:1000
91.0:1000
74.0:1000
35.0:1000
104.0:500
50.0:1000
18.0:1000
83.0:1000
39.0:1000
5.0:600
66.0:1000
54.0:1000
96.0:1000
 */