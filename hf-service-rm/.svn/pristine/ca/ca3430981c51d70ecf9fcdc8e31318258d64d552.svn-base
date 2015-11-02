package com.homefellas.rm.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

import com.homefellas.batch.INotifiable;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.rm.ISynchronizeableInitialized;
import com.homefellas.user.Profile;

@XmlRootElement
@Entity
@Table(name="u_devices")
public class Device extends AbstractGUIDModel implements ISynchronizeableInitialized, INotifiable
{
	@Column(nullable=false)
	private int pushTypeOrdinal = PushTypeEnum.NONE.ordinal();
	
	private String description;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="profileId")
	@Index(name="deviceProfileIndex")
	private Profile profile;
	
	private String lastModifiedDeviceId;
	
	private boolean production=true;
	
	public Device()
	{
		
	}
	
	public Device(String id)
	{
		super.id = id;
	}
	
	public int getPushTypeOrdinal()
	{
		return pushTypeOrdinal;
	}

	public void setPushTypeOrdinal(int pushTypeOrdinal)
	{
		this.pushTypeOrdinal = pushTypeOrdinal;
	}

	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "profile";
	}

	@Override
	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	@Override
	public void setLastModifiedDeviceId(String deviceId)
	{
		this.lastModifiedDeviceId=deviceId;
	}

	@Override
	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	public boolean isProduction()
	{
		return production;
	}

	public void setProduction(boolean production)
	{
		this.production = production;
	}
	
	@Override
	@JsonIgnore
	public String getNotificationId()
	{
		return getId();
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
}
