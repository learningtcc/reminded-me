package com.homefellas.email.core;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.homefellas.model.core.Email;
import com.homefellas.service.core.AbstractService;

public class EmailServiceJavaMail extends AbstractService implements IEmailServiceJavaMail {
	private JavaMailSenderImpl mailSender;
	private String defaultUserName;
	private String defaultPassword;
	private int defaultAttempts = 1;	
	
	@Autowired
	private IEmailDao emailDao;

	@Override
	public void onInit() {
		super.onInit();
		
		if(mailSender == null)
			throw new IllegalArgumentException("mailSender should not be null");		
	}
	
	/**
	 * Returns a MimeMessagePreparator built from an Email object
	 * 
	 * @param email
	 * @return MimeMessagePreparator
	 */
	private static final MimeMessagePreparator getMimeMessagePreparator(final Email email) {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				
				if(email.getBcc() != null)
					message.setBcc(email.getBcc()); 
				
				if(email.getCc() != null)
					message.setCc(email.getCc());
				
				if(email.getMultipleRecipients() != null)
					message.setCc(email.getMultipleRecipients());
				
				if(email.getFrom() != null)
					message.setFrom(new InternetAddress(email.getFrom(), email.getDisplayName())); 
				
				if(email.getReplyTo() != null)
					message.setReplyTo(email.getReplyTo());
				
				if(email.getSubject() != null)
					message.setSubject(email.getSubject()); 
				
				if(email.getText() != null)
					message.setText(email.getText(), email.isHtml());
				
				if(email.getTo() != null)
					message.setTo(email.getTo()); 
	      	}
		};
	}

	/**
	 * Saves an email for later delivery
	 * 
	 * @param email
	 */
	public void saveEmail(final Email email) {
		emailDao.createEmail(email);
	}
	
	/**
	 * Sends a email
	 * 
	 * @param email
	 * @return true if the mail was successfully sent; false otherwise
	 */
	public boolean sendEmail(final Email email) {
		return sendEmail(email, defaultAttempts, false);
	}
	 
	/**
	 * Sends a email
	 * 
	 * @param email
	 * @param persistOnFail
	 * @return true if the mail was successfully sent; false otherwise
	 */
	public boolean sendEmail(final Email email, final boolean persistOnFail) {
		return sendEmail(email, defaultAttempts, persistOnFail);
	}
	
	/**
	 * Sends a email
	 * 
	 * @param email
	 * @param attempts
	 * @return true if the mail was successfully sent; false otherwise
	 */
	public boolean sendEmail(final Email email, final int attempts) {
		return sendEmail(email, attempts, false);
	}

	/**
	 * Sends a email with the specified amount of attempts in the event of a MailException
	 * 
	 * @param email
	 * @param attempts
	 * @param persistOnFail
	 * @return true if the mail was successfully sent; false otherwise
	 */
	public boolean sendEmail(final Email email, int attempts, final boolean persistOnFail) {
		MimeMessagePreparator mimeMessagePreparator = getMimeMessagePreparator(email);
		Exception exception = null;
		
		for(int i = 0; i<attempts; i++)
		{
			try
			{
				sendEmail(mimeMessagePreparator, email.getFromPassword(), email.getFrom());
				persistEmail(email, null, persistOnFail);
				return true;
			}
			catch(MailException mailException)
			{
				exception = mailException;
				email.addAttempToSend();
			}			
		}
		email.setSuccess(false);
		persistEmail(email, exception, persistOnFail);
		return false;
	}	
	
	/**
	 * Hook into an unsuccessful attempt
	 * 
	 * @param email
	 * @param mailException
	 * @param persistOnFail
	 */
	protected void persistEmail(final Email email, final Exception exception, final boolean persistOnFail) {
//		logger.info("["+this.getClass().getSimpleName()+"]" +
//				" Exception trying to send email with subject=["+email.getSubject()+"] to=["+email.getTo()+"]" +
//				" - " + exception.getMessage());	
		
//		if(persistOnFail)
//		{
			// save the reason it failed
			email.setEmailTimeCreated(new Date(System.currentTimeMillis()));
			email.setFailureMessage(exception != null ? exception.getMessage() : "");
			// save the unsent email
			emailDao.createEmail(email);
//		}
	}
	
	/**
	 * Sends a email prepared through a MimeMessagePreparator
	 * fromPassword - to validae the google smtp server using the password
	 * userName - to validate the username for the smtp server, if both the above doesnt exist then i will validate with the default one's
	 * @param mimeMessagePreparator
	 */
	private void sendEmail(final MimeMessagePreparator mimeMessagePreparator, String fromPassword, String userName) throws MailException {
		if((userName != null && !userName.equals("")) && (fromPassword != null && !fromPassword.equals(""))) {
			this.mailSender.setUsername(userName);
			this.mailSender.setPassword(fromPassword);
		}
		else {
			this.mailSender.setUsername(defaultUserName);
			this.mailSender.setUsername(defaultPassword);
		}
		this.mailSender.send(mimeMessagePreparator);
	}

	// getters and setters
	public int getDefaultAttempts() {
		return defaultAttempts;
	}
	public void setDefaultAttempts(int defaultAttempts) {
		this.defaultAttempts = defaultAttempts;
	}
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public String getDefaultUserName() {
		return defaultUserName;
	}

	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}		
	
}