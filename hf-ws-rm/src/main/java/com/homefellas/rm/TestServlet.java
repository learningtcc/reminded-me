package com.homefellas.rm;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class TestServlet extends ServletContainer {

	public TestServlet()
	{
		System.out.println("loaded");
	}
}
