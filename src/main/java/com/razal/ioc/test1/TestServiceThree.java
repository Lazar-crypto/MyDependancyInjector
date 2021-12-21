package com.razal.ioc.test1;

import com.razal.ioc.annotations.MyBean;
import com.razal.ioc.annotations.MyService;

@MyService
public class TestServiceThree {


    @MyBean
    public OtherTestClass otherTestClass(){
        return new OtherTestClass();
    }

}
