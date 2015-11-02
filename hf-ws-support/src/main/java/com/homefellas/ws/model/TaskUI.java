package com.homefellas.ws.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.homefellas.rm.calendar.Calendar;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.repeatsetup.TaskRepeat;
import com.homefellas.rm.repeatsetup.TaskTemplate;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;

public final class TaskUI extends AbstractUI
{

	protected String who;
	protected String title;
	protected String notes;
	protected String taskLocation;
	protected String parentId;
	
	protected int shareStatus;
	protected int subTasksCount=0;
	protected int sortOrder;
	protected int priority;
	protected int progress;
	
	protected boolean hasBeenShared;
	protected boolean timeLessTask;
	protected boolean startTimeLessTask;
	protected boolean aParent=false;
	protected boolean show=true;
	protected boolean publicTask=true;
	
	protected long endTimeMilli;
	protected long sortTimeStampMilli;
	protected long startTimeMilli;
	protected String timeZone;
	
	protected RepeatSetupUI repeatSetup;
	protected UserUI taskCreator;
	protected AppleIOSCallEventUI appleIOSCallEvent;
	
	protected List<ShareUI> shares;
	protected List<AlarmUI> alarms;
	protected List<TaskRepeatUI> taskRepeats;
	protected Set<CalendarUI> calendars;
	
	TaskUI(){}
	
	public TaskUI(Task task, List<Share> shares, List<Alarm> alarms, List<TaskRepeat> taskRepeats)
	{
		super(task.getId(), task.getLastModifiedDeviceId(), task.getCreatedDate(), task.getModifiedDate(), task.getCreatedDateZone(), task.getModifiedDateZone(), task.getClientUpdateTimeStamp());
	
		this.who=task.getWho();
		this.title=task.getTitle();
		this.notes=task.getNotes();
		this.taskLocation=task.getTaskLocation();
//		this.modelName=task.getModelName();
		this.parentId=task.getParentId();
		
		this.shareStatus=task.getShareStatus();
		this.subTasksCount=task.getSubTasksCount();
		this.sortOrder=task.getSortOrder();
		this.priority=task.getPriority();
		this.progress=task.getProgress();
		
		this.hasBeenShared=task.isHasBeenShared();
		this.timeLessTask=task.isTimeLessTask();
		this.startTimeLessTask=task.isStartTimeLessTask();
		this.aParent=task.isaParent();
		this.show=task.isShow();
		this.publicTask=task.isPublicTask();
		
//		this.endTime=new DateTime(task.getEndTimeMilli());
		this.endTimeMilli=task.getEndTimeMilli();
//		this.endTimeZone=task.getEndTimeZone();
//		this.sortTimeStamp=new DateTime(task.getSortTimeStampMilli());
		this.sortTimeStampMilli=task.getSortTimeStampMilli();
//		this.startTime=new DateTime(task.getStartTimeMilli());
		this.startTimeMilli=task.getStartTimeMilli();
//		this.startTimeZone=task.getStartTimeZone();
		
		if (task.getRepeatSetup() != null)
			this.repeatSetup = new RepeatSetupUI(task.getRepeatSetup()); 
		
//		this.taskCreator = new Profile(task.getTaskCreator().getId());
		this.taskCreator = new UserUI(task.getTaskCreator());
		
		if (task.getAppleIOSCalEvent() != null)
			this.appleIOSCallEvent = new AppleIOSCallEventUI();
		
		calendars = new HashSet<CalendarUI>(task.getCalendars().size());
		for (Calendar cal:task.getCalendars())
		{
			calendars.add(new CalendarUI(cal));
		}
		
		this.shares = new ArrayList<ShareUI>(shares.size());
		for (Share share : shares)
		{
			this.shares.add(new ShareUI(share));
		}

		this.alarms = new ArrayList<AlarmUI>(alarms.size());
		for (Alarm alarm : alarms)
		{
			this.alarms.add(new AlarmUI(alarm));
		}
		
		this.taskRepeats = new ArrayList<TaskRepeatUI>(taskRepeats.size());
		for (TaskRepeat taskRepeat : taskRepeats)
		{
			this.taskRepeats.add(new TaskRepeatUI(taskRepeat));
		}
	}
	
	public TaskUI(TaskTemplate task, List<Share> shares, List<Alarm> alarms, List<TaskRepeat> taskRepeats)
	{
		super(task.getId(), task.getLastModifiedDeviceId(), task.getCreatedDate(), task.getModifiedDate(), task.getCreatedDateZone(), task.getModifiedDateZone(), task.getClientUpdateTimeStamp());
	
		this.who=null;
		this.title=task.getTitle();
		this.notes=task.getNotes();
		this.taskLocation=task.getTaskLocation();
//		this.modelName=task.getModelName();
		this.parentId=task.getParentId();
		
		this.shareStatus=task.getShareStatus();
		this.subTasksCount=task.getSubTasksCount();
		this.sortOrder=task.getSortOrder();
		this.priority=task.getPriority();
		this.progress=task.getProgress();
		
		this.hasBeenShared=task.isHasBeenShared();
		this.timeLessTask=task.isTimeLessTask();
		this.startTimeLessTask=task.isStartTimeLessTask();
		this.aParent=task.isaParent();
		this.show=task.isShow();
		this.publicTask=task.isPublicTask();
		
//		this.endTime=new DateTime(task.getEndTimeMilli());
		this.endTimeMilli=task.getEndTimeMilli();
//		this.endTimeZone=task.getEndTimeZone();
//		this.sortTimeStamp=new DateTime(task.getSortTimeStampMilli());
		this.sortTimeStampMilli=task.getSortTimeStampMilli();
//		this.startTime=new DateTime(task.getStartTimeMilli());
		this.startTimeMilli=task.getStartTimeMilli();
//		this.startTimeZone=task.getStartTimeZone();
		
		this.repeatSetup = null;
		this.taskCreator = new UserUI(task.getTaskCreator());
//		this.taskCreator = new Profile(task.getTaskCreator().getId());
		
		calendars = new HashSet<CalendarUI>();
		for (Calendar cal:task.getCalendars())
		{
			calendars.add(new CalendarUI(cal));
		}
		
		this.shares = new ArrayList<ShareUI>(shares.size());
		for (Share share : shares)
		{
			this.shares.add(new ShareUI(share));
		}

		this.alarms = new ArrayList<AlarmUI>(alarms.size());
		for (Alarm alarm : alarms)
		{
			this.alarms.add(new AlarmUI(alarm));
		}
		
		this.taskRepeats = new ArrayList<TaskRepeatUI>(taskRepeats.size());
		for (TaskRepeat taskRepeat : taskRepeats)
		{
			this.taskRepeats.add(new TaskRepeatUI(taskRepeat));
		}
	}

	public String getWho()
	{
		return who;
	}

	public String getTitle()
	{
		return title;
	}

	public String getNotes()
	{
		return notes;
	}

	public String getTaskLocation()
	{
		return taskLocation;
	}

//	public String getModelName()
//	{
//		return modelName;
//	}

	public String getParentId()
	{
		return parentId;
	}

	public int getShareStatus()
	{
		return shareStatus;
	}

	public int getSubTasksCount()
	{
		return subTasksCount;
	}

	public int getSortOrder()
	{
		return sortOrder;
	}

	public int getPriority()
	{
		return priority;
	}

	public int getProgress()
	{
		return progress;
	}

	public boolean isHasBeenShared()
	{
		return hasBeenShared;
	}

	public boolean isTimeLessTask()
	{
		return timeLessTask;
	}

	public boolean isStartTimeLessTask()
	{
		return startTimeLessTask;
	}

	public boolean isaParent()
	{
		return aParent;
	}

	public boolean isShow()
	{
		return show;
	}

	public boolean isPublicTask()
	{
		return publicTask;
	}

	public long getEndTimeMilli()
	{
		return endTimeMilli;
	}

	public long getSortTimeStampMilli()
	{
		return sortTimeStampMilli;
	}


	public long getStartTimeMilli()
	{
		return startTimeMilli;
	}


	public RepeatSetupUI getRepeatSetup()
	{
		return repeatSetup;
	}


	public List<ShareUI> getShares()
	{
		return shares;
	}

	public List<AlarmUI> getAlarms()
	{
		return alarms;
	}

	public List<TaskRepeatUI> getTaskRepeats()
	{
		return taskRepeats;
	}

	public Set<CalendarUI> getCalendars()
	{
		return calendars;
	}

	void setWho(String who)
	{
		this.who = who;
	}

	void setTitle(String title)
	{
		this.title = title;
	}

	void setNotes(String notes)
	{
		this.notes = notes;
	}

	void setTaskLocation(String taskLocation)
	{
		this.taskLocation = taskLocation;
	}

//	void setModelName(String modelName)
//	{
//		this.modelName = modelName;
//	}

	void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	void setShareStatus(int shareStatus)
	{
		this.shareStatus = shareStatus;
	}

	void setSubTasksCount(int subTasksCount)
	{
		this.subTasksCount = subTasksCount;
	}

	void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	void setPriority(int priority)
	{
		this.priority = priority;
	}

	void setProgress(int progress)
	{
		this.progress = progress;
	}

	void setHasBeenShared(boolean hasBeenShared)
	{
		this.hasBeenShared = hasBeenShared;
	}

	void setTimeLessTask(boolean timeLessTask)
	{
		this.timeLessTask = timeLessTask;
	}

	void setStartTimeLessTask(boolean startTimeLessTask)
	{
		this.startTimeLessTask = startTimeLessTask;
	}

	void setaParent(boolean aParent)
	{
		this.aParent = aParent;
	}

	void setShow(boolean show)
	{
		this.show = show;
	}

	void setPublicTask(boolean publicTask)
	{
		this.publicTask = publicTask;
	}
	
	void setEndTimeMilli(long endTimeMilli)
	{
		this.endTimeMilli = endTimeMilli;
	}


	void setSortTimeStampMilli(long sortTimeStampMilli)
	{
		this.sortTimeStampMilli = sortTimeStampMilli;
	}

	void setStartTimeMilli(long startTimeMilli)
	{
		this.startTimeMilli = startTimeMilli;
	}

	void setRepeatSetup(RepeatSetupUI repeatSetup)
	{
		this.repeatSetup = repeatSetup;
	}


	void setShares(List<ShareUI> shares)
	{
		this.shares = shares;
	}

	void setAlarms(List<AlarmUI> alarms)
	{
		this.alarms = alarms;
	}

	void setTaskRepeats(List<TaskRepeatUI> taskRepeats)
	{
		this.taskRepeats = taskRepeats;
	}

	void setCalendars(Set<CalendarUI> calendars)
	{
		this.calendars = calendars;
	}

	public AppleIOSCallEventUI getAppleIOSCallEvent() {
		return appleIOSCallEvent;
	}

	public void setAppleIOSCallEvent(AppleIOSCallEventUI appleIOSCallEvent) {
		this.appleIOSCallEvent = appleIOSCallEvent;
	}

	
	
	
	
	
}
