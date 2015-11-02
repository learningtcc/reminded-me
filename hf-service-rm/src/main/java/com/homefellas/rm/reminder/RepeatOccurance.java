package com.homefellas.rm.reminder;

public enum RepeatOccurance {

	NONE, minute, hour, day, week, month, year, custom;
	
	public static RepeatOccurance getInstance(int ordinal)
	{
		for (RepeatOccurance occurance : values())
		{
			if (occurance.ordinal()==ordinal)
				return occurance;
		}
		
		return NONE;
	}
}
