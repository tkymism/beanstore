package com.tkym.labs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanstoreException;
import com.tkym.labs.beanstore.record.BeanstoreRootServiceRecord;
import com.tkym.labs.beanstore.record.BeanstoreServiceRecordFactory;
import com.tkym.labs.record.SqliteRecordstoreRepository;
import com.tkym.labs.uke.Iyakuhin;
import com.tkym.labs.uke.IyakuhinMeta;

public class IyakuhinTest {
	private static BeanstoreRootServiceRecord service;
	private static IyakuhinMeta IYAKUHIN = IyakuhinMeta.get();
//	@BeforeClass
	public static void setupClass() throws Exception{
		service = new BeanstoreServiceRecordFactory(SqliteRecordstoreRepository.inMemory()).create();
	}
	
//	@AfterClass
	public static void teardownClass() throws Exception {
//		service.drop(IYAKUHIN);
		service.getTransaction().close();
	}
	
//	@Test
	public void prepareIyakuhin() throws BeanstoreException{
		List<Iyakuhin> list =  CsvDataProvider.get().iyakuhinList();
		for (Iyakuhin bean : list)
			service.store(IYAKUHIN).put(bean);
		service.getTransaction().commit();
	}
	
//	@Test
	public void testCase001() throws BeanstoreException{
		Iterator<Key<Iyakuhin,Integer>> ite = 
				service.query(IyakuhinMeta.get()).
				filter(IYAKUHIN.kouhatsu).equalsTo(0).
				filter(IYAKUHIN.syubetsu).startsWith("aaa").
				sort(IYAKUHIN.yakkaKijunCode).asc().
//				filter(IYAKUHIN.kanjiMeisyou).startsWith("グラニセトロン点滴静注液３ｍｇ").
//				filter(IYAKUHIN.kanjiMeisyou).startsWith("ドネペジル塩酸塩錠").
//				filter(IYAKUHIN.kanjiMeisyou).startsWith("ファ").
				key().asIterator();
		
		int count = 0;
		while(ite.hasNext()){
			Iyakuhin senpatsu = service.store(IYAKUHIN).get(ite.next().value());
			if (senpatsu.getYakkaKijunCode().equals("")) continue;
			String yjCode = senpatsu.getYakkaKijunCode().substring(0,9);
			
			Iterator<Iyakuhin> kouhatsuIte = service.query(IyakuhinMeta.get())
					.filter(IYAKUHIN.yakkaKijunCode).startsWith(yjCode)
					.sort(IYAKUHIN.yakkaKijunCode).asc()
					.bean().asIterator();
			
			while(kouhatsuIte.hasNext()){
				Iyakuhin kouhatsu = kouhatsuIte.next();
				if (senpatsu.getIyakuhinCode() != kouhatsu.getIyakuhinCode())
					System.out.println(
						kouhatsu.getIyakuhinCode()+","+senpatsu.getIyakuhinCode()+","+
						kouhatsu.getYakkaKijunCode()+","+senpatsu.getYakkaKijunCode()+","+
						kouhatsu.getKingaku()+","+senpatsu.getKingaku()+","+
						kouhatsu.getKanjiMeisyou()+","+senpatsu.getKanjiMeisyou()+"");
				else
					System.out.print(kouhatsu.getIyakuhinCode()+","+"");
			}
			count++;
			if (count > 100)
				break;
		}
	}
	
//	@Test
	public void testCase002() throws BeanstoreException{
		Iterator<Key<Iyakuhin,Integer>> ite = 
				service.query(IyakuhinMeta.get()).
				filter(IYAKUHIN.kouhatsu).equalsTo(0).
				sort(IYAKUHIN.yakkaKijunCode).asc().
				key().asIterator();
		
		while(ite.hasNext()){
			Iyakuhin senpatsu = service.store(IYAKUHIN).get(ite.next().value());
			if (senpatsu.getYakkaKijunCode().equals("")) continue;
			String yjCode = senpatsu.getYakkaKijunCode().substring(0,9);
			GenericModel model = new GenericModel(senpatsu); 
			
			Iterator<Iyakuhin> kouhatsuIte = 
					service.query(IyakuhinMeta.get()).
					filter(IYAKUHIN.yakkaKijunCode).startsWith(yjCode).
					filter(IYAKUHIN.kouhatsu).equalsTo(1).
					filter(IYAKUHIN.yakkaKijunCode).notEquals(senpatsu.getYakkaKijunCode()).
					sort(IYAKUHIN.yakkaKijunCode).asc().
					bean().asIterator();
			
			while(kouhatsuIte.hasNext())
				model.addGeneric(kouhatsuIte.next());
			
			if (model.genericCount() == 0) continue;
			
			List<String> resultData = new ArrayList<String>();
			resultData.add(Integer.toString(model.getSenpatsu().getIyakuhinCode()));
			resultData.add(model.getSenpatsu().getYakkaKijunCode());
			resultData.add(model.getSenpatsu().getKanjiMeisyou());
			resultData.add(Double.toString(model.getSenpatsu().getKingaku()));
			resultData.add(Integer.toString(model.genericCount()));
			resultData.add(Double.toString(model.inexpencive()));
			
			boolean first = true;
			StringBuilder sb = new StringBuilder();
			for (String element : resultData){
				if(first) first = false;
				else sb.append(",");
				sb.append("\"");
				sb.append(element);
				sb.append("\"");
			}
			
			System.out.println(sb.toString());
		}
	}
}