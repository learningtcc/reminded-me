package com.homefellas.rm.notification;

import java.util.List;

import com.homefellas.batch.Notification;

public interface IClientNotificationDao
{

	//create
	public ClientNotification createClientNotification(ClientNotification clientNotification);
	public Device createDevice(Device device);
	
	//read
	public List<ClientNotification> getClientNotificationByIds(List<String> ids);
	public List<Device> getBulkDevices(List<String> ids, String email);
	public Device getDeviceById(String id);
	public ClientNotification getClientNotificaitonById(String id);
	public List<Notification> getNotificationQueue();
	
	//update
	public Device updateDevice(Device device);
	public ClientNotification updateClientNotification(ClientNotification clientNotification);
}
