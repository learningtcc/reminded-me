package com.homefellas.metrics;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GenericCacheEventListenerFactory extends CacheEventListenerFactory implements ApplicationContextAware 
{

	private static ApplicationContext context;
	
	
	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		 String beanName = properties.getProperty( "bean" );
	        if ( beanName == null ) {
	            throw new IllegalArgumentException( "The cache event listener factory must be configured with 'bean' " +
	                                                        "property pointing to the Spring bean to return as cache event listener" );
	        }
	 
	        return (CacheEventListener) getContext().getBean( beanName );
	}
	
	
	 
    public static ApplicationContext getContext() {
        if ( context != null ) {
            return context;
        } else {
            throw new IllegalStateException( "The Spring application context is not yet available. " +
                                                     "The call to this method has been performed before the application context " +
                                                     "provider was initialized" );
        }
    }



	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if ( context == null ) {
            this.context = applicationContext;
        } else {
            throw new IllegalStateException( "The application context provider was already initialized. " +
                                                     "It is illegal to place try to initialize the context provider twice or create " +
                                                     "two different beans of this type (even if the contexts are different)" );
        }
	}



	

	
}