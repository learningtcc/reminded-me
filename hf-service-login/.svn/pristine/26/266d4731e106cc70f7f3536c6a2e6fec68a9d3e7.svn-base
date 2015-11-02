package com.homefellas.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.homefellas.batch.INotifiable;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.ws.core.JodaDateTimeJsonDeSerializer;
import com.homefellas.ws.core.JodaDateTimeJsonSerializer;

/**
 * Member is a user that has registerd and has a valid email address and password
 * @author prc9041
 *
 */
//@XmlRootElement
@Entity
@Table(name="u_profiles")
public class Profile extends AbstractGUIDModel implements Comparable<Profile>, Authentication {

	@Column(nullable=false)
	private String name;
	
	@OneToOne(fetch=FetchType.EAGER,optional=false,cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="memberId")
	private Member member;
	
	@Column
	private boolean confirmed=false;
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="profile",orphanRemoval=true)
	@JsonManagedReference("profile-userattributes")
	private Set<UserAttribute> userAttributes=new HashSet<UserAttribute>();
	
	@Transient
	private String baseURL;
	
	private String facebookId;

	@Columns(columns={@Column(name="joinDate",insertable=true,updatable=true),@Column(name="joinDateZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	private DateTime joinDate;
	
	private String joinIpAddress;
	
	private boolean dailyDYKNotification=true;
	
	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	private int facebookLogins=0;
	
	private boolean searchable=true;
	
	private long nextUpdateDateTime;
	
	private int returnCount;
	
	public Profile()
	{
		
	}
	
	public Profile(String id)
	{
		this.id = id;
	}
	
	
	
	public long getNextUpdateDateTime() {
		return nextUpdateDateTime;
	}

	public void setNextUpdateDateTime(long nextUpdateDateTime) {
		this.nextUpdateDateTime = nextUpdateDateTime;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}
	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}
	public void clearAttributes()
	{
		this.facebookId=null;
		this.joinIpAddress=null;
		this.joinDate=null;
		this.userAttributes.clear();
		this.member.clearAttributes();
		this.clientUpdateTimeStamp=null;
		this.createdDate=0;
		this.createdDateZone=null;
		this.modifiedDate=0;
		this.modifiedDateZone=null;
	}
	
	@Transient
	private boolean authenicated;
	
	public Member getMember() {
		return member;
	}
	public void setMember(Member authenication) {
		this.member = authenication;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isConfirmed()
	{
		return confirmed;
	}
	public void setConfirmed(boolean confirmed)
	{
		this.confirmed = confirmed;
	}
	public String getBaseURL()
	{
		return baseURL;
	}
	public void setBaseURL(String baseURL)
	{
		this.baseURL = baseURL;
	}
	
	@JsonIgnore
	public String getNameAndEmail()
	{
		StringBuffer buffer = new StringBuffer();
		if (name!=null&&!"".equals(name))
		{
			buffer.append(name);
			buffer.append(" <");
			buffer.append(member.getEmail());
			buffer.append(">");
		}
		else
		{
			buffer.append(member.getEmail());
		}
		
		return buffer.toString();
		
		
	}
	@Override
	public int compareTo(Profile p)
	{
		return getNameAndEmail().compareToIgnoreCase(p.getNameAndEmail());
	}
	public Set<UserAttribute> getUserAttributes()
	{
		return userAttributes;
	}
	public void setUserAttributes(Set<UserAttribute> userAttributes)
	{
		this.userAttributes = userAttributes;
	}
	
	@JsonIgnore
	public void addUserAttribute(UserAttribute userAttribute)
	{
		this.userAttributes.add(userAttribute);
	}
	
	@JsonIgnore
	public void clearUserAttributes()
	{
		this.userAttributes.clear();
	}
	public String getFacebookId()
	{
		return facebookId;
	}
	public void setFacebookId(String facebookId)
	{
		this.facebookId = facebookId;
	}
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return member.getAuthorities();
	}
	@Override
	@JsonIgnore
	public Object getCredentials()
	{
		return member.getPassword();
	}
	@Override
	@JsonIgnore
	public Object getDetails()
	{
		return member;
	}
	@Override
	@JsonIgnore
	public Object getPrincipal()
	{
		return member.getEmail();
	}
	@Override
	@JsonIgnore
	public boolean isAuthenticated()
	{
		return this.authenicated;
	}
	
	@Override
	@JsonIgnore
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException
	{
		this.authenicated = isAuthenticated;
		
	}
	
	@JsonIgnore
	public String getEmail()
	{
		if (member==null)
			return null;
		else
			return member.getEmail();
		
	}
	public DateTime getJoinDate()
	{
		return joinDate;
	}
	public void setJoinDate(DateTime joinDate)
	{
		this.joinDate = joinDate;
	}
	
	public String getJoinIpAddress()
	{
		return joinIpAddress;
	}
	public void setJoinIpAddress(String joinIpAddress)
	{
		this.joinIpAddress = joinIpAddress;
	}
	public boolean isDailyDYKNotification()
	{
		return dailyDYKNotification;
	}
	public void setDailyDYKNotification(boolean dailyDYKNotification)
	{
		this.dailyDYKNotification = dailyDYKNotification;
	}
	public boolean isSearchable()
	{
		return searchable;
	}
	public void setSearchable(boolean searchable)
	{
		this.searchable = searchable;
	}
	public int getFacebookLogins()
	{
		return facebookLogins;
	}
	public void setFacebookLogins(int facebookLogins)
	{
		this.facebookLogins = facebookLogins;
	}
	
	public void increamentFacebookLogins()
	{
		this.facebookLogins++;
	}
	
}
