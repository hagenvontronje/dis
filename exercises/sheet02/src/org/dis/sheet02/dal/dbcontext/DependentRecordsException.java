package org.dis.sheet02.dal.dbcontext;

import java.sql.SQLException;

public class DependentRecordsException extends SQLException {

	public DependentRecordsException(Exception e) {
		super(e);
	}

	/** Generated serial version UID */
	private static final long serialVersionUID = 6483262940522322678L;

}
