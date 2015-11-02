package com.homefellas.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="u_registerTickets")
public class RegisterTicket extends AbstractSequenceModel {

	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String ticket;
	
	public RegisterTicket()
	{
		
	}
	
	public RegisterTicket(String ticket, String email)
	{
		this.ticket = ticket;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	
}
