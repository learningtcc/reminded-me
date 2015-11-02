package com.homefellas.rm;

import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.share.SentShare;
import com.homefellas.rm.share.Share;

public interface ITaskDependable extends ISynchronizeableFiltered
{

	public String getTaskAttributeName();
	
	public enum TaskDependableObject 
	{ 
		
		Share(Share.class),
		SentShare(SentShare.class),
		Alarm(Alarm.class);
		
		/**
		 * The class that should be passed
		 */
		private Class<? extends ITaskDependable> clazz;
		private Class<? extends ITaskDependable> mapKeyClass;
		
		private TaskDependableObject(Class<? extends ITaskDependable> clazz)
		{
			this.clazz = clazz;
			this.mapKeyClass = clazz;
		}
		
		private TaskDependableObject(Class<? extends ITaskDependable> clazz, Class<? extends ITaskDependable> mapKey)
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
		public ITaskDependable getClassInstance()
		{
			try
			{
				return (ITaskDependable)Class.forName(getClassName()).newInstance();
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
