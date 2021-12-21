package com.razal.ioc.exceptions;

public class PreDestroyException extends InstanceException{
    public PreDestroyException(String message) {
        super(message);
    }

    public PreDestroyException(String message, Throwable cause) {
        super(message, cause);
    }
}
