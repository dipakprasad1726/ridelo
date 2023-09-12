package com.ridelo.management.common;

public class ExceptionConstants {
    private ExceptionConstants(){}

    public static final String INACTIVE_DRIVER_ERROR = "Driver is not active anymore. " +
            "Please reach out to system admin to set active again.";
    public static final String DOCUMENT_VARIFICATION_EXCEPTION = "Driver document verification is not complete yet, " +
            "you need to wait for the verification to complete before setting yourself active.";
//    public static final String
    public static final String NAME_CANNOT_BE_EMPTY = "Driver name cannot be empty.";
    public static final String EMAIL_CANNOT_BE_EMPTY = "Driver email cannot be empty.";
    public static final String ADDRESS_CANNOT_BE_EMPTY = "Driver address cannot be empty.";
    public static final String EMAIL_ALREADY_EXIST = "Driver email is already registered in our system.";
    public static final String CONTACT_CANNOT_BE_EMPTY = "Driver contact cannot be empty.";
}
