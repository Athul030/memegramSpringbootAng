package com.athul.memegramspring.exceptions;

import jakarta.persistence.Entity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponseCustom apiResponse = new ApiResponseCustom(message,HttpStatus.NOT_FOUND);
        return new ResponseEntity<ApiResponseCustom>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
          Map<String,String> resp = new HashMap<>();
          ex.getBindingResult().getAllErrors().forEach((error)->{
              String fieldName = ((FieldError) error).getField();
              String message = error.getDefaultMessage();
              resp.put(fieldName,message);
          });
          return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentialException(BadCredentialsException ex){
        Map<String,String> resp = new HashMap<>();
        resp.put("message",ex.getMessage());
        return new ResponseEntity<>(resp,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseCustom> handleApiException(ApiException ex){
        String message = ex.getMessage();
        ApiResponseCustom apiResponse = new ApiResponseCustom(message,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ApiResponseCustom>(apiResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
//        return ResponseEntity.badRequest().body("Email already exists");
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ApiResponseCustomBlockedUser.class)
    public ResponseEntity<Object>  handleBlockedUserException(ApiResponseCustomBlockedUser ex){
        return new ResponseEntity<>("You are blocked from accessing Memegram",HttpStatus.LOCKED);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDeniedException(PermissionDeniedException ex){
        return new ResponseEntity<>("You don't have permission to delete this comment",HttpStatus.FORBIDDEN);
    }






}

