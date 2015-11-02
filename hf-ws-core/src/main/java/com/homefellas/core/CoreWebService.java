package com.homefellas.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.joda.time.DateTime;

import com.homefellas.exception.ValidationException;
import com.homefellas.model.core.AbstractModel;
import com.homefellas.rm.IRemindedMeService;
import com.homefellas.rm.ValidationCodeEnum;
import com.homefellas.rm.reminder.Alarm;
import com.homefellas.rm.task.Category;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Member;
import com.homefellas.ws.core.AbstractWebService;

@Path("/core")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CoreWebService extends AbstractWebService 
{
	private IRemindedMeService remindedMeService;
	
	public void setRemindedMeService(IRemindedMeService remindedMeService)
	{
		this.remindedMeService = remindedMeService;
	}
	

	/**
	 * This method is a 404 tester.  A lot of the times when bad json is sent to a post, the request comes back as invalid because the json parms
	 * do not match what is expected.  This generates a 404 error which is a false positive.  This method is used to try and help with those errors
	 * and will validate the payload against an object.
	 * @param jsonPayload
	 * @param className
	 * @return
	 */
	@POST
	@Path("/validate/payload/{classname}")
	public Response validatePayload(String jsonPayload, @PathParam("classname")String className)
	{
		Class clazz;
		try
		{
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			return handleException(e, "The action you are calling "+e.getMessage()+" is not a valid class name.  Please use a valid class name.");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			Object o = objectMapper.readValue(jsonPayload, clazz);
			System.out.println(o.toString());
		}
		catch (JsonParseException e)
		{
			return handleException(e);
		}
		catch (JsonMappingException e)
		{
			return handleException(e);
		}
		catch (IOException e)
		{
			return handleException(e);
		}
		
		return buildSuccessResponse("Your input is valid");
	}
	
	private Object recursiveObjectCreation(Class<?> clazz, Object o) throws Exception
	{
		
		for (Method method :clazz.getMethods())
		{
			String methodName = method.getName();
			if (methodName.startsWith("get") && !methodName.equals("getClass"))
			{
				String attributeName = methodName.substring(3,4).toLowerCase()+methodName.substring(4);
				try
				{
					Annotation[] annotations = clazz.getDeclaredField(attributeName).getAnnotations();
				
					for (int i=0;annotations!=null&&i<annotations.length;i++)
					{
						if (annotations[i].annotationType().equals(JsonBackReference.class))
							return o;
					}
				}
				catch (NoSuchFieldException exception)
				{
					System.out.println(exception.getMessage());
				}
				
				String setterName = "set"+methodName.substring(3);
				Class<?> getObjectClass = method.getReturnType();
				if (getObjectClass.isPrimitive())
					continue;
				
				if (getObjectClass.getName().equals("java.util.Set") || getObjectClass.getName().equals("java.util.List"))
				{
					
					ParameterizedType t = (ParameterizedType)method.getGenericReturnType();
					Object objectToBeAddedToMap;
					Class<?> genericTypeClass = (Class<?>)t.getActualTypeArguments()[0]; 

					if (genericTypeClass.isInterface())
					{
						if (genericTypeClass.getName().equals("com.homefellas.rm.task.ICategory"))
							objectToBeAddedToMap = new Category();
						else 
						{
							continue;
						}
					}
					else
						objectToBeAddedToMap = genericTypeClass.newInstance();
					
					if (objectToBeAddedToMap instanceof AbstractModel)
					{
						recursiveObjectCreation(genericTypeClass, objectToBeAddedToMap);
					}
					Collection<Object> collection=null;
					if (getObjectClass.getName().equals("java.util.Set"))
						collection = new HashSet<Object>();
					else if (getObjectClass.getName().equals("java.util.List"))
						collection = new ArrayList<Object>();
					else
						System.out.println("non-matched collection");
					collection.add(objectToBeAddedToMap);
					Method setterMethod = clazz.getMethod(setterName, getObjectClass);
					setterMethod.invoke(o, collection);
					
					continue;
				}
				
				if (getObjectClass.isInterface() || getObjectClass.isAnonymousClass())
				{
					System.out.println(getObjectClass.toString());
					continue;
				}
					
				Object getObjectType = getObjectClass.newInstance();
				
				if (getObjectType instanceof DateTime)
				{
					Method setterMethod = clazz.getMethod(setterName, getObjectClass);
					setterMethod.invoke(o, getObjectType);
				}
				if (getObjectType instanceof AbstractModel)
				{
					//call the g
					Method setterMethod = clazz.getMethod(setterName, getObjectClass);
					setterMethod.invoke(o, getObjectType);
					
					
					recursiveObjectCreation(getObjectType.getClass(), getObjectType);
				}
				
				
			}
		}
		return o;
	}
	
	/**
	 * This will convert an object to a json string.  This is done now by the WS API, but this was built before that existed
	 * @param fullyQualifiedClassName A fully qualified path of a class is required.  
	 * @return A string that represents the json of the class
	 */
	@GET
	@Path("/jsonize/{fqn}")
	public Response jsonizeObjectByFQN(@PathParam("fqn")String fullyQualifiedClassName)
	{
		ObjectMapper mapper = new ObjectMapper();
		Class<?> clazz;
		try
		{
			clazz = Class.forName(fullyQualifiedClassName);
			Object instance = clazz.newInstance();
			
			recursiveObjectCreation(clazz, instance);

			return buildSuccessResponse(mapper.writeValueAsString(instance));

		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}

	/**
	 * This will create all the default values for the database.  It can be run as many times as you want.  If the values
	 * exist in the database already it won't create multiple instances of them
	 * @return Returns if it was successfull or not.
	 */
	@GET
	@Path("/default")
	public Response createRemindedMeDataBaseDefaults()
	{
		try
		{
			remindedMeService.createDefaultDatabaseEntries();
			return buildSuccessResponse(true);
		}
		catch (Exception exception)
		{
			return handleException(exception);
		}
	}
	
	@POST
	@Path("/testtz")
	public Response testTimeZone(Alarm alarm)
	{
		System.out.println(alarm.getAlarmTime().toString());
		System.out.println(alarm.getAlarmTime().isBeforeNow());
		return buildSuccessResponse(alarm.getAlarmTime());
	}
}
