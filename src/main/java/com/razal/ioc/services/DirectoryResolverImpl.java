package com.razal.ioc.services;

import com.razal.ioc.enums.DirectoryType;
import com.razal.ioc.models.Directory;

import java.io.File;

public class DirectoryResolverImpl implements DirectoryResolver{

    private static final String JAR_FILE_EXTENSION = ".jar";

    @Override
    public Directory resolveDirectory(Class<?> startUpClass) {
        final String directory = this.getDirectory(startUpClass);
        return new Directory(directory,this.getDirectoryType(directory));
    }

    private String getDirectory(Class<?> cls){
        //returns root directory of my class (from target)
        return cls.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    private DirectoryType getDirectoryType(String directory){
        //ukoliko je jar vrati jar u suprotnom dir
        File file = new File(directory);
        if(!file.isDirectory() && directory.endsWith(JAR_FILE_EXTENSION)){
            return DirectoryType.JAR_FILE;
        }
        return DirectoryType.DIRECTORY;
    }
}
