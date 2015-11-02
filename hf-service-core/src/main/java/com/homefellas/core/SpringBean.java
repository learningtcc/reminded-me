package com.homefellas.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SpringBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// these booleans ensure that the init and destroy hooks call super if they are overridden
	private boolean onInitHook;
	private boolean onDestroyHook;
	
	/**
	 * Init method for any initialization logic. 
	 * 
	 */
	public final void init() {
		onInit();
		
		// ensure overriding method called super
		if(!onInitHook)
			throw new UnsupportedOperationException("Methods overriding onInit must call super()");
		
		logger.info("Initialized: {}", this);
	}
	
	/**
	 * Hook into the component initialization lifecycle.
	 * 
	 */
	protected void onInit() {
		onInitHook = true;
	}

	/**
	 * Destroy method for any cleanup logic.
	 *  
	 */
	public final void destroy() {
		onDestroy();
		
		// ensure overriding method called super
		if(!onDestroyHook)
			throw new UnsupportedOperationException("Methods overriding onDestroy must call super()");
		
		logger.info("Destroyed: {}", this.getClass().getName());
	}

	/**
	 * Hook into the component destruction lifecycle.
	 * 
	 */
	protected void onDestroy() {
		onDestroyHook = true;
	}
	
	/**
	 * Returns a simple toString identifier, to avoid printing large object graphs from nested core components.
	 * 
	 * @return String
	 */
	public String getSimpleToString() {
		return getClass().getName();
	}	
	
	@Override
	public String toString() {
		return SpringBeanToStringBuilder.reflectionToString(this);
	}
}
