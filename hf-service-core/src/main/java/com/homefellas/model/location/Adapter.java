package com.homefellas.model.location;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter extends XmlAdapter<Object, Object> {

	@Override
	public Object marshal(Object arg0) throws Exception {
		return arg0;
	}

	@Override
	public Object unmarshal(Object arg0) throws Exception {
		return arg0;
	}

	
}
