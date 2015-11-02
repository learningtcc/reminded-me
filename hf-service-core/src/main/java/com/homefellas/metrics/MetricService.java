package com.homefellas.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.service.core.AbstractService;
import com.homefellas.util.TimeHelper;

/**
 * This is the service that will handle metrics caching and metrics persisting.  Metrics spring information is located in the metrics-context.xml.
 * If you want to start recording metrics on a method then you can set the com.homefellas.metrics.CollectTimeMetrics annotation on that method.  This
 * will store a MetricDataPoint into cache.  On a given interval which is defined in the envinronment.properties file, a quart thread will fire and
 * clear the cache and write the data points to the data store.  
 * 
 * There is also a client metric input.  This method is wrapped by a web service and will store client metrics directly to the database.
 * 
 * @author Tim Delesio
 * @see {@link CollectTimeMetrics}, {@link MetricDataPoint}, {@link ClientMetric}
 */
public class MetricService extends AbstractService {
	
	@Autowired
	private IMetricDao metricDao;
	
	private Ehcache cache;
	private Ehcache webRequestCache;
	private int responseThreshold;
	private boolean captureWebMetrics=false;
	
	/**
	 * There are two methods used to calculate metrics.  There is floating point and normal.  Floating point will calculate an average as data
	 * is pulled from the cache.  This is done by re-averaging an average.  If this is set to true, this method will be used.  If it is set to false
	 * then normal (true) averages will be calculated by summing all the values and dividing by the number of occurances.
	 */
	private final boolean useMovingPointMetricCalculation=false;

	public void setCaptureWebMetrics(boolean captureWebMetrics) {
		this.captureWebMetrics = captureWebMetrics;
	}

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	public void saveWebRequestMetric(WebRequestMetric webRequestMetric)
	{
		Element element = new Element(UUID.randomUUID().getMostSignificantBits(), webRequestMetric);
		webRequestCache.put(element);
	}
	
	public void setWebRequestCache(Ehcache webRequestCache) {
		this.webRequestCache = webRequestCache;
	}

	public void setResponseThreshold(int responseThreshold)
	{
		this.responseThreshold = responseThreshold;
	}

	protected Ehcache getCache() {
		return cache;
	}
	
	/**
	 * This retrieves the metric data point from the cache
	 * @param uuid
	 * @return
	 */
	private MetricDataPoint getDataPointFromCache(UUID uuid)
	{
		return (MetricDataPoint) cache.get(uuid).getObjectValue();
	}
	
	
	private class MetricWrapper
	{
		private Collection<Metric> metrics;
		private List<MetricOutlier> outliers;
		public Collection<Metric> getMetrics()
		{
			return metrics;
		}
		public void setMetrics(Collection<Metric> metrics)
		{
			this.metrics = metrics;
		}
		public List<MetricOutlier> getOutliers()
		{
			return outliers;
		}
		public void setOutliers(List<MetricOutlier> outliers)
		{
			this.outliers = outliers;
		}
		
		
	}
	
	/**
	 * This is the method that does all the work for metric calculation.  There are two loops that occur.  First, we will loop
	 * through and separate all the datapoints into separate categories.  Then we will loop through each category and calculate the averages, 
	 * min, max, and occurances.
	 * @param uuids
	 * @param collectMetric
	 * @return
	 */
	protected MetricWrapper collectMetrics(List<UUID> uuids, boolean collectMetric)
	{
		//object used to sort the data
		MetricWrapper metricWrapper = new MetricWrapper();
		Map<String, List<MetricDataPoint>> sortedData = new HashMap<String, List<MetricDataPoint>>(1000);
		List<MetricOutlier> outliers = new ArrayList<MetricOutlier>(10);
		MetricDataPoint mdp = null;
		
		for (UUID uuid : uuids)
		{
			//if there is nothing in the cache, don't do anything
			if (uuid==null||cache == null || cache.get(uuid)==null)
				continue;
			
			//pull out a record
			mdp = getDataPointFromCache(uuid);

			//check to see if it has already been collectetd
			if (mdp.isCollected())
			{
				logger.info("metric has been collected so skipping");
				continue;
			}
			
			//check to see if we already collected data for this method name
			List<MetricDataPoint> metricDataPoints = sortedData.get(mdp.getFQMN());
			if (metricDataPoints == null)
			{
				//we have not, so create a new bucket
				metricDataPoints = new ArrayList<MetricDataPoint>();
				sortedData.put(mdp.getFQMN(), metricDataPoints);
			}
			
			//add the mdp to the bucket
			metricDataPoints.add(mdp);
			logger.info("adding "+mdp.getFQMN()+":"+mdp.getExecutionTime()+" to collection");
			
			//if we collected it, mark it
			if (collectMetric)
				mdp.collected();
		}
		
		//now we need to loop over and calculate the stats
		Set<String> keys = sortedData.keySet();
		List<Metric> metrics = new ArrayList<Metric>();
		for (String key:keys)
		{
			//reset the base numbers for the new bucket
			List<MetricDataPoint> dps = sortedData.get(key);
			int max = 0;
			int min = 10000000;
			int counter=0;
			long total=0;
			long executionTime;
			double average=0;
			Metric metric = new Metric();
			for (MetricDataPoint dp:dps)
			{
				outliers = checkForOutlier(outliers, mdp, metric);
				//loop through each bucket now
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
			 
			 //create a metric
			 
			 metric.setKey(key);
			 metric.setMax(max);
			 metric.setMin(min);
			 metric.setAverage(average);
			 metric.setOccurances(counter);
			 //add outlier data by thershold globaly
			 
			 logger.info("Aggregate created for "+metric.getKey()+" Max="+metric.getMax()+ "Min="+metric.getMin()+" Average="+metric.getAverage()+" Occurances="+metric.getOccurances());
			 
			 //add metric to the list of meticss
			 metrics.add(metric);
		}
		
		metricWrapper.setMetrics(metrics);
		metricWrapper.setOutliers(outliers);
		return metricWrapper;
	}
	
	private List<MetricOutlier> checkForOutlier(List<MetricOutlier> outliers, MetricDataPoint mdp, Metric metric)
	{
		if (mdp.getExecutionTime()>=responseThreshold)
		{
			//we have an outlier
			MetricOutlier metricOutlier = new MetricOutlier();
			metricOutlier.setThreshold(responseThreshold);
			metricOutlier.setExecutionTime(mdp.getExecutionTime());
			metricOutlier.setMetric(metric);
			outliers.add(metricOutlier);
			
		}
		
		return outliers;
	}
	
	/**
	 * This method will calcuate floating point averages.  There are less iterations with this method but more math calculations
	 * @param uuids
	 * @param collectMetric
	 * @return
	 */
	protected MetricWrapper collectMetricsWithMovingAverage(List<UUID> uuids, boolean collectMetric)
	{
//		Map<Object, MetricDataPoint> elements = cache.getAllWithLoader(uuids, null);
		MetricWrapper metricWrapper = new MetricWrapper();
		List<MetricOutlier> outliers = new ArrayList<MetricOutlier>(10);
		MetricDataPoint mdp = null;
		Map<String, Metric> metrics = new HashMap<String, Metric>(); 
		for (UUID uuid : uuids)
		{
			if (uuid==null||cache == null || cache.get(uuid)==null)
				continue;
//			mdp = (MetricDataPoint) cache.get(uuid).getObjectValue();
			mdp = getDataPointFromCache(uuid);
//			mdp = getDataPointFromCopy(uuid, elements);
			
			if (mdp.isCollected())
			{
				logger.info("metric has been collected so skipping");
				continue;
			}
			
			Metric metric = metrics.get(mdp.getFQMN());
			if (metric == null)
			{
				metric = new Metric(mdp.getFQMN(), mdp.getExecutionTime());
				metrics.put(mdp.getFQMN(), metric);
				
				outliers = checkForOutlier(outliers, mdp, metric);
			}
			else
			{
				outliers = checkForOutlier(outliers, mdp, metric);
				
				double average = metric.getAverage();
				int occurances = metric.getOccurances();
				double movingAverage = average*occurances;
				movingAverage += mdp.getExecutionTime();
				movingAverage = movingAverage / (occurances+1);
				
				metric.setAverage(movingAverage);
				metric.setOccurances(occurances+1);
				
				if (mdp.getExecutionTime()<metric.getMin())
					metric.setMin((int)mdp.getExecutionTime());
				
				if (mdp.getExecutionTime()>metric.getMax())
					metric.setMax((int)mdp.getExecutionTime());
				
				
			}
			logger.info("adding "+mdp.getFQMN()+":"+mdp.getExecutionTime()+" to collection");
			
			if (collectMetric)
				mdp.collected();
		}
		
		metricWrapper.setMetrics(metrics.values());
		metricWrapper.setOutliers(outliers);
		return metricWrapper;
	}
	
	/* (non-Javadoc)
	 * @see com.homefellas.metrics.IMetricService#flushCache()
	 */
	public void flushCache()
	{
		List<UUID> elements = cache.getKeys();
//		logger.info(TimeHelper.getSimpleDateFormat(System.currentTimeMillis())+" : flushing");
		System.out.println(TimeHelper.getSimpleDateFormat(System.currentTimeMillis())+" : flushing");
//		Collection<Metric> metrics;
		MetricWrapper wrapper;
		if (useMovingPointMetricCalculation)
			wrapper = collectMetricsWithMovingAverage(elements, true);
		else
			wrapper = collectMetrics(elements, true);
		
		saveAllMetrics(wrapper.getMetrics());
		saveAllOutliers(wrapper.getOutliers());
		
		if (captureWebMetrics)
		{
			saveAllWebMetrics();
		}
	}
	
	@Transactional
	public void saveAllWebMetrics()
	{
		List<Long> elementsWRC = webRequestCache.getKeys();
		for (Long uuid: elementsWRC)
		{
			Element element = webRequestCache.get(uuid);
			webRequestCache.remove(uuid);
			if (element==null)
				continue;
			WebRequestMetric webRequestMetric = (WebRequestMetric)element.getValue();
			metricDao.createWebRequestMetric(webRequestMetric);
		}
	}
	
	@Transactional
	public void saveAllOutliers(List<MetricOutlier> outliers)
	{
		for (MetricOutlier outlier : outliers)
			metricDao.createMetricOutlier(outlier);
	}

	/* (non-Javadoc)
	 * @see com.homefellas.metrics.IMetricService#saveAllMetrics(java.util.Collection)
	 */
	@Transactional
	public void saveAllMetrics(Collection<Metric> metrics)
	{
		for (Metric metric:metrics)
		{
			metricDao.createMetric(metric);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.homefellas.metrics.IMetricService#saveClientMetric(com.homefellas.metrics.ClientMetric)
	 */
	@Transactional
	public void saveClientMetric(ClientMetric clientMetric)
	{
		metricDao.createClientMetric(clientMetric);
	}
	
	/* (non-Javadoc)
	 * @see com.homefellas.metrics.IMetricService#saveBulkClientMetrics(java.util.List)
	 */
	@Transactional
	public void saveBulkClientMetrics(List<ClientMetric> clientMetrics)
	{
		for (ClientMetric clientMetric : clientMetrics)
			metricDao.createClientMetric(clientMetric);
	}
}
