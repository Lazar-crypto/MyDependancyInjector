package com.razal.ioc.services;

import com.razal.ioc.config.configurations.InstanceConfiguration;
import com.razal.ioc.exceptions.InstanceException;
import com.razal.ioc.models.EnqueuedServiceDetails;
import com.razal.ioc.models.ServiceBeanDetails;
import com.razal.ioc.models.ServiceDetails;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ServicesInstantiationImpl implements ServicesInstantiation{

    private static final String MAX_NUM_OF_ALLOWED_ITERATIONS_REACHER = "Maximum number of allowed iterations reached '%s'";
    private static final String CONSTRUCTOR_PARAMETAR_NOT_FOUND = "Could not create instance of '%s'. Constructor parametar '%s' not found";
    private final InstanceConfiguration configuration;
    private final ObjectInstantiation objectinstantiation;

    private final LinkedList<EnqueuedServiceDetails> enqueuedServiceDetails;
    private final List<Class<?>> allAvailableClasses;
    private final List<ServiceDetails<?>> instantiatedServices;

    public ServicesInstantiationImpl(InstanceConfiguration configuration, ObjectInstantiation objectinstantiation) {
        this.configuration = configuration;
        this.objectinstantiation = objectinstantiation;

        this.enqueuedServiceDetails = new LinkedList<>();
        this.allAvailableClasses = new ArrayList<>();
        this.instantiatedServices = new ArrayList<>();

    }

    @Override
    public List<ServiceDetails<?>> instantiateServicesAndBeans(Set<ServiceDetails<?>> mappedServices) throws InstanceException {
        init(mappedServices);
        checkForMissingServices(mappedServices);

        int counter = 0;
        int maxNumOfIterations = this.configuration.getMaxAllowedIterations();

        //nek se vrti dok ne dodje do 100 000(znaci da nije uspeo da instancira sve potrebne klase)
        //ili dok se ne isprazni lista u kojoj su sve servis klase(uspeo)
        while(!this.enqueuedServiceDetails.isEmpty()){
            if(counter > maxNumOfIterations)
                throw  new InstanceException(String.format(MAX_NUM_OF_ALLOWED_ITERATIONS_REACHER,maxNumOfIterations));

            EnqueuedServiceDetails enqueuedServiceDetails = this.enqueuedServiceDetails.removeFirst();
            //ukoliko nije prosledio null instanciraj objekat
            if(enqueuedServiceDetails.isResolved()){
                ServiceDetails<?> serviceDetails = enqueuedServiceDetails.getServiceDetails();
                Object[] dependancyInstances = enqueuedServiceDetails.getDependancyInstances();

                this.objectinstantiation.createInstance(serviceDetails,dependancyInstances);
                this.registerInstanceOfService(serviceDetails);
                //instanciraj beanove u tom objektu
                this.registerBeans(serviceDetails);
            }else {
                this.enqueuedServiceDetails.addLast(enqueuedServiceDetails);
                counter ++;
            }
        }
        return this.instantiatedServices;
    }

    private void registerBeans(ServiceDetails<?> serviceDetails) {
        for (Method beanMethod : serviceDetails.getBeans()) {
            ServiceBeanDetails<?> beanDetails = new ServiceBeanDetails(beanMethod.getReturnType(),beanMethod,serviceDetails);
            this.objectinstantiation.createBeanInstance(beanDetails);
            //i za bean treba da vidim da li mu trebaju dependancies klase
            this.registerInstanceOfService(beanDetails);
        }

    }

    private void registerInstanceOfService(ServiceDetails<?> serviceDetails) {
        if(!(serviceDetails instanceof ServiceBeanDetails)){
            //svaki put kad napravim servis klasu treba da obavestim sve servis klase da su dependancies te novokreirane klase
            updateDependantServices(serviceDetails);
        }
        this.instantiatedServices.add(serviceDetails);

        for (EnqueuedServiceDetails enqueuedService : this.enqueuedServiceDetails) {
            if(enqueuedService.isDependancyRequired(serviceDetails.getClassName()))
                enqueuedService.addDependancyInstance(serviceDetails.getInstance());
        }
    }

    private void updateDependantServices(ServiceDetails<?> newService) {
        for (Class<?> parameterType : newService.getTargetConstructor().getParameterTypes()) {
            for (ServiceDetails<?> instantiatedService : this.instantiatedServices) {
                if(parameterType.isAssignableFrom(instantiatedService.getClassName()))
                    instantiatedService.addDependantService(newService);
            }
        }
    }

    private void checkForMissingServices(Set<ServiceDetails<?>> mappedServices) throws InstanceException{
        for (ServiceDetails<?> mappedService : mappedServices) {
            for (Class<?> parameterType : mappedService.getTargetConstructor().getParameterTypes()) {
                if(!this.isAssignableTypePresent(parameterType))
                    throw new InstanceException(String.format(CONSTRUCTOR_PARAMETAR_NOT_FOUND
                            ,mappedService.getClassName().getName()
                            ,parameterType.getName()));
            }
        }
    }

    private boolean isAssignableTypePresent(Class<?> cls){
        for (Class<?> className : this.allAvailableClasses) {
            if(cls.isAssignableFrom(className))
                return true;
        }
        return false;
    }
    
    public void init(Set<ServiceDetails<?>> mappedServices){
        this.enqueuedServiceDetails.clear();
        this.allAvailableClasses.clear();
        this.instantiatedServices.clear();
        //prodji kroz mapirane klase i instanciraj ih,kao i njihove beanove
        for (ServiceDetails<?> mappedService : mappedServices) {
            //dodaj mapirane klase spremne za instanciranje
            this.enqueuedServiceDetails.add(new EnqueuedServiceDetails(mappedService));
            //dodaj sve sto je mapirano
            this.allAvailableClasses.add(mappedService.getClassName());
            this.allAvailableClasses.addAll(Arrays.stream(mappedService.getBeans())
                    .map(Method::getReturnType)
                    .collect(Collectors.toList()));
        }
    }
}
