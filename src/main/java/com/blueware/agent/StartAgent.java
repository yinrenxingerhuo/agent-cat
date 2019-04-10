package com.blueware.agent;

import java.lang.instrument.Instrumentation;

public class StartAgent {
    //代理程序入口函数
    public static void premain(String args, Instrumentation inst) throws InterruptedException {
        //添加字节码转换器
        Thread.sleep(1000);
        inst.addTransformer(new PrintTimeTransformer());
    }
}
