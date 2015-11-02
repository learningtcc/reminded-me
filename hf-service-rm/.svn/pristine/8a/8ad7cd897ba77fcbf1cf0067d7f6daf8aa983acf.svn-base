package com.homefellas.rm.dyk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="wp_did_you_know")
@Proxy(lazy=false)
@XmlRootElement
public class DidYouKnow extends AbstractSequenceModel
{
	@Column(length=500)
	private String message;
	
	@Column(length=200)
	private String url;

	@Column(nullable=false)
	private Date date;

	@Column(name="status",nullable=false)
	private int didYouKnowStatusOrdinal=1;
	
//	@Column(name="type",columnDefinition = "enum('message','archive')")
//	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private int didYouKnowTypeOrdinal;
	
	public enum DidYouKnowStatus { INACTIVE, ACTIVE; }
	public enum DidYouKnowType { archive, message; }
	
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public int getDidYouKnowStatusOrdinal()
	{
		return didYouKnowStatusOrdinal;
	}
	public void setDidYouKnowStatusOrdinal(int didYouKnowStatusOrdinal)
	{
		this.didYouKnowStatusOrdinal = didYouKnowStatusOrdinal;
	}
	public int getDidYouKnowTypeOrdinal()
	{
		return didYouKnowTypeOrdinal;
	}
	public void setDidYouKnowTypeOrdinal(int didYouKnowTypeOrdinal)
	{
		this.didYouKnowTypeOrdinal = didYouKnowTypeOrdinal;
	}
	
	
}
