package org.dis.sheet02.dal;

public class ContextBuilder {

	public static RealEstateContext build() {
		return new RealEstateHibernateContext();
	}
}
