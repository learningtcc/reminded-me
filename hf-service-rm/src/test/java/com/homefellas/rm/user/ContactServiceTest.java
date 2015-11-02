package com.homefellas.rm.user;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.homefellas.rm.AbstractRMTestDao;
import com.homefellas.rm.RMTestModelBuilder;
import com.homefellas.rm.share.Share;
import com.homefellas.rm.task.Task;
import com.homefellas.user.Profile;
import com.homefellas.util.FileHelper;

public class ContactServiceTest extends AbstractRMTestDao
{

	@Autowired
	private IContactService contactService;


	@After
	@Before
	public void deleteIndexFile()
	{
		String fileName = ((ContactService) contactService)
				.getAllUsersIndexFile();
		FileHelper.deleteFolder(fileName);

	}

	

	@Test
	public void search()
	{
		Profile tim = RMTestModelBuilder.profile("tdelesio@reminded.me");
		tim.setName("Tim Delesio");
		tim = createProfile(tim);

		Profile luv = RMTestModelBuilder.profile("luv@reminded.me");
		luv.setName("Luv Tulsidas");
		luv = createProfile(luv);

		Profile bijal = RMTestModelBuilder.profile("bijal@reminded.me");
		bijal.setName("Bijal Tulsidas");
		bijal = createProfile(bijal);

		Profile shavan = RMTestModelBuilder.profile("shavan@reminded.me");
		shavan.setName("Shavan");
		shavan = createProfile(shavan);

		Profile sabith = RMTestModelBuilder.profile("sp@reminded.me");
		sabith.setName("Sabith Pocker");
		sabith = createProfile(sabith);

		Profile jodie = RMTestModelBuilder.profile("jodie@reminded.me");
		jodie.setName("Jodie Delesio");
		jodie = createProfile(jodie);

		Profile goutham = RMTestModelBuilder.profile("goutham@reminded.me");
		goutham.setName("Galta");
		goutham = createProfile(goutham);

		Profile zak = RMTestModelBuilder.profile("zak@reminded.me");
		zak.setName("Zak ");
		zak = createProfile(zak);

		Profile anish = RMTestModelBuilder.profile("anish@reminded.me");
		anish.setName("anish");
		anish = createProfile(anish);

		Task timTask1 = createTask(tim);
		Task timTask2 = createTask(tim);
		Task timTask3 = createTask(tim);

		Share timShareTask1Luv = createShare(timTask1, luv);
		Share timShareTask2Luv = createShare(timTask2, luv);
		Share timShareTask1Jodie = createShare(timTask1, jodie);
		Share timShareTask2Jodie = createShare(timTask2, jodie);
		Share timShareTask3Zak = createShare(timTask3, zak);
		Share timShareTask2Anish = createShare(timTask2, anish);
		Share timShareTask3Jodie = createShare(timTask3, jodie);
		Share timShareTask1Sabith = createShare(timTask1, sabith);
		Share timShareTask2Bijal = createShare(timTask2, bijal);

		List<Contact> contacts;

		contacts = contactService.searchForContactsTX(tim.getMember()
				.getEmail(), "tulsi");
		Assert.assertEquals(
				contactService.getContactByContactIdTX(bijal.getId()).getId(),
				contacts.get(0).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(luv.getId())
				.getId(), contacts.get(1).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(jodie.getId())
						.getContact().getId(), contacts.get(2).getContact()
						.getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(anish.getId()).getId(),
				contacts.get(3).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(sabith.getId()).getId(),
				contacts.get(4).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(zak.getId())
				.getId(), contacts.get(5).getId());

		contacts = contactService.searchForContactsTX(tim.getMember()
				.getEmail(), "sab");
		Assert.assertEquals(
				contactService.getContactByContactIdTX(sabith.getId()).getId(),
				contacts.get(0).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(jodie.getId()).getId(),
				contacts.get(1).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(luv.getId())
				.getId(), contacts.get(2).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(bijal.getId()).getId(),
				contacts.get(4).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(anish.getId()).getId(),
				contacts.get(3).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(zak.getId())
				.getId(), contacts.get(5).getId());

		// search by email start
		contacts = contactService.searchForContactsTX(tim.getMember()
				.getEmail(), "jodie");
		Assert.assertEquals(
				contactService.getContactByContactIdTX(jodie.getId()).getId(),
				contacts.get(0).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(luv.getId())
				.getId(), contacts.get(1).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(bijal.getId()).getId(),
				contacts.get(3).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(sabith.getId()).getId(),
				contacts.get(4).getId());
		Assert.assertEquals(
				contactService.getContactByContactIdTX(anish.getId()).getId(),
				contacts.get(2).getId());
		Assert.assertEquals(contactService.getContactByContactIdTX(zak.getId())
				.getId(), contacts.get(5).getId());

		contacts = contactService.searchForContactsTX(tim.getMember()
				.getEmail(), "gal");
//		Assert.assertEquals(goutham.getEmail(), contacts.get(0).getEmail());
//		Assert.assertFalse(contacts.get(0).isaContact());

		// search by email @reminded

		// search by email middle
		// search by name
	}

	@Test
	public void getLatestSharesForMember()
	{
		Profile tim = RMTestModelBuilder.profile("tdelesio@reminded.me");
		tim.setName("Tim Delesio");
		tim = createProfile(tim);

		Profile luv = RMTestModelBuilder.profile("luv@reminded.me");
		luv.setName("Luv Tulsidas");
		luv = createProfile(luv);

		Profile shavan = RMTestModelBuilder.profile("shavan@reminded.me");
		shavan.setName("Shavan");
		shavan = createProfile(shavan);

		Profile sabith = RMTestModelBuilder.profile("sp@reminded.me");
		sabith.setName("Sabith Pocker");
		sabith = createProfile(sabith);

		Profile jodie = RMTestModelBuilder.profile("jodie@reminded.me");
		jodie.setName("Jodie Delesio");
		jodie = createProfile(jodie);

		Profile goutham = RMTestModelBuilder.profile("goutham@reminded.me");
		goutham.setName("Galta");
		goutham = createProfile(goutham);

		Profile zak = RMTestModelBuilder.profile("zak@reminded.me");
		zak.setName("Zak ");
		zak = createProfile(zak);

		Profile anish = RMTestModelBuilder.profile("anish@reminded.me");
		anish.setName("anish");
		anish = createProfile(anish);

		Task timTask1 = createTask(tim);
		Task timTask2 = createTask(tim);
		Task timTask3 = createTask(tim);

		Share timShareTask1Luv = createShare(timTask1, luv);
		Share timShareTask2Luv = createShare(timTask2, luv);
		Share timShareTask1Jodie = createShare(timTask1, jodie);
		Share timShareTask2Jodie = createShare(timTask2, jodie);
		Share timShareTask3Zak = createShare(timTask3, zak);
		Share timShareTask2Anish = createShare(timTask2, anish);
		Share timShareTask3Jodie = createShare(timTask3, jodie);
		Share timShareTask1Sabith = createShare(timTask1, sabith);

		List<Contact> latestShares = contactService
				.getPopularContactsForUserTX(tim.getMember().getEmail(), 0);

		Assert.assertTrue(latestShares.contains(contactService
				.getContactByContactIdTX(jodie.getId())));
		Assert.assertTrue(latestShares.contains(contactService
				.getContactByContactIdTX(zak.getId())));
		Assert.assertTrue(latestShares.contains(contactService
				.getContactByContactIdTX(luv.getId())));
		Assert.assertTrue(latestShares.contains(contactService
				.getContactByContactIdTX(anish.getId())));
		Assert.assertTrue(latestShares.contains(contactService
				.getContactByContactIdTX(sabith.getId())));
	}

}
