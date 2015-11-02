package com.homefellas.ws.core;

import org.junit.Rule;

import com.homefellas.rule.AbstractEmbeddedJettyServer;
import com.homefellas.rule.EmbeddedTestServer;

public class AbstractLocationWebServiceTest extends AbstractTestWebService {

	@Rule
	public EmbeddedTestServer server = new EmbeddedTestServer();
	
	@Override
	protected AbstractEmbeddedJettyServer getServer()
	{
		return server;
	}

	@Override
	protected String getContextRoot()
	{
		return "/location";
	}

	
}