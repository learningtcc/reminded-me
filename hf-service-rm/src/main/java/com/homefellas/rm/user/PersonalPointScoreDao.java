package com.homefellas.rm.user;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;

public class PersonalPointScoreDao extends HibernateCRUDDao implements IPersonalPointScoreDao
{

	public PersonalPointScore getPersonalPointScoreByDate(String email, Date date)
	{
		Query query = getQuery("from PersonalPointScore p where p.member.email=? and p.createDate=?");
		query.setString(0, email);
		query.setParameter(1, date);
		
		return (PersonalPointScore)query.uniqueResult();
	}
	
	public List<PersonalPointScore> getPersonalPointScoreByDateRange(String email, Date start, Date end)
	{
		Query query = getQuery("from PersonalPointScore p where p.member.email=? and p.createDate between ? and ?");
		query.setString(0, email);
		query.setParameter(1, start);
		query.setParameter(2, end);
//		query.setParameter(3, start);
//		query.setParameter(4, end);
		
		List<PersonalPointScore> list = query.list();
		return list;
	}

	@Override
	public PersonalPointScore createPersonalPointScore(
			PersonalPointScore personalPointScore) {
		save(personalPointScore);
		return personalPointScore;
	}

	@Override
	public PersonalPointScore updatePersonalPointScore(
			PersonalPointScore personalPointScore) {
		updateObject(personalPointScore);
		return personalPointScore;
	}

	@Override
	public PersonalPointScore getPersonalPointScoreById(String id) {
		return loadByPrimaryKey(PersonalPointScore.class, id);
	}
	
	
	
}
