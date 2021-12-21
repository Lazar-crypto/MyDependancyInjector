package com.razal.ioc.exceptions;

public class PostConstructException extends InstanceException{
    public PostConstructException(String message) {
        super(message);
    }

    public PostConstructException(String message, Throwable cause) {
        super(message, cause);
    }
}
