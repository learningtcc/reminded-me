package com.homefellas.dao.cassandra.core;

import java.sql.SQLException;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;


public interface ICassandraCallback<T> {

	T doInCassandra(Cassandra.Client client) throws Exception;
}
