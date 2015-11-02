package com.homefellas.rm.task;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Proxy;

import com.homefellas.batch.INotifiable;
import com.homefellas.rm.ISynchronizeableFilteredDateRange;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.rm.share.IShareable;

//@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Entity
@Table(name="t_tasks")
@Proxy(lazy=false)
@XmlRootElement
public class Task extends AbstractTask implements ISynchronizeableFilteredDateRange, INotifiable, IShareable {

	@Transient
	protected String who;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="appleIOSCalEventId")
	protected AppleIOSCalEvent appleIOSCalEvent;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="repeatSetupId")
	protected RepeatSetup repeatSetup;
	
	/**
	 * This is the set of calendars.  
	 * @see Calendar
	 */
	@ManyToMany(fetch=FetchType.EAGER,targetEntity=Calendar.class)
	@JoinTable(name="t_taskCalendars",joinColumns=@JoinColumn(name="taskId"),inverseJoinColumns=@JoinColumn(name="calendarId"))
	protected Set<Calendar> calendars;
	
	
	public Task()
	{
		
	}
	
	public Task(String id)
	{
		super(id);
	}
	

	public RepeatSetup getRepeatSetup()
	{
		return repeatSetup;
	}

	public void setRepeatSetup(RepeatSetup repeatSetup)
	{
		this.repeatSetup = repeatSetup;
	}
	
	@JsonIgnore
	public boolean isStatusExclusive()
	{
		return true;
	}

	@JsonIgnore
	public boolean isStartDateNull()
	{
		if (startTime==null)
			return true;
		else
			return false;
	}
	
	@JsonIgnore
	public boolean isEndDateNull()
	{
		if (endTime==null)
			return true;
		else
			return false;
	}

	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}
	
	@JsonIgnore
	public String getDeleteStatusField()
	{
		return "progress";
	}

	@JsonIgnore
	public String getNotificationId()
	{
		return String.valueOf(getId());
	}

	@JsonIgnore
	public String getClassName()
	{
		return getClass().getName();
	}

	@JsonIgnore
	public boolean isCallBack()
	{
//		if (repeatSetup == null)
//			return false;
//		else
//			return true;
		return false;
	}
	
	@JsonIgnore
	public String getFormatedEndDate()
	{
		if (endTime!=null)
			return endTime.toString("MM/dd/yyyy HH:mm zzz");
		else
			return "";
	}
	
	@JsonIgnore
	public String getFormatedStartDate()
	{
		if (startTime!=null)
			return startTime.toString("MM/dd/yyyy HH:mm zzz");
		else
			return "";
	}
	
	@JsonIgnore
	public int getDeletedTrueValue()
	{
		return ProgressEnum.DELETE.ordinal();
	}
	
	
	/**
	 * This is the set of calendars.  
	 * @see Calendar
	 * @return Set<Calednar>
	 */
	public Set<Calendar> getCalendars()
	{
		return calendars;
	}

	public void setCalendars(Set<Calendar> calendars)
	{
		this.calendars = calendars;
	}
	
	public void addCalendar(Calendar calendar)
	{
		if (calendars == null)
			calendars = new HashSet<Calendar>();
		
		calendars.add(calendar);
	}

	@Override
	@JsonIgnore
	public String getEndTimeAttributeName()
	{
		return "endTimeMilli";
	}


	@Override
	@JsonIgnore
	public String getStartTimeAttributeName()
	{
		return "startTimeMilli";
	}

	@Override
	@JsonIgnore
	public String getTimeLessTaskAttributeName()
	{
		return "timeLessTask";
	}

	public String getWho()
	{
		return who;
	}

	public void setWho(String who)
	{
		this.who = who;
	}

	public AppleIOSCalEvent getAppleIOSCalEvent() {
		return appleIOSCalEvent;
	}

	public void setAppleIOSCalEvent(AppleIOSCalEvent appleIOSCalEvent) {
		this.appleIOSCalEvent = appleIOSCalEvent;
	}
	
	@JsonIgnore
	public void removeAllCalendars()
	{
		if (calendars!=null)
		{
//			Iterator<Calendar> calIt = calendars.iterator();
//			for (Calendar calendar : calIt) 
//				calendars.remove(calendar);
			this.calendars.clear();
//			this.calendars.retainAll(new HashSet<Calendar>());
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
}
