package org.dis.sheet02.services;

import java.sql.SQLException;
import java.util.List;

import org.dis.sheet02.Constants;
import org.dis.sheet02.entities.EstateAgent;

/**
 * Service to use for accessing user (estate agent) management functions.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class UserManagementService extends BaseService {

	/** if <code>true</code> all management methods are locked. */
	private boolean isLocked = true;

	/**
	 * Retrieves all estate agents from the database.
	 * 
	 * @return A list of all estate agents.
	 * @throws SQLException
	 *             If an error occurs during the operation.
	 * @throws ServiceLockedException
	 *             If the service is locked.
	 */
	public List<EstateAgent> getAgents()
			throws SQLException, ServiceLockedException {
		throwIfLocked();
		ensureContextIsCreated();
		return dbcontext.getAgents().getAll();
	}

	private void throwIfLocked() throws ServiceLockedException {
		if (isLocked)
			throw new ServiceLockedException();
	}

	/**
	 * Deletes an estate agent from the database.
	 * 
	 * @param agent
	 *            The agent to delete.
	 * @throws SQLException
	 *             If an error occurs during the operation.
	 * @throws ServiceLockedException
	 *             If the service is locked.
	 */
	public void delete(EstateAgent agent)
			throws SQLException, ServiceLockedException {
		throwIfLocked();
		ensureContextIsCreated();
		dbcontext.getAgents().delete(agent);
	}

	/**
	 * Persists an estate agent in the database.
	 * 
	 * @param agent
	 *            The agent to save or update.
	 * @throws SQLException
	 *             If an error occurs during the operation.
	 * @throws ServiceLockedException
	 *             If the service is locked.
	 * @return An updated copy of the agent.
	 */
	public EstateAgent saveAgent(EstateAgent agent)
			throws SQLException, ServiceLockedException {
		throwIfLocked();
		ensureContextIsCreated();
		agent = dbcontext.getAgents().save(agent);
		return agent;
	}

	/**
	 * Unlocks the user management functions, if the password matches
	 * {@link Constants#ROOT_PASSWORD}.
	 * 
	 * @param password
	 *            The password to unlock the service.
	 * @return <code>true</code>, if the service was unlocked,
	 *         <code>false</code> otherwise.
	 */
	public boolean unlock(String password) {
		isLocked &= !Constants.ROOT_PASSWORD.equals(password);
		return isLocked;
	}

	/**
	 * Gets if the service is locked, i.e. no user management function are
	 * possible.
	 * 
	 * @return <code>true</code>, if the service is locked, <code>false</code>
	 *         otherwise.
	 */
	public boolean isLocked() {
		return isLocked;
	}
}
