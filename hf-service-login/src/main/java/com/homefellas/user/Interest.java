package com.homefellas.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="u_intersts")
@Proxy(lazy=false)
@XmlRootElement
public class Interest extends AbstractSequenceModel
{
	private String interestName;
	
	@JsonBackReference("intersts")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="extendedProfileId")
	private ExtendedProfile extendedProfile;

	/**
	 * This is the interest of the user.  This is free form text.
	 * @return interest
	 */
	public String getInterestName()
	{
		return interestName;
	}

	public void setInterestName(String interest)
	{
		this.interestName = interest;
	}

	/**
	 * This is the extended profile that relates back to.
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
				+ ((interestName == null) ? 0 : interestName.hashCode());
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
		Interest other = (Interest) obj;
		if (interestName == null)
		{
			if (other.interestName != null)
				return false;
		}
		else if (!interestName.equals(other.interestName))
			return false;
		return true;
	}

	
	
	
	

}
