package com.homefellas.rm.dyk;

import java.sql.Date;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Before;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowStatus;
import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowType;
import com.homefellas.test.core.AbstractTest;

public abstract class AbstractBrochureTest extends AbstractTest
{

	protected DidYouKnow message1Active;
	protected DidYouKnow message2InActive;
	protected DidYouKnow message3Active;
	protected DidYouKnow archieve1Active;
	protected DidYouKnow archieve2TypeInActive;

	@Resource(name="didYouKnowDao")
	protected IDidYouKnowDao didYouKnowDao;
	
	@Resource(name="&brochureSessionFactory")	 
	protected LocalSessionFactoryBean factory;
	
	@Before
	public void recreateSchema() 
	{
		 
		System.out.println("**RECREATION SCHEMA**");		
		factory.dropDatabaseSchema();
		factory.createDatabaseSchema();
		
		createDatabaseDefaults();
	}
	
	@Transactional("brochureTransactionManager")
	protected void createDatabaseDefaults()
	{
		factory.dropDatabaseSchema();
		factory.createDatabaseSchema();
//		super.createDatabaseDefaults();
		
		message1Active = new DidYouKnow();
		message1Active.setCreatedDateZone(new DateTime());
		message1Active.setDate(new Date(System.currentTimeMillis()));
		message1Active.setDidYouKnowStatusOrdinal(DidYouKnowStatus.ACTIVE.ordinal());
		message1Active.setMessage("message1Active");
		message1Active.setUrl("http://reminded.me");
		message1Active.setDidYouKnowTypeOrdinal(DidYouKnowType.message.ordinal());
		didYouKnowDao.saveDidYouKnow(message1Active);
		
		message2InActive = new DidYouKnow();
		message2InActive.setCreatedDateZone(new DateTime());
		message2InActive.setDate(new Date(System.currentTimeMillis()));
		message2InActive.setDidYouKnowStatusOrdinal(DidYouKnowStatus.INACTIVE.ordinal());
		message2InActive.setMessage("message2InActive");
		message2InActive.setUrl("http://reminded.me");
		message2InActive.setDidYouKnowTypeOrdinal(DidYouKnowType.message.ordinal());
		didYouKnowDao.saveDidYouKnow(message2InActive);
		
		message3Active = new DidYouKnow();
		message3Active.setCreatedDateZone(new DateTime());
		message3Active.setDate(new Date(System.currentTimeMillis()));
		message3Active.setDidYouKnowStatusOrdinal(DidYouKnowStatus.ACTIVE.ordinal());
		message3Active.setMessage("message3Active");
		message3Active.setUrl("http://reminded.me");
		message3Active.setDidYouKnowTypeOrdinal(DidYouKnowType.message.ordinal());
		didYouKnowDao.saveDidYouKnow(message3Active);
		
		archieve1Active = new DidYouKnow();
		archieve1Active.setCreatedDateZone(new DateTime());
		archieve1Active.setDate(new Date(System.currentTimeMillis()));
		archieve1Active.setDidYouKnowStatusOrdinal(DidYouKnowStatus.ACTIVE.ordinal());
		archieve1Active.setMessage("archieve1Active");
		archieve1Active.setUrl("http://reminded.me");
		archieve1Active.setDidYouKnowTypeOrdinal(DidYouKnowType.archive.ordinal());
		didYouKnowDao.saveDidYouKnow(archieve1Active);
		
		archieve2TypeInActive = new DidYouKnow();
		archieve2TypeInActive.setCreatedDateZone(new DateTime());
		archieve2TypeInActive.setDate(new Date(System.currentTimeMillis()));
		archieve2TypeInActive.setDidYouKnowStatusOrdinal(DidYouKnowStatus.INACTIVE.ordinal());
		archieve2TypeInActive.setMessage("archieve2TypeInActive");
		archieve2TypeInActive.setUrl("http://reminded.me");
		archieve2TypeInActive.setDidYouKnowTypeOrdinal(DidYouKnowType.archive.ordinal());
		didYouKnowDao.saveDidYouKnow(archieve2TypeInActive);
	}
}
