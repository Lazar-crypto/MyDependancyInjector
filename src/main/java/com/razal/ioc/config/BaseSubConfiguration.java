package com.razal.ioc.config;

public abstract class BaseSubConfiguration {

    //builder
    private final MyConfiguration parentConfig;

    protected BaseSubConfiguration(MyConfiguration parentConfig) {
        this.parentConfig = parentConfig;
    }

    public MyConfiguration and(){
        return this.parentConfig;
    }

}
