package com.homefellas.rm.notification;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.batch.Notification;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.rm.ISynchronizeable;
import com.homefellas.user.Member;

@Entity
@Table(name="n_clientnotifications")
@Proxy(lazy=false)
@XmlRootElement
public class ClientNotification extends AbstractGUIDModel implements ISynchronizeable
{
	private String referenceId;
	private String referenceClassName;
	private String notificationType = ClientNotificationTypeEnum.undefined.toString();	
	private String notificationStatus = ClientNotificationStatusEnum.unread.toString();
	
	private String lastModifiedDeviceId;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="memberId")
	@Index(name="clientNotificationMemberIndex")
	private Member member;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="notificationId")
	private Notification notification;
	
	private boolean sentNotification=false;
	
	public enum ClientNotificationStatusEnum { unread, read, deleted  }
	public enum ClientNotificationTypeEnum {undefined, taskShareRecieved, sublistShareRecieved, listShare, taskUpdated, taskCancelled, newTaskAddedToList, taskShareAccepted, taskShareDeclined, sublistShareAccepted, sublistShareDeclined, listShareAccepted, listShareDeclined, taskIsDue, newTaskAddedToSublist }
	public String getReferenceId()
	{
		return referenceId;
	}

	public void setReferenceId(String referenceId)
	{
		this.referenceId = referenceId;
	}

	public String getReferenceClassName()
	{
		return referenceClassName;
	}

	public void setReferenceClassName(String referenceClassName)
	{
		this.referenceClassName = referenceClassName;
	}



	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "member";
	}

	@Override
	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	@Override
	public void setLastModifiedDeviceId(String deviceId)
	{
		this.lastModifiedDeviceId = deviceId;
	}

	@Override
	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}

	public String getNotificationType()
	{
		return notificationType;
	}

	public void setNotificationType(String notificationType)
	{
		this.notificationType = notificationType;
	}

	public Member getMember()
	{
		return member;
	}

	public void setMember(Member member)
	{
		this.member = member;
	}

	public Notification getNotification()
	{
		return notification;
	}

	public void setNotification(Notification notification)
	{
		this.notification = notification;
	}

	public boolean isSentNotification()
	{
		return sentNotification;
	}

	public void setSentNotification(boolean sentNotification)
	{
		this.sentNotification = sentNotification;
	}

	public String getNotificationStatus()
	{
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus)
	{
		this.notificationStatus = notificationStatus;
	}
	
	
}
