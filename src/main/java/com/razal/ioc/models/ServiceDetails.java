package com.razal.ioc.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//model za cuvanje mapiranih klasa
public class ServiceDetails<T> {

    //naziv
    private Class<T> serviceType;

    //anotacija sa kojom cu da mapiram klasu
    private Annotation annotation;

    private Constructor<T> targetConstructor;

    private T instance;

    private Method postConstrucMethod;
    private Method preDestroyMethod;
    private Method[] beans;

    //servisi koji koriste this servis
    private final List<ServiceDetails<?>> dependantServices;

    public ServiceDetails() {
        this.dependantServices = new ArrayList<>();
    }

    public ServiceDetails(Class<T> serviceType,
                          Annotation annotation, Constructor<T> targetConstructor,
                          Method postConstrucMethod, Method preDestroyMethod,
                          Method[] beans){

        this();
        this.setServiceType(serviceType);
        this.setAnnotation(annotation);
        this.setTargetConstructor(targetConstructor);
        this.setPostConstrucMethod(postConstrucMethod);
        this.setPreDestroyMethod(preDestroyMethod);
        this.setBeans(beans);

    }


    public Class<T> getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class<T> serviceType) {
        this.serviceType = serviceType;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Constructor<T> getTargetConstructor() {
        return targetConstructor;
    }

    public void setTargetConstructor(Constructor<T> targetConstructor) {
        this.targetConstructor = targetConstructor;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public Method getPostConstrucMethod() {
        return postConstrucMethod;
    }

    public void setPostConstrucMethod(Method postConstrucMethod) {
        this.postConstrucMethod = postConstrucMethod;
    }

    public Method getPreDestroyMethod() {
        return preDestroyMethod;
    }

    public void setPreDestroyMethod(Method preDestroyMethod) {
        this.preDestroyMethod = preDestroyMethod;
    }

    public Method[] getBeans() {
        return beans;
    }

    public void setBeans(Method[] beans) {
        this.beans = beans;
    }

    public List<ServiceDetails<?>> getDependantServices() {
        return Collections.unmodifiableList(this.dependantServices);
    }

    public void addDependantService(ServiceDetails<?> serviceDetails){
        this.dependantServices.add(serviceDetails);
    }

    @Override
    public int hashCode(){
        if(this.serviceType == null){
            return super.hashCode();
        }
        return this.serviceType.hashCode();
    }

    @Override
    public String toString() {
        return this.getServiceType().getName();
    }
}
