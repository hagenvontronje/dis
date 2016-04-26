package org.dis.sheet02.services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.dis.sheet02.Constants;
import org.dis.sheet02.entities.EstateAgent;
import org.junit.Test;

public class UserManagementServiceTest {

	@Test
	public void testLock() {
		UserManagementService service = new UserManagementService();
		assertTrue(service.isLocked());
		
		try {
			service.getAgents();
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (ServiceLockedException e) {
			// Expected exception!
			assertTrue(true);
		}

		try {
			service.saveAgent(new EstateAgent());;
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (ServiceLockedException e) {
			// Expected exception!
			assertTrue(true);
		}

		try {
			service.delete(new EstateAgent());
		} catch (SQLException e) {
			fail(e.getMessage());
		} catch (ServiceLockedException e) {
			// Expected exception!
			assertTrue(true);
		}
		
		service.unlock(Constants.ROOT_PASSWORD);
		assertTrue(!service.isLocked());
		

		try {
			EstateAgent agent = new EstateAgent("test", "test address", "test", "testpw");
			service.saveAgent(agent);
			service.delete(agent);
			service.getAgents();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
