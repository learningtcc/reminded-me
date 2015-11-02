package com.homefellas.rm.task;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name="t_timelesstaskstats")
@Proxy(lazy=false)
@XmlRootElement
public class TimelessTaskStat extends AbstractGUIDModel implements ISynchronizeableInitialized, IGenericSynchroinzedLifeCycle
{
	private String timeLessTaskSlot;
	private int lowPC;
	private int mediumPC;
	private int highPC;
	private int criticalPC;
	private int tasksCount;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="memberId")
	private Member member;

	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
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

	public String getTimeLessTaskSlot()
	{
		return timeLessTaskSlot;
	}

	public void setTimeLessTaskSlot(String timeLessTaskSlot)
	{
		this.timeLessTaskSlot = timeLessTaskSlot;
	}

	public int getLowPC()
	{
		return lowPC;
	}

	public void setLowPC(int lowPC)
	{
		this.lowPC = lowPC;
	}

	public int getMediumPC()
	{
		return mediumPC;
	}

	public void setMediumPC(int mediumPC)
	{
		this.mediumPC = mediumPC;
	}

	public int getHighPC()
	{
		return highPC;
	}

	public void setHighPC(int highPC)
	{
		this.highPC = highPC;
	}

	public int getCriticalPC()
	{
		return criticalPC;
	}

	public void setCriticalPC(int criticalPC)
	{
		this.criticalPC = criticalPC;
	}

	public int getTasksCount()
	{
		return tasksCount;
	}

	public void setTasksCount(int tasksCount)
	{
		this.tasksCount = tasksCount;
	}

	public Member getMember()
	{
		return member;
	}

	public void setMember(Member member)
	{
		this.member = member;
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
