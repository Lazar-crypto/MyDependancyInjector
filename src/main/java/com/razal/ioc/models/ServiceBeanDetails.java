package com.razal.ioc.models;

import java.lang.reflect.Method;

public class ServiceBeanDetails<T> extends ServiceDetails<T>{

    //metoda koja ce da kreira bean
    private final Method originMethod;
    //klasa iz koje ce da se poziva kreiranje tog beana
    private final ServiceDetails<T> rootService;

    public ServiceBeanDetails(Class<T> beanType,Method originMethod, ServiceDetails<T> rootService) {
        this.setClassName(beanType);
        this.setBeans(new Method[0]);
        this.originMethod = originMethod;
        this.rootService = rootService;
    }

    public Method getOriginMethod() {
        return originMethod;
    }

    public ServiceDetails<T> getRootService() {
        return rootService;
    }
}
