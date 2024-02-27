package com.athul.memegramspring.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User doesn't have permission to delete this comment")
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String message){
        super(message);
    }

}
