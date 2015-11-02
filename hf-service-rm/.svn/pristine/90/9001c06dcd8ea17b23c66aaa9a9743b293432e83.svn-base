package com.homefellas.rm.dyk;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

public class DidYouKnowServiceTest extends AbstractBrochureTest
{

	@Resource(name="didYouKnowService")
	protected IDidYouKnowService didYouKnowService;
	
	@Test
	public void getValidDidYouKnowMessages()
	{
		List<DidYouKnow> didYouKnows = didYouKnowService.getValidDidYouKnowMessages();
		Assert.assertTrue(didYouKnows.contains(message1Active));
		Assert.assertTrue(didYouKnows.contains(message3Active));
		Assert.assertFalse(didYouKnows.contains(message2InActive));
		Assert.assertFalse(didYouKnows.contains(archieve1Active));
		Assert.assertFalse(didYouKnows.contains(archieve2TypeInActive));
	}
}
