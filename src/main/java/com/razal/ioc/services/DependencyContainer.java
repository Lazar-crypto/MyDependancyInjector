package com.razal.ioc.services;

import com.razal.ioc.exceptions.AlreadyInitException;
import com.razal.ioc.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.util.List;

public interface DependencyContainer {

    void init(List<ServiceDetails<?>> servicesAndBeans,ObjectInstantiation objectInstantiation) throws AlreadyInitException;

    <T> void reload(ServiceDetails<T> serviceDetails, boolean reloadDependantServices);

    <T> T reload(T service);

    <T> T reload(T service, boolean reloadDependantServices);

    <T> T getService(Class<T> className);

    <T> ServiceDetails<T> getServiceDetails(Class<T> className);

    List<ServiceDetails<?>> getServiceByAnnotation(Class<? extends Annotation> annotationType);

    List<Object> getAllServices();

    List<ServiceDetails<?>> getAllServiceDetails();
}
