package com.homefellas.email.core;

import com.homefellas.dao.hibernate.core.HibernateCRUDDao;
import com.homefellas.model.core.Email;

public class EmailHibernateDao extends HibernateCRUDDao implements IEmailDao {

	@Override
	public Email createEmail(Email email) {
		save(email);
		
		return email;
	}

}
