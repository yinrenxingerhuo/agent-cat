package com.cat;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

public class Context implements Cat.Context {

    private Map<String, String> map = new HashMap<String, String>();

    @Override
    public void addProperty(String s, String s1) {
        map.put(s, s1);
    }

    @Override
    public String getProperty(String s) {
        return map.get(s);
    }
}
