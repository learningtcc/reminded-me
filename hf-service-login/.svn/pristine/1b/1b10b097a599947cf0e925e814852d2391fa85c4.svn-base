package com.homefellas.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Proxy;

import com.google.code.yanf4j.util.ConcurrentHashSet;
import com.homefellas.model.core.AbstractSequenceModel;

@Entity
@Table(name="u_extendedprofile")
@Proxy(lazy=false)
@XmlRootElement
public class ExtendedProfile extends AbstractSequenceModel
{
	
	private int personType; 
	private String affiliation;
	private int howDidYouHearAboutUs;
	private String howDidYouHearAboutUsResponse;
	private String userComments;
	private String promoCode;
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="extendedProfile")
	@JsonManagedReference("friendemail")
	private Set<FriendEmail> friendEmails;
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL},mappedBy="extendedProfile")
	@JsonManagedReference("intersts")
	private Set<Interest> interests;
	
	
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	@JoinColumn(name="profileId")
	private Profile profile;
	
	public enum PersonTypeEnum { STUDENT, PROFESSIONAL, OTHER };
	public enum HowDidYouHearAboutUsEnum { FRIEND_TOLD_ME }

	/**
	 * This is the person type.  This should be an ordinal from the PersonTypeEnum.
	 * @return personType
	 * @see PersonTypeEnum
	 */
	public int getPersonType()
	{
		return personType;
	}
	public void setPersonType(int personType)
	{
		this.personType = personType;
	}
	
	/**
	 * This is the affiliation of the user.  This should be something like a university or a company that the user works for.
	 * @return affiliation
	 */
	public String getAffiliation()
	{
		return affiliation;
	}
	public void setAffiliation(String affiliation)
	{
		this.affiliation = affiliation;
	}
	
	/**
	 * This is how the user heard about rm.  This information should be the ordinal of the HowDidYouHearAboutUsEnum enum.
	 * @return howDidYouHearAboutUsEnum
	 * @see HowDidYouHearAboutUsEnum
	 */
	public int getHowDidYouHearAboutUs()
	{
		return howDidYouHearAboutUs;
	}
	public void setHowDidYouHearAboutUs(int howDidYouHearAboutUs)
	{
		this.howDidYouHearAboutUs = howDidYouHearAboutUs;
	}
	
	/**
	 * These are the user comments from step 3.  This is a free form field that hte user can just type into.
	 * @return userComments
	 */
	public String getUserComments()
	{
		return userComments;
	}
	public void setUserComments(String userComments)
	{
		this.userComments = userComments;
	}
	
	/**
	 * This is a list of email addresses that the user wants to recommend to.  On step 1 there is a spot for one email.  On
	 * step 2 you can add more.  From step one this should be added to the list.  
	 * @return
	 */
	public Set<FriendEmail> getFriendEmails()
	{
		return friendEmails;
	}
	public void setFriendEmails(Set<FriendEmail> friendEmails)
	{
		this.friendEmails = friendEmails;
	}
	public void addFriendEmail(String email)
	{
		FriendEmail friendEmail = new FriendEmail();
		friendEmail.setExtendedProfile(this);
		friendEmail.setFriendEmailAddress(email);
		
		if (friendEmails == null)
			friendEmails = new ConcurrentHashSet<FriendEmail>();
		
		friendEmails.add(friendEmail);
	}
	
	public void addAllFriends(Set<FriendEmail> friends)
	{
		if (friendEmails==null)
			friendEmails = new ConcurrentHashSet<FriendEmail>();
		
		friendEmails.addAll(friends);
	}
	
	/**
	 * This is for the user to add interests too.  
	 * @return interests
	 */
	public Set<Interest> getInterests()
	{
		return interests;
	}
	public void setInterests(Set<Interest> interests)
	{
		this.interests = interests;
	}
	
	public void addInterest(String interestString)
	{
		Interest interest = new Interest();
		
		interest.setExtendedProfile(this);
		interest.setInterestName(interestString);
		
		if (interests == null)
			interests = new HashSet<Interest>();
		
		interests.add(interest);
	}
	
	/**
	 * This is the profile that the extendprofile is mapped to
	 * @return profile
	 * @see Profile
	 */
	public Profile getProfile()
	{
		return profile;
	}
	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}
	public String getHowDidYouHearAboutUsResponse()
	{
		return howDidYouHearAboutUsResponse;
	}
	public void setHowDidYouHearAboutUsResponse(String howDidYouHearAboutUsResponse)
	{
		this.howDidYouHearAboutUsResponse = howDidYouHearAboutUsResponse;
	}
	public String getPromoCode()
	{
		return promoCode;
	}
	public void setPromoCode(String promoCode)
	{
		this.promoCode = promoCode;
	};
	
	
	
}
