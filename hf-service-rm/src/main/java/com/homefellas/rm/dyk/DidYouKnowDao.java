package com.homefellas.rm.dyk;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowStatus;
import com.homefellas.rm.dyk.DidYouKnow.DidYouKnowType;

public class DidYouKnowDao extends HibernateCRUDDao implements IDidYouKnowDao
{

	public List<DidYouKnow> getValidDidYouKnowMessages()
	{
		Query query = getBrochureQuery("from DidYouKnow d where d.didYouKnowTypeOrdinal = ? and d.didYouKnowStatusOrdinal = ?");
		query.setParameter(0, DidYouKnowType.message.ordinal());
		query.setParameter(1, DidYouKnowStatus.ACTIVE.ordinal());
		return query.list();
		
	}
	
	public List<DidYouKnow> getValidDidYouKnowMessages(Date date)
	{
		Query query = getBrochureQuery("from DidYouKnow d where d.didYouKnowTypeOrdinal = ? and d.didYouKnowStatusOrdinal = ? and d.date=?");
		query.setParameter(0, DidYouKnowType.message.ordinal());
		query.setParameter(1, DidYouKnowStatus.ACTIVE.ordinal());
		query.setParameter(2, date);
		return query.list();
		
	}
	
	public void saveDidYouKnow(DidYouKnow didYouKnow)
	{
		getBrochureSession().save(didYouKnow);
	}
}
