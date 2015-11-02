package com.homefellas.metrics;

import java.util.Map;

import com.homefellas.dao.cassandra.core.CassandraDao;

public class MetricCassandraDao extends CassandraDao {

	public void saveWebRequestMetric(WebRequestMetric webRequestMetric)
	{
		//search by email, tgt, request uri, status
		
		//first save the base class
		String query;
		Map<String, Object> attributes = buildAttributes(webRequestMetric);
		
		String baseTableName = getTableName(webRequestMetric);
		query = buildInsertCQL(baseTableName, attributes);
		System.out.println(query.toString());
		getSession().execute(query.toString());

		//save by email
		String tableName = baseTableName+"_by_email";
		query = buildInsertCQL(tableName, attributes);
		System.out.println(query.toString());
		getSession().execute(query.toString());
		
		//save by tgt
		tableName = baseTableName+"_by_tgt";
		query = buildInsertCQL(tableName, attributes);
		System.out.println(query.toString());
		getSession().execute(query.toString());
	}
}
