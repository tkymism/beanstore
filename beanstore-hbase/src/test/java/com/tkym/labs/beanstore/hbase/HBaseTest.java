package com.tkym.labs.beanstore.hbase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.AbstractBeanQueryExecutor;
import com.tkym.labs.beanstore.AbstractBeanstore;
import com.tkym.labs.beanstore.AbstractBeanstoreRootService;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class HBaseTest {
	private static final String PATH_OF_SITE_XML = 
			HBaseTest.class.getResource("hbase-site.xml").getPath();
	private static Configuration CONF;
	private static final String TABLE = "Person";
	private static final String HBASE_ZOOKEEPER_QUORUM = "hadoopmaster.iryo.tokyo.ssg.fujitsu.com";
	private static final String HBASE_MASTER = "10.56.128.145:60010";
	private static final String HBASE_CLIENT_PORT = "2181";
	
	@BeforeClass
	public static void setupClass() throws IOException{
		CONF = HBaseConfiguration.create();
		CONF.addResource(new Path(PATH_OF_SITE_XML));
		CONF.set("hbase.zookeeper.quorum", HBASE_ZOOKEEPER_QUORUM);
		CONF.set("hbase.master", HBASE_MASTER);
		CONF.set("hbase.zookeeper.property.clientPort",HBASE_CLIENT_PORT);
		HBaseAdmin admin = new HBaseAdmin(CONF); 
		HTableDescriptor tableDescripter = new HTableDescriptor(TABLE);
		tableDescripter.addFamily(new HColumnDescriptor("data"));
		admin.createTable(tableDescripter);
		admin.close();
	}
	
	@AfterClass
	public static void teardownClass() throws IOException{
		HBaseAdmin admin = new HBaseAdmin(CONF); 
		admin.disableTable(TABLE);
		admin.deleteTable(TABLE);
		admin.close();
	}
	
	@Test
	public void testHBaseCase001() throws IOException{
		Configuration conf = CONF;
		
		HTable hTable = new HTable(conf, "Person");
		
		byte[] family    = Bytes.toBytes("data");
		byte[] qualifier1 = Bytes.toBytes("c1");
		byte[] qualifier2 = Bytes.toBytes("c2");
		byte[] row1 = Bytes.toBytes("row1");
		byte[] row2 = Bytes.toBytes("row2");
        
        Put put1 = new Put(row1);
        put1.add(family, qualifier1, Bytes.toBytes("insert1"));
        hTable.put(put1);
        
        Put put2 = new Put(row2);
        put2.add(family, qualifier1, Bytes.toBytes("insert21"));
        put2.add(family, qualifier2, Bytes.toBytes("insert22"));
        hTable.put(put2);
        
        Get g = new Get(row1);
        Result r = hTable.get(g);
        List<KeyValue> values = r.getColumn(family, qualifier1);
        
        assertThat(values.size(), is(1));
        KeyValue kv1 = values.get(0);
        assertThat(new String(kv1.getValue()), is("insert1"));
        
        Get g2 = new Get(row2);
        Result r21 = hTable.get(g2);
        
        List<KeyValue> v2 = r21.getColumn(family, qualifier1);
        assertThat(v2.size(), is(1));
        KeyValue kv2 = v2.get(0);
        assertThat(new String(kv2.getValue()), is("insert21"));
        
        
        
        hTable.close();
	}
	
	private static final String PROPERTY_FAMILY_NAME = "p";
	
	@SuppressWarnings("serial")
	class BeanstoreHBaseException extends RuntimeException{
		BeanstoreHBaseException(Throwable t){
			super(t);
		}
	}
	class BeanstoreHBaseAdmin{
		private final HBaseAdmin admin;
		BeanstoreHBaseAdmin(HBaseAdmin admin){
			this.admin = admin;
		}
		<BT, KT extends Comparable<KT>> void create(BeanMeta<BT, KT> beanMeta){
			try {
				admin.createTable(createTableDescriptor(beanMeta));
			} catch (IOException e) {
				throw new BeanstoreHBaseException(e);
			}
		}
		<BT, KT extends Comparable<KT>> void delete(BeanMeta<BT, KT> beanMeta){
			try {
				admin.deleteTable(beanMeta.getName());
			} catch (IOException e) {
				throw new BeanstoreHBaseException(e);
			}
		}
		private <BT, KT extends Comparable<KT>> HTableDescriptor createTableDescriptor(BeanMeta<BT,KT> beanMeta){
			HTableDescriptor desc = new HTableDescriptor(beanMeta.getName());
			HColumnDescriptor family = new HColumnDescriptor(PROPERTY_FAMILY_NAME);
			desc.addFamily(family);
			return desc;
		}
	}
	class BeanMetaHbase<BT, KT extends Comparable<KT>>{
		private final HTable htable;
		private final BeanMeta<BT, KT> beanmeta;
		BeanMetaHbase(HTable htable, BeanMeta<BT,KT> beanmeta){
			this.htable = htable;
			this.beanmeta = beanmeta;
		}
	}
	class BeanstoreRootServiceHBase extends AbstractBeanstoreRootService{
		@Override
		public BeanstoreTransaction transaction() {
			// TODO
			return null;
		}
		@Override
		protected <BT, KT extends Comparable<KT>> AbstractBeanstore<BT, KT> createBeanstore(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return new BeanstoreHBase<BT, KT>(this, beanMeta, parent);
		}
		@Override
		protected <BT, KT extends Comparable<KT>> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			// TODO
			return null;
		}
	}
	class BeanstoreHBase<BT, KT extends  Comparable<KT>> extends AbstractBeanstore<BT, KT>{
		protected BeanstoreHBase(AbstractBeanstoreRootService rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
		}
		@Override
		protected BT getDelegate(KT key) throws Exception {
			return null;
		}
		@Override
		protected void removeDelegate(KT key) throws Exception {
			// TODO
		}
		@Override
		protected void putDelegate(KT key, BT bean) throws Exception {
			// TODO
		}
	}
}