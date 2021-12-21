package com.razal.ioc.config;

public abstract class BaseSubConfiguration {

    //builder
    private final MyConfiguration myConfig;

    protected BaseSubConfiguration(MyConfiguration myConfig) {
        this.myConfig = myConfig;
    }

    public MyConfiguration getMyConfig(){
        return this.myConfig;
    }

}
