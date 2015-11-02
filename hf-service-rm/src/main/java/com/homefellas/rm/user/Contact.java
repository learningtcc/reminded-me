package com.homefellas.rm.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
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
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractSequenceModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.ISynchronizeable;
import com.homefellas.rm.ISynchronizeableFiltered;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;

@Entity
@Table(name="u_contacts")
@Proxy(lazy=false)
@XmlRootElement
public class Contact extends AbstractGUIDModel implements ISynchronizeableFiltered, IGenericSynchroinzedLifeCycle, Comparable<Contact>
{

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="contactId")
	@Index(name="contactContactIndex")
	private Profile contact;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="contactOwnerId")
	@Index(name="contactOwnerContactIndex")
	private Member contactOwner;
	
	@Column(nullable=false)
	private int contactCounter=99;

	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	private int statusOrdinal = ContactStatusEnum.ACTIVE.ordinal();
	
	@Transient
	private int displayOrder=0;
	
	@Transient
	private boolean aContact = true;
		
	public enum ContactStatusEnum {ACTIVE, DELETED}
	 
	private String source; 
		
	
	public Contact()
	{
		
	}
		
	public void incrementCounter()
	{
		contactCounter++;
	}

	public int getContactCounter()
	{
		return contactCounter;
	}

	public void setContactCounter(int contactCounter)
	{
		this.contactCounter = contactCounter;
	}

	public Profile getContact()
	{
		return contact;
	}

	public void setContact(Profile contact)
	{
		this.contact = contact;
	}

	public Member getContactOwner()
	{
		return contactOwner;
	}

	public void setContactOwner(Member contactOwner)
	{
		this.contactOwner = contactOwner;
	}

	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "contactOwner";
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

	@Override
	public void validate() throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (!isPrimaryKeySet())
			codes.add(ValidationCodeEnum.PK_NOT_SET);
		
		if (contact==null||!contact.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		
		if (contactOwner==null || !contactOwner.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.CONTACT_OWNER_IS_NULL);
		
		if (!codes.isEmpty())
			throw new ValidationException(codes);
	}

	@Override
	@JsonIgnore
	public String getEmailForAuthorization()
	{
		return contactOwner.getEmail();
	}

	@Override
	@JsonIgnore
	public void markForDeletion()
	{
		statusOrdinal = ContactStatusEnum.DELETED.ordinal();
	}

	@Override
	@JsonIgnore
	public String getDeleteStatusField()
	{
		return "statusOrdinal";
	}

	@Override
	@JsonIgnore
	public int getDeletedTrueValue()
	{
		return ContactStatusEnum.DELETED.ordinal();
	}

	@Override
	@JsonIgnore
	public boolean isStatusExclusive()
	{
		return true;
	}

	@Override
	public int compareTo(Contact o)
	{
		if (getDisplayOrder() == o.getDisplayOrder())
		{
			if (getContactCounter() == o.getContactCounter())
			{
				if (getContact()==null || o.getContact()==null)
					return 0;
				
				String name = getContact().getName();
				String compareName = o.getContact().getName();
				
				if (name==null||compareName==null)
					return 0;
				
				return name.compareToIgnoreCase(compareName);
			}
			else if (getContactCounter() > o.getContactCounter())
				return -1;
			else
				return 1;
		}
		else if (getDisplayOrder() > o.getDisplayOrder())
			return 1;
		else 
			return -1;
	}

	public int getDisplayOrder()
	{
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder)
	{
		this.displayOrder = displayOrder;
	}

	public int getStatusOrdinal()
	{
		return statusOrdinal;
	}

	public void setStatusOrdinal(int statusOrdinal)
	{
		this.statusOrdinal = statusOrdinal;
	}

	public boolean isaContact()
	{
		return aContact;
	}

	public void setaContact(boolean aContact)
	{
		this.aContact = aContact;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}
	
	@JsonIgnore
	public String getEmail()
	{
		if (contact==null||contact.getMember()==null)
			return null;
		
		return contact.getMember().getEmail();
	}
	
}
