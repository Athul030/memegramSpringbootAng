package com.athul.memegramspring.exceptions;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseCustom {

    private String message;
    private HttpStatus httpStatus;

}
