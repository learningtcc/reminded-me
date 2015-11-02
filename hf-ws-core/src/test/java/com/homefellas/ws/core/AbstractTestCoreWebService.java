package com.homefellas.ws.core;

import org.junit.Rule;

import com.homefellas.rule.AbstractEmbeddedJettyServer;
import com.homefellas.rule.EmbeddedTestServer;

public abstract class AbstractTestCoreWebService extends AbstractTestWebService
{

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

		return "/ws-core";
	}


}
