package com.ridelo.management.globalException;

import com.ridelo.management.model.ExceptionResponse;

public class InvalidRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private ExceptionResponse exceptionResponse;

    public InvalidRequestException(String message){
        super(message);
    }
}
