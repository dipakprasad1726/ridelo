package com.ridelo.management.globalException;

public class InvalidDriverDataException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidDriverDataException(String errorMessage){
        super(errorMessage);
    }
}
