package com.homefellas.rm.task;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.rm.repeatsetup.RepeatSetup;
import com.homefellas.ws.core.JodaDateTimeJsonDeSerializer;
import com.homefellas.ws.core.JodaDateTimeJsonSerializer;

@Entity
@Table(name="t_skiptimes")
@Proxy(lazy=false)
@XmlRootElement
public class SkipTime extends AbstractGUIDModel
{
	@Columns(columns={@Column(name="skipStart",insertable=true,updatable=true),@Column(name="skipStartTimeZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	private DateTime skipStart;
	
	@Columns(columns={@Column(name="skipEnd",insertable=true,updatable=true),@Column(name="skupEndTimeZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	private DateTime skipEnd;

	@JsonBackReference("repeatsetup")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="repeatSetupId")
	private RepeatSetup repeatSetup;
	
	public DateTime getSkipStart()
	{
		return skipStart;
	}

	public void setSkipStart(DateTime skipStart)
	{
		this.skipStart = skipStart;
	}

	public DateTime getSkipEnd()
	{
		return skipEnd;
	}

	public void setSkipEnd(DateTime skipEnd)
	{
		this.skipEnd = skipEnd;
	}

	public RepeatSetup getRepeatSetup()
	{
		return repeatSetup;
	}

	public void setRepeatSetup(RepeatSetup repeatSetup)
	{
		this.repeatSetup = repeatSetup;
	}
	
	
}
