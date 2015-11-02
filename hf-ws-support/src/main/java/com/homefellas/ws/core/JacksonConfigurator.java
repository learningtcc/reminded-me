package com.homefellas.ws.core;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Component;

//@Component
@Provider
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper = new ObjectMapper();

	public JacksonConfigurator()
	{
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		mapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, false).configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
//		SerializationConfig serConfig = mapper.getSerializationConfig();
//        serConfig.setDateFormat(new SimpleDateFormat(<my format>));
//        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
//        deserializationConfig.setDateFormat(new SimpleDateFormat(<my format>));
//        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//        deserializationConfig
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
//		mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types

	}
	
	 @Override
	    public ObjectMapper getContext(Class<?> arg0) {
	        return mapper;
	    }

}
