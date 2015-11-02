package com.homefellas.dao.core;

import java.util.Map;
import java.util.UUID;

import ognl.Ognl;
import ognl.OgnlException;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Query;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.gson.Gson;
import com.homefellas.model.core.AbstractGUIDModel;

public abstract class CassandraDao<T extends AbstractGUIDModel> {

	private final static boolean print_cql = true;
	
	private final String cassandraHost = "127.0.0.1";
	private final int cassandraPort = 9142;
	private final String keyspace = "metrics";
	
	abstract public T save(T model);
	abstract public String getTableName();
	abstract public T load(String id);
	abstract public T upadte(T model);
	
	protected T update(T t)
	{
		Query query = QueryBuilder.update(getKeyspace(), getTableName());
		return t;
	}
	protected T load(String id, T t)
	{
	
//		ResultSet results = execute("select * from " + getKeyspace() + "."
//				+ getTableName() + " where id =" + id + ";");
		
		
		
		PreparedStatement statement = getSession().prepare("select * from " + getKeyspace() + "."
				+ getTableName() + " where id =?;");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet results = execute(boundStatement.bind(UUID.fromString(id)));
		
		Row row = results.all().get(0);
		return hydrateModel(row, t);
	}
	
	protected Session getSession()
	{
		Cluster cluster = Cluster.builder().addContactPoints(cassandraHost)
				.withPort(cassandraPort).build();
		return cluster.connect(keyspace);
	}
	public String getKeyspace() {
		return keyspace;
	}
	
	protected ResultSet execute(String query)
	{
		if (print_cql)
			System.out.println(query);
		return getSession().execute(query);
	}
	
	protected ResultSet execute(Query query)
	{
		return getSession().execute(query);
	}
	
	protected T hydrateModel(Row row, T model)
	{
		ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
		
		try {

			for (Definition definition : columnDefinitions) {
				String name = definition.getName();
				DataType dataType = definition.getType();
				
				if (!dataType.isCollection()) {
					Object value=null;
					
					if (DataType.text().equals(dataType)) {
						value = row.getString(name);
					} else if (DataType.bigint().equals(dataType)) {
						value = row.getLong(name);				
					} else if (DataType.uuid().equals(dataType)) {
						value = row.getUUID(name);
					}
					if (value!=null)
						Ognl.setValue(name, model, value);
				}
				else
				{
					
					if (DataType.map(DataType.text(),DataType.text()).equals(dataType))
					{
						Map<String, String> map = row.getMap(name, String.class, String.class);
						Gson gson = new Gson();
						String json = gson.toJson(map);
						Ognl.setValue(name, model, json);
					}
				}
			}
		} catch (OgnlException exception) {
			exception.printStackTrace();
			return null;
		}
		
		return model;
	}
	
	protected T hydrateModelById(Row row, T model)
	{
		String id = row.getUUID("id").toString();
		return load(id, model);
	}
	

}
