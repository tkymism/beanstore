package com.tkym.labs;

import java.util.List;

import com.tkym.labs.beanmeta.csv.CsvBeanConverterBuilder;
import com.tkym.labs.beanmeta.csv.CsvConverterException;
import com.tkym.labs.beanmeta.csv.CsvDataLoader;
import com.tkym.labs.uke.Iyakuhin;
import com.tkym.labs.uke.IyakuhinMeta;

public class CsvDataProvider {
	private static final CsvDataProvider singleton = new CsvDataProvider();
	private List<Iyakuhin> iyakuhinList;
	private CsvDataProvider(){}
	public static CsvDataProvider get(){ return singleton; }
	public List<Iyakuhin> iyakuhinList(){
		if (iyakuhinList == null)
			try {
				iyakuhinList = createIyakuhin().charset("MS932").asList();
			} catch (Exception e) {
				throw new CsvConverterException("exception on iyakuhinList", e);
			}
		return iyakuhinList;
	}
	
	private CsvDataLoader<Iyakuhin, Integer> createIyakuhin(){
		return new CsvDataLoader<Iyakuhin, Integer>(
				Iyakuhin.class.getResourceAsStream("y.csv"), 
					new CsvBeanConverterBuilder<Iyakuhin,Integer>(IyakuhinMeta.get()).buildAsAllProperty());
	}
	
	@SuppressWarnings("serial")
	class CsvDataLoadException extends RuntimeException{
		CsvDataLoadException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
