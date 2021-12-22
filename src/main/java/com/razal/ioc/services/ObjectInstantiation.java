package com.razal.ioc.services;

import com.razal.ioc.exceptions.BeanException;
import com.razal.ioc.exceptions.InstanceException;
import com.razal.ioc.exceptions.PreDestroyException;
import com.razal.ioc.models.ServiceBeanDetails;
import com.razal.ioc.models.ServiceDetails;

public interface ObjectInstantiation {

    void createInstance(ServiceDetails<?> serviceDetails, Object ... constructorParams) throws InstanceException;

    void createBeanInstance(ServiceBeanDetails<?> serviceBeanDetails) throws BeanException;

    void destroyInstance(ServiceDetails<?> serviceDetails) throws PreDestroyException;

}
