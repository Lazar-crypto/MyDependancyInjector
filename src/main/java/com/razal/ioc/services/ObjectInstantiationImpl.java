package com.razal.ioc.services;

import com.razal.ioc.exceptions.BeanException;
import com.razal.ioc.exceptions.InstanceException;
import com.razal.ioc.exceptions.PostConstructException;
import com.razal.ioc.exceptions.PreDestroyException;
import com.razal.ioc.models.ServiceBeanDetails;
import com.razal.ioc.models.ServiceDetails;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//faktori klasa za kreiranje objekata
public class ObjectInstantiationImpl implements ObjectInstantiation {

    private static final String INVALID_PARAMETARS_COUNT = "Invalid params count for '%s'";

    @Override
    public void createInstance(ServiceDetails<?> serviceDetails, Object... constructorParams) throws InstanceException {
        //parametri u konstruktoru moraju biti iste duzine
        Constructor<?> targetConstructor = serviceDetails.getTargetConstructor();
        if(constructorParams.length != targetConstructor.getParameterCount())
            throw new InstanceException(String.format(
                    INVALID_PARAMETARS_COUNT,serviceDetails.getClassName().getName()));

        try {
            Object instance = targetConstructor.newInstance(constructorParams);
            serviceDetails.setInstance(instance);
            this.invokePostConstruct(serviceDetails);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
           throw new InstanceException(e.getMessage(), e);
        }
    }

    private void invokePostConstruct(ServiceDetails<?> serviceDetails) throws PostConstructException{
        if(serviceDetails.getPostConstrucMethod() == null)
            return;
        //pozovi postConstruct
        try {
            serviceDetails.getPostConstrucMethod().invoke(serviceDetails.getInstance());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw  new PostConstructException(e.getMessage(),e);
        }
    }

    @Override
    public void createBeanInstance(ServiceBeanDetails<?> serviceBeanDetails) throws BeanException {
        //koji metod je pozvao pravljenje beana
        Method originMethod = serviceBeanDetails.getOriginMethod();
        //i kojoj klasi pripada taj metod(bean)
        Object rootInstance = serviceBeanDetails.getRootService().getInstance();

        try {
            Object instance = originMethod.invoke(rootInstance);
            serviceBeanDetails.setInstance(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanException(e.getMessage(),e);
        }

    }

    @Override
    public void destroyInstance(ServiceDetails<?> serviceDetails) throws PreDestroyException {
        if(serviceDetails.getPreDestroyMethod() != null){
            try {
                serviceDetails.getPreDestroyMethod().invoke(serviceDetails.getInstance());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PreDestroyException(e.getMessage(), e);
            }
        }
        serviceDetails.setInstance(null);
    }
}
