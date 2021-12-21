package com.razal.ioc.services;

import com.razal.ioc.exceptions.InstanceException;
import com.razal.ioc.models.ServiceDetails;

import java.util.List;
import java.util.Set;

public interface ServicesInstantiation {

    //prosledi mapirane klase i vrati ih instancirane
    List<ServiceDetails<?>> instantiateServicesAndBeans(Set<ServiceDetails<?>> mappedServices) throws InstanceException;

}
