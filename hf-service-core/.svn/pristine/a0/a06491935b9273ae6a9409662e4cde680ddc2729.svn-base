package com.homefellas.core;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SpringBeanToStringBuilder extends ReflectionToStringBuilder {

	/**
	 * Returns a String representation of a core component.
	 * 
	 * @param component
	 * @return String
	 */
	public static String reflectionToString(SpringBean component) {
		return new SpringBeanToStringBuilder(component).toString();
	}

	// component toString style
	private static final ToStringStyle COMPONENT_TO_STRING_STYLE = new ComponentToStringStyle();

	// constructor
	private SpringBeanToStringBuilder(Object object) {
		super(object, COMPONENT_TO_STRING_STYLE);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void appendFieldsIn(Class clazz) {
		
		if (clazz.isArray()) {
			this.reflectionAppendArray(this.getObject());
			return;
		}
		
		Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		
		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			String fieldName = field.getName();
			
			if(!containsSetter(fieldName, clazz))
				continue;
			
			if (this.accept(field))
			{
				try
				{
					Object fieldValue = this.getValue(field);
					
					this.append(fieldName, fieldValue instanceof SpringBean ? 
							((SpringBean)fieldValue).getSimpleToString() : fieldValue);
				}
				catch (IllegalAccessException ex)
				{
					throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
				}
			}
		}
	}

	/**
	 * Returns true if a class contains a setter method for a fieldName.
	 * 
	 * @param fieldName
	 * @param clazz
	 * @return boolean
	 */
	private boolean containsSetter(String fieldName, Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		
		for(int i=0; i<methods.length; i++)
			if(methods[i].getName().toLowerCase().equals("set"+fieldName.toLowerCase()))
				return true;
		
		return false;
	}

	/**
	 * ComponentToStringStyle
	 * 
	 * Style that outputs on multiple lines with indentation for legibility.
	 * 
	 * @version 1.0
	 * @author Anthony DePalma
	 */
	private static final class ComponentToStringStyle extends ToStringStyle {

		// constructor
		ComponentToStringStyle() {
			super();
			this.setContentStart(SystemUtils.LINE_SEPARATOR + "[");
			this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "    ");
			this.setFieldSeparatorAtStart(true);
			this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
		}

	}
	
}