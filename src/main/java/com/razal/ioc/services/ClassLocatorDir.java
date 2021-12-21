package com.razal.ioc.services;

import com.razal.ioc.constants.Constants;
import com.razal.ioc.exceptions.ClassLocationException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClassLocatorDir implements ClassLocator{

    private static final String INVALID_DIRECTORY_MSG = "Invalid directory '%s'";
    
    private final Set<Class<?>> locatedClasses;

    public ClassLocatorDir() {
        locatedClasses = new HashSet<>();
    }


    @Override
    public Set<Class<?>> locateClasses(String directory) throws ClassLocationException {

        this.locatedClasses.clear();

        File file = new File(directory);

        if(!file.isDirectory())
            throw new ClassLocationException(INVALID_DIRECTORY_MSG);

        //pocni od roota - \classes
        //this.scanDirectory(file,"");

        //pocni od jednog ispod fajla da se krene od \com (prvog paketa)
        try {
            for (File innerFile : file.listFiles()) {
                //System.out.println(file);
                //System.out.println(innerFile);
                this.scanDirectory(innerFile, "");
            }
        }catch (ClassNotFoundException e) {
            throw new ClassLocationException(e.getMessage(),e);
            }

        return this.locatedClasses;
    }

    private void scanDirectory(File file,String packageName) throws ClassNotFoundException {

        if(file.isDirectory()){
            packageName += file.getName() + ".";
            //System.out.println(packageName);
            for (File innerFile : file.listFiles()) {
                this.scanDirectory(innerFile,packageName);
            }
        }
        //dosao sam do fajla
        else{
            if(!file.getName().endsWith(Constants.JAVA_BINARY_EXTENSION))
                return;
            final String className = packageName + file.getName().replace(Constants.JAVA_BINARY_EXTENSION,"");
            //System.out.println(className);
            this.locatedClasses.add(Class.forName(className));
        }

    }

}
