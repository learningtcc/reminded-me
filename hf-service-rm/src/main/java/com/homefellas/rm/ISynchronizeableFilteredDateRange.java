package com.homefellas.rm;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.homefellas.rm.share.SynchronizeShare;
import com.homefellas.rm.task.Task;

public interface ISynchronizeableFilteredDateRange extends
		ISynchronizeableFiltered
{

	@JsonIgnore
	public String getEndTimeAttributeName();
	
	@JsonIgnore
	public String getStartTimeAttributeName();
	
	@JsonIgnore
	public String getTimeLessTaskAttributeName();
	
	public enum SynchronizeableFilteredDateRangeObject 
	{ 
		
		Task(Task.class),
		SynchronizeShare(SynchronizeShare.class, Task.class);

		
		/**
		 * The class that should be passed
		 */
		private Class<? extends ISynchronizeableFilteredDateRange> clazz;
		private Class<? extends ISynchronizeableFilteredDateRange> mapKeyClass;
		
		private SynchronizeableFilteredDateRangeObject(Class<? extends ISynchronizeableFilteredDateRange> clazz)
		{
			this.clazz = clazz;
			this.mapKeyClass = clazz;
		}
		
		private SynchronizeableFilteredDateRangeObject(Class<? extends ISynchronizeableFilteredDateRange> clazz, Class<? extends ISynchronizeableFilteredDateRange> mapKey)
		{
			this.clazz = clazz;
			this.mapKeyClass = mapKey;
		}
		
		public String getClassName()
		{
			return clazz.getName();
		}
		
		public String getMapKeyClass()
		{
			return mapKeyClass.getName();
		}
		
		/**
		 * This will create a new instance of the class.  A default constuctor must be defined.
		 * @return
		 */
		public ISynchronizeableFilteredDateRange getClassInstance()
		{
			try
			{
				return (ISynchronizeableFilteredDateRange)Class.forName(getClassName()).newInstance();
			}
			catch (ClassNotFoundException classNotFoundException)
			{
				classNotFoundException.printStackTrace();
				return null;
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
				return null;
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
				return null;
			}
		
		}
		
	}
}
