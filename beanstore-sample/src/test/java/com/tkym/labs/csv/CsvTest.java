package com.tkym.labs.csv;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.tkym.labs.beanmeta.csv.CsvBeanConverter;
import com.tkym.labs.beanmeta.csv.CsvBeanConverterBuilder;
import com.tkym.labs.beanmeta.csv.CsvDataLoader;
import com.tkym.labs.uke.Iyakuhin;
import com.tkym.labs.uke.IyakuhinMeta;

public class CsvTest {
	static final String COMMA = ",";
	
	@Test
	public void testCsvLoad() throws Exception{
		IyakuhinMeta IYAKUHIN = IyakuhinMeta.get();
		CsvBeanConverterBuilder<Iyakuhin, Integer> builder = new CsvBeanConverterBuilder<Iyakuhin, Integer>(IYAKUHIN);
		CsvBeanConverter<Iyakuhin, Integer> converter = builder.
			property(IYAKUHIN.henkouKubun).
			property(IYAKUHIN.syubetsu).
			property(IYAKUHIN.iyakuhinCode).
			property(IYAKUHIN.kanjiKetasuu).
			property(IYAKUHIN.kanjiMeisyou).
			property(IYAKUHIN.kanaKetasuu).
			property(IYAKUHIN.kanaName).
			property(IYAKUHIN.taniCode).
			property(IYAKUHIN.taniKetasuu).
			property(IYAKUHIN.taniName).
			property(IYAKUHIN.kingakuSyubetsu).
			property(IYAKUHIN.kingaku).
			property(IYAKUHIN.yobi1).
			property(IYAKUHIN.mayaku).
			property(IYAKUHIN.shinkei).
			property(IYAKUHIN.seibutsu).
			property(IYAKUHIN.kouhatsu).
			property(IYAKUHIN.yobi2).
			property(IYAKUHIN.shikaTokutei).
			property(IYAKUHIN.zoueizai).
			property(IYAKUHIN.youryou).
			property(IYAKUHIN.syuusai).
			property(IYAKUHIN.syouhinmeiKanren).
			property(IYAKUHIN.kyuuKingakuSyubetsu).
			property(IYAKUHIN.kyuuKingaku).
			property(IYAKUHIN.kanjiHenkou).
			property(IYAKUHIN.kanaHenkou).
			property(IYAKUHIN.zaikei).
			property(IYAKUHIN.yobi3).
			property(IYAKUHIN.henkouDate).
			property(IYAKUHIN.haishiDate).
			property(IYAKUHIN.yakkaKijunCode).
			property(IYAKUHIN.kouhyouJyunjo).
			property(IYAKUHIN.keikasochi).
			converter();
		
		InputStream istream = Iyakuhin.class.getResourceAsStream("y.csv");
		if (istream == null) fail();
		
		List<Iyakuhin> iyakuhinList = 
				new CsvDataLoader<Iyakuhin,Integer>(istream, converter).asList();
		assertThat(iyakuhinList.size(), is(18597));
	}
}
