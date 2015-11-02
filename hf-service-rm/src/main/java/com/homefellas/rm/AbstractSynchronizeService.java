package com.homefellas.rm;

import org.joda.time.DateTime;

import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractGUIDModel;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.rm.calendar.Calendar;
import com.homefellas.service.core.AbstractService;

public abstract class AbstractSynchronizeService extends AbstractService
{
//
//	public <T extends ISynchronizeable, V extends IRMValidator> T updateSynchonrizedObject(Class<T> clazz, T objectToUpdate, V validator) throws ValidationException
//	{
//		T objectFromDb = dao.loadByPrimaryKey(clazz, objectToUpdate.getPrimaryKey());
//		validator.validateSynchronizationUpdate(objectToUpdate, objectFromDb);
//		objectToUpdate.setModifiedDateZone(new DateTime());
//		
//		dao.merge(objectToUpdate);
//		
//		return objectToUpdate;
//	}
}
