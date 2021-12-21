package com.razal.ioc.test1;

import com.razal.ioc.annotations.MyAutowired;
import com.razal.ioc.annotations.MyPostConstruct;
import com.razal.ioc.annotations.MyService;

@MyService
public class TestService {

    private final OtherTestClass otherTestClass;

    @MyAutowired
    public TestService(OtherTestClass otherTestClass) {
        this.otherTestClass = otherTestClass;
    }

    @MyPostConstruct
    private void onInit(){
        System.out.println("Testing MyPostConstruct in " + this.getClass().getName());
    }

}
