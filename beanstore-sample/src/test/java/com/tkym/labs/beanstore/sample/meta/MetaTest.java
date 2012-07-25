package com.tkym.labs.beanstore.sample.meta;


public class MetaTest {
	public enum DataType{
		NUMERIC,STRING
	}
	public static enum UkeMeta{
		N1(DataType.NUMERIC,1,false),
		N2(DataType.NUMERIC,2,false),
		N3(DataType.NUMERIC,3,false),
		N4(DataType.NUMERIC,4,false),
		N5(DataType.NUMERIC,5,false),
		N6(DataType.NUMERIC,6,false),
		N7(DataType.NUMERIC,7,false),
		N8(DataType.NUMERIC,8,false),
		N9(DataType.NUMERIC,9,false),
		X1(DataType.STRING,1,false),
		X2(DataType.STRING,2,false),
		X3(DataType.STRING,3,false),
		X4(DataType.STRING,4,false),
		X5(DataType.STRING,5,false),
		X6(DataType.STRING,6,false),
		X7(DataType.STRING,7,false),
		X8(DataType.STRING,8,false),
		X9(DataType.STRING,9,false),
		;
		private final DataType type;
		private final int length;
		private final boolean variable;
		UkeMeta(DataType type, int length, boolean variable){
			this.type = type;
			this.length = length;
			this.variable = variable;
		}
		public DataType getType() {
			return type;
		}
		public int getLength() {
			return length;
		}
		public boolean isVariable() {
			return variable;
		}
	}
}
