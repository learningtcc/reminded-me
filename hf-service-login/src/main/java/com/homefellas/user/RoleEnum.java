package com.homefellas.user;

public enum RoleEnum {

	HF_USER_ROLE("ROLE_HF_USER", 1),
	HF_ADMIN_ROLE("ROLE_ADMIN", 2),
	BLOG_ADMIN_ROLE("ROLE_BLOG_ADMIN", 3),
	BLOG_OWNER_ROLE("ROLE_BLOG_OWNER", 4),
	BLOG_PUBLISHER_ROLE("ROLE_BLOG_PUBLISHER", 5),
	BLOG_CONTRIBUTOR_ROLE("ROLE_BLOG_CONTRIBUTOR", 6),
	BLOG_READER_ROLE("ROLE_BLOG_READER", 7),
	GUEST("GUEST",8);
	
	private String role;
	private long id;
	
	private RoleEnum(String role, long id)
	{
		this.role = role;
		this.id = id;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public long getId()
	{
		return id;
	}
	
	public Role getRoleInstance()
	{
		Role roleInstance = new Role();
		roleInstance.setId(id);
		roleInstance.setRoleName(role);
		return roleInstance;
	}
}
