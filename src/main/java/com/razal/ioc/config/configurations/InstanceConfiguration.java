package com.razal.ioc.config.configurations;

import com.razal.ioc.config.BaseSubConfiguration;
import com.razal.ioc.config.MyConfiguration;
import com.razal.ioc.constants.Constants;

public class InstanceConfiguration extends BaseSubConfiguration {

    private int maxAllowedIterations;

    public InstanceConfiguration(MyConfiguration parentConfig) {
        super(parentConfig);
        this.maxAllowedIterations = Constants.MAX_NUMBER_OF_INSTANTIATION_ITERATIONS;
    }

    public InstanceConfiguration setMaximumNumberOfAllowedIterations(int num){
        this.maxAllowedIterations = num;
        return  this;
    }

    public int getMaxAllowedIterations() {
        return maxAllowedIterations;
    }
}
