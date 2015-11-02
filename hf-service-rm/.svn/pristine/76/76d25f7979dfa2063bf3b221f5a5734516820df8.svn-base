package com.homefellas.rm.note;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.user.Member;

@Entity
@Table(name="n_attachments")
@Proxy(lazy=false)
@XmlRootElement
public class Attachment extends AbstractGUIDModel
{

	@Column
	private String urlPath;
	
	@Column(nullable=false)
	private String fileName;
	
	@Column(nullable=false)
	private String mimeType;
	
	@Column
	private String description;
	
	@Column
	private int fileSize;
	
	@Column(nullable=false)
	private int attachmentStateOrginal=AttachmentStateEnum.INITIAL.ordinal();
	
	@Column(nullable=false)
	private int attachmentProviderOrdinal=AttachmentProviderEnum.NOT_APPLICABLE.ordinal();
	
	@Column(nullable=false)
	private String systemPathToAttachment;
	
	@Columns(columns={@Column(name="uploadStartTimeStamp",insertable=true,updatable=true,nullable=false),@Column(name="uploadStartTimeStampzone",insertable=true,updatable=true,nullable=false)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	private DateTime uploadedStartDate;
	
	@Columns(columns={@Column(name="uploadEndTimeStamp",insertable=true,updatable=true),@Column(name="uploadEndTimeStampzone",insertable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	private DateTime uploadedEndDate;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="uploadId")
	private Member uploader;
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="attachment")
	@JsonManagedReference("attachment-back")	
	private Set<AttachmentMetaData> attachmentMetaData;

	/**
	 * This is the path to where the attachment is located on the internet.  
	 * @return urlPath
	 */
	public String getUrlPath()
	{
		return urlPath;
	}

	public void setUrlPath(String urlPath)
	{
		this.urlPath = urlPath;
	}

	/**
	 * This is the file name that the user selects for the attachment.  This is required.
	 * @return fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * This is the mimeType of the attachment.  This should be one of the standard mime types.  This is required.
	 * @return mimeType
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	/**
	 * This is the description of what the user is uploading.
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * This is the file size of the attachment.  This should not be set by the client, but should come from the server.
	 * @return fileSize
	 */
	public int getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(int fileSize)
	{
		this.fileSize = fileSize;
	}

	/**
	 * This is the state of the attachment.  This will be used to do some kind of processsing on the kind.  The state is stored in the AttachmentStateEnum.  This is required.  
	 * @return attachmentStateOrginal
	 * @see AttachmentStateEnum
	 */
	public int getAttachmentStateOrginal()
	{
		return attachmentStateOrginal;
	}

	public void setAttachmentStateOrginal(int attachmentStateOrginal)
	{
		this.attachmentStateOrginal = attachmentStateOrginal;
	}

	/**
	 * This is the ordinal of the provider.  This will be used at a high level to find where the attachment is located.  For certain attachments this may not be required and should
	 * be set to NA.  This is required. 
	 * @return attachmentProviderOrdinal
	 */
	public int getAttachmentProviderOrdinal()
	{
		return attachmentProviderOrdinal;
	}

	public void setAttachmentProviderOrdinal(int attachmentProviderOrdinal)
	{
		this.attachmentProviderOrdinal = attachmentProviderOrdinal;
	}

	/**
	 * This is the physical path on the server to the attachment.  This should not be exposed to the client.  This is required, but should be populated by the service.
	 * @return systemPathToAttachment
	 */
	public String getSystemPathToAttachment()
	{
		return systemPathToAttachment;
	}

	public void setSystemPathToAttachment(String systemPathToAttachment)
	{
		this.systemPathToAttachment = systemPathToAttachment;
	}

	/**
	 * This is the time that the uploaded started.  This is required.
	 * @return uploadedStartDate
	 */
	public DateTime getUploadedStartDate()
	{
		return uploadedStartDate;
	}

	public void setUploadedStartDate(DateTime uploadedDate)
	{
		this.uploadedStartDate= uploadedDate;
	}

	
	/**
	 * This is the time that the uploaded ended.  This is not required in case there is an error during transmit.
	 * @return
	 */
	public DateTime getUploadedEndDate()
	{
		return uploadedEndDate;
	}

	public void setUploadedEndDate(DateTime uploadedEndDate)
	{
		this.uploadedEndDate = uploadedEndDate;
	}


	/**
	 * This is the member that uploads the attachment.  This is required.
	 * @return uploader
	 */
	public Member getUploader()
	{
		return uploader;
	}

	public void setUploader(Member uploader)
	{
		this.uploader = uploader;
	}

	/**
	 * This is a list of name/value pairs that contain meta data about the attachment.
	 * @return attachmentMetaData
	 */
	public Set<AttachmentMetaData> getAttachmentMetaData()
	{
		return attachmentMetaData;
	}

	public void setAttachmentMetaData(Set<AttachmentMetaData> attachmentMetaData)
	{
		this.attachmentMetaData = attachmentMetaData;
	}
	
	public void addAttachmentMetaData(AttachmentMetaData attachmentMetaData)
	{
		if (this.attachmentMetaData == null)
			this.attachmentMetaData = new HashSet<AttachmentMetaData>();
		
		this.attachmentMetaData.add(attachmentMetaData);
		
	}
	
}
