package com.razal.ioc.models;

public class EnqueuedServiceDetails {
    //treba mi:
    //mapirana klasa koju hocu da instanciram
     private final ServiceDetails<?> serviceDetails;
    //dependancies te klase
     private final Class<?>[] dependancies;
    //dependancies koje cu da instanciram
     private final Object[] dependancyInstances;

    public EnqueuedServiceDetails(ServiceDetails<?> serviceDetails) {
        this.serviceDetails = serviceDetails;
        this.dependancies = serviceDetails.getTargetConstructor().getParameterTypes();
        this.dependancyInstances = new Object[this.dependancies.length];
    }

    public ServiceDetails<?> getServiceDetails() {
        return serviceDetails;
    }

    public Class<?>[] getDependancies() {
        return dependancies;
    }

    public Object[] getDependancyInstances() {
        return dependancyInstances;
    }

    public void addDependancyInstance(Object instance){
        //ukoliko ima tip koji treba da se instancira ubaci ga u instancirane dependancies
        for (int i = 0; i < this.dependancies.length; i++) {
            if(this.dependancies[i].isAssignableFrom(instance.getClass())){
                this.dependancyInstances[i] = instance;
                return;
            }
        }

    }

    //da li su sve dependance instancirane
    public boolean isResolved(){
        for (Object dependancyInstance : this.dependancyInstances) {
            if(dependancyInstance == null)
                return false;
        }
        return true;
    }
    //da li serviceDetailsu treba odredjeni dependancy u konstruktoru
    public boolean isDependancyRequired(Class<?> dependancyType){
        for (Class<?> dependancy : this.dependancies) {
            if(dependancy.isAssignableFrom(dependancyType))
                return true;
        }
        return false;
    }
}
