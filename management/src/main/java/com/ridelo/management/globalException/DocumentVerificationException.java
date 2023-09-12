package com.ridelo.management.globalException;

import com.ridelo.management.common.ExceptionConstants;

public class DocumentVerificationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DocumentVerificationException(){
        super(ExceptionConstants.DOCUMENT_VARIFICATION_EXCEPTION);
    }
}
