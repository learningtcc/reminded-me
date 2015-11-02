package com.homefellas.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class MetricCache {

	private Map<String, List<MetricDataPoint>> cache1 = new ConcurrentHashMap(10000);
	private Map<String, List<MetricDataPoint>> cache2 = new ConcurrentHashMap(10000);
	
//	private static MetricCache metricCache = new MetricCache();
	
	private boolean useCache1=true;
	
//	private MetricCache()
//	{
//		
//	}
//	
//	public static MetricCache getInstance()
//	{
//		return metricCache;
//	}
	
	private Map<String, List<MetricDataPoint>> getActiveCache()
	{
		if (useCache1)
			return cache1;
		else
			return cache2;
	}
	
	private Map<String, List<MetricDataPoint>> getInActiveCache()
	{
		if (!useCache1)
			return cache1;
		else
			return cache2;
	}
	
	public void addMetric(MetricDataPoint dataPoint)
	{
		List<MetricDataPoint> dataPoints = getActiveCache().get(dataPoint.getFQMN());
		if (dataPoints == null)
		{
			dataPoints = new ArrayList<MetricDataPoint>();
		}
		
		dataPoints.add(dataPoint);
		
		getActiveCache().put(dataPoint.getFQMN(), dataPoints);
	}
	
	private synchronized void swapCaches()
	{
		useCache1 = !useCache1;
	}
	
	public List<Metric> reportFindings()
	{
		swapCaches();
		
		Map<String, List<MetricDataPoint>> cache = getInActiveCache();
		Set<String> keys = cache.keySet();
		List<Metric> metrics = new ArrayList<Metric>();
		for (String key:keys)
		{
			List<MetricDataPoint> dps = cache.get(key);
			int max = 0;
			int min = 10000000;
			int counter=0;
			long total=0;
			long executionTime;
			double average=0;
			for (MetricDataPoint dp:dps)
			{
				counter++;
				executionTime = dp.getExecutionTime();
				if (executionTime<min)
					min = (int)executionTime;
				
				if (executionTime>max)
					max = (int)executionTime;
				
				total += executionTime;
			}
			 if (counter>0)
				 average = total/counter;
			 
			 Metric metric = new Metric();
			 metric.setKey(key);
			 metric.setMax(max);
			 metric.setMin(min);
			 metric.setAverage(average);
			 
			 metrics.add(metric);
		}
		
		return metrics;
	}
}
