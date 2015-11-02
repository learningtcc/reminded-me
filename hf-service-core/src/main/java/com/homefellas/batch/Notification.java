package com.homefellas.batch;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="notification_queue")
@Proxy(lazy=false)
@XmlRootElement
public class Notification extends AbstractSequenceModel
{

	private long toSendTime;
	
	@Column(nullable=false)
	private String sendTo;
	
	@Column(nullable=false)
	private String sendFrom;
	
	private String sendFromAlias;
	
	private String replyTo;
	
	private String subject;
	
	@Column(nullable=false)
	@Lob
	private String body;
	
	private long dateSent;
	
	@Column(nullable=false)
	private int pushTypeOrdinal=PushTypeEnum.NONE.ordinal();
	
	@Column(nullable=false)
	private int notificationTypeOrdinal=NotificationTypeEnum.EMAIL.ordinal();
	
	@Column(nullable=false)
	private int priority=5;
	
	private int sentStatusOrdinal = NotificationStatusEnum.QUEUED.ordinal();
	
	private String iNotificationId;
	
	private String iNotificationClassName;

	private String error;
	
	//these three fields are for creating client notifications.  They need to be set in order for emails to go out.
	@Transient
	private String toProfileId;
	
	@Transient
	private String fromProfileId;
	
	@Transient
	private String clientNotificationType;
	
	public long getToSendTime()
	{
		return toSendTime;
	}

	public void setToSendTime(long toSendTime)
	{
		this.toSendTime = toSendTime;
	}

	public String getSendTo()
	{
		return sendTo;
	}

	public void setSendTo(String sendTo)
	{
		this.sendTo = sendTo;
	}

	public String getSendFrom()
	{
		return sendFrom;
	}

	public void setSendFrom(String sendFrom)
	{
		this.sendFrom = sendFrom;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public long getDateSent()
	{
		return dateSent;
	}

	public void setDateSent(long dateSent)
	{
		this.dateSent = dateSent;
	}

	public int getPushTypeOrdinal()
	{
		return pushTypeOrdinal;
	}

	public void setPushTypeOrdinal(int pushTypeOrdinal)
	{
		this.pushTypeOrdinal = pushTypeOrdinal;
	}

	public int getNotificationTypeOrdinal()
	{
		return notificationTypeOrdinal;
	}

	public void setNotificationTypeOrdinal(int notificationTypeOrdinal)
	{
		this.notificationTypeOrdinal = notificationTypeOrdinal;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public int getSentStatusOrdinal()
	{
		return sentStatusOrdinal;
	}

	public void setSentStatusOrdinal(int sentStatusOrdinal)
	{
		this.sentStatusOrdinal = sentStatusOrdinal;
	}

	public String getiNotificationId()
	{
		return iNotificationId;
	}

	public void setiNotificationId(String iNotificationId)
	{
		this.iNotificationId = iNotificationId;
	}

	public String getiNotificationClassName()
	{
		return iNotificationClassName;
	}

	public void setiNotificationClassName(String iNotificationClassName)
	{
		this.iNotificationClassName = iNotificationClassName;
	}

	public void setINotification(INotifiable iNotification)
	{
		this.iNotificationClassName = iNotification.getClassName();
		this.iNotificationId = iNotification.getNotificationId();
	}

	public String getReplyTo()
	{
		return replyTo;
	}

	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}

	public String getSendFromAlias()
	{
		return sendFromAlias;
	}

	public void setSendFromAlias(String sendFromAlias)
	{
		this.sendFromAlias = sendFromAlias;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public String getToProfileId()
	{
		return toProfileId;
	}

	public void setToProfileId(String toProfileId)
	{
		this.toProfileId = toProfileId;
	}

	public String getFromProfileId()
	{
		return fromProfileId;
	}

	public void setFromProfileId(String fromProfileId)
	{
		this.fromProfileId = fromProfileId;
	}

	public String getClientNotificationType()
	{
		return clientNotificationType;
	}

	public void setClientNotificationType(String clientNotificationType)
	{
		this.clientNotificationType = clientNotificationType;
	}
	

	
	
}
