package com.homefellas.rm.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.service.core.AbstractService;
import com.homefellas.user.IUserDao;
import com.homefellas.user.Member;
import com.homefellas.user.Profile;
import com.homefellas.user.UserService;

import edu.emory.mathcs.backport.java.util.Collections;

public class ContactService extends AbstractService implements IContactService
{
	
	public static final int RETURNED_CONTACTS = 10;
	@Autowired
	private IContactDao contactDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private UserService userService;
	
	private String allUsersIndexFile;
	
	@Autowired
	private Ehcache userCache;
	
	
	
	@CollectTimeMetrics
	@Transactional
	public void importContacts(List<Contact> contacts, String loggedInEmailAddress)
	{
		Profile owner = userService.getProfileByEmail(loggedInEmailAddress);
		if (owner == null)
			return;
		
		for (Contact contact : contacts)
		{
			
			String email = contact.getContact().getEmail();
			if (email == null || "".equals(email))
				continue;
			Profile  profile = userService.getProfileByEmail(email);
			if (profile!=null)
			{
				Contact contactFromDB = contactDao.getContactByContactEmailAndOwnerEmail(owner.getEmail(), profile.getEmail());
				//need to check to see if the owner has the contact listed already, if not create a reference
				if (contactFromDB != null)
					continue;
				else
				{
					Contact newContact = new Contact();
					newContact.generateGUIDKey();
					newContact.setContact(profile);
					newContact.setContactCounter(1);
					newContact.setContactOwner(owner.getMember());
					newContact.setSource(contact.getSource());
					contactDao.createContact(newContact);
				}
			}
			else
			{
				String name = contact.getContact().getName();
				try
				{
					profile = userService.reigsterGuest(email, name);
					
					Contact newContact = new Contact();
					newContact.generateGUIDKey();
					newContact.setContact(profile);
					newContact.setContactCounter(1);
					newContact.setContactOwner(owner.getMember());
					newContact.setSource(contact.getSource());
					contactDao.createContact(newContact);
				}
				catch (ValidationException e)
				{
					e.printStackTrace();
					continue;
				}
				catch (DatabaseNotInitializedException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
		
		//rebuild the indexes
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		try
		{
			buildAllUserIndex(analyzer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//clear the cache for the email address
		userCache.remove(loggedInEmailAddress);
		
	}
	
	public String getAllUsersIndexFile()
	{
		return allUsersIndexFile;
	}

	public void setAllUsersIndexFile(String allUsersIndexFile)
	{
		this.allUsersIndexFile = allUsersIndexFile;
	}

	@Transactional
	public List<Contact> getContactsForUser(String email)
	{
		return contactDao.getContactsForUser(email, 0);
	}
	
	@Transactional
	public List<Profile> getProfiles()
	{
		return userDao.getAllProfiles();
	}
	
	@Transactional
	public List<GroupContact> getGroupContactsForUser(String email, int maxResults)
	{
		return contactDao.getGroupContactsForUser(email, maxResults);
	}
	
	@Transactional
	public Contact createContact(Contact contact)
	{
		return contactDao.createContact(contact);
	}
	
	
	@Transactional
	public List<GroupContact> getGroupContactsForUserTX(String email, int maxResults)
	{
		return getGroupContactsForUser(email, maxResults);
	}
	
	
	@Transactional
	public List<Contact> getPopularContactsForUserTX(String email, int maxResults)
	{
		return getPopularContactsForUser(email, maxResults);
	}
	
	@Transactional
	public List<GroupContact> getGroupContactsForUser(String email)
	{
		return contactDao.getGroupContactsForUser(email, 10);
	}
	
	@Transactional
	public List<Contact> getPopularContactsForUser(String email)
	{
		return getPopularContactsForUser(email, 10);
	}
	
	@Transactional
	public List<Contact> getPopularContactsForUser(String email, int maxResults)
	{
		return contactDao.getContactsForUser(email, maxResults);
	}

	private Directory buildAllUserIndex(StandardAnalyzer analyzer) throws IOException
	{
		Element element;
//		if ((element = userCache.get("profile")) != null)
//		{
//			return (Directory) element.getObjectValue();
//		}
		
		synchronized ("mutex")
		{
			//index contacts
//			Directory index = new RAMDirectory();
			Directory index = new NIOFSDirectory(new File(allUsersIndexFile));
	
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
	
			IndexWriter w = new IndexWriter(index, config);
							
			List<Profile> profiles = userDao.getAllProfiles();
			for (Profile profile : profiles)
			{
				indexProfile(w, profile);
			}
				
			w.commit();	
			w.close();
			
//			userCache.put(new Element("profile", index));
			
			return index;
		}
	}
	
	private Directory buildContactsForUserIndex(StandardAnalyzer analyzer, String email) throws IOException
	{
		Element element;
		if ((element = userCache.get(email)) != null)
		{
			return (Directory) element.getObjectValue();
		}
		
//		synchronized ("mutex")
//		{
			//index contacts
			Directory index = new RAMDirectory();
	
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
	
				IndexWriter w = new IndexWriter(index, config);
			
			List<Contact> contacts = getContactsForUser(email);
			for (Contact contact : contacts)
			{
				indexContact(w, contact);
			}
			
			w.close();
			
			userCache.put(new Element(email, index));
			
			return index;
//		}
	}
	
	@Transactional
	public List<Contact> searchForContactsTX(String email, String searchTerm)
	{
//		List<Contact> foundContactsList;
		Map<String, Contact> contactMap = new HashMap<String, Contact>(11);
		
		searchTerm = searchTerm+"* OR"+searchTerm+"~";
		try
		{
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			Directory index = buildContactsForUserIndex(analyzer, email);
			
			
			//a. check contact by email
			Query q = new QueryParser(Version.LUCENE_43, "email", analyzer).parse(searchTerm);
			contactMap = search(Contact.class, q, index, 1, RETURNED_CONTACTS);
		    
			
		    if (contactMap.size()<RETURNED_CONTACTS)
		    {
		    	//b.  check contacts by name
		    	q = new QueryParser(Version.LUCENE_43, "name", analyzer).parse(searchTerm);
		    	Collection<Contact> searchByNameContacts = search(Contact.class, q, index, 2, RETURNED_CONTACTS-contactMap.size()).values();
		    	for (Contact nameContact:searchByNameContacts)
		    	{
		    		//check the map to see if we have found the contact already 
		    		if (contactMap.get(nameContact.getEmail())== null)
		    		{
		    			//put the contact in the map
		    			contactMap.put(nameContact.getEmail(), nameContact);
		    		}
		    		
		    	}
			
		    	//check to see if we satisfied the number of contacts
		    	if (contactMap.size()<RETURNED_CONTACTS)
				{
//					List<Contact> allUsers = searchAllUsers(searchTerm, RETURNED_CONTACTS-contactMap.size());
//					for (Contact allUser:allUsers)
//			    	{
//						//check the map to see if we have found the contact already 
//			    		if (contactMap.get(allUser.getEmail())== null)
//			    		{
//			    			//put the contact in the map
//			    			contactMap.put(allUser.getEmail(), allUser);
//			    		}			    		
//			    	}	
					
					if (contactMap.size()<RETURNED_CONTACTS)
					{
						//c.  add latest shares
						List<Contact> lastSharedMembers = getPopularContactsForUser(email, RETURNED_CONTACTS-contactMap.size());
						for (Contact latestShare:lastSharedMembers)
				    	{
							//check the map to see if we have found the contact already 
				    		if (contactMap.get(latestShare.getEmail())== null)
				    		{
				    			//put the contact in the map
				    			latestShare.setDisplayOrder(5);
				    			contactMap.put(latestShare.getEmail(), latestShare);
				    		}
				    	}	
					}
				}
				
		    }
		    
		    Collection<Contact> foundContactsCollection = contactMap.values();
		    List<Contact> foundContactsList = new ArrayList<Contact>(foundContactsCollection);
		    Collections.sort(foundContactsList);
		    return foundContactsList;
		} 
		catch (IOException ioException)
		{
			System.out.println(ioException.getMessage());
			return getPopularContactsForUser(email);
		}
		catch (ParseException parseException)
		{
			System.out.println(parseException.getMessage());
			return getPopularContactsForUser(email);
		}

	}
	
	private <T> Map<String, Contact> search(Class<T> clazz, Query q, Directory index, int priority, int hitsPerPage) throws IOException
	{
		IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
//	    List<Contact> foundContacts = new ArrayList<Contact>(hits.length);
	    Map<String, Contact> foundContacts = new HashMap<String, Contact>(hits.length);
	    
	    for(int i=0;i<hits.length;++i) {
	        int docId = hits[i].doc;
	        Document d = searcher.doc(docId);
	        String profileid = d.get("profileid");
	        String contactid = d.get("contactid");
	        String type = d.get("type");
	        String name = d.get("name");
	        String email = d.get("email");
	        boolean searchable = Boolean.valueOf(d.get("searchable"));
	        
	        //create a dummy object so we don't need to go back to db
	        Profile profile = new Profile();
        	profile.setId(profileid);
        	profile.setName(name);
        	profile.setSearchable(searchable);
        	Member member = new Member();
        	member.setId(profileid);
        	member.setEmail(email);
        	profile.setMember(member);
        	
        	Contact contact = new Contact();
        	contact.setContact(profile);
        	contact.setDisplayOrder(priority);
        	contact.setId(contactid);
        
        	//check to see if its a profile and mark it as one
	        if (type.equals(Profile.class.getName()))
	        {
//	        	Profile profile = (Profile)dao.loadByPrimaryKey(Profile.class, id);
	        	contact.setaContact(false);
	        }
	        
	        //add it to the list
	        foundContacts.put(email, contact);
	        
	    }
	    
	    reader.close();
	    
	    return foundContacts;
	}
	
	private void indexContact(IndexWriter w, Contact contact) throws IOException 
	{
	    Document doc = new Document();
	    doc.add(new TextField("email", contact.getContact().getEmail(), Field.Store.YES));
	    doc.add(new TextField("name", contact.getContact().getName(), Field.Store.YES));
	    doc.add(new StringField("contactid", contact.getId(), Field.Store.YES));
	    doc.add(new StringField("profileid", contact.getContact().getId(), Field.Store.YES));
	    doc.add(new StringField("searchable", String.valueOf(contact.getContact().isSearchable()), Field.Store.YES));
	    doc.add(new StringField("type", contact.getClass().getName(), Field.Store.YES));
	    w.addDocument(doc);
	 }
	
	private void indexProfile(IndexWriter w, Profile profile) throws IOException 
	{
	    Document doc = new Document();
	    doc.add(new TextField("email", profile.getEmail(), Field.Store.YES));
	    doc.add(new TextField("name", profile.getName(), Field.Store.YES));
	    doc.add(new StringField("profileid", profile.getId(), Field.Store.YES));
	    doc.add(new StringField("type", profile.getClass().getName(), Field.Store.YES));
	    doc.add(new StringField("searchable", String.valueOf(profile.isSearchable()), Field.Store.YES));
	    w.addDocument(doc);
	 }
		
	@Transactional
	public Contact getContactByContactIdTX(String id)
	{
		return contactDao.getContactByContactId(id);
	} 
	
	@Transactional
	public List<Contact> searchAllUsers(String searchTerm, int maxResults)
	{
//		searchTerm += " NOT \"test\"";
		
		try
		{
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			File file = new File(allUsersIndexFile);
			Directory index;
			if (file.exists())
				index = NIOFSDirectory.open(file);
			else
				index = buildAllUserIndex(analyzer);
				
			//a. check contact by email
			Query q = new QueryParser(Version.LUCENE_43, "email", analyzer).parse(searchTerm);
//			List<Contact> foundContacts = search(Profile.class, q, index, 3, maxResults);
			Map<String, Contact> contactMap = search(Profile.class, q, index, 3, maxResults);
			
			List<Contact> filteredContacts = new ArrayList<Contact>(contactMap.size());
			
			
		    if (contactMap.size()<maxResults)
		    {
		    	//b.  check contacts by name
		    	q = new QueryParser(Version.LUCENE_43, "name", analyzer).parse(searchTerm);
		    	Collection<Contact> searchByNameContacts = search(Profile.class, q, index, 4, maxResults-contactMap.size()).values();
		    	for (Contact nameContact:searchByNameContacts)
		    	{
		    		//check the map to see if we have found the contact already 
		    		if (contactMap.get(nameContact.getEmail())== null)
		    		{
		    			//put the contact in the map
		    			contactMap.put(nameContact.getEmail(), nameContact);
		    		}
//			    		if (!foundContacts.contains(nameContact))
//			    		{
//			    			foundContacts.add(nameContact);
//			    		}
		    	}
			
		    }
		    
		    List<Contact> foundContacts = new ArrayList();
//		    (List<Contact>)contactMap.values();
		    for (Contact contact : contactMap.values())
			{
				if (contact.getContact().isSearchable() && !contact.getContact().getNameAndEmail().contains("test") && !contact.getContact().getNameAndEmail().contains("+"))
					foundContacts.add(contact);
			}
		    
//		    foundContacts.removeAll(filteredContacts);
		    
		    return foundContacts;
		} 
		catch (IOException ioException)
		{
			System.out.println(ioException.getMessage());
			return Collections.emptyList();
		}
		catch (ParseException parseException)
		{
			System.out.println(parseException.getMessage());
			return Collections.emptyList();
		}
	}
	
	public Contact updateContact(Contact contact)
	{
		return contactDao.updateContact(contact);

	}
}