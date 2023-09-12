package com.ridelo.management.globalException;

import com.ridelo.management.common.ExceptionConstants;

public class InactiveDriverException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InactiveDriverException(){
        super(ExceptionConstants.INACTIVE_DRIVER_ERROR);
    }
}
