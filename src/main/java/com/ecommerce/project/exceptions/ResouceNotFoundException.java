package com.ecommerce.project.exceptions;

public class ResouceNotFoundException extends RuntimeException {

	String resouceName;
	String field;
	String fieldName;
	Long fieldId;
	
	public ResouceNotFoundException() {
		
	}

	public ResouceNotFoundException(String resouceName, String field, String fieldName) {
		super(String.format("%s Not Found With %s: %s", resouceName, field, fieldName));
		this.resouceName = resouceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResouceNotFoundException(String resouceName, String field, Long fieldId) {
		super(String.format("%s Not Found With %s: %d", resouceName, field, fieldId));
		this.resouceName = resouceName;
		this.field = field;
		this.fieldId = fieldId;
	}

}
