package com.homefellas.test.core;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.homefellas.dao.cassandra.core.CassandraDao;

@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
@CassandraDataSet(value = { "simple.cql" })
@EmbeddedCassandra
public class CassandraUnitTester extends AbstractTest {

	@Autowired 
	protected CassandraDao cassandraDao;
	
	protected Session session;
	
	@Before
	public void createSession()
	{
		Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1")
				.withPort(9142).build();
		session = cluster.connect("rm");
	}
	
			@Test 
			public void insert()
			{
//			CassandraTestModel model = new CassandraTestModel();
//			model.setFirstName("Tim");
//			model.setLastName("Delesio");
//			model.setSsn("234-423-3442");
//			cassandraDao.save(model);
//			
////			ResultSet result = session.execute("select * from cassandratestmodel WHERE firstName='Tim'");
////			Assert.assertEquals(result.iterator().next().getString("value"), "Tim");
//			
//			ResultSet result = session.execute("select * from cassandratestmodel WHERE id='"+model.getId()+"'");
//			Assert.assertEquals(result.iterator().next().getString("id"), model.getId());
		}
//	
//	@Test
//	public void should_have_started_and_execute_cql_script() throws Exception {
//		
//
//		ResultSet result = session
//				.execute("select * from mytable WHERE id='myKey01'");
//		Assert.assertEquals(result.iterator().next().getString("value"), "myValue01");
//
//	}

}
