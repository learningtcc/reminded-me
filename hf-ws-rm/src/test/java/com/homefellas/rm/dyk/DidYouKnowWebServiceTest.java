package com.homefellas.rm.dyk;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowStatus;
import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowType;
import com.homefellas.ws.core.AbstractTestRMWebService;
import com.sun.jersey.api.client.GenericType;


public class DidYouKnowWebServiceTest extends AbstractTestRMWebService
{

	protected PlatformTransactionManager brochureTransactionManager;
	private IDidYouKnowDao didYouKnowDao;
	
	@Before
	public void setupDefaultDatabaseValues()
	{
		super.setupDefaultDatabaseValues();
		
		brochureTransactionManager = (PlatformTransactionManager)getServer().getSpringBean("brochureTransactionManager");
		didYouKnowDao = (DidYouKnowDao)getServer().getSpringBean("didYouKnowDao");
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(brochureTransactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
			   DidYouKnow message1Active = new DidYouKnow();
				message1Active.setCreatedDateZone(new DateTime());
				message1Active.setDate(new Date(System.currentTimeMillis()));
				message1Active.setDidYouKnowStatusOrdinal(DidYouKnowStatus.ACTIVE.ordinal());
				message1Active.setMessage("message1Active");
				message1Active.setUrl("http://reminded.me");
				message1Active.setDidYouKnowTypeOrdinal(DidYouKnowType.message.ordinal());
				didYouKnowDao.saveDidYouKnow(message1Active);
		   }});
	}
	
	@Test
	public void getMessagesByDate()
	{
		DateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String date = formatter.format(new Date(System.currentTimeMillis()));
		List<DidYouKnow> didYouKnows = callWebService(DidYouKnowWebService.class, "getMessagesByDate", new GenericType<List<DidYouKnow>>(){}, buildPathParms("{date}", date));
		Assert.assertTrue(didYouKnows.size()>0);
	}
	@Test
	public void getValidMessages()
	{
		List<DidYouKnow> didYouKnows = callWebService(DidYouKnowWebService.class, "getMessages", new GenericType<List<DidYouKnow>>(){}, buildPathParms("", ""));
		Assert.assertTrue(didYouKnows.size()>0);
	}
}
