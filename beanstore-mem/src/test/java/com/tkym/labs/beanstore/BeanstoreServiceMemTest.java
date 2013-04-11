package com.tkym.labs.beanstore;

import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.api.BeanQuery;
import com.tkym.labs.beanstore.api.Beanstore;
import com.tkym.labs.beanstore.api.BeanstoreRootService;
import com.tkym.labs.beanstore.api.BeanstoreService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class BeanstoreServiceMemTest {
	class BeanstoreRootServiceMem extends AbstractBeanstoreRootService{
		@Override
		public BeanstoreTransaction transaction() {
			return null;
		}
		@Override
		protected <BT, KT> AbstractBeanstore<BT, KT> createBeanstore(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return null;
		}
		@Override
		protected <BT, KT> AbstractBeanQueryExecutor<BT, KT> createBeanQueryExecutor(
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return null;
		}
	}
	class BeanstoreMem<BT,KT> extends AbstractBeanstore<BT, KT>{
		protected BeanstoreMem(AbstractBeanstoreRootService rootService,
				BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
		}
		@Override
		protected BT getDelegate(KT key) throws Exception {
			return null;
		}

		@Override
		protected void removeDelegate(KT key) throws Exception {
			
		}

		@Override
		protected void putDelegate(KT key, BT bean) throws Exception {
			
		}
	}
	class BeanstoreServiceMem<PB, PK> implements BeanstoreService<PB, PK>{
		@Override
		public <BT, KT> BeanQuery<BT, KT> query(BeanMeta<BT, KT> meta) {
			return null;
		}
		@Override
		public <BT, KT> Beanstore<BT, KT> store(BeanMeta<BT, KT> meta) {
			return null;
		}
		@Override
		public BeanstoreTransaction transaction() {
			return null;
		}
	}
	class BeanstoreServiceFactoryMem implements BeanstoreServiceFactory{
		@Override
		public BeanstoreRootService create() {
			return null;
		}
	}
}
