package com.homefellas.metrics;

import java.lang.reflect.Method;
import java.util.UUID;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.homefellas.util.TimeHelper;

public abstract class AbstractMetricAspect {

	
	protected final Log logger = LogFactory.getLog(getClass());
	private Ehcache cache;
	
	private int metricLevel=MetricCollectionPriority.ALWAYS.ordinal();
		
	protected MetricCollectionPriority getMetricPriority(ProceedingJoinPoint pjp)
	{
		final MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
		Method method = methodSignature.getMethod();
		CollectTimeMetrics collectTimeMetric = method.getAnnotation(CollectTimeMetrics.class);
		return collectTimeMetric.value();
	}
	
	protected void before()
	{
		
		
	}
	
	protected void after()
	{
		
	}
	

	public Object doPointCut(ProceedingJoinPoint pjp, CollectTimeMetrics collectTimeMetric) throws Throwable {

//		final MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
//		Method method = methodSignature.getMethod();
//		CollectTimeMetrics collectTimeMetric = method.getAnnotation(CollectTimeMetrics.class);
		
//System.out.println("in doPointCut");		
		//first we need to see if we should log this metric based on the level
		if (collectTimeMetric.value().ordinal()>metricLevel)
		{
			logger.debug("Log level to low...skipping metric");
			return pjp.proceed();
		}
		
		 // start stopwatch
		long startTime = System.currentTimeMillis();
		Object retVal = null;
		try
		{
			retVal = pjp.proceed();
		}
		finally
		{
		    // stop stopwatch
			long stopTime = System.currentTimeMillis();
			long executionTime = stopTime-startTime;

		
		
			MetricDataPoint metricDataPoint = new MetricDataPoint();
			metricDataPoint.setClassName(pjp.getTarget().getClass().getName());
			metricDataPoint.setMethodName(pjp.getSignature().getName());
			metricDataPoint.setTimestamp(System.currentTimeMillis());
			metricDataPoint.setExecutionTime(executionTime);
			
			logger.debug(metricDataPoint.getClassName()+":"+metricDataPoint.getMethodName()+" took "+metricDataPoint.getExecutionTime()+" to complete on "+TimeHelper.getSimpleDateFormat(System.currentTimeMillis()));
					
			Element element = new Element(UUID.randomUUID(), metricDataPoint);
			cache.put(element);			
		}	
		return retVal;
		
	}

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	public void setMetricLevel(int metricLevel)
	{
		this.metricLevel = metricLevel;
	}


}
