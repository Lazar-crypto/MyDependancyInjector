package com.razal.ioc.services;

import com.razal.ioc.models.ServiceDetails;

import java.util.Set;

public interface ServicesScanning {

    //prosledi skenirane klase i vrati ih mapirane
    Set<ServiceDetails<?>> mapServices(Set<Class<?>> locatedClasses);

}
