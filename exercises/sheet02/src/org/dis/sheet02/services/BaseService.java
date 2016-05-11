package org.dis.sheet02.services;

import org.dis.sheet02.dal.ContextBuilder;
import org.dis.sheet02.dal.RealEstateContext;

public class BaseService {

	/** The database context to use. */
	protected RealEstateContext dbcontext = null;

	public BaseService() {
		super();
	}

	/**
	 * Creates a new database context, if it does not yet exist.
	 */
	protected void ensureContextIsCreated() {
		if (dbcontext == null)
			dbcontext = ContextBuilder.build();
	}

}