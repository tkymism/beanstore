package com.tkym.labs.beancache;

import java.util.Comparator;

class NullComparableValue<T extends Comparable<T>>{
	final T value; 
	NullComparableValue(T value) {
		this.value = value;
	}
	static class NullComparableValueComparator<T extends Comparable<T>> implements Comparator<NullComparableValue<T>>{
		private boolean isNullFirst = false;
		NullComparableValueComparator<T> nullFirst(){
			this.isNullFirst = true;
			return this;
		}
		NullComparableValueComparator<T> nullLast(){
			this.isNullFirst = false;
			return this;
		}
		@Override
		public int compare(NullComparableValue<T> o1, NullComparableValue<T> o2) {
			if (o1.value != null && o2.value != null)
				return o1.value.compareTo(o2.value);
			else if (o1.value == null && o2.value != null)
				if (isNullFirst) 	return -1;
				else 				return 1;
			else if (o1.value != null && o2.value == null)
				if (isNullFirst) 	return 1;
				else 				return -1;
			else
				return 0;
		}
	}
}