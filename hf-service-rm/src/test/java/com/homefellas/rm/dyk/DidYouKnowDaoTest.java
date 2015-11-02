package com.homefellas.rm.dyk;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowStatus;
import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowType;
import com.homefellas.test.core.AbstractTest;

//@TransactionConfiguration(transactionManager="brochureTransactionManager",defaultRollback=false)
public class DidYouKnowDaoTest extends AbstractBrochureTest
{
	
	@Test
	@Transactional("brochureTransactionManager")
	public void getValidDidYouKnowMessages()
	{
		List<DidYouKnow> didYouKnows = didYouKnowDao.getValidDidYouKnowMessages();
		Assert.assertTrue(didYouKnows.contains(message1Active));
		Assert.assertTrue(didYouKnows.contains(message3Active));
		Assert.assertFalse(didYouKnows.contains(message2InActive));
		Assert.assertFalse(didYouKnows.contains(archieve1Active));
		Assert.assertFalse(didYouKnows.contains(archieve2TypeInActive));
	}
}
