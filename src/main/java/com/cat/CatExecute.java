package com.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

import java.util.HashMap;
import java.util.Map;

public class CatExecute {

    private static Map<String, Long> map = new HashMap<>();

    public static TransacationInfo beginMonitor(String type, String name) {
        if (map.get(name) == null) {
            map.put(name, 0L);
        }
        Transaction t = Cat.newTransaction(type, name);
        long time1 = System.nanoTime();
        return new TransacationInfo(type, name, time1, t);
    }

    public static void endMonitor(TransacationInfo info) {
        if (info != null) {
            long time = System.nanoTime();
            long l = (time - info.getTime()) / 1000;
            if(map.get(info.getName()) != null) {
                Long all = map.get(info.getName());
                all = all + l;
                map.put(info.getName(), all);
            }
            Cat.logEvent(info.getType() + "::" + info.getName(), l + "us");
            Cat.logEvent(info.getName() + "AllTime", map.get(info.getName()) + "us");
            info.getT().setStatus(Transaction.SUCCESS);
            info.getT().complete();
        }

    }

    public static TransacationInfo beginDistributed(String type, String name) {

        Context context = new Context();
        Transaction t = Cat.newTransaction(type, name);
        long time1 = System.nanoTime();
        Cat.logRemoteCallClient(context);
        return new TransacationInfo(type, name, time1, t, context);
    }

    public static void endDistributed(TransacationInfo info) {
        if (info != null) {
            long time = System.nanoTime();
            distributed(info, time);
            info.getT().setStatus(Transaction.SUCCESS);
            info.getT().complete();
        }
    }

    protected static void distributed(final TransacationInfo info, final long time) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Transaction t = Cat.newTransaction(info.getType(), info.getName());
                Cat.logEvent(info.getType() + "::" + info.getName(), (time - info.getTime()) / 1000 + "ws");
                Cat.logRemoteCallServer(info.getContext());
                t.setStatus(Transaction.SUCCESS);
                t.complete();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
