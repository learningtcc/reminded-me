package com.homefellas.ws.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.homefellas.dao.core.IDao;
import com.homefellas.exception.IValidationCode;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.rule.AbstractEmbeddedJettyServer;
import com.homefellas.user.Role;
import com.homefellas.user.RoleEnum;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


public abstract class AbstractTestWebService
{
	@Rule 
	public TestName name = new TestName();
	
	
	private static VelocityEngine velocityEngine;
	private static String testName;
	private static Map<String, List<WebServiceCall>> singleMethodTest = new HashMap<String, List<WebServiceCall>>();
	private List<WebServiceCall> wsCalls;
	private WebServiceCall wsCall;
	
	protected abstract AbstractEmbeddedJettyServer getServer();
	protected abstract String getContextRoot();
	
	protected IDao dao;
	protected PlatformTransactionManager transactionManager;
	protected PasswordEncoder passwordEncoder;
	
	protected String getJerseyMapping()
	{
		return "/rest";
	}
	

	@BeforeClass
	public static void beforeClass()
	{
		singleMethodTest = new HashMap<String, List<WebServiceCall>>();
	}
	
	@Before
	public void setupDefaultDatabaseValues()
	{
		dao = (IDao)getServer().getSpringBean("dao");
		transactionManager = (PlatformTransactionManager)getServer().getSpringBean("transactionManager");
		passwordEncoder = (PasswordEncoder)getServer().getSpringBean("passwordEncoder");
		
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
				
				Role guest = new Role();
				guest.setRoleName(RoleEnum.GUEST.getRole());
				dao.save(guest);
				
		   }});
		
		wsCalls = new ArrayList<WebServiceCall>();
		testName = this.getClass().getSimpleName();
		//create html fle
	}
	
	@After
	public void recordTest()
	{
		singleMethodTest.put(name.getMethodName(), wsCalls);
	}
	
	@AfterClass
	public static void afterClass()
	{
		Template template = null;
		VelocityContext context = new VelocityContext();
		StringWriter sw = new StringWriter();
		try
		{
			Velocity.setProperty("resource.loader", "class");
			Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			Velocity.init();
			
			
			context.put("map", singleMethodTest);
		   template = Velocity.getTemplate("com/homefellas/docs/templates/wsTestTemplate.vm");
		   
		   template.merge( context, sw );
		   
		   FileWriter fstream = new FileWriter("target/enunciate/build/docs/"+testName+".html");
		   BufferedWriter out = new BufferedWriter(fstream);
		   out.write(sw.toString());
		   out.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}		

	}
		
	private void recordTest(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Object inputObject, URI action)
	{
		wsCall = new WebServiceCall();
		wsCall.setHttpMethod(getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, null));
		wsCall.setService(webServiceClass);
		wsCall.setMethodName(webServiceMethodName);
		wsCall.setAction(action);
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			mapper.writeValue(new File("c:\\user.json"), inputObject);
			wsCall.setJsonInput(mapper.writeValueAsString(inputObject));
		}
		catch (JsonGenerationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JsonMappingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		wsCall.jsonInput = (inputObject==null) ? null:new Gson().toJson(inputObject);
		
		wsCalls.add(wsCall);
		
	}
	
	protected Client createClient()
	{
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		return Client.create(clientConfig);
	}

	private WebResource resource(String href, Map<String, ?> params)
	{
		URI uri = UriBuilder.fromPath(href).buildFromMap(params);
		
		
		return resource(uri.getPath());
	}
	
	private WebResource resource(String path) {
		return createClient().resource(getServer().uri()).path(path);
	}
	
	protected void assertOKStatus(Response response)
	{
		Assert.assertEquals(Response.Status.OK, response.getStatus());
	}
	
	protected void assertOKStatus(ClientResponse response)
	{
		Assert.assertEquals(ClientResponse.Status.OK.getStatusCode(), response.getStatus());
	}
	
	private URI buildURI(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Object inputClass, Map<String, String> pathParms)
	{
		String wsPath;
		wsPath = getPathForMethod(webServiceClass, webServiceMethodName, (inputClass==null) ? null:new Class[]{inputClass.getClass()}, pathParms);
		
		
		URI uri = UriBuilder.fromPath(getContextRoot()+getJerseyMapping()+wsPath).build();
		
		recordTest(webServiceClass, webServiceMethodName, inputClass, uri);
		return uri;
	}
	
	
	//POST
	private <T>T postToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, null);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(returnClass, inputObject);
	}
	
	private <T>T postToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, null);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(returnClass, inputObject);
	}
	
	private <T>T postToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, Class<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, pathParms);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(returnClass, inputObject);
	}
	
	private <T>T postToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, GenericType<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, pathParms);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(returnClass, inputObject);
	}
	
	
	//GET
	private <T>T getToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, Class<T> returnClass)
	{
		return getToWebService(createClient(), webServiceClass, webServiceMethodName, pathParms, returnClass);
	}
	private <T>T getToWebService(Client client, Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, Class<T> returnClass)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, null, pathParms);
		return client.resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(returnClass);
	}
	
	private <T>T getToWebService(Client client, Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, GenericType<T> returnClass)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, null, pathParms);
		return client.resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(returnClass);
	}
	
	
	//PUT
	private <T>T putToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, null);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(returnClass, inputObject);
	}
	
	private <T>T putToWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Object inputObject)
	{
		URI uri = buildURI(webServiceClass, webServiceMethodName, inputObject, null);
		return createClient().resource(getServer().uri()).path(uri.getPath()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(returnClass, inputObject);
	}
	
	//ALL
	/**
	 * Passes Generic Type return and input
	 */
	protected ClientResponse executeWebServiceCall(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, AbstractModel inputObject)
	{
		return executeWebServiceCall(webServiceClass, webServiceMethodName, null, inputObject);
	}
	
	protected ClientResponse executeWebServiceCall(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms)
	{
		return executeWebServiceCall(webServiceClass, webServiceMethodName, pathParms, null);
	}
	
	protected ClientResponse executeWebServiceCall(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Map<String, String> pathParms, AbstractModel inputObject)
	{
		HTTPMethod method = getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, pathParms);
		if (method.equals(HTTPMethod.PUT))
			return putToWebService(webServiceClass, webServiceMethodName, ClientResponse.class, inputObject);
		else if (method.equals(HTTPMethod.POST))
			return postToWebService(webServiceClass, webServiceMethodName, pathParms, ClientResponse.class, inputObject);
		else if (method.equals(HTTPMethod.GET))
			return getToWebService(webServiceClass, webServiceMethodName, pathParms, ClientResponse.class);
		else
			throw new RuntimeException("Undefined method:"+method+" in callWebService");
	}
	
	
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, AbstractModel inputObject)
	{
		HTTPMethod method = getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, null);
		if (method.equals(HTTPMethod.PUT))
			return putToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else if (method.equals(HTTPMethod.POST))
			return postToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else
			throw new RuntimeException("Undefined method:"+method+" in callWebService");
	}
	
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Object inputObject)
	{
		HTTPMethod method = getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, null);
		if (method.equals(HTTPMethod.PUT))
			return putToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else if (method.equals(HTTPMethod.POST))
			return postToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else
			throw new RuntimeException("Undefined method:"+method+" in callWebService");
	}
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Map<String, String> pathParms)
	{
		return callWebService(webServiceClass, webServiceMethodName, returnClass, pathParms, null);
	}
	
	
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Map<String, String> pathParms, AbstractModel inputObject)
	{
		Client client = createClient();
		return callWebService(client, webServiceClass, webServiceMethodName, returnClass, pathParms, inputObject);
	}
	protected <T>T callWebService(Client client, Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, GenericType<T> returnClass, Map<String, String> pathParms, AbstractModel inputObject)
	{
		HTTPMethod method = getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, pathParms);
		if (method.equals(HTTPMethod.PUT))
			return putToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else if (method.equals(HTTPMethod.POST))
			return postToWebService(webServiceClass, webServiceMethodName, pathParms, returnClass, inputObject);
		else if (method.equals(HTTPMethod.GET))
			return getToWebService(client, webServiceClass, webServiceMethodName, pathParms, returnClass);
		else
			throw new RuntimeException("Undefined method:"+method+" in callWebService");
	}
	
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Map<String, String> pathParms)
	{
		return callWebService(webServiceClass, webServiceMethodName, returnClass, pathParms, null);
	}
	
	
	protected <T>T callWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Map<String, String> pathParms, Object inputObject)
	{
		if (pathParms!=null&&!pathParms.isEmpty())
		{
			Set<String> keys = pathParms.keySet();
			for (String key:keys)
			{
				if (!key.startsWith("{") || !key.endsWith("}"))
					Assert.fail("PathParm keys must start with a { and end with }"); 
//					RuntimeException();
			}
		}
		HTTPMethod method = getHTTPOperationForMethod(webServiceClass, webServiceMethodName, (inputObject==null) ? null:new Class[]{inputObject.getClass()}, pathParms);
		if (method.equals(HTTPMethod.PUT))
			return putToWebService(webServiceClass, webServiceMethodName, returnClass, inputObject);
		else if (method.equals(HTTPMethod.POST))
			return postToWebService(webServiceClass, webServiceMethodName, pathParms, returnClass, inputObject);
		else if (method.equals(HTTPMethod.GET))
			return getToWebService(webServiceClass, webServiceMethodName, pathParms, returnClass);
		else
			throw new RuntimeException("Undefined method:"+method+" in callWebService");
	}
	
	
	
	
	
	private Method getMethod(Class clazz, String methodName, Class[] arguements, Map<String, String> pathParams)
	{
		if (pathParams!=null)
		{
			if (arguements==null)
			{
				arguements = new Class[pathParams.size()];
				for (int i=0;i<pathParams.size();i++)
				{
					arguements[i] = String.class;
				}
			}
			else
			{
				Class[] oldArguements = arguements.clone();
				arguements = new Class[pathParams.size()+arguements.length];
				for (int i=0;i<arguements.length;i++)
				{
					if (i<oldArguements.length)
						arguements[i] = oldArguements[i];
					else
						arguements[i] = String.class;
					
				}
			}
		}
		
		Method method=null;
		try
		{
			method = clazz.getMethod(methodName, arguements);
		}
		catch (NoSuchMethodException e)
		{
			for (Method m:clazz.getMethods())
			{
				if (m.getName().equals(methodName))
					method = m;
			}
			
		}
		
		return method;
	}
	
	protected enum HTTPMethod {PUT,GET,POST,DELETE, UNDEFINED};
	
	
	public HTTPMethod getHTTPOperationForMethod(Class clazz, String methodName, Class[] arguements, Map<String, String> pathParams)
	{
		Method method = getMethod(clazz, methodName, arguements, pathParams);
		if (method.getAnnotation(PUT.class)!=null)
			return HTTPMethod.PUT;
		else if (method.getAnnotation(GET.class)!=null)
			return HTTPMethod.GET;
		else if (method.getAnnotation(POST.class)!=null)
			return HTTPMethod.POST;
		else if (method.getAnnotation(DELETE.class)!=null)
			return HTTPMethod.DELETE;
		else
			return HTTPMethod.UNDEFINED;
			
			
	}
	public String getPathForMethod(Class clazz, String methodName, Class[] arguements)
	{
		return getPathForMethod(clazz, methodName, arguements, null);
	}
	
	
	public String getPathForMethod(Class clazz, String methodName, Class[] arguements, Map<String, String> pathParams)
	{

		Method method=null;
		StringBuffer returnPath = new StringBuffer();
		
		
		try
		{
//			java.lang.annotation.Annotation clazzAnnotation = 
			
			Path clazzPath = (Path)clazz.getAnnotation(Path.class);
			returnPath.append(clazzPath.value());
			
			method = getMethod(clazz, methodName, arguements, pathParams);
			Path path = method.getAnnotation(Path.class);
			
			String pathValue = path.value(); 
			while (pathParams!=null && pathValue.contains("{") && pathValue.contains("}"))
			{
				int startIndex = pathValue.indexOf("{");
				int endIndex = pathValue.indexOf("}");
				String pathParm = pathValue.substring(startIndex, endIndex+1);
				String value = pathParams.get(pathParm);
				if (value == null)
				{
					Assert.fail(pathParm+" was not found in the pathParams map");
				}
				pathValue = pathValue.replace(pathParm, value);
			}
				
			returnPath.append(pathValue);
			return returnPath.toString();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected void assertValidationErrors(ClientResponse clientResponse, List<IValidationCode> validationCodes)
	{
 		Assert.assertEquals(400, clientResponse.getStatus());
		
 		String json = clientResponse.getEntity(String.class);
 		
 		Map<String, IValidationCode> exptectedErrors = new HashMap<String, IValidationCode>(validationCodes.size());
		for (IValidationCode code : validationCodes)
		{
			exptectedErrors.put(code.toString(), code);
		}
 		
 		ObjectMapper mapper = new ObjectMapper();
 		try
		{
			List<ValidationError> errors = mapper.readValue(json, new TypeReference<List<ValidationError>>() {});
			
			for (ValidationError error : errors)
				{
					if (exptectedErrors.get(error.getError()) ==null)
						Assert.fail("Validation code:"+error.getError()+" not found in list of error codes");
					else
						exptectedErrors.remove(error.getError());
				}
				
				if (!exptectedErrors.isEmpty())
				{
					for (IValidationCode code : exptectedErrors.values())
					{	
						Assert.fail(code+" was also returned from the server.  Why aren't you expecting this error?");
					}
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
//		List<Object> actualErrors= clientResponse.getEntity(new GenericType<List<Object>>(){});
		
//		Assert.assertTrue(actualErrors.size()>0);
//		
//		Map<String, IValidationCode> exptectedErrors = new HashMap<String, IValidationCode>(validationCodes.size());
//		for (IValidationCode code : validationCodes)
//		{
//			exptectedErrors.put(code.toString(), code);
//		}
//		
//		for (Object code:actualErrors)
//		{
//			if (exptectedErrors.get(code) ==null)
//				Assert.fail("Validation code:"+code+" not found in list of error codes");
//			else
//				exptectedErrors.remove(code);
//		}
//		
//		if (!exptectedErrors.isEmpty())
//		{
//			for (IValidationCode code : exptectedErrors.values())
//			{	
//				Assert.fail(code+" was also returned from the server.  Why aren't you expecting this error?");
//			}
//		}
	}
	protected void assertValidationError(ClientResponse clientResponse, IValidationCode validationCode)
	{
		assertValidationErrors(clientResponse, new ArrayList<IValidationCode>(Arrays.asList(validationCode)));
		
	}
	
	protected void assertSystemError(ClientResponse clientResponse)
	{
		Assert.assertEquals(400, clientResponse.getStatus());
	}
	
	protected Map<String, String> buildPathParms(String key, String value)
	{
	
		Map<String, String> pathParms = new HashMap<String, String>();
		pathParms.put(key, value);
		return pathParms;
	}
	
	protected Map<String, String> buildPathParms(String key, long value)
	{
	
		return buildPathParms(key, String.valueOf(value));
	}
	
	protected <T> T setEmailAndCallWebService(Class<? extends AbstractWebService> webServiceClass, String webServiceMethodName, Class<T> returnClass, Map<String, String> pathParms, String email)
	{
		Client client = createClient();
		
		client.resource(getServer().uri()+"/servlets/session?email="+email).post();
		return getToWebService(client, webServiceClass, webServiceMethodName, pathParms, returnClass);
	}
	
	protected void setLoggedInUserEmail(String email)
	{
		String url = getServer().uri()+"/servlets/session?email="+email;
		HttpMethod method = new GetMethod(url);
		HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
		HttpClient client = new HttpClient(connectionManager);
		try
		{
			client.executeMethod(method);
		}
		catch (HttpException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		createClient().resource(UriBuilder.fromPath().build());
//		try {
//		    URL myURL = new URL(getServer().uri()+"/servlets/session?email="+email);
//		    URLConnection myURLConnection = myURL.openConnection();
//		    myURLConnection.connect();
//		} 
//		catch (Exception e) { 
//		    e.printStackTrace();
//		} 
		
	}
//	protected void setLoggedInUserEmail(Class<? extends AbstractWebService> clazz, String email)
//	{
//		Map<String, String> pathParms = new HashMap<String, String>();
//		pathParms.put("{name}", AbstractCasFilter.CONST_CAS_ASSERTION);
//		pathParms.put("{value}", email);
//		ClientResponse response = callWebService(clazz, "setSessionValue", ClientResponse.class, pathParms);
//	}
}
