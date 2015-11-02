package com.homefellas.test.core;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:com/homefellas/context/unittest-context.xml"})
@TransactionConfiguration(defaultRollback=true)
//@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
//@CassandraDataSet(value = { "simple.cql" })
//@EmbeddedCassandra
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
