package com.homefellas.dao.cassandra.core;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ognl.Ognl;
import ognl.OgnlException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.homefellas.core.SpringBean;

@Deprecated
public class CassandraDao extends SpringBean {
	private String cassandraHost = "127.0.0.1";
	private int cassandraPort = 9142;
	private String keyspace = "rm";

	public Session getSession()
	{
		Cluster cluster = Cluster.builder().addContactPoints(cassandraHost)
				.withPort(cassandraPort).build();
		return cluster.connect(keyspace);
	}
	
//	@Override
//	public <T> T loadObject(Class<T> entityClass, Serializable primaryKey) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> List<T> loadAllObjects(Class<T> entityClass) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> void saveAllObjects(Collection<T> objects) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public <T> void saveOrUpdate(T object) {
//		
//		
//	}
//
//	@Override
//	public <T> void deleteObject(T object) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public <T> void deleteAllObjects(Collection<T> objects) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public <T> void updateObject(T object) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public <T> T loadByPrimaryKey(Class<T> clazz, long pk) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> T loadByPrimaryKey(Class<T> clazz, String pk) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> T loadByPrimaryKey(Class<T> clazz, Serializable pk) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> void merge(T object) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void flush() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void persist(Object object) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public <T> void refresh(T object) {
//		// TODO Auto-generated method stub
//
//	}
	protected <T> String getTableName(T model)
	{
		return model.getClass().getSimpleName().toLowerCase();
	}

	protected <T> Map<String, Object> buildAttributes(T model)
	{
		//get all the fields
		Field[] fields = model.getClass().getDeclaredFields();
		int numberOfFields = fields.length;
		
		
		//this should be cached so we don't have to make the expensive reflection call
		
		//we need to initialize a map of the names and values
		Map<String, Object> attributes = new LinkedHashMap<String, Object>(10);
		
		try
		{
			attributes.put("id", Ognl.getValue("id", model));
		}
		catch (OgnlException exception)
		{
			throw new RuntimeException(exception);
		}
		
		//loop over the fields
		for (int i = 0; fields != null && i < numberOfFields; i++) {
			
			//if the field is marked as tranient we should skip it
			if (fields[i].getAnnotation(Transient.class) != null)
				continue;

			StringBuilder attributeName = new StringBuilder(fields[i].getName().toLowerCase());
			if (fields[i].getAnnotation(ManyToOne.class) != null)
			{
				attributeName.append(".id");
			}
			
			Object attributeValue;
			try
			{
				attributeValue = Ognl.getValue(attributeName.toString(), model);
			}
			catch (OgnlException exception)
			{
				attributeValue = null;
			}
			attributes.put(attributeName.toString(), attributeValue);
		}
			

		return attributes;
	}
	
//	@Override
	public String buildInsertCQL(String tableName, Map<String, Object> attributes) 
	{

		StringBuilder query = new StringBuilder();		
		try
		{
			query.append("insert into ");
			StringBuilder valuesBuilder = new StringBuilder();
			query.append(tableName);
			query.append(" (");
			int counter=0;
			for (String key : attributes.keySet())
			{
				query.append(key);
				valuesBuilder.append("'");
				valuesBuilder.append(attributes.get(key));
				valuesBuilder.append("'");
				counter++;
				if (counter < attributes.size())
				{
					query.append(", ");
					valuesBuilder.append(", ");
				}
			}
			query.append(") values (");
			query.append(valuesBuilder.toString());
			query.append(");");
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		return query.toString();

	
	}

//	protected <T> T execute(ICassandraCallback<T> action) {
//		TTransport tr = null;
//		try {
//			tr = new TFramedTransport(new TSocket(cassandraHost, cassandraPort));
//			TProtocol proto = new TBinaryProtocol(tr);
//			Cassandra.Client client = new Cassandra.Client(proto);
//			tr.open();
//
//			return action.doInCassandra(client);
//
//		} catch (Exception teException) {
//			teException.printStackTrace();
//		} finally {
//			if (tr != null)
//				tr.close();
//		}
//
//		return null;
//
//	}

	// public AbstractModel get(String id)
	// {
	// try
	// {
	// try
	// {
	// getConnection();
	// String columnFamily = "Profiles";
	// ColumnParent columnParent = new ColumnParent(columnFamily);
	//
	// SlicePredicate predicate = new SlicePredicate();
	// predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]),
	// ByteBuffer.wrap(new byte[0]), false, 100));
	// List<ColumnOrSuperColumn> columnsByKey =
	// client.get_slice(ByteBuffer.wrap(id.getBytes()), columnParent, predicate,
	// ConsistencyLevel.ALL);
	// Profile profile = new Profile();
	// profile.setId(id);
	// Member member = new Member();
	// member.setId(id);
	// profile.setMember(member);
	// for (ColumnOrSuperColumn column:columnsByKey)
	// {
	// String name = new String(column.getColumn().getName());
	// if (name.equals("name"))
	// profile.setName(new String(column.getColumn().getValue()));
	// else if (name.equals("email"))
	// profile.getMember().setEmail(new String(column.getColumn().getValue()));
	// else if (name.equals("password"))
	// profile.getMember().setPassword(new
	// String(column.getColumn().getValue()));
	//
	// }
	//
	// return profile;
	// }
	// catch (InvalidRequestException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (UnavailableException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (TimedOutException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (TException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	// finally
	// {
	// closeConnection();
	// }
	// }
	//
	// public <T> T save(final T model)
	// {
	//
	//
	// return execute(new ICassandraCallback<T>() {
	//
	// @Override
	// public T doInCassandra(Client client)
	// throws Exception
	// {
	//
	// client.set_keyspace(keyspace);
	//
	// long timestamp = System.currentTimeMillis();
	// String pk = UUID.randomUUID().toString();
	// String columnFamily = model.getClass().getSimpleName();
	//
	// ColumnParent columnParent = new ColumnParent(columnFamily);
	//
	// Class<?> clazz = model.getClass();
	// Field[] fields = clazz.getDeclaredFields();
	// for (Field field:fields)
	// {
	// Transient transient1 = field.getAnnotation(Transient.class);
	// if (transient1!=null)
	// continue;
	//
	// OneToOne oneToOne = field.getAnnotation(OneToOne.class);
	// ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
	// if (oneToOne != null || manyToOne != null)
	// {
	// return doInCassandra(client);
	// }
	// else
	// {
	// String attribute = field.getName();
	// String methodName = "get"+attribute.substring(0,
	// 1).toUpperCase()+attribute.substring(1);
	// String value = clazz.getMethod(methodName, null).invoke(model,
	// null).toString();
	// Column column = new Column(ByteBuffer.wrap(attribute.getBytes()));
	// column.setTimestamp(timestamp);
	// column.setValue(value.getBytes());
	//
	// client.insert(ByteBuffer.wrap(pk.getBytes()),
	// columnParent,column,ConsistencyLevel.ALL) ;
	// }
	// }
	//
	// return model;
	//
	// }
	//
	//
	// });
	// }

	// public void insert(Profile profile)
	// {
	// try
	// {
	// profile.setId(UUID.randomUUID().toString());
	//
	// long timestamp = System.currentTimeMillis();
	//
	// String columnFamily = "Profiles";
	// Column name = new Column(ByteBuffer.wrap("name".getBytes()));
	// name.setTimestamp(timestamp);
	// name.setValue(profile.getName().getBytes());
	//
	// Column email = new Column(ByteBuffer.wrap("email".getBytes()));
	// email.setTimestamp(timestamp);
	// email.setValue(profile.getMember().getEmail().getBytes());
	//
	// Column password = new Column(ByteBuffer.wrap("password".getBytes()));
	// password.setTimestamp(timestamp);
	// password.setValue(profile.getMember().getPassword().getBytes());
	//
	// ColumnParent columnParent = new ColumnParent(columnFamily);
	// try
	// {
	// getConnection();
	//
	// client.insert(ByteBuffer.wrap(profile.getId().getBytes()),
	// columnParent,name,ConsistencyLevel.ALL) ;
	// client.insert(ByteBuffer.wrap(profile.getId().getBytes()),
	// columnParent,email,ConsistencyLevel.ALL) ;
	// client.insert(ByteBuffer.wrap(profile.getId().getBytes()),
	// columnParent,password,ConsistencyLevel.ALL) ;
	// }
	// catch (InvalidRequestException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (UnavailableException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (TimedOutException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch (TException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// finally
	// {
	// closeConnection();
	// }
	// }

}
