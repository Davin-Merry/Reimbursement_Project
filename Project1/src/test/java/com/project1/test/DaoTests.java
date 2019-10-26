package com.project1.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.project1.dao.ReimbursementDao;
import com.project1.model.Reimbursement;

/*
 * Dao Tests
 * 
 * The following tests ensure that specific Dao methods are performing the correct actions.
 * 
 * Daos being tested:
 * 		- ReimbursementDao.java
 * 		- UserDao.java
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaoTests {
	@Test
	public void AA_GetListOfReimbursements() {
		ReimbursementDao rd = new ReimbursementDao();
		List<Reimbursement> l = rd.getList();
		assertNotNull(l);
	}
}
