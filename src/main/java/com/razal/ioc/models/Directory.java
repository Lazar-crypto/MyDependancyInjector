package com.razal.ioc.models;

import com.razal.ioc.enums.DirectoryType;

public class Directory {

    //putanja direktorijuma
    private final String directory;
    //da li je string ili jar fajl
    private final DirectoryType directoryType;

    public Directory(String directory, DirectoryType directoryType) {
        this.directory = directory;
        this.directoryType = directoryType;
    }

    public String getDirectory() {
        return directory;
    }

    public DirectoryType getDirectoryType() {
        return directoryType;
    }
}
