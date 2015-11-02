package com.homefellas.rm.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.homefellas.metrics.AbstractMetricAspect;
import com.homefellas.metrics.CollectTimeMetrics;

@Aspect
public class MetricAspect extends AbstractMetricAspect
{

	@Around("execution(* *(..)) && @annotation(collectTimeMetric)")
	public Object calculateRunTime(final ProceedingJoinPoint pjp, final CollectTimeMetrics collectTimeMetric) throws Throwable 
	{
		return doPointCut(pjp, collectTimeMetric);
	}
}
