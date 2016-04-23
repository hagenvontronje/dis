package org.dis.sheet02.dal;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Id;

public class ColumnDef {
	private final Field field;
	private final Column columnAnnotation;
	private final boolean isId;

	public ColumnDef(Field field) {
		this.field = field;
		this.columnAnnotation = getColumnAnnotation(field);
		this.isId = isIdentity(field);
	}

	private boolean isIdentity(Field field) {
		return field.getAnnotation(Id.class) != null;
	}

	private Column getColumnAnnotation(Field field) {
		return field.getAnnotation(Column.class);
	}

	public Field getField() {
		return field;
	}

	public Column getColumnAnnotation() {
		return columnAnnotation;
	}

	public boolean isId() {
		return isId;
	}

	public String getColumnName() {
		return columnAnnotation.name();
	}
}