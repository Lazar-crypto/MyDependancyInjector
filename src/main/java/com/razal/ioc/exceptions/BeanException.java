package com.razal.ioc.exceptions;

public class BeanException extends InstanceException{
    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
