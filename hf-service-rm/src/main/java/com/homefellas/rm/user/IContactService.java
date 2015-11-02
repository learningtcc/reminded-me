package com.homefellas.rm.user;

import java.util.List;

public interface IContactService
{

	public List<Contact> searchForContactsTX(String email, String searchTerm);
	public Contact getContactByContactIdTX(String id);
	public List<GroupContact> getGroupContactsForUserTX(String email, int maxResults);
	public List<Contact> getPopularContactsForUserTX(String email, int maxResults);
	public void importContacts(List<Contact> contacts, String loggedInEmailAddress);
}
