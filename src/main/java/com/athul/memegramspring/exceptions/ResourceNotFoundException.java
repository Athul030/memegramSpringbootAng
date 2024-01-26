package com.athul.memegramspring.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String fieldName;
    long fieldValue;
    String username;

    public ResourceNotFoundException(String resourceName,
                                     String fieldName,
                                     long fieldValue){
        super(String.format("%s not found with %s : %d",resourceName,fieldName,fieldValue));
        this.resourceName=resourceName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
    }

    public ResourceNotFoundException(String resourceName,
                                     String fieldName,
                                     String username){
        super(String.format("%s not found with %s : %s",resourceName,fieldName,username));
        this.resourceName=resourceName;
        this.fieldName=fieldName;
        this.username=username;
    }
}
