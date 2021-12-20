package com.razal.ioc.config;

import com.razal.ioc.config.configurations.CustomAnnotationsConfiguration;

public class MyConfiguration {

    private final CustomAnnotationsConfiguration annotations;

    public MyConfiguration() {
        this.annotations = new CustomAnnotationsConfiguration(this);
    }

    public CustomAnnotationsConfiguration getAnnotations(){
        return  this.annotations;
    }

    public MyConfiguration build(){
        return this;
    }

}
