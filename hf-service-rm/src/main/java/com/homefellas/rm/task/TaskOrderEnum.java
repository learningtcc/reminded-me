package com.homefellas.rm.task;

import com.homefellas.exception.ValidationException;
import com.homefellas.rm.ValidationCodeEnum;

public enum TaskOrderEnum {

	PRIORITY, DUE_DATE, START_DATE, CATEGORY;
	
	public TaskOrderEnum getTaskOrderEnumber(int ordinal) throws ValidationException
	{
		TaskOrderEnum[] taskOrderEnums = values();
		for (int i=0;i<values().length;i++)
		{
			if (ordinal==taskOrderEnums[i].ordinal())
				return taskOrderEnums[i];
		}
		
		throw new ValidationException(ValidationCodeEnum.TASK_ORDER_NOT_SUPPORTED);
	}
}
