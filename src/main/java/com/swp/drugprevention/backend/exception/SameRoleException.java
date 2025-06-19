package com.swp.drugprevention.backend.exception;

public class SameRoleException extends RuntimeException{
    public SameRoleException(String message) {
        super(message);
    }

}
