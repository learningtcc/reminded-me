package com.homefellas.rm.share;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.batch.INotifiable;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractSequenceModel;
import com.homefellas.rm.ISynchronizeable;
import com.homefellas.rm.ISynchronizeableFiltered;
import com.homefellas.rm.ITaskDependable;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;

@Entity
@Table(name="s_taskshares", uniqueConstraints = { @UniqueConstraint( columnNames = { "taskId", "sharedUserId" } ) })
//uniqueConstraints={@UniqueConstraint(name="share_user_task_uq",columnNames={"taskId", "sharedUserId"})}
@Proxy(lazy=false)
@XmlRootElement
public class Share extends AbstractGUIDModel implements INotifiable, ISynchronizeableFiltered, IShare, ITaskDependable
{

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="taskId")
	@Index(name="shareTaskIndex")
	private Task task;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="sharedUserId")
	@Index(name="shareUserIndex")
	
	private Member user;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
//	@JoinColumn(name="inviteId")
//	@JsonBackReference("invite-shares")
	private Invite invite;
	
	@Column(nullable=false)
	private int shareApprovedStatusOrdinal = ShareApprovedStatus.NO_ACTION.ordinal();

	@Column(nullable=false)
	private boolean viewed=false;
		
	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	@Transient
	private boolean blurred=false;
	
	private int status=ShareStatus.UNACTIONED.ordinal();
	
	public enum ShareStatus {UNACTIONED, DELETED, VIEWED};
	
	public Share()
	{
		this.id=generateUnquieId();
	}
	
	/**
	 * This is the task that is linked with the share...that was shared with the user.
	 * @return Task
	 */
	public Task getTask()
	{
		return task;
	}

	public void setTask(Task task)
	{
		this.task = task;
	}

	/**
	 * This is the user that the task has been shared with
	 * @return Member
	 */
	public Member getUser()
	{
		return user;
	}

	public void setUser(Member user)
	{
		this.user = user;
	}

	/**
	 * This is the approval status of the share.  By default it is 0 or NO_ACTIOM
	 * @return a status code
	 * @see ShareApprovedStatus
	 */
	public int getShareApprovedStatusOrdinal()
	{
		return shareApprovedStatusOrdinal;
	}

	public void setShareApprovedStatusOrdinal(int shareApprovedStatusOrdinal)
	{
		this.shareApprovedStatusOrdinal = shareApprovedStatusOrdinal;
	}

	/**
	 * This is the invite that went out to the user to accept the share
	 * @return Invite
	 */
	public Invite getInvite()
	{
		return invite;
	}

	public void setInvite(Invite invite)
	{
		this.invite = invite;
	}
	
	@Override
	@JsonIgnore
	public String getNotificationId()
	{
		return String.valueOf(id);
	}

	@Override
	@JsonIgnore
	public String getClassName()
	{
		return getClass().getName();
	}

	@Override
	@JsonIgnore
	public boolean isCallBack()
	{
		return false;
	}

	/**
	 * This is a flag that should be used by the client to mark whether or not the share has been viewed.
	 * @return
	 */
	public boolean isViewed()
	{
		return viewed;
	}

	public void setViewed(boolean viewed)
	{
		this.viewed = viewed;
	}

	/**
	 * This is another flag that should be used by the client that will mark the share as blurred.  The client is
	 * responsible to set and maintain these flags.
	 * @return booelean
	 */
	public boolean isBlurred()
	{
		return blurred;
	}

	public void setBlurred(boolean blurred)
	{
		this.blurred = blurred;
	}

	@Override
	@JsonIgnore
	public String getModelName()
	{
		return getClass().getSimpleName();
	}

	@Override
	@JsonIgnore
	public void setModelName(String modelName)
	{
		
	}

	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "user";
	}

	@Override
	@JsonIgnore
	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	@Override
	@JsonIgnore
	public void setLastModifiedDeviceId(String deviceId)
	{
		this.lastModifiedDeviceId = deviceId;
	}


	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}

	/**
	 * This is the statu of the share.  It is used mostly for deletion and to mark the share as viewed for the notification
	 * page.
	 * @return
	 */
	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	@Override
	@JsonIgnore
	public void setIShareable(IShareable iShareable)
	{
		this.task = (Task)iShareable;
	}

	@Override
	@JsonIgnore
	public boolean isPublic()
	{
		return task.isPublicTask();
	}

	@Override
	@JsonIgnore
	public String getDeleteStatusField()
	{
		return "status";
	}

	@Override
	@JsonIgnore
	public int getDeletedTrueValue()
	{
		return ShareStatus.DELETED.ordinal();
	}

	@Override
	@JsonIgnore
	public boolean isStatusExclusive()
	{
		return true;
	}
	
	@Override
	@JsonIgnore
	public String getTaskAttributeName()
	{
		return "task";
	}
	
}
