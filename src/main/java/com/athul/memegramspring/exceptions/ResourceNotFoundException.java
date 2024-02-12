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
    String errorCode;


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

    //with error codes
    public ResourceNotFoundException(String resourceName,
                                     String fieldName,
                                     long fieldValue,String errorCode){
        super(String.format("%s not found with %s : %d -[%s]",resourceName,fieldName,fieldValue,errorCode));
        this.resourceName=resourceName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        this.errorCode = errorCode;
    }

    public ResourceNotFoundException(String resourceName,
                                     String fieldName,
                                     String username,String errorCode){
        super(String.format("%s not found with %s : %s -[%s]",resourceName,fieldName,username,errorCode));
        this.resourceName=resourceName;
        this.fieldName=fieldName;
        this.username=username;
        this.errorCode = errorCode;
    }
}
