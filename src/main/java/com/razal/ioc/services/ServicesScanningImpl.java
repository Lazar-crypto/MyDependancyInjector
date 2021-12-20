package com.razal.ioc.services;

import com.razal.ioc.annotations.*;
import com.razal.ioc.config.configurations.CustomAnnotationsConfiguration;
import com.razal.ioc.models.ServiceDetails;
import com.razal.ioc.utils.ServiceDetailsConstructComparator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ServicesScanningImpl implements ServicesScanning {

    private final CustomAnnotationsConfiguration configuration;

    public ServicesScanningImpl(CustomAnnotationsConfiguration configuration) {
        this.configuration = configuration;
        this.configuration.getCustomServiceAnnotations().add(MyService.class);
        this.configuration.getCustomBeanAnnotations().add(MyBean.class);
    }

    @Override
    public Set<ServiceDetails<?>> mapServices(Set<Class<?>> locatedClasses) {

        final Set<ServiceDetails<?>> serviceDetailsStorage = new HashSet<>();

        //uzmi sve anotacije koje je klijent napravio i MyService koja je default
        final Set<Class<? extends Annotation>> customServiceAnnotations = configuration.getCustomServiceAnnotations();

        //prodji kroz locirane klase i napravi objekat tipa ServiceDetails
        for (Class<?> cls : locatedClasses) {
            if(cls.isInterface())
                continue;

            //prodji kroz sve anotacije date klase i ukoliko je neka od mojih servis anotacija napravi ServiceDetails
            for (Annotation annotation : cls.getAnnotations()) {
                if(customServiceAnnotations.contains(annotation.annotationType())){
                    ServiceDetails<?> serviceDetails = new ServiceDetails(
                            cls,
                            annotation,
                            this.getConstructor(cls),
                            this.getConstructMethod(MyPostConstruct.class, cls),
                            this.getConstructMethod(MyPreDestroy.class, cls),
                            this.getBeans(cls)
                    );
                    serviceDetailsStorage.add(serviceDetails);
                    break;
                }
            }
        }

        return serviceDetailsStorage.stream()
                .sorted(new ServiceDetailsConstructComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }



    private Constructor<?> getConstructor(Class<?> cls){
        //ukoliko je konstruktor mapiran sa MyAutowired vrati ga u suprotnom vrati public konstruktor
        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            if(constructor.isAnnotationPresent(MyAutowired.class)){
                constructor.setAccessible(true);
                return constructor;
            }
        }
        return cls.getConstructors()[0];
    }

    //post i pre construct metode nemaju parametre i uvek su void
    private Method getConstructMethod(Class<? extends Annotation> annotation, Class<?> cls){
        for (Method method : cls.getDeclaredMethods()) {
            if(method.getParameterCount() != 0
                    || (method.getReturnType() != void.class && method.getReturnType() != Void.class)
                    || !method.isAnnotationPresent(annotation))
                continue;

            method.setAccessible(true);
            return method;
        }
        return null;
    }

    private Method[] getBeans(Class<?> cls) {
        final Set<Class<? extends Annotation>> beanAnnotations = this.configuration.getCustomBeanAnnotations();
        final Set<Method> beanMethods = new HashSet<>();

        for (Method method : cls.getDeclaredMethods()) {
            if(method.getParameterCount() != 0
                    || method.getReturnType() == void.class
                    || method.getReturnType() == Void.class)
                continue;

            for (Class<? extends Annotation> beanAnnotation : beanAnnotations) {
                if(method.isAnnotationPresent(beanAnnotation)){
                    method.setAccessible(true);
                    beanMethods.add(method);
                    break;
                }
            }

        }
        return beanMethods.toArray(Method[]::new);
    }


}


