package com.homefellas.metrics;

public interface IMetricDao {

	public MetricOutlier createMetricOutlier(MetricOutlier metricOutlier);
	public Metric createMetric(Metric metric);
	public ClientMetric createClientMetric(ClientMetric clientMetric);
	public WebRequestMetric createWebRequestMetric(WebRequestMetric webRequestMetric);
}
