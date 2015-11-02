package com.homefellas.rm.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.homefellas.exception.IValidationCode;
import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.IGenericSynchroinzedLifeCycle;
import com.homefellas.rm.ISynchronizeableInitialized;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.user.Member;
import com.homefellas.ws.core.JodaDateTimeJsonDeSerializer;
import com.homefellas.ws.core.JodaDateTimeJsonSerializer;
import com.homefellas.ws.core.SQLDateJsonDeSerializer;
import com.homefellas.ws.core.SQLDateJsonSerializer;

@Entity
@Table(name="u_pps", uniqueConstraints=@UniqueConstraint(columnNames={"memberId", "createDate"}))
@Proxy(lazy=false)
@XmlRootElement
public class PersonalPointScore extends AbstractGUIDModel implements ISynchronizeableInitialized, IGenericSynchroinzedLifeCycle
{

	private int yesterdayEarnedPts;
	
	private int yesterdayPotentialPts;
	
	private int todayEarnedPts;
	private int todayPotentialPts;
	private int cummulativeEarnedPts;
	private int cummulativePotentialPts;
	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="memberId")
	@Index(name="ppsMemberIndex")
	private Member member;
	
	@Columns(columns={@Column(name="lastResetDate",insertable=true,updatable=true),@Column(name="lastResetDateZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime lastResetDate;
	
	@Columns(columns={@Column(name="cummulativeLastUpdate",insertable=true,updatable=true),@Column(name="cummulativeLastUpdateZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	@JsonSerialize(using=JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using=JodaDateTimeJsonDeSerializer.class)
	protected DateTime cummulativeLastUpdate;
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="personalPointScore",orphanRemoval=true)
	@JsonManagedReference("pps-attributes")
	private Set<PersonalPointScoreAttribute> ppsAttributes=new HashSet<PersonalPointScoreAttribute>();
	
	@Column(nullable=false, unique=true)
	@JsonSerialize(using=SQLDateJsonSerializer.class)
	@JsonDeserialize(using=SQLDateJsonDeSerializer.class)
	Date createDate = new Date(Calendar.getInstance().getTime().getTime());
	
	public Member getMember()
	{
		return member;
	}
	public void setMember(Member member)
	{
		this.member = member;
	}
	public int getYesterdayEarnedPts()
	{
		return yesterdayEarnedPts;
	}
	public void setYesterdayEarnedPts(int yesterdayEarnedPts)
	{
		this.yesterdayEarnedPts = yesterdayEarnedPts;
	}
	public int getYesterdayPotentialPts()
	{
		return yesterdayPotentialPts;
	}
	public void setYesterdayPotentialPts(int yesterdayPotentialPts)
	{
		this.yesterdayPotentialPts = yesterdayPotentialPts;
	}
	public int getTodayEarnedPts()
	{
		return todayEarnedPts;
	}
	public void setTodayEarnedPts(int todayEarnedPts)
	{
		this.todayEarnedPts = todayEarnedPts;
	}
	public int getTodayPotentialPts()
	{
		return todayPotentialPts;
	}
	public void setTodayPotentialPts(int todayPotentialPts)
	{
		this.todayPotentialPts = todayPotentialPts;
	}
	public int getCummulativeEarnedPts()
	{
		return cummulativeEarnedPts;
	}
	public void setCummulativeEarnedPts(int cummulativeEarnedPts)
	{
		this.cummulativeEarnedPts = cummulativeEarnedPts;
	}
	public int getCummulativePotentialPts()
	{
		return cummulativePotentialPts;
	}
	public void setCummulativePotentialPts(int cummulativePotentialPts)
	{
		this.cummulativePotentialPts = cummulativePotentialPts;
	}
	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}
	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
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
	@Override
	@JsonIgnore
	public void validate() throws ValidationException
	{
		List<IValidationCode> codes = new ArrayList<IValidationCode>();
		
		if (!isPrimaryKeySet())
			codes.add(ValidationCodeEnum.PK_NOT_SET);
		
		if (member==null||!member.isPrimaryKeySet())
			codes.add(ValidationCodeEnum.MEMBER_ID_IS_NULL);
		
		if (createDate == null)
			codes.add(ValidationCodeEnum.CREATE_DATE_IS_NULL);
		
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
	public DateTime getLastResetDate()
	{
		return lastResetDate;
	}
	public void setLastResetDate(DateTime lastResetDate)
	{
		this.lastResetDate = lastResetDate;
	}
	public DateTime getCummulativeLastUpdate()
	{
		return cummulativeLastUpdate;
	}
	public void setCummulativeLastUpdate(DateTime cummulativeLastUpdate)
	{
		this.cummulativeLastUpdate = cummulativeLastUpdate;
	}
	public Set<PersonalPointScoreAttribute> getPpsAttributes()
	{
		return ppsAttributes;
	}
	public void setPpsAttributes(Set<PersonalPointScoreAttribute> ppsAttributes)
	{
		this.ppsAttributes = ppsAttributes;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	
	
}
