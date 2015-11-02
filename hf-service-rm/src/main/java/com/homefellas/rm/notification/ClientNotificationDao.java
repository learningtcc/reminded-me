package com.homefellas.rm.notification;

import java.util.List;

import org.hibernate.Query;

import com.homefellas.batch.Notification;
import com.homefellas.dao.hibernate.core.HibernateCRUDDao;

public class ClientNotificationDao extends HibernateCRUDDao implements IClientNotificationDao
{

	public List<ClientNotification> getClientNotificationByIds(List<String> ids)
	{
		Query query = getQuery("from ClientNotification cn " +
				"where cn.id in (:ids)");
		query.setParameterList("ids", ids);
		
		return query.list();		
		
		
		
	}
	
	public List<Device> getBulkDevices(List<String> ids, String email)
	{
		Query query = getQuery("from Device d " +
				"where d.id in (:ids) and d.profile.member.email=(:email)");
		query.setParameterList("ids", ids);
		query.setParameter("email", email);
		
		return query.list();		
	}

	@Override
	public ClientNotification createClientNotification(
			ClientNotification clientNotification) {
		save(clientNotification);
		return clientNotification;
	}

	@Override
	public Device createDevice(Device device) {
		save(device);
		return device;
	}

	@Override
	public Device getDeviceById(String id) {
		return loadByPrimaryKey(Device.class, id);
	}

	@Override
	public ClientNotification getClientNotificaitonById(String id) {
		return loadByPrimaryKey(ClientNotification.class, id);
	}

	@Override
	public Device updateDevice(Device device) {
		updateObject(device);
		return device;
	}

	@Override
	public ClientNotification updateClientNotification(
			ClientNotification clientNotification) {
		updateObject(clientNotification);
		return clientNotification;
	}

	@Override
	public List<Notification> getNotificationQueue() {
		return loadAllObjects(Notification.class);
	}
	
	
	
	
}
