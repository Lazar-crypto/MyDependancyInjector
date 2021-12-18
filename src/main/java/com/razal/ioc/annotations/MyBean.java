package com.razal.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //Svi beanovi ce imati anotaciju do vremena izvrsavanja
@Target(ElementType.METHOD) // anotacija ce biti iznad metode
public @interface MyBean {
}
