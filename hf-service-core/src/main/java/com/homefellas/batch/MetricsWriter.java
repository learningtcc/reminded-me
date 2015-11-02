package com.homefellas.batch;

import java.util.List;

import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemWriter;

public class MetricsWriter extends StepExecutionListenerSupport implements
		ItemWriter<Object> {

	public void write(List<? extends Object> arg0) throws Exception {
		
	}

	
}
