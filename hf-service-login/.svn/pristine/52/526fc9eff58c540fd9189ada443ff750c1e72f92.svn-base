package com.homefellas.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractGUIDModel;

@Entity
@Table(name="u_attributes")
@Proxy(lazy=false)
@XmlRootElement
public class UserAttribute extends AbstractGUIDModel
{

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String value;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="profileId")
	@JsonBackReference("profile-userattributes")
	private Profile profile;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}
	
	
}
