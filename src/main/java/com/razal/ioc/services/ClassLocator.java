package com.razal.ioc.services;

import com.razal.ioc.exceptions.ClassLocationException;

import java.util.Set;

public interface ClassLocator {

    //vrati nazive svih klasa u programu
    Set<Class<?>> locateClasses(String directory) throws ClassLocationException;

}
