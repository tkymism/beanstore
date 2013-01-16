package com.tkym.labs.beanstore;

import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.BeanMetaUtils;
import com.tkym.labs.beanmeta.ChainComparator;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.beanmeta.ChainComparator.ChainComparatorBuilder;
import com.tkym.labs.beans.AccountMeta;
import com.tkym.labs.beans.BillMeta;
import com.tkym.labs.beans.PersonMeta;

public class BeanmemTest {
	private static final PersonMeta PERSON = PersonMeta.get();
	private static final AccountMeta ACCOUNT = AccountMeta.get();
	private static final BillMeta BILL = BillMeta.get();
	
	@Test
	public void testBeanmemKeyValueMapCase001(){
	}
	
	class BeanmemRepository{
		private final ConcurrentHashMap<BeanMeta<?, ?>, BeanmemMap<?,?>> maps = 
			new ConcurrentHashMap<BeanMeta<?,?>, BeanmemMap<?,?>>();
		<BT, KT> BeanmemMap<KT,BT> meta(BeanMeta<BT, KT> beanMeta){
			@SuppressWarnings("unchecked")
			BeanmemMap<KT,BT> map = (BeanmemMap<KT,BT>) maps.get(beanMeta);
			if (map == null){
				map = new BeanmemMap<KT, BT>(beanMeta); 
				maps.put(beanMeta, map);
			}
			return map;
		}
	}
	
	static class BeanMetaComparator<BT,PT extends Comparable<PT>> implements Comparator<BT>{
		private final BeanMetaComparator<BT,?> parentComparator;
		private final PropertyMeta<BT, PT> propertyMeta;
		BeanMetaComparator(PropertyMeta<BT, PT> propertyMeta){
			this(null, propertyMeta);
		}
		BeanMetaComparator(BeanMetaComparator<BT, ?> parentComparator, PropertyMeta<BT, PT> propertyMeta){
			this.parentComparator = parentComparator;
			this.propertyMeta = propertyMeta;
		}
		@Override
		public int compare(BT o1, BT o2) {
			int ret = 0;
			if (parentComparator != null)
				ret = parentComparator.compare(o1, o2);
			if (ret == 0)
				ret = valueOf(o1).compareTo(valueOf(o2));
			return ret;
		}
		PT valueOf(BT bean){
			return propertyMeta.access(bean).get();
		}
	}
	static class BeanmemComparator<BT,KT> {
		static <BT,KT> BeanmemComparatorBuilder<BT,KT> meta(BeanMeta<BT, KT> beanMeta){
			return new BeanmemComparatorBuilder<BT,KT>(beanMeta);
		}
		static class BeanmemComparatorBuilder<BT,KT>{
			private final BeanMeta<BT, KT> beanMeta;
			private KeyComparator<BT,KT> keyComparator;
			private ChainComparatorBuilder<Beanmem<BT,KT>> builder = null;
			private BeanmemComparatorBuilder(BeanMeta<BT, KT> beanMeta){	
				this.beanMeta = beanMeta;
				this.keyComparator = new KeyComparator<BT,KT>(beanMeta);
			}
			<PT> BeanmemComparatorBuilder<BT,KT> property(PropertyMeta<BT, PT> propertyMeta){
				Comparator<BT> propertyComparator = 
						BeanMetaUtils.get().meta(propertyMeta).comparator();
				BeanComparator<BT,KT> beanComparator = 
						new BeanComparator<BT, KT>(beanMeta, propertyComparator);
				pushBuilder(beanComparator);
				return this;
			}
			ChainComparatorBuilder<Beanmem<BT,KT>> pushBuilder(Comparator<Beanmem<BT,KT>> comp){
				if (builder != null)
					builder = builder.chain(comp);
				else
					builder = ChainComparator.chain(comp);
				return builder;
			}
			ChainComparator<Beanmem<BT,KT>> comparator(){
				return pushBuilder(keyComparator).build();
			} 
		}
		static class BeanComparator<BT,KT> implements Comparator<Beanmem<BT,KT>>{
			private final Comparator<BT> comparator;
			private BeanComparator(BeanMeta<BT,KT> beanmeta, Comparator<BT> comparator){
				this.comparator = comparator;
			}
			@Override
			public int compare(Beanmem<BT, KT> o1, Beanmem<BT, KT> o2) {
				BT b1 = o1.getBean();
				BT b2 = o2.getBean();
				if (b1 == null && b2 == null) return 0;
				else if (b1 != null && b2 == null) return 1;
				else if (b1 == null && b2 != null) return -1;
				else return this.comparator.compare(b1,b2);
			}
		}
		static class KeyComparator<BT,KT> implements Comparator<Beanmem<BT,KT>>{
			private KeyComparator(BeanMeta<BT,KT> beanmeta){};
			@Override
			public int compare(Beanmem<BT, KT> o1, Beanmem<BT, KT> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		}
	}
	
	/**
	 * @author takayama
	 * @param <KT>
	 * @param <BT>
	 */
	static class BeanmemMap<KT,BT>{
		private final BeanMeta<BT, KT> beanMeta;
		private final ConcurrentHashMap<Key<BT, KT>, Beanmem<BT,KT>> hashMap;
		BeanmemMap(BeanMeta<BT, KT> beanMeta){
			this.beanMeta = beanMeta;
			hashMap = new ConcurrentHashMap<Key<BT,KT>, Beanmem<BT,KT>>();
		}
		BT get(Key<BT, KT> key){
			Beanmem<BT,KT> element = hashMap.get(key);
			if (element != null)
				return element.use().getBean();
			return null;
		}
		BT put(Key<BT, KT> key, BT bean){
			Beanmem<BT,KT> element = hashMap.get(key);
			if (element == null){
				hashMap.put(key, new Beanmem<BT,KT>(key, bean));
				return null;
			} else {
				return element.use().replaceBean(bean);
			}
		}
	}
	
	/**
	 * @author takayama
	 * @param <BT> type of bean.
	 * @param <KT> property type of key.
	 */
	static class Beanmem<BT,KT>{
		private BT bean;
		private Key<BT, KT> key;
		private final Date initialDate;
		private Date lastUseDate;
		private long useTime;
		Beanmem(Key<BT,KT>key, BT bean){
			this.key = key;
			this.bean = bean;
			this.initialDate = new Date();
			this.lastUseDate = (Date) this.initialDate.clone();
			useTime = 0L;
		}
		BT replaceBean(BT bean) {
			this.bean = bean;
			return this.bean;
		}
		BT getBean() {
			return bean;
		}
		Key<BT, KT> getKey() {
			return key;
		}
		Beanmem<BT,KT> use() {
			this.lastUseDate = new Date();
			this.useTime++;
			return this;
		}
		Date getInitialDate() {
			return initialDate;
		}
		Date getLastUseDate() {
			return lastUseDate;
		}
		long getUseTime() {
			return useTime;
		}
	}
}