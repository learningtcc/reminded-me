package com.homefellas.service.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.batch.INotifiable;
import com.homefellas.batch.Notification;
import com.homefellas.batch.NotificationTypeEnum;
import com.homefellas.batch.PushTypeEnum;
import com.homefellas.cache.core.ICacheProvider;
import com.homefellas.core.SpringBean;
import com.homefellas.email.core.EmailTemplateEnum;

public abstract class AbstractService extends SpringBean {

	protected ICacheProvider cacheProvider;
	private String emailTemplateHomeDirectory;
	private VelocityEngine velocityEngine;
	
	public static final String SUBJECT = "subject";
	public static final String TEMPLATE = "template";
	public static final String TO = "to";
	public static final String FROM = "from";
	
	// the transaction manager
//	protected PlatformTransactionManager transactionManager;
	
	public ICacheProvider getCacheProvider() {
		return cacheProvider;
	}
	public void setCacheProvider(ICacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
	

	
	public void setVelocityEngine(VelocityEngine velocityEngine)
	{
		this.velocityEngine = velocityEngine;
	}

	
	

	public VelocityEngine getVelocityEngine()
	{
		return velocityEngine;
	}
	
//extends HibernateDaoSupport{
/*	public ICacheProvider getCacheProvider() {
		return cacheProvider;
	}
	public void setCacheProvider(ICacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
*/
	
	public void setEmailTemplateHomeDirectory(String emailTemplateHomeDirectory)
	{
		this.emailTemplateHomeDirectory = emailTemplateHomeDirectory;
	}
//	public PlatformTransactionManager getTransactionManager() {
//		return transactionManager;
//	}
//	public void setTransactionManager(PlatformTransactionManager transactionManager) {
//		this.transactionManager = transactionManager;
//	}
//		
//	public TransactionTemplate getTransactionTemplate() {
//		  TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
////		  transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
//		  return transactionTemplate;
//		 }
	
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
	
//	public <T> List<T> getBulk(Class<T> clazz, String tokenizedIds)
//	{
//		StringTokenizer stringTokenizer = new StringTokenizer(tokenizedIds, ",");
//		List<T> list = new ArrayList<T>(11);
//		while (stringTokenizer.hasMoreTokens())
//		{
//			String id = stringTokenizer.nextToken();
//			try
//			{
//				T object = dao.loadByPrimaryKey(clazz, id);
//				if (object==null)
//					continue;
//				list.add(object);
//			}
//			catch (Exception exception)
//			{
//				System.out.println(clazz.toString()+" with id "+id+" is possibly corrupt.");
//				exception.printStackTrace();		
//				continue;
//			}
//		}
//		
//		return list;
//	}
	
	public ResourceBundle getEmailResourceBundle(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		File file = new File(emailTemplateHomeDirectory);
		ClassLoader loader=null;
		try
		{
			URL[] urls = {file.toURI().toURL()};  
			loader = new URLClassLoader(urls);
		}
		catch (MalformedURLException exception)
		{
			exception.printStackTrace();
			return null;
		}
		
		
		ResourceBundle bundle = ResourceBundle.getBundle(emailTemplateEnum.getBundleName(), locale, loader);
		
		return bundle;
		
	}
	
	public String getEmailTemplate(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		return emailTemplateHomeDirectory+getEmailResourceBundle(emailTemplateEnum, locale).getString(TEMPLATE);
	}
	
	public String getEmailSubject(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		return getEmailResourceBundle(emailTemplateEnum, locale).getString(SUBJECT);
	}
	
	public String getSubject(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		return getEmailResourceBundle(emailTemplateEnum, locale).getString(SUBJECT);
	}
	
	public String getEmailFromAddress(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		return getEmailResourceBundle(emailTemplateEnum, locale).getString(FROM);
	}
	
	public String getEmailToAddress(EmailTemplateEnum emailTemplateEnum, Locale locale)
	{
		return getEmailResourceBundle(emailTemplateEnum, locale).getString(TO);
	}
	
	protected Notification buildImmediateEmailNotification(String subject, String sendFromEmail, Locale locale, INotifiable notifiable, String body, String to)
	{
		Notification notification = new Notification();
		notification.setBody(body);
		notification.setNotificationTypeOrdinal(NotificationTypeEnum.EMAIL.ordinal());
		notification.setPushTypeOrdinal(PushTypeEnum.NONE.ordinal());
		notification.setSendTo(to);
		notification.setSubject(subject);
		notification.setSendFrom(sendFromEmail);
		notification.setToSendTime(System.currentTimeMillis());
		notification.setINotification(notifiable);
		
		return notification;
	}
	
	protected List<String> tokenize(String taskDelimiter)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(taskDelimiter, ",");	
		List<String> ids = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens())
		{
			String id = stringTokenizer.nextToken();
			ids.add(id);
		}
		
		return ids;
	}
}
