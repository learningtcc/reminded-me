package com.homefellas.rm.share;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractSequenceModel;
import com.homefellas.rm.ISynchronizeable;
import com.homefellas.user.Member;

@Entity
@Table(name="s_invites")
@Proxy(lazy=false)
@XmlRootElement
public class Invite extends AbstractGUIDModel implements ISynchronizeable
{
	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	@Column(nullable=false)
	private String shareClassName;
	
	@Column(nullable=false)
	private String shareId;
	
	@Column(nullable=false, length=5000)
	private String message;
	
	@Column(nullable=false)
	private String subject;
		
	@Column(nullable=false)
	private int pushType=PushTypeEnum.NONE.ordinal();

	@Column(nullable=false)
	private int notificationType=NotificationTypeEnum.EMAIL.ordinal();
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="sharedUserId")
	@Index(name="inviteUserIndex")
	private Member inviter;
	
	@Transient
	private Map<String, String> emailAddresses;
		
	@Transient
	private String directLink;
	
	
	
	public Invite()
	{
		this.id = generateUnquieId();
	}
	
	

	public Member getInviter()
	{
		return inviter;
	}



	public void setInviter(Member inviter)
	{
		this.inviter = inviter;
	}



	public String getDirectLink()
	{
		return directLink;
	}



	public void setDirectLink(String directLink)
	{
		this.directLink = directLink;
	}



	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public int getPushType()
	{
		return pushType;
	}

	public void setPushType(int pushType)
	{
		this.pushType = pushType;
	}

	public int getNotificationType()
	{
		return notificationType;
	}

	public void setNotificationType(int notificationType)
	{
		this.notificationType = notificationType;
	}

	public String getShareClassName()
	{
		return shareClassName;
	}

	public void setShareClassName(String shareClassName)
	{
		this.shareClassName = shareClassName;
	}

	public String getShareId()
	{
		return shareId;
	}

	public void setShareId(String shareId)
	{
		this.shareId = shareId;
	}



	public Map<String, String> getEmailAddresses()
	{
		return emailAddresses;
	}



	public void setEmailAddresses(Map<String, String> emailAddresses)
	{
		this.emailAddresses = emailAddresses;
	}



	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((inviter == null) ? 0 : inviter.hashCode());
		result = prime * result
				+ ((shareClassName == null) ? 0 : shareClassName.hashCode());
		result = prime * result + ((shareId == null) ? 0 : shareId.hashCode());
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
		Invite other = (Invite) obj;
		if (inviter == null)
		{
			if (other.inviter != null)
				return false;
		}
		else if (!inviter.equals(other.inviter))
			return false;
		if (shareClassName == null)
		{
			if (other.shareClassName != null)
				return false;
		}
		else if (!shareClassName.equals(other.shareClassName))
			return false;
		if (shareId == null)
		{
			if (other.shareId != null)
				return false;
		}
		else if (!shareId.equals(other.shareId))
			return false;
		return true;
	}

	@JsonIgnore
	public boolean isMessageNull()
	{
		if (message==null||"".equals(message.trim()))
			return true;
		else
			return false;
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
		return "inviter";
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

	
}
