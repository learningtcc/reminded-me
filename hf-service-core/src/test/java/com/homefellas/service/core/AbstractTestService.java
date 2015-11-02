package com.homefellas.service.core;

import java.io.IOException;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.junit.runner.RunWith;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.homefellas.metrics.IMetricDao;
import com.homefellas.metrics.MetricService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:com/homefellas/context/unittest-context.xml"})
public abstract class AbstractTestService extends AbstractTransactionalJUnit4SpringContextTests {

//	protected IDao dao;
	
	//@Resource(name="&cacheManager")	 
	protected Ehcache cache;
	
	protected PasswordEncoder passwordEncoder;
	
	//services
//	protected LoginService loginService = new LoginService();
	protected MetricService metricService = new MetricService();

	//daos
//	protected ILoginDao loginDao;
	protected IMetricDao metricDao;
	
	@BeforeTransaction
	public void init()
	{
		//setup dao
//		dao = createMock(IDao.class);
//		cache = createMock(Ehcache.class);
////		loginDao = createMock(ILoginDao.class);
//		metricDao = createMock(IMetricDao.class);
//		passwordEncoder = createMock(PasswordEncoder.class);
		
//		//setup services
//		loginService.setDao(dao);
//		loginService.setLoginDao(loginDao);
//		loginService.setPasswordEncoder(passwordEncoder);
		
//		metricService.setDao(dao);
//		metricService.setCache(cache);
//		metricService.setMetricDao(metricDao);
	}
	
	protected Ehcache createCacheInstance(String cacheName, int maxElementsInMemory, boolean eternal, boolean overflowToDisk, int timeToLive,  MemoryStoreEvictionPolicy memoryStoreEvictionPolicy)
	{
		CacheManager cacheManager = CacheManager.create();
		
		EhCacheFactoryBean factory = new EhCacheFactoryBean();
		factory.setCacheManager(cacheManager);
		factory.setCacheName(cacheName);
		factory.setMaxElementsInMemory(maxElementsInMemory);
		factory.setEternal(eternal);
		factory.setOverflowToDisk(overflowToDisk);
		factory.setTimeToLive(timeToLive);
		factory.setMemoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
		try
		{
			factory.afterPropertiesSet();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		
		return factory.getObject();

	}
}
