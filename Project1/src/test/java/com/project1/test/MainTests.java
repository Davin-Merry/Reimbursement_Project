package com.project1.test;

import org.junit.*;
import org.junit.runners.MethodSorters;

import org.mockito.*;


/**
 * Contains all core tests used within Project 1. Most class files are tested here.
 * 
 * The following files are not included within this JUnit test:
 * 		- Reimbursement.java
 * 		- User.java
 * These model files don't necessarily need to be tested.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTests {
	
	/*
	 * Dao Tests
	 * 
	 * The following tests ensure that specific Dao methods are performing the correct actions.
	 * 
	 * Daos being tested:
	 * 		- ReimbursementDao.java
	 * 		- UserDao.java
	 */
	
	
	
	/*
	 * Service Tests
	 * 
	 * The following tests ensure that specific methods are performing the correct actions.
	 * Most services don't really perform very many tasks, making them a bit simplistic.
	 * 
	 * Services being tested:
	 * 		- ReimbursementService.java
	 * 		- UserService.java
	 */
	@Test
	public void AA_() {
		
	}
	
	/*
	 * Controller Tests
	 * 
	 * The following tests ensure that the proper strings are being passed during redirects/JSON requests.
	 * Controllers being tested:
	 * 		- EmployeeController.java
	 * 		- LoginController.java
	 * 		- ManagerController.java
	 */
	
	/*
	 * Handler Tests
	 * 
	 * The following tests ensure that the requests passed on by the servlets are handled accordingly.
	 * Handlers being tested:
	 * 		- RequestHandler.java
	 * 		- SessionHandler.java
	 */
}
