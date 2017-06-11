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
		String[] argsForTest = {"resources" + File.separator + "users.csv", "resources" + File.separator + "files.csv"};
		
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
	public void testUserListSize() {
		Map<Long, String> userList = runnerTest.getUserList();
		assertTrue(userList.size() == 2);
	}

}
