package com.homefellas.comment;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.homefellas.model.core.AbstractSequenceModel;

@MappedSuperclass
public class Comment extends AbstractSequenceModel {

	@Column(nullable=false)
	protected String userComment;
	
//	@ManyToOne(fetch=FetchType.EAGER,optional=false)
//	@JoinColumn(name="profileId")
//	protected Profile poster;
	
	@Columns(columns={@Column(name="commentTime",insertable=true,updatable=true),@Column(name="commentTimezone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	private DateTime commentTime;

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String comment) {
		this.userComment = comment;
	}

//	public Profile getPoster() {
//		return poster;
//	}
//
//	public void setPoster(Profile poster) {
//		this.poster = poster;
//	}

	public DateTime getCommentTime()
	{
		return commentTime;
	}

	public void setCommentTime(DateTime commentTime)
	{
		this.commentTime = commentTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((userComment == null) ? 0 : userComment.hashCode());
		result = prime * result
				+ ((commentTime == null) ? 0 : commentTime.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (userComment == null)
		{
			if (other.userComment != null)
				return false;
		}
		else if (!userComment.equals(other.userComment))
			return false;
		if (commentTime == null)
		{
			if (other.commentTime != null)
				return false;
		}
		else if (!commentTime.equals(other.commentTime))
			return false;
		
		return true;
	}
	
	

	

}
