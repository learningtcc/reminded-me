package com.homefellas.rule;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class LoginWebServiceTest
{

	@Rule
	public LoginEmbeddedServer server = new LoginEmbeddedServer();
	
//	@BeforeClass
//    public static void setUpClass() throws Exception {
//        // rcarver - setup the jndi context and the datasource
//        try {
//            // Create initial context
//            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//                "org.apache.naming.java.javaURLContextFactory");
//            System.setProperty(Context.URL_PKG_PREFIXES, 
//                "org.apache.naming");            
//            InitialContext ic = new InitialContext();
//
//            ic.createSubcontext("java:");
//            ic.createSubcontext("java:/comp");
//            ic.createSubcontext("java:/comp/env");
//            ic.createSubcontext("java:/comp/env/jdbc");
//            
//            DataSource dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource("org.hsqldb.jdbcDriver","jdbc:hsqldb:mem:mydb;sql.enforce_strict_size=false;", "sa", "");
//           
//            ic.bind("java:/comp/env/jdbc/rm", dataSource);
//            ic.bind("java:/comp/env/jdbc/cas", dataSource);
//            
//            Context initContext = new InitialContext();
//            initContext.lookup("java:comp/env/jdbc/cas");
//        }
//        catch (Exception exception)
//        {
//        	exception.printStackTrace();
//        }
//	}
//	
	
	
/*	@Test
	public void testLogin()
	{
		Client client = new Client(Protocol.HTTP);
		Request request = new Request(Method.POST, "/login/v1/tickets");
		request.setEntity("username=tdelesio@gmail.com&password=test12", MediaType.APPLICATION_JSON);
		Response response = client.handle(request);
		
		System.out.println(response.getStatus());

		
		
	}*/
}
