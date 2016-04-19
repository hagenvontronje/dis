package org.dis.sheet02.dal;


public class QueryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723608407085767263L;
	
	private final Exception inner;

	public QueryException(Exception e) {
		this.inner = e;
	}

	public QueryException(String message) {
		super(message);
		inner = null;
	}

	@Override
	public String getMessage() {
		return String.format("An error ocurred during execution: %s", 
							 inner != null ? inner.getMessage() : super.getMessage());
	}
}
