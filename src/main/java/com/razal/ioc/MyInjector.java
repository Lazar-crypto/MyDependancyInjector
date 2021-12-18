package com.razal.ioc;

import com.razal.ioc.config.MyConfiguration;
import com.razal.ioc.enums.DirectoryType;
import com.razal.ioc.models.Directory;
import com.razal.ioc.models.ServiceDetails;
import com.razal.ioc.services.*;

import java.util.Set;

public class MyInjector {

    public static void main(String[] args) {
        run(MyInjector.class);
    }

    public static void run(Class<?> startupClass){
        run(startupClass,new MyConfiguration());
    }

    public static void run(Class<?> startupClass, MyConfiguration myConfiguration){


        System.out.println("Dir is:");
        Directory directory = new DirectoryResolverImpl().resolveDirectory(startupClass);
        System.out.println(directory.getDirectoryType());

        ClassLocator classLocator = new ClassLocatorDir();
        if(directory.getDirectoryType() == DirectoryType.JAR_FILE)
            classLocator = new ClassLocatorJar();

        Set<Class<?>> locatedClasses = classLocator.locateClasses(directory.getDirectory());

        ServicesScanning servicesScanning = new ServicesScanningImpl(myConfiguration.getAnnotations());
        Set<ServiceDetails<?>> serviceDetails = servicesScanning.mapServices(locatedClasses);
        System.out.println(serviceDetails);
    }
}
