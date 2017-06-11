package com.reengen.utils.audireporter.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;

import com.reengen.utils.auditreporter.Runner;

public class RunnerTest {
	Runner runnerTest = new Runner();

	@Before
	public void setUp() throws Exception {
		runnerTest.loadData("resources" + File.separator + "users.csv", "resources" + File.separator + "files.csv");
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testUsernameMethodForCorrectInput() {
			assertTrue("## User: userTest".equals(runnerTest.printUserHeader("userTest")));
	}
	
	@Test
	public void testUsernameMethodForWrongInput() {
			assertFalse("## User: userTest".equals(runnerTest.printUserHeader("userTestFalse")));
			
	}
	
	@Test
	public void testUserInformationForJpublicUser() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertTrue(userList.get(new Long(1)).equals("jpublic"));
	}
	
	@Test
	public void testUserInformationForAtesterUser() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertFalse(userList.get(new Long(2)).equals("jpublic"));
	}
	
	@Test
	public void testUserListSizeForWrongSize() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertFalse(userList.size() == 3);
	}
	
	@Test
	public void testUserListSize() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertTrue(userList.size() == 2);
	}
	
	@Test
	public void testUserListSizeForWrongSize2() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertFalse(userList.size() == 3);
	}	
}
