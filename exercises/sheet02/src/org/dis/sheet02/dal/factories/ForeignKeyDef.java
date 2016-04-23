package org.dis.sheet02.dal.factories;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class ForeignKeyDef {

	private final boolean isOptional;
	private final EntityInfo<?> targetEntity;
	private final Column sourceAnnotation;

	@SuppressWarnings("unchecked")
	public ForeignKeyDef(Column column, ManyToOne annotation) {
		targetEntity = new EntityInfo<>(annotation.targetEntity());
		isOptional = annotation.optional();
		sourceAnnotation = column;
	}
	
	public String getTargetTable() {
		return targetEntity.getTableName();
	}
	
	public String getTargetColumn() {
		return targetEntity.getIdColumn().getColumnName();
	}
	
	public String getSourceColumn() {
		return sourceAnnotation.name();
	}
	
	public boolean isOptional() {
		return isOptional;
	}

	public Class<?> getTargetType() {
		return targetEntity.getEntityType();
	}
}
