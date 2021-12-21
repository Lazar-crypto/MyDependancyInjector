package com.razal.ioc.config;

import com.razal.ioc.config.configurations.CustomAnnotationsConfiguration;
import com.razal.ioc.config.configurations.InstanceConfiguration;

public class MyConfiguration {

    private final CustomAnnotationsConfiguration annotations;
    private final InstanceConfiguration instances;

    public MyConfiguration() {
        this.annotations = new CustomAnnotationsConfiguration(this);
        this.instances = new InstanceConfiguration(this);
    }

    public CustomAnnotationsConfiguration getAnnotations(){
        return  this.annotations;
    }

    public InstanceConfiguration getInstances() {
        return instances;
    }

    public MyConfiguration build(){
        return this;
    }

}
