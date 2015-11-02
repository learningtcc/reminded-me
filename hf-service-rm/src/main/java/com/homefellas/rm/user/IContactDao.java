package com.homefellas.rm.user;

import java.util.List;

import com.homefellas.user.Member;
import com.homefellas.user.Profile;

public interface IContactDao
{
	//create
	public Contact createContact(Contact contact);
	
	//read
	public List<Contact> getContactsForUser(String email, int maxResults);
	public List<GroupContact> getGroupContactsForUser(String email, int maxResults);
	public List<Contact> getContactsByIds(List<String> ids);
	public Contact getContactByContactId(String id);
	public Contact getContactByContactEmailAndOwnerEmail(String ownerOwnEmail, String contactEmail);
	
	//update
	public Contact updateContact(Contact contact);
	
	
	

}
