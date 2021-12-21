package com.razal.ioc.test1;

import com.razal.ioc.annotations.*;

@MyService
public class TestServiceTwo {

    private final TestService serviceOne;
    //private final OtherService otherService;

    @MyAutowired
    public TestServiceTwo(TestService serviceOne) {
        this.serviceOne = serviceOne;
       // this.otherService = otherService;
    }


    @MyPostConstruct
    private void onInit(){
        System.out.println("Testing MyPostConstruct in " + this.getClass().getName());
    }

    @MyPreDestroy
    public void onDestroy(){

    }

    @MyBean
    public OtherTestClass otherTestClass(){
        return new OtherTestClass();
    }
}
