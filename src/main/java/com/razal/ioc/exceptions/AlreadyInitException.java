package com.razal.ioc.exceptions;

public class AlreadyInitException extends RuntimeException{
    public AlreadyInitException(String message) {
        super(message);
    }

    public AlreadyInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
