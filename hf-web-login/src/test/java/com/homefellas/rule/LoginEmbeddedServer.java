package com.homefellas.rule;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.rules.ExternalResource;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.xml.XmlConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LoginEmbeddedServer extends ExternalResource
{
	private Server server;
	private int port = 8888;
	
	public Object getSpringBean(String beanName) {
        WebAppContext jettyWebAppContext = (WebAppContext) server.getHandler();
        ServletContext servletContext = jettyWebAppContext.getServletHandler().getServletContext();
        WebApplicationContext springWebAppContext =
            WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        return springWebAppContext.getBean(beanName);
    }
	
	@Override
	protected void before() throws Throwable
	{
		String[] configurationClasses = new String[] {"org.mortbay.jetty.webapp.WebInfConfiguration",
				"org.mortbay.jetty.plus.webapp.EnvConfiguration",
				"org.mortbay.jetty.plus.webapp.Configuration",
				"org.mortbay.jetty.webapp.JettyWebXmlConfiguration",
				"org.mortbay.jetty.webapp.TagLibConfiguration"};
		
		WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/login");
//		WebAppContext webAppContext = new WebAppContext();
//		webAppContext.setContextPath("/login");
		webAppContext.setConfigurationClasses(configurationClasses);
		
//		Resource casjndi = new Resource("jdbc/cas", new org.springframework.jdbc.datasource.DriverManagerDataSource("org.hsqldb.jdbcDriver","jdbc:hsqldb:mem:mydb;sql.enforce_strict_size=false;", "sa", ""));
//		Resource rmjndi = new Resource("jdbc/rm", new org.springframework.jdbc.datasource.DriverManagerDataSource("org.hsqldb.jdbcDriver","jdbc:hsqldb:mem:mydb;sql.enforce_strict_size=false;", "sa", ""));
		
		server = new Server(port);
		server.addHandler(webAppContext );
		
		String[] configFiles = {"jetty.xml"};
		for(String configFile : configFiles) {
			XmlConfiguration configuration = new XmlConfiguration(new File(configFile).toURI().toURL());
			configuration.configure(server);
		}

		
		
//		WebAppDeployer webAppDeployer = new WebAppDeployer();
//		webAppDeployer.setConfigurationClasses(configurationClasses);
//		webAppDeployer.setParentLoaderPriority(false);
//		webAppDeployer.setExtract(true);
//		webAppDeployer.setContexts(webAppContext);
//		webAppDeployer.setAllowDuplicates(false);
//		webAppDeployer.setWebAppDir("src/main/webapp");
//		server.addLifeCycle(webAppDeployer);
		
//		HandlerList handlerList = new HandlerList();
//		Handler[] handlers = new Handler{webAppContext, new DefaultHandler()};
//		handlerList.setHandlers(handlers);

		server.start();
	}

	@Override
	protected void after()
	{
		try
		{
			server.stop();
		}
		catch (Throwable t)
		{
		}
	}

	public String uri()
	{
		return "http://localhost:" + port;
	}
}
