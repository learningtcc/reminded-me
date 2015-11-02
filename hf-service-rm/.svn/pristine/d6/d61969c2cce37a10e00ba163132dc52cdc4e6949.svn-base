package com.homefellas.rm.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.user.Profile;

@Entity
@Table(name="u_ppsattributes")
@Proxy(lazy=false)
@XmlRootElement
public class PersonalPointScoreAttribute extends AbstractGUIDModel
{

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String value;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="ppsId")
	@JsonBackReference("pps-attributes")
	private PersonalPointScore personalPointScore;

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

	public PersonalPointScore getPersonalPointScore()
	{
		return personalPointScore;
	}

	public void setPersonalPointScore(PersonalPointScore personalPointScore)
	{
		this.personalPointScore = personalPointScore;
	}
	
	
}
