package com.homefellas.ws.model;

import java.util.List;

import org.joda.time.DateTime;

import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.task.SkipTime;

public class RepeatSetupUI extends AbstractUI
{

	private String monthlyrule;
	private String repeatOccurance;
	
	private long repeatPeriod;
	
	private boolean skipMonday;
	private boolean skipTuesday;
	private boolean skipWednesday;
	private boolean skipThursday;
	private boolean skipFriday;
	private boolean skipSaturday;
	private boolean skipSunday;
	private boolean usingEndsAfterOccurance;
	
	private int endsAfterOccurance;
	private int remainingOccurances; 
	
	private DateTime startsRepeatingOn;
	private DateTime endsRepeatingOn;
	private DateTime lastGeneratedDate;
	
	private List<SkipTimeUI> skipTimes;

	private TaskUI clonedrepeatSetup;
	
	RepeatSetupUI(){}
	 
	public RepeatSetupUI(RepeatSetup repeatSetup)
	{
		super(repeatSetup.getId(), repeatSetup.getLastModifiedDeviceId(), repeatSetup.getCreatedDate(), repeatSetup.getModifiedDate(), repeatSetup.getCreatedDateZone(), repeatSetup.getModifiedDateZone(), repeatSetup.getClientUpdateTimeStamp());
		
		this.monthlyrule=repeatSetup.getMonthlyrule();
		this.repeatOccurance=repeatSetup.getRepeatOccurance();
		
		this.repeatPeriod=repeatSetup.getRepeatPeriod();
		
		this.skipMonday=repeatSetup.isSkipMonday();
		this.skipTuesday=repeatSetup.isSkipTuesday();
		this.skipWednesday=repeatSetup.isSkipWednesday();
		this.skipThursday=repeatSetup.isSkipThursday();
		this.skipFriday=repeatSetup.isSkipFriday();
		this.skipSaturday=repeatSetup.isSkipSaturday();
		this.skipSunday=repeatSetup.isSkipSunday();
		this.usingEndsAfterOccurance=repeatSetup.isUsingEndsAfterOccurance();
		
		this.endsAfterOccurance=repeatSetup.getEndsAfterOccurance();
		this.remainingOccurances=repeatSetup. getRemainingOccurances();
		
		this.startsRepeatingOn=new DateTime(repeatSetup.getStartsRepeatingOn().getMillis());
		this.endsRepeatingOn=new DateTime(repeatSetup.getEndsRepeatingOn().getMillis());
		this.lastGeneratedDate=new DateTime(repeatSetup.getLastGeneratedDate().getMillis());
		
		for (SkipTime skipTime : repeatSetup.getSkipTimes())
		{
			skipTimes.add(new SkipTimeUI(skipTime));
		}
		
		this.clonedrepeatSetup = new TaskUI(repeatSetup.getClonedTask(), null, null, null);
	}

	public String getMonthlyrule()
	{
		return monthlyrule;
	}

	public String getRepeatOccurance()
	{
		return repeatOccurance;
	}

	public long getRepeatPeriod()
	{
		return repeatPeriod;
	}

	public boolean isSkipMonday()
	{
		return skipMonday;
	}

	public boolean isSkipTuesday()
	{
		return skipTuesday;
	}

	public boolean isSkipWednesday()
	{
		return skipWednesday;
	}

	public boolean isSkipThursday()
	{
		return skipThursday;
	}

	public boolean isSkipFriday()
	{
		return skipFriday;
	}

	public boolean isSkipSaturday()
	{
		return skipSaturday;
	}

	public boolean isSkipSunday()
	{
		return skipSunday;
	}

	public boolean isUsingEndsAfterOccurance()
	{
		return usingEndsAfterOccurance;
	}

	public int getEndsAfterOccurance()
	{
		return endsAfterOccurance;
	}

	public int getRemainingOccurances()
	{
		return remainingOccurances;
	}

	public DateTime getStartsRepeatingOn()
	{
		return startsRepeatingOn;
	}

	public DateTime getEndsRepeatingOn()
	{
		return endsRepeatingOn;
	}

	public DateTime getLastGeneratedDate()
	{
		return lastGeneratedDate;
	}

	public List<SkipTimeUI> getSkipTimes()
	{
		return skipTimes;
	}

	public TaskUI getClonedrepeatSetup()
	{
		return clonedrepeatSetup;
	}

	void setMonthlyrule(String monthlyrule)
	{
		this.monthlyrule = monthlyrule;
	}

	void setRepeatOccurance(String repeatOccurance)
	{
		this.repeatOccurance = repeatOccurance;
	}

	void setRepeatPeriod(long repeatPeriod)
	{
		this.repeatPeriod = repeatPeriod;
	}

	void setSkipMonday(boolean skipMonday)
	{
		this.skipMonday = skipMonday;
	}

	void setSkipTuesday(boolean skipTuesday)
	{
		this.skipTuesday = skipTuesday;
	}

	void setSkipWednesday(boolean skipWednesday)
	{
		this.skipWednesday = skipWednesday;
	}

	void setSkipThursday(boolean skipThursday)
	{
		this.skipThursday = skipThursday;
	}

	void setSkipFriday(boolean skipFriday)
	{
		this.skipFriday = skipFriday;
	}

	void setSkipSaturday(boolean skipSaturday)
	{
		this.skipSaturday = skipSaturday;
	}

	void setSkipSunday(boolean skipSunday)
	{
		this.skipSunday = skipSunday;
	}

	void setUsingEndsAfterOccurance(boolean usingEndsAfterOccurance)
	{
		this.usingEndsAfterOccurance = usingEndsAfterOccurance;
	}

	void setEndsAfterOccurance(int endsAfterOccurance)
	{
		this.endsAfterOccurance = endsAfterOccurance;
	}

	void setRemainingOccurances(int remainingOccurances)
	{
		this.remainingOccurances = remainingOccurances;
	}

	void setStartsRepeatingOn(DateTime startsRepeatingOn)
	{
		this.startsRepeatingOn = startsRepeatingOn;
	}

	void setEndsRepeatingOn(DateTime endsRepeatingOn)
	{
		this.endsRepeatingOn = endsRepeatingOn;
	}

	void setLastGeneratedDate(DateTime lastGeneratedDate)
	{
		this.lastGeneratedDate = lastGeneratedDate;
	}

	void setSkipTimes(List<SkipTimeUI> skipTimes)
	{
		this.skipTimes = skipTimes;
	}

	void setClonedrepeatSetup(TaskUI clonedrepeatSetup)
	{
		this.clonedrepeatSetup = clonedrepeatSetup;
	}
	
	
	
}
