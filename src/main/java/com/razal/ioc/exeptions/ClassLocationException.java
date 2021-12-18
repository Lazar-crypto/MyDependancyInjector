package com.razal.ioc.exeptions;

public class ClassLocationException extends RuntimeException {

    public ClassLocationException(String message) {
        super(message);
    }

    public ClassLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
