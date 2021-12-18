package com.razal.ioc.services;

import com.razal.ioc.models.Directory;

public interface DirectoryResolver {

    Directory resolveDirectory(Class<?> startUpClass);

}
