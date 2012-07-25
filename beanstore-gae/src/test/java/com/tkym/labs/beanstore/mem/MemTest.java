package com.tkym.labs.beanstore.mem;

import org.junit.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemTest {
	
	@Test
	public void testMem(){
		MemcacheService service = MemcacheServiceFactory.getMemcacheService();
		
	
	}
}