package com.tkym.labs.beanstore;

import static com.tkym.labs.beanstore.api.BeanQueryUtils.property;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beancache.BeancacheIndex;
import com.tkym.labs.beancache.BeancacheQuery;
import com.tkym.labs.beancache.BeancacheRepository;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.KeyBuilder;
import com.tkym.labs.beans.Account;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.Bill;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.Person;
import com.tkym.labs.beans.PersonMeta;
import com.tkym.labs.beanstore.api.BeanFilter;
import com.tkym.labs.beanstore.api.BeanFilterCriteria;

public class BeancacheFilterCriteriaProcesserTest {
	private static final int PERSON_SIZE = 100;
	private static final int ACCOUNT_SIZE = 10;
	private static final int BILL_SIZE = 100;
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	private static final DecimalFormat PF = new DecimalFormat("00000");
	private static final DecimalFormat AF = new DecimalFormat("000");
	private static final BeancacheRepository REPO = new BeancacheRepository();
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
	@BeforeClass
	public static void setupClass(){
		for (int i=0; i<PERSON_SIZE; i++){
			Person p = person(i);
			Key<Person, Long> pk = 
					KeyBuilder.root().
					meta(PERSON).is(p.getId()).
					build(); 
			REPO.get(PERSON).put(pk, p);
			for (int j=0; j<ACCOUNT_SIZE; j++){
				Account a = account(i,j);
				Key<Account, String> ak = 
						KeyBuilder.parent(pk).
						meta(ACCOUNT).is(a.getEmail()).
						build();
				REPO.get(ACCOUNT).put(ak, a);
				for (int k=0; k<BILL_SIZE; k++){
					Bill b = bill(i, j, k);
					Key<Bill, Integer> bk =
							KeyBuilder.parent(ak).
							meta(BILL).is(b.getNo()).
							build();
					REPO.get(BILL).put(bk, b);
				}
			}
		}
	}
	
	static <BT,KT extends Comparable<KT>> 
		BeancacheFilterCriteriaProcesser<BT,KT> processer(BeanMeta<BT, KT> beanMeta){
		return new BeancacheFilterCriteriaProcesser<BT,KT>(REPO.get(beanMeta).query()); 
	}
	
	@Test
	public void testEqualsCase001(){
		Set<Key<Person, Long>> r = 
				processer(PERSON).
				filters(property(PERSON.name).equalsTo("a00001")).
				asKeySet();
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testEqualsCase002(){
		Set<Key<Person, Long>> r = 
				processer(PERSON).
				filters(property(PERSON.name).lessThan("a00001")).
				asKeySet();
		assertThat(r.size(), is(1));
	}
	
	@Test
	public void testEqualsCase003(){
		Set<Key<Person, Long>> r = 
				processer(PERSON).
				filters(property(PERSON.name).lessEqual("a00001")).
				asKeySet();
		assertThat(r.size(), is(2));
	}
	
	@Test
	public void testEqualsCase004(){
		Set<Key<Person, Long>> r = 
				processer(PERSON).
				filters(property(PERSON.name).greaterThan("a00001")).
				asKeySet();
		assertThat(r.size(), is(98));
	}
	
	@Test
	public void testEqualsCase005(){
		Set<Key<Person, Long>> r = 
				processer(PERSON).
				filters(property(PERSON.name).greaterEqual("a00001")).
				asKeySet();
		assertThat(r.size(), is(99));
	}
	
	static class BeancacheFilterCriteriaProcesser<BT, KT extends Comparable<KT>>{
		private final BeancacheQuery<BT, KT> beancacheIndex;
		BeancacheFilterCriteriaProcesser(BeancacheQuery<BT, KT> beancacheScaner) {
			this.beancacheIndex = beancacheScaner;
		}
		Set<Key<BT,KT>> asKeySet(){
			return this.beancacheIndex.keySet();
		}
		BeancacheQuery<BT, KT> scaner(){
			return this.beancacheIndex;
		}
		BeancacheFilterCriteriaProcesser<BT, KT> filters(List<BeanFilterCriteria> filters){
			for (BeanFilterCriteria criteria : filters) processFilter(criteria);
			return this;
		}
		BeancacheFilterCriteriaProcesser<BT, KT> filters(BeanFilterCriteria... filters){
			for (BeanFilterCriteria criteria : filters) processFilter(criteria);
			return this;
		}
		@SuppressWarnings("unchecked")
		BeancacheFilterCriteriaProcesser<BT, KT> processFilter(BeanFilterCriteria criteria){
			if (criteria instanceof BeanFilter)
				return processFilter((BeanFilter<BT, KT>) criteria);
			else
				throw new UnsupportedOperationException(
					"unsupport processFilter(BeanFilterCriteria criteria)");
		}
		/**
		START_WITH,
		END_WITH,
		CONTAIN
		 * @param filter
		 * @return
		 */
		<PT extends Comparable<PT>> BeancacheFilterCriteriaProcesser<BT, KT> processFilter(BeanFilter<BT,PT> filter){
			BeancacheIndex<BT, KT, PT> propertyIndex = 
				this.beancacheIndex.property(filter.getMeta());
			switch (filter.getOperator()) {
			case EQUAL: 
				propertyIndex.equalsTo(filter.value());
				return this;
			case NOT_EQUAL: 
				propertyIndex.notEqualsTo(filter.value());
				return this;
			case GREATER_THAN: 
				propertyIndex.greaterThan(filter.value());
				return this;
			case GREATER_THAN_OR_EQUAL: 
				propertyIndex.greaterEqual(filter.value());
				return this;
			case LESS_THAN: 
				propertyIndex.lessThan(filter.value());
				return this;
			case LESS_THAN_OR_EQUAL: 
				propertyIndex.lessEqual(filter.value());
				return this;
			case IN: 
				propertyIndex.in(filter.getValues());
				return this;
			default:
				throw new UnsupportedOperationException(
						"unsupport Operation:"+filter.getOperator());
			}
		}
	}
}
