package com.blueware.agent;

import com.cat.CatExecute;
import com.cat.TransacationInfo;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

import java.util.HashMap;
import java.util.Map;

public class ProcessUtil {
    private static TransacationInfo t1;
    private static Transaction t2;

    private static Map<String, Integer> map = new HashMap<>();

    public Map<String, Integer> map1 = new HashMap<String, Integer>();

    public Map<String, Integer> getMap1() {
        map1 = map;
        return map1;
    }

    public void setMap1(Map<String, Integer> map1) {
        map1 = map;
        this.map1 = map1;
    }

    private static String mName;

    public ProcessUtil() {
    }

    public static void setEndTime(String key) throws InterruptedException {
        if (map.size() != 0) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Cat.logEvent(entry.getKey(), entry.getValue() + "");
            }
        }
        CatExecute.endMonitor(t1);
        t2.setStatus(Transaction.SUCCESS);
        t2.complete();
        map.clear();
    }

    public static void setStartTime(String className, String methodName, String methodDesc) {
        String name = updateThisName(className);
        t2 = Cat.newTransaction("Service", name);
        ProcessUtil.t1 = CatExecute.beginMonitor(name, methodName);
    }

    private static String updateThisName(String className) {
        String[] split = className.split("/");
        String s = split[split.length - 1];
        return s;
    }

    public static void eventBegin(String className, String methodName, String methodDesc) {
        mName = methodName;
        Integer i = 0;
        if (map.get(methodName) == null) {
            map.put(methodName, i);
        }
    }

    public static void eventEnd(String key) {
        Integer i = map.get(mName);
        i++;
        map.put(mName, i);
    }

}
