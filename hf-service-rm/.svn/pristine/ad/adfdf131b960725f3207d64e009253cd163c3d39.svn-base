package com.homefellas.rm.note;

import javax.persistence.Column;
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
@Table(name="n_attachment_meta")
@Proxy(lazy=false)
@XmlRootElement
public class AttachmentMetaData extends AbstractSequenceModel
{

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String value;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="attachmentId")
	@JsonBackReference("attachment-back")
	private Attachment attachment;

	/**
	 * This is the name of the meta data attribute that you want to be set
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * This is the value that makes the name.
	 * @return value
	 */
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * This is the link back to the attachment.
	 * @return
	 */
	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment(Attachment attachment)
	{
		this.attachment = attachment;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
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
		return true;
	}

	
	
}
