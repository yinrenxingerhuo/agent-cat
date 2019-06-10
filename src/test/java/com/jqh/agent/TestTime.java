package com.jqh.agent;


import java.io.IOException;

@ExclusiveTime
public class TestTime {

    public void test() {
        System.out.println(123);
    }

    @MethodCode
    public static void main(String[] args) throws InterruptedException, IOException {
        Thread.sleep(2000);
        new TestTime().test();
    }

}
