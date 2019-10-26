package com.project1.test;

import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.project1.util.ConnectionUtil;

/**
 * Utility Test
 * 
 * The following tests ensure that the Utility classes perform the correct operations.
 * Currently, the only utility file that exists is the ConnectionUtil.java class.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UtilityTests {
	@Test
	public void AA_ConnectionGetInstance() {
		ConnectionUtil cu = null;
		try {
			cu = ConnectionUtil.getInstance();
		} catch (SQLException e) {
			//Do nothing
		}
		
		assertNotEquals(null, cu);
	}
	
	@Test
	public void AB_ConnectionGetConnection() {
		Connection c = null;
		try {
			c = ConnectionUtil.getInstance().getConnection();
		} catch (SQLException e) {
			//Do nothing
		}
		
		assertNotEquals(null, c);
	}
	
	@Test
	public void AC_ConnectionNewConnectionOnClose() {
		ConnectionUtil cu = null;
		Connection c = null;
		try {
			cu = ConnectionUtil.getInstance();
			cu.getConnection().close();
			c = cu.getConnection();
		} catch (SQLException e) {
			//Do nothing
		}
		
		assertNotEquals(null, cu);
		assertNotEquals(null, c);
	}
}
