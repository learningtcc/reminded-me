package com.homefellas.batch;

public enum NotificationTypeEnum {

	//0 none
	//1 email
	//2 SMS
	//3 1+2
	//4 PUSH
	//5 1+4
	//6 2+5
	//7 all
	NONE, EMAIL, SMS, EMAIL_SMS, PUSH, EMAIL_PUSH, SMS_PUSH, ALL;
	
	public static boolean isEmail(int orginal)
	{
		if (orginal == EMAIL.ordinal() || orginal == EMAIL_SMS.ordinal() || orginal == EMAIL_PUSH.ordinal() || orginal == ALL.ordinal())
			return true;
		else
			return false;
	}
	
	public static boolean isPush(int orginal)
	{
		if (orginal == PUSH.ordinal() || orginal == EMAIL_PUSH.ordinal() || orginal == SMS_PUSH.ordinal() || orginal == ALL.ordinal())
			return true;
		else
			return false;
	}
	
	public static boolean isSMS(int orginal)
	{
		if (orginal == SMS.ordinal() || orginal == EMAIL_SMS.ordinal() || orginal == SMS_PUSH.ordinal() || orginal == ALL.ordinal())
			return true;
		else
			return false;
	}
}
