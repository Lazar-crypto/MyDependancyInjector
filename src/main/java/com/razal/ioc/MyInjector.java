package com.razal.ioc;

import com.razal.ioc.annotations.StartUp;
import com.razal.ioc.config.MyConfiguration;
import com.razal.ioc.enums.DirectoryType;
import com.razal.ioc.models.Directory;
import com.razal.ioc.models.ServiceDetails;
import com.razal.ioc.services.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class MyInjector {

    public static final DependencyContainer DEPENDENCY_CONTAINER;
    static {
        DEPENDENCY_CONTAINER = new DependencyContainerImpl();
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

        Set<Class<?>> scannedClasses = classLocator.locateClasses(directory.getDirectory());

        //get all scanned classes
        ServicesScanning servicesScanning = new ServicesScanningImpl(myConfiguration.getAnnotations());

        //get all mapped annotated classes
        Set<ServiceDetails<?>> mappedClasses = servicesScanning.mapServices(scannedClasses);
        System.out.println(mappedClasses);

        //instantiate all mapped classes(services and beans)
        ObjectInstantiation objectInstantiation = new ObjectInstantiationImpl();
        ServicesInstantiation servicesInstantiation = new ServicesInstantiationImpl(myConfiguration.getInstances(),objectInstantiation);
        List<ServiceDetails<?>> instantiatedClasses = servicesInstantiation.instantiateServicesAndBeans(mappedClasses);

        //run from my container
        DEPENDENCY_CONTAINER.init(instantiatedClasses,objectInstantiation);
        runStartUpMethod(startupClass);

    }
    //
    private static void runStartUpMethod(Class<?> startUpClass){
        ServiceDetails<?> serviceDetails = DEPENDENCY_CONTAINER.getServiceDetails(startUpClass);

        for (Method declaredMethod : serviceDetails.getClassName().getDeclaredMethods()) {
            if(declaredMethod.getParameterCount() != 0
                || (declaredMethod.getReturnType() != void.class && declaredMethod.getReturnType() != Void.class)
                || !declaredMethod.isAnnotationPresent(StartUp.class))
                continue;
            declaredMethod.setAccessible(true);
            try {
                declaredMethod.invoke(serviceDetails.getInstance());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw  new RuntimeException(e);
            }
            return;
        }
    }


}
