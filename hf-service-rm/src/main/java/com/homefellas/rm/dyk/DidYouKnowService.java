package com.homefellas.rm.dyk;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.service.core.AbstractService;

public class DidYouKnowService extends AbstractService implements IDidYouKnowService
{

	@Autowired
	private IDidYouKnowDao didYouKnowDao;

	
	@Transactional("brochureTransactionManager")
	public List<DidYouKnow> getValidDidYouKnowMessages()
	{
		return didYouKnowDao.getValidDidYouKnowMessages();
	}
	
	@Transactional("brochureTransactionManager")
	public List<DidYouKnow> getValidDidYouKnowMessages(Date date)
	{
		return didYouKnowDao.getValidDidYouKnowMessages(date);
	}
	
}
