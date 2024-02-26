package com.athul.memegramspring.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseCustomBlockedUser extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
    private String httpStatusCode;

    @Override
    public String toString() {
        return "ApiResponseCustomBlockedUser{" +
                "message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                ", httpStatusCode='" + httpStatusCode + '\'' +
                '}';
    }
}
