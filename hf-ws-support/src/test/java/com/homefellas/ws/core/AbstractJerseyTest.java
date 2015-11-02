package com.homefellas.ws.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.homefellas.dao.core.IDao;
import com.homefellas.user.Role;
import com.homefellas.user.RoleEnum;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

@Deprecated
public abstract class AbstractJerseyTest extends JerseyTest {

	private String unitTestContext;
	protected IDao dao;
	protected PlatformTransactionManager transactionManager;
	
	public AbstractJerseyTest(String wsPackage, String springContextLocation)
	{
		super(new WebAppDescriptor.Builder(wsPackage)
        .contextPath("ws")
        .contextParam("contextConfigLocation",springContextLocation)
        .servletClass(SpringServlet.class)
        .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
        .contextListenerClass(ContextLoaderListener.class)
        .requestListenerClass( RequestContextListener.class )
        .build()); 
		
		this.unitTestContext = springContextLocation;
	}
	
	@BeforeClass
    public static void beforeClass() 
	{
    
		
		Connection c=null;
		Statement s=null;
		try
		{
			c = DriverManager.getConnection( "jdbc:hsqldb:mem:mydb", "SA", "" );
			s = c.createStatement();
			s.execute("CREATE SCHEMA MYDB AUTHORIZATION DBA" );
			s.execute("SET DATABASE DEFAULT INITIAL SCHEMA MYDB");
			
				
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			try
			{
				s.close();
				c.close();
			}
			catch (Exception exception2)
			{
				exception2.printStackTrace();
			}
		}
    }
	
	@Before
	public void setupDefaultDatabaseValues()
	{
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(unitTestContext);
		dao = (IDao)applicationContext.getBean("dao");
		transactionManager = (PlatformTransactionManager)applicationContext.getBean("transactionManager");
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		   @Override
		   protected void doInTransactionWithoutResult(TransactionStatus status) {
				Role role = new Role();
				role.setRoleName(RoleEnum.HF_USER_ROLE.getRole());
				dao.save(role);
				
				Role admin = new Role();
				admin.setRoleName(RoleEnum.HF_ADMIN_ROLE.getRole());
				dao.save(admin);
		   }});
//		Connection c=null;
//		Statement s=null;
//		try
//		{
//			
//			c = DriverManager.getConnection( "jdbc:hsqldb:mem:mydb", "SA", "" );
//			s = c.createStatement();
//			
//			s.execute("INSERT INTO u_role VALUES (1,'"+AuthorizationEnum.HF_USER_ROLE.getRole()+"')");
//			s.execute("INSERT INTO u_role VALUES (2,'"+AuthorizationEnum.HF_ADMIN_ROLE.getRole()+"')");
//		}
//		catch (Exception exception)
//		{
//			exception.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				s.close();
//				c.close();
//			}
//			catch (Exception exception2)
//			{
//				exception2.printStackTrace();
//			}
//		}
	}
}
