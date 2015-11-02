package com.homefellas.rm.task;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.codehaus.enunciate.json.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.ui.context.Theme;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.rm.ISynchronizeable;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.share.ShareApprovedStatus;
import com.homefellas.user.Profile;
import com.homefellas.ws.core.JodaDateTimeJsonDeSerializer;
import com.homefellas.ws.core.JodaDateTimeJsonSerializer;

@MappedSuperclass
public abstract class AbstractTask extends AbstractGUIDModel
{
	@Transient
	protected int shareStatus = ShareApprovedStatus.OWNER_TASK.ordinal();
	
	//this was named incorrectly and should be changed at some point 
	@Column(name="been_subtasked",nullable=false)
	protected boolean hasBeenShared=false;
	
	@Column(name="sub_task_count",nullable=false)
	protected int subTasksCount=0;
	
//	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="task",orphanRemoval=true)
//	@JsonManagedReference("task-repeats")
//	protected Set<TaskRepeat> taskRepeats = new HashSet<TaskRepeat>();
	
	@Column
	protected boolean timeLessTask;
	
	@Column
	protected boolean startTimeLessTask;
	
	@Column(nullable=false)
	@JsonProperty
	/**
	 * This is the title of the task.  It can not be null. 
	 */
	protected String title;
	
	/**
	 * This is a flag used to mark whether or not a task is marked as a parent.  This is used to speed up the query as if it
	 * is not a parent there is no reaso to check 
	 */
	@Column(nullable=false)
	protected boolean aParent=false;
	
	@Columns(columns={@Column(name="sortTimeStamp",insertable=true,updatable=true),@Column(name="sortTimeStampzone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime sortTimeStamp;

	
	protected long sortTimeStampMilli;
	
	/**
	 * This is the start time of the task.  It should include both the time and the time zone.  The timezone will be 
	 * split out to a separate column on save.
	 */
	@Columns(columns={@Column(name="startTime",insertable=true,updatable=true),@Column(name="startTimezone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime startTime;
	
	
	protected int sortOrder;
	
	protected long startTimeMilli;
	
	@Column(name="realStartTimeZone")
	protected String startTimeZone;
	
	@Column(name="realEndTimeZone")
	protected String endTimeZone;
	
	@Column(length=2000)
	protected String notes;
	
	/**
	 * This is the end time or the due date of the task.  It should include both the time and the time zone.  the time zone will 
	 * be split out to a separate column on save.
	 */
	@Columns(columns={@Column(name="endTime",insertable=true,updatable=true),@Column(name="endTimezone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime endTime;
	
	protected long endTimeMilli;
		
	/**
	 * This is used on the presentation tier to indicate to the client whether or not to show the task.  If it is marked as false then you should 
	 * hide this task from the user.  The default is true and it can not be null.
	 */
	@Column(nullable=false, name="showTask")
	protected boolean show=true;
	
	/**
	 * This is used to indicate whether or not the user wants to show the task publicly.  It is up to the client to use or not use this field. 
	 * The default value is true and it can not be full.
	 */
	@Column(nullable=false)
	protected boolean publicTask=true;
	
	/**
	 * This is for the user to put where the task is to take place.  In the future this may be transformed into a location field, but for now
	 * it is free form text.
	 */
	@Column(name="whereLocation")
	protected String taskLocation;
	
	/**
	 * This is the priority of the task.  The value should be the ordinal of the PriorityEnum.  The default is Medium.  It can not be null.
	 * @see PriorityEnum
	 */
	@Column(nullable=false,name="taskPriority")
	protected int priority = PriorityEnum.MEDIUM.ordinal();
	
	/**
	 * This is the progress of the task.  The value should be the ordinal of the ProgressEnum.  the default is open.  It can not be null.
	 * @see ProgressEnum
	 */
	@Column(nullable=false, name="progress")
//	@Index(name="progressIndex")
	protected int progress = ProgressEnum.OPEN.ordinal();
	
	/**
	 * This is the model name.  It is used by thte ISynchonizeable interface.  The default is the simple class name (name of class w/o package).
	 * This value has hte setter class there to satisfy the json conversation but will not overwrite the value.
	 * @see ISynchronizeable  
	 */
	@Transient
	protected String modelName = getClass().getSimpleName();
	
	/**
	 * This is the person who created the task.  This is required.  The taskCreator.id must be set (rest of the object is not used).
	 * @see Profile
	 */
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="taskCreatorId")
//	@Index(name="creatorIndex")
	protected Profile taskCreator;
	
	
	
	
//	/**
//	 * This is a set of all users who are able to view this task.  The userid is stored in a ternary table along with the task id.  All changes are
//	 * cascaded to that table.  This is not required.
//	 * @see User
//	 */
//	@ManyToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
//	@JoinTable(name="t_taskViewers")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
//	protected Set<User> viewers;
//	
//	/**
//	 * This is a set of all the members that are able to modify the task.  The memberId is sored in a ternary table along with the task id.  All 
//	 * changes are cascaded to that table.  This is not required.
//	 * @see Member
//	 */
//	@ManyToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
//	@JoinTable(name="t_taskEditors")
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
//	protected Set<Member> editors;
	
	protected String parentId;
	
//	@OneToMany(fetch=FetchType.EAGER,mappedBy="parentTask")
//	@JsonManagedReference("subtaskes")	
//	protected List<SubTask> subTaskes;
	
	/**
	 * This is the theme for the task.  It is not required.
	 * @see Theme
	 */
//	@ManyToOne(fetch=FetchType.EAGER,optional=true)
//	@JoinColumn(name="themeId")
//	protected Theme theme;
	
	@Column(nullable=true)
	protected String lastModifiedDeviceId;
	
//	protected Note note;
	
	//how?
	//progress wall?
	
	public enum PriorityEnum { HIGH, MEDIUM, LOW }
	public enum ProgressEnum {OPEN, STARTED, WORKING_ON_IT, ON_HOLD, ALMOST_THERE, RUNNING_LATE, DONE, DELETE};
	
	public AbstractTask()
	{
		
	}
	
	public AbstractTask(String id)
	{
		super.id = id;
	}
	

	/**
	 * This is the title of the task.  It can not be null.
	 * @return somethng to return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This sets a title
	 * @param title is some more document
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getStartTimeZone()
	{
		return startTimeZone;
	}

	public void setStartTimeZone(String startTimeZone)
	{
		this.startTimeZone = startTimeZone;
	}

	public String getEndTimeZone()
	{
		return endTimeZone;
	}

	public void setEndTimeZone(String endTimeZone)
	{
		this.endTimeZone = endTimeZone;
	}
	
	/**
	 * This is a flag used to mark whether or not a task is marked as a parent.  This is used to speed up the query as if it
	 * is not a parent there is no reaso to check 
	 * @return the aParent
	 */
	public boolean isaParent()
	{
		return aParent;
	}


	/**
	 * @param aParent the aParent to set
	 */
	public void setaParent(boolean aParent)
	{
		this.aParent = aParent;
	}

	

	/**
	 * This is the start time of the task.  It should be set in ISO standard time.  It should inlcude the time zone which will be saved in a separate column.
	 * @return startTime
	 */
	public DateTime getStartTime()
	{
		return startTime;
	}

	public void setStartTime(DateTime startTime)
	{
		this.startTime = startTime;
		if (startTime!=null)
			this.startTimeMilli = startTime.getMillis();
	}

	/**
	 * This is the end time or the due date of the task. It should be set in ISO standard time.
	 * @return endTime
	 */
	public DateTime getEndTime()
	{
		return endTime;
	}

	public void setEndTime(DateTime endTime)
	{
		this.endTime = endTime;
		if (endTime!=null)
			this.endTimeMilli = endTime.getMillis();
	}

	/**
	 * This is used to indicate whether or not the user wants to show the task publicly.  It is up to the client to use or not use this field. 
	 * The default value is true and it can not be null.
	 * @return publicTask
	 */
	public boolean isPublicTask() {
		return publicTask;
	}

	public void setPublicTask(boolean publicTask) {
		this.publicTask = publicTask;
	}

	

	/**
	 * This is for the user to put where the task is to take place.  In the future this may be transformed into a location field, but for now
	 * it is free form text.  This is also known as the where
	 * @return the taskLocation
	 */
	public String getTaskLocation()
	{
		return taskLocation;
	}

	/**
	 * @param taskLocation the taskLocation to set
	 */
	public void setTaskLocation(String taskLocation)
	{
		this.taskLocation = taskLocation;
	}

	/**
	 * This is the priority of the task.  The value should be the ordinal of the PriorityEnum.  The default is Medium.  It can not be null.
	 * @return priority
	 * @see PriorityEnum
	 */
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	
	/**
	 * This is the person who created the task.  This is required.  The taskCreator.id must be set (rest of the object is not used).
	 * @see Profile
	 * @return Profile
	 */
	public Profile getTaskCreator() {
		return taskCreator;
	}

	public void setTaskCreator(Profile taskCreator) {
		this.taskCreator = taskCreator;
	}

//	/**
//	 * This is a set of all users who are able to view this task.  The userid is stored in a ternary table along with the task id.  All changes are
//	 * cascaded to that table.  This is not required.
//	 * @see User
//	 * @return Set<User>
//	 */
//	public Set<User> getViewers() {
//		return viewers;
//	}
//
//	public void setViewers(Set<User> viewers) {
//		this.viewers = viewers;
//	}
//
//	/**
//	 * This is a set of all the members that are able to modify the task.  The memberId is sored in a ternary table along with the task id.  All 
//	 * changes are cascaded to that table.  This is not required.
//	 * @see Member
//	 * @return Set<Member>
//	 */
//	public Set<Member> getEditors() {
//		return editors;
//	}
//
//	public void setEditors(Set<Member> editors) {
//		this.editors = editors;
//	}

	

	/**
	 * This is used on the presentation tier to indicate to the client whether or not to show the task.  If it is marked as false then you should 
	 * hide this task from the user.  The default is true and it can not be null.
	 * @return
	 */
	public boolean isShow()
	{
		return show;
	}

	public void setShow(boolean show)
	{
		this.show = show;
	}

	/**
	 * This is the progress of the task.  The value should be the ordinal of the ProgressEnum.  the default is open.  It can not be null.
	 * @see ProgressEnum
	 * @return progress
	 */
	public int getProgress()
	{
		return progress;
	}

	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	

	/**
	 * This is the theme for the task.  It is not required.
	 * @return theme
	 */
//	public Theme getTheme()
//	{
//		return theme;
//	}
//
//	public void setTheme(Theme theme)
//	{
//		this.theme = theme;
//	}

//	public List<SubTask> getSubTaskes()
//	{
//		return subTaskes;
//	}
//
//	public void setSubTaskes(List<SubTask> subTaskes)
//	{
//		this.subTaskes = subTaskes;
//	}
	
	
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "taskCreator"; 
	}

	/**
	 * This is not used by the client.  It's required for any object that needs to be synchronized.  It's default value is the Class Name.
	 * @return modelName
	 */
	@Override
	@JsonSerialize
	public String getModelName()
	{
		return modelName;
	}

	@Override
	public void setModelName(String modelName)
	{
		
	}
	
	
	
	
	
	/**
	 * This is the milliseconds resprentation of startTime.  This should not be set directly by the client and is only used by the database.
	 * @return the startTimeMilli
	 */
	public long getStartTimeMilli()
	{
		return startTimeMilli;
	}

	/**
	 * @param startTimeMilli the startTimeMilli to set
	 */
	public void setStartTimeMilli(long startTimeMilli)
	{
		this.startTimeMilli = startTimeMilli;
	}

	/**
	 * This is the milliseconds resprentation of endTime.  This should not be set directly by the client and is only used by the database.
	 * @return the endTimeMilli
	 */
	public long getEndTimeMilli()
	{
		return endTimeMilli;
	}

	/**
	 * @param endTimeMilli the endTimeMilli to set
	 */
	public void setEndTimeMilli(long endTimeMilli)
	{
		this.endTimeMilli = endTimeMilli;
	}
	
	

	/**
	 * This is the sortTimeStamp used by the client side.  Not sure of its function
	 * @return the sortTimeStamp
	 */
	public DateTime getSortTimeStamp()
	{
		return sortTimeStamp;
	}

	/**
	 * @param sortTimeStamp the sortTimeStamp to set
	 */
	public void setSortTimeStamp(DateTime sortTimeStamp)
	{
		this.sortTimeStamp = sortTimeStamp;
		if (sortTimeStamp!=null)
			this.sortTimeStampMilli = sortTimeStamp.getMillis();
	}

	/**
	 * This is the milliseconds resprentation of sortTime.  This should not be set directly by the client and is only used by the database.
	 * @return the sortTimeStampMilli
	 */
	public long getSortTimeStampMilli()
	{
		return sortTimeStampMilli;
	}

	/**
	 * @param sortTimeStampMilli the sortTimeStampMilli to set
	 */
	public void setSortTimeStampMilli(long sortTimeStampMilli)
	{
		this.sortTimeStampMilli = sortTimeStampMilli;
	}
	
	

	public int getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + priority;
		result = prime * result + progress;
		result = prime * result + (publicTask ? 1231 : 1237);
		result = prime * result + (show ? 1231 : 1237);
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{ 
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTask other = (AbstractTask) obj;
		if (endTime == null)
		{
			if (other.endTime != null)
				return false;
		}
		else if (!endTime.equals(other.endTime))
			return false;
		if (priority != other.priority)
			return false;
		if (progress != other.progress)
			return false;
		if (publicTask != other.publicTask)
			return false;
		if (show != other.show)
			return false;
		if (startTime == null)
		{
			if (other.startTime != null)
				return false;
		}
		else if (!startTime.equals(other.startTime))
			return false;
		if (title == null)
		{
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		
		return true;
	}


	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}

	

	public boolean isTimeLessTask()
	{
		return timeLessTask;
	}

	public void setTimeLessTask(boolean timeLessTask)
	{
		this.timeLessTask = timeLessTask;
	}

	


//	public Set<TaskRepeat> getTaskRepeats()
//	{
//		return taskRepeats;
//	}
//
//	public void setTaskRepeats(Set<TaskRepeat> taskRepeats)
//	{
//		this.taskRepeats = taskRepeats;
//	}

	@JsonIgnore
	public String getIShareImplClassName()
	{
		return Share.class.getName();
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public int getSubTasksCount()
	{
		return subTasksCount;
	}

	public void setSubTasksCount(int subTasksCount)
	{
		this.subTasksCount = subTasksCount;
	}

	@JsonIgnore
	public boolean isHasBeenSubTasked()
	{
		if (subTasksCount>0)
			return true;
		else
			return false;
	}

	public int getShareStatus()
	{
		return shareStatus;
	}

	public void setShareStatus(int shareStatus)
	{
		this.shareStatus = shareStatus;
	}

	public boolean isHasBeenShared()
	{
		return hasBeenShared;
	}

	public void setHasBeenShared(boolean hasBeenShared)
	{
		this.hasBeenShared = hasBeenShared;
	}

	public boolean isStartTimeLessTask()
	{
		return startTimeLessTask;
	}

	public void setStartTimeLessTask(boolean startTimeLessTask)
	{
		this.startTimeLessTask = startTimeLessTask;
	}

	public void incrementSubTaskCount()
	{
		subTasksCount++;
	}
	
	@JsonIgnore
	public boolean isASubTask()
	{
		if (parentId==null || parentId.equals(""))
			return false;
		else 
			return true;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	
	
}
