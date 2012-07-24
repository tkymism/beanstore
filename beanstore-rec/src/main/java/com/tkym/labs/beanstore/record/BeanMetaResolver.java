package com.tkym.labs.beanstore.record;

import java.util.ArrayList;
import java.util.List;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanmeta.PropertyMeta;
import com.tkym.labs.record.RecordKey;
import com.tkym.labs.record.TableMeta;
import com.tkym.labs.record.RecordKey.RecordKeyBuilder;
import com.tkym.labs.record.TableMeta.ColumnMetaBuilder;
import com.tkym.labs.record.TableMeta.TableMetaBuilder;

class BeanMetaResolver<B,K> {
	private final TableMeta tableMeta;
	private KeyConverterByRecordKey<B,K> converter;
	
	BeanMetaResolver(BeanMeta<B,K> beanMeta){
		tableMeta = buildTableFactory(beanMeta).create();
		converter = new KeyConverterByRecordKey<B,K>(beanMeta);
	}
	
	public TableMeta getTableMeta() {
		return tableMeta;
	}
	
	public KeyConverterByRecordKey<B,K> getKeyConverter(){
		return this.converter;
	}
	
	public RecordKeyBuilder createRecordKeyBuilder(Key<?, ?> parent){
		return new RecordKeyBuilder(tableMeta, new AncestorKeyStack(parent).getKeyValuesArray());
	}
	
	static class AncestorKeyStack{
		private final List<Key<?,?>> keyList = new ArrayList<Key<?,?>>(); 
		AncestorKeyStack(Key<?,?> key){
			if (key != null)
				buildKeyList(key, keyList);
		}
		private void buildKeyList(Key<?,?> key, List<Key<?,?>> keyList){
			if (key.getParent() != null) buildKeyList(key.getParent(), keyList);
			keyList.add(key);
		}
		List<Object> getValueList(){
			List<Object> list = new ArrayList<Object>(keyList.size());
			for (Key<?,?> key : keyList) list.add(key.value());
			return list;
		}
		Object[] getKeyValuesArray(){
			List<Object> parentKeyValues = getValueList();
			Object[] parentKeyValuesArray = new Object[parentKeyValues.size()];
			parentKeyValues.toArray(parentKeyValuesArray);
			return parentKeyValuesArray;
		}
		List<BeanMeta<?,?>> getBeanMetaArray(){
			List<BeanMeta<?,?>> list = new ArrayList<BeanMeta<?,?>>(keyList.size());
			for (Key<?,?> key : keyList) list.add(key.getBeanMeta());
			return list;
		}
	}
	
	class KeyConverterByRecordKey<BT,KT>{
		private final PropertyMeta<?, ?>[] keyMetas;
		private final BeanMeta<?,?>[] beanMetas;
		KeyConverterByRecordKey(BeanMeta<BT,KT> beanMeta){
			keyMetas = createRecordKeyMetaArrays(beanMeta);
			beanMetas = createAncestorBeanMetaArrays(beanMeta);
		}
		@SuppressWarnings("unchecked")
		Key<BT,KT> convert(RecordKey recordKey){
			Key<?, ?> key = null;
			for (int i=0; i<keyMetas.length; i++){
				Object value = recordKey.value(keyMetas[i].getPropertyName());
				key = createKeyUnsafe(beanMetas[i], key, value);
			}
			return (Key<BT,KT>) key;
		}
		@SuppressWarnings("unchecked")
		<CBT,CKT> Key<CBT,CKT> createKeyUnsafe(BeanMeta<?,?> beanMeta, Key<?,?> parent, Object value){
			return (Key<CBT,CKT>) ((BeanMeta<CBT,CKT>) beanMeta).key(parent, (CKT)value);
		}
	}
	
	static <BT, KT> TableMetaFactory buildTableFactory(BeanMeta<BT, KT> beanMeta){
		String tableName = beanMeta.getName();
		PropertyMeta<?, ?>[] keyMetas = createRecordKeyMetaArrays(beanMeta); 
		PropertyMeta<?, ?>[] columnMetas = createColumnsMetaArray(beanMeta); 
		return new TableMetaFactory(tableName, keyMetas, columnMetas);
	}
	
	private static PropertyMeta<?,?>[] createRecordKeyMetaArrays(BeanMeta<?,?> beanMeta){
		List<PropertyMeta<?, ?>> primarykeyMetaList = createKeyPropertyMetaList(beanMeta); 
		int primaryKeyLevelSize = primarykeyMetaList.size();
		PropertyMeta<?, ?>[] keyMetas = new PropertyMeta<?, ?>[primaryKeyLevelSize];
		primarykeyMetaList.toArray(keyMetas);
		return keyMetas;
	}
	
	private static List<PropertyMeta<?,?>> createKeyPropertyMetaList(BeanMeta<?,?> beanMeta){
		BeanMeta<?,?> thisMeta = beanMeta;
		List<BeanMeta<?,?>> beanlist = createAncestorBeanMetaList(beanMeta);
		List<PropertyMeta<?,?>> propertyList = new ArrayList<PropertyMeta<?,?>>(beanlist.size());
		for(BeanMeta<?,?> meta : beanlist) {
			PropertyMeta<?, ?> keyMeta = meta.getKeyPropertyMeta();
			keyMeta.rename(thisMeta.parentKeyProperty(meta));
			propertyList.add(keyMeta);
		}
		return propertyList;
	}
	
	private static BeanMeta<?,?>[] createAncestorBeanMetaArrays(BeanMeta<?,?> beanMeta){
		List<BeanMeta<?,?>> list = createAncestorBeanMetaList(beanMeta);
		BeanMeta<?,?>[] beanMetaArray = new BeanMeta<?,?>[list.size()];
		list.toArray(beanMetaArray);
		return beanMetaArray;
	}
	
	private static List<BeanMeta<?,?>> createAncestorBeanMetaList(BeanMeta<?,?> beanMeta){
		List<BeanMeta<?,?>> list = new ArrayList<BeanMeta<?,?>>();
		if(beanMeta.parent() != null)
			list.addAll(createAncestorBeanMetaList(beanMeta.parent()));
		list.add(beanMeta);
		return list;
	}
	
	private static PropertyMeta<?,?>[] createColumnsMetaArray(BeanMeta<?,?> beanMeta){
		List<PropertyMeta<?,?>> columnsMetaList = new ArrayList<PropertyMeta<?,?>>();
		for(String propertyName : beanMeta.getPropertyNames())
			if(!beanMeta.getKeyPropertyMeta().getPropertyName().equals(propertyName))
				columnsMetaList.add(beanMeta.getPropertyMeta(propertyName));
		PropertyMeta<?, ?>[] columnMetas = new PropertyMeta<?, ?>[columnsMetaList.size()];
		columnsMetaList.toArray(columnMetas);
		return columnMetas;
	}
	
	static class TableMetaFactory{
		private final String tableName;
		private final PropertyMeta<?, ?>[] keyMeta;
		private final PropertyMeta<?, ?>[] columnMeta;
		
		private TableMetaFactory(String tableName, PropertyMeta<?, ?>[] keyMeta, PropertyMeta<?, ?>[] columnMeta){
			this.tableName = tableName;
			this.keyMeta = keyMeta;
			this.columnMeta = columnMeta;
		}
		
		TableMeta create(){
			TableMetaBuilder builder = TableMeta.table(this.tableName);
			for(PropertyMeta<?, ?> meta : keyMeta)
				builder = resolveUnsafety(builder.key(meta.getPropertyName()), meta);
			for(PropertyMeta<?, ?> meta : columnMeta)
				builder = resolveUnsafety(builder.column(meta.getPropertyName()), meta);
			return builder.meta();
		}
		
		private TableMetaBuilder resolveUnsafety(ColumnMetaBuilder columnMetaBuilder, PropertyMeta<?, ?> propertyMeta){
			return resolve(columnMetaBuilder, propertyMeta.getPropertyType());
		}
		
		private <PT> TableMetaBuilder resolve(ColumnMetaBuilder columnMetaBuilder, Class<PT> propertyType){
			if(propertyType.equals(String.class))       return columnMetaBuilder.asString();
			else if(propertyType.equals(Integer.class)) return columnMetaBuilder.asInteger();
			else if(propertyType.equals(Long.class))    return columnMetaBuilder.asLong();
			else if(propertyType.equals(Short.class))   return columnMetaBuilder.asShort();
			else if(propertyType.equals(Byte.class))    return columnMetaBuilder.asByte();
			else if(propertyType.equals(Double.class))  return columnMetaBuilder.asDouble();
			else if(propertyType.equals(Float.class))   return columnMetaBuilder.asFloat();
			else if(propertyType.equals(Boolean.class)) return columnMetaBuilder.asBoolean();
			else throw new IllegalArgumentException("unsuport type "+propertyType.getName());
		}
	}
}