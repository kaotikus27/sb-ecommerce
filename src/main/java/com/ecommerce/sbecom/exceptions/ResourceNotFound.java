package com.ecommerce.sbecom.exceptions;

public class ResourceNotFound extends RuntimeException {

    String resouceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFound() {
    }

    public ResourceNotFound(String resouceName, String field, String fieldName) {
        super();
        this.resouceName = resouceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFound(String resouceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %s"), resouceName, field , fieldId);
        this.resouceName = resouceName;
        this.field = field;
        this.fieldId = fieldId;
    }


}
