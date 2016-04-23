package org.dis.sheet02.dal.factories;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class ColumnDef {
	private final Field field;
	private final Column columnAnnotation;
	private final boolean isId;
	private final ForeignKeyDef foreignKeyDef;
	
	public ColumnDef(Field field) {
		this.field = field;
		this.columnAnnotation = getColumnAnnotation(field);
		this.isId = isIdentity(field);
		foreignKeyDef = field.getAnnotation(ManyToOne.class) != null 
				? new ForeignKeyDef(
						columnAnnotation,
						field.getAnnotation(ManyToOne.class))
				: null;
	}

	private static boolean isIdentity(Field field) {
		return field.getAnnotation(Id.class) != null;
	}

	private static Column getColumnAnnotation(Field field) {
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

	public boolean isForeignkey() {
		return getForeignKeyDefinition() != null;
	}

	public ForeignKeyDef getForeignKeyDefinition() {
		return foreignKeyDef;
	}

	public boolean isNullable() {
		return isForeignkey() 
				? foreignKeyDef.isOptional() 
				: columnAnnotation.nullable() && !isId;
	}
	
	public int getPrecision() {
		return columnAnnotation.precision();
	}
	
	public int getScale() {
		return columnAnnotation.scale();
	}
	
	public int getLength() {
		return columnAnnotation.length();
	}
}