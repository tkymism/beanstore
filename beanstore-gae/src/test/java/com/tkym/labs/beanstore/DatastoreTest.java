package com.tkym.labs.beanstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DatastoreTest {
	
	private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    @Test
    public void testCase(){
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	int count = ds.prepare(new Query("sample")).countEntities(FetchOptions.Builder.withLimit(10));
    	assertThat(count, is(0));
    	ds.put(new Entity("sample"));
    	ds.put(new Entity("sample"));
    	int count2 = ds.prepare(new Query("sample")).countEntities(FetchOptions.Builder.withLimit(10));
    	assertThat(count2, is(2));
    }

    @Test
    public void testCase002(){
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	int count = ds.prepare(new Query("sample")).countEntities(FetchOptions.Builder.withLimit(10));
    	assertThat(count, is(0));
    	ds.put(new Entity("sample"));
    	ds.put(new Entity("sample"));
    	int count2 = ds.prepare(new Query("sample")).countEntities(FetchOptions.Builder.withLimit(10));
    	assertThat(count2, is(2));
    }
}