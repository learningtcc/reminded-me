package com.homefellas.util;

import org.junit.Test;

import com.google.gson.Gson;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.task.Category;
import com.homefellas.user.Profile;

public class JSONHelper {

	/**
	 * @param args
	 */
	@Test
	public void printme()
	{
		Profile profile = RMTestModelBuilder.buildBasicMember(false,null);
		Category category = RMTestModelBuilder.buildSampleCategory(true, true, profile.getMember());
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(category));
	}

}
