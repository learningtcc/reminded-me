package com.homefellas.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.security.core.GrantedAuthority;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="u_role")
//@XmlRootElement
public class Role extends AbstractSequenceModel implements GrantedAuthority{

	@Basic(fetch=FetchType.EAGER)
	@Column(unique=true,nullable=false)
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String role) {
		this.roleName = role;
	}
	
	public String getAuthority() {
		return roleName;
	}

}
