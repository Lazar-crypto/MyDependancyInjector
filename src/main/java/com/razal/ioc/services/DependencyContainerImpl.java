package com.razal.ioc.services;

import com.razal.ioc.exceptions.AlreadyInitException;
import com.razal.ioc.models.ServiceBeanDetails;
import com.razal.ioc.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyContainerImpl implements DependencyContainer{

    private static final String ALREADY_INITIALIZED = "Dependency container already initialized";
    private boolean isInit;
    private List<ServiceDetails<?>> servicesAndBeans;
    private ObjectInstantiation objectInstantiation;

    public DependencyContainerImpl() {
        this.isInit = false;
    }

    @Override
    public void init(List<ServiceDetails<?>> servicesAndBeans, ObjectInstantiation objectInstantiation) throws AlreadyInitException {
        if(this.isInit) throw  new AlreadyInitException(ALREADY_INITIALIZED);

        this.isInit = true;
        this.servicesAndBeans = servicesAndBeans;
        this.objectInstantiation = objectInstantiation;
    }

    @Override
    public <T> void reload(ServiceDetails<T> serviceDetails, boolean reloadDependantServices) {
        this.objectInstantiation.destroyInstance(serviceDetails);
        this.handleReload(serviceDetails);

        if(reloadDependantServices){
            for (ServiceDetails<?> dependantService : serviceDetails.getDependantServices()) {
                this.reload(dependantService,reloadDependantServices);
            }

        }
    }
    private void handleReload(ServiceDetails<?> serviceDetails){
        if(serviceDetails instanceof ServiceBeanDetails)
            this.objectInstantiation.createBeanInstance((ServiceBeanDetails<?>) serviceDetails);
        else{
            this.objectInstantiation.createInstance(serviceDetails,this.collectDependencies(serviceDetails));
        }
    }
    private Object[] collectDependencies(ServiceDetails<?> serviceDetails){
        Class<?>[] parametarTypes = serviceDetails.getTargetConstructor().getParameterTypes();
        Object[] dependantInstances = new Object[parametarTypes.length];
        for (int i = 0; i <parametarTypes.length ; i++) {
            dependantInstances[i] = this.getService(parametarTypes[i]);
        }
        return dependantInstances;
    }

    @Override
    public <T> T reload(T service) {
        return this.reload(service,false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T reload(T service, boolean reloadDependantServices) {
        ServiceDetails<T> serviceDetails = (ServiceDetails<T>) this.getServiceDetails(service.getClass());

        if(serviceDetails == null)
            return null;
        this.reload(serviceDetails,reloadDependantServices);
        return serviceDetails.getInstance();
    }

    @Override
    public <T> T getService(Class<T> className) {
        ServiceDetails<T> serviceDetails = this.getServiceDetails(className);
        if(serviceDetails != null)
            return serviceDetails.getInstance();
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ServiceDetails<T> getServiceDetails(Class<T> className) {
        return (ServiceDetails<T>) this.servicesAndBeans
                .stream()
                .filter(serviceDetails -> className.isAssignableFrom(serviceDetails.getClassName()))
                .findFirst().orElse(null);
    }

    @Override
    public List<ServiceDetails<?>> getServiceByAnnotation(Class<? extends Annotation> annotationType) {
        return this.servicesAndBeans
                .stream()
                .filter(serviceDetails -> serviceDetails.getAnnotation().annotationType() == annotationType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> getAllServices() {
        return this.servicesAndBeans
                .stream()
                .map(ServiceDetails::getInstance)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDetails<?>> getAllServiceDetails() {
        return Collections.unmodifiableList(this.servicesAndBeans);
    }
}
