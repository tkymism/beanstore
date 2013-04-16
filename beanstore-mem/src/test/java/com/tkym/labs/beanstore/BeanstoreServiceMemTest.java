package com.tkym.labs.beanstore;

import com.tkym.labs.beancache.Beancache;
import com.tkym.labs.beancache.BeancacheQuery;
import com.tkym.labs.beancache.BeancacheRepository;
import com.tkym.labs.beanmeta.BeanMeta;
import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beanstore.BeanQueryResultBuilder.QueryResultFetcher;
import com.tkym.labs.beanstore.api.BeanQuerySource;
import com.tkym.labs.beanstore.api.BeanstoreRootService;
import com.tkym.labs.beanstore.api.BeanstoreServiceFactory;
import com.tkym.labs.beanstore.api.BeanstoreTransaction;

public class BeanstoreServiceMemTest {
	class BeanstoreRootServiceMem extends AbstractBeanstoreRootService{
		private final BeancacheRepository repository;
		BeanstoreRootServiceMem(BeancacheRepository repository){
			this.repository = repository;
		}
		@Override
		public BeanstoreTransaction transaction() {
			return null;
		}
		@Override
		protected <BT, KT extends Comparable<KT>> AbstractBeanstore<BT, KT> createBeanstore(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			return new BeanstoreMem<BT, KT>(this, beanMeta, parent);
		}
		@Override
		protected <BT, KT extends Comparable<KT>> AbstractBeanQueryExecutor<BT, KT> 
				createBeanQueryExecutor(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent){
			return new BeanQueryExecutor<BT,KT>(beanMeta, parent, repository.get(beanMeta));
		}
	}
	class BeanstoreMem<BT,KT extends Comparable<KT>> extends AbstractBeanstore<BT, KT>{
		private final Beancache<BT, KT> beancache;
		protected BeanstoreMem(BeanstoreRootServiceMem rootService, BeanMeta<BT, KT> beanMeta, Key<?, ?> parent) {
			super(rootService, beanMeta, parent);
			this.beancache = rootService.repository.get(beanMeta); 
		}
		@Override
		protected BT getDelegate(KT key) throws Exception {
			return beancache.get(beanMeta.key(parent, key));
		}
		@Override
		protected void removeDelegate(KT key) throws Exception {
			beancache.remove(beanMeta.key(parent, key));
		}
		@Override
		protected void putDelegate(KT key, BT bean) throws Exception {
			beancache.put(beanMeta.key(parent, key), bean);
		}
	}
	class BeanQueryExecutor<BT,KT extends Comparable<KT>> extends AbstractBeanQueryExecutor<BT, KT>{
		@SuppressWarnings("unused")
		private final BeancacheQuery<BT, KT> beancacheQuery; 
		BeanQueryExecutor(BeanMeta<BT, KT> beanMeta, Key<?, ?> parent, Beancache<BT, KT> beancache) {
			super(beanMeta, parent);
			if (parent != null)
				this.beancacheQuery = beancache.queryFor(parent);
			else
				this.beancacheQuery = beancache.queryAll();
		}
		@Override
		public QueryResultFetcher<BT> executeQueryAsBean(
				BeanQuerySource<BT, KT> objects) throws Exception {
			return null;
		}
		@Override
		public QueryResultFetcher<Key<BT, KT>> executeQueryAsKey(
				BeanQuerySource<BT, KT> objects) throws Exception {
			return null;
		}
	}
	public class BeanstoreServiceFactoryMem implements BeanstoreServiceFactory{
		@Override
		public BeanstoreRootService create() {
			return new BeanstoreRootServiceMem(new BeancacheRepository());
		}
	}
}
