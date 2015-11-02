package com.homefellas.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Proxy;

import com.homefellas.batch.INotifiable;
import com.homefellas.model.core.AbstractSequenceModel;

@XmlRootElement
@Entity
@Table(name="u_friendemails")
@Proxy(lazy=false)
public class FriendEmail extends AbstractSequenceModel implements INotifiable
{

	private String friendEmailAddress;
	
	@JsonBackReference("friendemail")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="extendedProfileId")
	private ExtendedProfile extendedProfile;

	/**
	 * This is the email address of the friend that the user is recommending.
	 * @return friendEmail
	 */
	public String getFriendEmailAddress()
	{
		return friendEmailAddress;
	}

	public void setFriendEmailAddress(String friendEmail)
	{
		this.friendEmailAddress = friendEmail;
	}

	/**
	 * This is hte extended profile that is mapped back to.
	 * @return extendedProfile
	 */
	public ExtendedProfile getExtendedProfile()
	{
		return extendedProfile;
	}

	public void setExtendedProfile(ExtendedProfile extendedProfile)
	{
		this.extendedProfile = extendedProfile;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((friendEmailAddress == null) ? 0 : friendEmailAddress.hashCode());
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
		FriendEmail other = (FriendEmail) obj;
		if (friendEmailAddress == null)
		{
			if (other.friendEmailAddress != null)
				return false;
		}
		else if (!friendEmailAddress.equals(other.friendEmailAddress))
			return false;
		return true;
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

	

	
	
	
}
