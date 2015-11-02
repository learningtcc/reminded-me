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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.homefellas.batch.INotifiable;
import com.homefellas.model.core.AbstractGUIDModel;

/**
 * member
 * @author prc9041
 *
 */
@Entity
@Table(name="u_members")
//@XmlRootElement
public class Member extends AbstractGUIDModel implements UserDetails, INotifiable {

	@Transient
	protected String password;
	
	@Column(name="password")
	protected String creditials;
	
	@Column(nullable=false,unique=true)
	protected String email;
	
	@Transient
	protected String ticket;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="u_memberRoles")
//	@JoinTable(name="u_memberRoles",joinColumns=@JoinColumn(name="u_members_id"),inverseJoinColumns=@JoinColumn(name="roles_id"))
	@JsonIgnore
	protected Set<Role> roles;

	@Columns(columns={@Column(name="lastLoginDate",insertable=true,updatable=true),@Column(name="lastLoginDateZone",insertable=true,updatable=true)})
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTimeTZ")
	private DateTime lastLoginDate;
	
	@Transient
	protected String sharedTaskId;
	
	@Column(nullable=true)
	private String lastModifiedDeviceId;
	
	
	public String getLastModifiedDeviceId()
	{
		return lastModifiedDeviceId;
	}
	public void setLastModifiedDeviceId(String lastModifiedDeviceId)
	{
		this.lastModifiedDeviceId = lastModifiedDeviceId;
	}
	
	public Member()
	{
		
	}
	
	public Member(String id)
	{
		
		this.id=id;
	}
	
	public void clearAttributes()
	{
		this.clientUpdateTimeStamp=null;
		this.createdDate=0;
		this.createdDateZone=null;
		this.lastLoginDate=null;
		this.modifiedDate=0;
		this.modifiedDateZone=null;
		this.password=null;
		this.roles.clear();
		this.sharedTaskId=null;
		this.ticket=null;
		
	}
	
	@JsonIgnore
	public Collection<GrantedAuthority> getAuthorities() {
		return new HashSet<GrantedAuthority>(roles);
	}
 
	/**
	 * This is the password for the member that the ui and ws use to send the password to the server
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	
	
	public String getSharedTaskId()
	{
		return sharedTaskId;
	}

	public void setSharedTaskId(String sharedTaskId)
	{
		this.sharedTaskId = sharedTaskId;
	}

	@JsonIgnore
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	/**
	 * This is used for the join process.  This should be guid that is set by the client and passed in.  It is used 
	 * to register the user into the system.  It can not be null during register.
	 * @return ticket
	 */
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

	/**
	 * This is a set of all the roles that the user has.  This is used for authorization.  The default roles will be 
	 * set.  If for some reason you get an invalid role error during the join process, then you will need to create the
	 * default roles in the database.  
	 * @return roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	@JsonIgnore
	public String getClassName()
	{
		return getClass().getName();
	}

	@Override
	@JsonIgnore
	public boolean isCallBack()
	{
		return false;
	}

	@Override
	@JsonIgnore
	public String getNotificationId()
	{
		return id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}


	public void setGuest(boolean g)
	{
		
	}
	
	public boolean isGuest()
	{
		if (roles==null||roles.isEmpty())
			return true;
		
		for (Role role:roles)
		{
			if (role.getRoleName().equals(RoleEnum.GUEST.getRole()))
				return true;
		}
		return false;
	}

	@JsonIgnore
	public void removeGuestRole()
	{
		Role removalrole=null;
		for (Role role : roles)
		{
			if (role.getRoleName().equals(RoleEnum.GUEST.getRole()))
				removalrole = role;
		}
		
		if (removalrole!=null)
		{
			roles.remove(removalrole);
		}
	}

	public DateTime getLastLoginDate()
	{
		return lastLoginDate;
	}

	public void setLastLoginDate(DateTime lastLoginDate)
	{
		this.lastLoginDate = lastLoginDate;
	}
	
	/**
	 * This is the field that is actually persisted to the database with the password
	 * @return
	 */
	@JsonIgnore
	public String getCreditials()
	{
		return creditials;
	}
	
	@JsonIgnore
	public void setCreditials(String creditials)
	{
		this.creditials = creditials;
	}


	
	
	
}
