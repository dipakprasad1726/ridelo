package com.ridelo.management.globalException;

import java.util.UUID;

public class InvalidDriverIdException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidDriverIdException(UUID id){
        super("Invalid Driver Id "+id);
    }
}
