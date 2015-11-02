package com.homefellas.metrics;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;

public class MetricDao extends HibernateCRUDDao implements IMetricDao {

	@Override
	public MetricOutlier createMetricOutlier(MetricOutlier metricOutlier) {
		save(metricOutlier);
		
		return metricOutlier;
	}

	@Override
	public Metric createMetric(Metric metric) {
		save(metric);
		
		
		return metric;
	}

	@Override
	public ClientMetric createClientMetric(ClientMetric clientMetric) {
		save(clientMetric);
		
		return clientMetric;
	}

	@Override
	public WebRequestMetric createWebRequestMetric(
			WebRequestMetric webRequestMetric) {
		save(webRequestMetric);
		
		return webRequestMetric;
	}
	
	

}
