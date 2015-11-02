package com.homefellas.rm.user;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.metrics.CollectTimeMetrics;
import com.homefellas.service.core.AbstractService;

public class PersonalPointScoreService extends AbstractService 
{

	@Autowired
	private IPersonalPointScoreDao personalPointScoreDao;
	
	@Transactional
	@CollectTimeMetrics
	public List<PersonalPointScore> getLatestPersonalPointScoresTX(String email)
	{
		DateTime today = new DateTime();
		List<PersonalPointScore> scores = new ArrayList<PersonalPointScore>(1);
		PersonalPointScore personalPointScore = personalPointScoreDao.getPersonalPointScoreByDate(email, new Date(today.getMillis()));
		if (personalPointScore == null)
			return Collections.EMPTY_LIST;
		
		scores.add(personalPointScore);
		return scores;
	}
	
	@Transactional
	@CollectTimeMetrics
	public PersonalPointScore getPersonalPointScoreByDateTX(String email, String date)
	{
		DateFormat formatter = new SimpleDateFormat("MMddyyyy");
		try
		{
			return personalPointScoreDao.getPersonalPointScoreByDate(email, new Date(formatter.parse(date).getTime()));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	@CollectTimeMetrics
	public List<PersonalPointScore> getPersonalPointScoreByDateRangeTX(String email, String start, String end)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat("MMddyyyy");
			Date startDate =  new Date(formatter.parse(start).getTime());
			Date endDate =  new Date(formatter.parse(end).getTime());
			
			return personalPointScoreDao.getPersonalPointScoreByDateRange(email,startDate,endDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Transactional
	public PersonalPointScore createPersonalPointScore(PersonalPointScore personalPointScore)
	{
		return personalPointScoreDao.createPersonalPointScore(personalPointScore);
	}
}
