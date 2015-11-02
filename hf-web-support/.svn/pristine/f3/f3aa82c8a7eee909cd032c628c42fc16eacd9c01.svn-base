package com.hiweb.facebook.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FacebookFriendScore implements Serializable {

	private static final long serialVersionUID = -3402351133565466276L;
	
	private String uid;
	private String name;
	
	private String picSquare;
	private String picture;
	
	private int score;
	private List<Long> mutualFriendsWithPlayer;
	 
	public FacebookFriendScore(String uid, String name, Integer score) {
		this.uid = uid;
		this.name = name;
		this.score = score;
	}
	
	// UID is unique and ensure equality
	@Override
	public boolean equals(Object o) {
		if (o == this) { return true; }
		if (!(o instanceof FacebookFriendScore)) { return false; }
		
		FacebookFriendScore otherFriend = (FacebookFriendScore) o;
		
		return new EqualsBuilder()
			.append(this.uid, otherFriend.uid)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
	    return new HashCodeBuilder(127, 137)
	       .append(this.uid)
	       .toHashCode();
	}
	
	@Override
	public String toString() {
		return "UID: " + this.uid + " | SCORE: " + this.score;
	}

	public String getUid() {
		return this.uid;
	}

	public List<Long> getMutualFriendsWithPlayer() {
		return this.mutualFriendsWithPlayer;
	}

	public void setMutualFriendsWithPlayer(List<Long> mutualFriendsWithPlayer) {
		this.mutualFriendsWithPlayer = mutualFriendsWithPlayer;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return this.name;
	}

	public String getPicSquare() {
		return this.picSquare;
	}

	public void setPicSquare(String picSquare) {
		this.picSquare = picSquare;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
