package com.tkym.labs.beanstore.api;

import java.util.ArrayList;
import java.util.List;

import com.tkym.labs.beanmeta.BeanMeta;

public class BeanQuerySource<BT,KT>{
	private final BeanMeta<BT, KT> meta;
	private final List<BeanFilterCriteria> filterList = 
			new ArrayList<BeanFilterCriteria>();
	private final List<BeanSortCriteria> sortList = 
			new ArrayList<BeanSortCriteria>();
	BeanQuerySource(BeanMeta<BT, KT> meta){
		this.meta = meta;
	}
	void addFilter(BeanFilterCriteria criteria){
		filterList.add(criteria);
	}
	void addSort(BeanSortCriteria criteria){
		sortList.add(criteria);
	}
	public List<BeanFilterCriteria> filterList(){
		return filterList;
	}
	public List<BeanSortCriteria> sortList(){
		return sortList;
	}
	BeanMeta<BT, KT> getBeanMeta(){
		return meta;
	}
}