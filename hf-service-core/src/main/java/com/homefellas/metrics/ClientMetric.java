package com.homefellas.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.doc.DocumentationExample;
import org.codehaus.enunciate.json.JsonName;
import org.codehaus.enunciate.json.JsonRootType;
import org.codehaus.enunciate.json.JsonType;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="metric_client")
@XmlRootElement
public class ClientMetric extends AbstractSequenceModel
{

	@Column(name="memberId")
	private String memberId;
	
	@Column(name="category",length=50)
	private String category;
	
	@Column(name="action",length=50)
	private String action;
	
	@Column(name="label",length=255)
	private String label;
	
	@Column(name="value")
	private int value;

	public String getMemberId()
	{
		return memberId;
	}

	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
	
	
}
