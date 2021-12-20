package com.razal.ioc.config.configurations;

import com.razal.ioc.config.BaseSubConfiguration;
import com.razal.ioc.config.MyConfiguration;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CustomAnnotationsConfiguration extends BaseSubConfiguration {

    //anotacije koje ce biti configureable service i bean
    private final Set<Class<? extends Annotation>> customServiceAnnotations;
    private final Set<Class<? extends Annotation>> customBeanAnnotations;

    public CustomAnnotationsConfiguration(MyConfiguration parentConfig) {
        super(parentConfig);
        this.customServiceAnnotations = new HashSet<>();
        this.customBeanAnnotations = new HashSet<>();
    }

    //metode za builder
    public CustomAnnotationsConfiguration addCustomServiceAnnotation(Class<? extends Annotation> annotation){
        this.customServiceAnnotations.add(annotation);
        return this;
    }

    public CustomAnnotationsConfiguration addCustomServiceAnnotation(Class<? extends Annotation> ... annotations){
        this.customServiceAnnotations.addAll(Arrays.asList(annotations));
        return this;
    }

    public CustomAnnotationsConfiguration addCustomBeanAnnotation(Class<? extends Annotation> annotation){
        this.customServiceAnnotations.add(annotation);
        return this;
    }

    public CustomAnnotationsConfiguration addCustomBeanAnnotation(Class<? extends Annotation> ... annotations){
        this.customServiceAnnotations.addAll(Arrays.asList(annotations));
        return this;
    }

    //geteri
    public Set<Class<? extends Annotation>> getCustomServiceAnnotations() {
        return customServiceAnnotations;
    }

    public Set<Class<? extends Annotation>> getCustomBeanAnnotations() {
        return customBeanAnnotations;
    }
}
