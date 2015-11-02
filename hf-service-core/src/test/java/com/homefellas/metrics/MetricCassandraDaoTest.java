package com.homefellas.metrics;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.homefellas.model.core.TestModelBuilder;
import com.homefellas.test.core.CassandraUnitTester;

public class MetricCassandraDaoTest extends CassandraUnitTester{

	@Autowired
	private MetricCassandraDao metricCassandraDao;
	
	@Test 
	public void saveWebRequestMetric()
	{
//		WebRequestMetric webRequestMetric = TestModelBuilder.webRequestMetric("test@reminded.me", "/ws/task/create");
//		
//		metricCassandraDao.saveWebRequestMetric(webRequestMetric);
	}
}
