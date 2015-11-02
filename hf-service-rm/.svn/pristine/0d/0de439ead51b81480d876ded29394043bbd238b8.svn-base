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
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.ISynchronizeableInitialized;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.user.Member;

@Entity
@Table(name="u_groupcontacts")
@Proxy(lazy=false)
@XmlRootElement
public class GroupContact extends AbstractGUIDModel implements ISynchronizeableInitialized, IGenericSynchroinzedLifeCycle
{
	@Column(nullable=false)
	private String groupName;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="memberId")
	@Index(name="groupContactMember")
	private Member member;

	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	@ManyToMany(fetch=FetchType.EAGER,targetEntity=Contact.class)
	@JoinTable(name="u_contactGroupContacts",joinColumns=@JoinColumn(name="groupId"),inverseJoinColumns=@JoinColumn(name="contactId"))
	private Set<Contact> contacts;
	
	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public Member getMember()
	{
		return member;
	}

	public void setMember(Member member)
	{
		this.member = member;
	}

	@Override
	@JsonIgnore
	public String getMemberAttributeName()
	{
		return "member";
	}

	@Override
	@JsonIgnore
	public String getSyncId()
	{
		return "id";
	}

	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}

	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}

	public Set<Contact> getContacts()
	{
		return contacts;
	}

	public void setContacts(Set<Contact> contacts)
	{
		this.contacts = contacts;
	}

	@Override
	@JsonIgnore
	public void validate() throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (!isPrimaryKeySet())
			codes.add(ValidationCodeEnum.PK_NOT_SET);
		
		if (member==null||!member.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		
		if (!codes.isEmpty())
			throw new ValidationException(codes);
	}

	@Override
	@JsonIgnore
	public String getEmailForAuthorization()
	{
		return member.getEmail();
	}

	@Override
	@JsonIgnore
	public void markForDeletion()
	{
		
	}
	
	
}
