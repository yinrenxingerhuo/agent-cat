package com.cat;

import com.dianping.cat.message.Transaction;

public class TransacationInfo {

    private String type;

    private String name;

    private long time;

    private Transaction t;

    private Context context;

    public Context getContext() {
        return context;
    }

    public TransacationInfo(String type, String name, long time, Transaction t) {
        this.type = type;
        this.name = name;
        this.time = time;
        this.t = t;
    }

    public TransacationInfo(String type, String name, long time, Transaction t, Context context) {
        this.type = type;
        this.name = name;
        this.time = time;
        this.t = t;
        this.context = context;
    }

    public String getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    public long getTime() {
        return time;
    }


    public Transaction getT() {
        return t;
    }

}
