package org.dis.sheet02.services;

import java.sql.SQLException;
import java.util.List;

import org.dis.sheet02.Constants;
import org.dis.sheet02.entities.EstateAgent;

public class LoginService extends BaseService {

	public static EstateAgent User;

	public boolean login(String username, String password) throws SQLException {
		ensureContextIsCreated();
		if (username == null  || password == null)
			return false;
		if (Constants.ROOT_USER.equals(username)) {
			if (Constants.ROOT_PASSWORD.equals(password)) {
				User = new EstateAgent();
				return true;
			}
			return false;
		} 
		else {
			List<EstateAgent> agents = dbcontext.getAgents().getAll();
			for (EstateAgent agent : agents) {
				if (username.equals(agent.getLogin()) 
						&& password.equals(agent.getPassword())) {
					User = agent;
					return true;
				}
			}
			return false;
		}
	}
}
