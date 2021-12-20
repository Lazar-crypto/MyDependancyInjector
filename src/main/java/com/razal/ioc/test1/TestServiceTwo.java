package com.razal.ioc.test1;

import com.razal.ioc.annotations.*;

@MyService
public class TestServiceTwo {

    private final TestService serviceOne;

    @MyAutowired
    public TestServiceTwo(TestService serviceOne) {
        this.serviceOne = serviceOne;
    }


    @MyPostConstruct
    private void onInit(){

    }

    @MyPreDestroy
    public void onDestroy(){

    }

    @MyBean
    public OtherService otherService(){
        return new OtherService();
    }
}
