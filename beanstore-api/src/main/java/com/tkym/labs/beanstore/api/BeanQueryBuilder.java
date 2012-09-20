package com.tkym.labs.beanstore.api;
import static com.tkym.labs.beanstore.api.BeanQueryUtils.property;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.PropertyMeta;

public class BeanQueryBuilder<BT,KT> {
	private final BeanQuerySource<BT,KT> querySource;
	private BeanQueryBuilder(BeanMeta<BT, KT> meta){
		this.querySource = new BeanQuerySource<BT, KT>(meta);
	}
	public static <BT,KT> BeanQueryBuilder<BT,KT> create(BeanMeta<BT, KT> meta){
		return new BeanQueryBuilder<BT,KT>(meta);
	}
	public <PT> BeanFilterBuildHelper<BT,KT,PT> filter(PropertyMeta<BT, PT> propertyMeta){
		return new BeanFilterBuildHelper<BT,KT,PT>(this, propertyMeta);
	}
	public BeanQueryBuilder<BT,KT> filter(BeanFilterCriteria filter){	
		querySource.addFilter(filter);
		return this;
	}
	public <PT> BeanSortBuildHelper<BT,KT,PT> sort(PropertyMeta<BT, PT> propertyMeta){
		return new BeanSortBuildHelper<BT,KT,PT>(this, propertyMeta);
	}
	public BeanQueryBuilder<BT,KT> sort(BeanSortCriteria criteria){	
		querySource.addSort(criteria);
		return this;
	}
	public BeanQuerySource<BT, KT> getQuerySource(){
		return this.querySource;
	}
	public static class BeanSortBuildHelper<BT,KT,PT>{
		private final BeanQueryBuilder<BT,KT> builder;
		private final PropertyMeta<BT, PT> propertyMeta;
		private BeanSortBuildHelper(BeanQueryBuilder<BT, KT> builder,
				PropertyMeta<BT, PT> propertyMeta) {
			this.builder = builder;
			this.propertyMeta = propertyMeta;
		}
		public BeanQueryBuilder<BT,KT> asc(){
			return this.builder.sort(property(propertyMeta).asc());
		}
		public BeanQueryBuilder<BT,KT> desc(){
			return this.builder.sort(property(propertyMeta).desc());
		}
	}
	public static class BeanFilterBuildHelper<BT,KT,PT>{
		private final BeanQueryBuilder<BT,KT> builder;
		private final PropertyMeta<BT, PT> propertyMeta;
		private BeanFilterBuildHelper(BeanQueryBuilder<BT,KT> builder, PropertyMeta<BT, PT> propertyMeta){
			this.builder = builder;
			this.propertyMeta = propertyMeta;
		}
		public BeanQueryBuilder<BT,KT> equalsTo(PT value){
			return builder.filter(property(propertyMeta).equalsTo(value));
		}
		public BeanQueryBuilder<BT,KT> greaterThan(PT value){
			return builder.filter(property(propertyMeta).greaterThan(value));
		}
		public BeanQueryBuilder<BT,KT> greaterEqual(PT value){
			return builder.filter(property(propertyMeta).greaterEqual(value));
		}
		public BeanQueryBuilder<BT,KT> lessThan(PT value){
			return builder.filter(property(propertyMeta).lessThan(value));
		}
		public BeanQueryBuilder<BT,KT> lessEqual(PT value){
			return builder.filter(property(propertyMeta).lessEqual(value));
		}
		public BeanQueryBuilder<BT,KT> notEquals(PT value){
			return builder.filter(property(propertyMeta).notEquals(value));
		}
		public BeanQueryBuilder<BT,KT> in(PT... value){
			return builder.filter(property(propertyMeta).in(value));
		}
		public BeanQueryBuilder<BT,KT> startsWith(PT value){
			return builder.filter(property(propertyMeta).startsWith(value));
		}
		public BeanQueryBuilder<BT,KT> endsWith(PT value){
			return builder.filter(property(propertyMeta).endsWith(value));
		}
		public BeanQueryBuilder<BT,KT> contains(PT value){
			return builder.filter(property(propertyMeta).contains(value));
		}
	}
}
