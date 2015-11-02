package com.homefellas.rm.share;

public enum ShareApprovedStatus {

	
	NO_ACTION("no action"),
	APPROVED("accepted"),
	DECLINED("declined"),
	OWNER_TASK("not share"),
	SHARED_TASK("shared_task");
	
	private String emailMessage;
	
	private ShareApprovedStatus(String message)
	{
		this.emailMessage = message;		
	}
	
	public String getEmailMessage()
	{
		return emailMessage;
	}
	
	 public static ShareApprovedStatus getByOrdinal(int c)     
	    {     
	        for (ShareApprovedStatus type: values())  
	        {  
	            if (type.ordinal() == c)  
	            {  
	                return type;  
	            }  
	        }  
	        return null;     
	    }     
	
}
